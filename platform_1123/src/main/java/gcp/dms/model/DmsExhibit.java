package gcp.dms.model;

import java.util.List;

import gcp.ccs.model.CcsControl;
import gcp.dms.model.base.BaseDmsExhibit;
import gcp.mms.model.MmsMemberZts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibit extends BaseDmsExhibit {
	
	String[] brandIds;
	String displayCategoryId;
	String saveType;
	String restrictYn;
	String brandId;
	
	private List<DmsExhibitbrand> dmsExhibitbrands;
	private List<DmsExhibitcoupon> dmsExhibitcoupons;
	private List<DmsExhibitdisplaycategory> dmsExhibitdisplaycategorys;
	private List<DmsExhibitgroup> dmsExhibitgroups;
	private List<DmsExhibitlang> dmsExhibitlangs;
	private List<DmsExhibitmainproduct> dmsExhibitmainproducts;
	private List<MmsMemberZts> mmsMemberZtss;
	private CcsControl ccsControl;
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}