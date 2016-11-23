/**
 * 
 */
package gcp.common.util;

import java.util.List;

import intune.gsf.model.Language;

/**
 * 
 * @Pagckage Name : gcp.common.util
 * @FileName : BaseDataUtil.java
 * @author : Administrator
 * @date : 2016. 4. 21.
 * @description :
 */
public class BaseDataUtil {

//	private String getRequestUrl() {
//		// this is a wicket-specific request interface
//		final Request request = getRequest();
//		if (request instanceof WebRequest) {
//			final WebRequest wr = (WebRequest) request;
//			// but this is the real thing
//			final HttpServletRequest hsr = wr.getHttpServletRequest();
//			String reqUrl = hsr.getRequestURL().toString();
//			final String queryString = hsr.getQueryString();
//			if (queryString != null) {
//				reqUrl += "?" + queryString;
//			}
//			return reqUrl;
//		}
//		return null;
//	}

	static private List<Language>	languageList;

	public static List<Language> getLanguageList() {
		return languageList;
	}

	public static void setLanguageList(List<Language> languageList) {
		BaseDataUtil.languageList = languageList;
	}
}
