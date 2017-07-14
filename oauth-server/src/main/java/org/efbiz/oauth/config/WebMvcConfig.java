package org.efbiz.oauth.config;

import java.util.ArrayList;
import java.util.List;
import org.efbiz.oauth.security.Oauth2AccessTokenResolveInterceptor;
import org.efbiz.oauth.utils.TimestampFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Value("${icec.resources.captchaOn:false}") 
	private boolean captchaOn;
	
	@Bean
	public static CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("AUTHSESSIONID");
		return serializer;
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		if (captchaOn) 
			registry.addViewController("/login").setViewName("loginWithCaptcha");
		else
			registry.addViewController("/login").setViewName("login");
	}
	
	@Autowired(required=false)
    private Oauth2AccessTokenResolveInterceptor oauth2AccessTokenResolveInterceptor;
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer c) {
        c.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (null != oauth2AccessTokenResolveInterceptor) {
            registry
                .addInterceptor(oauth2AccessTokenResolveInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/js/**", 
                        "/app/**", 
                        "/css/**", 
                        "/images/**", 
                        "/static/**", 
                        "/webjars/**");
        }
    }
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter)converter).setSupportedMediaTypes(MediaType.parseMediaTypes(
                        "application/json;charset=utf-8,text/plain;charset=utf-8"));
            }
        }
    }
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new TimestampFormatter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper _halObjectMapper) {
        
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.addAll(MediaType.parseMediaTypes(
                "text/plain; charset=utf-8,"
                + "plain/text; charset=utf-8,"
                + "application/json; charset=utf-8,"
                + "application/hal+json; charset=UTF-8"));
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        jsonConverter.setObjectMapper(_halObjectMapper);
        return jsonConverter;
    }
	
}
