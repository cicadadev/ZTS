package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsExhibitdisplaycategory;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitdisplaycategory extends BaseDmsExhibitdisplaycategory {
	String displayCategoryName;
}