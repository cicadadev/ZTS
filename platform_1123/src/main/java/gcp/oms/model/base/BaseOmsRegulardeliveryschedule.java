package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsRegulardeliveryproduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsRegulardeliveryschedule extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal deliveryProductNo; //정기배송상품일련번호		[primary key, primary key, primary key, not null]
	private String regularDeliveryId; //정기배송신청ID		[primary key, primary key, primary key, not null]
	private BigDecimal regularDeliveryOrder; //정기배송회차		[primary key, primary key, primary key, not null]
	private String regularDeliveryDt; //정기배송일자		[not null]
	private String deliveryScheduleStateCd; //정기배송스케줄상태코드		[not null]
	private String shipDt; //결제안내일시		[null]
	private String deliveryDt; //주문완료일시		[null]

	private OmsRegulardeliveryproduct omsRegulardeliveryproduct;

	public String getDeliveryScheduleStateName(){
			return CodeUtil.getCodeName("DELIVERY_SCHEDULE_STATE_CD", getDeliveryScheduleStateCd());
	}
}