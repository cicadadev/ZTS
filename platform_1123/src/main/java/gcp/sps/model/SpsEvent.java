package gcp.sps.model;

import gcp.sps.model.base.BaseSpsEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEvent extends BaseSpsEvent {
	private String unRestrictYn;
	private String couponExistYn;
	private String prizeExistYn;
	private String joinExistYn;
	private String productExistYn;
	private String giftExistYn;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	
	private String expBadge;
	private String joinNumber;

}