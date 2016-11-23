package gcp.mms.model;

import java.math.BigDecimal;

import gcp.mms.model.base.BaseMmsMemberZts;
import gcp.sps.model.SpsCouponissue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsMemberZts extends BaseMmsMemberZts {

	private MmsMember		mmsMember;
	private MmsBlacklist	mmsBlacklist;
	private String			address;

	private MmsCarrot		mmsCarrot;
	private MmsDeposit		mmsDeposit;
	private SpsCouponissue	spsCouponissue;
//	private MmsPoint		mmsPoint;
	private BigDecimal		coupon;
	private BigDecimal		carrot;
	private BigDecimal		deposit;
	private String		point;
	private String blacklistYn;
	
	private Long cpnIssueCnt;
	private BigDecimal depositBalanceAmt;
	private BigDecimal carrotBalanceAmt;
	
	private String latestAppLoginDt;
	private String latestPcLoginDt;
	private String latestMwLoginDt;
	
	private String updName;		// 수정자
	private String babyMonth;	// 아기 나이표현
	
	private int expireMonthPlus;// 제휴카드 만기일
	
}