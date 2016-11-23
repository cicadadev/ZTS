package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class PmsEpexcproductSearch extends BaseSearchCondition {

	private String	brandId;
	private String	businessId;
	private String	excProductTypeCd;
	private String	productName;

	private String	searchId;
	private String	infoType;

	private String	saleStateCds;
	private String	productTypeCds;
	
	private String erpProductId;

}
