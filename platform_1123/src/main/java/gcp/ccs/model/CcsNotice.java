package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsNotice extends BaseCcsNotice {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String period;
	private String insName;
	private String updName;
	
	private String[] ccsBrands;
	
	// FO
	
	private String eventName;  // 이벤트 이름
	private String brandName;  // 브렌드 이름
	private String newYn;	   // 신규 공지 여부
}