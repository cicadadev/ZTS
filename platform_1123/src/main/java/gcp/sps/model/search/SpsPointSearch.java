package gcp.sps.model.search;

import gcp.oms.model.search.OmsOrderSearch;
import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsPointSearch extends BaseSearchCondition {

	private String	pointSaveStateCds;		//포인트상태
	private String	pointSaveIds;			//포인트 IDS
	private String	pointSaveId;				//포인트 ID
	private String	name;					//포인트명	

	private String	infoType;

	private String	productId;
	private String	categoryId;
	private String	brandId;

}
