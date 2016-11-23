package gcp.frontpc.controller.view.mms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.service.CodeService;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.StyleService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("memberViewController")
@RequestMapping("mms/member")
public class MemberController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private StyleService	styleService;
	
	@Autowired
	private CodeService		codeService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;

		/**
		 * chnlCd 로그인 채널 코드 3 PC 41 MOBILE WEB 42 MOBILE APP
		 */
//		String deviceType = FrontUtil.getDeviceTypeCd(request);
//		
//		String url = "http://pickpoint.com:9081/co/li/coli01t1.do?chnlCd=41&returnUrl=http://local.0to7.com:7002/&coopcoCd=7020";
//		
//		response.sendRedirect(url);
	}

	/**
	 * 브랜드관 템플릿 스타일
	 * 
	 * @Method Name : getStyleListAjax
	 * @author : stella
	 * @date : 2016. 10. 4.
	 * @description : 
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/style/list/ajax", method = RequestMethod.POST)
	public ModelAndView getStyleListAjax(@RequestBody MmsStyleSearch styleSearch, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("/dms/template/inner/templateA/brandStyle");

		styleSearch.setStoreId(SessionUtil.getStoreId());
		styleSearch.setPageSize(16);
		
		if (FoSessionUtil.isMemberLogin()) {
			styleSearch.setMemberNo(SessionUtil.getMemberNo());
		}
		
		List<MmsStyle> styleList = styleService.getStyleList(styleSearch);
		mv.addObject("styleList", styleList);
		
		mv.addObject("search", styleSearch);
		mv.addObject("totalCount", styleList.size());
		
		return mv;
	}

	/**
	 * 가입/수정 후 callback
	 * 
	 * @Method Name : loginCallback
	 * @author : intune
	 * @date : 2016. 10. 26.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public ModelAndView loginCallback(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/mms/member/callback");

		// 로그인 후 이동할 url
		mv.addObject("url", request.getParameter("url"));

		//주문에서 필요
		return mv;
	}
}
