package gcp.ccs.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.ccs.model.base.BaseCcsOffshop;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsOffshop extends BaseCcsOffshop {
	private String address;
	private String offshopBrands;
	private String distance;
	private String totalAmt;
	private BigDecimal realStockQty;
	
	private List<String> openDays;
	
	private List<CcsOffshop> ccsOffshops;
	
	// Front
	private String mainBrand;
	private String interestYn;
	private String topYn;
}