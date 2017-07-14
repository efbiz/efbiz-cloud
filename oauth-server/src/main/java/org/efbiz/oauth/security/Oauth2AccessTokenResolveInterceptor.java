package org.efbiz.oauth.security;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

@Component
@ConditionalOnMissingBean(name="authorizationEndpoint",
    type="org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint")
public class Oauth2AccessTokenResolveInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(Oauth2AccessTokenResolveInterceptor.class);
    
    private final String cacheName = "EC_AUTH_USER";
    private final String keyPrefix = "com.cassmall:auth.cache:";
    
    private Cache authCache;
    
    @Value("${icec.security.userInfoEndpointUrl}") 
    private String userInfoEndpointUrl;
    /*@Autowired 
    private CacheManager cacheManager;*/
    @Autowired private RedisConnectionFactory redisFactory;
    @Autowired private ObjectMapper _halObjectMapper;
    
    
    
    private Cache getAuthCache() {
        if (null == authCache) {
            StringRedisTemplate template = new StringRedisTemplate(redisFactory);  
            template.setValueSerializer(new JdkSerializationRedisSerializer());  
            template.afterPropertiesSet();
            
            RedisCacheManager cacheManager = new RedisCacheManager(template);
            //默认24小时失效
            cacheManager.setDefaultExpiration(1000*60*24);
            authCache = cacheManager.getCache(cacheName);
        }
        return authCache;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        
        String authStr = request.getHeader("Authorization");
        /*if (authStr.indexOf(" ") > 0) {
            authStr = authStr.split(" ")[1];
        }*/
        //System.out.println(request.getHeader("Host"));
        //System.out.println(request.getRequestURI() + ", authStr=" + authStr);
        
        if (null != authStr) {
            
            String key = keyPrefix + authStr;
            
            //先尝试从缓存读取用户信息
            Authentication auth = getAuthCache().get(key, Authentication.class);
            
            if (null == auth) { //再尝试从授权中心读取用户信息
                
                try {
                    auth = this.loadAuthentication(authStr);
                } catch (Exception e) {
                    logger.warn("获取登入用户信息时发生错误！", e);
                }
                
                if (null != auth) getAuthCache().put(key, auth);
            }
            
            if (null != auth) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                logger.warn("无法获取到用户的登入信息！");
            }
            
        }
        
        
        return true;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Authentication loadAuthentication(String authStr) throws Exception {
        
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        ClientHttpRequest request = requestFactory.createRequest(new URI(userInfoEndpointUrl), HttpMethod.GET);
        request.getHeaders().add("Authorization", authStr);
        ClientHttpResponse response = request.execute();
        if (response.getStatusCode().series().equals(HttpStatus.Series.SUCCESSFUL)) {
            Map<String, Object> map = _halObjectMapper.readValue(response.getBody(), Map.class);
            
            if (map.containsKey("username")) {
                Set<GrantedAuthority> authorities = Sets.newConcurrentHashSet();
                if (map.containsKey("authorities")) {
                    List<Map<String, String>> roles = (List)map.get("authorities");
                    for (Map<String, String> role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                    }
                }
                UserDetails user = new User(map.get("username").toString(), "", authorities);
                
                return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
            }
            
        }
        
        return null;
    }
}

