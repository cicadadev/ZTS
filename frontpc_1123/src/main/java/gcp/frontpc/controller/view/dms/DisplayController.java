package gcp.frontpc.controller.view.dms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.CodeService;
import gcp.ccs.service.OffshopService;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsSearchOptionFilter;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.CornerService;
import gcp.dms.service.ExhibitService;
import gcp.dms.service.SearchService;
import gcp.dms.service.SpecialService;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.vo.SearchData;
import gcp.external.service.RecobelRecommendationService;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.StyleService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("displayViewController")
@RequestMapping("dms/display")
public class DisplayController {
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private RecobelRecommendationService 	recobelRecommendationService;
	
	@Autowired
	private CategoryService	 				displayCategoryService;
	
	@Autowired
	private StyleService					styleService;
	
	@Autowired
	private CodeService						codeService;

	@Autowired
	private OffshopService					offshopService;

	@Autowired
	private ExhibitService					exhibitService;

	@Autowired
	private SpecialService					specialService;

	@Autowired
	private CornerService					cornerService;
	
	@Autowired
	private ProductService					productService;

	private final Log	logger	= LogFactory.getLog(getClass());
	
	/**
	 * 베스트샵 메인
	 * @Method Name : getBest
	 * @author : emily
	 * @date : 2016. 8. 4.
	 * @description : 
	 *
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/bestShop")
	public ModelAndView getBestShop(HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		if(CommonUtil.equals("mainCon", mobileType)){
			mv.setViewName("/dms/display/inner/displayBestShop.mainCon");
		}else{
			mv.setViewName(CommonUtil.makeJspUrl(request));
		}
		
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id")); // 전시공통카테고리의  root카테고리ID 추가
		
		List<DmsDisplaycategory> categoryList = displayCategoryService.getDepth2CategoryList(search);
		List<DmsDisplaycategory> ctgList =  displayCategoryService.getDepthCategoryList(search, categoryList);
		mv.addObject("categoryList", ctgList);
		
		return mv;
	}
	
	/**
	 * 베스트샵
	 * @Method Name : getBest
	 * @author : emily
	 * @date : 2016. 8. 4.
	 * @description : 
	 *
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bestShop/ajax", method = RequestMethod.GET)
	public ModelAndView getBestShopAjax(@RequestParam Map<String, String> map, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/display/inner/bestList");
		
		logger.debug("@param:" + map.toString());
		logger.debug("@param:" + request.getParameter("type"));
		
		List<PmsProduct> list = recobelRecommendationService.getRecommendationList(map);
		if (list.size() <= 0) {
			PmsProductSearch search = new PmsProductSearch();
			search.setStoreId(SessionUtil.getStoreId());
			
			if(CommonUtil.equals("main", CommonUtil.replaceNull(request.getParameter("type"), ""))){
				search.setPageSize(10);
			}else{
				search.setPageSize(50);
			}
			
			String displayCategoryId = map.get("key").split("-")[0];
			if (CommonUtil.isNotEmpty(displayCategoryId)) {
				search.setDisplayCategoryId(displayCategoryId);
			}
					
			mv.addObject("products", productService.getBrandBestProduct(search));
		} else {
			mv.addObject("products", list);
		}
				
		return mv;
	}

	/**
	 * 판매자 매장
	 * @Method Name : getBusinessShop
	 * @author : emily
	 * @date : 2016. 8. 4.
	 * @description : 배송비 절약하기 
	 *	 
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sellerShop")
	public ModelAndView getBusinessShop(@ModelAttribute("dmsDisplaySearch") DmsDisplaySearch search, HttpServletRequest request)throws Exception{
		ModelAndView model = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		logger.debug("===================================================");
		logger.debug("REQEUST BUSINESS_ID:"+request.getParameter("businessId"));
		logger.debug("===================================================");
		
		String businessId = CommonUtil.replaceNull(request.getParameter("businessId"),"");
		
		//검색API 조회를 위한 search
		SearchApiSearch searchParam = new SearchApiSearch();
		//검색option
		DmsSearchOptionFilter option = new DmsSearchOptionFilter();
		//검색결과
		SearchData searchApi = new SearchData();
		
		searchParam.setBusinessId(businessId);
		searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_BUSINESS);
		
		try{
			//검색엔진호출
			option = searchService.getSearchFilter(searchParam);
			searchApi = searchService.getSearchApiPrdList(searchParam);
			searchApi.setAgeCodeList(option.getAgeCodeList());
			searchApi.setGenderCodeList(option.getGenderCodeList());
			searchParam.setTotalCount(searchApi.getTotalCount());
			
		}catch(Exception e){
			logger.error(e);
			//e.printStackTrace();
		}
		
		model.addObject("searchApi", searchApi);
		model.addObject("search", searchParam);
		
		return model;
	}

	/**
	 * 
	 * @Method Name : getStyleMain
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 스타일 메인
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/styleMain")
	public ModelAndView getStyle(HttpServletRequest request, MmsStyleSearch search) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		String styleBannerId = Config.getString("corner.style.banner.img.1");
		DmsDisplaySearch displaySearch = new DmsDisplaySearch();
		displaySearch.setStoreId(SessionUtil.getStoreId());
		displaySearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		displaySearch.setDisplayId(styleBannerId);
		List<DmsDisplay> bannerList = cornerService.getCommonCornerList(displaySearch);
		mv.addObject("bannerList", bannerList);

		if(CommonUtil.equals("mainCon", mobileType)){
			mv.setViewName("/dms/display/inner/displayStyleMain.mainCon");
		}else{
			mv.setViewName(CommonUtil.makeJspUrl(request));
		}
		
		mv.addObject("search", search);
		return mv;
	}

	/**
	 * 
	 * @Method Name : getStyleListAjax
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 스타일리스트 AJAX
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/style/list/ajax")
	public ModelAndView getStyleListAjax(HttpServletRequest request, @RequestBody MmsStyleSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/display/inner/styleList");

		if (SessionUtil.isMemberLogin()) {
			search.setMemberNo(SessionUtil.getMemberNo());
		}

		List<MmsStyle> styleList = styleService.getMainStyleList(search);


		if (styleList != null) {
			for (MmsStyle mmsStyle : styleList) {
				String hashTag = "";
				if (StringUtils.isNotEmpty(mmsStyle.getGenderTypeCd())) {
					hashTag += " #" + mmsStyle.getGenderTypeName();
				}
				if (StringUtils.isNotEmpty(mmsStyle.getThemeCd())) {
					hashTag += " #" + mmsStyle.getThemeName();
					mmsStyle.setThemeCdName(mmsStyle.getThemeName());
				}
				if (StringUtils.isNotEmpty(mmsStyle.getBrandId())) {
					hashTag += " #" + mmsStyle.getBrandName();
				}
				mmsStyle.setHashTag(hashTag.trim());
			}
			mv.addObject("styleList", styleList);
		}

		// paging
		if (styleList != null && styleList.size() > 0) {
			mv.addObject("totalCount", styleList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		mv.addObject("search", search);
		return mv;
	}

	/**
	 * 
	 * @Method Name : getStyleDetail
	 * @author : intune
	 * @date : 2016. 10. 10.
	 * @description :
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/styleDetail")
	public ModelAndView getStyleDetail(HttpServletRequest request, MmsStyleSearch search) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		if (search.getMemberNo() == null) {
			search.setMemberNo(SessionUtil.getMemberNo());
		}

		MmsStyle styleDetail = styleService.getMemberStyleDetail(search);
		if (styleDetail != null) {
			String hashTag = "";
			if (StringUtils.isNotEmpty(styleDetail.getGenderTypeCd())) {
				hashTag += " #" + styleDetail.getGenderTypeName();
			}
			if (StringUtils.isNotEmpty(styleDetail.getThemeCd())) {
				hashTag += " #" + styleDetail.getThemeName();
				styleDetail.setThemeCdName(styleDetail.getThemeName());
			}
			if (StringUtils.isNotEmpty(styleDetail.getBrandName())) {
				hashTag += " #" + styleDetail.getBrandName();
			}
			styleDetail.setHashTag(hashTag.trim());
			mv.addObject("styleDetail", styleDetail);
			mv.addObject("ogTagTitle", styleDetail.getTitle());
			mv.addObject("ogTagImage", Config.getString("image.domain") + styleDetail.getStyleImg());
			mv.addObject("ogTagUrl", Config.getString("front.domain.url") +
					"/dms/display/styleDetail?styleNo=" + styleDetail.getStyleNo() + "&memberNo=" + styleDetail.getMemberNo());
			
			boolean isMobile = FoSessionUtil.isMobile(request); //PC or Mobile

			if (!isMobile) {
				mv.setViewName("/dms/display/displayStyleMain");
			}
		}

		return mv;
	}
	
	/**
	 * 월령 상품목록 조회
	 * @Method Name : getAgeProductList
	 * @author : emily
	 * @date : 2016. 10. 12.
	 * @description : 
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ageShop")
	public ModelAndView getAgeProductList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		logger.debug("REQEUST AGECODE:"+request.getParameter("ageCode"));
		
		/**********************************************************************************
		 * 공통변수 선언
		 **********************************************************************************/
		SearchData result = new SearchData();
		SearchApiSearch param = new  SearchApiSearch(); 
		List<String> ageTypeCodeList = new ArrayList<String>();
		String ageCode = CommonUtil.replaceNull(request.getParameter("ageCode"),"");
		param.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_AGE);
		ageTypeCodeList.add(ageCode);
		param.setAgeTypeCodeList(ageTypeCodeList);

		try{
			
			/**********************************************************************************
			 * 검색API 호출
			 **********************************************************************************/
			result = searchService.getSearchApiPrdList(param);
			param.setTotalCount(result.getTotalCount());
			
			DmsSearchOptionFilter option = searchService.getSearchFilter(param);
			result.setAgeCodeList(option.getAgeCodeList());
			result.setGenderCodeList(option.getGenderCodeList());
			
		}catch(Exception e){
			logger.error(e);
			//e.printStackTrace();
		}
		
		logger.debug("##### :"+param.getAgeTypeCodeList().toString());
		mv.addObject("searchApi", result);
		mv.addObject("search", param);
		
		/*****************************************************************************
		 * 월령 코드 목록 조회 - 상품의 월령코드 목록만 조회함.
		 *****************************************************************************/
		CcsCodeSearch codeSearch = new CcsCodeSearch();
		codeSearch.setCdGroupCd("AGE_TYPE_CD");
		codeSearch.setStoreId(SessionUtil.getStoreId());
		CcsCodegroup ageCodeInfo = codeService.getFrontCodeList(codeSearch);
		mv.addObject("ageCodeList", ageCodeInfo.getCcsCodes());
		
		/*****************************************************************************
		 * 현재 월령코드 정보 
		 *****************************************************************************/
		codeSearch.setCd("AGE_TYPE_CD."+ageCode);
		CcsCodegroup currentCode = codeService.getFrontCodeList(codeSearch);
		mv.addObject("currentCode", currentCode.getCcsCodes().get(0));
		return mv;
	}

	/**
	 * 
	 * @Method Name : shopOn
	 * @author : allen
	 * @date : 2016. 10. 14.
	 * @description :
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shopOn")
	public ModelAndView shopOn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		ModelAndView mv = new ModelAndView("/dms/display/inner/displayShopOn");
/*		ModelAndView mv = new ModelAndView();
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		if(CommonUtil.equals("mainCon", mobileType)){
			mv.setViewName("/dms/display/inner/displayShopOn.mainCon");
		}else{
			mv.setViewName(CommonUtil.makeJspUrl(request));
		}*/
		
		// 회원 관심 매장 리스트 조회
		if (SessionUtil.getMemberNo() != null) {
			CcsOffshopSearch search = new CcsOffshopSearch();
			search.setStoreId(SessionUtil.getStoreId());
			search.setMemberNo(SessionUtil.getMemberNo().toString());
			search.setPagingYn(BaseConstants.YN_Y);
			List<MmsInterestoffshop> interestOffshopList = offshopService.getShopOnOffShopInfoList(search);
			mv.addObject("interestOffshopList", interestOffshopList);
		}
		
		// 오프라인 기획전 조회
		DmsExhibitSearch search = new DmsExhibitSearch();
		search.setStoreId(SessionUtil.getStoreId());
		List<DmsExhibit> exhibitList = exhibitService.getOffshopExhibitList(search);
		mv.addObject("exhibitList", exhibitList);

		// 매장 픽업 브랜드 리스트
		DmsDisplaySearch displaySearch = new DmsDisplaySearch();
		displaySearch.setStoreId(SessionUtil.getStoreId());
		displaySearch.setRootCategoryId(Config.getString("root.display.category.id"));
		List<PmsBrand> brandList = specialService.getPickupBrandList(displaySearch);
		mv.addObject("brandList", brandList);

		// 브랜드의 카테고리 및 상품 리스트 조회
		if (brandList != null && brandList.size() > 0) {
			List<DmsDisplaycategory> categoryList = specialService.getPickupInfoList(displaySearch);
			mv.addObject("categoryList", categoryList);
		}

		CcsOffshopSearch offshopSearch = new CcsOffshopSearch();
		offshopSearch.setPickupYn("Y");
		mv.addObject("area1List", offshopService.getOffshopAreaDiv1List(offshopSearch));

		return mv;
	}
}
