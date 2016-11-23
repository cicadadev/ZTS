package gcp.frontpc.controller.view.dms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsCatalogimg;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsSearchOptionFilter;
import gcp.dms.model.DmsTemplate;
import gcp.dms.model.DmsTemplateDisplay;
import gcp.dms.model.search.DmsCatalogSearch;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CatalogService;
import gcp.dms.service.CategoryService;
import gcp.dms.service.DisplayCommonService;
import gcp.dms.service.ExhibitService;
import gcp.dms.service.SearchService;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.vo.SearchData;
import gcp.external.service.RecobelRecommendationService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.ProductService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("displayCommonViewController")
@RequestMapping("dms/common")
public class DisplayCommonController {
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private DisplayCommonService displayCommonService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private ExhibitService exhibitService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private RecobelRecommendationService recobelRecommendationService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CatalogService catalogService;

	/**
	 * 템플릿, 코너, 컨텐츠 조회
	 * @Method Name : getDisplayTemplate
	 * @author : emily
	 * @date : 2016. 7. 5.
	 * @description : 
	 * 				전시카테고리 매장 :  dispCategoryId 필수 값
	 * 				브랜드매장 : brandId 필수
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/templateDisplay")
	public ModelAndView getDisplayTemplate(@ModelAttribute("dmsDisplaySearch") DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		logger.debug("===================================================");
		logger.debug("REQEUST BRAND_ID:"+request.getParameter("brandId"));
		logger.debug("REQEUST DISPLAYCATEGORY_ID:"+request.getParameter("dispCategoryId"));
		logger.debug("===================================================");
		
		/**********************************************************************************
		 * 공통 파라미터 & searchModel 설정
		 **********************************************************************************/
		ModelAndView model = new ModelAndView();
		boolean isMoblie = FoSessionUtil.isMobile(request);
		DmsTemplate tmpResult = new DmsTemplate();
		PmsBrand brandInfo = new PmsBrand();	//브랜드관 네비게이션, 브랜드 로고
		
		//현재카테고리 정보
		List<DmsDisplaycategory> currentCategory = new ArrayList<DmsDisplaycategory>();
		
		//Request 카테고리ID
		String displayCategoryId = CommonUtil.replaceNull(request.getParameter("dispCategoryId"),"");
		model.addObject("dispCategoryId", displayCategoryId);
		
		//Reqeust 브랜드ID
		String brandId = CommonUtil.replaceNull(request.getParameter("brandId"),"");
		model.addObject("brandId", brandId);
		
		//템플릿정보 조회를 위한 search
		DmsDisplaySearch tmpSearch = new DmsDisplaySearch();
		tmpSearch.setStoreId(BaseConstants.STORE_ID);
		tmpSearch.setDisplayCategoryId(displayCategoryId);
		
		//검색API 조회를 위한 search
		SearchApiSearch searchParam = new SearchApiSearch();
		List<String> categoryIdList = new ArrayList<String>();
		List<String> brandIdList = new ArrayList<String>();
		boolean searchApiYn = false;
		
		//카테고리 조회를 위한 search
		DmsDisplaySearch ctgrSearch = new DmsDisplaySearch();
		boolean tempYn = true;
		
		/**********************************************************************************
		 * 전시카테고리 템플릿
		 * - 카테고리 매장
		 * - 필수값 : 전시카테고리ID
		 ***********************************************************************************/
		if(CommonUtil.isNotEmpty(displayCategoryId)){
			
			ctgrSearch.setStoreId(SessionUtil.getStoreId());
			ctgrSearch.setDisplayCategoryId(displayCategoryId);
			ctgrSearch.setRootCategoryId(Config.getString("root.display.category.id")); 
			
			//현재카테고리 depth정보
			currentCategory = categoryService.getAllCategoryList(ctgrSearch);
			model.addObject("currentCategory", currentCategory.get(0));
			
			/*******************************************************************************
			 * Leaf 카테고리 경우
			 * - 같은 레벨의 Leaf카테고리 목록을 가져온다.
			 *******************************************************************************/
			if(CommonUtil.equals("Y", currentCategory.get(0).getLeafYn())){
				//현재카테고리의 상위카테고리 ID
				ctgrSearch.setDisplayCategoryId(currentCategory.get(0).getUpperDisplayCategoryId());
				searchParam.getCategoryIdList().add(displayCategoryId);
				tempYn = false;
			}
			
			/*****************************************************************************	
			 * depth 카테고리 목록 조회
			 * 	- 리프여부 'Y' : 상위 카테고리의  depth정보를 가져온다. 
			 *  - 리프여부 'N' : 현재 카테고리의  depth정보를 가져온다.
			 *****************************************************************************/
			List<DmsDisplaycategory> ctgList =  categoryService.getDepth2CategoryList(ctgrSearch);
			
			if(ctgList != null && ctgList.size()> 0 ){
				if(isMoblie){
					
					//모바일 하위카테고리목록  페이징
					int maxPage = ctgList.get(0).getTotalCount()/6;
					if (ctgList.get(0).getTotalCount() % 6 != 0) {
						maxPage++;
					}
					ctgList.get(0).setPageSize(maxPage);
				}
				model.addObject("category", ctgList.get(0));
				
				if(ctgList.get(0).getDmsDisplaycategorys() != null && ctgList.get(0).getDmsDisplaycategorys().size() > 0){
					model.addObject("depthCategory", ctgList.get(0).getDmsDisplaycategorys().get(0).getDisplayCategoryId());
				}
			}
			
			/*****************************************************************************
			 * 검색API 호출을 위한 파라미터 설정
			 *****************************************************************************/
			if((!isMoblie && CommonUtil.equals("Y", currentCategory.get(0).getLeafYn())) || isMoblie){
				
				//검색 호출 여부
				searchApiYn = true;
				
				//PC인데 중카엔 검색엔진 호출 안함.
				if(CommonUtil.equals("N", currentCategory.get(0).getLeafYn())){
					if(ctgList.size() > 0){
						for (DmsDisplaycategory info : ctgList.get(0).getDmsDisplaycategorys()) {
							categoryIdList.add(info.getDisplayCategoryId());
						}
					}
					searchParam.setCategoryIdList(categoryIdList);
				}
			}
			
			/*******************************************************************************
			 * 카테고리의 기획전 정보 조회
			 *******************************************************************************/
			DmsExhibitSearch exhibitSearch = new DmsExhibitSearch();
			exhibitSearch.setDisplayCategoryId(displayCategoryId);
			List<DmsExhibit> exhibitList = exhibitService.getCategoryExhibit(exhibitSearch);
			model.addObject("exhibitList", exhibitList);		
					
			//템플릿조회 파라미터 설정
			tmpSearch.setTemplateId(currentCategory.get(0).getTemplateId());
			tmpSearch.setTemplateTypeCd(BaseConstants.TEMPLATE_TYPE_CD_DISPLAYCATEGORY);
			tmpSearch.setDisplayCategoryId(displayCategoryId);
			if(isMoblie){
				tmpSearch.setRownum(4);
			}
			 
		}else if(CommonUtil.isNotEmpty(brandId)){
		/*****************************************************************************
		 * 브랜드관 템플릿
		 * - 자사 브랜드관 : 템플릿유형 A,B
		 * - 필수값 : 브랜드ID, 템플릿ID
		 * - 브랜드유형별로 하나의 템플릿의 브랜드별 아이템을 조회해온다. 
		 *****************************************************************************/
			// 브랜드 정보 조회(브랜드 템플릿번호, 네비게이션 브랜드 이름, 브랜드 로고)
			BrandSearch brandSearch = new BrandSearch();
			brandSearch.setStoreId(tmpSearch.getStoreId());
			brandSearch.setBrandId(brandId);
			brandInfo = brandService.getBrandShopDetail(brandSearch);
			
			tmpSearch.setTemplateId(brandInfo.getTemplateId());
			tmpSearch.setTemplateTypeCd(BaseConstants.TEMPLATE_TYPE_CD_BRAND);
			//tmpSearch.setDisplayItemId(brandId);
			
			String brandClassName = "";
			if(!CommonUtil.equals(brandInfo.getTemplateId(), Config.getString("corner.brand.templC"))) {
				brandClassName = getBrandClassName(brandId).get(brandInfo.getName());
				model.addObject("brandClassName", brandClassName);
			}
			model.addObject("brandInfo", brandInfo);
			
			// SNS 공유_ direct용 구분자 추가(16.11.01)
			String direct = CommonUtil.replaceNull(request.getParameter("direct"));
			if (CommonUtil.isNotEmpty(direct)) {
				if ("catalogDetail".equals(direct)) {
					String catalogId = CommonUtil.replaceNull(request.getParameter("catalogId"));
					String catalogImgNo = CommonUtil.replaceNull(request.getParameter("catalogImgNo"));

					if (CommonUtil.isNotEmpty(catalogId) && CommonUtil.isNotEmpty(catalogImgNo)) {
						DmsCatalogSearch catalogSearch = new DmsCatalogSearch();
						catalogSearch.setStoreId(tmpSearch.getStoreId());
						catalogSearch.setBrandId(brandId);
						catalogSearch.setCatalogId(catalogId);
						catalogSearch.setCatalogImgNo(catalogImgNo);
						DmsCatalogimg catalogItem = catalogService.getCatalogImgDetail(catalogSearch);

						if (CommonUtil.isNotEmpty(catalogItem)) {
							model.addObject("ogTagTitle", catalogItem.getName());
							model.addObject("ogTagImage", Config.getString("image.domain") + catalogItem.getImg2());
							model.addObject("ogTagUrl",
									Config.getString("front.domain.url") + "/dms/common/templateDisplay?brandId=" + brandId
											+ "&direct=catalogDetail&catalogId=" + catalogId + "&catalogImgNo=" + catalogImgNo);
						}
					}
					model.addObject("catalogId", catalogId);
					model.addObject("catalogImgNo", catalogImgNo);

				}
				model.addObject("direct", direct);
			}
			
			/*****************************************************************************
			 * 자사가 아닌 브랜드관 템플릿 유형 C
			 * - 상품목록을 검색 엔진을 통해서 조회 해온다.
			 * - 현재 템플릿유형이 C형인지 판단할려면 템플릿ID를 config에서 하드코딩 해야함. 
			 *****************************************************************************/
			if(CommonUtil.equals(brandInfo.getTemplateId(), Config.getString("corner.brand.templC"))){
				brandIdList.add(brandId);
				searchParam.setBrandIdList(brandIdList);
				searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_BRAND);
				searchApiYn = true;
				tmpSearch.setDisplayItemDivId(brandId);
			}else{
				model.addObject("brandCategory", getBrandCategory(brandClassName));
				tmpSearch.setDisplayItemId(brandId);
			}
			
			/*****************************************************************************
			 * 모바일 history 쿠키 _ 브랜드 취득 
			 *****************************************************************************/
			if(FoSessionUtil.isMobile(request)) {
				String temp = "BRAND," +brandInfo.getBrandId(); 
				CookieUtil.createCookieString(request, response, "moHistory", temp, CookieUtil.SECONDS_OF_10YEAR, new BigDecimal(3650));
			}
		}
		
		/*****************************************************************************
		 * 검색API 호출
		 * - 카테고리 목록을 검색API를 통해서 조회한다.
		 * - 필수값 : categoryId or categoryIdList
		 * 1) 카테고리 매장
		 * Leaf 카테고리가 아닌 경우
		 * - 모바일 : 하위 카테고리의 상품목록 조회
		 * 			중카에서 하위카테고리의 상품목록을 보여준다.
		 *****************************************************************************/
		try{
			
			if(searchApiYn){
				//카테고리매장일경우 구별하기위함.
				searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_CATEGORY);
				DmsSearchOptionFilter option = searchService.getSearchFilter(searchParam);
				SearchData searchApi = searchService.getSearchApiPrdList(searchParam);
				
				searchApi.setAgeCodeList(option.getAgeCodeList());
				searchApi.setGenderCodeList(option.getGenderCodeList());
				searchParam.setTotalCount(searchApi.getTotalCount());
				
				model.addObject("searchApi", searchApi);
				model.addObject("search", searchParam);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		
		/*******************************************************************************
		 * 카테고리 정보 Cookie 설정
		 * - 카테고리 정보, 브랜드관 정보
		 * - 히스토리 : 모바일 history 쿠키 계속유지 => 쿠키 10년, 쿠키 내 데이터 10년 set함  
		 *******************************************************************************/
		if(isMoblie) {
			if(CommonUtil.isNotEmpty(displayCategoryId) && CommonUtil.equals(BaseConstants.TEMPLATE_TYPE_CD_DISPLAYCATEGORY, tmpSearch.getTemplateTypeCd())){
				String temp = "CATEGORY," + displayCategoryId;
				CookieUtil.createCookieString(request, response, "moHistory", temp, CookieUtil.SECONDS_OF_10YEAR, new BigDecimal(3650));
				
			}else if(CommonUtil.equals(BaseConstants.TEMPLATE_TYPE_CD_BRAND, tmpSearch.getTemplateTypeCd())){
				
			}
		}
		
		/*******************************************************************************
		 * 템플릿, 코너, 코너아이템 정보  조회
		 * templateId 필수 값
		 *******************************************************************************/
		if(tempYn){
			
			try{
				//템플릿,코너,아이템 정보 조회
				if (CommonUtil.isNotEmpty(brandId)) {	// 브랜드관 상품은 페이징 안함(ex. 브랜드관 메인 코디상품 & 추천상품)
					tmpSearch.setPagingYn("N");
				}
				tmpResult = displayCommonService.getTemplateInfo(tmpSearch);
				
				Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
				for (DmsTemplateDisplay tm : tmpResult.getDmsTemplateDisplays()) {
					
					//코너의 상품 페이징
					int maxPage = tm.getDmsDisplay().getTotalCount()/4;
					if (tm.getDmsDisplay().getTotalCount() % 4 != 0) {
						maxPage++;
					}
					tm.getDmsDisplay().setPageSize(maxPage);
					
					//템플릿의 코너정보를 Map으로 반환
					returnMap.put(tm.getDmsDisplay().getDisplayId(), tm.getDmsDisplay());
					//중카의 TEXT 배너의 카테고리 정보가 있는 코너인 경우
					if(CommonUtil.equals(Config.getString("category.corner.ctgBest"),tm.getDmsDisplay().getDisplayId())){
						List<PmsProduct> ctgPrdList = tm.getDmsDisplay().getDmsDisplayitems().get(0).getPmsProductList();
						model.addObject("prdList", ctgPrdList);
					}
				}
				model.addObject("cornerMap",returnMap);
				
			}catch(ServiceException e){
				logger.error(e.getMessage());
			}
			
			String viewName = tmpResult.getUrl();
			if (FoSessionUtil.isMobile() && viewName.indexOf("brand") > -1) {
				viewName += "_mo";
			}
			model.setViewName(viewName);
		}else{
			if(CommonUtil.equals(BaseConstants.TEMPLATE_TYPE_CD_DISPLAYCATEGORY, tmpSearch.getTemplateTypeCd())){
				if(currentCategory != null && currentCategory.size() > 0){
					if(CommonUtil.equals("Y", currentCategory.get(0).getLeafYn())){
						model.setViewName("/dms/template/categoryProduct");
					}else{
						model.setViewName(tmpResult.getUrl());
					}
				}
			}
		}
		
		return model;
	}
	
	
	/**
	 * 모바일 좌측 메뉴 - 브랜드 검색 목록
	 * @Method Name : searchScBrandAjax
	 * @author : roy
	 * @date : 2016. 9. 10.
	 * @description : 
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search/list/ajax", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView searchScBrandAjax(HttpServletRequest request, BrandSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/gsf/layout/page/inner/brandSearchList");

		search.setStoreId(SessionUtil.getStoreId());

		List<PmsBrand> list = displayCommonService.getBrandSearchList(search);

		// title 설정
		if (CommonUtil.isNotEmpty(search.getSearchKeyword())) { // keyword 검색
			mv.addObject("title", search.getSearchKeyword());
		} else if (CommonUtil.isNotEmpty(search.getConsonant())) { // 자음 검색
			mv.addObject("title", search.getConsonant());
		}
		mv.addObject("list", list);

		return mv;
	}
	
	/**
	 * 브랜드관 템플릿 브랜드별 클래스명
	 * 
	 * @Method Name : getBrandClassName
	 * @author : stella
	 * @date : 2016. 10. 5.
	 * @description : 브랜드별로 CSS 적용하기 위해 클래스명 지정(common.css 참조)
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getBrandClassName(String brandName) {
		Map<String, String> brandClassMap = new HashMap<>();
		
		brandClassMap.put("알로앤루", "brand1");
		brandClassMap.put("궁중비책", "brand2");
		brandClassMap.put("토미티피", "brand3");
		brandClassMap.put("와이볼루션", "brand4");
		brandClassMap.put("섀르반", "brand5");
		brandClassMap.put("알퐁소", "brand6");
		brandClassMap.put("츄즈", "brand7");
		brandClassMap.put("포래즈", "brand8");
		
		return brandClassMap;
	}

	/**
	 * 브랜드별 카테고리
	 * 
	 * @Method Name : getBrandClassName
	 * @author : stella
	 * @date : 2016. 10. 5.
	 * @description : Config.getString("[brandClassName]".root.category.id)
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private List<DmsDisplaycategory> getBrandCategory(String brandClassName) throws Exception {
		
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString(brandClassName + ".root.category.id"));
		
		List<DmsDisplaycategory> categoryList = categoryService.getDepth2CategoryList(search);
		if (CommonUtil.equals("brand4", brandClassName)) {
			for (DmsDisplaycategory category : categoryList) {
				if ("2".equals(category.getDepth())) {
					search.setDisplayCategoryId(category.getDisplayCategoryId());
					search.setAddCategoryId(Config.getString("brand.yvol.pms.category.id"));
					
					category.setDmsDisplaycategoryproducts(categoryService.getCategoryProduct(search));
				}
			}
		}
		List<DmsDisplaycategory> brandCategory = categoryService.getDepthCategoryList(search, categoryList);
		
		return brandCategory;
	}
	
	@RequestMapping(value = "/salesAssist/{brandId}", method = RequestMethod.GET)
	public ModelAndView salseAssistPage(@PathVariable("brandId") String brandId, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/template/salesAssist");
		
		logger.debug("===================================================");
		logger.debug("REQEUST BRAND_ID:"+request.getParameter("brandId"));
		logger.debug("===================================================");

		BrandSearch brandSearch = new BrandSearch();
		brandSearch.setStoreId(BaseConstants.STORE_ID);
		brandSearch.setBrandId(brandId);
		
		PmsBrand brandInfo = new PmsBrand();	//브랜드관 네비게이션, 브랜드 로고
		brandInfo = brandService.getBrandShopDetail(brandSearch);
		brandInfo.setSalesAssist("Y");
		
		String brandClassName = getBrandClassName(brandId).get(brandInfo.getName());
		mv.addObject("brandClassName", brandClassName);
		mv.addObject("brandCategory", getBrandCategory(brandClassName));
		mv.addObject("brandInfo", brandInfo);
		
		return mv;
	}
	
}
