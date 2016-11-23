package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsRole;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsRole extends BaseCcsRole {
	private String nextRoleId;
}