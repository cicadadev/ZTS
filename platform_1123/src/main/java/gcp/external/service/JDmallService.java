package gcp.external.service;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import gcp.ccs.service.BaseService;
import gcp.external.model.OmsReceiveordermapping;
import gcp.external.model.jdmall.JDmallCoupon;
import gcp.external.model.jdmall.JDmallItem;
import gcp.external.model.jdmall.JDmallOrder;
import gcp.external.model.jdmall.JDmallSearch;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.JsonUtil;

@Service
public class JDmallService extends BaseService {
	private final Log		logger			= LogFactory.getLog(getClass());

	//주문수집 접속정보
//	private final String	JD_URL			= "https://gw.api.360buy.com/routerjson";
	private final String	JD_URL			= "https://api.jd.com/routerjson";
	private final String	METHOD			= "360buy.order.search";
	private final String	VERSION			= "2.0";
	private final String	PAGE_SIZE		= "100";
//	private final String	ORDER_STATE			= "WAIT_SELLER_STOCK_OUT";
	private final String	ORDER_STATE			= "FINISHED_L";

	//궁중비책 정보
	private final String	GOONG_APP_KEY		= "A4D12C9088602C46D4B1F0BD80E8BCD0";
	private final String	GOONG_APP_SECRET	= "dd7d8227756e4ddaa3677b247090559e";
	private final String	GOONG_ACCESS_TOKEN	= "52e9b71a-4519-4963-a0f0-4c4006f8fcbd";

	//제로투세븐 정보
	private final String	ZTS_APP_KEY			= "F92986136DCED4F5A3D71558182D3360";
	private final String	ZTS_APP_SECRET		= "e44429015f5a4a73880110a65500d309";
	private final String	ZTS_ACCESS_TOKEN	= "38a40803-313a-4f00-8259-c73ba954827d";

	//저장변수
	private final String	GOONG_MALL_ID		= "JD_HK_Goong";
	private final String	ZTS_MALL_ID			= "JD_HK_0to7";
	private final String	RECEIVE_TEL			= "010-6660-2414";
	private final String	RECEIVE_CEL			= "010-6660-2414";
	private final String	RECEIVE_NAME		= "김도형";
	private final String	RECEIVE_ZIPCODE		= "415816";
	private final String	RECEIVE_ADDR		= "경기도 김포시 고촌읍 전호리 748 소재 물류창고 지상 3층 B구간 현대로지스틱스㈜";

	public int getJDmallOrder(String storeId) throws Exception {
		int goongOrderCnt = 0;
		int ztsOrderCnt = 0;

		//환율정보
		String currencyCd = "CNY";
		String cnyAmount = (String) dao.selectOne("external.receiveorder.getExchangeRateAmount", currencyCd);
		logger.info("JDmall Exchange Info: CNY [" + cnyAmount + "]");

		//궁중비책 실행
		goongOrderCnt = processJDmallOrder(storeId, GOONG_MALL_ID, Double.parseDouble(cnyAmount));

		//제로투세븐 실행
		ztsOrderCnt = processJDmallOrder(storeId, ZTS_MALL_ID, Double.parseDouble(cnyAmount));

		int sumOrderCnt = goongOrderCnt + ztsOrderCnt;
		logger.info(GOONG_MALL_ID + " Total Count [" + goongOrderCnt + "] + " + ZTS_MALL_ID + " Total Count [" + ztsOrderCnt
				+ "] = [" + sumOrderCnt + "]");

		return sumOrderCnt;
	}

	private int processJDmallOrder(String storeId, String mallId, Double cnyAmount) throws Exception {
		//JDmall 접속 후 결과
		String jdmallRes = connectJDmall("1", mallId);
		logger.info(mallId + " Response: " + jdmallRes);

		//결과 처리
		int totalOrderCnt = 0;
		if (null != jdmallRes && !"".equals(jdmallRes)) { //성공
			//주문data parsing
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jdmallRes);
			JSONObject orderSearchRes = (JSONObject) jsonObj.get("order_search_response");
			logger.debug(mallId + " JDmall Result: " + orderSearchRes);
			//결과가 오류인 경우
			if (null == orderSearchRes) {
				JSONObject errorRes = (JSONObject) jsonObj.get("error_response");
				logger.error(mallId + " JDmall Error Response: Code [" + (String) errorRes.get("code") + "], Msg ["
						+ (String) errorRes.get("en_desc") + "]");
				throw new Exception();
			}
			Gson gson = new Gson();
			JDmallSearch search = gson.fromJson(gson.toJson(orderSearchRes.get("order_search")), JDmallSearch.class);
			//주문 총갯수: pageSize(100)보다 크면 반복해서 처리
			totalOrderCnt = search.getOrder_total();
			logger.debug(mallId + " JDmall Total Order Count: " + totalOrderCnt);
			//주문 리스트
			logger.debug(mallId + " Order Info Count: " + search.getOrder_info_list().size());
			if (search.getOrder_info_list().size() > 0) {
				//주문수집 Terminal 등록
				insertReceiveOrder(search.getOrder_info_list(), cnyAmount, storeId, mallId);
			}
		} else {
			logger.error(mallId + " JDmall Receive Order API Error");
			throw new Exception();
		}

		//전체 주문건수가 100개 이상이면 page 값을 증가시키며 반복작업
		if (totalOrderCnt > 100) {
			int loop = 0;
			int rest = totalOrderCnt % 100;
			if (rest == 0) {
				loop = totalOrderCnt / 100;
			} else {
				loop = (totalOrderCnt / 100) + 1;
			}
			//1 page는 이미 실행했으므로 2 page부터 처리
			for (int page = 2; page <= loop; page++) {
				jdmallRes = connectJDmall(String.valueOf(page), mallId);
				if (null != jdmallRes && !"".equals(jdmallRes)) { //성공
					//주문data parsing
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = (JSONObject) jsonParser.parse(jdmallRes);
					JSONObject orderSearchRes = (JSONObject) jsonObj.get("order_search_response");
					logger.debug("JDmall Result: " + orderSearchRes);
					//결과가 오류인 경우
					if (null == orderSearchRes) {
						JSONObject errorRes = (JSONObject) jsonObj.get("error_response");
						logger.error("JDmall Error Response: Code [" + (String) errorRes.get("code") + "], Msg ["
								+ (String) errorRes.get("en_desc") + "]");
						throw new Exception();
					}
					Gson gson = new Gson();
					JDmallSearch search = gson.fromJson(gson.toJson(orderSearchRes.get("order_search")), JDmallSearch.class);
					//주문 리스트
					logger.debug(mallId + " Order Info Count: " + search.getOrder_info_list().size());
					if (search.getOrder_info_list().size() > 0) {
						//주문수집 Terminal 등록
						insertReceiveOrder(search.getOrder_info_list(), cnyAmount, storeId, mallId);
					}
				}
			}
		}

		return totalOrderCnt;
	}

	private void insertReceiveOrder(List<JDmallOrder> orderList, double cnyAmt, String storeId, String mallId) throws Exception {
		for (JDmallOrder order : orderList) {
			//주문번호별 할인액 정보 추출
			Map<String, Double> discMap = getCouponPrice(order.getCoupon_detail_list(),
					(double) order.getItem_info_list().size());

			//주문수집모델값 설정
			OmsReceiveordermapping mapping = new OmsReceiveordermapping();
			//주문번호
			mapping.setBoOrderId(order.getOrder_id());
			//Mall ID
			mapping.setMallId(mallId);
			//Mall User ID
			mapping.setMallUserId(mallId);
			//수취인 전화번호
			mapping.setReceiveTel(RECEIVE_TEL);
			//수취인 핸드폰번호
			mapping.setReceiveCel(RECEIVE_CEL);
			//수취인명
			mapping.setReceiveName(RECEIVE_NAME);
			//수취인 우편번호
			mapping.setReceiveZipcode(RECEIVE_ZIPCODE);
			//수취인 주소
			mapping.setReceiveAddr(RECEIVE_ADDR);
			//주문일자
			mapping.setOrderDate(order.getOrder_start_time().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
			//통화코드
			mapping.setCurrencyCode("CNY");
			//LP번호: 주문번호로 저장
			mapping.setLpNo(order.getOrder_id());
			//EMS번호
			mapping.setEmsNo("");
			//alipay_trans_id
			mapping.setAlipayTransId("");
			//partner_trans_id
			mapping.setPartnerTransId("");
			//bo_yn: "J"
			mapping.setBoYn("J");
			//주문단품리스트
			for (JDmallItem item : order.getItem_info_list()) {
				//기등록 주문여부 확인: 이미 등록된 자료는 skip
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("orderId", order.getOrder_id());
				srchMap.put("skuAlias", item.getOuter_sku_id());
				if (dao.selectCount("external.receiveorder.getExistCount", srchMap) > 0) {
					continue;
				}

				logger.debug("skuId: " + item.getSku_id() + ", outerSkuId: " + item.getOuter_sku_id() + ", skuName: "
						+ item.getSku_name() + ", jdPrice: " + item.getJd_price() + ", itemTotal: " + item.getItem_total());
				//단품ID
				mapping.setSkuAlias(item.getOuter_sku_id());
				//단품명
				mapping.setProductName(item.getSku_name());
				//주문수량
				mapping.setSaleCnt(item.getItem_total());
				//외화단가(환율적용전)
				double jdPrice = Double.parseDouble(item.getJd_price()) * Integer.parseInt(item.getItem_total())
						- discMap.get("eachDisc");
				if (null != discMap.get(item.getSku_id()) && !"".equals(discMap.get(item.getSku_id()))) {
					jdPrice = jdPrice - discMap.get(item.getSku_id());
				}
				if (jdPrice < 0.0) {
					jdPrice = 0;
				}
				mapping.setCurrencyPrice(String.valueOf(jdPrice));
				//판매단가(환율적용후)
				int saleCost = (int) Math.round(jdPrice * cnyAmt);
				mapping.setSaleCost(String.valueOf(saleCost));
				//결제금액(판매단가 * 주문수량)
				mapping.setPayCost(String.valueOf(saleCost * Integer.parseInt(item.getItem_total())));

				//주문정보 저장
				dao.insert("external.receiveorder.insertReceiveOrderFromChina", mapping);
			}
		}
	}

	/**
	 * @Method Name : getCouponPrice
	 * @author : peter
	 * @date : 2016. 11. 21.
	 * @description : 주문번호당 할인정보 Map 저장
	 *
	 * @param couponList
	 * @param itemCnt
	 * @return
	 */
	private Map<String, Double> getCouponPrice(List<JDmallCoupon> couponList, double itemCnt) {
		double discountAmt = 0.0d;
		Map<String, Double> discountMap = new HashMap<String, Double>();
		for (JDmallCoupon coupon : couponList) {
			if (null != coupon.getSku_id() && !"".equals(coupon.getSku_id())) {
				discountMap.put(coupon.getSku_id(), Double.parseDouble(coupon.getCoupon_price()));
			} else {
				if (null != coupon.getCoupon_price() && !"".equals(coupon.getCoupon_price())) {
					discountAmt += Double.parseDouble(coupon.getCoupon_price());
				}
			}
		}
		//주문상품 건당 할인액: 소수 둘째자리 버림
		discountMap.put("eachDisc", Math.floor((discountAmt / itemCnt) * 100d) / 100d);

		return discountMap;
	}

	/**
	 * @Method Name : connectJDmall
	 * @author : peter
	 * @date : 2016. 11. 21.
	 * @description : JDmall API 호출
	 *
	 * @param pages
	 * @param mallId
	 * @return
	 * @throws Exception
	 */
	private String connectJDmall(String pages, String mallId) throws Exception {
		//mall ID에 따른 접속환경 변수 설정
		String appKey = "";
		String appSecret = "";
		String accessToken = "";
		if (GOONG_MALL_ID.equals(mallId)) {
			appKey = GOONG_APP_KEY;
			appSecret = GOONG_APP_SECRET;
			accessToken = GOONG_ACCESS_TOKEN;
		} else {
			appKey = ZTS_APP_KEY;
			appSecret = ZTS_APP_SECRET;
			accessToken = ZTS_ACCESS_TOKEN;
		}

		//접속환경 설정
		Map<String, String> sysParams = new HashMap<String, String>();
		sysParams.put("app_key", appKey);
		sysParams.put("access_token", accessToken);
		sysParams.put("method", METHOD);
		sysParams.put("v", VERSION);
		sysParams.put("timestamp", DateUtil.getCurrentDate(DateUtil.FORMAT_1));

		//조회조건 설정
		Map<String, String> bussinessParams = new HashMap<String, String>();
		//날짜설정
		String format = DateUtil.FORMAT_3 + " HHmm";
		String endDate = DateUtil.getCurrentDate(format);
		String startDate = DateUtil.getAddDate(format, endDate, new BigDecimal(-5));
		String strStartDate = DateUtil.convertFormat(startDate, format, DateUtil.FORMAT_1);
		String strEndDate = DateUtil.convertFormat(endDate, format, DateUtil.FORMAT_1);
		logger.debug("strStartDate: " + strStartDate + ", strEndDate: " + strEndDate);
		bussinessParams.put("page", pages);
		bussinessParams.put("page_size", PAGE_SIZE);
		bussinessParams.put("order_state", ORDER_STATE);
		bussinessParams.put("start_date", strStartDate);
		bussinessParams.put("end_date", strEndDate);

		ObjectMapper mapper = new ObjectMapper();
		sysParams.put("360buy_param_json", mapper.writeValueAsString(bussinessParams));
		sysParams.put("sign", sign(sysParams, appSecret));

		//parameter setting
		StringBuilder sbParams = new StringBuilder();
		for (String key : sysParams.keySet()) {
			sbParams.append(key).append("=").append(sysParams.get(key)).append("&");
		}
		logger.debug("Param: " + sbParams.substring(0, sbParams.length() - 1));

		String jdmallRes = JsonUtil.Json2StringUrl(sbParams.substring(0, sbParams.length() - 1), JD_URL);

		return jdmallRes;
	}

	/**
	 * @Method Name : sign
	 * @author : peter
	 * @date : 2016. 11. 21.
	 * @description : 보안키 설정
	 *
	 * @param sysParams
	 * @param appSecret
	 * @return
	 * @throws Exception
	 */
	private String sign(Map<String, String> sysParams, String appSecret) throws Exception {
		Map<String, String> sortedParams = new TreeMap<String, String>(sysParams);

		StringBuilder sbSecret = new StringBuilder();
		sbSecret.append(appSecret);
		for (String key : sortedParams.keySet()) {
			sbSecret.append(key).append(sortedParams.get(key));
		}
		sbSecret.append(appSecret);

		return getEncryptMD5(sbSecret.toString()).toUpperCase();
	}

	/**
	 * @Method Name : getEncryptMD5
	 * @author : peter
	 * @date : 2016. 11. 21.
	 * @description : 암호화
	 *
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private String getEncryptMD5(String text) throws Exception {
		StringBuffer sbuf = new StringBuffer();

		MessageDigest mDigest = MessageDigest.getInstance("MD5");
		mDigest.update(text.getBytes());

		byte[] msgStr = mDigest.digest();

		for (int i = 0; i < msgStr.length; i++) {
			String tmpEncTxt = Integer.toHexString((int) msgStr[i] & 0x00ff);
			sbuf.append(tmpEncTxt);
		}

		return sbuf.toString();
	}
}
