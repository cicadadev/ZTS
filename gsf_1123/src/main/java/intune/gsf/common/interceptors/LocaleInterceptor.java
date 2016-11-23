package intune.gsf.common.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import intune.gsf.common.constants.BaseConstants;

public class LocaleInterceptor extends HandlerInterceptorAdapter {
	
	private String paramName = BaseConstants.LOCALE_PARAM;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) {
		String locale = request.getParameter(paramName);

		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

		//default
		//localeResolver.setLocale(request, response, Locale.KOREAN);

		if (locale != null) {
			//System.out.println("@@StringUtils.parseLocaleString(locale):: "+StringUtils.parseLocaleString(locale));
			localeResolver.setLocale(request, response, StringUtils.parseLocaleString(locale));
		}
		//System.out.println("@@localeResolver.resolveLocale(request);:: " + localeResolver.resolveLocale(request));
		localeResolver.resolveLocale(request);
		return true;
	}
}
