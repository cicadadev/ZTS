package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaimdelivery;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsOrder;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsClaim extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal claimNo; //클레임번호		[primary key, primary key, primary key, not null]
	private String claimTypeCd; //클레임유형코드		[not null]
	private BigDecimal orderCouponDcCancelAmt; //주문쿠폰할인취소금액||클레임당		[null]
	private String returnPickupTypeCd; //반품수거방식		[null]
	private String claimStateCd; //클레임상태코드		[not null]
	private String reqDt; //신청일시		[not null]
	private String acceptDt; //접수일시		[null]
	private String completeDt; //완료일시		[null]
	private String cancelDt; //취소일시||반려일시||철회일시		[null]
	private BigDecimal productCouponDcCancelAmt; //상품쿠폰할인취소금액		[null]
	private BigDecimal plusCouponDcCancelAmt; //플러스쿠폰할인취소금액		[null]

	private List<OmsClaimdelivery> omsClaimdeliverys;
	private List<OmsClaimproduct> omsClaimproducts;
	private List<OmsOrderproduct> omsOrderproducts;
	private List<OmsPayment> omsPayments;
	private OmsOrder omsOrder;

	public String getClaimTypeName(){
			return CodeUtil.getCodeName("CLAIM_TYPE_CD", getClaimTypeCd());
	}

	public String getReturnPickupTypeName(){
			return CodeUtil.getCodeName("RETURN_PICKUP_TYPE_CD", getReturnPickupTypeCd());
	}

	public String getClaimStateName(){
			return CodeUtil.getCodeName("CLAIM_STATE_CD", getClaimStateCd());
	}
}