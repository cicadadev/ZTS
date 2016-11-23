package gcp.frontpc.controller.rest.dms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.ExhibitService;
import gcp.dms.service.SearchService;
import gcp.external.model.search.SearchApiSearch;
import gcp.pms.model.PmsBrand;
import gcp.pms.service.BrandService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import net.sf.json.JSONObject;

@RestController("searchRestController")
@RequestMapping("api/dms/search")
public class SearchController {
	
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private ExhibitService exhibitService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	
	/**
	 * 검색 자동완성 & 인기검색어 API
	 * 
	 * @Method Name : getSeachApi
	 * @author : emily
	 * @date : 2016. 8. 24.
	 * @description : 
	 *	인기검색어 파라미터
	 *	- target : popword(고정)
	 * 	- collection : shopping(고정)
	 *	- range : day(일간) / week(주간) / month(월간)
	 *	- datatype : json / xml
	 *	자동완성 검색어 파라미터 
	 *	- query : 검색어 
	 *	- convert : fw(고정)
	 *	- target : common(고정)
	 *	- charset : UTF-8(고정)
	 *	- datatype : json / xml
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApi", method = RequestMethod.POST)
	public String getSearchApi(@RequestBody ModelMap map, HttpServletRequest request) throws Exception{
		
		String searchWord = CommonUtil.replaceNull((String) map.get("searchWord"));
		String type = CommonUtil.replaceNull((String) map.get("type"));
		String range = CommonUtil.replaceNull((String) map.get("range"));
		
		SearchApiSearch param = new  SearchApiSearch(); 
		param.setSearchKeyword(searchWord);
		param.setApiType(type);
		param.setRange(range);
		
		String result = searchService.getSearchApi(param);
		
		return result;
	}
	
	/**
	 * 최근 검색어 조회
	 * @Method Name : getLatelySearch
	 * @author : emily
	 * @date : 2016. 9. 21.
	 * @description : 
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/latelySearch", method = RequestMethod.POST)
	public String getLatelySearch(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String searchKeyWord = CookieUtil.getCookieList(request,response,"searchKeyWord",9999);
		return searchKeyWord;
	}
	
	/**
	 * 최근검색어 삭제
	 * @Method Name : deleteLatelySearch
	 * @author : emily
	 * @date : 2016. 9. 21.
	 * @description : 
	 *
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteSearch", method = { RequestMethod.GET, RequestMethod.POST })
	public JSONObject deleteLatelySearch(@RequestBody ModelMap map, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		JSONObject result = new JSONObject();
		try{
			
			if(CommonUtil.equals("ALL", (String) map.get("type"))){
				CookieUtil.removeCookie(response,"searchKeyWord");
			}else{
				CookieUtil.removeCookieData(request, response, "searchKeyWord", (String) map.get("keyWord"));
			}
			result.put("message", BaseConstants.RESULT_FLAG_SUCCESS);
		}catch(Exception e){
			result.put("message", BaseConstants.RESULT_FLAG_FAIL);
			logger.error(e);
			//e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 추천 카테고리, 추천기획전, 추천브랜드 조회
	 * @Method Name : getRecommend
	 * @author : emily
	 * @date : 2016. 11. 1.
	 * @description : 
	 *
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/recommend", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getRecommend(@RequestBody ModelMap map, HttpServletRequest request)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		try{
			String searchWord = CommonUtil.replaceNull((String) map.get("searchWord"));
			//기획전
			List<DmsExhibit> exhibitList = exhibitService.getRecommendExhibit(searchWord);
			if(exhibitList != null && exhibitList.size() > 0){
				result.put("exhibitList", exhibitList);
			}
			
			//카테고리
			DmsDisplaySearch search = new DmsDisplaySearch();
			search.setRootCategoryId(Config.getString("root.display.category.id"));
			search.setLeafYn(BaseConstants.YN_Y);
			search.setSearchKeyword(searchWord);
			List<DmsDisplaycategory> categoryList = categoryService.getRecommendCategory(search);
			if(categoryList != null && categoryList.size() > 0){
				
				for (DmsDisplaycategory dms : categoryList) {
					dms.setDepthFullName(dms.getDepthFullName().replace("|", " > "));
				}
				result.put("categoryList", categoryList);
			}
			
			//브랜드
			List<PmsBrand> brandList = brandService.getRecommendBrand(searchWord);
			if(brandList != null && brandList.size() > 0){
				result.put("brandList", brandList);
			}
			
			result.put("message", BaseConstants.RESULT_FLAG_SUCCESS);
		}catch(Exception e){
			result.put("message", BaseConstants.RESULT_FLAG_FAIL);
			logger.error(e);
			//e.printStackTrace();
		}
		
		return result;
	}
	
}
