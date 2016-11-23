package gcp.frontpc.common.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.MessageUtil;

public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final Logger	logger						= LoggerFactory.getLogger(LoginFailHandler.class);

	public static String	DEFAULT_TARGET_PARAMETER	= "spring-security-redirect-login-failure";
	private String			targetUrlParameter			= DEFAULT_TARGET_PARAMETER;

	public String getTargetUrlParameter() {
         return targetUrlParameter;
    }

    public void setTargetUrlParameter(String targetUrlParameter) {
         this.targetUrlParameter = targetUrlParameter;
    }

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException)
			throws IOException, ServletException {
		String accept = request.getHeader("Accept");
		String ajax = request.getHeader("Ajax");
		logger.debug("================ LOGIN FAIL ===============");
		logger.debug("================ Accept :" + accept);
		logger.debug("================ ajax :" + ajax);

		String error = "true";

		String message = authenticationException.getMessage();

		if (CommonUtil.isEmpty(authenticationException)) {
			message = MessageUtil.getMessage("ccs.user.login.fail", request.getLocale());
		}

         if( StringUtils.indexOf(accept, "html") > -1 ) {

              String redirectUrl = request.getParameter(this.targetUrlParameter);
              if (redirectUrl != null) {
                   super.logger.debug("Found redirect URL: " + redirectUrl);
                   getRedirectStrategy().sendRedirect(request, response, redirectUrl);
              } else {
                   super.onAuthenticationFailure(request, response, authenticationException);
              }

         } else if( StringUtils.indexOf(accept, "xml") > -1 ) {
              response.setContentType("application/xml");
              response.setCharacterEncoding("utf-8");

              String data = StringUtils.join(new String[] {
                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                   "<response>",
                   "<error>" , error , "</error>",
                   "<message>" , message , "</message>",
                   "</response>"
              });

              PrintWriter out = response.getWriter();
              out.print(data);
              out.flush();
              out.close();

		} else if (StringUtils.indexOf(accept, "json") > -1 || "Y".equals(ajax)) {
              response.setContentType("application/json");
              response.setCharacterEncoding("utf-8");

              String data = StringUtils.join(new String[] {
					" { \"response\" : {", " \"error\" : ", error, ", ", " \"message\" : \"", message, "\" ", "} } "
              });

              PrintWriter out = response.getWriter();
              out.print(data);
              out.flush();
              out.close();

         }
    }


}
