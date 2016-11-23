package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class CcsUserPopupSearch extends BaseSearchCondition {
	private String type;
	private String userStateCd;
	private String userTypeCd;

	private String searchUserId;
	private String searchName;
}
