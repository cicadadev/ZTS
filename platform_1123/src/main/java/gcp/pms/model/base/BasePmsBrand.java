package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsNoticeBrand;
import gcp.ccs.model.CcsOffshopbrand;
import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsExhibitbrand;
import gcp.pms.model.PmsBrandlang;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSizechart;
import gcp.sps.model.SpsEventbrand;
import gcp.dms.model.DmsTemplate;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsBrand extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String brandId; //브랜드ID		[primary key, primary key, primary key, not null]
	private String erpBrandId; //ERP브랜드ID		[null]
	private String name; //브랜드명		[not null]
	private String detail; //브랜드소개		[null]
	private String story; //브랜드스토리		[null]
	private String productInfo; //제품소개		[null]
	private String templateId; //템플릿ID		[null]
	private String logoImg; //로고이미지		[null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]

	private List<CcsInquiry> ccsInquirys;
	private List<CcsNoticeBrand> ccsNoticeBrands;
	private List<CcsOffshopbrand> ccsOffshopbrands;
	private List<DmsCatalog> dmsCatalogs;
	private List<DmsExhibitbrand> dmsExhibitbrands;
	private List<PmsBrandlang> pmsBrandlangs;
	private List<PmsProduct> pmsProducts;
	private List<PmsSizechart> pmsSizecharts;
	private List<SpsEventbrand> spsEventbrands;
	private DmsTemplate dmsTemplate;
}