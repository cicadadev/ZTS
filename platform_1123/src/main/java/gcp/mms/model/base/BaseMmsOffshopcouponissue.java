package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsOffshopcouponissue extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal couponIssueNo; //쿠폰발행번호		[primary key, primary key, primary key, not null]
	private String couponId; //쿠폰ID		[primary key, primary key, primary key, not null]
	private String name; //쿠폰명		[not null]
	private String privateCin; //개별인증코드		[null]
	private String couponIssueStateCd; //쿠폰발행상태코드		[not null]
	private BigDecimal memberNo; //회원번호		[null]
	private String dcApplyTypeCd; //할인적용유형코드		[not null]
	private BigDecimal dcValue; //할인적용값		[not null]
	private BigDecimal maxDcAmt; //최대할인금액		[null]
	private BigDecimal minOrderAmt; //사용가능최소주문금액		[null]
	private String regDt; //등록일시		[null]
	private String useStartDt; //사용가능시작일시		[null]
	private String useEndDt; //사용가능종료일시		[null]
	private String useDt; //사용일시		[null]
	private String campaignYn; //캠페인쿠폰여부		[null]
	private String campaignId; //캠페인ID		[null]
	private BigDecimal campaignOrder; //캠페인실행회차		[null]
	private String segmentId; //고객군ID		[null]

	private MmsMemberZts mmsMemberZts;

	public String getCouponIssueStateName(){
			return CodeUtil.getCodeName("COUPON_ISSUE_STATE_CD", getCouponIssueStateCd());
	}

	public String getDcApplyTypeName(){
			return CodeUtil.getCodeName("DC_APPLY_TYPE_CD", getDcApplyTypeCd());
	}
}