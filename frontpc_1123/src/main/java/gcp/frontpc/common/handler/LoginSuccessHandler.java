package gcp.frontpc.common.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsQuickmenu;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsOrder;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.dao.DataAccessObject;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

	@Autowired
	private DataAccessObject<T>	dao;

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
		
		String redirectURI = request.getHeader("redirectURI");

		String accept = request.getHeader("Accept");
		String ajax = request.getHeader("Ajax");

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

		} else if (StringUtils.indexOf(accept, "json") > -1 || "Y".equals(ajax)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");

			String data = StringUtils.join(
					new String[] { " { \"response\" : {", " \"error\" : false , ", " \"message\" : \"" + message + "\" , ",
							" \"redirectUrl\" : \"" + redirectURI + "\"} } " });

			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		}

	}

	@SuppressWarnings("unchecked")
	private boolean createSession(HttpServletRequest request, Authentication authentication) throws ServletException {

		SessionUtil.removeLoginInfo(request);
		User user = (User) authentication.getPrincipal();

		String loginId = user.getUsername();

		FoLoginInfo loginInfo = new FoLoginInfo();

		String type = loginId.substring(0, loginId.indexOf("_"));
		String id = loginId.substring(loginId.indexOf("_") + 1);


		try {
			if ("M".equals(type)) {
				MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
				mmsMemberSearch.setMemberId(id);

				//member info
				MmsMember mmsMember = (MmsMember) dao.selectOne("mms.member.getMemberLogin", mmsMemberSearch);

				mmsMemberSearch.setMemberNo(mmsMember.getMemberNo());
				// member Quick Memu
				List<MmsQuickmenu> memberMenus = new ArrayList<MmsQuickmenu>();
				memberMenus = (List<MmsQuickmenu>) dao.selectList("mms.member.getMemberQuickmenuList",
						mmsMemberSearch);

				loginInfo.setStoreId(mmsMember.getMmsMemberZts().getStoreId());
				loginInfo.setLoginId(mmsMember.getMemberId());	//member id
				loginInfo.setLoginName(mmsMember.getMemberName());	//member name
				loginInfo.setMemberNo(mmsMember.getMemberNo());	//member number
//				loginInfo.setMemberYn(Constants.YN_Y);
				if (memberMenus != null) {
					loginInfo.setMemberMenus(memberMenus);
				}
				loginInfo.setMembershipYn(mmsMember.getMmsMemberZts().getMembershipYn());
				loginInfo.setB2eYn(mmsMember.getMmsMemberZts().getB2eYn());
				loginInfo.setChildrenYn(mmsMember.getMmsMemberZts().getChildrenYn());
				loginInfo.setPremiumYn(mmsMember.getPremiumYn());
				loginInfo.setMemGradeCd(mmsMember.getMmsMemberZts().getMemGradeCd());
				//임직원여부
				loginInfo.setEmployeeYn(mmsMember.getEmployeeYn());
				loginInfo.setPhone2(mmsMember.getPhone2());

			} else {

				// 비회원 로그인
				OmsOrder omsOrder = new OmsOrder();
				omsOrder.setOrderId(id);
				omsOrder = (OmsOrder) dao.selectOne("oms.order.getOrderLogin", omsOrder);

				loginInfo.setLoginId(omsOrder.getOrderId());
				loginInfo.setLoginName(omsOrder.getName1());
//				loginInfo.setMemberYn(Constants.YN_N);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServletException("Login Info create error");
		}
		SessionUtil.setLoginInfo(request, loginInfo);

		return true;
	}

}
