package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsCatalogproduct;
import gcp.dms.model.DmsCatalog;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsCatalogimg extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String catalogId; //카탈로그ID		[primary key, primary key, primary key, not null]
	private BigDecimal catalogImgNo; //카탈로그이미지번호		[primary key, primary key, primary key, not null]
	private String name; //이미지명		[not null]
	private String img1; //이미지1		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]
	private String img2; //이미지2		[null]

	private List<DmsCatalogproduct> dmsCatalogproducts;
	private DmsCatalog dmsCatalog;
}