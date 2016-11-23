package gcp.frontpc.controller.view.ccs;

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

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.OffshopService;
import gcp.common.util.FoSessionUtil;
import gcp.external.service.ErpService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("offshopViewController")
@RequestMapping("ccs/offshop")
public class OffshopController {
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private OffshopService offshopService;
	
	@Autowired
	private ErpService erpService;
	
	@Autowired
	private BrandService brandService;
	
	/**
	 * @Method Name : mainPage
	 * @author : stella
	 * @date : 2016. 8. 3.
	 * @description : 매장 찾기
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView offshopSearchMain(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request)); 
		
		// 특정 브랜드 매장찾기(브랜드 있을 경우)
		String brandName = CommonUtil.replaceNull(request.getParameter("brandName"),"");
		mv.addObject("brandName", brandName);
		
		// 시/도	
		CcsOffshopSearch offshopSearch = new CcsOffshopSearch();
		mv.addObject("area1List", offshopService.getOffshopAreaDiv1List(offshopSearch));
		
		// 브랜드 목록
		BrandSearch brandSearch = new BrandSearch(); 
		List<PmsBrand> brandCode = brandService.getBrandCodeList(brandSearch);
		mv.addObject("brandCodeList", brandCode);
		
 		return mv;
	}
	
	/**
	 * @Method Name : getOffshopListAjax
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description : 매장 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getOffshopListAjax(@RequestBody CcsOffshopSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/offshop/inner/offshopList");

		// 매장 조회
		search.setStoreId(SessionUtil.getStoreId());
		if (FoSessionUtil.isMemberLogin()) {
			search.setMemberNo(SessionUtil.getMemberNo().toString());
		}

		if (FoSessionUtil.isMobile()) {
			search.setIsMobile("Y");
		} else {
			search.setIsMobile("N");
		}
		List<CcsOffshop> offshopList = offshopService.getOffshopSearchList(search);
		for (CcsOffshop offshop : offshopList) {
			//offshop.setMainBrand(erpService.getOffshopMainBrand(offshop.getOffshopId()));
			if (CommonUtil.isNotEmpty(search.getOffshopBrand())) {
				offshop.setMainBrand(search.getOffshopBrand());
			} else {
				if (CommonUtil.isNotEmpty(offshop.getCcsOffshopbrands())) {
					offshop.setMainBrand(offshop.getCcsOffshopbrands().get(0).getName());
				}		
			}		
		}
		mv.addObject("offshopList", offshopList);
		mv.addObject("totalCount", offshopList.size());

		return mv;
	}
	
	/**
	 * @Method Name : offshopInfoLayer
	 * @author : stella
	 * @date : 2016. 9. 7.
	 * @description : 매장 정보(레이어 팝업)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/layer", method = RequestMethod.POST)
	public ModelAndView offshopInfoLayer(@RequestBody CcsOffshopSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/offshop/layer/offshopLayer");
		
		search.setStoreId(SessionUtil.getStoreId());
		mv.addObject("offshopInfo", offshopService.getOffshopInfo(search));

		return mv;
	}	

}
