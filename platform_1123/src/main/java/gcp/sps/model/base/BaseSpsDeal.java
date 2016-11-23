package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import gcp.oms.model.OmsCart;
import gcp.sps.model.SpsCoupondeal;
import gcp.sps.model.SpsDealgroup;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsPresentdeal;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDeal extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String dealId; //딜ID		[primary key, primary key, primary key, not null]
	private String name; //딜명		[not null]
	private String dealTypeCd; //딜유형코드		[not null]
	private String childrencardTypeCd; //다자녀카드유형코드		[null]
	private BigDecimal controlNo; //노출제어번호		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]

	private List<MmsMemberZts> mmsMemberZtss;
	private List<OmsCart> omsCarts;
	private List<SpsCoupondeal> spsCoupondeals;
	private List<SpsDealgroup> spsDealgroups;
	private List<SpsDealproduct> spsDealproducts;
	private List<SpsPresentdeal> spsPresentdeals;
	private CcsControl ccsControl;

	public String getDealTypeName(){
			return CodeUtil.getCodeName("DEAL_TYPE_CD", getDealTypeCd());
	}

	public String getChildrencardTypeName(){
			return CodeUtil.getCodeName("CHILDRENCARD_TYPE_CD", getChildrencardTypeCd());
	}
}