package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsApplytarget;
import gcp.dms.model.DmsDisplaycategory;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsApplytarget extends BaseCcsApplytarget {

	private String				saveType;			//U,I

	private PmsProduct			pmsProduct;
	private PmsBrand			pmsBrand;
//	private PmsCategory			pmsCategory;
	private DmsDisplaycategory	dmsDisplaycategory;

	private String				targetTypeCd;
	private String				type;		

	private String insName;		// 등록자
	private String updName;		// 수정자

}