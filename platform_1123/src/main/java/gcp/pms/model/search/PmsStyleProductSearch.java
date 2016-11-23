package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class PmsStyleProductSearch extends BaseSearchCondition {

	private String useYnCds;
	private String styleProductItemCd;
	private String brandId;
	
	private String productSearchKeyword;
	private String productInfoType;
	private String styleProductNo;
}
