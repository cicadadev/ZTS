package gcp.frontpc.controller.view.ccs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.search.CcsFaqSearch;
import gcp.ccs.service.QnaService;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.service.MemberService;
import gcp.oms.service.OrderService;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("qnaViewController")
@RequestMapping("ccs/cs/qna")
public class QnaController {

	@Autowired
	private QnaService qnaService;
	
	@Autowired
	private BrandService brandService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MemberService memberService;
	/**
	 * 1:1 문의
	 * 
	 * @Method Name : qna
	 * @author : roy
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/insert", method ={ RequestMethod.GET , RequestMethod.POST })
	public ModelAndView qna(CcsFaqSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		// 브랜드 목록
		BrandSearch searchBrand = new BrandSearch();
		
		searchBrand.setStoreId(SessionUtil.getStoreId());		
		
		mv.addObject("brand", brandService.getBrandList(searchBrand));	
		
		// 회원 정보
		MmsMemberZts member = memberService.getMemberDetail(SessionUtil.getMemberNo());

		mv.addObject("member", member);
		
		// 브랜드관에서 들어올 경우(브랜드관 1:1 문의)
		mv.addObject("brandId", CommonUtil.replaceNull(request.getParameter("brandId")));

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "1:1 문의하기");
		mv.addObject("pageNavi", pageMap);
		
		return mv;
	}
	
}
