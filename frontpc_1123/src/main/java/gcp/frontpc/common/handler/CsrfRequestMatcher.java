package gcp.frontpc.common.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

import intune.gsf.common.utils.Config;

public class CsrfRequestMatcher implements RequestMatcher {

	private static final Logger logger = LoggerFactory.getLogger(CsrfRequestMatcher.class);

	@Override
	public boolean matches(HttpServletRequest request) {
		boolean result = request.isSecure();
		String url = request.getRequestURI();
		String[] unsecurityUrls = (Config.getString("unsecurity.url")).split(",");

//		logger.debug("request uri : " + url);

		for (String unsecurityUrl : unsecurityUrls) {
//			logger.debug("unsecurity url : " + unsecurityUrl);
			if (url.equals(unsecurityUrl)) {
				result = false;
			}
		}

		return result;
	}

}
