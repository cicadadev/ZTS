package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsExhibitgroup;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibitproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private BigDecimal groupNo; //상품그룹번호		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private DmsExhibitgroup dmsExhibitgroup;
	private PmsProduct pmsProduct;
}