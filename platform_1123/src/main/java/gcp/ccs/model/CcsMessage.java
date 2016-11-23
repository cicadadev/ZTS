package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsMessage;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsMessage extends BaseCcsMessage {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -2568980501661849240L;
	private String id;
	private String no;
	private Object returnObject;
}