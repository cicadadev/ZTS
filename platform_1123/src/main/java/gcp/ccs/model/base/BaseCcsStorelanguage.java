package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsStore;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsStorelanguage extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]

	private CcsStore ccsStore;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}