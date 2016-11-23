package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsProduct;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductattribute extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String attributeId; //속성ID		[primary key, primary key, primary key, not null]
	private String attributeValue; //속성값||복수		[not null]

	private PmsAttribute pmsAttribute;
	private PmsProduct pmsProduct;
}