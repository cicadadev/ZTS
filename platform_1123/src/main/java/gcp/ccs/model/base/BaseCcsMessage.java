package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsMessagelang;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsMessage extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String msgCd; //메시지코드		[primary key, primary key, primary key, not null]
	private String msgTypeCd; //메시지유형코드		[not null]
	private String msg; //메시지		[not null]

	private List<CcsMessagelang> ccsMessagelangs;

	public String getMsgName(){
			return CodeUtil.getCodeName("MSG_CD", getMsgCd());
	}

	public String getMsgTypeName(){
			return CodeUtil.getCodeName("MSG_TYPE_CD", getMsgTypeCd());
	}
}