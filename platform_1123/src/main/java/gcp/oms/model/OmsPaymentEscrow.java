package gcp.oms.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPaymentEscrow {

	private String LGD_ESCROW_GOODID;
	private String LGD_ESCROW_GOODNAME;
	private String LGD_ESCROW_GOODCODE;
	private String LGD_ESCROW_UNITPRICE;
	private String LGD_ESCROW_QUANTITY;
	private String LGD_ESCROW_ZIPCODE;
	private String LGD_ESCROW_ADDRESS1;
	private String LGD_ESCROW_ADDRESS2;
	private String LGD_ESCROW_BUYERPHONE;
}
