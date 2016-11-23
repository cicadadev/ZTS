package gcp.oms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsRegularSearch extends BaseSearchCondition {

	private static final long serialVersionUID = -349964084749866042L;
	
	private String ordererType; // 신청자
	private String orderer;
	
	private String deliveryScheduleStateCd;	// 정기배송 스케쥴상태
	private String deliveryProductStateCd;	// 정기배송 상품상태
	
	private String productId; // 상품번호
	
	private String regularDeliveryId; // 정기배송신청ID
	private String deliveryProductNo; // 정기배송상품일련번호
}
