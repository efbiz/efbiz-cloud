package org.efbiz.oauth.config;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.efbiz.oauth.security.LoginAndChangePasswordFilter;
import org.efbiz.oauth.security.NeedChangePasswordException;
import org.efbiz.oauth.security.OauthenticationProvider;
import org.efbiz.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Order(-20)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    
    // @Autowired private LoginAndChangePasswordFilter changePasswordFilter;
    
    @Autowired(required = false)
    @Qualifier("accessTokenProviderChain")
    private AccessTokenProviderChain accessTokenProviderChain;
    
    @Autowired
    private UserService              icecUserService;
     
    @Autowired
    private OauthenticationProvider  authenticationProvider;
    
    @Bean
    protected OAuth2RestTemplate OAuth2RestTemplate(OAuth2ProtectedResourceDetails resource,OAuth2ClientContext context) {
        
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, context);
        
        if (null != accessTokenProviderChain) {
            restTemplate.setAccessTokenProvider(accessTokenProviderChain);
        }
        return restTemplate;
        
    }
    
    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
    
    /*
     * @Bean
     * public CookieSerializer cookieSerializer() {
     * DefaultCookieSerializer serializer = new DefaultCookieSerializer();
     * //serializer.setDomainName("local.com");
     * serializer.setCookiePath("/oauth2");
     * return serializer;
     * }
     */
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.addFilterBefore(new LoginAndChangePasswordFilter(icecUserService, "false","http://localhost"), UsernamePasswordAuthenticationFilter.class).formLogin().loginPage("/login").failureUrl("/login?error=e1").failureHandler(new OAuthenticationFailureHandler()).and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessHandler(getLogoutSuccessHandler()).and().requestMatchers()
                .antMatchers("/", "/login", "/changePassword", "/logout", "/captcha-image", "/oauth/authorize", "/oauth/confirm_access", "/actuator/**", "/register", "/getgeoinfo", "/registeruser", "/getmobileverificationcode", "/validatemobileverificationcode", "/validatedcellphone", "/findpwd/**").and().authorizeRequests()
                .antMatchers("/", "/login", "/changePassword", "/captcha-image", "/register", "/getgeoinfo", "/registeruser", "/getmobileverificationcode", "/validatemobileverificationcode", "/validatedcellphone", "/findpwd/**").permitAll().anyRequest().authenticated().and().csrf().disable();
        // .csrfTokenRepository(new CookieCsrfTokenRepository());
        // @formatter:on
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
    
    private LogoutSuccessHandler getLogoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler urlLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        urlLogoutHandler.setDefaultTargetUrl("/login?logout");
        urlLogoutHandler.setTargetUrlParameter("service");
        // urlLogoutHandler.setUseReferer(true);
        return urlLogoutHandler;
    }
    
    class OAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler{
        @Override
        public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response,AuthenticationException exception) throws IOException,ServletException {
            if (exception instanceof NeedChangePasswordException) {
                String username = ((NeedChangePasswordException) exception).getUsername();
                super.setDefaultFailureUrl("/changePassword?username=" + username);
                request.getSession().setAttribute(LoginAndChangePasswordFilter.USERNAME_TO_CHANGE_ATTRIBUTE, username);
            } else {
                super.setDefaultFailureUrl("/login?error=e1");
            }
            super.onAuthenticationFailure(request, response, exception);
        }
    }
    
}
