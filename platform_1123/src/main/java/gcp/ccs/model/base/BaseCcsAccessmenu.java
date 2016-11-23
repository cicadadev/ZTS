package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsAccessfunction;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsRole;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsAccessmenu extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String roleId; //역할ID		[primary key, primary key, primary key, not null]
	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]

	private List<CcsAccessfunction> ccsAccessfunctions;
	private CcsMenu ccsMenu;
	private CcsRole ccsRole;
}