package gcp.sps.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsCouponIssueSearch extends BaseSearchCondition {

	private String		couponId;
	private String		couponIssueStateCds;
	private String		privateCin;
	private String		memberNos;
	private String		memGradeCds;
	private String		infoType;
	private String		searchKeyword;

	/* FO */
	private BigDecimal	memberNo;
	private String		couponTypeCd;
	private String		termType;
	private String		usePlace;

}
