package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsExhibitcoupon;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitcoupon extends BaseDmsExhibitcoupon {
	private String name;
	private String issueStartDt;
	private String issueEndDt;
	private String exceedYn;
}