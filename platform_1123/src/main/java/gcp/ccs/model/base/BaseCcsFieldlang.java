package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsField;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsFieldlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String fieldCd; //항목코드		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String msg; //메시지		[not null]

	private CcsField ccsField;

	public String getFieldName(){
			return CodeUtil.getCodeName("FIELD_CD", getFieldCd());
	}

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}