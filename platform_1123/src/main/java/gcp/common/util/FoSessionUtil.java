package gcp.common.util;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import gcp.mms.model.custom.FoLoginInfo;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;


public class FoSessionUtil extends SessionUtil {

	/**
	 * 멤버십 회원 여부 조회
	 * 
	 * @Method Name : getMembershipYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getMembershipYn() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getMembershipYn();
		}

		return null;
	}

	/**
	 * B2E회원 여부 조회
	 * 
	 * @Method Name : getB2eYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getB2eYn() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getB2eYn();
		}
		return null;
	}

	/**
	 * 다자녀 회원 여부
	 * 
	 * @Method Name : getChildrenYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getChildrenYn() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getChildrenYn();
		}
		return null;
	}

	/**
	 * 다자녀 DealId 조회
	 * 
	 * @Method Name : getChildrenDealId
	 * @author : intune
	 * @date : 2016. 10. 17.
	 * @description :
	 *
	 * @return
	 */
	public static String getChildrenDealId() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getChildrenDealId();
		}
		return null;
	}

	/**
	 * 프리미엄 회원 여부조회
	 * 
	 * @Method Name : getPremiumYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getPremiumYn() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getPremiumYn();
		}

		return null;
	}


	/**
	 * 임직원 회원 여부 조회
	 * 
	 * @Method Name : getEmployeeYn
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getEmployeeYn() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getEmployeeYn();
		}

		return null;
	}

	/**
	 * 회원 등급 조회
	 * 
	 * @Method Name : getMemGradeCd
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @return
	 */
	public static String getMemGradeCd() {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			return login.getMemGradeCd();
		}
		return null;
	}

	public static String[] gradeArr = { "MEM_GRADE_CD.WELCOME", "MEM_GRADE_CD.FAMILY", "MEM_GRADE_CD.SILVER", "MEM_GRADE_CD.GOLD",
			"MEM_GRADE_CD.VIP" };

	/**
	 * 회원의 다음 등급 조회
	 * 
	 * @Method Name : getMemGradeCd
	 * @author : intune
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @return
	 */
	public static String getNextMemGradeCd(String gradeCd) {
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		String next = gradeArr[1];
		if (login != null) {

			for (int i = 0; i < gradeArr.length; i++) {
				if (gradeArr[i].equals(gradeCd)) {
					if (i == gradeArr.length - 1) {
						next = gradeArr[i];
					} else {
						next = gradeArr[i + 1];
					}

				}
			}
		}
		return next;
	}



	public static int getGradeLevel(String gradeCd) {

		for (int i = 0; i < gradeArr.length; i++) {
			if (gradeArr[i].equals(gradeCd)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 비회원 로그인 여부
	 * 
	 * @Method Name : isNonMemberLogin
	 * @author : intune
	 * @date : 2016. 9. 20.
	 * @description :true 면 회원, false면 비회원(비회원주문조회)
	 *
	 * @return
	 */
	public static boolean isNonMemberLogin() {

		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			if (login.getLoginId() != null && login.getMemberNo() == null) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}
	
	/**
	 * @Method Name : setMembershipYn
	 * @author : ian
	 * @date : 2016. 10. 23.
	 * @description : 멤버십 회원 Y 업데이트 
	 *
	 * @param yn
	 */
	public static void setMembershipYn(String yn) {

		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login != null) {
			login.setMembershipYn(yn);
		}

	}

	/**
	 * 레코벨 전송용 성별코드 조회
	 * 
	 * @Method Name : getRecobellGenderCd
	 * @author : intune
	 * @date : 2016. 10. 24.
	 * @description :
	 *
	 * @return
	 */
	public static String getRecobellGenderCd() {

		//System.out.println("##getRecobellGenderCd()");

		FoLoginInfo login = (FoLoginInfo) getLoginInfo();


		if (login == null) {
			return "";
		}
		String gender = login.getGenderCd();
		String returnCd = "";

		//System.out.println("##gender:" + gender);
		if (CommonUtil.isNotEmpty(gender)) {
			if (gender.indexOf("GENDER_CD.MALE") >= 0) {
				returnCd = "A";
			} else if (gender.indexOf("GENDER_CD.FEMALE") >= 0) {
				returnCd = "B";
			}
		}
		return returnCd;
	}

	/**
	 * 레코벨 전송용 아기성별코드 조회
	 * 
	 * @Method Name : getRecobellBabyGenderCd
	 * @author : intune
	 * @date : 2016. 10. 24.
	 * @description :
	 *
	 * @return
	 */
	public static String getRecobellBabyGenderCd() {
		
		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login == null) {
			return "";
		}

		String gender = login.getBabyGenderCd();
		String returnCd = "";
		if(CommonUtil.isNotEmpty(gender)){
			if ("BABY_GENDER_CD.BOY".equals(gender)) {
				returnCd = "A";
			} else if ("BABY_GENDER_CD.GIRL".equals(gender)) {
				returnCd = "B";
			}
		}

		return returnCd;
	}

	/**
	 * 레코벨 추천 수집용 아기개월코드
	 * 
	 * @Method Name : getRecobellBabyMonthCd
	 * @author : intune
	 * @date : 2016. 10. 24.
	 * @description :
	 *
	 * @return
	 */
	public static String getRecobellBabyMonthCd() {

		FoLoginInfo login = (FoLoginInfo) getLoginInfo();

		if (login == null) {
			return "";
		}

		int babyMonthInt = login.getAgeMonth();


		String babyMonthStr = "";
		if (CommonUtil.isNotEmpty(babyMonthInt)) {
			if (babyMonthInt < 0) {

			} else if (babyMonthInt >= 0 && babyMonthInt <= 3) {
				babyMonthStr = "A";
			} else if (babyMonthInt <= 6) {
				babyMonthStr = "B";
			} else if (babyMonthInt <= 12) {
				babyMonthStr = "C";
			} else if (babyMonthInt <= 24) {
				babyMonthStr = "D";
			} else if (babyMonthInt <= 48) {
				babyMonthStr = "E";
			} else if (babyMonthInt <= 72) {
				babyMonthStr = "F";
			} else if (babyMonthInt > 72) {
				babyMonthStr = "G";
			}
		}

		// TODO 임시 코드 : 삭제바람
		if (CommonUtil.isEmpty(babyMonthStr)) {
			Random r = new Random();
			String[] arr = { "A", "B", "C", "D", "E", "F", "G" };
			babyMonthStr = arr[r.nextInt(7)];
		}
		return babyMonthStr;
	}

	/**
	 * 프리미엄회원, 멤버십 회원의 등급 조회(상품 가격조회용)
	 * 
	 * @Method Name : getMemberGradeForPrice
	 * @author : eddie
	 * @date : 2016. 9. 13.
	 * @description :
	 *
	 * @return null 이면 일반가격조회, null이 아니면 등급별 가격 조회
	 */
	public static String getMemberGradeForPrice() {
		// 등급 : welcome, family, silver, gold, vip, prestage
		// prestage는 premiumYn = 'Y' , membershipYn 은 관계없음
		// welcome, family, silver, gold, vip은 membershipYn = 'Y'인것만 해당

		String memberGrade = "";
		if (isMemberLogin()) {
//			if (BaseConstants.YN_Y.equals(getPremiumYn())) {
//				return memberGrade = BaseConstants.MEMBER_GRADE_PRESTAGE;
//			}
			if (BaseConstants.MEM_GRADE_CD_VIP.equals(getMemGradeCd())) {
				memberGrade = BaseConstants.MEMBER_GRADE_VIP;
			} else if (BaseConstants.MEM_GRADE_CD_GOLD.equals(getMemGradeCd())) {
				memberGrade = BaseConstants.MEMBER_GRADE_GOLD;
			} else if (BaseConstants.MEM_GRADE_CD_SILVER.equals(getMemGradeCd())) {
				memberGrade = BaseConstants.MEMBER_GRADE_SILVER;
			} else if (BaseConstants.MEM_GRADE_CD_FAMILY.equals(getMemGradeCd())) {
				memberGrade = BaseConstants.MEMBER_GRADE_FAMILY;
			} else if (BaseConstants.MEM_GRADE_CD_WELCOME.equals(getMemGradeCd())) {
				memberGrade = BaseConstants.MEMBER_GRADE_WELCOME;
			}
		}

		return memberGrade;
	}
	
	public static boolean isMobile(HttpServletRequest request) {//app포함.
		boolean isMobile = false;
		String userAgent = (String) request.getHeader(BaseConstants.SYSTEM_USER_AGENT);

		int j = -1;

		for (int i = 0; i < BaseConstants.SYSTEM_MOBILE_OS.length; i++) {
			j = userAgent.indexOf(BaseConstants.SYSTEM_MOBILE_OS[i]);
			if (j > -1) {
				isMobile = true;
				break;
			}
		}
		return isMobile;
	}

	public static boolean isMobile() {	//app포함.

		HttpServletRequest request = SessionUtil.getHttpServletRequest();

		return isMobile(request);
	}


	public static boolean isApp(HttpServletRequest request) {
		boolean isApp = false;
		String userAgent = (String) request.getHeader(BaseConstants.SYSTEM_USER_AGENT);

		int j = -1;

		for (int i = 0; i < BaseConstants.SYSTEM_MOBILE_APP.length; i++) {
			j = userAgent.indexOf(BaseConstants.SYSTEM_MOBILE_APP[i]);
			if (j > -1) {
				isApp = true;
				break;
			}
		}
		return isApp;
	}
	
	public static boolean isApp() {	//app포함.
		
		HttpServletRequest request = SessionUtil.getHttpServletRequest();
		
		return isApp(request);
	}
	
	public static String getDeviceTypeCd(HttpServletRequest request) {
		String deviceTypeCd = "";
		if (isApp(request)) {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_APP;
		} else if (isMobile(request)) {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_MW;
		} else {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_PC;
		}


		return deviceTypeCd;
	}

	public static String getDeviceTypeCd() {
		HttpServletRequest request = SessionUtil.getHttpServletRequest();
		String deviceTypeCd = "";
		if (isApp(request)) {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_APP;
		} else if (isMobile(request)) {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_MW;
		} else {
			deviceTypeCd = BaseConstants.DEVICE_TYPE_CD_PC;
		}

		return deviceTypeCd;
	}

}
