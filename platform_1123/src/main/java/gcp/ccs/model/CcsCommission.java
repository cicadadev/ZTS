package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsCommission;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsCommission extends BaseCcsCommission {
	private	String	userId;
	private String strCommissionRate;
}