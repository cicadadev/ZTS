package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsUserSearch extends BaseSearchCondition {
	private String userId;	
	private String name;
	private String userIds;
	private String userStateCds;
	private String userTypeCds;
	private String businessId;
	private String roleId;
	private String roleName;
	private String searchType;
	private String mdYnCds;
	private String phone;
	private String regNo;
	
	//비밀번호 초기화용
	private String systemType;
	private String myPwd;
	
}
