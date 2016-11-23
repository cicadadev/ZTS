package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusinessinquirycategory;
import gcp.ccs.model.CcsCommission;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsCategoryattribute;
import gcp.pms.model.PmsCategorylang;
import gcp.pms.model.PmsCategoryrating;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSizechart;
import gcp.ccs.model.CcsStore;
import gcp.ccs.model.CcsUser;
import gcp.pms.model.PmsCategory;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsCategory extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[primary key, primary key, primary key, not null]
	private String upperCategoryId; //상위표준카테고리ID		[null]
	private String name; //표준카테고리명		[not null]
	private String leafYn; //리프여부		[not null]
	private String secondApprovalYn; //2차승인여부		[null]
	private String newIconYn; //NEW아이콘여부		[not null]
	private String erpProductId; //ERP대표상품ID		[null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private String userId; //담당MDID		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<CcsBusinessinquirycategory> ccsBusinessinquirycategorys;
	private List<CcsCommission> ccsCommissions;
	private List<PmsCategory> pmsCategorys;
	private List<PmsCategoryattribute> pmsCategoryattributes;
	private List<PmsCategorylang> pmsCategorylangs;
	private List<PmsCategoryrating> pmsCategoryratings;
	private List<PmsProduct> pmsProducts;
	private List<PmsSizechart> pmsSizecharts;
	private CcsStore ccsStore;
	private CcsUser ccsUser;
	private PmsCategory pmsCategory;
}