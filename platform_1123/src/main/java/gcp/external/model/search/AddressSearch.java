package gcp.external.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class AddressSearch extends BaseSearchCondition {

	private String	searchStr;
	private String	searchFlag;
	
	private String sidoName;
	private String sigunguName;
	private String pageNo;
	
	private String addrType;
	
}
