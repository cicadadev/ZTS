package gcp.external.model.coupon;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UvMmsOffshopcouponissue {

	private String couponIssueNo;
	private String couponId;
	private String name;
	private String privateCin;
	private String couponIssueStateCd;
	private BigDecimal memberNo;
	private String dcApplyTypeCd;
	private BigDecimal dcValue;
	private BigDecimal maxDcAmt;
	private BigDecimal minOrderAmt;
	private String regDt;
	private String useStartDt;
	private String useEndDt;
	private String campaignYn;
	private String campaignId;
	private BigDecimal campaignOrder;
	private String segmentId;
	private String insDt;
	private String insId;
	private String updDt;
	private String updId;

	private int useCnt;
	private int endCnt;
	private String deadLine;
	
	// 리스트 카운트
	private int totalCount;
}
