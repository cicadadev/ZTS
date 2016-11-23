package gcp.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gcp.mms.model.custom.BoLoginInfo;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.SessionUtil;

public class BoSessionUtil extends SessionUtil {

	/**
	 * 세션으로부터 시스템 타입 조회 ( PO, BO )
	 * 
	 * @Method Name : getSystemType
	 * @author : intune
	 * @date : 2016. 8. 10.
	 * @description :
	 *
	 * @return
	 */
	public static String getSystemType() {
		HttpServletRequest request = getHttpServletRequest();
		if (request != null) {
			return (String) SessionUtil.getSession(getHttpServletRequest(), BaseConstants.SESSION_KEY_SYSTEM_TYPE);
		}
		return null;
	}

	/**
	 * MD여부 조회
	 * 
	 * @Method Name : getMdYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getMdYn() {
		BoLoginInfo login = (BoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getMdYn();
		}
		return null;
	}

	/**
	 * PO 로그인시 업체ID조회
	 * 
	 * @Method Name : getBusinessId
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getBusinessId() {
		BoLoginInfo login = (BoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getBusinessId();
		}
		return null;
	}
	
	public static String getRoleId() {
		BoLoginInfo login = (BoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getRoleId();
		}
		return null;
	}

	/**
	 * 권한없는rest url 목록(어드민 권한 체크용)
	 * 
	 * @Method Name : getNotAccessibleUrls
	 * @author : eddie
	 * @date : 2016. 7. 14.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	public static List getNotAccessibleUrls(HttpServletRequest request) {
		return (List) request.getSession().getAttribute(BaseConstants.SESSION_KEY_BLACK_LIST_URL);
	}

	/**
	 * 
	 * @Method Name : setNotAccessibleUrls
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param urls
	 */
	public static void setNotAccessibleUrls(HttpServletRequest request, List<String> urls) {
		request.getSession().setAttribute(BaseConstants.SESSION_KEY_BLACK_LIST_URL, urls);
	}

}
