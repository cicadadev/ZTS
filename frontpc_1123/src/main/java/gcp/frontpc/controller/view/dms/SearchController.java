package gcp.frontpc.controller.view.dms;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsSearchOptionFilter;
import gcp.dms.service.SearchService;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.api.UnifiedSearchApi;
import gcp.external.searchApi.vo.SearchBenefitList;
import gcp.external.searchApi.vo.SearchData;
import gcp.external.service.RecobelRecommendationService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.service.BrandService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("searchController")
@RequestMapping("dms/search")
public class SearchController {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private RecobelRecommendationService 	recobelRecommendationService;
	
	@Autowired
	private BrandService					brandService;

	public static void main(String[] args) {

		UnifiedSearchApi search = new UnifiedSearchApi();

		List<String> categoryIdList = new ArrayList<String>();
		List<String> brandIdList = new ArrayList<String>();
		List<String> ageTypeCodeList = new ArrayList<String>();
		List<String> colorList = new ArrayList<String>();
		List<String> materialList = new ArrayList<String>();
		List<String> material1List = new ArrayList<String>();
		SearchBenefitList benefitList = new SearchBenefitList();

		//파라미터세팅 예시
		String temp[] = { "100003", "20001", "20002", "20003", "20004", "20005", "20006", "20007", "20008", "20009", "20010",
				"20012", "20022", "20023", "20024", "20025", "20026", "20027", "20028", "20029", "20030", "20031", "20032" };
	
		for (String aa : temp) {
			//categoryIdList.add(aa);
		}
		//categoryIdList.add("20001");
	//	categoryIdList.add("20002");
		

		//brandIdList.add("500035");
		//brandIdList.add(null);
		//brandIdList.add("10");

		//ageTypeCodeList.add("3MONTH");
		//ageTypeCodeList.add("12MONTH");

		//colorList.add("black");

		//materialList.add("면");

		//benefitList.setCouponYn("Y");
		benefitList.setRegularDeliveryYn("Y");
		
		System.out.println("======================================");
		System.out.println("categoryIdList:"+categoryIdList.toString());
		System.out.println("brandIdList:"+brandIdList.toString());
		System.out.println("ageTypeCodeList:"+ageTypeCodeList.toString());
		System.out.println("colorList:"+colorList.toString());
		System.out.println("materialList:"+materialList.toString());
		System.out.println("benefitList:"+benefitList.toString());
		System.out.println("======================================");
		

		//키워드 기본검색
		SearchData resultJson = search.runKeywordSearch("가방", 0, 100, "ORDER_QTY/DESC", "Y", categoryIdList, brandIdList,
				ageTypeCodeList,
				"", 0, 0, colorList, materialList, material1List, benefitList, "", "dev");
		//카테고리 검색
		//resultJson = search.runCategorySearch(0, 10, "REVIEW_COUNT/DESC");

		//키워드 상세검색
		//resultJson = search.runDetailSearch("dennis", 0, 10, "RANK/DESC", "Y", 10000, 50000);


		System.out.println(resultJson);
	}
	
	/**
	 * 전시 검색API 상품목록
	 * @Method Name : getSearchProductList
	 * @author : emily
	 * @date : 2016. 8. 25.
	 * @description : 
	 *
	 * @param param
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/productList",method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getSearchProductList( SearchApiSearch param, HttpServletRequest request) throws Exception{
		
		ModelAndView model = new ModelAndView("/dms/include/searchProductList");
		
		logger.debug("======================================");
		logger.debug("SearchKeyword:"+param.getSearchKeyword());
		logger.debug("======================================");
		
		/**********************************************************************************
		 * 검색API 호출
		 **********************************************************************************/
		SearchData result = new SearchData();
		try{
			result = searchService.getSearchApiPrdList(param);
			param.setTotalCount(result.getTotalCount());
			if(CommonUtil.equals(BaseConstants.SEARCH_API_VIEW_TYPE_KEYWORD, param.getSearchViewType()) 
					&& CommonUtil.equals("Y", param.getOptionBrandSrcYn())){
				String optionCtg = "";
				//검색한 카테고리 목록 String으로 화면에 저장
				if(result.getCategoryList() != null && result.getCategoryList().size() > 0){
					for (int i = 0; i < result.getCategoryList().size(); i++) {
						String id = result.getCategoryList().get(i).getCategoryId();
						String a = "";
						if(i > 0){
							a=",";
						}
						optionCtg+=(a+id);
					}
					param.setOptionCtgStrs(optionCtg);
				}
			}
		
		}catch(Exception e){
			logger.error(e);
			//e.printStackTrace();
		}
		
		model.addObject("searchApi", result);
		model.addObject("search", param);
		
		return model;
	}
	
	/**
	 * 검색 메인 페이지
	 * @Method Name : getSearchMain
	 * @author : emily
	 * @date : 2016. 8. 30.
	 * @description : 
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/main")
	public ModelAndView getSearchMain(HttpServletRequest request, HttpServletResponse response) throws Exception{	
		
		ModelAndView model = new ModelAndView();
		//키워드
		String paramKeyWord = CommonUtil.replaceNull(request.getParameter("keyword"), "");
		
		//브랜드관ID
		String brandShopId = CommonUtil.replaceNull(request.getParameter("brandShopId"), "");
		
		String keyword ="";
		if(CommonUtil.isNotEmpty(paramKeyWord)){
		
			keyword = URLDecoder.decode(paramKeyWord,"UTF-8");
			logger.debug("=========================");
			logger.debug("keyword:"+keyword);
			logger.debug("=========================");
			
			boolean isMobile = FoSessionUtil.isMobile(request);
			
			/**********************************************************************************
			 * 검색어 정보 Cookie 설정
			 * - 최근 검색어
			 * - 히스토리 : 모바일 history 쿠키 계속유지 => 쿠키 10년, 쿠키 내 데이터 10년 set함 
			 * 특수문자가 포함된 문자는 쿠키에 담지 않는다.
			 **********************************************************************************/
			//최근검색어
			CookieUtil.createCookieString(request,response,"searchKeyWord",keyword,60 * 60 * 24, new BigDecimal(30));
			
			Pattern p1 = Pattern.compile(".*[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s].*");
			Matcher m1 = p1.matcher(keyword);
			if(!m1.matches()) {
				//히스토리
				if(isMobile) {
					String temp = "SEARCH," + keyword;
					CookieUtil.createCookieString(request, response, "moHistory", temp, CookieUtil.SECONDS_OF_10YEAR, new BigDecimal(3650));
				}	
			}
			
			/**********************************************************************************
			 * 공통변수 선언
			 **********************************************************************************/
			SearchData result = new SearchData();
			SearchApiSearch param = new  SearchApiSearch(); 
			JSONObject jsonObject  =new JSONObject();
			param.setSearchKeyword(keyword);
			
			if(CommonUtil.isNotEmpty(brandShopId)){
				param.setBrandShopId(brandShopId);
				param.getBrandIdList().add(brandShopId);
				param.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_BRANDSHOP);
			}else{
				param.setSearchViewType(BaseConstants.SEARCH_API_VIEW_TYPE_KEYWORD);
			}
			
			try{
				/**********************************************************************************
				 * 검색API 호출
				 **********************************************************************************/
				result = searchService.getSearchApiPrdList(param);
				param.setTotalCount(result.getTotalCount());
				
				DmsSearchOptionFilter option = searchService.getSearchFilter(param);
				result.setAgeCodeList(option.getAgeCodeList());
				result.setGenderCodeList(option.getGenderCodeList());
				
				/**********************************************************************************
				 * 연관검색 API 호출
				 **********************************************************************************/
				param.setApiType(BaseConstants.SEARCH_API_TYPE_RELATION);
				String relation = searchService.getSearchApi(param);
				JSONParser jsonParser = new JSONParser();
				jsonObject = (JSONObject) jsonParser.parse(relation);
				
			}catch(Exception e){
				logger.error(e);
				//e.printStackTrace();
			}
			
			//footer노출여부에 따라서 VIEW 분기처리
			if(CommonUtil.isNotEmpty(brandShopId)){
				if(result !=null && result.getTotalCount() > 0){
					model.setViewName("/dms/search/brandSearchMain");
				}else{
					model.setViewName("/dms/search/brandSearchMain_notResult");
				}

				// 브랜드 로고 이미지 사용 위해 brandId 리턴
				BrandSearch brandSearch = new BrandSearch();
				brandSearch.setStoreId(SessionUtil.getStoreId());
				brandSearch.setBrandId(brandShopId);
				PmsBrand brandInfo = brandService.getBrandShopDetail(brandSearch);

				model.addObject("brandInfo", brandInfo);
			}else{
				if(result !=null && result.getTotalCount() > 0){
					model.setViewName(CommonUtil.makeJspUrl(request));
				}else{
					model.setViewName(CommonUtil.makeJspUrl(request)+"_notResult");
				}
			}
			
			model.addObject("relation", jsonObject.get("Data"));
			model.addObject("searchApi", result);
			model.addObject("search", param);
			model.addObject("suggestedQuery", result.getSuggestedQuery());

		}else{
			//throw new ServiceException("dms.search.empty.keyword");
			keyword = paramKeyWord;
			if(CommonUtil.isNotEmpty(brandShopId)){
				model.setViewName("/dms/search/brandSearchMain_notResult");
			}else{
				model.setViewName(CommonUtil.makeJspUrl(request)+"_notResult");
			}
		}

		model.addObject("keyword", keyword);
		
		return model;
	}
	
	/**
	 * 검색의 연관검색페이지 추천엔진
	 * @Method Name : getCategoryBestProduct
	 * @author : intune
	 * @date : 2016. 11. 21.
	 * @description : 
	 *
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/recommenadation/ajax", method = { RequestMethod.POST })
	public ModelAndView getCategoryBestProduct(@RequestParam Map<String, String> map, HttpServletRequest request)throws Exception {
		ModelAndView mv = new ModelAndView("/dms/display/inner/recobellProducts");

		List<PmsProduct> list = recobelRecommendationService.getRecommendationList(map);
		mv.addObject("products", list);
		mv.addObject("productsSize", list.size());
		mv.addObject("keyword", map.get("st"));
		
		logger.debug("list.size():"+list.size());
		return mv;
	}

}
