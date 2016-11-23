package gcp.frontpc.controller.rest.ccs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.OffshopService;
import gcp.common.util.FoSessionUtil;
import gcp.external.model.search.AddressSearch;
import gcp.external.service.AddressService;
import gcp.mms.model.MmsInterestoffshop;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/offshop")
public class OffshopController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private OffshopService offshopService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private BrandService brandService;

	/**
	 * @Method Name : getArea1List
	 * @author : stella
	 * @date : 2016. 9. 26.
	 * @description : 시/도 데이터 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/area1/search", method = { RequestMethod.POST })
	public ModelMap getArea1List(@RequestBody CcsOffshopSearch search, HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		
		// 시/도 조회
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(search.getOffshopBrand())) {
			BrandSearch brandSearch = new BrandSearch();
			brandSearch.setStoreId(SessionUtil.getStoreId());
			brandSearch.setSearchKeyword("name");
			brandSearch.setBrand(search.getOffshopBrand());
			
			search.setBrandId(brandService.getBrandList(brandSearch).get(0).getBrandId());
		}
		map.addAttribute("area1List", offshopService.getOffshopAreaDiv1List(search));		

		return map;
	}
	
	/**
	 * @Method Name : getArea2List
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description : 시/군/구 데이터 조회
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/area2/search", method = { RequestMethod.POST })
	public ModelMap getArea2List(@RequestBody CcsOffshopSearch search, HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();
		
		// 시/군/구 조회
		search.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(search.getOffshopBrand())) {
			BrandSearch brandSearch = new BrandSearch();
			brandSearch.setStoreId(SessionUtil.getStoreId());
			brandSearch.setSearchKeyword("name");
			brandSearch.setBrand(search.getOffshopBrand());
			
			search.setBrandId(brandService.getBrandList(brandSearch).get(0).getBrandId());
		}
		map.addAttribute("area2List", offshopService.getOffshopAreaDiv2List(search));

		return map;
	}
	
	/**
	 * @Method Name : updateInterestOffshop
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description : 관심매장 등록/해제
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update/interest", method = { RequestMethod.POST })
	public void updateInterestOffshop(@RequestBody List<MmsInterestoffshop> interestOffshop, HttpServletRequest request) throws Exception {		
		offshopService.updateInterestOffshop(interestOffshop);
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
	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public ModelMap getOffshopList(@RequestBody CcsOffshopSearch search, HttpServletRequest request) throws Exception {
		ModelMap map = new ModelMap();

		// 매장 조회
		search.setStoreId(SessionUtil.getStoreId());
		if (FoSessionUtil.isMemberLogin()) {
			search.setMemberNo(SessionUtil.getMemberNo().toString());
		}

		map.addAttribute("offshopList", offshopService.getOffshopSearchList(search));

		return map;
	}
}

