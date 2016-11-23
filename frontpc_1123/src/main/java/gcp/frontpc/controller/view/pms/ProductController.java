package gcp.frontpc.controller.view.pms;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.service.QnaService;
import gcp.common.util.FoSessionUtil;
import gcp.common.util.PmsUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.CornerService;
import gcp.external.service.RecobelRecommendationService;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductoption;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsSaleproductoptionvalue;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.custom.PmsOptionvalue;
import gcp.pms.model.search.PmsProductQnaSearch;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.ProductService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.ImageUrlUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("productViewController")
@RequestMapping("pms/product")
public class ProductController {
	
	
	private final Log						logger	= LogFactory.getLog(getClass());

	@Autowired
	private ProductService					productService;
	@Autowired
	private QnaService						qnaService;

	@Autowired
	private ProductReviewService			productReviewService;

	@Autowired
	private RecobelRecommendationService	recobelRecommendationService;

	@Autowired
	private CornerService					cornerService;
	
	@Autowired
	private CategoryService 				categoryService;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ModelAndView product(PmsProductSearch search, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (CommonUtil.isEmpty(search.getProductId()) && CommonUtil.isEmpty(search.getBarcode())) {
			throw new ServiceException("pms.product.detail");
		}

		// referer 로부터 displayCategoryId 조회
		String referer = request.getHeader("referer");
		if (CommonUtil.isNotEmpty(referer)) {
			search.setDisplayCategoryId(PmsUtil.getDisplayCategoryIdFromRefer(referer));
		}

		boolean mobilecheck = FoSessionUtil.isMobile(request);
		String viewName = CommonUtil.makeJspUrl(request, mobilecheck);

		ModelAndView mv = new ModelAndView(viewName);

		search.setStoreId(SessionUtil.getStoreId());

		search.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		PmsProduct product = productService.getFoProductDetail(search);

		// 코너 조회(배너)
		String displayID = Config.getString("corner.product.detail.img.1");
		DmsDisplaySearch cornerSearch = new DmsDisplaySearch();
		cornerSearch.setDisplayId(displayID);
		cornerSearch.setStoreId(SessionUtil.getStoreId());
		List<DmsDisplay> bannerList = cornerService.getCommonCornerList(cornerSearch);
		if (bannerList != null) {
			mv.addObject("bannerList", bannerList.get(0).getDmsDisplayitems());
		}

		if (CommonUtil.isNotEmpty(request.getParameter("keyword"))) {
			mv.addObject("keyword", URLDecoder.decode(request.getParameter("keyword"), "UTF-8"));// 추천데이터 수집을위한 검색어
		}

		CookieUtil cookie = new CookieUtil();

		if(mobilecheck) {
			/*
			 * 모바일 history 쿠키 계속유지 => 쿠키 10년, 쿠키 내 데이터 10년 set함
			 * */ 
			String temp = "PRODUCT," + product.getProductId();
			cookie.createCookieString(request, response, "moHistory", temp, cookie.SECONDS_OF_10YEAR, new BigDecimal(3650));
		} else {
			// 최근 본 상품 ID 쿠키 저장 (30일)
			cookie.createCookieString(request, response, "latestProduct", product.getProductId(), 2592000, new BigDecimal(30));
		}

		// 상품의 최저가
		BigDecimal minPrice = product.getOptimalprice().getTotalSalePrice().subtract(product.getOptimalprice().getCouponDcAmt());
		mv.addObject("minSalePrice", minPrice);

		// 세트이거나, 옵션 없는 상품의 총구매 초기값
		if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd()) || "N".equals(product.getOptionYn())) {
			mv.addObject("initSalePrice", minPrice);
		} else {
			mv.addObject("initSalePrice", 0);
		}

		// 픽업상품이고 옵션이 없는경우 화면에서 가격 필요
		if ("Y".equals(product.getOffshopPickupYn())) {
			mv.addObject("initPickupSalePrice",
					PmsUtil.applyDcRateToPrice(product.getSalePrice(), product.getOffshopPickupDcRate()));
		} else {
			mv.addObject("initPickupSalePrice", 0);
		}

//		offshopid

		mv.addObject("displayCategoryId", search.getDisplayCategoryId());
		mv.addObject("offshopId", request.getParameter("offshopId"));
		mv.addObject("styleNo", request.getParameter("styleNo"));
		mv.addObject("product", product);
		mv.addObject("ogTagTitle", product.getName());
		mv.addObject("ogTagImage", ImageUrlUtil.productMakeURL(product.getProductId(), "500", "0", request));
		mv.addObject("ogTagUrl",
				Config.getString("front.domain.url") + "/pms/product/detail?productId=" + product.getProductId());
		
		/**********************************************************************************
		 * 카테고리 목록 조회
		 * - 같은 depth의 카테고리 목록을 조회한다. 
		 ***********************************************************************************/
		if(mobilecheck){
			List<DmsDisplaycategory> currentCategory = new ArrayList<DmsDisplaycategory>();
			
			if(CommonUtil.isNotEmpty(search.getDisplayCategoryId())){
				DmsDisplaySearch ctgrSearch = new DmsDisplaySearch();
				ctgrSearch.setStoreId(SessionUtil.getStoreId());
				
				//현재 카테고리 depth 정보를 가져온다
				ctgrSearch.setDisplayCategoryId(search.getDisplayCategoryId());
				currentCategory = categoryService.getAllCategoryList(ctgrSearch);
				if(currentCategory != null && currentCategory.size() > 0){
					mv.addObject("currentCategory", currentCategory.get(0));
				}
			}
		}
		
		mv.addObject("isReload", "N");
		return mv;
	}

	/**
	 * 상품상세 문의 목록 조회
	 * 
	 * @Method Name : getProductQnaListByProductId
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/qna/list/ajax", method = RequestMethod.GET)
	public ModelAndView getProductQnaListByProductId(PmsProductQnaSearch search, HttpServletRequest request) {

		search.setStoreId(SessionUtil.getStoreId());
		search.setPageSize(10);

		List<PmsProductqna> list = qnaService.getProductQnaListByProductId(search);

		String viewName = "/pms/product/inner/qnaList";
		if (FoSessionUtil.isMobile(request)) {
			viewName = "/pms/product/inner/qnaList_mo";
		}

		ModelAndView mv = new ModelAndView(viewName);

		mv.addObject("list", list);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

	/**
	 * 상품상세 상품평 목록 조회
	 * 
	 * @Method Name : getProductQnaListByProductId
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/reviews/list/ajax", method = RequestMethod.GET)
	public ModelAndView getProductReviewListByProductId(PmsProductReviewSearch search, HttpServletRequest request) {

		search.setStoreId(SessionUtil.getStoreId());
		search.setPageSize(10);

		if ("img".equals(search.getType())) {
			search.setImgYn("Y");
		} else if ("permit".equals(search.getType())) {
			search.setPermitYn("Y");
		}
		List<PmsReview> list = productReviewService.getProductReviewListByProductId(search);

		String viewName = "/pms/product/inner/reviewList";
		if (FoSessionUtil.isMobile(request)) {
			viewName = "/pms/product/inner/reviewList_mo";
		}

		PmsProductReviewSearch reviewSearch = new PmsProductReviewSearch();
		reviewSearch.setStoreId(SessionUtil.getStoreId());
		reviewSearch.setProductId(search.getProductId());

		PmsReview listCount = productReviewService.getProductReviewCountByMemberNo(reviewSearch);

		ModelAndView mv = new ModelAndView(viewName);

		mv.addObject("list", list);
		mv.addObject("listCount", listCount);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

	@RequestMapping(value = "/option/change/ajax" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public ModelAndView getOptionChangeLayer(PmsProductSearch search, HttpServletRequest request) {

		search.setStoreId(SessionUtil.getStoreId());

		String pickupProduct = search.getPickupProduct();
		String offshopId = search.getOffshopId();

		// 단품번호로 옵션값 조회
		List<PmsSaleproductoptionvalue> selectedOptions = productService.selectSaleproductOptionvalues(search);

		Map<String, String> selectedOptionMap = new HashMap<String, String>();
		// 선택된 옵션값을 맵으로 만든다
		for (PmsSaleproductoptionvalue selected : selectedOptions) {
			selectedOptionMap.put(selected.getOptionName(), selected.getOptionValue());
		}

		List<PmsProductoption> optionList = productService.getPmsProductOptionList(search);

		if (optionList != null && optionList.size() > 0) {

			// 첫번째 옵션 목록 조회
			PmsProductSearch s = new PmsProductSearch();
			s.setStoreId(search.getStoreId());
			s.setProductId(search.getProductId());
			s.setPickupProduct(pickupProduct);
			s.setOffshopId(offshopId);
			setOptionList(optionList.get(0), selectedOptionMap, s);

			// 첫번째 이후 옵션 목록 조회
			List<PmsSaleproductoptionvalue> selectedParam = new ArrayList<PmsSaleproductoptionvalue>();
			// s = new PmsProductSearch();

			for (int i = 1; i < optionList.size(); i++) {

				// 필터세팅 : 상위 콤보의 선택값 세팅
				PmsSaleproductoptionvalue v = new PmsSaleproductoptionvalue();
				v.setOptionName(optionList.get(i - 1).getOptionName());
				v.setOptionValue(selectedOptionMap.get(optionList.get(i - 1).getOptionName()));
				selectedParam.add(v);
				s.setSelectedOptions(selectedParam);

				setOptionList(optionList.get(i), selectedOptionMap, s);

			}
		}

		// 선택된 수량
		selectedOptionMap.put("qty", search.getQty());

		ModelAndView mv = new ModelAndView(StringUtils.defaultString(search.getSearchId(), "/ccs/common/layer/optionChangeLayer"));
		mv.addObject("optionList", optionList);
		mv.addObject("selected", selectedOptionMap);
		mv.addObject("productId", search.getProductId());
		return mv;
	}

	private void setOptionList(PmsProductoption targetOption, Map<String, String> selectedOptionMap, PmsProductSearch search) {

		// 첫번째 옵션 목록 조회
		search.setTargetOptionName(targetOption.getOptionName());
		List<PmsOptionvalue> valueList = productService.getOptionValueList(search);

		// 선택되어지게 처리
		for (PmsOptionvalue value : valueList) {
			if (value.getOptionValue().equals(selectedOptionMap.get(value.getOptionName()))) {
				value.setSelected("Y");
				break;
			}
		}
		targetOption.setOptionValues(valueList);

	}

	/**
	 * 세트옵션변경 팝업.
	 * 
	 * @Method Name : getOptionChangeLayer1
	 * @author : victor
	 * @date : 2016. 9. 28.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/option/change/ajax1", method = RequestMethod.GET)
	public ModelAndView getOptionChangeLayer1(PmsProductSearch search, HttpServletRequest request) {
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsSetproduct> pmsSetproducts = productService.selectSetproductList(search);

		ModelAndView mv = new ModelAndView(StringUtils.defaultString(search.getSearchId(), "/ccs/common/layer/optionChangeLayer1"));
		mv.addObject("pmsSetproducts", pmsSetproducts);
		return mv;
	}

	/**
	 * 옵션선택 레이어 팝업
	 * 
	 * @Method Name : getOptionSelectLayer
	 * @author : stella
	 * @date : 2016. 9. 13.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/option/select/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getOptionSelectLayer(@RequestBody PmsProductSearch search, HttpServletRequest request) {
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsProductoption> optionList = productService.getPmsProductOptionList(search);

		Map<String, String> selectedOptionMap = new HashMap<String, String>();

		// 첫번째 옵션 목록 조회
		PmsProductSearch s = new PmsProductSearch();
		s.setStoreId(search.getStoreId());
		s.setProductId(search.getProductId());

		for (int i = 0; i < optionList.size(); i++) {
			setOptionList(optionList.get(i), selectedOptionMap, s);
		}

		ModelAndView mv = new ModelAndView("/ccs/common/layer/optionSelectLayer");
		mv.addObject("optionList", optionList);
		mv.addObject("selectPart", search.getSearchKeyword());	// cart:장바구니, order:바로구매

		if (optionList.get(0).getOptionValues().get(0).getOptionValue().equals("없음")) {
			mv.addObject("optionCnt", 0);
		} else {
			mv.addObject("optionCnt", optionList.size());
		}

		return mv;
	}

	@RequestMapping(value = "/recommenadation/ajax", method = { RequestMethod.POST })
	public ModelAndView getCategoryBestProduct(@RequestParam Map<String, String> map, HttpServletRequest request)
			throws Exception {
		ModelAndView mv = new ModelAndView("/pms/product/inner/recobellProducts");

		List<PmsProduct> list = recobelRecommendationService.getRecommendationList(map);
		mv.addObject("products", list);

		return mv;
	}
	
	/**
	 * 브랜드관 브랜드별 Best 상품
	 * 
	 * @Method Name : getBrandBestProduct
	 * @author : stella
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/brand/best", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getBrandBestProduct(@RequestBody PmsProductSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/template/inner/brandProductList");
		
		search.setStoreId(SessionUtil.getStoreId());
		
		List<PmsProduct> bestProductList = productService.getBrandBestProduct(search);		
		mv.addObject("bestProductList", bestProductList);
		mv.addObject("bestTotal", bestProductList.get(0).getTotalCount());
		
		return mv;
	}
	
	/**
	 * 브랜드관 브랜드별 실시간 인기상품
	 * 
	 * @Method Name : getBrandRealtimeProduct
	 * @author : stella
	 * @date : 2016. 11. 9.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/brand/realtimeProduct", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getBrandRealtimeProduct(@RequestBody PmsProductSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/template/inner/brandProductList");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("recType", "b004");
		map.put("size", "8");
		map.put("bids", search.getErpBrandId());

		logger.debug("@param:" + map.toString());
		List<PmsProduct> list = recobelRecommendationService.getRecommendationList(map);	
		mv.addObject("realtimeProductList", list);
		mv.addObject("realtimeTotal", list.size());
		
		return mv;
	}
}
