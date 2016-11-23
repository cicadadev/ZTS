package gcp.frontpc.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gcp.common.util.FoSessionUtil;
import gcp.frontpc.common.contants.Constants;
import intune.gsf.common.constants.BaseConstants;

public class FrontUtil {

	private static final Logger logger = LoggerFactory.getLogger(FrontUtil.class);



	public static String getMobileOs(HttpServletRequest request) {
		String result = "";
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
		if (BaseConstants.DEVICE_TYPE_CD_MW.equals(deviceTypeCd) || BaseConstants.DEVICE_TYPE_CD_APP.equals(deviceTypeCd)) {
			String userAgent = (String) request.getHeader(Constants.SYSTEM_USER_AGENT);

			int j = -1;

			for (int i = 0; i < Constants.SYSTEM_MOBILE_OS.length; i++) {
				j = userAgent.indexOf(Constants.SYSTEM_MOBILE_OS[i]);
				if (j > -1) {
					logger.debug(Constants.SYSTEM_MOBILE_OS[i]);
					result = Constants.SYSTEM_MOBILE_OS[i];
					break;
				}
			}
		}
		return result;
	}

	public static boolean isMypage(HttpServletRequest request) {
		boolean isMypage = false;
		if (request.getRequestURI().contains("/mypage/")) {
			isMypage = true;
		}
		return isMypage;
	}

	public static Map getParameterMap(HttpServletRequest request) {

		Map parameterMap = new HashMap();
		Enumeration enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName = (String) enums.nextElement();
			String[] parameters = request.getParameterValues(paramName);

			// Parameter가 배열일 경우
			if (parameters.length > 1) {
				parameterMap.put(paramName, parameters);
				// Parameter가 배열이 아닌 경우
			} else {
				parameterMap.put(paramName, parameters[0]);
			}
		}

		return parameterMap;
	}


}
