package gcp.dms.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsCode;
import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsSearchOptionFilter;
import gcp.external.model.search.SearchApiSearch;
import gcp.external.searchApi.api.UnifiedSearchApi;
import gcp.external.searchApi.vo.SearchBenefitList;
import gcp.external.searchApi.vo.SearchBrandList;
import gcp.external.searchApi.vo.SearchCategoryList;
import gcp.external.searchApi.vo.SearchColorList;
import gcp.external.searchApi.vo.SearchData;
import gcp.external.searchApi.vo.SearchMaterial1List;
import gcp.external.searchApi.vo.SearchMaterialList;
import gcp.external.searchApi.vo.SearchProduct;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;

@Service("searchService")
public class SearchService extends BaseService{
	
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 검색을 위한 OptionFilter 조회
	 * @Method Name : getSearchFilter
	 * @author : emily
	 * @param searchParam 
	 * @date : 2016. 8. 31.
	 * @description : 
	 * 	검색의 filter가 있는 화면 월령 코드, 성별코드,
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DmsSearchOptionFilter getSearchFilter(SearchApiSearch searchParam) {
		DmsSearchOptionFilter reValue = new DmsSearchOptionFilter();
		
		//월령
		List<CcsCode> ageCodeList = (List<CcsCode>) dao.selectList("dms.search.getAgeTypeCode",null);
		reValue.setAgeCodeList(ageCodeList);
		
		//성별
		List<CcsCode> genderCodeList  = (List<CcsCode>) dao.selectList("dms.search.getGenderTypeCode",null);
		reValue.setGenderCodeList(genderCodeList);
		
		return reValue;
	}
	
	/**
	 * 검색 API : 상품검색
	 * @Method Name : getSearchApiPrdList
	 * @author : emily
	 * @date : 2016. 9. 7.
	 * @description : 파라미터 
	 *				1) SearchType
	 *					- 키워드검색을 제외한 화면은 'N'
	 *				2) 페이지 정보는 디폴트
	 *					- sort, direction, firstRow, pageSize
	 *				3) Request Pamater
	 * @param param
	 * @return
	 */
	public SearchData getSearchApiPrdList( SearchApiSearch param){
		
		/**********************************************************************************
		 * 검색API 옵션 Parameter Validation Check
		 **********************************************************************************/
		param.setDirection(CommonUtil.isEmpty(param.getDirection())?"DESC":param.getDirection());
		param.setSort(CommonUtil.isEmpty(param.getSort())?"ORDER_QTY":param.getSort());
		
		String sort = CommonUtil.isNull(param.getSort())|| CommonUtil.isEmpty(param.getSort())?"":param.getSort()+"/"+param.getDirection();
		int firstRow = param.getSearchFirstRow() == 1 ? 0 : param.getSearchFirstRow();
		/*int firstRow = 0;
		if(!CommonUtil.equals(param.getHistoryYn(), "Y")){
			firstRow = param.getSearchFirstRow();
		}*/
		String genderCode = CommonUtil.replaceNull(param.getGenderTypeCod(), "");
		if(CommonUtil.equals("ALL", genderCode)){
			param.setGenderTypeCod("");
		}else{
			param.setGenderTypeCod(genderCode);
		}
		String businessId = CommonUtil.replaceNull(param.getBusinessId(), "");
		String query = "";
		
		//키워드 검색인지 구분
		if(CommonUtil.equals(BaseConstants.SEARCH_API_VIEW_TYPE_KEYWORD, param.getSearchViewType()) 
				|| CommonUtil.equals(BaseConstants.SEARCH_API_VIEW_TYPE_BRANDSHOP, param.getSearchViewType())){
			param.setSearchType("Y");
			String query1 = CommonUtil.replaceNull(param.getSearchKeyword(), "");
			String query2 = CommonUtil.replaceNull(param.getKeyword(), "");
			query = query1+(query2 !=""?(" "+query2):"");
		}else{
			param.setSearchType("N");
		}
		
		//서버타입
		String serverType = Config.getString("search.api.server.type");
		
		/**********************************************************************************
		 * API 파라미터 설정		
		 **********************************************************************************/
		SearchData result = new SearchData();
		List<String> categoryIdList = new ArrayList<String>();
		List<String> brandIdList = new ArrayList<String>();
		List<String> ageTypeCodeList = new ArrayList<String>();
		List<String> colorList = new ArrayList<String>();
		List<String> materialList = new ArrayList<String>();
		List<String> material1List = new ArrayList<String>();
		SearchBenefitList benefitList = new SearchBenefitList();

		//카테고리 
		if(param.getCategoryIdList() != null && param.getCategoryIdList().size() > 0){
			categoryIdList.addAll(param.getCategoryIdList());
		}
		//브랜드
		if(param.getBrandIdList() != null && param.getBrandIdList().size() > 0){
			brandIdList.addAll(param.getBrandIdList());
		}
		//월령
		if(param.getAgeTypeCodeList() != null && param.getAgeTypeCodeList().size() > 0){
			ageTypeCodeList.addAll(param.getAgeTypeCodeList());
		}
		//컬러
		if(param.getColorList() != null && param.getColorList().size() > 0){
			colorList.addAll(param.getColorList());
		}
		//소재
		if(param.getMaterialList() != null && param.getMaterialList().size() > 0){
			materialList.addAll(param.getMaterialList());
		}
		//젖병소재
		if(param.getMaterial1List() != null && param.getMaterial1List().size() > 0){
			material1List.addAll(param.getMaterial1List());
		}
		//혜택
		benefitList.setCouponYn(param.getCouponYn());
		benefitList.setDeliveryFreeYn(param.getDeliveryFreeYn());
		benefitList.setOffshopPickupYn(param.getOffshopPickupYn());
		benefitList.setPointSaveYn(param.getPointSaveYn());
		benefitList.setPresentYn(param.getPresentYn());
		benefitList.setRegularDeliveryYn(param.getRegularDeliveryYn());
		
		/**********************************************************************************
		 * 검색API 호출
		 **********************************************************************************/
		//TEST
		logger.debug("======================================");
		logger.debug("query:"+query);
		logger.debug("firstRow:"+firstRow);
		logger.debug("pageSize:"+param.getPageSize());
		logger.debug("sort:"+sort);
		logger.debug("categoryIdList:"+categoryIdList.toString());
		logger.debug("brandIdList:"+brandIdList.toString());
		logger.debug("ageTypeCodeList:"+ageTypeCodeList.toString());
		logger.debug("genderTypeCod:"+param.getGenderTypeCod());
		logger.debug("lowPrice:"+param.getLowPrice());
		logger.debug("highPrice:"+param.getHighPrice());
		logger.debug("colorList:"+colorList.toString());
		logger.debug("materialList:"+materialList.toString());
		logger.debug("material1List:"+material1List.toString());
		logger.debug("benefitList:"+benefitList.toString());
		logger.debug("businessId:"+businessId);
		logger.debug("searchType:"+param.getSearchType());
		logger.debug("serverType:"+serverType);
		logger.debug("======================================");
		
		//기본검색API
		result = new UnifiedSearchApi().runKeywordSearch(query, firstRow, param.getPageSize(), sort
				, param.getSearchType(), categoryIdList, brandIdList,ageTypeCodeList,param.getGenderTypeCod(), param.getLowPrice(), param.getHighPrice()
				, colorList, materialList, material1List,benefitList,businessId,serverType);
		
		/**********************************************************************************
		 * 카테고리, 브랜드, 컬러, 소재 목록의  총 카운트와 null값 제외
		 **********************************************************************************/
		//카테고리 목록
		if(result.getCategoryList() != null && result.getCategoryList().size()>0){
			int ctgTotalCnt =0;
			for (SearchCategoryList info : result.getCategoryList()) {
				ctgTotalCnt+=info.getCategoryCount();
			}
			result.setCatgtotalCount(ctgTotalCnt);
		}
		
		//브랜드목록
		if(result.getBrandList() != null && result.getBrandList().size()>0){
			int brdTotalCnt =0;
			for (SearchBrandList info : result.getBrandList()) {
				if("null".equals(info.getBrandId()) || "null".equals(info.getBrandName())){
					info.setBrandName("기타");
				}
				brdTotalCnt+=info.getBrandCount();
			}
			result.setBrdtotalCount(brdTotalCnt);
		}
		
		//컬러목록
		if(result.getColorList() != null && result.getColorList().size() > 0){
			List<SearchColorList> list = new ArrayList<SearchColorList>();
			int colorTotalCnt =0;
			for (SearchColorList info : result.getColorList()) {
				if(!"null".equals(info.getColorName())){
					colorTotalCnt+=info.getColorCount();
					list.add(info);
				}
			}
			result.setColorList(list);
			result.setColorTotalCount(colorTotalCnt);
		}
		
		//일반소재목록
		if(result.getMaterialList() != null && result.getMaterialList().size() > 0){
			List<SearchMaterialList> list = new ArrayList<SearchMaterialList>();
			int materTotalCount =0;
			for (SearchMaterialList info : result.getMaterialList()) {
				if(!"null".equals(info.getMaterialName())){
					materTotalCount+=info.getMaterialCount();
					list.add(info);
				}
			}
			result.setMaterialList(list);
		}
		
		//젖병소재목록
		if(result.getMaterial1List() != null && result.getMaterial1List().size() > 0){
			List<SearchMaterial1List> list = new ArrayList<SearchMaterial1List>();
			int materTotalCount =0;
			for (SearchMaterial1List info : result.getMaterial1List()) {
				if(!"null".equals(info.getMaterial1Name())){
					materTotalCount+=info.getMaterial1Count();
					list.add(info);
				}
			}
			result.setMaterial1List(list);
		}
		
		//상품명의 키워드의 태그 없애기
		if(result.getProductList() != null && result.getProductList().size() > 0){
			for (SearchProduct info : result.getProductList()) {
				String productName = info.getProductName(); 
				productName = productName.replaceAll("<!HS>", "");
				productName = productName.replaceAll("<!HE>", "");
				info.setProductName(productName);
			}
		}
		
		logger.debug("result:"+result);
		return result;
	}

	/**
	 * 자동검색API
	 * @Method Name : getSearchApi
	 * @author : intune
	 * @date : 2016. 9. 12.
	 * @description : 
	 *	연관검색어, 자동완성검색, 인기검색어 API
	 * @param searchParam
	 * @return
	 * @throws Exception
	 */
	public String getSearchApi(SearchApiSearch searchParam) throws Exception{
		
		String range = searchParam.getRange();
		String searchWord = searchParam.getSearchKeyword();
		String type = searchParam.getApiType();
		String url = Config.getString("search.api.url");
		String param = "";
		int timeout = 1000;	// 1000분의 500초 : 0.5초이내에 응답이 없는 경우 연결 종료
		
		logger.debug("===================================");
		logger.debug("searchWord: "+searchWord);
		logger.debug("type: "+type);
		logger.debug("range: "+range);
		logger.debug("===================================");
		
		
		if(CommonUtil.equals(BaseConstants.SEARCH_API_TYPE_AUTO, type)){
			searchWord = URLEncoder.encode(searchWord, "UTF-8");
			param = "query=" + searchWord + "&convert=fw&target=common&charset=UTF-8&datatype=json";
		}else if(CommonUtil.equals(BaseConstants.SEARCH_API_TYPE_POPULAR, type)){
			param = "target=popword&collection=shopping&range="+range+"&datatype=json";
		}else if(CommonUtil.equals(BaseConstants.SEARCH_API_TYPE_RELATION, type)){
			param = "query=" + searchWord + "&target=recommend&label=_ALL_&datatype=json";
		}
		
		HttpURLConnection uc = null;
		BufferedReader in = null;
		StringBuffer receiveMsg = new StringBuffer();
	
		try {
			
			URL servletUrl = new URL(url);
			uc = (HttpURLConnection) servletUrl.openConnection();
			uc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			uc.setRequestMethod("POST");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setDefaultUseCaches(false);
			uc.setConnectTimeout(timeout);
			uc.setReadTimeout(timeout);
			DataOutputStream dos = new DataOutputStream (uc.getOutputStream());
			dos.write(param.getBytes());
			dos.flush();
			dos.close();
			
			int errorCode = 0;
			//logger.debug(param);
			// -- Network error check
			logger.debug("[URLConnection Response Code] " + uc.getResponseCode());
			
			if (uc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String currLine = "";
	           
				// UTF-8
	            in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
	            while ((currLine = in.readLine()) != null) {
	            	receiveMsg.append(currLine).append("\r\n");
	            }
	            in.close();
	            
			} else {
	              errorCode = uc.getResponseCode();
	              return receiveMsg.toString();
			}
			
		} catch(Exception ex) {
			logger.error(ex);
		} finally {
			if(in != null) in.close();
			if(uc != null) uc.disconnect();
		}
	
		logger.debug(receiveMsg.toString());
		return receiveMsg.toString();
	}
	
}
