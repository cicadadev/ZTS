package gcp.oms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsPickupSearch extends BaseSearchCondition {
	private static final long serialVersionUID = 1460283023569595626L;

	// private String orderIds; // 주문번호
	private String pickupProductStateCds; // 픽업상태(복수)
	private String pickupProductStateCd; // 픽업상태(단수)

	private String ordererType; // 신청자
	private String orderer;
	private String offShopType; // 픽업매장
	private String offShop;

	private String pickupId;
	private String productId; // 상품번호
}
