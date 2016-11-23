package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsMessage;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsMessagelang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String msgCd; //메시지코드		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String msg; //메시지		[not null]

	private CcsMessage ccsMessage;

	public String getMsgName(){
			return CodeUtil.getCodeName("MSG_CD", getMsgCd());
	}

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}