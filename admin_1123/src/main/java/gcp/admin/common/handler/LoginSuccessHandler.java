package gcp.admin.common.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsMenugroup;
import gcp.ccs.model.CcsStore;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsAccessmenuSearch;
import gcp.common.util.BoSessionUtil;
import gcp.mms.model.custom.BoLoginInfo;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.dao.DataAccessObject;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

	@Autowired
	@Qualifier("dao")
	private DataAccessObject<T>		queryDao;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.debug("Login success.............");

//		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//		if (csrf != null) {
//			Cookie cookie = new Cookie("X-XSRF-TOKEN", csrf.getToken());
//			cookie.setPath("/main");
//			response.addCookie(cookie);
//		}

		String message = MessageUtil.getMessage("ccs.user.login.success", request.getLocale());

		if (!createSession(request, authentication)) {
			throw new ServletException("Create Session Error");
		}

		if (!createAuthMenu(request)) {
			throw new ServletException("Create AuthMenu Error");
		}

		String accept = request.getHeader("Accept");

		//server type (BO, PO)
		String systemType = request.getHeader(BaseConstants.SESSION_KEY_SYSTEM_TYPE);
		
		SessionUtil.setSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE, systemType);

		//logger.debug("Request accept : " + accept);

		if (StringUtils.indexOf(accept, "html") > -1) {

			super.onAuthenticationSuccess(request, response, authentication);

		} else if (StringUtils.indexOf(accept, "xml") > -1) {
			response.setContentType("application/xml");
			response.setCharacterEncoding("utf-8");

			String data = StringUtils.join(new String[] { "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<response>",
					"<error>false</error>", "<message>" + message + "</message>", "</response>" });

			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		} else if (StringUtils.indexOf(accept, "json") > -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");

			String data = StringUtils.join(
					new String[] { " { \"response\" : {", " \"error\" : false , ", " \"message\" : \"" + message + "\" ",
							"} } " });

			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		}

	}

	@SuppressWarnings("unused")
	private boolean createSession(HttpServletRequest request, Authentication authentication) throws ServletException {

		SessionUtil.removeLoginInfo(request);
		User user = (User) authentication.getPrincipal();

		BoLoginInfo loginInfo = new BoLoginInfo();

		CcsUser ccsUser = new CcsUser();
		CcsStore ccsStore = new CcsStore();
		CcsBusiness ccsBusiness = new CcsBusiness();

		try {

			String systemType = "";
			String userId = "";
			String businessId = "";
			String userParam = user.getUsername();
			String[] userInfos = null;

			if (!CommonUtil.isEmpty(userParam)) {
				userInfos = userParam.split(",");
				systemType = userInfos[0];
				userId = userInfos[1];
			}

			//user info
			ccsUser.setUserId(userId);
			ccsUser = (CcsUser) queryDao.selectOneTable(ccsUser);
			//store info
			ccsStore.setStoreId(ccsUser.getStoreId());
			ccsStore = (CcsStore) queryDao.selectOneTable(ccsStore);

			loginInfo.setLoginId(ccsUser.getUserId());	//user id
			loginInfo.setLoginName(ccsUser.getName());	//user name
			loginInfo.setRoleId(ccsUser.getRoleId());	//role id
			loginInfo.setStoreId(ccsUser.getStoreId());	//store id
			if (StringUtils.isNotEmpty(ccsUser.getBusinessId())) {
				loginInfo.setBusinessId(ccsUser.getBusinessId()); //businessId
			}

			loginInfo.setMdYn(ccsUser.getMdYn());	//md 여부

			loginInfo.setStoreName(ccsStore.getName());	//store name
			loginInfo.setLangCd(ccsStore.getLangCd());	//lang cd
			loginInfo.setSystemType(systemType);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new ServletException("Login Info create error");
		}
		SessionUtil.setLoginInfo(request, loginInfo);

		return true;
	}

	private boolean createAuthMenu(HttpServletRequest request){
		
		BoLoginInfo loginInfo = (BoLoginInfo) SessionUtil.getLoginInfo(request);
		logger.debug(loginInfo.toString());
		//menu info
		CcsAccessmenuSearch casearch = new CcsAccessmenuSearch();
		casearch.setRoleId(loginInfo.getRoleId());
		casearch.setLangCd(loginInfo.getLangCd());
		casearch.setSystemTypeCd(loginInfo.getSystemType());
		// 권한 기능 조회
		List<CcsMenugroup> authMenuList = (List<CcsMenugroup>) queryDao.selectList("ccs.menu.getMenuGroupUserRoleAuth",
				casearch);
		loginInfo.setMenuList(authMenuList);

		SessionUtil.setLoginInfo(request, loginInfo);
		if ("BO".equals(BoSessionUtil.getSystemType())) {
			// 로그인한 계정권한의 접근 불가 URL 조회하여 세션이 담는다 
			List<String> urls = (List<String>) queryDao.selectList("ccs.menu.getBlackListUrl", casearch);
			BoSessionUtil.setNotAccessibleUrls(request, urls);
		}


		return true;
	}

}
