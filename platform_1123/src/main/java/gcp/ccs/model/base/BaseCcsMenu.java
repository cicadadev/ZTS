package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsAccessmenu;
import gcp.ccs.model.CcsFunction;
import gcp.ccs.model.CcsMenulang;
import gcp.ccs.model.CcsMenugroup;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsMenu extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]
	private String menuGroupId; //메뉴그룹ID		[not null]
	private String systemTypeCd; //시스템유형코드		[not null]
	private String name; //메뉴명		[not null]
	private String url; //페이지URL		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<CcsAccessmenu> ccsAccessmenus;
	private List<CcsFunction> ccsFunctions;
	private List<CcsMenulang> ccsMenulangs;
	private CcsMenugroup ccsMenugroup;

	public String getSystemTypeName(){
			return CodeUtil.getCodeName("SYSTEM_TYPE_CD", getSystemTypeCd());
	}
}