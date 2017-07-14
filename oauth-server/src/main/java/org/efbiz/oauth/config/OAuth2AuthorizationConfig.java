package org.efbiz.oauth.config;

import java.security.KeyPair;
import javax.sql.DataSource;
import org.efbiz.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter{
    
    @Autowired
    private DataSource            dataSource;
    @Autowired
    private UserService           userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//      KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");
//      converter.setKeyPair(keyPair);
        converter.setSigningKey("123");
        return converter;
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
         * clients.inMemory()
         * .withClient("cassmall")
         * .secret("secret")
         * .authorizedGrantTypes("authorization_code", "refresh_token",
         * "password").scopes("webapps").autoApprove(true);
         */
        clients.jdbc(dataSource);
        
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
        // refresh_token时需要一个userDetailsService
                .userDetailsService(userService)
                // reuseRefreshTokens不设置成false，refresh_token只能执行一次，
                // 因为在refresh_token时JdbcTokenStore不会保存新的refresh_token
                .reuseRefreshTokens(false).authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource)).tokenStore(new JdbcTokenStore(dataSource)).accessTokenConverter(jwtAccessTokenConverter());
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
}
