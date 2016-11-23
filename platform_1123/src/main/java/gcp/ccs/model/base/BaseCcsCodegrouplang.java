package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsCodegroup;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsCodegrouplang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String cdGroupCd; //코드그룹코드		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //코드그룹명		[not null]
	private String note; //설명		[null]

	private CcsCodegroup ccsCodegroup;

	public String getCdGroupName(){
			return CodeUtil.getCodeName("CD_GROUP_CD", getCdGroupCd());
	}

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}