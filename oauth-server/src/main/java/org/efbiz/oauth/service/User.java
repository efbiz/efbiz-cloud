package org.efbiz.oauth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Setter;

public class User implements UserDetails {

	private static final long serialVersionUID = 5991564843796134547L;
	
	@Setter private String username;
	@Setter private String password;
	@Setter private Boolean enabled;
	@Setter private Boolean requirePasswordChange;
    private List<SimpleGrantedAuthority> authorities;

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void addAuthoritie(SimpleGrantedAuthority role) {
		if (null == authorities) authorities = new ArrayList<>();
		authorities.add(role);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * 账号没有过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 账号没有锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 密码没有过期
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		//return !requirePasswordChange;
		return true; //暂时不启用首次登入修改密码
	}

	/**
	 * 是有效的
	 */
	@Override
	public boolean isEnabled() {
		return null==enabled ? true : enabled;
	}

}
