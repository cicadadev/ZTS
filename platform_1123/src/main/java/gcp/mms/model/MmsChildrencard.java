package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsChildrencard;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsChildrencard extends BaseMmsChildrencard {
	
	private String memberName;		// 등록자
	
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	private String beforeAccountNo; 

}