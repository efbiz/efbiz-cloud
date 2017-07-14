package org.efbiz.oauth.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
public class CaptchaConfig {
	
	@Bean  
    public DefaultKaptcha defaultKaptcha(){  
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();  
        Properties properties = new Properties();  
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "175,175,175");
        properties.setProperty("kaptcha.textproducer.font.color", "51,51,51");
        properties.setProperty("kaptcha.image.width", "125");
        properties.setProperty("kaptcha.image.height", "45");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.string", "23456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        properties.setProperty("kaptcha.background.clear.from", "204,204,204");
        properties.setProperty("kaptcha.background.clear.to", "204,204,204");   
        //properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty("kaptcha.noise.color", "blue");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;  
    }
	
}
