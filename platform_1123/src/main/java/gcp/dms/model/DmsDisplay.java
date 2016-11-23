package gcp.dms.model;

import gcp.dms.model.base.BaseDmsDisplay;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsDisplay extends BaseDmsDisplay {
	
	private int depth;
	private String displayItemType;
	private String lastNodeYn;
	private int pageSize;	
}