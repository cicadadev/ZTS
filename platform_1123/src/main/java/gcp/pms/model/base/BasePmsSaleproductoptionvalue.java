package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttributevalue;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsSaleproductoptionvalue extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String saleproductId; //단품ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal optionNo; //옵션번호		[primary key, primary key, primary key, not null]
	private String optionName; //옵션명		[null]
	private String optionValue; //옵션값		[null]
	private String attributeId; //매핑속성ID		[null]
	private String attributeValue; //매핑속성값		[null]

	private PmsAttributevalue pmsAttributevalue;
	private PmsSaleproduct pmsSaleproduct;
}