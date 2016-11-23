package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsOrdercoupon;
import gcp.mms.model.MmsMember;
import gcp.sps.model.SpsCoupon;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsCouponissue extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String couponId; //쿠폰ID		[primary key, primary key, primary key, not null]
	private BigDecimal couponIssueNo; //쿠폰발행번호		[primary key, primary key, primary key, not null]
	private String privateCin; //개별인증코드		[null]
	private String couponIssueStateCd; //쿠폰발행상태코드		[not null]
	private BigDecimal memberNo; //회원번호		[null]
	private String regDt; //등록일시		[null]
	private String useStartDt; //사용가능시작일시		[null]
	private String useEndDt; //사용가능종료일시		[null]
	private String useDt; //사용일시		[null]
	private String campaignId; //캠페인ID		[null]
	private BigDecimal campaignOrder; //캠페인실행회차		[null]
	private String segmentId; //고객군ID		[null]

	private List<OmsOrdercoupon> omsOrdercoupons;
	private MmsMember mmsMember;
	private SpsCoupon spsCoupon;

	public String getCouponIssueStateName(){
			return CodeUtil.getCodeName("COUPON_ISSUE_STATE_CD", getCouponIssueStateCd());
	}
}