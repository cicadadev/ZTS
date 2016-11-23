package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsPresent;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsPresentlang extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String presentId; //사은품ID		[primary key, primary key, primary key, not null]
	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String name; //사은품프로모션명		[not null]

	private SpsPresent spsPresent;

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}