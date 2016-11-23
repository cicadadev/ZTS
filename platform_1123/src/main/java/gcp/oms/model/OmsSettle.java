package gcp.oms.model;

import java.math.BigDecimal;

import gcp.common.util.CodeUtil;
import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsSettle extends BaseEntity {
	//주문구분
	private String		orderType;
	//매출기준일
	private String		saleStandardDt;
	//주문일
	private String		orderDt;
	//주문번호
	private String		orderId;
	//정산유형
	private String		settleType;
	//공급업체ID
	private String		businessId;
	//공급업체명
	private String		businessName;
	//ERP업체코드
	private String 		erpBusinessId;
	//상품ID
	private String		productId;
	//상품명
	private String		productName;
	//단품ID
	private String		saleproductId;
	//단품명
	private String		saleproductName;
	//공급단가
	private BigDecimal	supplyPrice;
	//판매단가
	private BigDecimal	salePrice;
	//공급금액
	private BigDecimal	supplyAmt;
	//판매금액
	private BigDecimal	saleAmt;
	//수수료율
	private BigDecimal	commissionRate;
	//고객결제금액
	private BigDecimal	paymentAmt;
	//사입 회계매출
	private BigDecimal purchaseSales;
	//위탁 회계매출
	private BigDecimal consignSales;
	//매일포인트
	private BigDecimal maeilPoint;
	//예치금사용액
	private BigDecimal depositAmt;
	//자사쿠폰금액
	private BigDecimal ownCouponAmt;
	//업체쿠폰금액
	private BigDecimal bizCouponAmt;
	//플러스쿠폰금액
	private BigDecimal plusCouponAmt;
	//장바구니쿠폰금액
	private BigDecimal basketCouponAmt;
	//수량
	private BigDecimal qty;
	//사이트ID
	private String siteId;
	//사이트명
	private String siteName;
	//표준카테고리ID
	private String categoryId;
	//표준카테고리명
	private String categoryName;
	//과세구분
	private String taxTypeCd;
	//클레임번호
	private String claimNo;
	//클레임유형명
	private String claimTypeCd;
	//출고완료일
	private String shipDt;
	//입고완료일
	private String returnDt;
	//클레임완료일
	private String claimDt;
	//채널ID
	private String channelId;
	//채널명
	private String channelName;
	//OK캐쉬백번호
	private String okcashbagNo;

	//수수료
	private BigDecimal chargeAmt;
	//결제수단
	private String paymentMethodCd;
	//기타 금액
	private BigDecimal etcAmt;
	//업체에 지급할 금액
	private BigDecimal supportAmt;

	//기타 변수: ERP 상품유형코드
	private String erpProductTypeCd;
	//기타 변수: 매입유형코드
	private String saleTypeCd;
	//기타 변수: 위탁매입여부
	private String purchaseYn;
	//기타 변수: 수수료율 % 포함
	private String strCommissionRate;

	public String getTaxTypeName(){
		return CodeUtil.getCodeName("TAX_TYPE_CD", getTaxTypeCd());
	}

	public String getClaimTypeName(){
		return CodeUtil.getCodeName("CLAIM_TYPE_CD", getClaimTypeCd());
	}

	public String getPaymentMethodName(){
		return CodeUtil.getCodeName("PAYMENT_METHOD_CD", getPaymentMethodCd());
	}
}
