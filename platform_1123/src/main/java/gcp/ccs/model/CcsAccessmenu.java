package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsAccessmenu;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsAccessmenu extends BaseCcsAccessmenu {
	private String menuRoleYn;
}