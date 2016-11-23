package gcp.oms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsTermorderSearch extends BaseSearchCondition {
	//사이트
	private String	siteId;
	//처리여부
	private String	processYn;
	//사이트유형코드
	private String siteTypeCd;
}
