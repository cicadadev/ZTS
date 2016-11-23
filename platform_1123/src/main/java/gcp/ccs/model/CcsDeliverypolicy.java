package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsDeliverypolicy;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsDeliverypolicy extends BaseCcsDeliverypolicy {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String address;
	
	private String insName;	  	// 등록자
	private String updName;		// 수정자
}