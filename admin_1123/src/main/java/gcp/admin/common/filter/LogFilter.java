package gcp.admin.common.filter;

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

import gcp.admin.common.wrapper.LoggerRequestWrapper;
import gcp.admin.common.wrapper.LoggerResponseWrapper;
import intune.gsf.common.utils.CommonUtil;

public class LogFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

	private static boolean		filterLogger	= true;

	public static void setFilterLogger(boolean filterLogger) {
		LogFilter.filterLogger = filterLogger;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (filterLogger && isContentJson(request.getContentType())) {
		
			LoggerRequestWrapper requestWrapper = new LoggerRequestWrapper((HttpServletRequest) request);
			LoggerResponseWrapper responseWrapper = new LoggerResponseWrapper((HttpServletResponse) response);

			boolean reqb = isContentJson(requestWrapper.getContentType());

			StringBuffer requestLog = new StringBuffer("\n");
			requestLog.append("\n======================= REST Request =============================");
			requestLog.append("\n* RequestURI	\t:: " + requestWrapper.getRequestURI());
			requestLog.append("\n* REMOTE ADDRESS\t:: " + requestWrapper.getRemoteAddr());
			requestLog.append("\n* HEADERS	\t::  " + CommonUtil.prettyHeader(requestWrapper));
			requestLog.append("\n* REQUEST BODY Size\t:: " + requestWrapper.payload.length() + " bytes");
			requestLog.append("\n* REQUEST BODY	\t:: " + ((reqb) ? CommonUtil.prettyJson(requestWrapper.payload) : ""));
			requestLog.append("\n* HTTP METHOD	\t:: " + requestWrapper.getMethod());
			requestLog.append("\n* ContentType	\t:: " + requestWrapper.getContentType());
			requestLog.append("\n======================= REST Request =============================");
			logger.debug(requestLog.toString());

			chain.doFilter(requestWrapper, responseWrapper);

			boolean resb = isContentJson(responseWrapper.getContentType());

			StringBuffer responseLog = new StringBuffer("\n");
			responseLog.append("\n======================= REST Response ============================");
			responseLog.append("\n* Response BODY Size\t:: " + responseWrapper.getContent().length() + " bytes");
			responseLog.append("\n* Content Type	\t:: " + responseWrapper.getContentType());
			responseLog
					.append("\n* Response BODY	\t:: " + ((resb) ? CommonUtil.prettyJson(responseWrapper.getContent()) : ""));
			responseLog.append("\n======================= REST Response ============================");
			logger.debug(responseLog.toString());
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isContentJson(String contentType) {
		if (contentType != null) {
			return contentType.startsWith("application/json");
		}
		return false;
	}

}
