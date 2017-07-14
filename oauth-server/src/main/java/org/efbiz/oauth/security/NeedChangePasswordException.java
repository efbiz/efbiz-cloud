package org.efbiz.oauth.security;

import org.springframework.security.authentication.CredentialsExpiredException;

public class NeedChangePasswordException extends CredentialsExpiredException {

	private static final long serialVersionUID = -8112102673066792092L;
	
	private String username;

	public NeedChangePasswordException(String username, String msg) {
		super(msg);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	

}
