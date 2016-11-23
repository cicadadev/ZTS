package gcp.mms.model;

import gcp.mms.model.base.BaseMmsMember;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsMember extends BaseMmsMember {
	
	private String issueMem;
	private int rnum;
	private String name;
	private String phone;
	private String employeeYn;
	private MmsMemberZts mmsMemberZts;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	
	private MmsCarrot mmsCarrot;

}