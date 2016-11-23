package gcp.external.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.service.BaseService;
import gcp.common.util.FoSessionUtil;
import gcp.external.model.recobell.RecobellProductResult;
import gcp.external.model.recobell.RecobellResponse;
import gcp.pms.model.PmsProduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.RestUtil;
import intune.gsf.model.RestParam;

/**
 * 레코벨 개인화 추천 API
 * 
 * @Pagckage Name : gcp.external.service
 * @FileName : RecobelRecommendationService.java
 * @author : eddie
 * @date : 2016. 9. 26.
 * @description :
 */
@Service
public class RecobelRecommendationService extends BaseService {

	private final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@Cacheable(cacheName = "getRecommendationList-cache")
	public List<PmsProduct> getRecommendationList(Map<String, String> map) throws Exception {

		String paramStr = "";

		List<PmsProduct> results = new ArrayList<PmsProduct>();
		if (CommonUtil.isEmpty(map.get("recType"))) {
			return results;
		}

		for (String key : map.keySet()) {
			if ("recType".equals(key)) {
				continue;
			}
			paramStr += "&" + key + "=" + map.get(key);
		}

		// 장바구니일때 관심정보의 성별, 아기월령정보 설정
		if (map.get("recType").equals("x")) {
			if (!paramStr.contains("key")) {
				paramStr += "&key=-" + FoSessionUtil.getRecobellBabyGenderCd() + "-" + FoSessionUtil.getRecobellBabyMonthCd()
						+ "-view";
			}
		}

		String url = Config.getString("recobell.url") + map.get("recType") + "?cuid=" + Config.getString("recobell.cuid")
				+ "&format=json&" + paramStr;

		long startTime = System.currentTimeMillis();

		logger.info("---------------------------------------------------------------------------------------");
		logger.info("### Recobell url [" + map.get("recType") + "]  : " + url);
		logger.info("---------------------------------------------------------------------------------------");

		RecobellResponse res = (RecobellResponse) RestUtil.call(new RestParam(url), RecobellResponse.class);


		long endTime = System.currentTimeMillis();
		float periodTime = (endTime - startTime) / 1000.0f;

		List<String> prdList = new ArrayList<String>();
		if (res != null && res.getResults() != null && res.getResults().size() > 0) {

			logger.info("---------------------------------------------------------------------------------------");
			logger.info("### Recobell Result Count [" + map.get("recType") + "] : " + res.getResults().size() + ", time : "
					+ periodTime);
			logger.info("---------------------------------------------------------------------------------------");
			
			Map<String, String> sortPrd = new HashMap<String, String>();
			
			for (RecobellProductResult result : res.getResults()) {
				logger.debug("itemId:"+result.getItemId());
				prdList.add(result.getItemId());
				
				//랭킹 별로 orderBy
				if(CommonUtil.equals("x", map.get("recType"))){
					sortPrd.put(result.getItemId(), result.getRank());
				}
			}
			
			List<PmsProduct> resultList = (List<PmsProduct>) dao.selectList("pms.product.getProductListByProductIds", prdList); 
			
			//랭킹 별로 orderBy
			if(CommonUtil.equals("x", map.get("recType")) && (resultList != null && resultList.size() > 0)){
				for (PmsProduct prd : resultList) {
					prd.setBestRank(Long.valueOf(sortPrd.get(prd.getProductId())));
				}
				
				Collections.sort(resultList, new NoAscCompare());
			}
			
			
			/*for (PmsProduct prdk : resultList) {
				logger.info("############1:"+prdk.getProductId());
				logger.info("############2:"+prdk.getBestRank());
			}*/
			
			return resultList;
		} else {
			return results;
		}

	}
	
	static class NoAscCompare implements Comparator<PmsProduct> {
	   public int compare(PmsProduct arg0, PmsProduct arg1) {
	       return arg0.getBestRank().compareTo(arg1.getBestRank());
	   }
	}

//	public static void main(String args[]) throws Exception {
//		RecobelRecommendationService r = new RecobelRecommendationService();
//		RecobellRequest req = new RecobellRequest();
//		req.setIds("2749828");
//		RecobellResponse res = r.getRecommendationList("a001", req);
//
//		for (RecobellProductResult result : res.getResults()) {
//			System.out.println(result.getItemId());
//		}
//	}

}
