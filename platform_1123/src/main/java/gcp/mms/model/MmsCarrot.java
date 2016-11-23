package gcp.mms.model;

import java.math.BigDecimal;

import gcp.mms.model.base.BaseMmsCarrot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsCarrot extends BaseMmsCarrot {

	private MmsMember mmsMember;

	private BigDecimal balanceAmt;

	/* bo */
	private String memberInfo;
	private BigDecimal eventPlusAmt;
	private BigDecimal eventMinusAmt;
	private BigDecimal csPlusAmt;
	private BigDecimal csMinusAmt;
	private BigDecimal expiredAmt;
	private BigDecimal latestCarrotAmt;

	private BigDecimal plusMem;
	private BigDecimal plusCarrot;
	private BigDecimal minusMem;
	private BigDecimal minusCarrot;

	/* fo */
	private BigDecimal endCarrot;
	private BigDecimal plus;
	private BigDecimal minus;
	private String term;

	private String insName; // 등록자
	private String updName; // 수정자

	private String nextMonthFirst;	// 안내 메일 시작일
	private String nextMonthLast;	// 안내 메일 종료일
	
	private BigDecimal expireCarrot;	// 만료 당근
	
}