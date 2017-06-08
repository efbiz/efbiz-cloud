package org.efbiz.oauth.config;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * User: Joni
 * Email: joni@efbiz.org
 * Date: 2017/3/29
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    CustomUserDetailsService userDetailsService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (Strings.isNullOrEmpty(username)) {
            throw new BadCredentialsException("Username not found.");
        }
        if (Strings.isNullOrEmpty(password)) {
            throw new BadCredentialsException("Wrong password.");
        }
        UserDetails userDetails = (UserDetails)this.userDetailsService.loadUserByUsername(authentication.getName());
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),userDetails.getAuthorities());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
