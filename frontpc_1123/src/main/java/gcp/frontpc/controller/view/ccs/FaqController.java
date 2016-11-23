package gcp.frontpc.controller.view.ccs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.search.CcsFaqSearch;
import gcp.ccs.service.FaqService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("faqViewController")
@RequestMapping("ccs/cs/faq")
public class FaqController {

	@Autowired
	private FaqService faqService;
	
	/**
	 * FAQ
	 * 
	 * @Method Name : faq
	 * @author : roy
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list", method ={ RequestMethod.GET , RequestMethod.POST })
	public ModelAndView faq(CcsFaqSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		mv.addObject("search", search);
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "FAQ");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * FAQ 목록 조회
	 * 
	 * @Method Name : getFaqListSearch
	 * @author : roy
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list/ajax", method = RequestMethod.GET)
	public ModelAndView getFaqListSearch(CcsFaqSearch search, HttpServletRequest request) {

		search.setStoreId(SessionUtil.getStoreId());
		search.setPageSize(10);

		List<CcsFaq> list = faqService.getFaqListSearch(search);

		ModelAndView mv = new ModelAndView("/ccs/cs/faq/inner/faqList");
		mv.addObject("list", list);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}
}
