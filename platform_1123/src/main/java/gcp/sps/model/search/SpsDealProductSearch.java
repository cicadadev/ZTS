package gcp.sps.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsDealProductSearch extends BaseSearchCondition {

	private String dealId;
	private String dealGroupNo;
	private String dealProductNo;
	private String productIds; 
	private String saleStateCds; 
	private String productName; 
	private String businessId; 
	private String brandId; 
	private String startDt; 
	private String endDt;
	private String productId;
	
	private String[] productIdArray;
	
	private String productInfoType;
	private String productSearchKeyword;
	private String sortType;
}
