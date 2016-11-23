package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsWarehouselocation;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsWarehouselocation extends BasePmsWarehouselocation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5863490881843156078L;
	
	private String warehouseName;
	
}