package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsAddress;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsAddress extends BaseMmsAddress {
	private String defaultAddrNo;
	private String basicYn;
}