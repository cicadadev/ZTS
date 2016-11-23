package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class CcsBusinessSearch extends BaseSearchCondition {

	private String	businessTypeCd;
	private String	type;
	private String	businessStateCd;
	private String	businessId;
	private String	erpBusinessId;
	private String	businessTypeCds;
	private String	businessStateCds;
	private String	businessIds;
	private String  purchaseYn;
	private String 	saleTypeCds;
	private String  mdId;
	private String 	mdName;
	private String 	businessName;
	private String 	businessName2;
	private String categoryId;
	
	private String searchBusinessId;
	private String searchBusinessName;
}
