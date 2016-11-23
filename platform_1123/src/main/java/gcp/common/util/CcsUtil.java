package gcp.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gcp.mms.model.custom.FoLoginInfo;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

public class CcsUtil {

	/**
	 * 문의에 대한 경과시간 구하기
	 * 
	 * @Method Name : getPassTime
	 * @author : intune
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param inquiryDt
	 * @param answerDt
	 * @return
	 */
	public static String getPassTime(String inquiryDt, String answerDt) {

		Long time = 0L;
		Long day = 0L;
		Long passTime = 0L;
		Long differenceTime = 0L;

		if (CommonUtil.isEmpty(inquiryDt)) {
			return "";
		}
		try {

			Date inquiryDate = DateUtil.convertStringToDate(inquiryDt, DateUtil.FORMAT_1);

			if (CommonUtil.isNotEmpty(answerDt)) {
				//답변 등록일로부터 현재까지 경과시간
				Date answerDate = DateUtil.convertStringToDate(answerDt, DateUtil.FORMAT_1);
				differenceTime = answerDate.getTime() - inquiryDate.getTime();

			} else {
				//문의등록일부터 현재까지 시간경과
				Date toDay = new Date();
				differenceTime = toDay.getTime() - inquiryDate.getTime();

			}
			passTime = differenceTime / (60 * 60 * 1000); //시간
			time = passTime % 24;
			day = passTime / 24;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		String totalTimeStr = "";
		if (day == 0L && time == 0L) {
			totalTimeStr = "1시간 미만";
		} else {
			totalTimeStr = (day == 0L ? "" : (String.valueOf(day) + "일 ")) + (time == 0L ? "" : (String.valueOf(time) + "시간"));
		}
		return totalTimeStr;

	}

	/**
	 * 비회원 정보 조회
	 * 
	 * @Method Name : getNonMemberInfo
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : 비회원 SESSION 정보
	 *
	 * @return
	 */
	public static FoLoginInfo getNonMemberInfo(HttpServletRequest request) {
		FoLoginInfo loginInfo = new FoLoginInfo();

		String loginId = "";
		String langCd = BaseConstants.DEFAULT_LANG_CD;
//		BigDecimal memberNo = BaseConstants.NON_MEMBER_NO;

		String value = CookieUtil.getCookieValue(request, BaseConstants.COOKIE_NON_MEMBER_INFO);
		if (CommonUtil.isNotEmpty(value)) {
			String[] val = value.split(":");
			loginId = val[0];
			loginInfo.setStoreId(BaseConstants.STORE_ID);	//비회원 STORE ID
			loginInfo.setLoginId(loginId);
			loginInfo.setLangCd(langCd);
//			loginInfo.setMemberNo(memberNo);
//			loginInfo.setMemberYn(BaseConstants.YN_N);
		}
		return loginInfo;
	}

	/**
	 * 
	 * @Method Name : getNonMemberInfo
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : 비회원정보 (없으면 생성)
	 *
	 * @return
	 */
	public static FoLoginInfo getNonMemberInfo() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		FoLoginInfo loginInfo = new FoLoginInfo();

		String loginId = "";
		String langCd = BaseConstants.DEFAULT_LANG_CD;
//		BigDecimal memberNo = BaseConstants.NON_MEMBER_NO;

		boolean ex = false;
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();

			String value = CookieUtil.getCookieValue(request, BaseConstants.COOKIE_NON_MEMBER_INFO);

			if (CommonUtil.isNotEmpty(value)) {
				String[] val = value.split(":");
				loginId = val[0];
				ex = true;
			}
			if (!ex) {
				HttpServletResponse response = attributes.getResponse();
				Random random = new Random();
				String randomSeq = CommonUtil.leftPad(random.nextInt(9999) + "", 4, "0");
				String randomLoginId = DateUtil.getCurrentDate(DateUtil.FORMAT_10) + randomSeq;
				CookieUtil.createCookie(response, BaseConstants.COOKIE_NON_MEMBER_INFO, randomLoginId, -1);

			}

			loginInfo.setStoreId(BaseConstants.STORE_ID);	//비회원 STORE ID
			loginInfo.setLoginId(loginId);
			loginInfo.setLangCd(langCd);
//			loginInfo.setMemberNo(memberNo);
//			loginInfo.setMemberYn(BaseConstants.YN_N);

		} else {
			return null;
		}
		return loginInfo;
	}

	/**
	 * 
	 * @Method Name : setSessionLoginInfo
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : OBJECT에 SESSION 정보 SET
	 *
	 * @param obj
	 */
	public static void setSessionLoginInfo(Object obj) {
		setSessionLoginInfo(obj, false);
	}

	/**
	 * 
	 * @Method Name : setSessionLoginInfo
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : OBJECT에 SESSION 정보 SET
	 *
	 * @param obj
	 * @param approvalNonmember (비회원일때 SET 유무)
	 */
	public static Object setSessionLoginInfo(Object obj, boolean approvalNonmember) {

		if (obj != null) {
			Class cls = obj.getClass();
			try {
				setCustomField(cls, obj, approvalNonmember);
				while (cls.getSuperclass() != null) {
					cls = cls.getSuperclass();
					setCustomField(cls, obj, approvalNonmember);
				}
			} catch (Exception e) {
				//logger.error(e.getMessage());
				e.printStackTrace();
			}
			return obj;
		} else {
			return null;
		}

	}

	public static void setCustomField(Class cls, Object obj, boolean approvalNonmember) throws Exception {
		String storeId = null;
		String langCd = null;
		String loginId = null;
		BigDecimal memberNo = null;

		if (SessionUtil.isMemberLogin()) {
			storeId = SessionUtil.getStoreId();
			langCd = SessionUtil.getLangCd();
			loginId = SessionUtil.getLoginId();
			memberNo = SessionUtil.getMemberNo();
		} else {
			if (approvalNonmember) {
				FoLoginInfo nonMemberInfo = CcsUtil.getNonMemberInfo();
				storeId = nonMemberInfo.getStoreId();
				langCd = nonMemberInfo.getLangCd();
				loginId = nonMemberInfo.getLoginId();
				memberNo = nonMemberInfo.getMemberNo();
			}
		}

		Field[] fields = cls.getDeclaredFields();
//		logger.debug("class name : " + cls.getName());
		for (Field field : fields) {
			field.setAccessible(true);

//			logger.debug("field : " + field.getName());

			if (!CommonUtil.isEmpty(field.get(obj))) {
				continue;
			}

			try {
				if (BaseConstants.PARAM_STORE_ID.equals(field.getName())) {
					field.set(obj, storeId);
				} else if (BaseConstants.PARAM_LANG_CD.equals(field.getName())) {
					field.set(obj, langCd);
				} else if (BaseConstants.PARAM_MEMBER_NO.equals(field.getName())) {
					field.set(obj, memberNo);
				} else if (BaseConstants.PARAM_USER_ID.equals(field.getName())) {
					field.set(obj, loginId);
				} else if (BaseConstants.PARAM_USERID.equals(field.getName())) {
					field.set(obj, loginId);
				} else if (BaseConstants.PARAM_CART_ID.equals(field.getName())) {
					field.set(obj, loginId);
				} else if ("LGD_BUYERID".equals(field.getName())) {
					field.set(obj, loginId);
				} else if ("memberId".equals(field.getName())) {
					field.set(obj, loginId);
				}
			} catch (UsernameNotFoundException u) {
				//logger.error(u.getMessage(), u);
			}
		}
	}

}
