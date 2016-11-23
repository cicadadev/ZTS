package gcp.pms.model;

import gcp.common.util.CodeUtil;
import gcp.pms.model.base.BasePmsEpexcproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsEpexcproduct extends BasePmsEpexcproduct {

	private String	businessInfo;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
}