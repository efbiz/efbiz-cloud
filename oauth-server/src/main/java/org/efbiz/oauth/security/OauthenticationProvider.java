package org.efbiz.oauth.security;

import org.efbiz.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OauthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService icecUserService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
		//String username = token.getName();
		UserDetails userDetails = icecUserService.loadUserByUsername(token.getName());
		
		if(null == userDetails) {
			throw new UsernameNotFoundException("找不到输入的用户名");
		} else if (!userDetails.isEnabled()){  
            throw new DisabledException("用户已被禁用");  
        } else if (!userDetails.isAccountNonExpired()) {  
            throw new AccountExpiredException("账号已过期");  
        } else if (!userDetails.isAccountNonLocked()) {  
            throw new LockedException("账号已被锁定");  
        } else if (!userDetails.isCredentialsNonExpired()) {  
            throw new NeedChangePasswordException(token.getName(), "凭证已过期");  
        }
		
		if(!icecUserService.validatePassword(userDetails, token.getCredentials().toString())) {
			throw new BadCredentialsException("密码错误");  
        } 
        
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		//返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型  
        return UsernamePasswordAuthenticationToken.class.equals(authentication); 
	}

}
