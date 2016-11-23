package intune.gsf.common.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Component("loginInterceptor")
public class LoginInterceptor extends HandlerInterceptorAdapter {

//	@Value("#{config['login.page']}")
//	private String loginPage;

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws IOException {
//		SessionUtil.setReturnURL(request, request.getServletPath());

		//System.out.println("========== interceptor:: " + request.getRequestURI());
		
		if (SessionUtil.isMemberLogin()) {
			return true;
		}
		// 로그인이 필요한 url 
		String[] checkUrlPattern = Config.getString("login.mandatory.url").split(",");
		String[] exceptUrl = Config.getString("login.mandatory.url.except").split(",");

		//예외 URL
		for (String check : exceptUrl) {
			if (request.getRequestURI().indexOf(check) >= 0) {
				return true;
			}
		}
		
		// 체크 URL
		for (String check : checkUrlPattern) {

			if (request.getRequestURI().indexOf(check) >= 0) {
				response.sendRedirect("/ccs/common/main?login=Y&returnUrl=" + request.getRequestURI());
				return false;
			}
		}

		// https 로 보낼 url
//		for (String check : checkUrlPattern) {
//			if (request.getRequestURI().indexOf(check) >= 0) {
//				String url = Config.getString("front.domain.ssl.url") + request.getRequestURI();
//
//				System.out.println("========== sendRedirect:: " + url);
//
//				response.sendRedirect(url);
//				return false;
//			}
//		}

		return true;
	}

	/**
	 * view 로 forward 되기전 처리
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 끝난뒤 처리
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
