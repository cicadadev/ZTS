package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsOffshopSearch extends BaseSearchCondition {
	private String offshopStateCds;
	private String offshopTypeCd;
	private String offshopTypeCds;
	private String offName;
	private String offshopId;
	private String offshopBrand;
	private String offshopArea1;
	private String offshopArea2;
	private String latitude;
	private String longitude;
	private String defaultYn;
	private String searchKeyword;
	private String memberNo;
	private String offshopAffiliation;
	private String isMobile;
	private String saleproductId;
	private int cntLimit;
	private String brandId;
	private String pickupYn;

}
