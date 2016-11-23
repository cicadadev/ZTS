package gcp.sps.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.common.util.CodeUtil;
import gcp.mms.model.MmsMember;
import gcp.oms.model.OmsOrdercoupon;
import gcp.sps.model.base.BaseSpsCouponissue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsCouponissue extends BaseSpsCouponissue {

	private String			memberInfo;

	private List<MmsMember>	mmsMembers;
	private MmsMember		mmsMember;
	private OmsOrdercoupon	omsOrdercoupon;

	private String			deleteType;
	private BigDecimal		count;

	private String			couponName;
	private String			couponTypeCd;
	private String			dcApplyTypeCd;
	private String			dcValue;
	private int			noUseCount;
	private int			couponTotalCount;
	
	private String			memIssueBasisCd;	// 인당 발급 기준 코드

	private String			insName;		// 등록자
	private String			updName;		// 수정자

	private String			orderId;		// 주문번호
	
	/* FO */
	private String			useCoupon;
	private String			endCoupon;
	private String			useCouponOff;
	private String			endCouponOff;
	private String			deadLine;
	private String         campaignYn;
	
	private String			isApp;			// 마이페이지 앱전용 뱃지 표시
	
	private String         controllCheckType;// BO/FO
	
	private String offshopDataAreaId = "ZTS";
	private String offshopCouponType = "2";
	private String offshopMemberNo;
	private String offshopTypeNo;	//	
	private String offshopType;	//오프라인쿠폰 종류
	private String offshopSalePrice;	//오프라인쿠폰 대상 상품판매가	
	private String offshopValidDt;	//쿠폰 유효기간
	private String result;
	
	private List <SpsCouponissue> couponIssues;

	public String getCouponTypeName() {
		return CodeUtil.getCodeName("COUPON_TYPE_CD", getCouponTypeCd());
	}

	public String getDcApplyTypeName() {
		return CodeUtil.getCodeName("DC_APPLY_TYPE_CD", getDcApplyTypeCd());
	}

}