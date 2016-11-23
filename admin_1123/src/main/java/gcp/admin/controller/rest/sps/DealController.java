package gcp.admin.controller.rest.sps;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealgroup;
import gcp.sps.model.SpsDealmember;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsDealsaleproductprice;
import gcp.sps.model.search.SpsDealProductSearch;
import gcp.sps.model.search.SpsDealSearch;
import gcp.sps.service.DealService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/sps/deal")
public class DealController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private DealService	dealService;
	
	@Autowired
	private ProductService	productService;
	/**
	 * 
	 * @Method Name : getDealList
	 * @author : allen
	 * @date : 2016. 6. 8.
	 * @description : 딜 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<SpsDeal> getDealList() throws Exception {
		SpsDealSearch dealsearch = new SpsDealSearch();
		dealsearch.setStoreId(SessionUtil.getStoreId());
		return dealService.getDealList(dealsearch);
	}

	/**
	 * 
	 * @Method Name : getDealDetail
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 딜 상세
	 *
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{dealId}", method = RequestMethod.POST)
	public SpsDeal getDealDetail(@PathVariable("dealId") String dealId) throws Exception {
		SpsDealSearch dealsearch = new SpsDealSearch();
		dealsearch.setStoreId(SessionUtil.getStoreId());
		dealsearch.setDealId(dealId);
		return dealService.getDealList(dealsearch).get(0);
	}

	/**
	 * 
	 * @Method Name : udpateDeal
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 딜 상세 정보 업데이트
	 *
	 * @param spsDeal
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateDeal", method = RequestMethod.POST)
	public void udpateDeal(@RequestBody SpsDeal spsDeal) throws Exception {
		dealService.updateDeal(spsDeal);
	}

	/**
	 * 
	 * @Method Name : getOneDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 1depth 구분 타이틀 조회
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/oneDepthDivTit/list", method = RequestMethod.POST)
	public List<SpsDealgroup> getOneDepthDivTitle(@RequestBody SpsDealSearch spsDealSearch) throws Exception {
		return dealService.getOneDepthDealGroupList(spsDealSearch);
	}

	/**
	 * 
	 * @Method Name : saveOneDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 1depth 구분 타이틀 저장
	 *
	 * @param spsDealGroupLsit
	 * @throws Exception
	 */
	@RequestMapping(value = "/oneDepthDivTit/save", method = RequestMethod.POST)
	public void saveOneDepthDivTitle(@RequestBody List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		dealService.saveOneDepthDivTitle(spsDealGroupLsit);
	}

	/**
	 * 
	 * @Method Name : delOneDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 1depth 구분 타이틀 삭제
	 *
	 * @param spsDealGroupLsit
	 * @throws Exception
	 */
	@RequestMapping(value = "/oneDepthDivTit/delete", method = RequestMethod.POST)
	public void delOneDepthDivTitle(@RequestBody List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		dealService.delOneDepthDivTitle(spsDealGroupLsit);
	}

	/**
	 * 
	 * @Method Name : getTwoDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 2depth 구분 타이틀 조회
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/twoDepthDivTit/list", method = RequestMethod.POST)
	public List<SpsDealgroup> getTwoDepthDivTitle(@RequestBody SpsDealSearch spsDealSearch) throws Exception {
		return dealService.getTwoDepthDealGroupList(spsDealSearch);
	}

	/**
	 * 
	 * @Method Name : saveTwoDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 2depth 구분 타이틀 저장
	 *
	 * @param spsDealGroupLsit
	 * @throws Exception
	 */
	@RequestMapping(value = "/twoDepthDivTit/save", method = RequestMethod.POST)
	public void saveTwoDepthDivTitle(@RequestBody List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		dealService.saveTwoDepthDivTitle(spsDealGroupLsit);
	}

	/**
	 * 
	 * @Method Name : delOneDepthDivTitle
	 * @author : allen
	 * @date : 2016. 6. 13.
	 * @description : 1depth 구분 타이틀 삭제
	 *
	 * @param spsDealGroupLsit
	 * @throws Exception
	 */
	@RequestMapping(value = "/twoDepthDivTit/delete", method = RequestMethod.POST)
	public void delTwoDepthDivTitle(@RequestBody List<SpsDealgroup> spsDealGroupLsit) throws Exception {
		dealService.delTwoDepthDivTitle(spsDealGroupLsit);
	}

	/**
	 * 
	 * @Method Name : getDealgroupTree
	 * @author : allen
	 * @date : 2016. 6. 14.
	 * @description : 딜 그룹 Tree 조회
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dealGroupTree", method = RequestMethod.POST)
	public List<SpsDealgroup> getDealgroupTree(@RequestBody SpsDealSearch search) throws Exception {
		return dealService.getDealGroupTree(search);
	}

	/**
	 * 
	 * @Method Name : getDealProductList
	 * @author : allen
	 * @date : 2016. 6. 17.
	 * @description : 딜 상품 목록 조회
	 *
	 * @param spsDealProductSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.POST)
	public List<SpsDealproduct> getDealProductList(@RequestBody SpsDealProductSearch spsDealProductSearch) throws Exception {
		List<SpsDealproduct> productList = dealService.getDealProductList(spsDealProductSearch);
		return productList;
	}

	/**
	 * 
	 * @Method Name : saveDealProduct
	 * @author : allen
	 * @date : 2016. 6. 17.
	 * @description : 딜 상품 저장
	 *
	 * @param spsDealProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/save", method = RequestMethod.POST)
	public void saveDealProduct(@RequestBody List<SpsDealproduct> spsDealProductList) throws Exception {
		dealService.saveDealProduct(spsDealProductList);
	}

	/**
	 * 
	 * @Method Name : deleteDealProduct
	 * @author : allen
	 * @date : 2016. 6. 17.
	 * @description : 딜 상품 삭제
	 *
	 * @param spsDealProductList
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST)
	public void deleteDealProduct(@RequestBody List<SpsDealproduct> spsDealProductList) throws Exception {
		dealService.deleteDealProduct(spsDealProductList);
	}

	/**
	 * 
	 * @Method Name : getDealSaleProductList
	 * @author : allen
	 * @date : 2016. 6. 17.
	 * @description : 딜 상품의 단품 목록 조회
	 *
	 * @param spsDealProductList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/saleProdcutList", method = RequestMethod.POST)
	public List<PmsSaleproduct> getDealSaleProductList(@RequestBody PmsProductSearch productSearch) throws Exception {
		productSearch.setStoreId(SessionUtil.getStoreId());
		return productService.getSaleProductList(productSearch);
	}

	@RequestMapping(value = "/product/checkDealProduct", method = RequestMethod.POST)
	public int checkDealProduct(@RequestBody SpsDealProductSearch productSearch) throws Exception {
		productSearch.setStoreId(SessionUtil.getStoreId());
		return dealService.checkDealProductPeriod(productSearch);
	}

	/**
	 * 
	 * @Method Name : getMemberGradeBenefit
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 딜 상품의 설정된 등급별 선오픈일 및 할인 금액 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/memberGradeBenefit", method = RequestMethod.POST)
	public List<SpsDealmember> getMemberGradeBenefit(@RequestBody SpsDealProductSearch search) throws Exception {

		search.setStoreId(SessionUtil.getStoreId());
		return dealService.getMemberGradeBenefit(search);
	}

	/**
	 * 
	 * @Method Name : getSaleProductPrice
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 딜 상품의 설정된 단품 추가 할인 금액 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/SaleProductPrice", method = RequestMethod.POST)
	public List<SpsDealsaleproductprice> getSaleProductPrice(@RequestBody SpsDealProductSearch search) throws Exception {

		search.setStoreId(SessionUtil.getStoreId());
		return dealService.getSaleProductPrice(search);
	}

}
