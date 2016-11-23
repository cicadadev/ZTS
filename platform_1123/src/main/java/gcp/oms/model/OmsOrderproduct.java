package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.common.util.CodeUtil;
import gcp.oms.model.base.BaseOmsOrderproduct;
import gcp.sps.model.SpsPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsOrderproduct extends BaseOmsOrderproduct {

	private static final long serialVersionUID = 8699885732578824015L;
	
	private List<SpsPresent> spsPresents; // 상품사은품

	private List<OmsOrdercoupon> omsOrdercoupons; // 상품 쿠폰
	private String couponId;	//최적 쿠폰id (앞에서 넘어온것)
	private OmsOrdercoupon optimalProductCoupon;	//최적쿠폰
	private OmsOrdercoupon optimalPlusCoupon;	//최적쿠폰
	
	private int treeLevel;
	private BigDecimal availableClaimQty; // 클레임가능수량
	private BigDecimal claimDeliveryFee; // 클레임에서 발생한 배송비
	private BigDecimal dcAmt;
	private BigDecimal orderAmt;
	private BigDecimal newOrderProductNo;
	private BigDecimal orgTotalSalePrice; // 원판매금액
	private String newSaleProductId; // 단품ID(재배송,교환)
	private String newSaleProductNm; // 단품이름(재배송,교환)
	private String deliveryChangeYn; // 배송변경 Y/N
	private String optionChangeYn; // 옵션변경 Y/N
	private String cancelYn; // 주문취소 Y/N
	private String returnYn; // 교환반품 Y/N
	private String reviewYn; // 상품평 Y/N
	private String trackingYn; // 배송추적 Y/N
	private String partCancelYn;
	
	private boolean cancelAll;
	private String deliveryCouponId;
	private String wrapCouponId;
	

	private BigDecimal setTotalSalePrice;	// set 구성상품 판매가 * 수량 합
	
	private String siteId;                 // 주문사이트ID
	private String siteName;               // 주문사이트이름
	private String deliveryMethod;         // 배송방법
	private String ordererId;              // 주문자ID
	private BigDecimal outReserveQty;      // 출고예정수량
	private String wrapTogetherYn;         // 합포장여부
	private BigDecimal wrapSize;           // 포장부피 * 주문수량
	
	private String deliveryOrder;           // 배송차수
	private String invoiceDt;               // 운송장생성일
	private String quotient;                // 피킹리스트 box수량
	private String remainder;               // 피킹리스트 EA
	private String locationUseYn;           // 매핑된로케이션사용여부
	private BigDecimal deliveryTogetherQty; // 합배송수량
	private BigDecimal divideCnt;           // 배송번호 분할갯수
	private String groupSeq;                // 배송번호별 그룹번호
	private String divideYn;                // 배송번호 분할여부
	private String lastYn;                  // 배송지그룹별 마지막주문상품 여부
	private String dualWrapYn;              // 이중포장여부
	private String orderDate;               // 주문일
	private String shipYn;                  // 출고완료여부
	private String gubun;                   // 상품,합배송요약 구분
	private String infoGubun;               // 배송정보 구분
	
	private String custOrdSeq;              // 배송번호별 그룹시퀀스
	private String deliveryServiceCd;       // 택배사 코드
	
	// 물류쪽 승인화면용(주문자/수취인정보)
	private String orderTypeCd;             // 주문구분
	private String ordererNm;               // 주문자이름
	private String ordererMobile;           // 주문자핸드폰번호
	private String receiverNm;              // 수취인이름
	private String receiverPhone;           // 수취인전화번호
	private String receiverMobile;          // 수취인핸드폰번호
	private String zipCd;                   // 수취인우편번호
	private String deliveryAddress;         // 수취인주소
	
	private String phone2;                  // 주문자핸드폰번호
	
	private BigDecimal controlNo;
	private BigDecimal dealControlNo;
	
	private String memGradeCd;	//회원등급.
	private String dealTypeCds;	//딜유형코드
	
	private BigDecimal minQty;	//최소주문수량
	
	private BigDecimal realStockQty;	//재고수량
	private BigDecimal personQty;	//1인구매재한 수량
	
	private String presentName;	//사은품 프로모션명
	private String startDt;	//사은품 시작
	private String endDt;	//사은품 종료.
	
	private List<OmsOrderproduct> omsOrderproducts;
	private OmsDelivery omsDelivery;        // 주문상품 배송정책
	
	// FO 
	
	private String imgUrl;	 // 이미지 Url
	private String reviewAbleDt;	 // 작성 가능일
	
	private BigDecimal productCnt;	// 주문상품 수량
	private String brandName;
	
	private BigDecimal regularDeliveryOrder;
	private BigDecimal deliveryProductNo;
	private String regularDeliveryId;
	
	private String orderStateCd;
	private OmsClaimproduct omsClaimproduct;
	
	private String saleStateCd;
	private String saleproductStateCd;
	
	private BigDecimal productCouponCnt;
	private BigDecimal plusCouponCnt;
	
	private String offshopPickupYn;
	private String regularDeliveryYn;
	
	public String getOrderProductStateNote(){
		return CodeUtil.getCodeNote(getOrderProductStateCd());
	}
	
	public String getOrderTypeName(){
		return CodeUtil.getCodeName("ORDER_TYPE_CD", getOrderTypeCd());
	}
	
}