package gcp.external.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import gcp.ccs.service.BaseService;
import gcp.external.model.ErpPresent;
import gcp.external.model.OmsReceiveordermapping;
import gcp.external.model.tmall.TmallItem;
import gcp.external.model.tmall.TmallOrder;
import gcp.external.model.tmall.TmallResponse;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.JsonUtil;

@Service
public class TmallService extends BaseService {
	private final Log		logger				= LogFactory.getLog(getClass());

	//주문수집 접속정보
	private final String	TMALL_ORDER_URL				= "https://api.cn.amgbs.com/ta/sync";
	private final String	TMALL_ORDER_PARTNER_ID		= "zerotoseven";
	private final String	TMALL_ORDER_SERVICE_TYPE	= "orderSearch";
	private final String	TMALL_LP_SERVICE_TYPE		= "ICBOrderSearch";

	//저장변수
	private final String	MALL_ID				= "TMALL";
	private final String	RECEIVE_TEL					= "010-6660-2414";
	private final String	RECEIVE_CEL					= "010-6660-2414";
	private final String	RECEIVE_NAME		= "김도형";
	private final String	RECEIVE_ZIPCODE		= "415816";
	private final String	RECEIVE_ADDR		= "경기도 김포시 고촌읍 전호리 748 소재 물류창고 지상 3층 B구간 현대로지스틱스㈜";

	/**
	 * @Method Name : getTmallOrder
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : Tmall 주문수집
	 *
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	public int getTmallOrder(String storeId, String startDate, String endDate) throws Exception {
		//날짜설정
		String startTime = "";
		String endTime = "";
		if ((null != startDate && !"".equals(startDate)) && (null != endDate && !"".equals(endDate))) {
			startTime = startDate + " 00:00:00";
			endTime = endDate + " 23:59:59";
		} else {
			endTime = DateUtil.getCurrentDate(DateUtil.FORMAT_1);
			startTime = DateUtil.getAddDate(DateUtil.FORMAT_2, endTime, new BigDecimal(-1)) + " 00:00:00";
		}
		logger.debug("startTime: " + startTime + ", endTime: " + endTime);

		//환율조회
		String currencyCd = "CNY";
		String cnyAmount = (String) dao.selectOne("external.receiveorder.getExchangeRateAmount", currencyCd);
		Double cnyAmt = Double.parseDouble(cnyAmount);
		logger.info("Tmall Exchange Info: CNY [" + cnyAmount + "]");

		//Tmall API 호출
		String tmallRes = JsonUtil.Json2StringUrl(makeParameters(startTime, endTime, "", 1), TMALL_ORDER_URL);
//		String tmallRes = TerminalUtil.makeHttpConnection(TMALL_ORDER_URL, makeParameters(startTime, endTime, 1));
		logger.debug("1 Page Tmall OrderSearch Response: " + tmallRes);
		int totalCount = 0;
		if (null == tmallRes) {
			logger.error("1 Page Tmall OrderSearch API Connection Error!!");
			throw new Exception();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> resMap = mapper.readValue(tmallRes, new TypeReference<HashMap<String, Object>>() {});
			String rtnCode = (String) resMap.get("error_code");
			if (!"SUCCESS".equals(rtnCode)) {
				logger.error("1 Page Tmall OrderSearch API Return Error: [" + (String) resMap.get("error_msg") + "]");
				throw new Exception();
			} else {
				Gson gson = new Gson();
				TmallResponse resData = gson.fromJson(gson.toJson(resMap.get("response")), TmallResponse.class);
				logger.info("Tmall Total Count [" + resData.getTotal_count() + "], Total Page [" + resData.getTotal_page() + "]");
				totalCount = Integer.parseInt(resData.getTotal_count());
				int totalPage = Integer.parseInt(resData.getTotal_page());
				//주문 리스트
				List<TmallOrder> orderList = resData.getOrders();
//				logger.debug("Current Order Count: " + orderList.size());
				if (orderList.size() > 0) {
					insertReceiveOrder(orderList, cnyAmt, storeId);
				}
				//페이지가 많으면 loop
				if (totalPage > 1) {
					repeatTmallOrder(storeId, totalPage, cnyAmt, startTime, endTime);
				}
			}
		}
		logger.debug("Tmall API Success!!: Total Count [" + totalCount + "]");

		return totalCount;
	}

	/**
	 * @Method Name : repeatTmallOrder
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : Tmall 주문수집 반복처리
	 *
	 * @param storeId
	 * @param totalPage
	 * @param cnyAmt
	 * @param startTime
	 * @param endTime
	 * @throws Exception
	 */
	private void repeatTmallOrder(String storeId, int totalPage, double cnyAmt, String startTime, String endTime)
			throws Exception {
		for (int page = 2; page <= totalPage; page++) {
			//Tmall API 호출
			String tmallRes = JsonUtil.Json2StringUrl(makeParameters(startTime, endTime, "", page), TMALL_ORDER_URL);
//			String tmallRes = TerminalUtil.makeHttpConnection(TMALL_ORDER_URL, makeParameters(startTime, endTime, page));
			logger.debug(page + " Page Tmall OrderSearch Response: " + tmallRes);
			if (null == tmallRes) {
				logger.error(page + " Page Tmall OrderSearch API Connection Error!!");
				throw new Exception();
			} else {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> resMap = mapper.readValue(tmallRes, new TypeReference<HashMap<String, Object>>() {});
				String rtnCode = (String) resMap.get("error_code");
				if (!"SUCCESS".equals(rtnCode)) {
					logger.error(page + " Page Tmall OrderSearch API Return Error: [" + (String) resMap.get("error_msg") + "]");
					throw new Exception();
				} else {
					Gson gson = new Gson();
					TmallResponse resData = gson.fromJson(gson.toJson(resMap.get("response")), TmallResponse.class);
					//주문 리스트
					List<TmallOrder> orderList = resData.getOrders();
//					logger.debug("Current Order Count: " + orderList.size());
					if (orderList.size() > 0) {
						insertReceiveOrder(orderList, cnyAmt, storeId);
					}
				}
			}
		}
	}

	/**
	 * @Method Name : makeParameters
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : 통신 파라미터 설정
	 *
	 * @param startTime
	 * @param endTime
	 * @param curPage
	 * @return
	 */
	private String makeParameters(String startTime, String endTime, String orderId, int curPage) {
		//Request Parameter 생성
		StringBuffer sbParam = new StringBuffer();

		//parameter: partner_id
		sbParam.append("partner_id=").append(TMALL_ORDER_PARTNER_ID);
		//parameter: service_type
		if (null != orderId && !"".equals(orderId)) {
			sbParam.append("&service_type=").append(TMALL_LP_SERVICE_TYPE);
		} else {
			sbParam.append("&service_type=").append(TMALL_ORDER_SERVICE_TYPE);
		}
		//parameter: service_data
		sbParam.append("&service_data=");

		//service_data 변수
		Map<String, String> svcDataMap = new HashMap<String, String>();
		if (null != orderId && !"".equals(orderId)) {
			svcDataMap.put("tid", orderId);
		} else {
			svcDataMap.put("start_time", startTime);
			svcDataMap.put("end_time", endTime);
			svcDataMap.put("page", String.valueOf(curPage));
			svcDataMap.put("per_page", "15");
		}
		Gson gson = new Gson();
		sbParam.append(gson.toJson(svcDataMap));
		//parameter: msg_id
		String msgId = "mid" + DateUtil.getCurrentDate(DateUtil.FORMAT_10) + ((int) (Math.random() * 1000) + 1);
		sbParam.append("&msg_id=").append(msgId);
		if (null != orderId && !"".equals(orderId)) {
			logger.debug("Tmall ICBSOrderSearch Request: " + sbParam.toString());
		} else {
			logger.debug(curPage + " Page Tmall OrderSearch Request: " + sbParam.toString());
		}

		return sbParam.toString();
	}

	/**
	 * @Method Name : insertReceiveOrder
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : 주문수집 데이터 터미널에 저장
	 *
	 * @param orderList
	 * @param cnyAmt
	 * @param storeId
	 * @throws Exception
	 */
	private void insertReceiveOrder(List<TmallOrder> orderList, double cnyAmt, String storeId) throws Exception {
		for (TmallOrder order : orderList) {
			//receive_order table model
			OmsReceiveordermapping mapping = new OmsReceiveordermapping();

			//주문번호
			mapping.setOrderId(order.getTid());
			//LP번호: 주문단위로 하나만 존재
			String lpNo = getLpNoOfOrderId(order.getTid());
			//LP번호가 없으면 skip
			if (null == lpNo || "".equals(lpNo)) {
				continue;
			}
			mapping.setLpNo(lpNo);
			//Mall ID
			mapping.setMallId(MALL_ID);
			//Mall User ID
			mapping.setMallUserId(MALL_ID);
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
			String orderDate = order.getCreated().replace("-", "").replace(" ", "").replace(":", "");
			logger.debug("orderDate: " + orderDate);
			mapping.setOrderDate(orderDate);
			//통화코드
			mapping.setCurrencyCode("CNY");
			//alipay_trans_id
			mapping.setAlipayTransId("");
			//partner_trans_id
			mapping.setPartnerTransId("");
			//bo_yn: "T"
			mapping.setBoYn("T");
			//주문단품리스트
			for (TmallItem item : order.getItems()) {
				//기등록 주문여부 확인: 이미 등록된 자료는 skip
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("orderId", order.getTid());
				srchMap.put("skuAlias", item.getSku_code());
				srchMap.put("orderDate", orderDate);
				if (dao.selectCount("external.receiveorder.getExistCount", srchMap) > 0) {
					continue;
				}

				//단품ID
				mapping.setSkuAlias(item.getSku_code());
				//상품명
				mapping.setProductName(item.getTitle());
				//주문수량
				mapping.setSaleCnt(item.getNum());
				//외화단가 계산: 실상품결제금액(관세포함) - 품목별관세금액 => 내 방식
				//관세제외 상품실결제금액 = 상품금액*수량 - 주문할인-상품품목할인 => agent 방식
				double cnyPayment = Double.parseDouble(item.getPayment()) - Double.parseDouble(item.getSub_order_tax_fee());
				double cnySalePrice = Math.round(cnyPayment / Double.parseDouble(item.getNum()));
				if (cnySalePrice < 0.0) {
					logger.debug("oid: " + item.getOid());
					cnySalePrice = 0;
				}
				//외화단가
				mapping.setCurrencyPrice(String.valueOf(cnySalePrice));
				//판매단가
				mapping.setSaleCost(String.valueOf(Math.round(cnySalePrice * cnyAmt)));
				//결제금액
				mapping.setPayCost(String.valueOf(Math.round(cnyPayment * cnyAmt)));
				//사은품코드, 사은품수량
				ErpPresent present = getPresentInfo(storeId, item.getSku_code(), order.getCreated());
				if (null != present && !"".equals(present)) {
					logger.debug("presentId: " + present.getGiftbocode() + ", qty: " + present.getGiftqty());
					//사은품코드
					mapping.setOrderEtc2(present.getGiftbocode());
					//사은품수량: 사은품기본수량 * 주문수량
					int giftQty = Integer.parseInt(present.getGiftqty()) + Integer.parseInt(item.getNum());
					mapping.setOrderEtc3(String.valueOf(giftQty));
				}

				logger.debug("id:" + mapping.getSkuAlias() + ",name:" + mapping.getProductName() + ",cnyAmt:"
						+ mapping.getCurrencyPrice() + ",payAmt:" + mapping.getPayCost());

				dao.insert("external.receiveorder.insertReceiveOrderFromChina", mapping);
			}
		}
	}

	/**
	 * @Method Name : getPresentInfo
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : 사은품정보 조회
	 *
	 * @param storeId
	 * @param saleproductId
	 * @param orderDate
	 * @return
	 */
	private ErpPresent getPresentInfo(String storeId, String saleproductId, String orderDate) {
		ErpPresent present = null;

		Map<String, String> boMap = new HashMap<String, String>();
		boMap.put("storeId", storeId);
		boMap.put("saleproductId", saleproductId);
		String productId = (String) dao.selectOne("external.receiveorder.getProductId", boMap);
		if (null != productId && !"".equals(productId)) {
			Map<String, String> erpMap = new HashMap<String, String>();
			erpMap.put("productId", productId);
			erpMap.put("orderDate", orderDate.replace("-", "").substring(0, 8));
			logger.debug("date: " + orderDate.replace("-", "").substring(0, 8));
			present = (ErpPresent) erp.selectOne("external.erp.getErpTmallPresentInfo", erpMap);
		}

		return present;
	}

	/**
	 * @Method Name : getLpNoOfOrderId
	 * @author : peter
	 * @date : 2016. 11. 17.
	 * @description : 주문번호별 LP번호 조회
	 *
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getLpNoOfOrderId(String orderId) throws Exception {
		String lpNo = "";

		//Tmall API 호출
		String tmallRes = JsonUtil.Json2StringUrl(makeParameters("", "", orderId, 0), TMALL_ORDER_URL);
//		String tmallRes = TerminalUtil.makeHttpConnection(TMALL_ORDER_URL, makeParameters("", "", orderId, 0));
		logger.debug("Tmall ICBOrderSearch Response: " + tmallRes);
		if (null == tmallRes) {
			logger.error("Tmall ICBOrderSearch API Connection Error!!");
			throw new Exception();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> resMap = mapper.readValue(tmallRes, new TypeReference<HashMap<String, Object>>() {});
			String rtnCode = (String) resMap.get("error_code");
			if (!"SUCCESS".equals(rtnCode)) {
				if ("NO_RECORD".equals(rtnCode)) {
					logger.debug("Tmall LP NO not Exist: Order ID [" + orderId + "]");
				} else {
					logger.error("Tmall ICBOrderSearch API Return Error: Order ID [" + orderId + "], errorMsg ["
							+ (String) resMap.get("error_msg") + "]");
				}
				lpNo = null;
			} else {
				Gson gson = new Gson();
				Map<String, Object> lpNoMap = mapper.readValue(gson.toJson(resMap.get("response")),
						new TypeReference<HashMap<String, Object>>() {});
				List<Map<String, String>> lpNoList = (List<Map<String, String>>) lpNoMap.get("response");
				lpNo = (String) lpNoList.get(0).get("lgorder_code");
				logger.debug("LP No: " + lpNo);
			}
		}

		return lpNo;
	}
}
