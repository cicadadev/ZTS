package gcp.frontpc.controller.rest.mms;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsLoginhistory;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.service.LoginService;
import gcp.oms.model.OmsOrder;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;
import jwork.sso.agent.SSOManager;

@RestController
@RequestMapping("api/mms/login")
public class LoginController {

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private LoginService loginService;

	/**
	 * 프론트 로그아웃
	 * 
	 * @Method Name : logout
	 * @author : eddie
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param mmsMemberZts
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout/ajax", method = RequestMethod.GET)
	public void logout(HttpServletRequest request) throws Exception {

		//TODO SSO 로그아웃

		logger.info("##########  logout !!!!!!!!!!!!!!!!!!");

		// ZTS 로그아웃
		SessionUtil.removeLoginInfo(request);
	}

	/**
	 * 로그인 여부체크
	 * 
	 * @Method Name : checklogin
	 * @author : eddie
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param request
	 * @return 1: 일반회원 로그인, 2: 비회원 로그인 , null 로그인 정보 없음
	 * @throws Exception
	 */
	@RequestMapping(value = "/checklogin/ajax", method = RequestMethod.GET)
	public Map<String, String> checklogin(HttpServletRequest request) throws Exception {

		Map<String, String> result = new HashMap<String, String>();

		//logger.debug("####LoginSession :: " + request.getSession().getAttribute(BaseConstants.SESSION_KEY_LOGIN_INFO));

		result.put("loginType", FoSessionUtil.isMemberLogin() ? "1" : FoSessionUtil.isNonMemberLogin() ? "2" : "");
		return result;
	}

	/**
	 * 비회원 주문 조회 로그인
	 * 
	 * @Method Name : doNonMemberLogin
	 * @author : eddie
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nonmember/ajax", method = RequestMethod.POST)
	public String doNonMemberLogin(HttpServletRequest request, @RequestBody OmsOrder order) throws Exception {

		FoLoginInfo loginInfo = loginService.doNonMemberLogin(order);

		// 세션생성
		if (loginInfo != null) {
			FoSessionUtil.setLoginInfo(request, loginInfo);
			return "success";
		} else {
			return "fail";
		}

	}

	@RequestMapping(value = "/sso/ajax", method = RequestMethod.POST)
	public FoLoginInfo ssoLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody String j_sso_q) throws Exception {

		//index 페이지에서 받은 1회용 접속 키
//		String j_sso_q = request.getParameter("j_sso_q");

		logger.info("@@ j_sso_q : " + j_sso_q);

		String responseMessage = SSOManager.getSSOMemberInfoByKey(j_sso_q);

		logger.info("@@ responseMessage : " + responseMessage);

		FoLoginInfo loginInfo = null;

		if (SSOManager.isSuccess(responseMessage)) {

			//통합회원 서비스 번호
			String memberNo = SSOManager.getResponseData(responseMessage);

			logger.info("@@ memberNo : " + memberNo);
			//-----------------------------------------------------------------------------
			// 이부분에서 통합회원 서비스 번호를 가지고 자사 DB에서 기타 회원정보를 
			// 조회하여 로그인 프로세스를 진행합니다.
			// 만일 서비스 이용 약관동의 회원이 아닌 경우는 약관동의 프로세스를 
			// 진행하시기 바랍니다.
			// 자사 DB가 없을시에는 생략
			//-----------------------------------------------------------------------------
			//SSOVO vo  = SSOUtil.getMeberInfoByUserNo(USER_NO);
			//String userId = "ssotest"; // 서비스 번호를 이용 자사 DB에서 회원정보 조회
			//String userId = vo.getUserID();

			// Zero To Seven 세션 생성
			if (CommonUtil.isNotEmpty(memberNo)) {

				loginInfo = loginService.doFrontLogin(memberNo);

				if (loginInfo != null && "MEMBER_STATE_CD.NORMAL".equals(loginInfo.getMemberStateCd())) {

					logger.info("@@ loginId : " + loginInfo.getLoginId());

					// 세션생성
					SessionUtil.setLoginInfo(request, loginInfo);

					// 쿠키생성
					CookieUtil.createCookie(response, "MEMBER_NO", memberNo, CookieUtil.SECONDS_OF_10YEAR);
					
					//로그인 이력
					MmsLoginhistory hist = new MmsLoginhistory();
					hist.setMemberNo(new BigDecimal(memberNo));
					hist.setLoginDt(BaseConstants.SYSDATE);
					hist.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd());
					String ua = request.getHeader("User-Agent");

					if (CommonUtil.isNotEmpty(ua)) {

						logger.info("##### UserAgent::::" + ua);

						//zerotosevenapp//app_version:3.00//os_type:iOS//os_version:10.0.2//device_model:iPhone7,1//
						try {
							String arr[] = ua.split("//zerotosevenapp//");
							if (arr != null && arr.length > 1) {
								String osInfo = arr[1];
								String arr2[] = osInfo.split("//");
								if (arr2 != null && arr2.length > 1) {
									hist.setMobileOsTypeCd(arr2[1].split(":")[1]);
									hist.setAppVersion(arr2[0].split(":")[1]);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 로그인 히스토리
					loginService.insertOneTable(hist);

				}
			}

		}

		return loginInfo;

	}

	@RequestMapping(value = "/temp/ajax", method = RequestMethod.POST)
	public FoLoginInfo templogin(HttpServletRequest request) {
		boolean isSuccess = false;
		FoLoginInfo loginInfo = loginService.doFrontLogin("1000000005");
		if (loginInfo != null) {
			// 세션생성
			SessionUtil.setLoginInfo(request, loginInfo);
			isSuccess = true;
		} else {
			isSuccess = false;
		}
		// 세션생성
		if (isSuccess) {
			return loginInfo;
		} else {
			return loginInfo;
		}
	}

}
