package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsExhibit;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibitmainproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String name; //대표상품명		[not null]
	private String img; //대표이미지		[null]
	private BigDecimal sortNo; //정렬순서		[null]

	private DmsExhibit dmsExhibit;
	private PmsProduct pmsProduct;
}