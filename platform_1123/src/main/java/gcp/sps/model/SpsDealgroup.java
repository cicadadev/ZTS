package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsDealgroup;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsDealgroup extends BaseSpsDealgroup {
	private String depth;
	private String leafYn;
	private String upperGroupName;
	private String crudType;
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}