package gcp.frontpc.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.NoticeService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.search.SpsCouponIssueSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/notice")
public class NoticeController {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private NoticeService noticeService;

	/**
	 * 공지사항 목록 조회
	 * @Method Name : getList
	 * @author : emily
	 * @date : 2016. 8. 2.
	 * @description : 
	 *		비동기식으로 페이징 처리를 위한 테스트 메소드임.
	 *			삭제될 예정임.
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public JSONObject  getList(@ModelAttribute CcsNoticeSearch search, HttpServletRequest request){
		JSONObject returnVal = new JSONObject();
		
		logger.debug("======================================");
		logger.debug("CurrentPage:"+search.getCurrentPage());
		logger.debug("PageSize:"+search.getPageSize());
		logger.debug("Sort:"+search.getSort());
		logger.debug("Direction:"+search.getDirection());
		logger.debug("======================================");
		
		search.setStoreId(SessionUtil.getStoreId());
		List<CcsNotice> list= noticeService.selectList(search);
		returnVal.put("list", list);
		
		return returnVal;
	}
	
	
	/**
	 * 
	 * @Method Name : getBrandTemplate
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "/brand/list", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelMap getBrandTemplateNotice(HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		
		// 브랜드관 메인 공지사항
		CcsNoticeSearch search = new CcsNoticeSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setBrandId(CommonUtil.replaceNull(request.getParameter("brandId"), ""));
		search.setPageSize(1);
		
		CcsNotice notice = noticeService.getBrandNotice(search);
		map.addAttribute("notice", notice);
		
		return map;
	}
	
}
