package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsFaq;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsFaq extends BaseCcsFaq {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String insName;
	private String updName;
}