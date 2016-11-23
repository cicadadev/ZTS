package gcp.oms.model;

import java.util.List;


import gcp.oms.model.base.BaseOmsDeliveryaddress;
import gcp.sps.model.SpsCouponissue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsDeliveryaddress extends BaseOmsDeliveryaddress {
	
	private static final long serialVersionUID = 5723665343521402617L;

	private List<SpsCouponissue> spsCouponissues;
	
	private String deliveryAddress;
	private String hidZipCd;
	private String hidAddress1;
	private String hidAddress2;
	private String productId;
	private String saleProductId;
	private String rownumber;
}