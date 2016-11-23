package gcp.sps.model;

import gcp.sps.model.base.BaseSpsEventjoin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEventjoin extends BaseSpsEventjoin {
	private String memName;
	private String phone1;
	private String phone2;
	private String winHist;	//이력보기
	private String joinHist; //정보보기
	private String orderId;
	private String insName;
	private String updName;
	private String giftName;
	private String memAddress;
	
	// FRONT
	private String gubun;
	private String runEventCount;
	private String stopEventCount;

}