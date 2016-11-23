package gcp.pms.model;

import gcp.pms.model.base.BasePmsBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsBrand extends BasePmsBrand {
	
	private String insName;
	private String updName;
	private String loginId;
	private String path;
	private int depth;
	private String imgPath;
	
	//검색API property
	private String brandName;
//	private String brandId;	TODO: BasePmsBrand에 brandId 존재함
	private String brandCount;
	private String corporateYn;
	
	//sales assist
	private String salesAssist;

}