package gcp.frontpc.common.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (authentication != null) {
			//session 삭제
			SessionUtil.removeLoginInfo(request);
		}

		String accept = request.getHeader("Accept");
		String ajax = request.getHeader("Ajax");
		String message = MessageUtil.getMessage("ccs.user.logout.success", request.getLocale());

		logger.debug("Request accept : " + accept);

		if (StringUtils.indexOf(accept, "html") > -1) {

			super.onLogoutSuccess(request, response, authentication);

		} else if (StringUtils.indexOf(accept, "xml") > -1) {
			response.setContentType("application/xml");
			response.setCharacterEncoding("utf-8");

			String data = StringUtils.join(new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<response>",
					"<error>false</error>", "<message>" + message + "</message>", "</response>" });

			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		} else if (StringUtils.indexOf(accept, "json") > -1 || "Y".equals(ajax)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");

			String data = StringUtils.join(
					new String[] { " { \"response\" : {", " \"error\" : false , ", " \"message\" : \"" + message + "\" ",
							"} } " });

			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		}

	}
}
