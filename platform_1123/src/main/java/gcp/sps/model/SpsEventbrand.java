package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsEventbrand;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEventbrand extends BaseSpsEventbrand {
	private String brandName;
}