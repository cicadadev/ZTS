package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class PmsAttributeSearch extends BaseSearchCondition {
	private String storeId; //primary key, not null
	private String attributeId; //primary key, not null
	private String name; //not null
	private String attributeTypeCd; //not null
	private String categoryId;
	private String attributeTypeCds;
}
