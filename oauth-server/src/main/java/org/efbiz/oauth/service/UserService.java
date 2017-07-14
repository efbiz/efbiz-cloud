package org.efbiz.oauth.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.efbiz.oauth.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	private static final String DEFAULT_SELECT_USER_STATEMENT = "select user_login_id as username, current_password as `password`, enabled, require_password_change as requirePasswordChange from user_info where user_login_id = ? or cellphone = ?";
	
	private static final String DEFAULT_SELECT_ROLE_STATEMENT = "select distinct role_id from user_role where user_login_id = ?";
	
	private static final String DEFAULT_CHANGE_PASSWORD_STATEMENT = "update user_info set current_password = ?, require_password_change = 'N', last_updated_tx_stamp = sysdate() where user_login_id = ?";
	
	private String selectUserSql = DEFAULT_SELECT_USER_STATEMENT;
	
	private String selectRoleSql = DEFAULT_SELECT_ROLE_STATEMENT;
	
	private String changePasswordSql = DEFAULT_CHANGE_PASSWORD_STATEMENT;
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if (!StringUtils.hasLength(username)) 
			return null;
		
		User user = null;
		
		try {
			List<User> users = jdbcTemplate.query(selectUserSql, 
				new String[]{ username, username }, 
				new BeanPropertyRowMapper<User>(User.class));
			if (null != users && users.size() > 0) {
				user = users.get(0);
				logger.info(String.format("用户输入 '%s' 请求登入，获取到用户 '%s'", username, user.getUsername()));
			}
		} catch(Exception e) {
			String msg = "获取用户报错，username=" + username;
			logger.warn(msg, e);
			throw new UsernameNotFoundException(msg);
		}

		try {
			List<String> roles = jdbcTemplate.queryForList(selectRoleSql, new String[]{ user.getUsername() }, String.class);
			for (String role : roles) {
				user.addAuthoritie(new SimpleGrantedAuthority(role));
			}
		} catch(Exception e) {
			logger.warn("获取用户角色列表报错，username=" + username, e);
		}
		
		return user;
		
	}
	
	public int changePassword(String username, String newPass) {
		User user = null;
		try {
			user = jdbcTemplate.queryForObject(selectUserSql, 
				new String[]{ username }, 
				new BeanPropertyRowMapper<User>(User.class));
		} catch(Exception e) {
			logger.warn("获取用户报错，username=" + username, e);
		}
		
		if (null != user) {
			String dbPassword = user.getPassword();
			String encryptedPassword = passwordEncoder.encode(newPass, dbPassword);
			return jdbcTemplate.update(changePasswordSql, encryptedPassword, username);
		} else {
			return 0;
		}
	}
	
	public boolean validatePassword(UserDetails user, String inputPassword) {
		if (null == inputPassword) return false;
		
		if (null == user || null == user.getUsername() && null == user.getPassword()) 
			return false;
		

		String username = user.getUsername();
		String password = user.getPassword();
		
		String encryptedPassword = passwordEncoder.encode(inputPassword, password);
		if (password.equals(encryptedPassword)) {
			if (logger.isDebugEnabled()) {
				logger.info("用户 '" + username + "' 通过密码成功登入!");
			}
			return true;
		} else {
			String[] secretCodes = inputPassword.split("\\.");
			if (secretCodes.length == 2) {
				String token = secretCodes[0];
				String locToken = md5(password + username + secretCodes[1]);
				if(locToken.equals(token)) {
					if (logger.isDebugEnabled()) {
						logger.info("用户 '" + username + "' 通过授权码(token+timestamp)成功登入!");
					}
					return true;
				}
			}
			return false;
		}
	}
	private String md5(String str){
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				}				
				else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}				
			}
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}
	
}
