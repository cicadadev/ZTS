package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsStyleproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsStyleproduct extends BasePmsStyleproduct {
	
	private String brandId;
	private String brandName;
	private String productName;
	private int popularCnt;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	private String displayCategoryId;
	
}