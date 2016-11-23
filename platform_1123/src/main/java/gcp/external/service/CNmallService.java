package gcp.external.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.OmsReceiveordermapping;

@Service
public class CNmallService extends BaseService {
	private final Log		logger	= LogFactory.getLog(getClass());

	//저장변수
	private final String	MALL_ID			= "0to7_CN";
	private final String	RECEIVE_TEL		= "01066602414";
	private final String	RECEIVE_CEL		= "01066602414";
	private final String	RECEIVE_NAME	= "김도형";
	private final String	RECEIVE_ZIPCODE	= "415816";
	private final String	RECEIVE_ADDR	= "경기도 김포시 고촌읍 전호리 748 소재 물류창고 지상 3층 B구간 현대로지스틱스㈜";

	public int getCNmallOrder(HttpServletRequest request, String storeId) throws Exception {
		request.setCharacterEncoding("utf-8");
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			String[] data = request.getParameterValues(name);
			if (data != null) {
				for (String eachdata : data) {
					logger.debug(name + " : " + eachdata);
				}
			}
		}

		//parametr name
		String[] paramNames = new String[] { "order_id", "mall_id", "receive_tel", "receive_cel", "receive_name",
				"receive_zipcode", "receive_addr", "order_date", "product_name", "sale_cnt", "sku_alias", "currency_code",
				"currency_price", "alipay_trans_id", "partner_trans_id" };
		//parameter size: 값이 (-)이면 고정
		int[] paramSizes = new int[] { 30, 30, 30, 30, 30, -5, 100, 14, 100, 10, 20, 3, 100, 100, 100 };

		//parameter data 유효성 체크
		String resMsg = "";
		for (int i = 0, paramCnt = paramNames.length; i < paramCnt; i++) {
			resMsg = checkValidation(paramNames[i], request.getParameterValues(paramNames[i]), paramSizes[i]);

			if (!"".equals(resMsg)) {
				break;
			}
		}

		if (!"".equals(resMsg)) {
			logger.debug("Parameter Error: " + resMsg);
			return 0;
		}

		int orderCnt = insertReceiveOrder(request, storeId);

		return orderCnt;
	}

	private int insertReceiveOrder(HttpServletRequest request, String storeId) throws Exception {
		//전체 주문건수
		int dataCnt = 0;
		int insCnt = 0;

		//파라미터 설정
		String[] orderId = request.getParameterValues("order_id");
//		String[] mallId = request.getParameterValues("mall_id");
//		String[] receiveTel = request.getParameterValues("receive_tel");
//		String[] receiveCel = request.getParameterValues("receive_cel");
//		String[] receiveName = request.getParameterValues("receive_name");
//		String[] receiveZipcode = request.getParameterValues("receive_zipcode");
//		String[] receiveAddr = request.getParameterValues("receive_addr");
		String[] orderDate = request.getParameterValues("order_date");
		String[] productName = request.getParameterValues("product_name");
		String[] saleCnt = request.getParameterValues("sale_cnt");
		String[] skuAlias = request.getParameterValues("sku_alias");
		String[] currencyCode = request.getParameterValues("currency_code");
		String[] currencyPrice = request.getParameterValues("currency_price");
		String[] alipayTransId = request.getParameterValues("alipay_trans_id");
		String[] partnerTransId = request.getParameterValues("partner_trans_id");

		//환율정보(USD)
		String currencyCd = currencyCode[0];
		String currencyAmt = (String) dao.selectOne("external.receiveorder.getExchangeRateAmount", currencyCd);
		logger.debug(currencyCd + " Exchange Rate: " + currencyAmt);
		try {
			dataCnt = orderId.length;
			for (int i = 0; i < dataCnt; i++) {
				//기등록 주문여부 확인: 이미 등록된 자료는 skip
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("orderId", orderId[i]);
				srchMap.put("skuAlias", skuAlias[i]);
				if (dao.selectCount("external.receiveorder.getExistCount", srchMap) > 0) {
					continue;
				}

				//receive_order table model
				OmsReceiveordermapping mapping = new OmsReceiveordermapping();
				//주문번호
				mapping.setOrderId(orderId[i]);
				//Mall ID
				mapping.setMallId(MALL_ID);
				//Mall User ID
				mapping.setMallUserId(MALL_ID);
				//수취인 전화번호: 고정값
				mapping.setReceiveTel(RECEIVE_TEL);
				//수취인 핸드폰번호: 고정값
				mapping.setReceiveCel(RECEIVE_CEL);
				//수취인명: 고정값
				mapping.setReceiveName(RECEIVE_NAME);
				//수취인 우편번호: 고정값
				mapping.setReceiveZipcode(RECEIVE_ZIPCODE);
				//수취인 주소: 고정값
				mapping.setReceiveAddr(RECEIVE_ADDR);
				//단품ID
				mapping.setSkuAlias(skuAlias[i]);
				//단품명
				mapping.setProductName(productName[i]);
				//주문수량
				mapping.setSaleCnt(saleCnt[i]);
				//판매단가(환율적용후)
				Double saleCost = (double) Math.round(Double.parseDouble(currencyPrice[i]) * Double.parseDouble(currencyAmt));
				mapping.setSaleCost(String.valueOf(saleCost));
				//결제금액(판매단가 * 주문수량)
				mapping.setPayCost(String.valueOf(saleCost * Integer.parseInt(saleCnt[i])));
				//주문일자
				mapping.setOrderDate(orderDate[i]);
//						(tradeOne.get("created").toString()).replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
				//환율코드
				mapping.setCurrencyCode(currencyCode[i]);
				//외화단가(환율적용전)
				mapping.setCurrencyPrice(currencyPrice[i]);
				//alipay trans ID
				mapping.setAlipayTransId(alipayTransId[i]);
				//partner trans ID
				mapping.setPartnerTransId(partnerTransId[i]);
				//bo_yn: "C"
				mapping.setBoYn("C");

				insCnt += dao.insert("external.receiveorder.insertReceiveOrderFromChina", mapping);
			}

			if (dataCnt != insCnt) {
				throw new Exception("수신주문: " + dataCnt + "건, 등록주문: " + insCnt + "건");
			}
		} catch (Exception e) {
			throw new Exception("ERROR : " + e.getMessage());
		}
		logger.info("전체 입력건수: " + dataCnt);

		return dataCnt;
	}

	private String checkValidation(String paramName, String[] paramVals, int paramSize) {
		if (paramName != null && paramName.length() > 0) {
			if (paramVals != null) {
				for (String paramVal : paramVals) {
					if (paramVal != null && paramVal.length() > 0) {
						if (!checkValSize(paramVal, paramSize)) {
							if (paramSize > 0) {
								return "ERROR : " + paramName + " 길이가 " + paramSize + " 이하 여야 합니다.";
							} else {
								return "ERROR : " + paramName + " 길이가 " + (paramSize * -1) + " 여야 합니다.";
							}
						}
					} else {
						return "ERROR : " + paramName + " 값이 없습니다.";
					}
				}
			} else {
				return "ERROR : " + paramName + " 값이 없습니다.";
			}
		} else {
			return "ERROR : 파라미터 이름이 없습니다.";
		}

		return "";
	}

	private boolean checkValSize(String paramVal, int paramSize) {
		boolean isPassed = true;

		if (paramSize > 0) {
			if (paramVal.length() > paramSize) {
				isPassed = false;
			}
		} else {
			if (paramVal.length() != paramSize * -1) {
				isPassed = false;
			}
		}

		return isPassed;
	}
}
