package org.efbiz.oauth.web;

import java.awt.image.BufferedImage;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.efbiz.oauth.service.User;
import org.efbiz.oauth.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@Controller
public class LoginController {
	
	@Value("${webRoot}") 
	private String webRoot;
	
	@Value("${commonStatic}") 
	private String commonStatic;
	
	@Autowired private DefaultKaptcha defaultKaptcha;
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setRequestContextAttribute("request");
        Map<String, String> attrMap = new HashMap<>();
        attrMap.put("webRoot", webRoot);
        attrMap.put("commonStatic", commonStatic);
        resolver.setAttributesMap(attrMap);
        resolver.setRedirectHttp10Compatible(false);
        return resolver;
    }
	
	/**
	 * 获取当前认证信息
	 * @param user
	 * @return
	 */
	@RequestMapping("/userinfo")
	@ResponseBody
	public UserDetails getUserinfo(Principal user) {
		if (OAuth2Authentication.class.equals(user.getClass()))
			return (User)((OAuth2Authentication)user).getPrincipal();
		else 
			return null;
	}
	
	@RequestMapping(value = "/captcha-image")  //login
    public ModelAndView getKaptchaImage(HttpServletRequest request,  
            HttpServletResponse response) throws Exception {

		String attributeName = "captchap-text";
		
		if(StringUtil.isNotEmpty(request.getParameter("attr")))
			attributeName = request.getParameter("attr");
				
       return  generateKaptchaImage(request,response, attributeName);
    } 

    private ModelAndView generateKaptchaImage(HttpServletRequest request,
        HttpServletResponse response, String attributeName) throws Exception{

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = defaultKaptcha.createText();
		logger.info(request.getRemoteAddr() + ":" + attributeName + ": " + capText);

        request.getSession().setAttribute(attributeName, capText);

        BufferedImage bi = defaultKaptcha.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }
}
