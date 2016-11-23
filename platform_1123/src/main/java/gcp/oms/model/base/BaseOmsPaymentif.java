package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPaymentif extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal paymentNo; //결제번호		[primary key, primary key, primary key, not null]
	private BigDecimal paymentIfNo; //결제연동번호		[primary key, primary key, primary key, not null]
	private String paymentReqData; //결제연동요청전문		[null]
	private String paymentReturnData; //결제연동수신전문		[null]
	private String paymentIfResult; //결제연동결과		[null]

}