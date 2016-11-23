package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsDealgroup;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealgroup;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsDealgroup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String dealId; //딜ID		[primary key, primary key, primary key, not null]
	private BigDecimal dealGroupNo; //상품그룹번호		[primary key, primary key, primary key, not null]
	private BigDecimal upperDealGroupNo; //상위상품그룹번호		[null]
	private String name; //그룹명		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]
	private String img; //딜그룹이미지		[null]

	private List<SpsDealgroup> spsDealgroups;
	private List<SpsDealproduct> spsDealproducts;
	private SpsDeal spsDeal;
	private SpsDealgroup spsDealgroup;
}