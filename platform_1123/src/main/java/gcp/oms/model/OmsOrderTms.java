package gcp.oms.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gcp.external.model.TmsQueue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsOrderTms extends TmsQueue {

	//기본
	private String type;
	private String orderName;	//주문자명
	private String deliveryName;	//받는사람.
	private String phone1;	//전화번호
	private String phone2;	//핸드폰번호
	private String productName;		//상품명
	private String orderId;		//주문,픽업,정기배송 ID
	private String paymentAmt;
	private String addPhone;	//매장매니저번호, 기프트콘 대상자번호
	private String	orderDt;
	
	//pickup
	private String offshopName;
	private String pickupDate;
	
	//virtual
	private String bankName;
	private String accountNo;
	private String endDt;
	
	//regular
	private String regularDeliveryDt;
	private String firstDate;
	private String deliveryCnt;
	private String periodCd;
	private String periodValue;	
	private String address;
	private String note;
	private String mapContent;
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
