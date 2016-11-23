package gcp.oms.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsSite;
import gcp.ccs.service.BaseService;
import gcp.external.model.OmsReceiveordermapping;
import gcp.external.service.TmallService;
import gcp.external.util.TerminalUtil;
import gcp.oms.model.search.OmsTermorderSearch;

@Service
public class TermOrderService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	//주문 공통 서비스 선언
//	@Autowired
//	private CommonOmsService			commonOmsService;

	//Tmall 주문수집
	@Autowired
	private TmallService		tmallService;

	//사이트 정보 저장
//	private static Map<String, String>	siteMap	= new HashMap<String, String>();

	/**
	 * @Method Name : getExternalSiteList
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 국내외부몰 사이트 조회
	 *
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CcsSite> getExternalSiteList(String storeId) throws Exception {
		return (List<CcsSite>) dao.selectList("oms.termorder.getExternalSiteList", storeId);
	}

	/**
	 * @Method Name : getUnHandleReceiveOrderList
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 사방넷 주문 미처리 내역 조회
	 *
	 * @param ots
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsReceiveordermapping> getUnHandleReceiveOrderList(OmsTermorderSearch ots) throws Exception {
		//siteId 가 Tmall(246), CNmall(252), JDmall(265,266) 인 경우
		if ("246".equals(ots.getSiteId()) || "252".equals(ots.getSiteId()) || "265".equals(ots.getSiteId())
				|| "266".equals(ots.getSiteId())) {
			ots.setSiteTypeCd("SITE_TYPE_CD.CHINA");
		} else {
			ots.setSiteTypeCd("SITE_TYPE_CD.DOMESTIC");
		}

		return (List<OmsReceiveordermapping>) dao.selectList("oms.termorder.getUnHandleReceiveOrderList", ots);
	}

	/**
	 * @Method Name : deleteOrderList
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 주문 삭제 처리
	 *
	 * @param ormList
	 * @param storeId
	 * @throws Exception
	 */
	public void deleteOrderList(List<OmsReceiveordermapping> ormList, String storeId) throws Exception {
		for (OmsReceiveordermapping ormOne : ormList) {
			ormOne.setStoreId(storeId);
			//삭제시 idx 앞에 "DEL_" 추가
			if (!ormOne.getIdx().startsWith("DEL_")) {
				ormOne.setIdx("DEL_" + ormOne.getIdx());
			}
			dao.update("oms.termorder.updateReceiveOrderDelete", ormOne);
		}
	}

	/**
	 * @Method Name : updateOrderList
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 주문 항목 수정
	 *
	 * @param ormList
	 * @param storeId
	 * @throws Exception
	 */
	public void updateOrderList(List<OmsReceiveordermapping> ormList, String storeId) throws Exception {
		for (OmsReceiveordermapping ormOne : ormList) {
			ormOne.setStoreId(storeId);
			dao.update("oms.termorder.updateReceiveOrderSave", ormOne);
		}
	}

	/**
	 * @Method Name : receiveSbnOrder
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 사방넷에서 터미널로 주문수집
	 *
	 * @param ots
	 * @return
	 * @throws Exception
	 */
	public String receiveSbnOrder(OmsTermorderSearch ots) throws Exception {
		//검색날짜
		logger.debug("startDate: " + ots.getStartDate() + ", endDate: " + ots.getEndDate());

		return TerminalUtil.sendPostSbn("order", ots.getStartDate().replaceAll("-", ""), ots.getEndDate().replaceAll("-", ""));
	}

	/**
	 * @Method Name : receiveTmallOrder
	 * @author : peter
	 * @date : 2016. 11. 17.
	 * @description : Tmall에서 터미널로 주문수집
	 *
	 * @param ots
	 * @return
	 * @throws Exception
	 */
	public int receiveTmallOrder(OmsTermorderSearch ots) throws Exception {
		//검색날짜
		logger.debug("startDate: " + ots.getStartDate() + ", endDate: " + ots.getEndDate() + ", storeId: " + ots.getStoreId());

		return tmallService.getTmallOrder(ots.getStoreId(), ots.getStartDate(), ots.getEndDate());
	}

	/**
	 * @Method Name : processReceiveOrder
	 * @author : peter
	 * @date : 2016. 11. 8.
	 * @description : 터미널에서 BO로 주문데이터 생성
	 *
	 * @param storeId
	 * @param loginId
	 * @return
	 * @throws Exception
	 */
	/*	@SuppressWarnings("unchecked")
		public String processReceiveOrder(String storeId, String loginId) throws Exception {
			//조회용 Map
			Map<String, String> srchMap = new HashMap<String, String>();
			srchMap.put("storeId", storeId);
			srchMap.put("insId", loginId);
			srchMap.put("mallType", null);
	
			//사전오류 체크 update
			((TermOrderService) getThis()).updatePreProcessReceiveOrderNewTx(srchMap);
	
			//최종 처리대상 조회
			List<OmsReceiveordermapping> ormList = (List<OmsReceiveordermapping>) dao
					.selectList("external.receiveorder.getReceiveOrderList", srchMap);
	
			//주문번호별 자료저장을 위한 변수
			Map<String, ArrayList<OmsReceiveordermapping>> ormGroupMap = new HashMap<String, ArrayList<OmsReceiveordermapping>>();
			Map<String, PmsProduct> productMap = new HashMap<String, PmsProduct>();
			//바로 전 정보 저장변수: 중복체크용
			String sbnOldOrderInfo = ""; //주문번호_siteId
			for (OmsReceiveordermapping ormOne : ormList) {
				//주문번호에서 "+" 제거
				ormOne.setOrderId(ormOne.getOrderId().replace("+", ""));
				//주문번호 정보
				String sbnNewOrderInfo = ormOne.getOrderId() + "_" + ormOne.getSiteId();
				logger.debug("SEQ: " + ormOne.getSeq() + ", Old Order: " + sbnOldOrderInfo + ", New Order: " + sbnNewOrderInfo);
	
				//새로운 주문정보인 경우
				if (!sbnNewOrderInfo.equals(sbnOldOrderInfo)) {
					//새로운 주문정보를 중복체크용으로 old 변수에 저장
					sbnOldOrderInfo = sbnNewOrderInfo;
	
					//주문번호별 Terminal 정보그룹 저장: 신규 Key(boOrderId) 생성
					ArrayList<OmsReceiveordermapping> ormGroupList = new ArrayList<OmsReceiveordermapping>();
					ormGroupList.add(ormOne);
					ormGroupMap.put(sbnNewOrderInfo, ormGroupList);
				}
				//동일한 주문정보인 경우
				else {
					//주문번호별 Terminal 정보그룹 저장: 기존 Key(boOrderId)에 추가
					ArrayList<OmsReceiveordermapping> ormElements = ormGroupMap.get(sbnOldOrderInfo);
					ormElements.add(ormOne);
					ormGroupMap.put(sbnOldOrderInfo, ormElements);
				}
	
				//상품,단품정보 저장
				PmsProduct product = commonOmsService.getProductInfo(storeId, ormOne.getSkuAlias());
				productMap.put(ormOne.getSeq(), product);
			}
	
			//주문번호별 BO 주문데이터 생성
			int successCnt = 0;
			int errorCnt = 0;
			for (String orderInfo : ormGroupMap.keySet()) {
				//현 주문번호에 속한 Order List
				List<OmsReceiveordermapping> ormGroup = ormGroupMap.get(orderInfo);
				try {
					((TermOrderService) getThis()).insertSbnOrderDataNewTx(orderInfo, ormGroup, productMap, storeId, loginId);
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
		}*/

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
		commonOmsService.preTreatmentReceiveOrder("SBN");

		//개별 오류 처리
		commonOmsService.checkEachReceiveOrder(srchMap);
	}*/

	/**
	 * @Method Name : insertSbnOrderDataNewTx
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
/*	public void insertSbnOrderDataNewTx(String orderInfo, List<OmsReceiveordermapping> ormGroup,
			Map<String, PmsProduct> productMap, String storeId, String loginId) throws Exception {
		logger.debug("insertSbnOrderDataNewTx: Order Info [" + orderInfo + "], Count [" + ormGroup.size() + "]");

		//주문마스터table 입력
		OmsOrder order = commonOmsService.setOmsOrder(ormGroup.get(0));
		order.setStoreId(storeId);
		order.setInsId(loginId);
		order.setUpdId(loginId);
//		dao.insertOne(order);
		dao.insert("common.insert.OmsOrder", order);
		//신규 주문번호
		String boOrderId = order.getOrderId();
		logger.debug("BO Order ID: " + boOrderId);

		//결제table 입력
		OmsPayment payment = commonOmsService.setOmsPayment(ormGroup.get(0));
		payment.setOrderId(boOrderId);
		payment.setInsId(loginId);
		payment.setUpdId(loginId);
//		dao.insertOne(payment);
		dao.insert("common.insert.OmsPayment", payment);

		String sbnOldReceiveName = ""; //이전 수취자명
		String sbnOldReceiveAddr = ""; //이전 수취자 주소
		BigDecimal boDeliveryAddressNo = new BigDecimal(0); //BO 배송지번호

		//주문번호별 주문총결제금액의 합계
		int totalOrderAmt = 0;
		//주문별,배송지번호별 배송정책번호 리스트
		List<BigDecimal> policyList = null;
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
				OmsDeliveryaddress address = commonOmsService.setDeliveryAddress(ormUnit, false);
				address.setOrderId(boOrderId);
				address.setInsId(loginId);
				address.setUpdId(loginId);
//				dao.insertOne(address);
				dao.insert("common.insert.OmsDeliveryaddress", address);
				//신규 배송지 번호
				boDeliveryAddressNo = address.getDeliveryAddressNo();
				//배송정책번호 리스트 재설정
				policyList = new ArrayList<BigDecimal>();
			}

			//상품,단품정보 조회
			PmsProduct product = productMap.get(ormUnit.getSeq());
			logger.debug("Product ID: " + product.getProductId() + ", Saleproduct ID: "
					+ product.getPmsSaleproduct().getSaleproductId());

			//일반,세트-사은품 상품정보 저장
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
//					dao.insertOne(delivery);
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
				oopOne.setInsId(loginId);
				oopOne.setUpdId(loginId);
				//배송지번호
				oopOne.setDeliveryAddressNo(boDeliveryAddressNo);
//				dao.insertOne(oopOne);
				dao.insert("common.insert.OmsOrderproduct", oopOne);
			}

			//사은품의 경우 상위주문상품일련번호를 부모의 order_product_no 로 update
			if (null != ormUnit.getOrderEtc2() && !"".equals(ormUnit.getOrderEtc2())) {
				Map<String, String> updMap1 = new HashMap<String, String>();
				updMap1.put("orderId", boOrderId);
				updMap1.put("skuAlias", ormUnit.getSkuAlias());
				updMap1.put("orderEtc2", ormUnit.getOrderEtc2());
				dao.update("external.receiveorder.updateUpperOrderProductNoOfPresent", updMap1);
			}

			//세트 구성상품의 경우 상위주문상품일련번호를 부모세트의 order_product_no 로 update
			if (isExistSub) {
				dao.update("external.receiveorder.updateUpperOrderProductNoOfSub", boOrderId);
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

		//Terminal 처리결과 update
		Map<String, Object> updMap3 = new HashMap<String, Object>();
		updMap3.put("orderId", boOrderId);
		for (OmsReceiveordermapping ormUnit : ormGroup) {
			updMap3.put("seq", ormUnit.getSeq());
			dao.update("external.receiveorder.updateReceiveOrderSuccess", updMap3);
		}
	}*/

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
/*	private boolean isDeliveryPolicyNotExist(List<BigDecimal> policyList, BigDecimal policyNo) {
		boolean result = true;
		for (BigDecimal policy : policyList) {
			if (policy.compareTo(policyNo) == 0) {
				result = false;
				break;
			}
		}

		return result;
	}*/
}
