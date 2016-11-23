package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsAccessfunction;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsAccessfunction extends BaseCcsAccessfunction {
	private String functionRoleYn;
	private String useYn;
}