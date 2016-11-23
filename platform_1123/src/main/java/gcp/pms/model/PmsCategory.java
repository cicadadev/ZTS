package gcp.pms.model;

import gcp.pms.model.base.BasePmsCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsCategory extends BasePmsCategory {
	
	private String depth;
	private String depthFullName;
	private String strCommissionRate;
	private String lastNodeYn;
}