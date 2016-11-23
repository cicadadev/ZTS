package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsFaqSearch extends BaseSearchCondition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String	faqNos;
	private String	useYn;
	private String	faqTypeCd;
	private String	infoType;
	private String	info;
	private String	displayYnCds;
	
	private String 	insInfoId;
	private String 	insInfoName;
	
	private String 	isScroll;
}
