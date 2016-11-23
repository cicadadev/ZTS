package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplaycategory;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsDisplaycategoryproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String displayCategoryId; //전시카테고리ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private DmsDisplaycategory dmsDisplaycategory;
	private PmsProduct pmsProduct;
}