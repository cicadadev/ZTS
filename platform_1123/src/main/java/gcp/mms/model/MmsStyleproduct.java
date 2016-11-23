package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsStyleproduct;
import gcp.pms.model.PmsStyleproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsStyleproduct extends BaseMmsStyleproduct {
	private PmsStyleproduct pmsStyleproduct;
	private String themeName;
}