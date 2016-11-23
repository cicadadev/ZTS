package gcp.oms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.oms.model.base.BaseOmsOrdermemo;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsOrdermemo extends BaseOmsOrdermemo {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1586288888970780622L;

	private String insName;
}