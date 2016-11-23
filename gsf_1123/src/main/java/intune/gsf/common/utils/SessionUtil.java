package intune.gsf.common.utils;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import intune.gsf.common.constants.BaseConstants;
import intune.gsf.model.BaseLoginInfo;

public class SessionUtil {

	private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

	/**
	 * 로그인 회원 정보 조회
	 * 
	 * @Method Name : getLoginInfo
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	private static Object getSessionLoginInfo(HttpServletRequest request) {
		return getSession(request, BaseConstants.SESSION_KEY_LOGIN_INFO);
	}

	private static String getChannelId(HttpServletRequest request) {
		return (String) getSession(request, BaseConstants.SESSION_KEY_CHANNEL);
	}

	/**
	 * 로그인 회원 정보 세션 추가
	 * 
	 * @Method Name : setLoginInfo
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param obj
	 */
	public static void setLoginInfo(HttpServletRequest request, Object obj) {
		setSession(request, BaseConstants.SESSION_KEY_LOGIN_INFO, obj);
	}

	/**
	 * 로그인 정보 세션 제거
	 * 
	 * @Method Name : removeLoginInfo
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 */
	public static void removeLoginInfo(HttpServletRequest request) {
		removeSession(request, BaseConstants.SESSION_KEY_LOGIN_INFO);
	}

//	/**
//	 * 리턴 URL 조회
//	 * 
//	 * @Method Name : getReturnURL
//	 * @author : intune
//	 * @date : 2016. 9. 20.
//	 * @description :
//	 *
//	 * @param request
//	 * @return
//	 */
	public static Object getReturnURL(HttpServletRequest request) {
		return getSession(request, BaseConstants.SESSION_KEY_RETURN_URL);
	}

	public static void setReturnURL(HttpServletRequest request, Object obj) {
		setSession(request, BaseConstants.SESSION_KEY_RETURN_URL, obj);
	}

//	public static void removeReturnURL(HttpServletRequest request) {
//		removeSession(request, BaseConstants.SESSION_KEY_RETURN_URL);
//	}

	/**
	 * 세션으로부터 언어코드 조회
	 * 
	 * @Method Name : getLangCd
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	public static String getLangCd(HttpServletRequest request) {
		return (String) getSession(request, BaseConstants.SESSION_KEY_LANG_CD_INFO);
	}

	/**
	 * 세션에 언어코드 추가
	 * 
	 * @Method Name : setLangCd
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param obj
	 */
	public static void setLangCd(HttpServletRequest request, Object obj) {
		setSession(request, BaseConstants.SESSION_KEY_LANG_CD_INFO, obj);
	}

	public static void removeLangCd(HttpServletRequest request) {
		removeSession(request, BaseConstants.SESSION_KEY_LANG_CD_INFO);
	}

	/**
	 * key값에 해당하는 세션 제거
	 * 
	 * @Method Name : removeSession
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param key
	 */
	public static void removeSession(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}

	/**
	 * key값에 해당하는 세션 조회
	 * 
	 * @Method Name : getSession
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param key
	 * @return
	 */
	public static Object getSession(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}

	/**
	 * 세션 생성
	 * 
	 * @Method Name : setSession
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param request
	 * @param key
	 * @param data
	 */
	public static void setSession(HttpServletRequest request, String key, Object data) {
		request.getSession().setAttribute(key, data);
	}

	protected static void setNotAccessibleUrls(List<String> urls) {

		HttpServletRequest request = getHttpServletRequest();

		if (request != null) {
			request.getSession().setAttribute(BaseConstants.SESSION_KEY_BLACK_LIST_URL, urls);
		}

	}

	public static HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			return request;
		}
		return null;
	}
	/**
	 * 세션에 있는 로그인 정보 조회
	 * 
	 * @Method Name : getSessionLoginInfo
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description : bo는 BoLoginInfo, fo는 FoLoginInfo 객체 리턴
	 * @return
	 */
	public static Object getLoginInfo() {

		HttpServletRequest request = getHttpServletRequest();

		if (request == null || request.getSession() == null) {
			return null;
		}
		return getSessionLoginInfo(request);
	}

	/**
	 * 외부 제휴 채널 ID조회(제휴채널을 통해 게이트웨이를 통해 들어온경우)
	 * 
	 * @Method Name : getChannelId
	 * @author : eddie
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @return
	 */
	public static String getChannelId() {

		HttpServletRequest request = getHttpServletRequest();

		if (request == null || request.getSession() == null) {
			return null;
		}
		return getChannelId(request);
	}

	public static Object getLoginInfo(HttpServletRequest request) {
//		if (isBoSession(request)) {
//			return (BoLoginInfo) getSessionLoginInfo(request);
//		} else {
//			return (FoLoginInfo) getSessionLoginInfo(request);
//		}
		return getSessionLoginInfo(request);
	}

	/**
	 * BO 세션 인지 여부
	 * 
	 * @Method Name : isBoSession
	 * @author : intune
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	public static boolean isBoSession() {

		HttpServletRequest request = getHttpServletRequest();

		// fo or bo 여부에따라 객체가 다름
		if (CommonUtil.isNotEmpty((String) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE))) {
			return true;//어드민 세션
		}
		return false;//프론트 세션
	}

	/**
	 * storeId 조회
	 * 
	 * @Method Name : getStoreId
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getStoreId() {
		BaseLoginInfo info = (BaseLoginInfo) getLoginInfo();
		if (info != null) {
			return info.getStoreId();
		} else {
			return BaseConstants.STORE_ID;
		}
	}

	/**
	 * 로그인ID 조회
	 * 
	 * @Method Name : getLoginId
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getLoginId() {
		BaseLoginInfo login = (BaseLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getLoginId();
		}
		return null;
	}

	/**
	 * 세션으로부터 언어코드 조회
	 * 
	 * @Method Name : getLangCd
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param
	 * @return
	 */

	public static String getLangCd() {
		BaseLoginInfo login = (BaseLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getLangCd();
		}
		return null;
	}

	/**
	 * 현재 접속 IP 조회
	 * 
	 * @Method Name : getIP
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getIP() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			return request.getRemoteAddr();
		} else {
			return null;
		}
	}

	/**
	 * 회원 로그인 여부- BO, FO(일반회원만 체크함, 비회원은 isNonMemberLogin() 로 판단함. )
	 * 
	 * @Method Name : isLogin
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static boolean isMemberLogin() {


		if (getLoginInfo() == null) {
			return false;
		}

		if (!isBoSession()) {
			BaseLoginInfo info = (BaseLoginInfo)getLoginInfo();
			if (info.getMemberNo() != null) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}



	/**
	 * 로그인 회원번호 조회
	 * 
	 * @Method Name : getMemberNo
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static BigDecimal getMemberNo() {

		BaseLoginInfo login = (BaseLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getMemberNo();
		}
		return null;
	}


}
