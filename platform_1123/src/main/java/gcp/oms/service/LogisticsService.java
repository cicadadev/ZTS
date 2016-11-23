package gcp.oms.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;


import gcp.ccs.model.CcsSite;
import gcp.ccs.service.BaseService;
import gcp.external.model.IfLabel;
import gcp.external.model.TmsQueue;
import gcp.external.model.ZeroOrderMtr;
import gcp.external.service.TmsService;
import gcp.external.util.TerminalUtil;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsDeliverytracking;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.search.OmsLogisticsSearch;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsWarehouse;
import gcp.pms.model.PmsWarehouselocation;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.LocaleUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class LogisticsService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(LogisticsService.class);
	
	 @Autowired
	 private TmsService tmsService;
	
	/**
	 * 
	 * @Method Name : getOrderProductList
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송승인 화면 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> getOrderProductList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsOrderproduct>) dao.selectList("oms.logistics.orderProductList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : insertLogistics
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송 승인
	 *
	 * @param checkedList
	 * @param approvalType
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String insertLogistics(List<OmsOrderproduct> deliveryApprovalList, String approvalType) throws Exception {
		// 연동한 물류데이터 총 갯수
		int insCnt = 0;
		// DAS인지 한진인지 구분
		boolean isDasYn = "DAS".equalsIgnoreCase(approvalType);
		String storeId = SessionUtil.getStoreId();
		String userId = SessionUtil.getLoginId();
		
		// 데이터 조회를 위한 파라미터 맵 생성
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("storeId", storeId);
		paramMap.put("updId", userId);
		paramMap.put("list", deliveryApprovalList);
		
		Set<String> errorZipCd = new HashSet<>();
		Set<String> errorOrderIdSet = new HashSet<>();
		
		// 1.배송승인 전송오류 체크
		for(int i = 0; i < deliveryApprovalList.size(); i++) {
			OmsOrderproduct omsOrderproduct = deliveryApprovalList.get(i);
			
			String orderId = omsOrderproduct.getOrderId();
			String zipCd = CommonUtil.replaceNull(omsOrderproduct.getZipCd()).replace("-", "");
			boolean isWrap = "ORDER_PRODUCT_TYPE_CD.WRAP".equals(omsOrderproduct.getOrderProductTypeCd());

			// 우편번호 유효성 체크
			if(errorZipCd.contains(zipCd)){
				this.updateErrorReasonCd(omsOrderproduct, "SEND_ERROR_REASON_CD.ZIPCD");
				errorOrderIdSet.add(orderId);
				continue;
			}else{
				if(edi.selectCount("oms.logistics.checkZipCodeHanjin", zipCd) < 1){
					this.updateErrorReasonCd(omsOrderproduct, "SEND_ERROR_REASON_CD.ZIPCD");
					errorZipCd.add(zipCd);
					errorOrderIdSet.add(orderId);
					continue;
				}
			}
			// 선물포장비는 체크하지 않는다.
			if(!isWrap){
				// 로케이션ID 등록여부체크, 등록된 로케이션 사용여부 체크
				if(dao.selectCount("oms.logistics.checkLocationUseYn", omsOrderproduct) < 1){
					this.updateErrorReasonCd(omsOrderproduct, "SEND_ERROR_REASON_CD.LOCATION");
					errorOrderIdSet.add(orderId);
					continue;
				}
				// ERP_PRODUCT_ID, ERP_SALEPRODUCT_ID 미설정 체크
				if(CommonUtil.isEmpty(omsOrderproduct.getErpProductId()) ||  CommonUtil.isEmpty(omsOrderproduct.getErpSaleproductId())){
					this.updateErrorReasonCd(omsOrderproduct, "SEND_ERROR_REASON_CD.ERP");
					errorOrderIdSet.add(orderId);
					continue;
				}
			}
			// valid 상품은 이전 전송오류를 리셋해준다.
			if(BaseConstants.YN_Y.equals(omsOrderproduct.getSendErrorYn())){
				this.updateErrorReasonCd(omsOrderproduct, "");
			}
		}
		// 2.승인오류 상품의 동일주문 상품도 오류처리
		if(errorOrderIdSet.size() > 0){
			// 승인 오류 상품 주문번호 리스트
			paramMap.put("errorList", errorOrderIdSet);
			paramMap.put("sendErrorReasonCd", "SEND_ERROR_REASON_CD.PRODUCT");
			paramMap.put("sendErrorYn", "Y");
			
			dao.update("oms.logistics.updateSendErrorOrderReasonCd", paramMap);
		}
		
		// 배송승인지시일시
		String approvalDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8); 
		paramMap.put("deliveryIfTypeCd", isDasYn ? "DELIVERY_IF_TYPE_CD.DAS" : "DELIVERY_IF_TYPE_CD.HANJIN");
		paramMap.put("logisticsStateCd", isDasYn ? "LOGISTICS_STATE_CD.SEND" : "LOGISTICS_STATE_CD.APPROVAL");
		
		// 3.전송오류 없는 valid한 주문상품의 배송데이터 일괄 INSERT
		insCnt = dao.insert("oms.logistics.insertLogisticsList", paramMap);
		
		if(insCnt > 0){
			// 승인된 상품 리스트를 가져와서 상태변경을 해준다.
			List<OmsOrderproduct> approvalOrderProductList = (List<OmsOrderproduct>) dao.selectList("oms.logistics.getApprovalProduct", paramMap);
			
			for(OmsOrderproduct omsOrderproduct : approvalOrderProductList){
				
				// 주문상품 상태코드 업데이트
				omsOrderproduct.setStoreId(storeId);
				omsOrderproduct.setUpdId(userId);
				omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER");
				omsOrderproduct.setDeliveryOrderDt(approvalDt);
				omsOrderproduct.setDeliveryCancelReasonCd("");
				
				if(dao.updateOneTable(omsOrderproduct) > 0){
					// 세트SUB상품일 경우 부모상태 업데이트, 주문테이블의 배송상태 업데이트
					this.updateOrderStateCd(omsOrderproduct, "ORDER_DELIVERY_STATE_CD.DELIVERY_ORDER", "ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER", null);
				}
			}
			
			// DAS 배송 승인일 경우 DAS테이블에 데이터 생성
			if(isDasYn){
				ZeroOrderMtr zeroOrderMtr = this.getZeroOrderMtr(approvalOrderProductList ,isDasYn);
				
				// DAS_BTOC.ZERO_ORD_MTR에 데이터를 넣어준다.
				dao.insert("oms.logistics.insertZeroOrderMtr", zeroOrderMtr);
				
				// 테스트용 DAS 운송장 데이터 INSERT    TODO : 테스트 이후 삭제한다.
				if(CommonUtil.isNotEmpty(approvalOrderProductList)){
					try {
						for(OmsOrderproduct omsOrderproduct : approvalOrderProductList){
							dao.insert("oms.logistics.insertIfLabelList", omsOrderproduct);
						}
					} catch (Exception e) {
						logger.error("========= 테스트 운송장 생성 에러 : " + e.getMessage() + "=========");
					}
				}
			}
		}
			
		
		StringBuilder result = new StringBuilder();
		
		result.append("총 승인갯수 : " + String.valueOf(deliveryApprovalList.size()) + "건\n")
			  .append("총 연동갯수 : " + String.valueOf(insCnt) + "건\n");
		
		return result.toString();
	}
	
	/**
	 * 
	 * @Method Name : updateOrderStateCd
	 * @author : brad
	 * @date : 2016. 9. 8.
	 * @description : 주문상태코드 변경 공통 메소드
	 *
	 * @param omsOrderproduct
	 * @param orderDeliveryStateCd
	 * @param orderProductStateCd
	 * @param claimStateCd
	 * @throws Exception
	 */
	private void updateOrderStateCd(OmsOrderproduct omsOrderproduct, String orderDeliveryStateCd, String orderProductStateCd, String claimStateCd) throws Exception{
		// UPD_ID
		BigDecimal claimNo = omsOrderproduct.getClaimNo();
		BigDecimal upperOrderProductNo = omsOrderproduct.getUpperOrderProductNo();
		String orderProductTypeCd = omsOrderproduct.getOrderProductTypeCd();
		String orderDeliveryTypeCd = omsOrderproduct.getOrderDeliveryTypeCd();
		String deliveryOrderDt = CommonUtil.replaceNull(omsOrderproduct.getDeliveryOrderDt());
		String shipDt = CommonUtil.replaceNull(omsOrderproduct.getShipDt());
		String deliveryDt = CommonUtil.replaceNull(omsOrderproduct.getDeliveryDt());
		
		Map<String, Object> params = new HashMap<>();
		params.put("updId", omsOrderproduct.getUpdId());
		params.put("storeId", omsOrderproduct.getStoreId());
		params.put("orderId", omsOrderproduct.getOrderId());
		
		// 재배송, 교환상품은 주문테이블과 세트상품의 상태변경을 하지 않는다.
		if("ORDER_DELIVERY_TYPE_CD.ORDER".equals(orderDeliveryTypeCd)){
			// 세트의 자식상품은 부모의 상태를 업데이트 해준다.
			if("ORDER_PRODUCT_TYPE_CD.SUB".equals(orderProductTypeCd) && CommonUtil.isNotEmpty(orderProductStateCd) && CommonUtil.isNotEmpty(upperOrderProductNo)){
				// 주문상품 테이블의 ORDER_PRODUCT_TYPE_CD.SET의 상태 코드 변경
				params.put("orderProductNo", upperOrderProductNo);
				params.put("orderProductStateCd", orderProductStateCd);
				params.put("deliveryOrderDt", deliveryOrderDt);
				params.put("shipDt", shipDt);
				params.put("deliveryDt", deliveryDt);
				
				dao.update("oms.logistics.updateOrderProductState", params);
			}
			
			// 주문 테이블의 배송상태 코드 변경
			if(CommonUtil.isNotEmpty(orderDeliveryStateCd)){
				params.put("orderDeliveryStateCd", orderDeliveryStateCd);
				dao.update("oms.logistics.updateOrderDeliveryState", params);
			}
			
		}else{
			// 교환상품은 해당클레임 상품이 모두 입출고 완료시 클레임 테이블의 클레임 상태 변경
			if(CommonUtil.isNotEmpty(claimNo) && CommonUtil.isNotEmpty(claimStateCd)){
				
				params.put("claimNo", omsOrderproduct.getClaimNo());
				params.put("claimStateCd", claimStateCd);
				params.put("orderDeliveryTypeCd", orderDeliveryTypeCd);
				
				dao.update("oms.logistics.updateClaimState", params);
			}
		}
	}
	
	/**
	 * 
	 * @Method Name : updateErrorReasonCd
	 * @author : brad
	 * @date : 2016. 10. 20.
	 * @description : 배송 승인 전송 에러상품 상태 업데이트
	 *
	 * @param approvalList
	 * @param isDasYn
	 * @throws Exception
	 */
	private void updateErrorReasonCd(OmsOrderproduct p, String sendErrorReasonCd) throws Exception{
		
		if(CommonUtil.isEmpty(sendErrorReasonCd)){
			p.setSendErrorYn("N");
		}else{
			p.setSendErrorYn("Y");
		}
		p.setSendErrorReasonCd(sendErrorReasonCd);
		p.setUpdId(SessionUtil.getLoginId());
		
		dao.update("oms.logistics.updateSendErrorReasonCd", p);
	}
	
	/**
	 * 
	 * @Method Name : getZeroOrderMtr
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송 승인 후 DAS_BTOC.ZERO_ORD_MTR테이블 데이터 생성 / 한진DB ZERO_ORD_MTR테이블 데이터 생성을 위한 파마미터 VO
	 *
	 * @param approvalList
	 * @param isDasYn
	 * @throws Exception
	 */
	private ZeroOrderMtr getZeroOrderMtr(List<OmsOrderproduct> approvalList, boolean isDasYn) throws Exception {
		
		ZeroOrderMtr zeroOrderMtr = new ZeroOrderMtr();
		
		zeroOrderMtr.setStoreId(SessionUtil.getStoreId());
		zeroOrderMtr.setList(approvalList);
		
		if(isDasYn){
			zeroOrderMtr.setStaffYn(BaseConstants.YN_N);                                          // 조건 추가해야함(고정 배송지 여부), 직원몰에 대한 구분 값(현재 DAS에서는 수신만 받고, 사용 하지 않는다.)
//			zeroOrderMtr.setComposeNum(1);                                                         // 입 수량(제품 단위 수량) - 현재 DAS에서는 수량에 대한 값을 수신 하기만 하고, 사용 하지는 않는다.                      
//			zeroOrderMtr.setOrdLogisticsNos("1");                                                  // das에서는 수신 받지 않는 데이터(ERP에서 사용 하기 위해서 생성한 데이터로 생각됨)            
		}
		
		zeroOrderMtr.setStx(BaseConstants.OMS_LOGISTICS_STX);
		zeroOrderMtr.setPickPostno(BaseConstants.OMS_LOGISTICS_PICK_POST_NO);
		zeroOrderMtr.setSellPostno(BaseConstants.OMS_LOGISTICS_SELL_POST_NO);
		zeroOrderMtr.setSellAddress1(BaseConstants.OMS_LOGISTICS_SELL_ADDRESS1);
		zeroOrderMtr.setSellAddress2(BaseConstants.OMS_LOGISTICS_SELL_ADDRESS2);
		zeroOrderMtr.setBxCode(BaseConstants.OMS_LOGISTICS_BXCODE2);
		zeroOrderMtr.setSellNm(BaseConstants.OMS_LOGISTICS_SELL_NM);
		zeroOrderMtr.setSellPhoneno1(BaseConstants.OMS_LOGISTICS_SELL_PHONENO1);
		zeroOrderMtr.setPayAmount(BaseConstants.OMS_LOGISTICS_PAYAMOUNT);   
		
		return zeroOrderMtr; 
	}
	
	/**
	 * 
	 * @Method Name : insertHanjinZeroOrderMtr
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 한진 배송 승인 후 한진DB DAS_BTOC.ZERO_ORD_MTR테이블 데이터 생성
	 *
	 * @param omsLogistics
	 * @param itemSeq
	 * @throws Exception
	 */
	private int insertHanjinZeroOrderMtr(OmsLogistics omsLogistics, Integer itemSeq) throws Exception {
		// 배송번호에 해당하는 주문상품
		OmsOrderproduct omsOrderproduct = omsLogistics.getOmsOrderproduct();
		// 기본값 세팅
		String zipCd = CommonUtil.replaceNull(omsOrderproduct.getOmsDeliveryaddress().getZipCd()).replace("-", "");                                            // 우편번호
		String receiverAddress1 = omsOrderproduct.getOmsDeliveryaddress().getAddress1();                                                                       // 주소1
		String receiverAddress2 = omsOrderproduct.getOmsDeliveryaddress().getAddress2();                                                                       // 주소2
		String receiverNm = omsOrderproduct.getOmsDeliveryaddress().getName1();                                                                                // 수령인명
		String receiverPhone1 = omsOrderproduct.getOmsDeliveryaddress().getPhone1();                                                                           // 수령인 전화번호
		String receiverPhone2 = omsOrderproduct.getOmsDeliveryaddress().getPhone2();                                                                           // 수령인 핸드폰번호
		String note = omsOrderproduct.getOmsDeliveryaddress().getNote();                                                                                       // 배송메모
		BigDecimal deliveryNo = omsLogistics.getLogisticsInoutNo();                                                                                            // 배송번호
		String custOrdSeq = omsOrderproduct.getCustOrdSeq();                                                                                                   // 배송번호별 상품일련번호
		String productId = omsOrderproduct.getProductId();                                                                                                     // 상품번호
		String productName = omsOrderproduct.getProductName();                                                                                                 // 상품명
		String saleProductName = omsOrderproduct.getSaleproductName();                                                                                         // 단품명
		String boxQty = omsLogistics.getOutReserveQty();                                                                                                       // 출고수량
		String wrapTogetherYn = omsOrderproduct.getOmsDelivery().getWrapTogetherYn();                                                                          // 합포장여부
		String wrapYn = omsOrderproduct.getWrapYn();                                                                                                           // 선물포장여부
		BigDecimal wrapSize = (BigDecimal) CommonUtil.replaceNull(omsOrderproduct.getWrapSize(), 0);                                                           // 포장부피 * 출고예정수량
		
		String itemName = "";
		StringBuffer giftStr = new StringBuffer("");
		if( "Y".equals(wrapYn)){
			if("Y".equals(wrapTogetherYn)){
				giftStr.append("[선물합포]");
			} else {
				giftStr.append("[선물개별:" + Math.ceil(wrapSize.doubleValue()) + "]");						
			}
		}
		
		if (CommonUtil.isEmpty(saleProductName) || "없음".equals(saleProductName)){
			itemName = giftStr.toString() + productName;					
		}else{
			itemName = giftStr.toString() + productName + "[" + saleProductName +  "]";	
		}	
		
		ZeroOrderMtr zeroOrderMtr = new ZeroOrderMtr();
		
		zeroOrderMtr.setStx(BaseConstants.OMS_LOGISTICS_STX);
		zeroOrderMtr.setPickPostno(BaseConstants.OMS_LOGISTICS_PICK_POST_NO);
		zeroOrderMtr.setSellPostno(BaseConstants.OMS_LOGISTICS_SELL_POST_NO);
		zeroOrderMtr.setSellAddress1(BaseConstants.OMS_LOGISTICS_SELL_ADDRESS1);
		zeroOrderMtr.setSellAddress2(BaseConstants.OMS_LOGISTICS_SELL_ADDRESS2);
		zeroOrderMtr.setBxCode(BaseConstants.OMS_LOGISTICS_BXCODE2);
		zeroOrderMtr.setSellNm(BaseConstants.OMS_LOGISTICS_SELL_NM);
		zeroOrderMtr.setSellPhoneno1(BaseConstants.OMS_LOGISTICS_SELL_PHONENO1);
		zeroOrderMtr.setCustPostnpo(zipCd);
		zeroOrderMtr.setCustAddress1(receiverAddress1);
		zeroOrderMtr.setCustAddress2(receiverAddress2);
		zeroOrderMtr.setCustNm(receiverNm);
		zeroOrderMtr.setCustPhoneno1(CommonUtil.isEmpty(receiverPhone1) ? receiverPhone2 : receiverPhone1);
		zeroOrderMtr.setCustPhoneno2(receiverPhone2);
		zeroOrderMtr.setMessage(note);
		zeroOrderMtr.setOrderNo(String.valueOf(deliveryNo));
		zeroOrderMtr.setOrderSeq(custOrdSeq);
		zeroOrderMtr.setItemcd(productId);        
		zeroOrderMtr.setItemNm(itemName);
		zeroOrderMtr.setBoxQty(boxQty);                                        
//		zeroOrderMtr.setPayCond("");                                                           
		zeroOrderMtr.setPayAmount(BaseConstants.OMS_LOGISTICS_PAYAMOUNT);   
		zeroOrderMtr.setItemseq(String.valueOf(itemSeq));                                      
		
		return edi.insert("oms.logistics.insertHanjinZeroOrderMtr", zeroOrderMtr);
	}
	
	/**
	 * 
	 * @Method Name : getCancelApprovalList
	 * @author : brad
	 * @date : 2016. 7. 19.
	 * @description : 배송 승인 취소 화면 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getCancelApprovalList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.cancelApprovalList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : selectCancelApprovalList
	 * @author : brad
	 * @date : 2016. 9. 28.
	 * @description : 배송 승인 취소 엑셀 일괄 업로드 데이터 조회
	 *
	 * @param bulk
	 * @throws Exception
	 */
	public OmsLogistics selectCancelApprovalList(Object bulk) throws Exception {
		return (OmsLogistics) dao.selectOne("oms.logistics.bulkCancelApprovalList", bulk);
	}
	
	/**
	 * 
	 * @Method Name : updateCancelDeliveryApproval
	 * @author : brad
	 * @date : 2016. 8. 3.
	 * @description : 배송 승인 취소
	 *
	 * @param cancelApprovalList
	 * @throws Exception
	 */
	public String updateCancelDeliveryApproval(List<OmsLogistics> cancelApprovalList) throws Exception {
		// 업데이트 카운트
		int updCnt = 0;
		// 승인취소 일시
		String cancelDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);
		String updId = SessionUtil.getLoginId();
		String storeId = SessionUtil.getStoreId();
		
		for(OmsLogistics omsLogistics : cancelApprovalList){
			
			String orderId = omsLogistics.getOrderId();
			BigDecimal orderProductNo = omsLogistics.getOrderProductNo();
			
			omsLogistics.setStoreId(storeId);
			omsLogistics.setUpdId(updId);
			omsLogistics.setUpdDt(cancelDt);
			omsLogistics.setCompleteDt(cancelDt);
			omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.CANCELAPPROVAL");
			
			if(dao.update("oms.logistics.updateCancelApproval", omsLogistics) > 0){
				updCnt++;
				
				OmsOrderproduct omsOrderproduct = new OmsOrderproduct();
				
				omsOrderproduct.setOrderId(orderId);
				omsOrderproduct.setOrderProductNo(orderProductNo);
				omsOrderproduct.setStoreId(storeId);
				omsOrderproduct.setUpdId(updId);
				omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL");
				omsOrderproduct.setDeliveryCancelReasonCd(omsLogistics.getDeliveryCancelReasonCd());
				
				dao.updateOneTable(omsOrderproduct);
			}
		}
		
		return "총 주문상품 " + String.valueOf(cancelApprovalList.size()) +  "건중 " + String.valueOf(updCnt) + "건을 성공적으로 처리했습니다.";
	}
	
	/**
	 * 
	 * @Method Name : getInvoiceList
	 * @author : brad
	 * @date : 2016. 7. 27.
	 * @description : 운송장 생성 대상 리스트 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getInvoiceList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.getInvoiceList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : updateInvoiceNo
	 * @author : brad
	 * @date : 2016. 7. 28.
	 * @description : DAS / HANJIN 운송장 생성
	 *
	 * @param type
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> updateInvoiceNo(String type) throws Exception {
		// 인터페이스 타입
		boolean isDasYn = "DAS".equalsIgnoreCase(type);
		// 사방넷 주문 운송장 생성여부
		boolean isExistSbnOrder = false;
		// 운송장 생성일
		String invoiceDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);
		// 업데이트 상품 총 갯수
		int updCnt = 0;
		// 금일 배송차수
		int deliveryOrder = (int) dao.selectOne("oms.logistics.getMaxDeliveryOrder", null);
		// BXCODE
		String bxCode = BaseConstants.OMS_LOGISTICS_BXCODE2;
		
		List<IfLabel> invoiceList = null;
		if(isDasYn){
			invoiceList = (List<IfLabel>) dao.selectList("oms.logistics.getInvoiceLabelList", null);
		}else{
			invoiceList = (List<IfLabel>) edi.selectList("oms.logistics.getHanjinInvoiceList", bxCode);
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		if(CommonUtil.isNotEmpty(invoiceList)){
			// 업데이트한 배송번호 리스트
			List<String> updateList = new ArrayList<>();
			
			for (IfLabel invoice : invoiceList) {
				// 생성된 운송장 번호 업데이트
				OmsLogistics omsLogistics = new OmsLogistics();
				omsLogistics.setUpdId(SessionUtil.getLoginId());
				omsLogistics.setUpdDt(invoiceDt);
				omsLogistics.setInvoiceDt(invoiceDt);
				omsLogistics.setLogisticsInoutNo(new BigDecimal(invoice.getOrdNo()));
				omsLogistics.setInvoiceNo(invoice.getInvNo());
				omsLogistics.setDeliveryOrder(new BigDecimal(deliveryOrder));
				omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.INVOICE");
				omsLogistics.setDeliveryIfTypeCd(isDasYn ? "DELIVERY_IF_TYPE_CD.DAS" : "DELIVERY_IF_TYPE_CD.HANJIN");
				
				int count = dao.update("oms.logistics.updateInvoiceNo", omsLogistics);
				if(count > 0){
					updCnt = updCnt + count;
					updateList.add(invoice.getOrdNo());
					//사방넷 주문상품만 ZTS_TERM.SEND_DELIVERY에 운송장 정보를 insert해준다.
					if(dao.insert("oms.logistics.insertSendDelivery", invoice) > 0 && !isExistSbnOrder){
						isExistSbnOrder = true;
					}
				}
			}
			// DAS_BTOC.IF_LABEL_LIST STATE 업데이트
			if(CommonUtil.isNotEmpty(updateList)){
				if(isDasYn){
					dao.update("oms.logistics.updateLabelList", updateList);
				}else{
					Map<String, Object> params = new HashMap<>();
					params.put("bxCode", bxCode);
					params.put("list", updateList);
					
					edi.update("oms.logistics.updateHanjinInvoiceList", params);
				}
			}
			
			resultMap.put("resultMsg", String.valueOf(updateList.size()) +  "건의 배송번호와 " + String.valueOf(updCnt) + "건의 상품을 성공적으로 처리했습니다.");
		}else{
			resultMap.put("resultMsg", "다운로드할 운송장 내역이 존재하지 않습니다.");
		}
		resultMap.put("existSbnOrder", isExistSbnOrder == true ? "Y" : "N");
		
		return resultMap;
	}
	
	/**
	 * 
	 * @Method Name : sendInvoiceInfoToSbn
	 * @author : brad
	 * @date : 2016. 10. 13.
	 * @description : 사방넷에 운송장 생성 여부 전송
	 *
	 */
	public void sendInvoiceInfoToSbn() {
		try {
			String rtnValue = TerminalUtil.sendPostSbn("delivery");
			
			if (!"0".equals(rtnValue)) {
				logger.error("사방넷 운송장 생성 여부 호출 에러!!! ");
			}else{
				logger.info("사방넷 운송장 생성 여부 호출 성공!!! ");
			}
		} catch (Exception e) {
			logger.error("사방넷 운송장 생성 여부 호출 에러 : " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Method Name : insertHanjinData
	 * @author : brad
	 * @date : 2016. 8. 3.
	 * @description : 한진 데이터 전송
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String insertHanjinData(List<OmsLogistics> omsLogisticsList) throws Exception {
		String result = "";
		// 파마미터 맵 생성
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("storeId", SessionUtil.getStoreId());
		paramMap.put("list", omsLogisticsList);
		
		List<OmsLogistics> sendHanjinDataList = (List<OmsLogistics>) dao.selectList("oms.logistics.getSendHanjinList", paramMap);
		
		if(CommonUtil.isNotEmpty(sendHanjinDataList)){
			// 동일 배송번호별 아이템 시퀀스를 구하기 위해 맵에 담아둔다.
			Map<BigDecimal, Integer> itemSeqHistory = new HashMap<>();
			List<OmsLogistics> sendHistoryList = new ArrayList<>();
			
			for(OmsLogistics omsLogistics : sendHanjinDataList){
				// Exceoption Args 생성
				String[] msgArgs = {String.valueOf(omsLogistics.getLogisticsInoutNo()), omsLogistics.getOmsOrderproduct().getOmsDeliveryaddress().getZipCd()};

				try {
					// 배송번호별 아이템 시퀀스를 구한다.
					BigDecimal deliNo = omsLogistics.getLogisticsInoutNo();
					if(itemSeqHistory.containsKey(deliNo)){
						itemSeqHistory.put(deliNo, itemSeqHistory.get(deliNo) + 1);
					}else{
						itemSeqHistory.put(deliNo, 1);
					}
					// 한진 테이블에 데이터를 넣어준다.
					if(this.insertHanjinZeroOrderMtr(omsLogistics, itemSeqHistory.get(deliNo)) > 0){
						// OMS_LOGISTICS 테이블의 LOGISTICS_STATE를 업데이트 하기 위해 리스트에 담아둔다.
						sendHistoryList.add(omsLogistics);
					}
				} catch (Exception e) {
					throw new ServiceException("oms.logistics.invoice.error", msgArgs);
				}
			}
			
			if(CommonUtil.isNotEmpty(sendHistoryList)){
				Map<String, Object> params = new HashMap<>();
				params.put("logisticsStateCd", "LOGISTICS_STATE_CD.SEND");
				params.put("updId", SessionUtil.getLoginId());
				params.put("list", sendHistoryList);
				
				dao.update("oms.logistics.updateLogisticsState", params);
			}
			
			result = "성공적으로 처리했습니다.";
		}else{
			result = "전송할 데이터 내역이 존재하지 않습니다.";
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name : getPickingList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 피킹리스트 화면 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> getPickingList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsOrderproduct>) dao.selectList("oms.logistics.getPickingList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : getAllSiteList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 사이트 목록 조회
	 *
	 * @param storeId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CcsSite> getAllSiteList(String storeId) throws Exception {
		return (List<CcsSite>) dao.selectList("oms.logistics.getSiteList", storeId);
	}
	
	/**
	 * 
	 * @Method Name : getAllWarehouseList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 창고 목록 조회
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsWarehouse> getAllWarehouseList() throws Exception {
		return (List<PmsWarehouse>) dao.selectList("oms.logistics.getWarehouseList", null);
	}
	
	/**
	 * 
	 * @Method Name : getAllLocationList
	 * @author : brad
	 * @date : 2016. 8. 8.
	 * @description : 모든 창고 로케이션 목록 조회
	 *
	 * @param warehouseId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsWarehouselocation> getAllLocationList(String warehouseId) throws Exception {
		return (List<PmsWarehouselocation>) dao.selectList("oms.logistics.getAllLocationList", warehouseId);
	}
	
	/**
	 * 
	 * @Method Name : getLocationList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 창고 로케이션 설정 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsWarehouselocation> getLocationList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<PmsWarehouselocation>) dao.selectList("oms.logistics.getLocationList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : updateLocationList
	 * @author : brad
	 * @date : 2016. 8. 23.
	 * @description : 창고 로케이션 업데이트
	 *
	 * @param locationList
	 * @throws Exception
	 */
	public void updateLocationList(List<PmsWarehouselocation> locationList) throws Exception {
		
		for (PmsWarehouselocation pmsWarehouselocation : locationList) {
			// Exceoption Args 생성
			String[] msgArgs = {pmsWarehouselocation.getLocationId()};
			
			if (StringUtils.isEmpty(pmsWarehouselocation.getInsDt())) {
				pmsWarehouselocation.setWarehouseId(BaseConstants.WAREHOUSE_ID);
				
				int cnt = (int) dao.selectCount("oms.logistics.checkLocationId", pmsWarehouselocation);
				if (cnt > 0) {
					throw new ServiceException("oms.logistics.location.save.error", msgArgs);
				}

				dao.insertOneTable(pmsWarehouselocation);
			} else {
				dao.updateOneTable(pmsWarehouselocation);
			}
		}
	}
	
	/**
	 * 
	 * @Method Name : getLocationMappingList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 창고 로케이션 매핑 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsSaleproduct> getLocationMappingList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		return (List<PmsSaleproduct>) dao.selectList("oms.logistics.getLocationMappingList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : getMappingSaleProductList
	 * @author : brad
	 * @date : 2016. 8. 23.
	 * @description : 창고 로케이션 매핑 수정 단품 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsSaleproduct> getMappingSaleProductList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		return (List<PmsSaleproduct>) dao.selectList("oms.logistics.getMappingSaleproductList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : updateMappingLocationId
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description : 창고 로케이션 매핑 개별 수정
	 *
	 * @param pmsSaleproductList
	 * @throws Exception
	 */
	public void updateMappingLocationId(List<PmsSaleproduct> pmsSaleproductList) throws Exception {
		for(PmsSaleproduct pmsSaleproduct : pmsSaleproductList){
			pmsSaleproduct.setWarehouseId(BaseConstants.WAREHOUSE_ID);
			dao.updateOneTable(pmsSaleproduct);
		}
	}
	
	public void insertExcelNewTx(Object model, String methodName) throws Exception {
		try {
			Method method = this.getClass().getMethod(methodName, model.getClass());
			ReflectionUtils.invokeMethod(method, this, model);
		} catch (NoSuchMethodException e) {
			String[] args = { methodName };
			String errMsg = MessageUtil.getMessage("ccs.common.error.nosuchmethod", args, LocaleUtil.getCurrentLocale());
			throw new NoSuchMethodException(errMsg);
		} catch (SecurityException e) {
			throw new SecurityException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Method Name : updateLocationWithSaleproductId
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description : 창고 로케이션 매핑 엑셀 일괄 수정
	 *
	 * @param pmsSaleproduct
	 * @throws Exception
	 */
	public void updateLocationWithSaleproductId(PmsSaleproduct pmsSaleproduct) throws Exception {
		dao.updateOneTable(pmsSaleproduct);
	}
	
	/**
	 * 
	 * @Method Name : updateLocationId
	 * @author : brad
	 * @date : 2016. 8. 24.
	 * @description : 창고 로케이션 매핑 엑셀 일괄 수정
	 *
	 * @param pmsSaleproduct
	 * @throws Exception
	 */
	public void updateLocationWithProductId(PmsSaleproduct pmsSaleproduct) throws Exception {
		dao.update("oms.logistics.updateLocationWithProductId", pmsSaleproduct);
	}
	
	/**
	 * 
	 * @Method Name : getShippingProcessList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 출고완료처리 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getShippingProcessList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.getShippingProcessList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : updateShippingConfirm
	 * @author : brad
	 * @date : 2016. 9. 5.
	 * @description : 출고완료 처리(센터/협력사)
	 *
	 * @param omsLogisticsList
	 * @throws Exception
	 */
	public String updateShippingConfirm(List<OmsLogistics> omsLogisticsList) throws Exception {
		// 출고확정 일시
		String shipDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);
		String updId = SessionUtil.getLoginId();
		String storeId = SessionUtil.getStoreId();
		int updCnt = 0;
		
		Set<String> shipProduct = new HashSet<>();
		Map<String, List<OmsOrderproduct>> sendTmsMap = new HashMap<>();
		
		for(OmsLogistics omsLogistics : omsLogisticsList) {
			
			BigDecimal outReserveQty = new BigDecimal(omsLogistics.getOutReserveQty());
			BigDecimal outQty = omsLogistics.getOutQty();
			
			BigDecimal cancelDeliveryQty = outReserveQty.subtract(outQty);
			String saleTypeCd = omsLogistics.getSaleTypeCd();
			
			boolean isShipped = outQty.compareTo(new BigDecimal(0)) > 0;
			
			omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.CONFIRM");
			omsLogistics.setCompleteDt(shipDt);
			omsLogistics.setUpdId(updId);
			omsLogistics.setUpdDt(shipDt);
			omsLogistics.setStoreId(storeId);
			
			// 협력업체의 경우는 운송장생성일과 출고확정일이 동일
			if("SALE_TYPE_CD.CONSIGN".equals(saleTypeCd)){
				omsLogistics.setInvoiceDt(shipDt);
			}
			// 부분출고일 경우에는 미출고수량 세팅
			if(cancelDeliveryQty.compareTo(new BigDecimal(0)) > 0){
				omsLogistics.setCancelDeliveryQty(String.valueOf(cancelDeliveryQty));
			}
			// 출고수량이 0일 경우는 생성된 운송장 번호 삭제
			if(!isShipped){
				omsLogistics.setInvoiceNo("");
			}
			// LOGISTICS 데이터 업데이트(출고완료)
			if(dao.update("oms.logistics.updateShippingConfirm", omsLogistics) > 0){
				updCnt++;
				// 주문상품
				OmsOrderproduct omsOrderproduct = (OmsOrderproduct) dao.selectOne("oms.logistics.selectOrderproduct", omsLogistics);
				omsOrderproduct.setUpdId(updId);
				omsOrderproduct.setUpdDt(shipDt);
				// 분할상품의 마지막 물류데이터인지 판단
				boolean isLast = dao.selectCount("oms.logistics.selectInvoiceLogistics", omsOrderproduct) == 0;
				// 상품 분할 배송때문에 운송장 생성된 물류 데이터가 없을때만 상품 상태를 업데이트 한다.
				if(isLast){
					String orderProductStateCd = omsOrderproduct.getOrderProductStateCd();
					// 출고나 부분출고일때만 출고일 업데이트
					if("ORDER_PRODUCT_STATE_CD.SHIP".equals(orderProductStateCd) || "ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY".equals(orderProductStateCd)){
						omsOrderproduct.setShipDt(shipDt);
					}
					// 주문상품 출고상태 업데이트
					if(dao.updateOneTable(omsOrderproduct) > 0){
						this.updateOrderStateCd(omsOrderproduct, "ORDER_DELIVERY_STATE_CD.SHIP", "ORDER_PRODUCT_STATE_CD.SHIP", "CLAIM_STATE_CD.COMPLETE");
					}
				}else{
					dao.update("oms.logistics.updateOutQty", omsOrderproduct);
				}
				// 출고수량이 0보다 크면 TMS전송
				String key = omsLogistics.getOrderId() + "," + String.valueOf(omsLogistics.getOrderProductNo());
				if(isShipped && !shipProduct.contains(key)){
					String orderId = omsOrderproduct.getOrderId();
					List<OmsOrderproduct> tmsOrderProductList = null;
					
					if(sendTmsMap.containsKey(orderId)){
						tmsOrderProductList = sendTmsMap.get(orderId);
						tmsOrderProductList.add(omsOrderproduct);
					}else{
						tmsOrderProductList = new ArrayList<>();
						tmsOrderProductList.add(omsOrderproduct);
						
						sendTmsMap.put(orderId, tmsOrderProductList);
					}
					shipProduct.add(key);
				}
			}
		}
		
		if(sendTmsMap.size() > 0){
			this.sendShippingTms(sendTmsMap, shipDt);
		}
		
		return String.valueOf(updCnt) + "건을 성공적으로 처리했습니다."; 
	}
	
	/**
	 * 
	 * @Method Name : sendShippingTms
	 * @author : brad
	 * @date : 2016. 10. 31.
	 * @description : 출고시 TMS 메세지 전송
	 *
	 * @param sendTmsMap
	 * @param shipDt
	 */
	private void sendShippingTms(Map<String, List<OmsOrderproduct>> sendTmsMap, String shipDt) {
		
		TmsQueue shipTms = new TmsQueue();
		shipTms.setMsgCode("120");
		
		for( String key : sendTmsMap.keySet() ){
			List<OmsOrderproduct> list = sendTmsMap.get(key);
			
			int size = list.size();
			String productName = list.get(0).getProductName();
//			String ordererPhone = list.get(0).getPhone2();
			String ordererPhone = "01028563767"; // TODO : LIVE일때는 바꿀것
			
			String map1 = null;
			if(size == 1){
				map1 = productName;
			}else{
				map1 = productName + " 외 " + String.valueOf(size - 1) + "건";
			}
			String map3 = key;
			String map4 = shipDt;
			
			shipTms.setMap1(map1);
			shipTms.setMap2("");
			shipTms.setMap3(map3);
			shipTms.setMap4(map4);
			shipTms.setToPhone(ordererPhone);
			
			logger.debug("shipTms : " + shipTms.toString());
			
			tmsService.sendTmsSmsQueue(shipTms);
        }
	}
	
	/**
	 * 
	 * @Method Name : getShippingList
	 * @author : brad
	 * @date : 2016. 8. 9.
	 * @description : 출고/미출고 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getShippingList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.getShippingList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : getPartnerDeliveryList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 배송의뢰서_협력사 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> getPartnerDeliveryList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsOrderproduct>) dao.selectList("oms.logistics.getPartnerDelivery", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : insertPartnerLogistics
	 * @author : brad
	 * @date : 2016. 9. 10.
	 * @description : 협력사 배송 승인
	 *
	 * @param deliveryApprovalList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String insertPartnerLogistics(List<OmsOrderproduct> deliveryApprovalList) throws Exception {
		String updId = SessionUtil.getLoginId();
		String storeId = SessionUtil.getStoreId();
		// 배송준비지시일시
		String approvalDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);
		// 업데이트 상품 총 갯수
		int updCnt = 0;
		// 파라미터맵 생성
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("storeId", storeId);
		paramMap.put("updId", updId);
		paramMap.put("list", deliveryApprovalList);
		
		if(dao.insert("oms.logistics.insertPartnerLogisticsList", paramMap) > 0){
			// 승인된 상품 리스트를 가져와서 상태변경을 해준다.
			List<OmsOrderproduct> approvalOrderProductList = (List<OmsOrderproduct>) dao.selectList("oms.logistics.getPartnerApprovalProduct", paramMap);
			
			for(OmsOrderproduct omsOrderproduct : approvalOrderProductList){
				
				// 주문상품 상태코드 업데이트
				omsOrderproduct.setStoreId(storeId);
				omsOrderproduct.setUpdId(updId);
				omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER");
				omsOrderproduct.setDeliveryOrderDt(approvalDt);
				omsOrderproduct.setDeliveryCancelReasonCd("");
				
				if(dao.updateOneTable(omsOrderproduct) > 0){
					updCnt++;
					// 세트SUB상품일 경우 부모상태 업데이트, 주문테이블의 배송상태 업데이트
					this.updateOrderStateCd(omsOrderproduct, "ORDER_DELIVERY_STATE_CD.DELIVERY_ORDER", "ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER", null);
				}
			}
		}
		
		return "총 주문상품 " + String.valueOf(deliveryApprovalList.size()) +  "건중 " + String.valueOf(updCnt) + "건을 성공적으로 처리했습니다.";
	}
	
	/**
	 * 
	 * @Method Name : getPartnerShippingList
	 * @author : brad
	 * @date : 2016. 8. 10.
	 * @description : 출고완료처리_협력사 목록 조회
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getPartnerShippingList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.getPartnerShipping", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : getReturnList
	 * @author : brad
	 * @date : 2016. 8. 11.
	 * @description : 반품확인
	 *
	 * @param omsLogisticsSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OmsLogistics> getReturnConfirmList(OmsLogisticsSearch omsLogisticsSearch) throws Exception {
		return (List<OmsLogistics>) dao.selectList("oms.logistics.getReturnConfirmList", omsLogisticsSearch);
	}
	
	/**
	 * 
	 * @Method Name : updateReturnConfirm
	 * @author : brad
	 * @date : 2016. 9. 10.
	 * @description : 반품 확인 처리
	 *
	 * @param returnConfirmList
	 * @throws Exception
	 */
	public String updateReturnConfirm(List<OmsLogistics> returnConfirmList) throws Exception {
		int updCnt = 0;
		// 입고확정 일시
		String returnDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);
		String updId = SessionUtil.getLoginId();
		String storeId = SessionUtil.getStoreId();
		
		Map<String, Object> params = new HashMap<>();
		params.put("updId", updId);
		params.put("storeId", storeId);
		
		for(OmsLogistics omsLogistics : returnConfirmList) {
			OmsOrderproduct omsOrderproduct = omsLogistics.getOmsOrderproduct();        // 주문상품
			OmsClaimproduct omsClaimproduct = omsLogistics.getOmsClaimproduct();        // 클레임상품
			
			String claimTypeCd = omsClaimproduct.getOmsClaim().getClaimTypeCd();        // 클레임타입
			String orderProductTypeCd = omsOrderproduct.getOrderProductTypeCd();        // 주문상품유형코드
			String orderId = omsLogistics.getOrderId();                                 // 주문번호
			BigDecimal claimNo = omsLogistics.getClaimNo();                             // 클레임번호
			BigDecimal upperOrderProductNo = omsOrderproduct.getUpperOrderProductNo();  // 세트상품의 부모의 일련번호
			
			BigDecimal goodInQty = omsLogistics.getGoodInQty();                                                // 정상입고수량
			BigDecimal badInQty = (BigDecimal) CommonUtil.replaceNull(omsLogistics.getBadInQty(), 0);          // 불량입고수량
			BigDecimal virtualInQty = (BigDecimal) CommonUtil.replaceNull(omsLogistics.getVirtualInQty(), 0);  // 가상입고수량
			
			BigDecimal returnQty = goodInQty.add(badInQty).add(virtualInQty);            // 입고 업데이트 수량
			BigDecimal updateStockQty = goodInQty.add(badInQty);                         // 재고 업데이트 수량(가상입고수량은 제외한다.)
			
			omsLogistics.setLogisticsStateCd("LOGISTICS_STATE_CD.RETURN");
			omsLogistics.setCompleteDt(returnDt);
			omsLogistics.setUpdDt(returnDt);
			omsLogistics.setUpdId(updId);
			
			if(dao.update("oms.logistics.updateReturnConfirm", omsLogistics) > 0){
				updCnt++;
				// 클레임상품 상태코드 업데이트
				omsClaimproduct.setClaimProductStateCd("CLAIM_PRODUCT_STATE_CD.RETURN");
				omsClaimproduct.setReturnDt(returnDt);
				omsClaimproduct.setUpdId(updId);
				dao.updateOneTable(omsClaimproduct);
				
				if("CLAIM_TYPE_CD.EXCHANGE".equals(claimTypeCd)){
					// 주문상품 테이블의 반품수량 업데이트(교환일때만)
					omsOrderproduct.setReturnQty(returnQty);
					omsOrderproduct.setUpdId(updId);
					
					dao.update("oms.logistics.updateReturnQty", omsOrderproduct);
					
					// 교환상품은 입고시 클레임상품들이 모두 입출고 되면 클레임 테이블의 상태를 바꿔준다.
					params.put("orderId", orderId);
					params.put("claimNo", claimNo);
					params.put("claimStateCd", "CLAIM_STATE_CD.COMPLETE");
					params.put("orderDeliveryTypeCd", "ORDER_DELIVERY_TYPE_CD.EXCHANGE");
					
					dao.update("oms.logistics.updateClaimState", params);
					
				}else if("CLAIM_TYPE_CD.RETURN".equals(claimTypeCd)){
					// 세트구성상품은 부모의 상태도 변경해준다.
					if("ORDER_PRODUCT_TYPE_CD.SUB".equals(orderProductTypeCd) && CommonUtil.isNotEmpty(upperOrderProductNo)){
						
						params.put("orderId", orderId);
						params.put("claimNo", claimNo);
						params.put("returnDt", returnDt);
						params.put("orderProductNo", upperOrderProductNo);
						params.put("claimProductStateCd", "CLAIM_PRODUCT_STATE_CD.RETURN");
						
						dao.update("oms.logistics.updateClaimOrderProductState", params);
					}
				}
				// 재고 업데이트
				updateStockQty(omsLogistics, updateStockQty, false, "");
			}
		}
		
		return "총 " + String.valueOf(returnConfirmList.size()) +  "건중 " + String.valueOf(updCnt) + "건을 성공적으로 처리했습니다.";
	}
	
	/**
	 * 
	 * @Method Name : updateStockQty
	 * @author : brad
	 * @date : 2016. 9. 15.
	 * @description : 상품재고 UPDATE
	 *
	 * @param omsLogistics
	 * @param updateStock
	 * @param minus
	 * @param stockMinus
	 */
	private void updateStockQty(OmsLogistics omsLogistics, BigDecimal updateStock, boolean minus, String stockMinus) throws Exception {
		PmsSaleproduct pmsSaleproduct = new PmsSaleproduct();
		pmsSaleproduct.setStoreId(omsLogistics.getOmsOrderproduct().getStoreId());
		pmsSaleproduct.setSaleproductId(omsLogistics.getOmsOrderproduct().getSaleproductId());
		pmsSaleproduct.setStockMinus(stockMinus);
		
		if (minus) {
			pmsSaleproduct.setRealStockQty(updateStock.negate());
		} else {
			pmsSaleproduct.setRealStockQty(updateStock);
		}
		
		dao.update("pms.product.updateStock", pmsSaleproduct);

		if (!"SUCCESS".equals(pmsSaleproduct.getResult())) {
			String msg = pmsSaleproduct.getMsg();
			logger.debug(msg);
			String productname = omsLogistics.getOmsOrderproduct().getProductName() + "-" + omsLogistics.getOmsOrderproduct().getSaleproductName();
			throw new ServiceException("oms.order.nonStockQty", new String[] { productname });
		}
	}
	
	/**
	 * 
	 * @Method Name : updateInvoiceNo
	 * @author : brad
	 * @date : 2016. 11. 4.
	 * @description : 협력사 운송장번호 업데이트
	 *
	 * @param omsLogisticsList
	 * @return
	 * @throws Exception
	 */
	public String updatePartnerInvoiceNo(List<OmsLogistics> omsLogisticsList) throws Exception {
		int updCnt = 0;
		String updId = SessionUtil.getLoginId();
		
		for(OmsLogistics omsLogistics : omsLogisticsList) {
			omsLogistics.setUpdId(updId);
			
			if(dao.updateOneTable(omsLogistics) > 0){
				updCnt++;
			}
		}
		
		return "총 " + String.valueOf(omsLogisticsList.size()) +  "건중 " + String.valueOf(updCnt) + "건의 운송장번호를 성공적으로 변경하였습니다.";
	}
	
	/**
	 * 
	 * @Method Name : selectShippingLogistics
	 * @author : brad
	 * @date : 2016. 9. 28.
	 * @description : 출고완료 엑셀 일괄 업로드 데이터 조회
	 *
	 * @param bulk
	 * @throws Exception
	 */
	public OmsLogistics selectShippingLogistics(Object bulk) throws Exception {
		return (OmsLogistics) dao.selectOne("oms.logistics.selectShippingLogistics", bulk);
	}
	
	/**
	 * 
	 * @Method Name : orderdeliveryComplete
	 * @author : brad
	 * @date : 2016. 10. 28.
	 * @description : 배송 완료 상태 업데이트(배치)
	 *
	 * @param bulk
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> orderDeliveryComplete(String updId) throws Exception {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		
		String storeId = Config.getString("store.id");
		String deliveryDt = DateUtil.getCurrentDate(DateUtil.FORMAT_8);;
		
		List<OmsOrderproduct> list = (List<OmsOrderproduct>) dao.selectList("oms.logistics.selectDeliveryCompleteOrderProductList", storeId);
		
		int totalCnt = list.size();
		int successCnt = 0;
		int failCnt = 0;
		
		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();
		
		for(OmsOrderproduct omsOrderproduct : list){
			
			Map<String, String> subResult = new HashMap<>();
			omsOrderproduct.setUpdId(updId);
			omsOrderproduct.setStoreId(storeId);
			omsOrderproduct.setDeliveryDt(deliveryDt);
			
			try {
				subResult = this.updateDeliveryCompleteOrderproduct(omsOrderproduct);
			
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}
			
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n======================================");
				errorMsg.append("\n주문 ID : " + omsOrderproduct.getOrderId());
				errorMsg.append("\n주문상품일렬번호 : " + omsOrderproduct.getOrderProductNo());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n======================================\n");
				resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;
			} else {
				successCnt++;
			}
		}
		
		resultMsg.append("\n총건수 	: " + totalCnt);
		resultMsg.append("\n성공 	: " + successCnt);
		resultMsg.append("\n실패 	: " + failCnt);
		
		if (BaseConstants.RESULT_FLAG_FAIL.equals(resultMap.get(BaseConstants.RESULT_FLAG))) {
			resultMsg.append(errorMsg);
		}
		
		resultMap.put(BaseConstants.RESULT_MESSAGE, resultMsg.toString());
		return resultMap;
	}
	
	/**
	 * 
	 * @Method Name : updateDeliveryCompleteOrderproduct
	 * @author : brad
	 * @date : 2016. 10. 28.
	 * @description : 주문상품별 배송 완료 상태 업데이트(배치)
	 *
	 * @param bulk
	 * @throws Exception
	 */
	public Map<String, String> updateDeliveryCompleteOrderproduct(OmsOrderproduct omsOrderproduct) throws Exception {
		
		Map<String, String> result = new HashMap<>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		
		omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.DELIVERY");
		
		if(dao.updateOneTable(omsOrderproduct) > 0){
			// 주문테이블의 배송상태 업데이트, 세트SUB상품일 경우는 부모의 상태도 업데이트 
			this.updateOrderStateCd(omsOrderproduct, "ORDER_DELIVERY_STATE_CD.DELIVERY", "ORDER_PRODUCT_STATE_CD.DELIVERY", null);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name : updateDeliveryComplete
	 * @author : brad
	 * @date : 2016. 10. 28.
	 * @description : 배송 완료 상태 업데이트(스윗트래커 API)
	 *
	 * @param bulk
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateDeliveryComplete(OmsDeliverytracking omsDeliverytracking) {
		
		String updId = omsDeliverytracking.getUpdId();
		String storeId = Config.getString("store.id");
		
		List<OmsOrderproduct> productList = (List<OmsOrderproduct>) dao.selectList("oms.logistics.selectSweetTrackerOrderProductList", omsDeliverytracking);
		
		for(OmsOrderproduct omsOrderproduct : productList){
			
			// 주문상품 상태코드 업데이트
			omsOrderproduct.setUpdId(updId);
			omsOrderproduct.setStoreId(storeId);
			omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.DELIVERY");
			
			try {
				
				if(dao.updateOneTable(omsOrderproduct) > 0){
					// 주문테이블의 배송상태 업데이트, 세트SUB상품일 경우는 부모의 상태도 업데이트 
					this.updateOrderStateCd(omsOrderproduct, "ORDER_DELIVERY_STATE_CD.DELIVERY", "ORDER_PRODUCT_STATE_CD.DELIVERY", null);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("\n====================================");
				logger.error("\n스윗트래커 배송 완료 업데이트 실패");
				logger.error("\n주문번호 : " + omsOrderproduct.getOrderId());
				logger.error("\n주문상품일련번호 : " + omsOrderproduct.getOrderProductNo());
				logger.error("\n에러 : " + e.getMessage());
				logger.error("\n====================================");
			}
			
		}
	}
}
