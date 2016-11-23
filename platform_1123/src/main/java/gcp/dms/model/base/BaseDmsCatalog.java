package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsCatalogimg;
import gcp.ccs.model.CcsStore;
import gcp.pms.model.PmsBrand;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsCatalog extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String catalogId; //카탈로그ID		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[null]
	private String name; //카탈로그명		[not null]
	private String catalogTypeCd; //카탈로그유형코드		[not null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String seasonCd; //시즌코드		[not null]

	private List<DmsCatalogimg> dmsCatalogimgs;
	private CcsStore ccsStore;
	private PmsBrand pmsBrand;

	public String getCatalogTypeName(){
			return CodeUtil.getCodeName("CATALOG_TYPE_CD", getCatalogTypeCd());
	}

	public String getSeasonName(){
			return CodeUtil.getCodeName("SEASON_CD", getSeasonCd());
	}
}