package gcp.mms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MmsMemberPopupSearch extends BaseSearchCondition {

	private String		memberId;
	private String		memberIds;
	
	private String		memId;
	private String		infoType;
	private String		phoneType;
	private String		searchPhoneKeyword;

	private BigDecimal	memberNo;
	private String		memberNos;
	private String		name;
	private String		memStateCds;
	private String		memGradeCds;
	private String		memberTypeCds;
	private String		memberType;
	private String		cellNo;

	private String		carrotTypeCds;
	private String		note;

	private String		generalYn;
	private String		membershipYn;
	private String		premiumYn;
	private String		employeeYn;
	private String		childrenYn;
	private String		b2eYn;

	private String		blacklistYn;
	private String		blacklistY;
	private String		blacklistN;

	private String		depositTypeCd;
	private String		depositTypeCds;

	private String		orderId;
	private BigDecimal	claimNo;
}
