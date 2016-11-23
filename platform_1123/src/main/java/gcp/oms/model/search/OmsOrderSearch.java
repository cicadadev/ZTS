package gcp.oms.model.search;

import java.math.BigDecimal;
import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsOrderSearch extends BaseSearchCondition {

	private static final long serialVersionUID = -6576315707762558071L;

	// 1. 주문메뉴 관련
	private String orderId; // 주문번호
	private String orderIds; // 주문번호's
	private String orderTypeCds; // 주문구분's
	private String orderStateCds; // 주문상태's
	private String orderDeliveryStateCds; // 배송상태's
	private String deviceTypeCds; // 구매채널's

	private String orderProductNo; // 주문단품
	private String ordererType; // 주문자
	private String orderer;
	private String receiverType; // 수취인
	private String receiver;
	
	private String pickupProductStateCd;

	private String productType; // 상품정보
	private String product; // 상품정보
	private String saleProductType; // 단품정보
	private String saleProduct;
	
	private String area1;
	private String area2;
	private String offshopId;
	

	private String siteId;      // 제휴사이트
	private String siteOrderId; // 제휴주문번호

	private String productId; // 상품번호
	private String productName; // 상품명
	private String pgShopId; // pg상점id
	private String couponStateCd; // 주문쿠폰적용상태
	private String couponTypeCds; // 쿠폰유형 
	private List<String> searchCoupons; // 쿠폰id+쿠폰issueNo 
	

	// 2. 회원메뉴 관련
	private BigDecimal memberNo; // member no
	private String dealId; // 딜ID

	private String brandId; // 브랜드ID
	private String categoryId; // 카테고리 ID

}
