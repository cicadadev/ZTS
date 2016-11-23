package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsProductimg;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsProductimg extends BasePmsProductimg {
	
	private String changeMode;
	private String orgPath;
	private String deleteImg;
}