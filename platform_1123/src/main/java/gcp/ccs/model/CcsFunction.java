package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsFunction;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsFunction extends BaseCcsFunction {
	private String url;
}