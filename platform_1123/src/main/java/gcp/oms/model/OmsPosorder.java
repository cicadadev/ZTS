package gcp.oms.model;

import gcp.ccs.model.CcsOffshop;
import gcp.oms.model.base.BaseOmsPosorder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPosorder extends BaseOmsPosorder {
	private String offshopName;
	private String brandName;
	private CcsOffshop ccsOffshop;
}