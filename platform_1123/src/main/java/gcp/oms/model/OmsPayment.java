package gcp.oms.model;

import java.math.BigDecimal;

import gcp.external.model.Kakao;
import gcp.mms.model.MmsDeposit;
import gcp.oms.model.base.BaseOmsPayment;
import kr.co.lgcns.module.lite.CnsPayWebConnector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPayment extends BaseOmsPayment {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -185422508865581030L;

	private String virtualInPeriod;
	private OmsPaymentif omsPaymentif;
	private CnsPayWebConnector connector;
	private Kakao kakao;

	private BigDecimal memberNo;
	private BigDecimal refundAmt; // 클레임에서 사용.
	private MmsDeposit mmsDeposit;
	private String continuePaymentMethod;	//지금 선택한 결제수단 다음에 사용.
	
	private String billingKey;	//정기배송 billingkey
	
	private String channelId;	
}