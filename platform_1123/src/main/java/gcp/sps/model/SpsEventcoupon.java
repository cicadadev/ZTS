package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsEventcoupon;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEventcoupon extends BaseSpsEventcoupon {
	private String expirationDate;
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}