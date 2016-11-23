package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsExhibitoffshop;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitoffshop extends BaseDmsExhibitoffshop {
	private String address;
	private String offshopBrands;
}