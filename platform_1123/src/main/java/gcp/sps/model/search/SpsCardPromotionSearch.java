package gcp.sps.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsCardPromotionSearch extends BaseSearchCondition {

	private String cardPromotionStateCd;
	private String cardPromotionStateCdCds;
	private String name;
	private String deviceTypeCd;

}
