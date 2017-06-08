package org.efbiz.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * User: Joni
 * Email: joni@efbiz.org
 * Date: 2017/3/29
 */
@RestController
public class AuthUserController {
    @Autowired
    ConsumerTokenServices tokenServices;

    @RequestMapping(value = "/oauthuser")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/revoketoken")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("bearer", "").trim();
            tokenServices.revokeToken(tokenValue);
            return;
        }
    }
}
