package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodegrouplang;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsCodegroup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String cdGroupCd; //코드그룹코드		[primary key, primary key, primary key, not null]
	private String name; //코드그룹명		[not null]
	private String cdGroupTypeCd; //코드그룹유형코드		[not null]
	private String note; //설명		[null]

	private List<CcsCode> ccsCodes;
	private List<CcsCodegrouplang> ccsCodegrouplangs;

	public String getCdGroupName(){
			return CodeUtil.getCodeName("CD_GROUP_CD", getCdGroupCd());
	}

	public String getCdGroupTypeName(){
			return CodeUtil.getCodeName("CD_GROUP_TYPE_CD", getCdGroupTypeCd());
	}
}