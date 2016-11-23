package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsLanguage extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String langCd; //언어코드		[primary key, primary key, primary key, not null]
	private String name; //명칭		[not null]
	private String englishName; //영문명칭		[not null]
	private String useYn; //사용여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]


	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}
}