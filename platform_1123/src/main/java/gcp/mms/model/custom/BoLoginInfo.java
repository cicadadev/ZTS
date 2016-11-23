package gcp.mms.model.custom;

import java.util.List;

import gcp.ccs.model.CcsMenugroup;
import intune.gsf.model.BaseLoginInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class BoLoginInfo extends BaseLoginInfo{
	private String roleId;
	private String businessId;
	private String mdYn;		// md여부
	private String systemType;
	private List<CcsMenugroup> menuList;
	
	
}
