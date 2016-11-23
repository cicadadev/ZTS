package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsEventprize;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEventprize extends BaseSpsEventprize {
	private String couponName;
	private String joinDate;
}