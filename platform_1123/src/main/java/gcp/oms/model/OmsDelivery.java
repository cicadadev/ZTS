package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.ccs.model.CcsDeliverypolicy;
import gcp.oms.model.base.BaseOmsDelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsDelivery extends BaseOmsDelivery {

	private static final long serialVersionUID = 979118359565396604L;

	private BigDecimal productSumAmt;
	private BigDecimal wrapVolume;
	private BigDecimal sumRefundWrapFee;
	private String deliveryFeeFreeYn;
	private List<OmsOrderproduct> omsOrderproducts;
	private BigDecimal totalDeliverySalePrice; // 배송지별 상품총판매금액.
	
	private CcsDeliverypolicy ccsDeliverypolicy;
}