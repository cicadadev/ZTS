package gcp.oms.model.search;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import gcp.pms.model.PmsSaleproduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsLogisticsSearch extends BaseSearchCondition {

	private static final long serialVersionUID = -6576315707762558071L;

	// 1. 주문메뉴 관련
	private String orderId;               // 주문번호
	private String orderIds;              // 주문번호
	
	private String orderType;             // 주문구분
	private String orderTypeCds;          // 주문구분코드
	private boolean orderTypeAllChecked; // 주문구분전체체크여부
	
	private String orderStateCds;         // 주문상태
	private String orderDeliveryStateCds; // 배송상태

	private String ordererType; // 주문자
	private String orderer;
	private String receiverType; // 수취인
	private String receiver;

	private String productType; // 상품정보
	private String product; // 상품정보
	
	private String saleProductType; // 단품정보
	private String saleProduct;

	private String siteId;      // 제휴사이트
	private String siteIds;     // 제휴사이트(복수)
	private String siteOrderId; // 제휴주문번호

	private String productId;   // 상품번호
	private String productName; // 상품명

	private BigDecimal memberNo; // member no

	private String brandId; // 브랜드ID
	private String categoryId; // 카테고리 ID

	// 2. 물류메뉴 관련
	private String businessName;             // 업체이름
	
	private String logisticsIfType;          // 연동타입(DAS, 한진)
	private String locationId;               // 창고로케이션ID
	private String startDeliveryOrder;       // 시작배송차수
	private String endDeliveryOrder;         // 종료배송차수

	private String deliveryMethodType;       // 배송방법(센터배송/업체배송)
	
	private String dateType;                 // 기간구분유형
	private String returnSenderType;         // 반품발송인유형
	private String returnSender;             // 반품발송인
	private String saleTypeCd;               // 매입유형코드
	private String locationRegistYn;         // 로케이션등록여부
	private String normalYn;                 // 승인에러여부
	private String cancelYn;                 // 정상/미출고,승인취소 여부
	private String sendErrorReasonCd;        // 승인에러사유코드
	private String deliveryCancelReasonCd;   // 배송승인취소사유코드
	private String lpNo;                     // LP넘버
	private String warehouseId;              // 창고아이디
	private String deliveryType;             // 배송구분
	private boolean fixedDelAddr;           // 고정배송지여부
	// 3.선물포장비 정보
	private String itemCd;                   // PRODUCT_ID
	private String unitId;                   // SALEPRODUCT_ID
	private String barcode;                  // ERP_SALEPRODUCT_ID
	private String sectionNm;                // LOCATION_ID
	private String venId;                    // BUSINESS_ID
	private String venName;                  // BUSINESS_NAME
	
	private List<PmsSaleproduct> pmsSaleproductList;  // 로케이션 관리시 선택한 단품리스트
	
	private boolean chinaMallYnAllChecked;    // 중국몰주문전체체크여부
	private String chinaMallYn;                // 중국몰주문여부 (Y: 중국몰포함, N: 중국몰미포함)
	
	private boolean orderProductTypeAllChecked; // 단품주문전체체크여부
	private String orderProductType;             // 단품주문여부
	private String orderProductTypeCds;          // 단품주문여부코드
	
	private boolean orderDeliveryTypeAllChecked; // 발송유형전체체크여부
	private String orderDeliveryType;             // 발송유형
	private String orderDeliveryTypeCds;          // 발송유형코드
	
	private String orderProductStateAllChecked;  // 주문상품상태코드(출고완료/미출고)
	private String orderProductState;            // 주문상품상태코드(출고완료/미출고)
	private String orderProductStateCds;         // 주문상품상태코드(출고완료/미출고)
	
	private boolean deliveryMethodAllChecked;   // 배송방법전체체크여부
	private String deliveryMethod;               // 배송방법
	private String deliveryMethodCds;            // 배송방법코드

	private boolean sendYnAllChecked;           // 한진데이터전송전체체크여부
	private String sendYn;                       // 한진데이터전송여부
	private String sendYnCds;            		  // 한진데이터전송여부코드
	
	private boolean returnYnAllChecked;         // 입고여부전체체크여부
	private String returnYn;                     // 입고여부
	private String returnYnCds;            		  // 입고여부코드
	
	private boolean confirmYnAllChecked;        // 출고완료전체체크여부
	private String confirmYn;                    // 출고완료여부
	private String confirmYnCds;            	  // 출고완료여부코드
	
	private boolean approvalStatusAllChecked;   // 배송승인전체체크여부
	private String approvalStatus;               // 배송승인여부
	private String approvalStatusCds;            // 배송승인여부코드

	private boolean logisticsStateAllChecked;   // 입고상태전체체크여부
	private String logisticsState;               // 입고상태
	private String logisticsStateCds;            // 입고상태코드

	private List<String> returnState;            // 반품상태
	private List<String> saleTypeList;           // 매입유형코드리스트
	
	// 4. 창고 로케이션 관리
	private boolean locationUseYnAllChecked;    // 로케이션사용전체체크여부
	private String locationUseYn;                // 로케이션사용여부
	private String locationUseYnCds;             // 로케이션사용여부코드
	
	public String getOrderType() {
		String retStr = null;
		if (!CommonUtil.isEmpty(orderType) && !orderTypeAllChecked) {
			Set<String> tmpOrderType = new HashSet<>();
			String[] orderTypeArray = orderType.split(",");
			
			for(String type : orderTypeArray){
				switch (type) {
					case "GENERAL": 
						tmpOrderType.add("ORDER_TYPE_CD.GENERAL");
						tmpOrderType.add("ORDER_TYPE_CD.GIFT");
						tmpOrderType.add("ORDER_TYPE_CD.EXTERNAL");
						tmpOrderType.add("ORDER_TYPE_CD.PROMOTION");
						break;
					case "RESERVE":
						tmpOrderType.add("ORDER_TYPE_CD.RESERVE");
						break;
					case "REGULARDELIVERY":
						tmpOrderType.add("ORDER_TYPE_CD.REGULARDELIVERY");
						break;
					case "B2E":
						tmpOrderType.add("ORDER_TYPE_CD.B2E");
						break;	
					default:
						break;
				}
			}
			retStr = "'" + StringUtils.join(tmpOrderType, "','") + "'";;
		}
		return retStr;
	}
	
	public String getChinaMallYn() {
		String retStr = null;
		if (!CommonUtil.isEmpty(chinaMallYn) && !chinaMallYnAllChecked) {
			retStr = chinaMallYn;
		}
		return retStr;
	}
	
//	public String getConfirmYn() {
//		String retStr = null;
//		if (!CommonUtil.isEmpty(confirmYn) && !confirmYnAllChecked) {
//			StringBuilder queryStr = new StringBuilder();
//			String[] statusArray = confirmYn.split(",");
//			
//			for(String status : statusArray){
//				switch (status) {
//					case "ORDER_PRODUCT_STATE_CD.SHIP": 
//						if(queryStr.length() > 0){
//							queryStr.append(" OR ");
//						}
//						queryStr.append("(T4.ORDER_PRODUCT_STATE_CD = 'ORDER_PRODUCT_STATE_CD.SHIP' AND T6.OUT_QTY > 0)");
//						break;
//					case "ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY":
//						if(queryStr.length() > 0){
//							queryStr.append(" OR ");
//						}
//						queryStr.append("(T4.ORDER_PRODUCT_STATE_CD = 'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY')");
//						break;
//					case "ORDER_PRODUCT_STATE_CD.CANCELDELIVERY": 
//						if(queryStr.length() > 0){
//							queryStr.append(" OR ");
//						}
//						queryStr.append("(T4.ORDER_PRODUCT_STATE_CD = 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY')");
//						break;
//					default:
//						break;
//				}
//			}
//			retStr = queryStr.toString();
//		}
//		return retStr;
//	}
	
	public String getReturnYn() {
		String retStr = null;
		if (!CommonUtil.isEmpty(returnYn) && !returnYnAllChecked) {
			retStr = returnYn;
		}
		return retStr;
	}
	
	public String getApprovalStatus() {
		String retStr = null;
		if (!CommonUtil.isEmpty(approvalStatus) && !approvalStatusAllChecked){
			Set<String> tmpOrderProductState = new HashSet<>();
			String[] statusArray = approvalStatus.split(",");
			
			for(String status : statusArray){
				switch (status) {
					case "APPROVAL": 
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER");
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.SHIP");
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.DELIVERY");
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.CONFIRMED");
						break;
					case "NO_APPROVAL": 
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.READY");
						break;
					case "CANCEL_DELIVERY": 
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.CANCELDELIVERY");
						break;
					case "CANCEL_APPROVAL":
						tmpOrderProductState.add("ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL");
						break;
					default:
						break;
				}
			}
			retStr = "'" + StringUtils.join(tmpOrderProductState, "','") + "'";
		}
		return retStr;
	}
	
	public String getOrderProductType() {
		String retStr = null;
		if (!CommonUtil.isEmpty(orderProductType) && !orderProductTypeAllChecked) {
			retStr = orderProductType;
		}
		return retStr;
	}
	
	public String getDeliveryMethod() {
		String retStr = null;
		if (!CommonUtil.isEmpty(deliveryMethod) && !deliveryMethodAllChecked) {
			retStr = deliveryMethod;
		}
		return retStr;
	}
	
	public String getLocationUseYn() {
		String retStr = null;
		
		if (!CommonUtil.isEmpty(locationUseYn) && !locationUseYnAllChecked) {
			Set<String> temp = new HashSet<>();
			String[] useYnArray = locationUseYn.split(",");
			
			for(String useYn : useYnArray){
				switch (useYn) {
					case "Y": 
						temp.add("Y");
						break;
					case "N": 
						temp.add("N");
						break;
					case "NR": 
						setLocationRegistYn("Y");
						break;
					default:
						break;
				}
			}
			
			if(CommonUtil.isNotEmpty(temp)){
				retStr = "'" + StringUtils.join(temp, "','") + "'";
			}
		}
		return retStr;
	}
	
	public String getReturnState() {
		String retStr = null;
		if (!CommonUtil.isEmpty(returnState) && returnState.size() < 2) {
			retStr = returnState.get(0);
		}
		return retStr;
	}
	
	public String getOrderDeliveryType() {
		String retStr = null;
		if (!CommonUtil.isEmpty(orderDeliveryType) && !orderDeliveryTypeAllChecked){
			Set<String> tmpOrderDeliveryType = new HashSet<>();
			String[] typeArray = orderDeliveryType.split(",");
			
			for(String type : typeArray){
				switch (type) {
					case "ORDER": 
						tmpOrderDeliveryType.add("ORDER_DELIVERY_TYPE_CD.ORDER");
						break;
					case "CLAIM":
						tmpOrderDeliveryType.add("ORDER_DELIVERY_TYPE_CD.EXCHANGE");
						tmpOrderDeliveryType.add("ORDER_DELIVERY_TYPE_CD.REDELIVERY");
						break;
					default:
						break;
				}
			}
			retStr = "'" + StringUtils.join(tmpOrderDeliveryType, "','") + "'";
		}
		return retStr;
	}
	
	public String getSaleTypeList() {
		String retStr = null;
		if (!CommonUtil.isEmpty(saleTypeList) && saleTypeList.size() < 2) {
			retStr = saleTypeList.get(0);
		}
		return retStr;
	}
	
	public String getSendYn() {
		String retStr = null;
		if (!CommonUtil.isEmpty(sendYn) && !sendYnAllChecked) {
			retStr = sendYn;
		}
		return retStr;
	}
	
}
