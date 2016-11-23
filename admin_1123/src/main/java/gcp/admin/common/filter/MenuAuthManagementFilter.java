package gcp.admin.common.filter;

import java.io.IOException;
import java.util.List;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import gcp.ccs.model.search.CcsAccessmenuSearch;
import gcp.common.util.BoSessionUtil;
import gcp.mms.model.custom.BoLoginInfo;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.dao.DataAccessObject;

public class MenuAuthManagementFilter extends OncePerRequestFilter {


	@Autowired
	@Qualifier("dao")
	private DataAccessObject<T>	queryDao;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		BoLoginInfo loginInfo = (BoLoginInfo) SessionUtil.getLoginInfo(request);

		boolean resultFlag = true;

		if (CommonUtil.isEmpty(loginInfo)) {
			//resultFlag = false;
		} else {
			String roleId = loginInfo.getRoleId();
			CcsAccessmenuSearch ccsAccessmenuSearch = new CcsAccessmenuSearch();
			ccsAccessmenuSearch.setRoleId(roleId);
			UrlPathHelper urlPathHelper = new UrlPathHelper();
			String originalURL = urlPathHelper.getOriginatingServletPath(request);


			String accept = request.getHeader("Accept");

			// 메뉴 권한 체크
			if (StringUtils.indexOf(accept, "html") > -1) {
				ccsAccessmenuSearch.setUrl(originalURL);
				ccsAccessmenuSearch.setSystemTypeCd(loginInfo.getSystemType());
				String menuAuthYn = (String) queryDao.selectOne("ccs.menu.getMenuAuthCheck", ccsAccessmenuSearch);

				if ("N".equals(menuAuthYn)) {
					resultFlag = false;
				}
			}

			// 기능 권한 체크
			List blackUrl = BoSessionUtil.getNotAccessibleUrls(request);

			if (blackUrl != null && blackUrl.indexOf(originalURL) > -1) {
				resultFlag = false;
			}

		}


		if (!resultFlag) {
			throw new AuthenticationException(
					MessageUtil.getMessage("AbstractAccessDecisionManager.accessDenied", request.getLocale()));
		}

		filterChain.doFilter(request, response);

	}

}
