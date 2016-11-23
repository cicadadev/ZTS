package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsOrderproduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsErpif extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal erpIfNo; //ERP연동번호		[primary key, primary key, primary key, not null]
	private BigDecimal erpProductNo; //ERP상품일련번호		[primary key, primary key, primary key, not null]
	private String erpProductTypeCd; //ERP상품유형코드		[not null]
	private String erpOrderTypeCd; //ERP주문유형코드		[not null]
	private BigDecimal orderProductNo; //주문상품일련번호		[null]
	private String productName; //상품명		[not null]
	private BigDecimal claimNo; //클레임번호		[null]
	private BigDecimal deliveryAddressNo; //배송지번호		[null]
	private BigDecimal deliveryPolicyNo; //배송정책번호		[null]
	private BigDecimal qty; //수량		[not null]
	private BigDecimal saleAmt; //판매금액||상품당		[not null]
	private BigDecimal ownDcAmt; //자사부담할인금액||상품당		[not null]
	private String productId; //상품ID		[not null]
	private String saleproductName; //단품명		[not null]
	private String saleproductId; //단품ID		[not null]
	private BigDecimal businessDcAmt; //업체부담할인금액||상품당		[not null]
	private BigDecimal plusCouponDcAmt; //플러스쿠폰할인금액||상품당		[not null]
	private BigDecimal orderCouponDcAmt; //주문쿠폰할인금액||상품당		[not null]
	private BigDecimal paymentAmt; //고객결제금액		[not null]
	private BigDecimal depositAmt; //예치금||상품당		[not null]
	private BigDecimal usePoint; //사용포인트||상품당		[not null]
	private BigDecimal cashRefundAmt; //현금환불액		[not null]
	private BigDecimal depositRefundAmt; //예치금환불액		[not null]
	private BigDecimal savePoint; //적립포인트||상품당		[not null]
	private String fixedDt; //기준일시		[not null]
	private String paymentMethodCd; //결제수단코드		[not null]
	private String businessId; //업체ID		[null]
	private String businessName; //업체명		[null]
	private String erpBusinessId; //ERP업체ID		[null]
	private String saleTypeCd; //매입유형코드		[not null]
	private String purchaseYn; //위탁매입여부		[not null]
	private String erpProductId; //ERP상품ID		[null]
	private String erpSaleproductId; //ERP단품ID		[null]
	private String erpColorId; //ERP색상옵션		[null]
	private String erpSizeId; //ERP사이즈옵션		[null]
	private String taxTypeCd; //과세구분코드		[not null]
	private BigDecimal totalSalePrice; //판매단가		[null]
	private BigDecimal supplyPrice; //공급가		[null]
	private BigDecimal commissionRate; //수수료율		[null]
	private String dealTypeCd; //딜유형코드		[null]

	private OmsDelivery omsDelivery;
	private OmsOrderproduct omsOrderproduct;

	public String getErpProductTypeName(){
			return CodeUtil.getCodeName("ERP_PRODUCT_TYPE_CD", getErpProductTypeCd());
	}

	public String getErpOrderTypeName(){
			return CodeUtil.getCodeName("ERP_ORDER_TYPE_CD", getErpOrderTypeCd());
	}

	public String getPaymentMethodName(){
			return CodeUtil.getCodeName("PAYMENT_METHOD_CD", getPaymentMethodCd());
	}

	public String getSaleTypeName(){
			return CodeUtil.getCodeName("SALE_TYPE_CD", getSaleTypeCd());
	}

	public String getTaxTypeName(){
			return CodeUtil.getCodeName("TAX_TYPE_CD", getTaxTypeCd());
	}

	public String getDealTypeName(){
			return CodeUtil.getCodeName("DEAL_TYPE_CD", getDealTypeCd());
	}
}