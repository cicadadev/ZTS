package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsBusinessholiday;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsBusinessholiday extends BaseCcsBusinessholiday {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String insName;	  	// 등록자
	private String updName;		// 수정자
}