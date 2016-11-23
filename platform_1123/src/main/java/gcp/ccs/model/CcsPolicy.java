package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsPolicy;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsPolicy extends BaseCcsPolicy {
	private String storeId;
}