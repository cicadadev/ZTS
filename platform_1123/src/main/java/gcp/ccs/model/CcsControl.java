package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsControl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsControl extends BaseCcsControl {
	
	private String memberTypes;
	private String memGrades;
	private String deviceTypes;
	
	private String[] memberTypeArr;
	private String[] memGradeArr;
	private String[] deviceTypeArr;
	private String[] channelIdArr;
	
}