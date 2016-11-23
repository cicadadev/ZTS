package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsCode;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsCode extends BaseCcsCode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String insName;	  	// 등록자
	private String updName;		// 수정자
}