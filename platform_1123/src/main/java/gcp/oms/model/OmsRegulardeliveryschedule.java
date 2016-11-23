package gcp.oms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

import gcp.oms.model.base.BaseOmsRegulardeliveryschedule;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsRegulardeliveryschedule extends BaseOmsRegulardeliveryschedule {
	private String scheduleOrderDt; 	//다음 결제예정일	
	
	private String cancelAll;
	
	private BigDecimal increaseDay; // 배송일 증가
}