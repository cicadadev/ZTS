package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsDisplaycategoryproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsDisplaycategoryproduct extends BaseDmsDisplaycategoryproduct {
	private String depthFullName;
	private String twoDepthCategoryId;
}