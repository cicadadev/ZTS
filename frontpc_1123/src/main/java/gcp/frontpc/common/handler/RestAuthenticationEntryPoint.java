package gcp.frontpc.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class RestAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	protected static final Log logger = LogFactory.getLog(RestAuthenticationEntryPoint.class);

	public RestAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
		// TODO Auto-generated constructor stub
	}

	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		logger.debug("====================== custom accesss denid ========================= ");
		String ajax = request.getHeader("Ajax");
		logger.debug("====================== ajax yn : " + ajax);

		if ("Y".equals(ajax)) {
			response.sendError(999, "Access Denied");
		} else {
			super.commence(request, response, authException);
		}
	}
}
