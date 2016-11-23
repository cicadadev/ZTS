package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsDeliverytracking extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal logisticsInoutNo; //주문상품입출고일련번호		[primary key, primary key, primary key, not null]
	private String invoiceNo; //송장번호		[primary key, primary key, primary key, not null]
	private String deliveryStepCd; //배송단계코드		[primary key, primary key, primary key, not null]
	private String deliveryServiceTime; //택배사처리시간		[null]
	private String trackerRegTime; //트래커등록시간		[not null]
	private String deliveryLocation; //택배위치		[null]
	private String officePhoneNo; //사업소기반전화번호		[null]
	private String deliverymanMobileNo; //배송기사전화번호		[null]
	private String deliveryDetail; //배송상세정보		[null]
	private String receiverAddress; //수취인주소		[null]
	private String receiverName; //수취인이름		[null]
	private String senderName; //발신인이름		[null]


	public String getDeliveryStepName(){
			return CodeUtil.getCodeName("DELIVERY_STEP_CD", getDeliveryStepCd());
	}
}