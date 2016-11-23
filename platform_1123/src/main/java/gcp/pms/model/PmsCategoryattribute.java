package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsCategoryattribute;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsCategoryattribute extends BasePmsCategoryattribute {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attributeName;
	private String langCd;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
}