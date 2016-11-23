package gcp.frontpc.controller.view.pms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsSearchOptionFilter;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.CornerService;
import gcp.dms.service.SearchService;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.vo.SearchCategoryList;
import gcp.external.searchApi.vo.SearchData;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("brandViewController")
@RequestMapping("pms/brand")
public class BrandController {
	
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private BrandService		brandService;
	
	@Autowired
	private SearchService 		searchService;
	
	@Autowired
	private CornerService		cornerService;
	
	@Autowired
	private CategoryService 	categoryService;
	
	/**
	 * 브랜드 목록 조회
	 * @Method Name : getBrandTypeC
	 * @author : intune
	 * @date : 2016. 10. 1.
	 * @description : 
	 *		검색 엔진을 통해서 가져 온다.
	 * @param search
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product")
	public ModelAndView getBrandTypeC(@ModelAttribute("dmsDisplaySearch") DmsDisplaySearch search, HttpServletRequest request)throws Exception{
		
		ModelAndView model = new ModelAndView();
		String url = CommonUtil.makeJspUrl(request);
		logger.debug("===================================================");
		logger.debug("REQEUST BRAND_ID:"+request.getParameter("brandId"));
		logger.debug("REQEUST DISPLAYCATEGORY_ID:"+request.getParameter("dispCategoryId"));
		logger.debug("REQEUST TYPE:"+request.getParameter("type"));
		logger.debug("===================================================");
		
		/**********************************************************************************
		 * 공통 파라미터 & searchModel 설정
		 **********************************************************************************/
		boolean isMoblie = FoSessionUtil.isMobile(request);
		
		//Request ID
		String brandId = CommonUtil.replaceNull(request.getParameter("brandId"),"");
		String type = CommonUtil.replaceNull(request.getParameter("type"),"");
		String displayCategoryId = CommonUtil.replaceNull(request.getParameter("dispCategoryId"),"");
		
		model.addObject("dispCategoryId", displayCategoryId);
		model.addObject("brandId", brandId);
		model.addObject("brandType", type);
				
		//검색API 조회를 위한 search
		SearchApiSearch searchParam = new SearchApiSearch();
		List<String> brandIdList = new ArrayList<String>();
		List<String> categoryIdList = new ArrayList<String>();
		List<SearchCategoryList> optionCategoryList = new ArrayList<SearchCategoryList>(); 
		searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_BRAND);
		brandIdList.add(brandId);
		searchParam.setBrandIdList(brandIdList);
		
		/**************************************************************************
		 * 브랜드 정보
		 **************************************************************************/
		BrandSearch brandSearch = new BrandSearch(); 
		brandSearch.setBrandId(brandId);
		//List<PmsBrand> brandInfo = brandService.getBrandCodeList(brandSearch);
		brandSearch.setStoreId(SessionUtil.getStoreId());
		PmsBrand brandInfo = brandService.getBrandShopDetail(brandSearch);
		model.addObject("brandInfo",brandInfo);
		
		/*****************************************************************************
		 * 브랜드목록 유형별로 검색파라미터 설정, 코너정보 조회
		 * BRAND_PRODUCT_TYPE_BRAND  
		 * - 필수 값 : brandId 
		 * - 브랜드유형C타입으로  상품상세에서 유입되며 브랜드정보로 조회한다.
		 * 
		 * BRAND_PRODUCT_TYPE_CATEGORY 
		 * - 필수값 : dispCategoryId 
		 * - 브랜드관에서 유입되며 중카테고리 정보로 조회한다.
		 *****************************************************************************/
		if(CommonUtil.equals(BaseConstants.BRAND_PRODUCT_TYPE_BRAND, type)){
			
			//searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_BRAND);
			
			/**************************************************************************
			 * 브랜드유형C타입 코너 아이템 조회
			 **************************************************************************/
			search.setDisplayId(Config.getString("corner.brand.templC.intro"));
			search.setDisplayItemDivId(brandId);
			List<DmsDisplay> conerList = cornerService.getCommonCornerList(search);
			if(conerList != null && conerList.size() > 0){
				DmsDisplay corner = conerList.get(0);
				
				if(corner.getDmsDisplayitems() != null && corner.getDmsDisplayitems().size() > 0){
					Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
					corner.setTotalCount(corner.getDmsDisplayitems().size());
					returnMap.put(corner.getDisplayId(), corner);
					
					model.addObject("cornerMap",returnMap);
				}
			}
			
			model.setViewName(url + "C");
			
		}else{
			
			/**************************************************************************
			 * 브랜드유형 카테고리 목록 조회
			 **************************************************************************/
			//searchParam.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_CATEGORY);
			
			//하위카테고리 목록 조회
			DmsDisplaySearch ctgrSearch = new DmsDisplaySearch();
			ctgrSearch.setStoreId(SessionUtil.getStoreId());
			ctgrSearch.setDisplayCategoryId(displayCategoryId);
			ctgrSearch.setRootCategoryId(request.getParameter("rootCategoryId"));
			List<DmsDisplaycategory> lowCtgList =  categoryService.getDepth2CategoryList(ctgrSearch);
			if(lowCtgList.size() > 0){
				if(lowCtgList.get(0).getDmsDisplaycategorys().size() > 0){
					for (DmsDisplaycategory info : lowCtgList.get(0).getDmsDisplaycategorys()) {
						categoryIdList.add(info.getDisplayCategoryId());
						
						SearchCategoryList option = new SearchCategoryList();
						option.setCategoryId(info.getDisplayCategoryId());
						option.setCategoryName(info.getName());
						optionCategoryList.add(option);
					}
				}else{
					categoryIdList.add(displayCategoryId);
				}
			}
			//검색파라미터 set
			searchParam.setCategoryIdList(categoryIdList);
			
			//같은depth의 중카테고리 목록
			ctgrSearch.setDepth("1");
			ctgrSearch.setDisplayCategoryId("");
			List<DmsDisplaycategory> categoryList = categoryService.getAllCategoryList(ctgrSearch);
			model.addObject("category",categoryList);
			
			model.addObject("rootCategoryId",request.getParameter("rootCategoryId"));
			
			//현재카테고리 정보
			DmsDisplaycategory search1 = new DmsDisplaycategory();
			search1.setDisplayCategoryId(request.getParameter("rootCategoryId"));
			search1.setStoreId(SessionUtil.getStoreId());
			DmsDisplaycategory currentCategory = categoryService.getCurrentCategory(search1);
			model.addObject("currentCategory",currentCategory);

			if (FoSessionUtil.isMobile()) {
				model.setViewName(url + "B_mo");
			} else {
				model.setViewName(url + "B");
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
		 * 2) 브랜드매장
		 *  
		 *****************************************************************************/
		
		DmsSearchOptionFilter option = searchService.getSearchFilter(searchParam);
		SearchData searchApi = searchService.getSearchApiPrdList(searchParam);
		
		searchApi.setAgeCodeList(option.getAgeCodeList());
		searchApi.setGenderCodeList(option.getGenderCodeList());
		searchApi.setCategoryList(optionCategoryList);
		searchParam.setTotalCount(searchApi.getTotalCount());
		//depthCategoryIds
		searchParam.setDepthCategoryIds(categoryIdList);
		model.addObject("searchApi", searchApi);
		model.addObject("search", searchParam);
		
		
		
		return model;
	}

}
