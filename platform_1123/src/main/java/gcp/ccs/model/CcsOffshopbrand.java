package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsOffshopbrand;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsOffshopbrand extends BaseCcsOffshopbrand {
	
	private String name;
	private String brandName;
	
}