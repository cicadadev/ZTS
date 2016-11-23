package gcp.mms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class MmsBlacklistSearch extends BaseSearchCondition {
	private String infoType;
	private String phoneType;
	private String searchPhoneKeyword;
	private String userid;
	private String email;
	private String name;
	private String tell;
	private String cellNo;
	
	private String memberNo ;
	private String memberNos ;
	
	private String startDt;
	private String endDt;

	private String memGrade;
	private String memGradeCds;
	
	private String memberType;
	private String memberTypeCds;

	private String memStateCd;
	private String memStateCds;
	
	private String blacklistType;
	private String blacklistTypeCds;

	private String blacklistNo;
	
	private String membershipYn;
	private String premiumYn;
	private String employeeYn;
	private String childrenYn;
	private String b2eYn;
	
}
