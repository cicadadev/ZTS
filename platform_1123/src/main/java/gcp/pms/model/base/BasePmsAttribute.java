package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttributelang;
import gcp.pms.model.PmsAttributevalue;
import gcp.pms.model.PmsAttributevaluedic;
import gcp.pms.model.PmsCategoryattribute;
import gcp.pms.model.PmsProductattribute;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsAttribute extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String attributeId; //속성ID		[primary key, primary key, primary key, not null]
	private String attributeTypeCd; //속성유형코드		[not null]
	private String name; //속성명		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<PmsAttributelang> pmsAttributelangs;
	private List<PmsAttributevalue> pmsAttributevalues;
	private List<PmsAttributevaluedic> pmsAttributevaluedics;
	private List<PmsCategoryattribute> pmsCategoryattributes;
	private List<PmsProductattribute> pmsProductattributes;
	private CcsStore ccsStore;

	public String getAttributeTypeName(){
			return CodeUtil.getCodeName("ATTRIBUTE_TYPE_CD", getAttributeTypeCd());
	}
}