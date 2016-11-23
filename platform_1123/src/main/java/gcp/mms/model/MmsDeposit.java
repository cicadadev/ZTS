package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

import gcp.mms.model.base.BaseMmsDeposit;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsDeposit extends BaseMmsDeposit {

	private BigDecimal balanceAmt;
	private MmsMember mmsMember;
	private BigDecimal claimNo;
	private String memberInfo;

	private BigDecimal depositPlusAmt;
	private BigDecimal depositMinusAmt;

	private String insName; // 등록자
	private String updName; // 수정자

	/* FO */
	private BigDecimal plus;
	private BigDecimal minus;

}