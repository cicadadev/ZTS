package gcp.pms.model;

import gcp.pms.model.base.BasePmsAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsAttribute extends BasePmsAttribute {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int lastSortNo; //정렬순서		[null]
	
	private String insName;		// 등록자
	private String updName;		// 수정자
}