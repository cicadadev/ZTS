package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsDisplaycategorylang;
import gcp.dms.model.DmsDisplaycategoryproduct;
import gcp.dms.model.DmsExhibitdisplaycategory;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsTemplate;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsDisplaycategory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String displayCategoryId; //전시카테고리ID		[primary key, primary key, primary key, not null]
	private String upperDisplayCategoryId; //상위전시카테고리ID		[null]
	private String name; //전시카테고리명		[not null]
	private String leafYn; //리프여부		[not null]
	private String templateId; //템플릿ID		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]

	private List<DmsDisplaycategory> dmsDisplaycategorys;
	private List<DmsDisplaycategorylang> dmsDisplaycategorylangs;
	private List<DmsDisplaycategoryproduct> dmsDisplaycategoryproducts;
	private List<DmsExhibitdisplaycategory> dmsExhibitdisplaycategorys;
	private DmsDisplaycategory dmsDisplaycategory;
	private DmsTemplate dmsTemplate;
}