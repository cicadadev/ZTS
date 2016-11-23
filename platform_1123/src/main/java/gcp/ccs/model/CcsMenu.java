package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsMenu;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsMenu extends BaseCcsMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String originMenuId;
	
	private String insName;	  	// 등록자
	private String updName;		// 수정자
}