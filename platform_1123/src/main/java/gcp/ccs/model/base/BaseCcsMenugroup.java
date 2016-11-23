package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsMenugrouplang;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsMenugroup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String menuGroupId; //메뉴그룹ID		[primary key, primary key, primary key, not null]
	private String name; //명칭		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<CcsMenu> ccsMenus;
	private List<CcsMenugrouplang> ccsMenugrouplangs;
}