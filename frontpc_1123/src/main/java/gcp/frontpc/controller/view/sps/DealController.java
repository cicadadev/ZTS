package gcp.frontpc.controller.view.sps;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.search.SpsDealProductSearch;
import gcp.sps.service.DealService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("dealViewController")
@RequestMapping("sps/deal")
public class DealController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private DealService	 dealService;

	/**
	 * @Method Name : getShockingzeroMain
	 * @author : stella
	 * @date : 2016. 8. 3.
	 * @description : 쇼킹제로 메인
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shockingzero/main", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getShockingzeroMain(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		if(CommonUtil.equals("mainCon", mobileType)){
			mv.setViewName("/sps/deal/shockingzero/inner/shockingzeroMain.mainCon");
		}else{
			mv.setViewName(CommonUtil.makeJspUrl(request));
		}
		
		SpsDealProductSearch dealProductSearch = new SpsDealProductSearch();
		dealProductSearch.setStoreId(SessionUtil.getStoreId());
		
		// 쇼킹제로 메인 조회
		dealProductSearch.setProductSearchKeyword(Config.getString("corner.special.shock.product.1"));
		List<SpsDealproduct> shockingMainProducts = dealService.getShockingzeroMainProduct(dealProductSearch);
		
		mv.addObject("shockingMainProducts", shockingMainProducts);
		
		return mv;
	}

	/**
	 * @Method Name : getShockingzeroProduct
	 * @author : stella
	 * @date : 2016. 9. 6.
	 * @description : 쇼킹제로 상품 정렬(인기상품순/신규오픈순/종료임박순)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shockingzero/product/ajax", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getShockingzeroProduct(SpsDealProductSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/sps/deal/shockingzero/inner/shockingzeroList");
				
		if (FoSessionUtil.isMobile()) {
			search.setPageSize(20 * search.getCurrentPage());
		} else {
			search.setPagingYn("N");
		}
		search.setStoreId(SessionUtil.getStoreId());
		List<SpsDealproduct> shockingProducts = dealService.getShockingzeroProductList(search);
		
		mv.addObject("shockingProducts", shockingProducts);
		
		if (FoSessionUtil.isMobile()) {
			mv.addObject("totalCount", dealService.checkShockingzeroCount(search).size());			
			mv.addObject("search", search);
		}

		return mv;
	}

}
