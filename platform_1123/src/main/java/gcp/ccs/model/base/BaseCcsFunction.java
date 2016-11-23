package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsAccessfunction;
import gcp.ccs.model.CcsFunctionlang;
import gcp.ccs.model.CcsFunctionurl;
import gcp.ccs.model.CcsMenu;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsFunction extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]
	private String functionId; //기능ID		[primary key, primary key, primary key, not null]
	private String groupName; //기능그룹명		[not null]
	private String name; //기능명		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<CcsAccessfunction> ccsAccessfunctions;
	private List<CcsFunctionlang> ccsFunctionlangs;
	private List<CcsFunctionurl> ccsFunctionurls;
	private CcsMenu ccsMenu;
}