package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsDelivery;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsClaimdelivery extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal deliveryAddressNo; //배송지번호		[primary key, primary key, primary key, not null]
	private BigDecimal deliveryPolicyNo; //배송정책번호		[primary key, primary key, primary key, not null]
	private BigDecimal claimNo; //클레임번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private BigDecimal refundDeliveryFee; //배송비환불		[not null]
	private BigDecimal orderDeliveryFee; //주문배송비		[not null]
	private BigDecimal returnDeliveryFee; //반품배송비		[not null]
	private BigDecimal exchangeDeliveryFee; //교환배송비		[not null]
	private BigDecimal refundWrapFee; //포장비환불		[not null]
	private String deliveryServiceCd; //택배사코드		[not null]
	private String returnName; //수거지수령인명1		[not null]
	private String returnPhone1; //수거지전화번호1		[null]
	private String returnPhone2; //수거지전화번호2		[null]
	private String returnEmail; //수거지이메일		[null]
	private String returnZipCd; //수거지우편번호		[null]
	private String returnAddress1; //수거지주소1		[null]
	private String returnAddress2; //수거지주소2		[null]
	private BigDecimal deliveryCouponDcCancelAmt; //배송비쿠폰할인취소금액		[null]
	private BigDecimal wrapCouponDcCancelAmt; //포장비쿠폰할인취소금액		[null]

	private OmsClaim omsClaim;
	private OmsDelivery omsDelivery;

	public String getDeliveryServiceName(){
			return CodeUtil.getCodeName("DELIVERY_SERVICE_CD", getDeliveryServiceCd());
	}

	public String getReturnZipName(){
			return CodeUtil.getCodeName("RETURN_ZIP_CD", getReturnZipCd());
	}
}