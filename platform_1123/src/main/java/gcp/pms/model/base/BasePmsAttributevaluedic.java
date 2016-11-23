package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsAttributevalue;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsAttributevaluedic extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String attributeId; //속성ID		[primary key, primary key, primary key, not null]
	private String dicAttributeValue; //사전속성값		[primary key, primary key, primary key, not null]
	private String attributeValue; //속성값		[null]

	private PmsAttribute pmsAttribute;
	private PmsAttributevalue pmsAttributevalue;
}