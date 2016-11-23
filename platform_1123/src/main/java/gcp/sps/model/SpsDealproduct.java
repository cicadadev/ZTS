package gcp.sps.model;

import java.util.List;

import gcp.sps.model.base.BaseSpsDealproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsDealproduct extends BaseSpsDealproduct {
	private String divTitDepth;
	private String productName;
	private String gradeBenefitManager;
	private String saleProductAddPriceManager;
	private String grossProfitAmount;
	private String saleProductTotalStock;
	private String dealPriceRate;
	private String dealPrice;
	private String normalPrice;
	private String badgeGubun;

	private List<SpsDealmember> spsDealmembers;
	private List<SpsDealsaleproductprice> spsDealsaleproductprices;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
//	private String newYn;
	private String presentYn;
	private String couponYn;
	private String pointYn;
	
	private String isCount;
	private String isDeadline;
	
	private String stockMinus;
	private String result;
	private String msg;
	
	private String couponId;
	private String presentProductId;
	
	private List<SpsCoupon> spsCoupons;

}