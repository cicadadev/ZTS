package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsChannel;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsChannel extends BaseCcsChannel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gateUrl;
//	private String businessName;
	private String insName;	  	// 등록자
	private String updName;		// 수정자
}