package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsOrder;
import gcp.sps.model.SpsCouponissue;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsOrdercoupon extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String couponId; //쿠폰ID		[primary key, primary key, primary key, not null]
	private BigDecimal couponIssueNo; //쿠폰발행번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private String name; //쿠폰명		[not null]
	private String couponTypeCd; //쿠폰유형코드		[not null]
	private String dcApplyTypeCd; //할인적용유형코드		[not null]
	private BigDecimal dcValue; //할인적용값		[not null]
	private BigDecimal maxDcAmt; //최대할인금액		[null]
	private String affiliateYn; //제휴쿠폰여부		[not null]
	private String businessId; //제휴업체ID		[null]
	private BigDecimal businessBurdenRate; //업체비용부담율		[null]
	private BigDecimal minOrderAmt; //사용가능최소주문금액		[not null]
	private String feeLimitApplyYn; //한계수수료미만상품적용가능여부		[not null]
	private BigDecimal couponDcAmt; //쿠폰할인금액||상품쿠폰개당_주문쿠폰주문당		[not null]
	private BigDecimal couponDcCancelAmt; //쿠폰할인취소금액||주문쿠폰주문당		[null]
	private String couponStateCd; //쿠폰상태코드		[not null]
	private String singleApplyYn; //1개적용여부		[null]

	private List<OmsDelivery> omsDeliverys;
	private List<OmsOrderproduct> omsOrderproducts;
	private OmsOrder omsOrder;
	private SpsCouponissue spsCouponissue;

	public String getCouponTypeName(){
			return CodeUtil.getCodeName("COUPON_TYPE_CD", getCouponTypeCd());
	}

	public String getDcApplyTypeName(){
			return CodeUtil.getCodeName("DC_APPLY_TYPE_CD", getDcApplyTypeCd());
	}

	public String getCouponStateName(){
			return CodeUtil.getCodeName("COUPON_STATE_CD", getCouponStateCd());
	}
}