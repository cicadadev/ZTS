package gcp.oms.model;

import java.math.BigDecimal;

import gcp.ccs.model.CcsOffshop;
import gcp.oms.model.base.BaseOmsPickupproduct;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPickupproduct extends BaseOmsPickupproduct {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -1526179280959156603L;

	private String pickupMaxDt;
	private String pickupMinDt;
	private String offshopName;

	private BigDecimal cartProductNo;
	
	private String pickupReserveDy;
	private CcsOffshop ccsOffshop;	
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
	
	private String saleStateCd;
	private String saleproductStateCd;
	
	private BigDecimal orderProductNo;
	private String orderProductTypeCd;
	
	private String offshopPickupYn;
	
}