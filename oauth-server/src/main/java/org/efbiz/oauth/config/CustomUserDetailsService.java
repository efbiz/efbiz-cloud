package org.efbiz.oauth.config;

import java.util.ArrayList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User: Joni
 * Email: joni@efbiz.org
 * Date: 2017/3/29
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @SuppressWarnings({
            "rawtypes", "unchecked" })
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ArrayList list = new ArrayList();
        list.add(new SimpleGrantedAuthority("ROLE_hello"));
        User details = new User("efbiz", "N/A", list);
        return details;
    }
}
