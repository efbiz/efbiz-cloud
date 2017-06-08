package org.efbiz.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * User: Joni
 * Email: joni@efbiz.org
 * Date: 2017/3/29
 */
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class OAuth2AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthServerApplication.class, args);
    }
}
