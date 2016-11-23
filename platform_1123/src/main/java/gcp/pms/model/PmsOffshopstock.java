package gcp.pms.model;

import gcp.pms.model.base.BasePmsOffshopstock;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsOffshopstock extends BasePmsOffshopstock {
	private String stockMinus;
	private String result;
	private String msg;
	private String name;
}