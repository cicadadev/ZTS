package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsExhibitbrand;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitbrand extends BaseDmsExhibitbrand {
	String brandName;
	String saveType;
}