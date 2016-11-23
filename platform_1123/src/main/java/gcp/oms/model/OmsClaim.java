package gcp.oms.model;

import gcp.oms.model.base.BaseOmsClaim;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsClaim extends BaseOmsClaim {

	private static final long serialVersionUID = 3626724920340499604L;

	private OmsClaimdelivery omsClaimdelivery;
	
	private String returnStatus;

	private boolean cancelAll = false;
}