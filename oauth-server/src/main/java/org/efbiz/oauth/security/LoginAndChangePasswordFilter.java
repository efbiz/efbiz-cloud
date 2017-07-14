package org.efbiz.oauth.security;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.efbiz.oauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class LoginAndChangePasswordFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(LoginAndChangePasswordFilter.class);

	private final static String loginUrl = "/login";
	private final static String failureUrl = "/changePassword?error";
	private final static String changePasswordUrl = "/changePassword";

	private final boolean captchaOn;
	private final String webRootUrl;
	private final UserService userService;
	
	private DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private RequestMatcher requestMatcher = new OrRequestMatcher(
			new AntPathRequestMatcher(loginUrl, "GET"),
			new AntPathRequestMatcher(loginUrl, "POST"),
			new AntPathRequestMatcher(changePasswordUrl, "GET"),
			new AntPathRequestMatcher(changePasswordUrl, "POST"));

	public final static String USERNAME_TO_CHANGE_ATTRIBUTE = "USERNAME_TO_CHANGE";
	
	public LoginAndChangePasswordFilter(UserService icecUserService, String captchaOn ,String webRoot ) {
		this.userService = icecUserService;
		this.captchaOn = Boolean.parseBoolean(captchaOn);
		this.webRootUrl = webRoot;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if (requestMatcher.matches(request)) {
			
			HttpSession session = request.getSession();

			if (new AntPathRequestMatcher(loginUrl, "GET").matches(request)) {
				/**
				 * 请求登入时先清掉当前session中的标记
				 */
				session.removeAttribute(USERNAME_TO_CHANGE_ATTRIBUTE);
				
				if (isLogin()) {
					redirectStrategy.sendRedirect(request, response, webRootUrl + "/logout");
					return;
				}
			} else if (new AntPathRequestMatcher(loginUrl, "POST").matches(request)) {
				if (captchaOn) {
					String input_captcha = request.getParameter("captcha");
					Object saved_captcha = request.getSession().getAttribute("captchap-text");
					if (null == input_captcha || !input_captcha.equals(saved_captcha)) {
						logger.warn("验证码错误，输入的：{}，应该是：{}", input_captcha, saved_captcha);
						redirectStrategy.sendRedirect(request, response, loginUrl + "?error=e2");
						return;
					}
				}
			} else if (new AntPathRequestMatcher(changePasswordUrl, "GET").matches(request)) {
				/**
				 * 这里双重校验：
				 * 1、有Referer，保证不能直接打开
				 * 2、session中保存有特定标记，这个值只有在登入时抛出NeedChangePasswordException才会获得
				 */
				if (null == session.getAttribute(USERNAME_TO_CHANGE_ATTRIBUTE) 
						|| null == request.getHeader("Referer")) {
					redirectStrategy.sendRedirect(request, response, loginUrl);
					return;
				}
				
			} else {
				String username = session.getAttribute(USERNAME_TO_CHANGE_ATTRIBUTE).toString();
				String password = request.getParameter("password");
				String newPass = request.getParameter("password1");
				String enterPass = request.getParameter("password2");
				
				int result = 0;
				UserDetails user = this.userService.loadUserByUsername(username);
				
				if (null != user && this.userService.validatePassword(user, password)) {
					//throw new UsernameNotFoundException("找不到输入的用户名");
					if (null != newPass && newPass.equals(enterPass)) {
						result = this.userService.changePassword(username, newPass);
					}
				}
				
				/*if (!icecUserService.validatePassword(user, password)) {
					throw new BadCredentialsException("密码错误"); 
				}
				
				if (null == newPass || !newPass.equals(enterPass)) {
					throw new RuntimeException("两次输入的密码不一致"); 
				}*/
				
				if (result == 1) {
					redirectStrategy.sendRedirect(request, response, loginUrl);
				} else {
					redirectStrategy.sendRedirect(request, response, failureUrl);
				}

				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isLogin() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (null == auth) return false;
		
		if (AnonymousAuthenticationToken.class.isAssignableFrom(auth.getClass())) 
			return false;
				
		return auth.isAuthenticated();
		
	}
}
