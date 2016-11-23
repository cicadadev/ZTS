package gcp.oms.model;

import gcp.oms.model.base.BaseOmsPickup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPickup extends BaseOmsPickup {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -4656618239706154066L;

	private String storeId;
	private String cartProductNos;	//선택된 장바구니상품 번호	
}