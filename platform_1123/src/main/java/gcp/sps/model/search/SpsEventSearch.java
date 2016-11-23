package gcp.sps.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsEventSearch extends BaseSearchCondition {

	private String eventId;
	private String eventIds;
	private String name;
	private String eventTypeCd;
	private String eventStateCd;
	private BigDecimal memberNo;
	
	private String eventTypeCds;
	private String eventDivCds;
	private String eventStateCds;
	
	private String productId;
	private String searchKeyword;
	
	// FO
	private String memberId;
	private String brandId;
	private String controlDeviceTypeCd;

}
