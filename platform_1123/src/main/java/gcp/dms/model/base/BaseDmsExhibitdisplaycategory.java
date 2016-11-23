package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibitdisplaycategory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private String displayCategoryId; //전시카테고리ID		[primary key, primary key, primary key, not null]

	private DmsDisplaycategory dmsDisplaycategory;
	private DmsExhibit dmsExhibit;
}