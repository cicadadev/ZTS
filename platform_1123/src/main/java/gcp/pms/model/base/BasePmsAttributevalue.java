package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttributevaluedic;
import gcp.pms.model.PmsSaleproductoptionvalue;
import gcp.pms.model.PmsAttribute;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsAttributevalue extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String attributeId; //속성ID		[primary key, primary key, primary key, not null]
	private String attributeValue; //속성값		[primary key, primary key, primary key, not null]
	private String addValue; //추가속성값||컬러칩코드등		[null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<PmsAttributevaluedic> pmsAttributevaluedics;
	private List<PmsSaleproductoptionvalue> pmsSaleproductoptionvalues;
	private PmsAttribute pmsAttribute;
}