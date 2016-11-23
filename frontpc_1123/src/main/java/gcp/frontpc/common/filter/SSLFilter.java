package gcp.frontpc.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;

public class SSLFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(SSLFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

//		logger.debug("SSL FILTER.......");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String sUri = req.getRequestURI();
		String sProtocol = req.getScheme();
		String sDomain = req.getServerName();
		String sPort = Integer.toString(req.getServerPort());
		String sQueryString = CommonUtil.replaceNull(req.getQueryString(), "");
		if (CommonUtil.isNotEmpty(sQueryString)) {
			sQueryString = "?" + sQueryString;
		}

//		logger.debug("sProtocol : " + sProtocol);
//		logger.debug("sDomain : " + sDomain);
//		logger.debug("sUri : " + sUri);
//		logger.debug("sPort : " + sPort);
//		logger.debug("sQueryString : " + sQueryString);
		if (!"LOCAL".equals(Config.getString("server"))) {
			String[] checkUrlPattern = Config.getString("ssl.mandatory.url").split(",");
			String[] exceptUrl = Config.getString("ssl.mandatory.url.except").split(",");

			//ssl url => https
			//ssl 예외 => request uri
			//둘다 아닐때 => http

			// 체크 URL
			boolean sslFlag = false;
			for (String check : checkUrlPattern) {

				if (req.getRequestURI().indexOf(check) >= 0) {
					sslFlag = true;
				}
			}

			//예외 URL
			boolean sslExFlag = false;
			for (String check : exceptUrl) {
				if (req.getRequestURI().indexOf(check) >= 0) {
					sslExFlag = true;
				}
			}

			if (sslFlag && !sslExFlag) {	//ssl이고 예외가 아닐때. (https)
				if (sProtocol.toLowerCase().equals("http")) {
					//				req.getRequestURL().toString().replaceAll("http://", "https://")
					String reUrl = "https://" + sDomain;
					if ("LOCAL".equals(Config.getString("server"))) {
						reUrl += ":8443";
					}
					reUrl += sUri + sQueryString;
					res.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
					res.setHeader("Location", reUrl);
				} else {
					chain.doFilter(request, response);
				}
			} else if (sslExFlag) {	//예외일때. (http or https)
				chain.doFilter(request, response);
			} else {				//둘다 아닐때 (http)
				if (sProtocol.toLowerCase().equals("https")) {
					String reUrl = "http://" + sDomain + sUri + sQueryString;
					res.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
					res.setHeader("Location", reUrl);
				} else {
					chain.doFilter(request, response);
				}
			}
		} else {
			chain.doFilter(request, response);
		}
	}

}
