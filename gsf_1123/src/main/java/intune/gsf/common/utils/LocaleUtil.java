package intune.gsf.common.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

public class LocaleUtil {

	public static Locale getCurrentLocale() {

		try {
			HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpRequest);
			return localeResolver.resolveLocale(httpRequest);

		} catch (Exception e) {			
			return Locale.getDefault();

		}

	}

	public static String getCurrentLanguage() {
		return LocaleUtil.getCurrentLocale().getLanguage();
	}

}
