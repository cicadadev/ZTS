package gcp.oms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsPgSettleSearch extends BaseSearchCondition {
	//기간종류
	private String	periodType;
	//PG사
	private String	pgCompany;
	//PG 상점ID
	private String	pgShopId;
	//오차여부
	private String	errorYn;
	//결제수단
	private String	paymentMethodCds;
	//주문번호
	private String	orderId;
	//승인번호
	private String	approvalNo;
}
