package gcp.ccs.model;


import java.util.List;

import gcp.dms.model.DmsDisplaycategory;
import gcp.pms.model.PmsBrand;
import intune.gsf.model.ExcelUploadResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsExcelUploadResult extends ExcelUploadResult {
	private List			resultList;
	private List			errorList;
	private PmsBrand			pmsBrand;
	private DmsDisplaycategory	dmsDisplaycategory;
}
