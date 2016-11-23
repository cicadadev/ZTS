package gcp.oms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsClaimSearch extends BaseSearchCondition {

	private static final long serialVersionUID = 2054913330397421196L;

	private String ordererType; // 신청자
	private String orderer;

	private String claimTypeCds; // 클레임유형코드
	private String claimStateCds; // 클레임상태코드
	private String claimProductStateCds; // 클레임상품상태코드

	private String orderIds; // 주문번호
	private String orderId; // 주문번호
	private String claimNo; // 클레임번호

	private String orderProductNos;
	private BigDecimal deliveryAddressNo;
	private BigDecimal deliveryPolicyNo;

}
