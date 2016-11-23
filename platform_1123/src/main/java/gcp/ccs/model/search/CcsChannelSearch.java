package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsChannelSearch extends BaseSearchCondition {

	private String[] scChannelIds;
	private String channelIds;
	private String channelId;
	private String useYnCds;
	private String useY;
	private String useN;
	private String channelTypeCds;
	private String channelStateCds;
	private String startDt;
	private String endDt;
	private String name;
	
}
