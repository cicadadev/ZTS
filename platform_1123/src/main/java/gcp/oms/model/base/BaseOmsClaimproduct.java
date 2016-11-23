package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsOrderproduct;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsClaimproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal claimNo; //클레임번호		[primary key, primary key, primary key, not null]
	private BigDecimal orderProductNo; //주문상품일련번호		[primary key, primary key, primary key, not null]
	private String claimReasonCd; //클레임사유코드		[null]
	private String claimProductStateCd; //클레임상품상태코드		[not null]
	private BigDecimal claimQty; //수량		[not null]
	private String returnOrderDt; //입고지시일시		[null]
	private String returnDt; //입고완료일시		[null]
	private String claimReason; //클레임사유		[null]

	private List<OmsLogistics> omsLogisticss;
	private OmsClaim omsClaim;
	private OmsOrderproduct omsOrderproduct;

	public String getClaimReasonName(){
			return CodeUtil.getCodeName("CLAIM_REASON_CD", getClaimReasonCd());
	}

	public String getClaimProductStateName(){
			return CodeUtil.getCodeName("CLAIM_PRODUCT_STATE_CD", getClaimProductStateCd());
	}
}