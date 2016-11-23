package gcp.oms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.common.util.CodeUtil;
import gcp.oms.model.base.BaseOmsClaimproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsClaimproduct extends BaseOmsClaimproduct {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = -7735177348914705689L;

	private String claimStateCd;
	private boolean cancelAll;

	public String getClaimStateName() {
		return CodeUtil.getCodeName("CLAIM_STATE_CD", this.getClaimStateCd());
	}
}