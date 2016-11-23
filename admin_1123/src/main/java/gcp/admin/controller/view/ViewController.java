package gcp.admin.controller.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gcp.mms.model.MmsReadhistory;
import gcp.mms.service.HistoryService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller
@RequestMapping({ "/sps", "/oms", "/ccs", "/mms", "/pms", "/dms", "/main", "/sample", "/common" })
public class ViewController {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	HistoryService		historyService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) {
		if("PO".equals(SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE))){
			return new ModelAndView("gsf/po").addObject("_system_type",
					SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE));
		}else{
			return new ModelAndView("gsf/bo").addObject("_system_type",
					SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE));
		}
	}

	@RequestMapping(value = "/popup/**", method = RequestMethod.GET)
	public ModelAndView viewCcsPopupPage(HttpServletRequest request) throws Exception {
		checkReadHistory(request);
		return new ModelAndView(CommonUtil.makeJspUrl(request, BaseConstants.VIEW_TYPE_POPUP_CCS)).addObject("_system_type",
				SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE));
	}

	@RequestMapping(value = "/**/popup/**", method = RequestMethod.GET)
	public ModelAndView viewPopupPage(HttpServletRequest request) throws Exception {
		checkReadHistory(request);
		return new ModelAndView(CommonUtil.makeJspUrl(request, BaseConstants.VIEW_TYPE_POPUP)).addObject("_system_type",
				SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE));
	}

	@RequestMapping(value = "/**/**", method = RequestMethod.GET)
	public ModelAndView viewPage(@RequestParam(required = false) String pageId, HttpServletRequest request) throws Exception {
		checkReadHistory(request);
		return new ModelAndView(CommonUtil.makeJspUrl(request, BaseConstants.VIEW_TYPE_COMMON)).addObject("pageId", pageId)
				.addObject("_system_type", SessionUtil.getSession(request, BaseConstants.SESSION_KEY_SYSTEM_TYPE));
	}

	private void checkReadHistory(HttpServletRequest request) throws Exception {
		// 개인정보 조회 이력 체크
		String[] urlArray = Config.getString("read.history.check.url").split(",");

		for (String url : urlArray) {
			if (request.getRequestURI().indexOf(url) >= 0) {
				MmsReadhistory mmsReadhistory = new MmsReadhistory();
				mmsReadhistory.setUserId(SessionUtil.getLoginId());
				mmsReadhistory.setDetail(url);
				mmsReadhistory.setInsId(SessionUtil.getLoginId());
				mmsReadhistory.setUpdId(SessionUtil.getLoginId());
				historyService.saveReadHistory(mmsReadhistory);
			}
		}
	}
}
