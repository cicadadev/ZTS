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

import gcp.ccs.service.BaseService;
import gcp.external.model.OmsReceiveordermapping;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.service.CommonOmsService;
import gcp.pms.model.PmsProduct;
import intune.gsf.common.utils.DateUtil;

@Service
public class ChinaReceiveOrderService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	//주문 공통 서비스 선언
	@Autowired
	private CommonOmsService			commonOmsService;

	//중국몰 ID 선언
//	private final String				TMALL_ID		= "TMALL";
//	private final String				CNMALL_ID		= "0to7_CN";
//	private final String				JDMALL_GOONG_ID	= "JD_HK_Goong";
//	private final String				JDMALL_ZTS_ID	= "JD_HK_0to7";

	/**
	 * @Method Name : insertSbnOrderToBo
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 사방넷 주문내역 BO 등록
	 *
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String processChinaReceiveOrder(String storeId, String updId, String mallType) throws Exception {
		//조회용 Map
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("insId", updId);
		srchMap.put("mallType", mallType);

		//사전오류 체크 update
//		((ChinaReceiveOrderService) getThis()).updatePreProcessReceiveOrderNewTx(srchMap);
		//사전 오류 처리
		commonOmsService.updatePretreatmentRoNewTx("SBN");

		//개별 오류 처리
		commonOmsService.updateCheckEachRoNewTx(srchMap);

		//중국몰 주문정보 모든 처리후 조회
		List<OmsReceiveordermapping> ormList = (List<OmsReceiveordermapping>) dao
				.selectList("external.receiveorder.getReceiveOrderList", srchMap);

		//주문번호별 자료저장을 위한 변수
		Map<String, ArrayList<OmsReceiveordermapping>> ormGroupMap = new HashMap<String, ArrayList<OmsReceiveordermapping>>();
		Map<String, PmsProduct> productMap = new HashMap<String, PmsProduct>();
		//바로 전 정보 저장변수: 중복체크용
		String chinaOldOrderId = ""; //주문번호
		for (OmsReceiveordermapping ormOne : ormList) {
			String chinaNewOrderId = ormOne.getOrderId();
			logger.debug("SEQ: " + ormOne.getSeq() + ", Old OrderID: " + chinaOldOrderId + ", New OrderID: " + chinaNewOrderId);

			//새로운 주문번호인 경우
			if (!chinaNewOrderId.equals(chinaOldOrderId)) {
				//새로운 주문정보를 중복체크용으로 old 변수에 저장
				chinaOldOrderId = chinaNewOrderId;

				//주문번호별 Terminal 정보그룹 저장: 신규 Key(boOrderId) 생성
				ArrayList<OmsReceiveordermapping> ormGroupList = new ArrayList<OmsReceiveordermapping>();
				ormGroupList.add(ormOne);
				ormGroupMap.put(chinaNewOrderId, ormGroupList);
			}
			//동일 주문번호인 경우
			else {
				//주문번호별 Terminal 정보그룹 저장: 기존 Key(boOrderId)에 추가
				ArrayList<OmsReceiveordermapping> ormElements = ormGroupMap.get(chinaOldOrderId);
				ormElements.add(ormOne);
				ormGroupMap.put(chinaOldOrderId, ormElements);
//				int idx = ormGroupMap.get(boOrderId).size();
//				ormGroupMap.get(boOrderId).set(idx, ormOne);
			}

			//상품,단품정보 저장
			PmsProduct product = commonOmsService.getProductInfo(storeId, ormOne.getSkuAlias());
			productMap.put(ormOne.getSeq(), product);
		}

		//BO 주문데이터 생성: 주문번호별 처리
		int successCnt = 0;
		int errorCnt = 0;
		for (String orderInfo : ormGroupMap.keySet()) {
			//현 주문번호에 속한 Order List
			List<OmsReceiveordermapping> ormGroup = ormGroupMap.get(orderInfo);
			try {
				((ChinaReceiveOrderService) getThis()).insertChinaOrderDataNewTx(orderInfo, ormGroup, productMap, storeId, updId);
				successCnt++;
				logger.error("ReceiveOrder Success: Order Info [" + orderInfo + "]");
			} catch (Exception e) {
				logger.error("ReceiveOrder Exception: Order Info [" + orderInfo + "]");
				errorCnt++;
				e.printStackTrace();
				continue;
			}
		}
		String rtnValue = "총 주문수 " + (successCnt + errorCnt) + "건 [성공-" + successCnt + ", 실패-" + errorCnt + "]";

		return rtnValue;
	}

	/**
	 * @Method Name : updatePreProcessReceiveOrder
	 * @author : peter
	 * @date : 2016. 11. 10.
	 * @description : 터미널자료 사전 오류처리
	 *
	 * @param srchMap
	 */
	/*	public void updatePreProcessReceiveOrderNewTx(Map<String, String> srchMap) {
			//사전 오류 처리
			commonOmsService.preTreatmentReceiveOrder("CHINA");
	
			//개별 오류 처리
			commonOmsService.checkEachReceiveOrder(srchMap);
		}*/

	/**
	 * @Method Name : insertChinaOrderDataNewTx
	 * @author : peter
	 * @date : 2016. 8. 11.
	 * @description : 수집된 주문자료로 BO table에 입력
	 *
	 * @param ormGroupMap
	 * @param productMap
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void insertChinaOrderDataNewTx(String orderInfo, List<OmsReceiveordermapping> ormGroup,
			Map<String, PmsProduct> productMap, String storeId, String updId) throws Exception {
		logger.debug("insertChinaOrderDataNewTx: Order ID [" + orderInfo + "], Count: " + ormGroup.size() + "]");

		//주문마스터table 입력
		OmsOrder order = commonOmsService.setOmsOrder(ormGroup.get(0));
		order.setStoreId(storeId);
		order.setInsId(updId);
		order.setUpdId(updId);
		dao.insert("common.insert.OmsOrder", order);
		//신규 주문번호
		String boOrderId = order.getOrderId();
		logger.debug("BO Order ID: " + boOrderId);

		//결제table 입력
		OmsPayment payment = commonOmsService.setOmsPayment(ormGroup.get(0));
		payment.setOrderId(boOrderId);
		payment.setInsId(updId);
		payment.setUpdId(updId);
		dao.insert("common.insert.OmsPayment", payment);

		String sbnOldReceiveName = ""; //이전 수취자명
		String sbnOldReceiveAddr = ""; //이전 수취자 주소
		BigDecimal boDeliveryAddressNo = new BigDecimal(0); //BO 배송지번호
		BigDecimal logisticsInoutNo = new BigDecimal(0); //보세일 경우 사용할 배송번호

		//주문번호별 주문총결제금액의 합계
		int totalOrderAmt = 0;
		//주문별,배송지번호별 배송정책번호 리스트
		List<BigDecimal> policyList = null;
		//보세 존재여부
		boolean islocalDeliveryExist = false;
		for (OmsReceiveordermapping ormUnit : ormGroup) {
			//주문총결제금액 누적
			totalOrderAmt += Integer.parseInt(ormUnit.getPayCost());

			//배송지table 입력: 동일배송지가 아닌 경우만 등록(수취인명과 수취주소가 동일하면 동일배송지로 인식)
			if (!ormUnit.getReceiveName().trim().equals(sbnOldReceiveName)
					|| !ormUnit.getReceiveAddr().trim().equals(sbnOldReceiveAddr)) {
				//새로운 수취자명과 주소를 중복체크용으로 변수에 저장
				sbnOldReceiveName = ormUnit.getReceiveName().trim();
				sbnOldReceiveAddr = ormUnit.getReceiveAddr().trim();

				//배송지table 입력
				OmsDeliveryaddress address = commonOmsService.setDeliveryAddress(ormUnit, true);
				address.setOrderId(boOrderId);
				if (ormUnit.getLpNo().contains("LP")) {
					address.setNote("[" + ormUnit.getLpNo() + "]");
				}
				address.setInsId(updId);
				address.setUpdId(updId);
				dao.insert("common.insert.OmsDeliveryaddress", address);
				//신규 배송지 번호
				boDeliveryAddressNo = address.getDeliveryAddressNo();
				//배송정책번호 리스트 재설정
				policyList = new ArrayList<BigDecimal>();
			}

			//상품,단품정보 조회
			PmsProduct product = productMap.get(ormUnit.getSeq());
			logger.debug("Seq: " + ormUnit.getSeq() + "Product ID: " + product.getProductId() + ", Saleproduct ID: "
					+ product.getPmsSaleproduct().getSaleproductId());

			//일반,세트,사은품 상품정보 저장
			List<OmsOrderproduct> orderProductList = commonOmsService.setOrderProduct(ormUnit, product);

			//주문상품별 처리
			boolean isExistSub = false;
			for (OmsOrderproduct oopOne : orderProductList) {
				//배송정책table 등록: 기등록 아닌 경우
				if (isDeliveryPolicyNotExist(policyList, oopOne.getDeliveryPolicyNo())) {
					//배송정책번호 리스트 추가
					policyList.add(oopOne.getDeliveryPolicyNo());

					//배송정책 조회
					OmsDelivery delivery = commonOmsService.setDelivery(oopOne.getDeliveryPolicyNo());
					//주문번호
					delivery.setOrderId(boOrderId);
					//배송지번호
					delivery.setDeliveryAddressNo(boDeliveryAddressNo);
					dao.insert("common.insert.OmsDelivery", delivery);
				}

				//주문상품table 등록
				//구성상품의 상위주문상품일련번호 update를 위해 설정
				if ("ORDER_PRODUCT_TYPE_CD.SUB".equals(oopOne.getOrderProductTypeCd())) {
					isExistSub = true;
				}
				oopOne.setStoreId(storeId);
				oopOne.setOrderId(boOrderId);
				//주문상품일련번호
				oopOne.setOrderProductNo((BigDecimal) dao.selectOne("external.receiveorder.getNewOrderProductNo", boOrderId));
				oopOne.setInsId(updId);
				oopOne.setUpdId(updId);
				//배송지번호
				oopOne.setDeliveryAddressNo(boDeliveryAddressNo);
				dao.insert("common.insert.OmsOrderproduct", oopOne);

				//출고완료이면 주문입출고내역도 생성
				if ("ORDER_PRODUCT_STATE_CD.SHIP".equals(oopOne.getOrderProductStateCd())) {
					//보세 존재여부
					islocalDeliveryExist = true;

					//배송번호는 최초 하나만 추출하여 사용
					if (logisticsInoutNo.intValue() == 0) {
						logisticsInoutNo = (BigDecimal) dao.selectOne("oms.logistics.getMaxLogisticsInoutNo", null);
					}

					//주문상품유형코드가 세트인 경우는 주문입출고 등록 skip
					if (!"ORDER_PRODUCT_TYPE_CD.SET".equals(oopOne.getOrderProductTypeCd())) {
						OmsLogistics logistics = new OmsLogistics();

						logistics.setLogisticsInoutNo(logisticsInoutNo);
						logistics.setOrderId(oopOne.getOrderId());
						logistics.setOrderProductNo(oopOne.getOrderProductNo());
						logistics.setIoSeq((BigDecimal) dao.selectOne("oms.logistics.getIoSeq", null));
						logistics.setWarehouseInoutTypeCd("WAREHOUSE_INOUT_TYPE_CD.OUT");
						logistics.setOutQty(oopOne.getOrderQty());
						logistics.setLogisticsStateCd("LOGISTICS_STATE_CD.CONFIRM");
						logistics.setCompleteDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));
						logistics.setTrackingIfYn("Y");
						//주문입출고table 등록
						dao.insert("common.insert.OmsLogistics", logistics);
					}

					//주문상품table의 출고수량: 주문수량과 동일하게 변경
					dao.update("external.receiveorder.updateOutQtyOfOrderproduct", oopOne);
				}
			}

			//현재 데이터가 사은품코드를 포함하고 있으면 사은품의 주문상품데이터는 부모의 order_product_no 로 update
			if (null != ormUnit.getOrderEtc2() && !"".equals(ormUnit.getOrderEtc2())) {
				Map<String, String> updMap1 = new HashMap<String, String>();
				updMap1.put("orderId", boOrderId);
				updMap1.put("skuAlias", ormUnit.getSkuAlias());
				updMap1.put("orderEtc2", ormUnit.getOrderEtc2());
				dao.update("external.receiveorder.updateUpperOrderProductNo", updMap1);
			}
		}
		//주문금액, 결제금액 update: OmsOrder, OmsPayment
		Map<String, Object> updMap2 = new HashMap<String, Object>();
		updMap2.put("orderId", boOrderId);
		updMap2.put("orderAmt", totalOrderAmt);
		//=> OmsOrder
		dao.update("external.receiveorder.updateOrderAmtInOmsOrder", updMap2);
		//=> OmsPayment
		dao.update("external.receiveorder.updatePaymentAmtInOmsPayment", updMap2);
		
		//보세상품 존재시 주문상품 전체가 출고완료이면 주문도 출고완료 처리
		if (islocalDeliveryExist) {
//			List<String> orderStateList = (List<String>) dao.selectList("external.receiveorder.getOrderProductStateList",
//					boOrderId);
			//주문상품상태코드가 한 종류이고 출고완료이면 주문마스터 update
//			if (orderStateList.size() == 1) {
//				if ("ORDER_PRODUCT_STATE_CD.SHIP".equals(orderStateList.get(0))) {
//					dao.update("external.receiveorder.updateDeliveryStateOfOmsOrder", boOrderId);
//				}
//			}
			dao.update("external.receiveorder.updateDeliveryStateOfOmsOrder", boOrderId);
		}

		//배송table 입력: 주문번호당 배송지번호별, 배송정책번호별로 생성
//		List<OmsDelivery> deliveryList = (List<OmsDelivery>) dao.selectList("external.receiveorder.getOmsDeliveryList", orderId);
////			List<OmsDelivery> deliveryList = setDelivery(storeId, orderId);
//		for (OmsDelivery odOne : deliveryList) {
//			odOne.setStoreId(storeId);
//			odOne.setInsId(updId);
//			odOne.setUpdId(updId);
//			dao.insertOneTable(odOne);
//		}

		//Terminal 처리결과 update
		Map<String, Object> updMap3 = new HashMap<String, Object>();
		updMap3.put("orderId", boOrderId);
		for (OmsReceiveordermapping ormUnit : ormGroup) {
			updMap3.put("seq", ormUnit.getSeq());
			dao.update("external.receiveorder.updateReceiveOrderSuccess", updMap3);
		}
	}

	/**
	 * @Method Name : isDeliveryPolicyNotExist
	 * @author : peter
	 * @date : 2016. 10. 21.
	 * @description : 배송정책 기등록여부
	 *
	 * @param orderId
	 * @param addressNo
	 * @param policyNo
	 * @return
	 */
	private boolean isDeliveryPolicyNotExist(List<BigDecimal> policyList, BigDecimal policyNo) {
		boolean result = true;
		for (BigDecimal policy : policyList) {
			if (policy.compareTo(policyNo) == 0) {
				result = false;
				break;
			}
		}

		return result;
//		Map<String, Object> srchMap = new HashMap<String, Object>();
//		srchMap.put("orderId", orderId);
//		srchMap.put("deliveryAddressNo", addressNo);
//		srchMap.put("deliveryPolicyNo", policyNo);
//		int deliveryExistCnt = (int) dao.selectOne("external.receiveorder.getOmsDeliveryExistCount", srchMap);

//		if (deliveryExistCnt == 0) {
//			return true;
//		} else {
//			return false;
//		}
	}
}