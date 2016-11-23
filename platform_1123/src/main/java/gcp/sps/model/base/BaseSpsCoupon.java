package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsExhibitcoupon;
import gcp.oms.model.OmsCart;
import gcp.sps.model.SpsCoupondeal;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsCouponlang;
import gcp.sps.model.SpsEventcoupon;
import gcp.sps.model.SpsEventprize;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsCoupon extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String couponId; //쿠폰ID		[primary key, primary key, primary key, not null]
	private String name; //쿠폰명		[not null]
	private String couponTypeCd; //쿠폰유형코드		[not null]
	private String downShowYn; //다운로드노출여부		[not null]
	private String dealApplyYn; //딜적용가능여부		[not null]
	private BigDecimal applyNo; //적용대상번호		[null]
	private BigDecimal controlNo; //사용제어번호		[null]
	private String dcApplyTypeCd; //할인적용유형코드		[not null]
	private BigDecimal dcValue; //할인적용값		[not null]
	private BigDecimal maxDcAmt; //최대할인금액		[null]
	private String affiliateYn; //제휴쿠폰여부		[not null]
	private String businessId; //제휴업체ID		[null]
	private BigDecimal businessBurdenRate; //업체비용부담율		[null]
	private BigDecimal minOrderAmt; //사용가능최소주문금액		[null]
	private String termTypeCd; //사용가능기간구분코드		[not null]
	private BigDecimal termDays; //사용가능일수		[null]
	private String termStartDt; //사용가능시작일시		[null]
	private String termEndDt; //사용가능종료일시		[null]
	private String memIssueBasisCd; //인당발급기간기준코드		[not null]
	private BigDecimal maxMemIssueQty; //인당발급가능수량		[not null]
	private BigDecimal maxIssueQty; //총발급가능수량		[not null]
	private String issueStartDt; //발급시작일자		[not null]
	private String issueEndDt; //발급종료일자		[not null]
	private String couponIssueTypeCd; //쿠폰발급유형코드		[not null]
	private String feeLimitApplyYn; //한계수수료미만상품적용가능여부		[not null]
	private String discountDupYn; //할인중복여부		[not null]
	private String couponStateCd; //쿠폰상태코드		[not null]
	private String campaignYn; //캠페인쿠폰여부		[null]
	private String singleApplyYn; //1개적용여부		[null]

	private List<DmsExhibitcoupon> dmsExhibitcoupons;
	private List<OmsCart> omsCarts;
	private List<SpsCoupondeal> spsCoupondeals;
	private List<SpsCouponissue> spsCouponissues;
	private List<SpsCouponlang> spsCouponlangs;
	private List<SpsEventcoupon> spsEventcoupons;
	private List<SpsEventprize> spsEventprizes;
	private CcsApply ccsApply;
	private CcsBusiness ccsBusiness;
	private CcsControl ccsControl;
	private CcsStore ccsStore;

	public String getCouponTypeName(){
			return CodeUtil.getCodeName("COUPON_TYPE_CD", getCouponTypeCd());
	}

	public String getDcApplyTypeName(){
			return CodeUtil.getCodeName("DC_APPLY_TYPE_CD", getDcApplyTypeCd());
	}

	public String getTermTypeName(){
			return CodeUtil.getCodeName("TERM_TYPE_CD", getTermTypeCd());
	}

	public String getMemIssueBasisName(){
			return CodeUtil.getCodeName("MEM_ISSUE_BASIS_CD", getMemIssueBasisCd());
	}

	public String getCouponIssueTypeName(){
			return CodeUtil.getCodeName("COUPON_ISSUE_TYPE_CD", getCouponIssueTypeCd());
	}

	public String getCouponStateName(){
			return CodeUtil.getCodeName("COUPON_STATE_CD", getCouponStateCd());
	}
}