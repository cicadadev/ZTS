package gcp.frontpc.controller.view.dms;

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
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.service.CommonService;
import gcp.ccs.service.OffshopService;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.CornerService;
import gcp.dms.service.SearchService;
import gcp.dms.service.SpecialService;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.vo.SearchCategoryList;
import gcp.external.searchApi.vo.SearchData;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealgroup;
import gcp.sps.model.search.SpsDealSearch;
import gcp.sps.service.DealService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("specialViewController")
@RequestMapping("dms/special")
public class SpecialController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private CornerService cornerService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SearchService searchService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private SpecialService	specialService;

	@Autowired
	private OffshopService offshopService;
	
	@Autowired
	private DealService dealService;

	@Autowired
	private MemberService memberService;

	
	/**
	 * [모바일]분유관, 출산준비관 카테고리 상품목록과 , 코너의 아이템 목록 조회
	 * @Method Name : getSpecialCategory
	 * @author : emily
	 * @date : 2016. 9. 26.
	 * @description : 
	 *			1depth 카테고리 변경시 2카테고리 목록과 코너의 아이템 정보 조회한다.
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/specialCategory")
	public ModelAndView getSpecialCategory(DmsDisplaySearch search , HttpServletRequest request)throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		/**********************************************************************************
		 * 공통 파라미터 & searchModel 설정
		 **********************************************************************************/
		boolean isMoblie = FoSessionUtil.isMobile(request);
		search.setStoreId(SessionUtil.getStoreId());
		if(CommonUtil.equals(BaseConstants.SPCEIAL_VIEW_TYPE_BIRTHREADY, search.getCategoryViewType())){
			search.setRootCategoryId(Config.getString("root.birthready.category.id"));
		}else if(CommonUtil.equals(BaseConstants.SPCEIAL_VIEW_TYPE_MILKPOWDER, search.getCategoryViewType())){
			search.setRootCategoryId(Config.getString("root.milkpowder.category.id"));
		}
		//reqeust 1depth 카테고리
		String upperDisplayCategoryId = CommonUtil.replaceNull(search.getDisplayCategoryId(), "");
		//2depth 카테고리 명
		String displayCategoryName="";
		String displayCategoryId = "";
		DmsDisplaycategory categoryDepth = new DmsDisplaycategory();
		
		/**********************************************************************************
		 * 2depth 카테고리 목록 조회
		 **********************************************************************************/
		if(CommonUtil.isNotEmpty(upperDisplayCategoryId)){
			
			//카테고리 전체의 상품을 조회한다.
			if(CommonUtil.equals("ALL", upperDisplayCategoryId)){
				List<String> categoryIdList = new ArrayList<String>();
				search.setDisplayCategoryId("");
				
				List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(search);
				if(categoryList != null && categoryList.size() > 0){
					for (DmsDisplaycategory ctg : categoryList) {
						if((Integer.parseInt(ctg.getDepth()) == 1 && CommonUtil.equals("Y", ctg.getLeafYn())) 
								|| Integer.parseInt(ctg.getDepth()) == 2 && CommonUtil.equals("Y", ctg.getLeafYn())){
							categoryIdList.add(ctg.getDisplayCategoryId());
						}
					}
				}
				
				search.setUpperDisplayCategoryId("ALL");
				displayCategoryId =categoryIdList.toString();
				displayCategoryName =search.getName();
			}else{
				//카테고리의 상품을 조회한다.
				List<DmsDisplaycategory> ctgList = categoryService.getDepth2CategoryList(search);
				if(ctgList != null && ctgList.size() > 0){
					DmsDisplaycategory category = ctgList.get(0);
					
					if(category.getDmsDisplaycategorys() != null && category.getDmsDisplaycategorys().size() > 0){
						displayCategoryId = category.getDmsDisplaycategorys().get(0).getDisplayCategoryId();
						displayCategoryName = category.getDmsDisplaycategorys().get(0).getName();
						categoryDepth = ctgList.get(0);
						
					}else{
						
						if(CommonUtil.equals("Y", category.getLeafYn())){
							displayCategoryId = category.getDisplayCategoryId();
							displayCategoryName = category.getName();
						}
					}
				}
			}
			
			search.setName(displayCategoryName);
			search.setUpperDisplayCategoryId(upperDisplayCategoryId);
			search.setDisplayCategoryId(displayCategoryId);
			model.addObject("categorySearch", search);
			model.addObject("category", categoryDepth);
		}
		
		/**********************************************************************************
		 * return View
		 **********************************************************************************/
		if(isMoblie){
			model.setViewName("/dms/include/categorySelete");
		}else{
			model.setViewName("/dms/include/categoryList");
		}
		
		return model;
	}
	
	/**
	 * [PC] 분유관, 출산 준비관  카테고리 상품목록과 , 코너의 아이템 목록 조회
	 * @Method Name : getCategoryProduct
	 * @author : intune
	 * @date : 2016. 11. 20.
	 * @description : 
	 *
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/categoryProduct",method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getCategoryProduct( DmsDisplaySearch search, HttpServletRequest request)throws Exception{
		ModelAndView model = new ModelAndView("/dms/special/inner/spcSearchProductList");
		
		logger.debug("====================================");
		logger.debug(""+search.getDisplayCategoryId());
		logger.debug("====================================");
		
		/**********************************************************************************
		 * 공통 파라미터 
		 **********************************************************************************/
		search.setStoreId(SessionUtil.getStoreId());
		if(CommonUtil.equals(BaseConstants.SPCEIAL_VIEW_TYPE_BIRTHREADY, search.getCategoryViewType())){
			search.setRootCategoryId(Config.getString("root.birthready.category.id"));
		}else if(CommonUtil.equals(BaseConstants.SPCEIAL_VIEW_TYPE_MILKPOWDER, search.getCategoryViewType())){
			search.setRootCategoryId(Config.getString("root.milkpowder.category.id"));
		}
		//reqeust 1depth 카테고리
		String upperDisplayCategoryId = CommonUtil.replaceNull(search.getDisplayCategoryId(), "");
		
		//2depth 카테고리 명
		String displayCategoryId = "";
		DmsDisplaycategory categoryDepth = new DmsDisplaycategory();
		List<DmsDisplaycategory> leafCategoryList = new ArrayList<DmsDisplaycategory>();
		
		//검섹엔진 카테고리 목록 파라미터
		List<String> categoryIdList = new ArrayList<String>();
		//검색엔진 파라미터 
		SearchApiSearch apiParam = new SearchApiSearch();
		//키워드검색을 제외한 화면은 Y 설정
		apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
		
		/**********************************************************************************
		 * 분유관 카테고리 전체 상품 목록 조회
		 * upperDisplayCategoryId : 파라미터로 넘어온 카테고리ID
		 **********************************************************************************/
		if(CommonUtil.isNotEmpty(upperDisplayCategoryId)){
			
			//카테고리 전체의 상품을 조회한다.
			if(CommonUtil.equals("ALL", upperDisplayCategoryId)){
				search.setDisplayCategoryId("");
				
				//전문관 전체 카테고리 목록 조회
				List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(search);
				if(categoryList != null && categoryList.size() > 0){
					for (DmsDisplaycategory ctg : categoryList) {
						if(Integer.parseInt(ctg.getDepth()) == 1 && CommonUtil.equals("Y", ctg.getLeafYn())){ 
							categoryIdList.add(ctg.getDisplayCategoryId());
						}else if(Integer.parseInt(ctg.getDepth()) == 2 && CommonUtil.equals("Y", ctg.getLeafYn())){
						//}else if(Integer.parseInt(ctg.getDepth()) == 2){
							categoryIdList.add(ctg.getDisplayCategoryId());
						}
					}
				}
				displayCategoryId =categoryIdList.toString();
				
				/*******************************************************************
				 * 검색API 호출
				 *******************************************************************/
				//apiParam.setPageSize(40);
				apiParam.setCategoryIdList(categoryIdList);
				
				SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
				apiParam.setTotalCount(searchApi.getTotalCount());
				
				model.addObject("searchApi", searchApi);
				model.addObject("search", apiParam);
			}else{
				
				/**********************************************************************************
				 * 2depth 카테고리 목록 조회
				 * upperDisplayCategoryId : 파라미터로 넘어온 카테고리ID
				 **********************************************************************************/
				List<SearchData> resultValue = new ArrayList<SearchData>();
				//카테고리의 상품을 조회한다.
				List<DmsDisplaycategory> ctgList = categoryService.getDepth2CategoryList(search);
				
				if(ctgList != null && ctgList.size() > 0){
					DmsDisplaycategory category = ctgList.get(0);
					
					if(category.getDmsDisplaycategorys() != null && category.getDmsDisplaycategorys().size() > 0){
						for (DmsDisplaycategory dms : category.getDmsDisplaycategorys()) {
							
							if(CommonUtil.equals("Y", dms.getLeafYn())){
								categoryIdList.add(dms.getDisplayCategoryId());
								displayCategoryId = categoryIdList.toString();
								leafCategoryList.add(dms);
							}
						}
						//리프 카테고리 목록 추출
						categoryDepth.setDmsDisplaycategorys(leafCategoryList);
						
					}else{
						
						if(CommonUtil.equals("Y", category.getLeafYn())){
							displayCategoryId = category.getDisplayCategoryId();
							categoryIdList.add(displayCategoryId);
						}
					}
	
					for (String categoryId : categoryIdList) {
						
						//검색엔진 호출
						List<String> categoryIds = new ArrayList<String>();
						categoryIds.add(categoryId);
						apiParam.setCategoryIdList(categoryIds);
						apiParam.setPageSize(80);
						apiParam.setPagingYn("N");
						
						SearchData result = searchService.getSearchApiPrdList(apiParam);
						apiParam.setTotalCount(result.getTotalCount());
						
						//현제 카테고리 정보
						search.setDisplayCategoryId(categoryId);
						List<DmsDisplaycategory> currentCategory = categoryService.getAllCategoryList(search);
						if(currentCategory != null && currentCategory.size() > 0){
							List<SearchCategoryList> ctList = new ArrayList<SearchCategoryList>();
							DmsDisplaycategory current = currentCategory.get(0);
							SearchCategoryList categoryIdList1 = new SearchCategoryList();
							categoryIdList1.setCategoryId(current.getDisplayCategoryId());
							categoryIdList1.setCategoryName(current.getName());
							
							ctList.add(categoryIdList1);
							result.setCategoryList(ctList);
						}
						
						if(result != null && result.getTotalCount() > 0){
							resultValue.add(result);
						}
					}
					
					model.addObject("searchPrd", resultValue);
					model.addObject("search", apiParam);
				}
			
			}
			
			search.setLinkType("S");
			search.setCategoryViewType(BaseConstants.SPCEIAL_VIEW_TYPE_MILKPOWDER);
			search.setUpperDisplayCategoryId(upperDisplayCategoryId);
			search.setDisplayCategoryId(displayCategoryId);
			model.addObject("categorySearch", search);
			model.addObject("category", categoryDepth);
			
			/**********************************************************************************
			 * 분유관 브랜드 소개 코너 조회
			 **********************************************************************************/
			if(CommonUtil.equals(BaseConstants.SPCEIAL_VIEW_TYPE_MILKPOWDER, search.getCategoryViewType())){
				
				DmsDisplaySearch search1 = new DmsDisplaySearch();
				search1.setStoreId(SessionUtil.getStoreId());
				//search.setDisplayItemDivId(categoryId);
				search1.setDisplayId(Config.getString("corner.special.milk1.img.2"));
				List<DmsDisplay> cornerList = cornerService.getCommonCornerList(search1);
				
				if(cornerList != null && cornerList.size() > 0){
					DmsDisplay cornerItem = cornerList.get(0);
					
					if(cornerItem.getDmsDisplayitems() != null && cornerItem.getDmsDisplayitems().size() > 0){
						Map<String, DmsDisplayitem> returnMap = new HashMap<String, DmsDisplayitem>();
						for (DmsDisplayitem item : cornerItem.getDmsDisplayitems()) {
							returnMap.put(item.getDisplayItemDivId(), item);			
						}
						model.addObject("cornerMap",returnMap);
					}
				}
			}
		}
		
		return model;
	}
	
	/**
	 * 출산 준비관
	 * @Method Name : birthready
	 * @author : emily
	 * @date : 2016. 9. 24.
	 * @description : 
	 *	상품 목록은 검색엔진을 사용한다 그래서 파라미터는 이곳에서 설정한다.
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/birthready")
	public ModelAndView birthready(DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response){
		
		/*******************************************************************
		 * 공통변수 설정
		 *******************************************************************/
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		List<String> categoryIdList = new ArrayList<String>();
		
		search.setStoreId(SessionUtil.getStoreId());
		
		/*******************************************************************
		 * 코너 조회
		 * - map으로 반환
		 *******************************************************************/
		String displayID = Config.getString("corner.special.ready.img.1");
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		search.setDisplayId(displayID);
		List<DmsDisplay> conerList = cornerService.getCommonCornerList(search);
		DmsDisplay corner = conerList.get(0);
		
		if(corner.getDmsDisplayitems() != null && corner.getDmsDisplayitems().size() > 0){
			Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
			corner.setTotalCount(corner.getDmsDisplayitems().size());
			returnMap.put(corner.getDisplayId(), corner);
			mv.addObject("cornerMap",returnMap);
		}
		
		/*******************************************************************
		 * 카테고리 조회
		 * - 출산준비관 카테고리 조회 : rootCategoryId=>00002
		 *******************************************************************/
		search.setRootCategoryId(Config.getString("root.birthready.category.id"));
		List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(search);
		List<DmsDisplaycategory> depth1 = new ArrayList<DmsDisplaycategory>();
		
		if(categoryList != null && categoryList.size() > 0){
			for (DmsDisplaycategory ctg : categoryList) {
				
				if(Integer.parseInt(ctg.getDepth()) == 1){
					depth1.add(ctg);
					if(CommonUtil.equals("Y", ctg.getLeafYn())){
						categoryIdList.add(ctg.getDisplayCategoryId());
					}
				}else if(Integer.parseInt(ctg.getDepth()) == 2 && CommonUtil.equals("Y", ctg.getLeafYn())){
					categoryIdList.add(ctg.getDisplayCategoryId());
				}
			}
		}
		//대카테고리 전체
		mv.addObject("depth1", depth1);

		search.setLinkType("S");
		search.setCategoryViewType(BaseConstants.SPCEIAL_VIEW_TYPE_BIRTHREADY);
		search.setUpperDisplayCategoryId("ALL");
		mv.addObject("categorySearch", search);
		
		/*******************************************************************
		 * 검색API 호출
		 *******************************************************************/
		SearchApiSearch apiParam = new SearchApiSearch();
		apiParam.setCategoryIdList(categoryIdList);
		//키워드검색을 제외한 화면은 Y 설정
		apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
		//카테고리매장일경우 구별하기위함.
		SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
		apiParam.setTotalCount(searchApi.getTotalCount());
		mv.addObject("searchApi", searchApi);
		mv.addObject("search", apiParam);
		
		return mv;
		
	}
	
	/**
	 * 분유관
	 * @Method Name : milkpowder
	 * @author : emily
	 * @date : 2016. 9. 24.
	 * @description : 
	 *	상품 목록은 검색엔진을 사용한다 그래서 파라미터는 이곳에서 설정한다.
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/milkPowder")
	public ModelAndView milkpowder(DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response){
		
		/*******************************************************************
		 * 공통변수 설정
		 *******************************************************************/
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		List<String> categoryIdList = new ArrayList<String>();
		
		search.setStoreId(SessionUtil.getStoreId());
		
		/*******************************************************************
		 * 분유관 상단 이미지 배너 코너  아이템 조회
		 * - map으로 반환
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.milk1.img.1"));
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		List<DmsDisplay> conerList = cornerService.getCommonCornerList(search);
		
		if(conerList !=null && conerList.size() > 0){
			DmsDisplay corner = conerList.get(0);
			if(corner.getDmsDisplayitems() != null && corner.getDmsDisplayitems().size() > 0){
				Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
				corner.setTotalCount(corner.getDmsDisplayitems().size());
				returnMap.put(corner.getDisplayId(), corner);
				mv.addObject("cornerMap",returnMap);
			}
		}
		
		/*******************************************************************
		 * 분유관 상품코너목록 조회
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.milk2"));
		List<DmsDisplay> prdCornerList = cornerService.getChildCornerList(search);
		if(prdCornerList != null && prdCornerList.size() > 0){
			mv.addObject("prdCornerList", prdCornerList);
		}
		
		/*******************************************************************
		 * 카테고리 조회
		 * - 출산준비관 카테고리 조회 : rootCategoryId=>00002
		 *******************************************************************/
		search.setRootCategoryId(Config.getString("root.milkpowder.category.id"));
		List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(search);
		List<DmsDisplaycategory> depth1 = new ArrayList<DmsDisplaycategory>();
		
		if(categoryList != null && categoryList.size() > 0){
			for (DmsDisplaycategory ctg : categoryList) {
				
				if(Integer.parseInt(ctg.getDepth()) == 1){
					depth1.add(ctg);
					if(CommonUtil.equals("Y", ctg.getLeafYn())){
						categoryIdList.add(ctg.getDisplayCategoryId());
					}
				}else if(Integer.parseInt(ctg.getDepth()) == 2 && CommonUtil.equals("Y", ctg.getLeafYn())){
				//}else if(Integer.parseInt(ctg.getDepth()) == 2){
					categoryIdList.add(ctg.getDisplayCategoryId());
				}
			}
		}
		//대카테고리 전체
		mv.addObject("depth1", depth1);

		search.setLinkType("S");
		search.setCategoryViewType(BaseConstants.SPCEIAL_VIEW_TYPE_MILKPOWDER);
		search.setUpperDisplayCategoryId("ALL");
		mv.addObject("categorySearch", search);
		
		/*******************************************************************
		 * 검색API 호출
		 *******************************************************************/
		SearchApiSearch apiParam = new SearchApiSearch();
		//apiParam.setPageSize(40);
		apiParam.setCategoryIdList(categoryIdList);
		//키워드검색을 제외한 화면은 Y 설정
		apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
		//카테고리매장일경우 구별하기위함.
		SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
		apiParam.setTotalCount(searchApi.getTotalCount());
		mv.addObject("searchApi", searchApi);
		mv.addObject("search", apiParam);
		
		return mv;
		
	}


	/**
	 * @Method Name : isPremium
	 * @author : ian
	 * @date : 2016. 10. 13.
	 * @description : 프리미엄 인증 전 / 후 분기
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/premium" ,method = RequestMethod.GET)
	public ModelAndView isPremium(SpsDealSearch dealSearch, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String url = "/dms/special/specialPremiumBefore";
		String premiumYn = BaseConstants.YN_N;
		boolean result = false;		// 비회원만 프리미엄 멤버쉽 안내가 있음
		
		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
		
		// 로그인 On && 멤버쉽관 회원
		if (loginInfo != null) {
			premiumYn = memberService.getMemberPremiumYn(loginInfo.getMemberNo());
			if(BaseConstants.YN_Y.equals(premiumYn)) {
				url = "/dms/special/specialPremiumAfter";
				result = true;
				
				// 멤버쉽관 ID 조회
				dealSearch.setStoreId(SessionUtil.getStoreId());
				dealSearch.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_PREMIUM);
				dealSearch = getDealSearch(dealSearch);
				
				// 딜 구분타이틀 1&2 depth 리스트 조회 (회원일때에만)
				List<SpsDealgroup> dealGroups = new ArrayList<SpsDealgroup>();
				dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(dealSearch);
				mv.addObject("depthList", dealGroups);
				
				boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
				if(isMobile) {
					dealSearch.setPageSize(10);
				} else {
					dealSearch.setPagingYn(BaseConstants.YN_N);
				}
				
				// 맴버쉽 상품
				List<PmsProduct> productList = (List<PmsProduct>) commonService.getDealProductLowestPriceList(dealSearch);

				// paging
				if (productList != null && productList.size() > 0) {
					mv.addObject("totalCount", productList.get(0).getTotalCount());
				} else {
					mv.addObject("totalCount", 0);
				}
				
				mv.addObject("productList", productList);
			}
		}
		
		if(!result) {
			/*******************************************************************
			 * 맴버쉽관 배너 조회 (비회원)
			 *******************************************************************/
			DmsDisplaySearch bnrSearch = new DmsDisplaySearch();
			bnrSearch.setStoreId(SessionUtil.getStoreId());
			bnrSearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
			List<DmsDisplay> conerList = (List<DmsDisplay>) cornerService.getCommonCornerList(bnrSearch);

			Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
			for (DmsDisplay dmsDisplay : conerList) {
				returnMap.put(dmsDisplay.getDisplayId(), dmsDisplay);
			}
			mv.addObject("cornerMap", returnMap);
		}
		
		mv.setViewName(url);
		
		return mv;
	}
	
	/**
	 * @Method Name : premiumBeforeContentAjax
	 * @author : ian
	 * @date : 2016. 11. 8.
	 * @description : 프리미엄 인증전 상품 조회
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/premium/before/content/ajax" ,method = { RequestMethod.GET })
	public ModelAndView premiumBeforeContentAjax(SpsDealSearch dealSearch, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/dms/special/inner/premiumBeforeProduct");

		// 멤버쉽관 딜 id 조회
		dealSearch.setStoreId(SessionUtil.getStoreId());
		dealSearch.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_PREMIUM);
		dealSearch = getDealSearch(dealSearch);

		boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
		if(isMobile) {
			dealSearch.setPageSize(10);
		} else {
			dealSearch.setPagingYn(BaseConstants.YN_N);
		}
		
		// 맴버쉽 상품 구분타이틀별 상품 두개씩 출력
		dealSearch.setMemGradeCd("MEM_GRADE_CD.WELCOME");	// 비회원일 경우 welcome 가
		dealSearch.setOneItem(BaseConstants.YN_Y); 
		List<PmsProduct> productList = (List<PmsProduct>) commonService.getDealProductLowestPriceList(dealSearch);

		// paging
		if (productList != null && productList.size() > 0) {
			mv.addObject("totalCount", productList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		mv.addObject("productList", productList);
		
		return mv;
	}
	
	/**
	 * @Method Name : premiumAfterProductAjax
	 * @author : ian
	 * @date : 2016. 11. 8.
	 * @description : 프리미엄 인증후 상품 조회 
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/premium/after/productList/ajax" ,method = { RequestMethod.GET })
	public ModelAndView premiumAfterProductAjax(SpsDealSearch search, HttpServletRequest request) throws Exception {
		
		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
		// 비 프리미엄 회원
		if (loginInfo == null || BaseConstants.YN_N.equals(memberService.getMemberPremiumYn(loginInfo.getMemberNo()))) {
			return (ModelAndView) new ModelAndView("redirect:/ccs/common/main");
		}
		
		ModelAndView mv = new ModelAndView("/dms/special/inner/premiumAfterProduct");

		// scroll 여부
		if(search.getCurrentPage() > 1) {
			mv.addObject("isScroll", true);
		} else {
			mv.addObject("isScroll", false);
		}
		
		search.setStoreId(SessionUtil.getStoreId());
		search.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_PREMIUM);
		search = getDealSearch(search);
		
		boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
		if(isMobile) {
			search.setPageSize(10);
		} else {
			search.setPagingYn(BaseConstants.YN_N);
		}
		
		// 딜 구분타이틀 1&2 depth 리스트 조회
		List<SpsDealgroup> dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(search);
		mv.addObject("depthList", dealGroups);
		
		// 맴버쉽 상품
		List<PmsProduct> productList = (List<PmsProduct>) commonService.getDealProductLowestPriceList(search);

		// paging
		if (productList != null && productList.size() > 0) {
			mv.addObject("totalCount", productList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		
		mv.addObject("productList", productList);
		mv.addObject("search", search);
		
		return mv;
	}
	
	/**
	 * @Method Name : getDealSearch
	 * @author : ian
	 * @date : 2016. 11. 8.
	 * @description : 딜 아이디 조회 
	 *
	 * @param dealTypeCd
	 * @return
	 * @throws Exception
	 */
	private SpsDealSearch getDealSearch(SpsDealSearch deal) throws Exception {
		List<SpsDeal> dealList = (List<SpsDeal>) dealService.getDealList(deal);
		deal.setDealId(dealList.get(0).getDealId());
		
		return deal;
	}

	/**
	 * @Method Name : giftShop
	 * @author : ian
	 * @date : 2016. 9. 27.
	 * @description : 기프트샵
	 *
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/giftShop")
	public ModelAndView giftShop(DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv = new ModelAndView("/dms/special/specialGiftShopList");

		search.setStoreId(SessionUtil.getStoreId());
		/*******************************************************************
		 * 기프트샵 키비주얼 배너 조회
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.gift.html.1"));
		List<DmsDisplay> cornerList = cornerService.getCommonCornerList(search);
		mv.addObject("keyVisualBanner", cornerList);

		/*******************************************************************
		 * 기프트샵 시즌상품 코너 조회
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.gift"));
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		List<DmsDisplay> seasonPrdConerList = cornerService.getChildCornerList(search);
		mv.addObject("seasonPrdConerList", seasonPrdConerList);
		
		/*******************************************************************
		 * 테마상품 조회
		 *******************************************************************/
		search.setPagingYn(BaseConstants.YN_N);
		List<PmsProduct> themeProductList = specialService.getThemeProductList(search);
		mv.addObject("themeProductList", themeProductList);
		
		return mv;
	}

	/**
	 * 
	 * @Method Name : giftShopProductList
	 * @author : allen
	 * @date : 2016. 10. 4.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/giftShop/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView giftShopProductList(@RequestBody DmsDisplaySearch search, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/dms/special/inner/giftShopProductList");
		
		if (FoSessionUtil.isMobile()) {
			search.setCurrentPage(search.getCurrentPage() + 1);
		} else {
			if (search.getCurrentPage() == 0) {
				search.setCurrentPage(1);
			}
		}

		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		search.setMemGradeCd((String) FoSessionUtil.getMemGradeCd());	//회원등급
		if ("theme".equals(search.getDisplayType())) {
			List<DmsDisplaycategory> categoryList = specialService.getThemeCategoryNproductList(search);
			mv.addObject("categoryList", categoryList);
			
		} else if ("corner".equals(search.getDisplayType())) {
			List<PmsProduct> cornerPrdList = specialService.getGiftShopCornerPrdList(search);
			mv.addObject("cornerPrdList", cornerPrdList);
		} else {

			if (FoSessionUtil.isMobile(request)) {
				search.setPageSize(20);
			}

			search.setPagingYn(BaseConstants.YN_Y);
			List<PmsProduct> themeProductList = specialService.getThemeProductList(search);
			mv.addObject("themeProductList", themeProductList);

			// paging
			if (themeProductList != null && themeProductList.size() > 0) {
				mv.addObject("totalCount", themeProductList.get(0).getTotalCount());
			} else {
				mv.addObject("totalCount", 0);
			}
		}
		mv.addObject("search", search);
		return mv;
	}

	@RequestMapping(value = "/giftShop/list/ajax2", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView giftShopProductList2(DmsDisplaySearch search, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/dms/special/inner/giftShopProductList");

		if (FoSessionUtil.isMobile()) {
			search.setCurrentPage(search.getCurrentPage() + 1);
		} else {
			if (search.getCurrentPage() == 0) {
				search.setCurrentPage(1);
			}
		}

		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		search.setMemGradeCd((String) FoSessionUtil.getMemGradeCd());	//회원등급
		if ("theme".equals(search.getDisplayType())) {
			List<DmsDisplaycategory> categoryList = specialService.getThemeCategoryNproductList(search);
			mv.addObject("categoryList", categoryList);

		} else if ("corner".equals(search.getDisplayType())) {
			List<PmsProduct> cornerPrdList = specialService.getGiftShopCornerPrdList(search);
			mv.addObject("cornerPrdList", cornerPrdList);
		} else {

			if (FoSessionUtil.isMobile(request)) {
				search.setPageSize(20);
			}

			search.setPagingYn(BaseConstants.YN_Y);
			List<PmsProduct> themeProductList = specialService.getThemeProductList(search);
			mv.addObject("themeProductList", themeProductList);

			// paging
			if (themeProductList != null && themeProductList.size() > 0) {
				mv.addObject("totalCount", themeProductList.get(0).getTotalCount());
			} else {
				mv.addObject("totalCount", 0);
			}
		}
		mv.addObject("search", search);
		return mv;
	}

	/**
	 * 정기배송관
	 * @Method Name : subscription
	 * @author : allen
	 * @date : 2016. 9. 30.
	 * @description : 정기배송관
	 *
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/subscription")
	public ModelAndView subscription(DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/dms/special/specialSubscriptionList");

		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		/*******************************************************************
		 * 정기배송관 메인 배너 코너 조회
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.regul.html.1"));
		List<DmsDisplay> cornerList = cornerService.getCommonCornerList(search);
		mv.addObject("cornerList", cornerList);

		/*******************************************************************
		 * 패키지 코너 조회
		 *******************************************************************/
		search.setDisplayId(Config.getString("corner.special.regul"));
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		List<DmsDisplay> packageConerList = cornerService.getChildCornerList(search);
		mv.addObject("packageConerList", packageConerList);

		/*******************************************************************
		 * 정기배송 상품의 중카테고리 조회
		 *******************************************************************/
		List<DmsDisplaycategory> twoDepthCateList = specialService.getSubscriptionPrdList(search);
		mv.addObject("twoDepthCateList", twoDepthCateList);

		/*******************************************************************
		 * 검색API 호출
		 *******************************************************************/
		SearchApiSearch apiParam = new SearchApiSearch();

		//키워드검색을 제외한 화면은 Y 설정
		apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
		apiParam.setRegularDeliveryYn(BaseConstants.YN_Y);
		apiParam.setPageSize(100);
		SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
		mv.addObject("searchApi", searchApi);

		return mv;
	}

	@RequestMapping(value = "/subscription/list/ajax")
	public ModelAndView subscriptionProductList(@RequestBody DmsDisplaySearch search, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/dms/special/inner/subscriptionProductList");

		SearchApiSearch apiParam = new SearchApiSearch();
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		if ("category".equals(search.getDisplayType())) {

			/*******************************************************************
			 * 검색API 호출
			 *******************************************************************/
			List<String> categoryIds = new ArrayList<String>();
			DmsDisplaySearch ctgrSearch = new DmsDisplaySearch();
			ctgrSearch.setStoreId(SessionUtil.getStoreId());
			ctgrSearch.setDisplayCategoryId(search.getDisplayCategoryId());
			ctgrSearch.setRootCategoryId(Config.getString("root.display.category.id"));

			List<DmsDisplaycategory> ctgList = categoryService.getDepth2CategoryList(ctgrSearch);

			if (ctgList.size() > 0) {
				for (DmsDisplaycategory info : ctgList.get(0).getDmsDisplaycategorys()) {
					categoryIds.add(info.getDisplayCategoryId());
				}
			}
			apiParam.setCategoryIdList(categoryIds);

			apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
			apiParam.setCategoryIdList(categoryIds);
			apiParam.setRegularDeliveryYn(BaseConstants.YN_Y);
			apiParam.setPageSize(100);

			if (StringUtils.isNotEmpty(search.getSort())) {

				if (search.getSort().equals("PRODUCT_SORT_CD.POPULAR")) {
					apiParam.setSort("ORDER_QTY");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.LATEST")) {
					apiParam.setSort("DATE");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.REVIEW")) {
					apiParam.setSort("RATING");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.LOWPRICE")) {
					apiParam.setSort("REGULAR_DELIVERY_PRICE");
					apiParam.setDirection("ASC");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.HIGHPRICE")) {
					apiParam.setSort("REGULAR_DELIVERY_PRICE");
					apiParam.setDirection("DESC");
				}
			}

			SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
			mv.addObject("searchApi", searchApi);

			mv.addObject("search", apiParam);
		} else if ("corner".equals(search.getDisplayType())) {
			search.setRegularDeliveryYn(BaseConstants.YN_Y);
			List<DmsDisplay> cornerList = cornerService.getCommonCornerList(search);
			mv.addObject("cornerPrdList", cornerList);
		} else {
			/*******************************************************************
			 * 검색API 호출
			 *******************************************************************/

			if (StringUtils.isNotEmpty(search.getSort())) {
				
				if (search.getSort().equals("PRODUCT_SORT_CD.POPULAR")) {
					apiParam.setSort("ORDER_QTY");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.LATEST")) {
					apiParam.setSort("DATE");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.REVIEW")) {
					apiParam.setSort("RATING");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.LOWPRICE")) {
					apiParam.setSort("REGULAR_DELIVERY_PRICE");
					apiParam.setDirection("ASC");
				} else if (search.getSort().equals("PRODUCT_SORT_CD.HIGHPRICE")) {
					apiParam.setSort("REGULAR_DELIVERY_PRICE");
					apiParam.setDirection("DESC");
				}
			}

//			apiParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_SPECIAL);
			apiParam.setRegularDeliveryYn(BaseConstants.YN_Y);
			apiParam.setPageSize(100);
			SearchData searchApi = searchService.getSearchApiPrdList(apiParam);
			mv.addObject("searchApi", searchApi);
		}
		mv.addObject("search", apiParam);
		mv.addObject("displaySearch", search);
		return mv;
	}

	/**
	 * 
	 * @Method Name : pickup
	 * @author : intune
	 * @date : 2016. 10. 3.
	 * @description : 매장픽업관
	 *
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pickup")
	public ModelAndView pickup(DmsDisplaySearch search, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		search.setRootCategoryId(Config.getString("root.display.category.id"));

		// 매장픽업관 배너 조회
		String pickupCornerId = Config.getString("corner.special.pickup.html.1");
		search.setDisplayId(pickupCornerId);
		search.setStoreId(SessionUtil.getStoreId());
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		List<DmsDisplay> cornerList = cornerService.getCommonCornerList(search);
		mv.addObject("keyVisualBanner", cornerList);

		// 브랜드 리스트 조회
		List<PmsBrand> brandList = specialService.getPickupBrandList(search);
		mv.addObject("brandList", brandList);

		// 브랜드의 카테고리 및 상품 리스트 조회
		if (brandList != null && brandList.size() > 0) {
			List<DmsDisplaycategory> categoryList = specialService.getPickupInfoList(search);
			mv.addObject("categoryList", categoryList);
		}

		mv.addObject("search", search);

		return mv;
	}
	
	/**
	 * 
	 * @Method Name : pickup
	 * @author : allen
	 * @date : 2016. 10. 1.
	 * @description : 매장픽업 상품 검색리스트
	 *
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pickup/list/ajax", method = RequestMethod.POST)
	public ModelAndView pickupListAjax(@RequestBody DmsDisplaySearch search, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/dms/special/inner/pickupProductList");
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		if ("all".equals(search.getDisplayType())) {
			search.setPageSize(40);
		} else {
			search.setPagingYn(BaseConstants.YN_N);
		}
		if (StringUtils.isNotEmpty(search.getInterestOffshopPrdView())) {
			if (BaseConstants.YN_Y.equals(search.getInterestOffshopPrdView())) {
				search.setMemberNo(SessionUtil.getMemberNo());
			}
		}

		List<DmsDisplaycategory> categoryList = specialService.getPickupInfoList(search);
		// paging
		if (categoryList != null && categoryList.size() > 0) {
			mv.addObject("totalCount", categoryList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		mv.addObject("categoryList", categoryList);
		mv.addObject("search", search);
		return mv;
	}

	@RequestMapping(value = "/employee" ,method = { RequestMethod.GET })
	public ModelAndView employ(DmsDisplaySearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/dms/special/specialEmployee");
		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
		
		/*******************************************************************
		 * 로그인 및 임직원여부 분기
		 *******************************************************************/
		// 비임직원
		if (loginInfo == null || "N".equals(loginInfo.getEmployeeYn())) {
			return (ModelAndView) new ModelAndView("redirect:/ccs/common/main");
		}
		// 로그인 On && 임직원여부
		else {
			String sotreId = SessionUtil.getStoreId();
			
			/*******************************************************************
			 * 임직원관 배너 조회
			 *******************************************************************/
			// 배너 호출
			DmsDisplaySearch bnrSearch = new DmsDisplaySearch();
			bnrSearch.setStoreId(SessionUtil.getStoreId());
			bnrSearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
			List<DmsDisplay> conerList = (List<DmsDisplay>) cornerService.getCommonCornerList(bnrSearch);
			
			Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
			for (DmsDisplay dmsDisplay : conerList) {
				returnMap.put(dmsDisplay.getDisplayId(), dmsDisplay);
			}
			mv.addObject("cornerMap", returnMap);
			
			/*******************************************************************
			 * 임직원관 구분타이틀(딜코너) 및 상품리스트 조회
			 *******************************************************************/
			// 임직원관 딜 id 조회
			SpsDealSearch dealSearch = new SpsDealSearch();
			dealSearch.setStoreId(sotreId);
			dealSearch.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_EMPLOYEE);
			List<SpsDeal> dealList = (List<SpsDeal>) dealService.getDealList(dealSearch);
			dealSearch.setDealId(dealList.get(0).getDealId());
			
			
			List<SpsDealgroup> dealGroups = new ArrayList<SpsDealgroup>();
			
			// 딜 구분타이틀 1&2 depth 리스트 조회
			dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(dealSearch);
			mv.addObject("depthList", dealGroups);
			
			boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
			if(isMobile) {
				dealSearch.setPageSize(10);
			} else {
				// 최초 페이징 40 unit
				dealSearch.setPageSize(40);
			}
			mv.addObject("productSortTypeCd", "PRODUCT_SORT_CD.POPULAR");
			
			// 임직원 상품
			List<PmsProduct> productList = (List<PmsProduct>) commonService.getDealProductLowestPriceList(dealSearch);

			// paging
			if (productList != null && productList.size() > 0) {
				mv.addObject("totalCount", productList.get(0).getTotalCount());
			} else {
				mv.addObject("totalCount", 0);
			}
			

			mv.addObject("productList", productList);
			mv.addObject("search", search);

			return mv;
		}
	}
	
	@RequestMapping(value = "/employee/productList/ajax" ,method = { RequestMethod.GET })
	public ModelAndView employeeContentAjax(SpsDealSearch search, HttpServletRequest request) throws Exception {
		String sotreId = SessionUtil.getStoreId();
		
		ModelAndView mv = new ModelAndView("/dms/special/inner/employeeProduct");

		// 임직원관 딜 id 조회
		search.setStoreId(sotreId);
		search.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_EMPLOYEE);
		List<SpsDeal> dealList = (List<SpsDeal>) dealService.getDealList(search);
		search.setDealId(dealList.get(0).getDealId());
		
		boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
		if(isMobile) {
			search.setPageSize(10);
		}
		
		// scroll 여부
		if(search.getCurrentPage() > 1) {
			mv.addObject("isScroll", true);
		} else {
			mv.addObject("isScroll", false);
		}
		
		
		List<SpsDealgroup> dealGroups = new ArrayList<SpsDealgroup>();
		
		// 딜 구분타이틀 1&2 depth 리스트 조회
		dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(search);
		mv.addObject("depthList", dealGroups);

		// 상세검색 (색상 소재)
		if(CommonUtil.isNotEmpty(search.getColor()) || CommonUtil.isNotEmpty(search.getMaterial())) {
			String attribute = "";
			if(CommonUtil.isNotEmpty(search.getMaterial())) {
				attribute = Config.getString("employee.detail.matarial");
			}
			
			if(CommonUtil.isNotEmpty(search.getColor())) {
				if(attribute != "") {
					attribute += "," + Config.getString("employee.detail.color");
				} else {
					attribute = Config.getString("employee.detail.color");
				}
			}
			search.setAttributeId(attribute);
		}
		
		// 맴버쉽 상품
		List<PmsProduct> productList = (List<PmsProduct>) commonService.getDealProductLowestPriceList(search);
		
		// paging
		if (productList != null && productList.size() > 0) {
			mv.addObject("totalCount", productList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		
		mv.addObject("productList", productList);
		mv.addObject("search", search);
		
		return mv;
	}
	
	/**
	 * 분유관 노하우 레이어 
	 * @Method Name : milkPowderTipLayer
	 * @author : intune
	 * @date : 2016. 10. 7.
	 * @description : 
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/milkPowderTip/layer", method = RequestMethod.POST)
	public ModelAndView milkPowderTipLayer(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/milkPowderTipLayer");
		return mv;
	}

	/**
	 * 다자녀우대관
	 * 
	 * @Method Name : multiChildren
	 * @author : roy.
	 * @date : 2016. 10. 14.
	 * @description :
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/multiChildrenInfo")
	public ModelAndView children(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		if (SessionUtil.isMemberLogin()) {
			String childrenYn = FoSessionUtil.getChildrenYn();
			if (BaseConstants.YN_Y.equals(childrenYn)) {
				String dealId = FoSessionUtil.getChildrenDealId();

				SpsDealSearch dealSearch = new SpsDealSearch();
				dealSearch.setStoreId(SessionUtil.getStoreId());
				dealSearch.setDealId(dealId);
				dealSearch.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_CHILDREN);
				List<SpsDeal> dealList = dealService.getDealList(dealSearch);

				if (dealList != null) {
					dealList.get(0).setChildrenCardName(dealList.get(0).getChildrencardTypeName());
					mv.addObject("dealInfo", dealList.get(0));
				}

				List<SpsDealgroup> dealGroups = new ArrayList<SpsDealgroup>();
				// 딜 구분타이틀 1&2 depth 리스트 조회
				dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(dealSearch);
				mv.addObject("depthList", dealGroups);

				List<PmsProduct> productList = commonService.getDealProductLowestPriceList(dealSearch);
				mv.addObject("productList", productList);

				// 다자녀 인증된 회원 다자녀관으로 바로 이동 
				mv.setViewName("/dms/special/specialMultiChildrenList");

			}
		}
		return mv;
	}

	/**
	 * 다자녀우대관
	 * 
	 * @Method Name : multiChildren
	 * @author : roy.
	 * @date : 2016. 10. 14.
	 * @description :
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/multiChildren")
	public ModelAndView childrenProductList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/special/specialMultiChildrenList");

		String childrenYn = FoSessionUtil.getChildrenYn();
		if (BaseConstants.YN_Y.equals(childrenYn)) {
			String dealId = FoSessionUtil.getChildrenDealId();

			SpsDealSearch dealSearch = new SpsDealSearch();
			dealSearch.setStoreId(SessionUtil.getStoreId());
			dealSearch.setDealId(dealId);
			dealSearch.setDealTypeCd(BaseConstants.DEAL_TYPE_CD_CHILDREN);

			List<SpsDeal> dealList = dealService.getDealList(dealSearch);

			if (dealList != null) {
				dealList.get(0).setChildrenCardName(dealList.get(0).getChildrencardTypeName());
				mv.addObject("dealInfo", dealList.get(0));
			}

			List<SpsDealgroup> dealGroups = new ArrayList<SpsDealgroup>();

			// 딜 구분타이틀 1&2 depth 리스트 조회
			dealGroups = (List<SpsDealgroup>) dealService.getDealDepthList(dealSearch);
			mv.addObject("depthList", dealGroups);

			List<PmsProduct> productList = commonService.getDealProductLowestPriceList(dealSearch);
			mv.addObject("productList", productList);

		} else {
			//TODO : ERROR 페이지
		}
		return mv;
	}
}
