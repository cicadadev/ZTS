package gcp.external.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import gcp.ccs.service.BaseService;
import gcp.external.model.SendgoodsSummarymapping;
import gcp.external.model.tmall.TmallStockRequest;
import gcp.external.util.TerminalUtil;
import gcp.oms.model.OmsOrder;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import intune.gsf.common.utils.DateUtil;

@Service
public class SyncStockService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ProductService	productService;

	//재고 구분 상수
	private final String	ERP_GBN_CD_ZTS			= "1";									//자사몰
	private final String	ERP_GBN_CD_SBN			= "2";									//국내외부몰
	private final String	ERP_GBN_CD_T			= "3";									//Tmall
	private final String	ERP_GBN_CD_CN			= "4";									//CNmall
	private final String	ERP_GBN_CD_JD			= "5";									//JDmall

	//TMALL 재고연동 접속정보
	private final String	TMALL_STOCK_URL			= "https://api.cn.amgbs.com/ta/sync";
	private final String	TMALL_STOCK_PARTNER_ID	= "zerotoseven";
	private final String	TMALL_SERVICE_TYPE		= "stockUpdate";
//	private final String	TMALL_SIGN				= "test";

	/**
	 * @Method Name : updateZtsProductStockByErp
	 * @author : peter
	 * @date : 2016. 7. 13.
	 * @description : ERP에서 받은 재고수량을 ZTS에 update
	 *
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String updateZtsProductStockByErp(String storeId, String updId) throws Exception {
		//검색조건 Map
		Map<String, String> srchMap = new HashMap<String, String>();

		//ERP 재고 조회(자사몰): 단품단위 조회
		String gbnCd = ERP_GBN_CD_ZTS;
		srchMap.put("storeId", storeId);
		srchMap.put("gbnCd", gbnCd);
		List<PmsProduct> erpStockList = (List<PmsProduct>) dao.selectList("external.syncstock.getErpStockListOfProductUnit",
				srchMap);
		int updCnt = 0;
		for (PmsProduct erpOne : erpStockList) {
			String productId = erpOne.getProductId();

			for (PmsSaleproduct pspOne : erpOne.getPmsSaleproducts()) {
				String saleproductId = pspOne.getSaleproductId();

				PmsSaleproduct saleproduct = new PmsSaleproduct();
				saleproduct.setStoreId(storeId);
				saleproduct.setProductId(productId);
				saleproduct.setSaleproductId(saleproductId);
				//재고수량 = ERP 재고수량 - 미배송주문수량 + 미배송취소수량
				//미배송주문 및 취소수량 조회: 최근 3개월 미배송주문수량을 차감하여 재고연동
				OmsOrder qtyInfo = (OmsOrder) dao.selectOne("external.syncstock.getOrderQtyCancelQty", saleproductId);
				String totalOrderQty = "0";
				String totalCancelQty = "0";
				if (null != qtyInfo) {
					if (null != qtyInfo.getOrderQty() && !"".equals(qtyInfo.getOrderQty())) {
						totalOrderQty = qtyInfo.getOrderQty();
					}
					if (null != qtyInfo.getCancelQty() && !"".equals(qtyInfo.getCancelQty())) {
						totalCancelQty = qtyInfo.getCancelQty();
					}
				}
				int restQty = pspOne.getErpStockQty().intValue() - Integer.parseInt(totalOrderQty)
						+ Integer.parseInt(totalCancelQty);
				logger.info("Rest Qty [" + restQty + "], Order Qty [" + totalOrderQty + "], Cancel Qty [" + totalCancelQty + "]");
				if (restQty < 0) {
					saleproduct.setRealStockQty(new BigDecimal(0));
				} else {
					saleproduct.setRealStockQty(new BigDecimal(restQty));
				}
				saleproduct.setUpdId(updId);

				//단품 재고수량 update
				dao.update("external.syncstock.updateSaleproductStock", saleproduct);
				updCnt++;
			}

			//상품상태 현행화
			PmsProductSearch search = new PmsProductSearch();
			search.setStoreId(storeId);
			search.setProductId(productId);
			search.setUpdId(updId);
			productService.updateProductStateOne(search);
		}
		String rtnValue = "OWN Stock Update: [" + updCnt + "]건";

		//ERP 재고연동결과 update
		dao.update("external.syncstock.updateErpStockResult", gbnCd);

		return rtnValue;
	}

	/**
	 * @Method Name : insertSbnProductStockByErp
	 * @author : peter
	 * @date : 2016. 7. 21.
	 * @description : ERP에서 받은 재고수량을 사방넷에 update
	 *
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String insertSbnProductStockByErp(String storeId, String updId) throws Exception {
		//검색조건 Map
		Map<String, String> srchMap = new HashMap<String, String>();

		//ERP 재고정보 조회(사방넷): 상품단위 조회
		String gbnCd = ERP_GBN_CD_SBN;
		srchMap.put("storeId", storeId);
		srchMap.put("gbnCd", gbnCd);
		List<PmsProduct> erpStockList = (List<PmsProduct>) dao.selectList("external.syncstock.getErpStockListOfProductUnit",
				srchMap);
		int updCnt = 0;
		for (PmsProduct erpOne : erpStockList) {
			String productId = erpOne.getProductId();

			//단품ID-ERP재고수량 Map 저장
			Map<String, Integer> saleproductMap = new HashMap<String, Integer>();
			for (PmsSaleproduct saleproductOne : erpOne.getPmsSaleproducts()) {
				String saleproductId = saleproductOne.getSaleproductId();
				//재고수량 = ERP 재고수량 - 미배송주문수량 + 미배송취소수량
				//미배송주문 및 취소수량 조회: 최근 3개월 미배송주문수량을 차감하여 재고연동
				OmsOrder qtyInfo = (OmsOrder) dao.selectOne("external.syncstock.getOrderQtyCancelQty", saleproductId);
				String totalOrderQty = "0";
				String totalCancelQty = "0";
				if (null != qtyInfo) {
					if (null != qtyInfo.getOrderQty() && !"".equals(qtyInfo.getOrderQty())) {
						totalOrderQty = qtyInfo.getOrderQty();
					}
					if (null != qtyInfo.getCancelQty() && !"".equals(qtyInfo.getCancelQty())) {
						totalCancelQty = qtyInfo.getCancelQty();
					}
				}
				int restQty = saleproductOne.getErpStockQty().intValue() - Integer.parseInt(totalOrderQty)
						+ Integer.parseInt(totalCancelQty);
				logger.debug("SBN Stock: saleproductId [" + saleproductId + "], Rest Qty [" + restQty + "], Order Qty ["
						+ totalOrderQty + "], Cancel Qty [" + totalCancelQty + "]");
				if (restQty < 0) {
					restQty = 0;
				}
				saleproductMap.put(saleproductId, restQty);
			}

			//상품,단품정보 조회: 일반상품만 조회
			srchMap.clear();
			srchMap.put("storeId", storeId);
			srchMap.put("productId", productId);
			PmsProduct product = (PmsProduct) dao.selectOne("external.syncstock.getProductInfo", srchMap);

			if (null != product) {
				SendgoodsSummarymapping sgsm = makeSendgoodsSummary(storeId, product, saleproductMap);

				//외부몰 재고수량 insert
				dao.insert("external.syncstock.insertSendgoodsSummary", sgsm);
				updCnt++;
			} else {
				logger.error("SBN Stock Error: productId [" + productId + "]");
			}
		}
		String rtnValue = "SBN Stock Update: " + updCnt + "건, SBN Send Result: ";

		//ERP 재고연동결과 update
		dao.update("external.syncstock.updateErpStockResult", gbnCd);

		return rtnValue;
	}

	private SendgoodsSummarymapping makeSendgoodsSummary(String storeId, PmsProduct product, Map<String, Integer> items) {
		//매핑용 모델
		SendgoodsSummarymapping sgsm = new SendgoodsSummarymapping();

		//상품명
		String goodsNm = product.getAdCopy() + product.getName() + "_" + product.getErpProductId();
		sgsm.setGoodsNm(goodsNm);
		//자체코드
		sgsm.setCompanyGoodsCd(product.getProductId());
		//상품상태
		sgsm.setStatus(product.getSaleStateCd());
		//원가
		sgsm.setGoodsCost("'" + product.getSupplyPrice());
		//판매가
		sgsm.setGoodsPrice("'" + product.getSalePrice());
		//TAG(소비자가)
		sgsm.setGoodsConsumerPrice("'" + product.getListPrice());
		//옵션1(옵션항목)
		String options = "";
		int realStockQty = 0;
		if ("Y".equals(product.getOptionYn())) { //옵션이 있는 경우
			for (PmsSaleproduct pspOne : product.getPmsSaleproducts()) {
				//해당 단품ID의 ERP 재고가 있으면 반영, 없으면 단품정보의 재고를 반영
				if (items.containsKey(pspOne.getSaleproductId())) {
					realStockQty = items.get(pspOne.getSaleproductId());
				} else {
					realStockQty = pspOne.getRealStockQty().intValue();
				}
				options += pspOne.getName() + "|" + pspOne.getSaleproductId() + "|^^" + realStockQty + "^^"
						+ pspOne.getAddSalePrice() + "^^" + pspOne.getSaleproductId() + "^^EA^^" + pspOne.getSaleproductStateCd()
						+ ",";
			}
		} else { //옵션이 없는 경우
			for (PmsSaleproduct pspOne : product.getPmsSaleproducts()) {
				//해당 단품ID의 ERP 재고가 있으면 반영, 없으면 단품정보의 재고를 반영
				if (items.containsKey(pspOne.getSaleproductId())) {
					realStockQty = items.get(pspOne.getSaleproductId());
				} else {
					realStockQty = pspOne.getRealStockQty().intValue();
				}
				options += "없음|" + pspOne.getSaleproductId() + "|^^" + realStockQty + "^^" + pspOne.getAddSalePrice()
						+ "^^" + pspOne.getSaleproductId() + "^^EA^^" + pspOne.getSaleproductStateCd() + ",";
			}
		}
		sgsm.setChar1Val(options.substring(0, options.length() - 1));
		logger.debug("General Product Option Value: " + sgsm.getChar1Val());

		return sgsm;
	}

	/**
	 * @Method Name : updateTmallProductStockByErp
	 * @author : peter
	 * @date : 2016. 9. 30.
	 * @description : ERP에서 받은 재고수량을 Tmall에 update
	 *
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String updateTmallProductStockByErp(String storeId, String updId) throws Exception {
		//Request Parameter 생성
		StringBuffer sbParam = new StringBuffer();

		//parameter: partner_id
		sbParam.append("partner_id=").append(TMALL_STOCK_PARTNER_ID);
		//parameter: service_type
		sbParam.append("&service_type=").append(TMALL_SERVICE_TYPE);
		//parameter: service_data
		sbParam.append("&service_data=");

		//=> ERP 재고 조회(Tmall): 단품단위 조회
		String gbnCd = ERP_GBN_CD_T;
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("gbnCd", gbnCd);
		List<PmsSaleproduct> erpStockList = (List<PmsSaleproduct>) dao
				.selectList("external.syncstock.getErpStockListOfSaleproductUnit", srchMap);
		int updCnt = erpStockList.size();
		List<TmallStockRequest> reqList = new ArrayList<TmallStockRequest>();
//		for (PmsSaleproduct pspOne : erpStockList) {
//			String saleproductId = pspOne.getSaleproductId();
//
//			TmallStockRequest stockReq = new TmallStockRequest();
//			//업체 SKU 코드
//			stockReq.setItem_vdr_code(saleproductId);
//			//재고수량 = ERP 재고수량 - 미배송주문수량 + 미배송취소수량
//			//=> 미배송주문 및 취소수량 조회: 최근 3개월 미배송주문수량을 차감하여 재고연동
//			OmsOrder qtyInfo = (OmsOrder) dao.selectOne("external.syncstock.getOrderQtyCancelQty", saleproductId);
//			String totalOrderQty = "0";
//			String totalCancelQty = "0";
//			if (null != qtyInfo) {
//				if (null != qtyInfo.getOrderQty() && !"".equals(qtyInfo.getOrderQty())) {
//					totalOrderQty = qtyInfo.getOrderQty();
//				}
//				if (null != qtyInfo.getCancelQty() && !"".equals(qtyInfo.getCancelQty())) {
//					totalCancelQty = qtyInfo.getCancelQty();
//				}
//			}
//			int restQty = pspOne.getErpStockQty().intValue() - Integer.parseInt(totalOrderQty) + Integer.parseInt(totalCancelQty);
//			logger.info("Rest Qty [" + restQty + "], Order Qty [" + totalOrderQty + "], Cancel Qty [" + totalCancelQty + "]");
//			if (restQty < 0) {
//				restQty = 0;
//			}
//			stockReq.setStk_num(String.valueOf(restQty));
//			//재고연동방식
//			stockReq.setType("1");
//
//			reqList.add(stockReq);
//		}
//		for (int i = 1; i < 3; i++) {
		TmallStockRequest stockReq = new TmallStockRequest();
		//			stockReq.setItem_vdr_code(String.valueOf(i * 10));
		stockReq.setItem_vdr_code("ZS1608290001001");
		stockReq.setStk_num("10");
		stockReq.setType("1");
		reqList.add(stockReq);
//		}
		Map<String, List<TmallStockRequest>> svcDataMap = new HashMap<String, List<TmallStockRequest>>();
		svcDataMap.put("items", reqList);
//		List<Map<String, List<TmallStockRequest>>> itemsList = new ArrayList<Map<String, List<TmallStockRequest>>>();
//		itemsList.add(svcDataMap);
		Gson gson = new Gson();
//		sbParam.append(gson.toJson(itemsList));
		sbParam.append(gson.toJson(svcDataMap));
		//parameter: sign
//		sbParam.append("&sign=").append(TMALL_SIGN);
		//parameter: msg_id
		String msgId = "mid" + DateUtil.getCurrentDate(DateUtil.FORMAT_10);
		sbParam.append("&msg_id=").append(msgId);
		logger.debug("Tmall stockUpdate Request: " + sbParam.toString());

		//Tmall API 호출
//		String tmallRes = JsonUtil.Json2StringUrl(sbParam.toString(), TMALL_STOCK_URL);
		String tmallRes = TerminalUtil.makeHttpConnection(TMALL_STOCK_URL, sbParam.toString());
		logger.debug("Tmall stockUpdate Response: " + tmallRes);
		if (null == tmallRes) {
			logger.error("Tmall stockUpdate API Connection Error!!");
			throw new Exception();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> resMap = mapper.readValue(tmallRes, new TypeReference<HashMap<String, Object>>() {});
			String rtnCode = (String) resMap.get("error_code");
			if (!"SUCCESS".equals(rtnCode)) {
				logger.error("Tmall stockUpdate API Return Error: " + rtnCode);
				throw new Exception();
			}
		}
		logger.debug("Tmall API Success!!");
		String rtnValue = "Tmall Stock Update: [" + updCnt + "]건";

		//ERP 재고연동결과 update
		dao.update("external.syncstock.updateErpStockResult", gbnCd);

		return rtnValue;
	}
}
