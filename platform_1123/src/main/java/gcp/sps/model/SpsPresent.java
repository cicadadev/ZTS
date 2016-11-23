package gcp.sps.model;

import java.util.List;

import gcp.ccs.model.CcsControl;
import gcp.oms.model.OmsOrderproduct;
import gcp.sps.model.base.BaseSpsPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsPresent extends BaseSpsPresent {
	private String			targetTypeCd;
	private String			orgTargetTypeCd;
	private String			controlType;
	private String			dealTypeCds;

	private List<SpsDeal>	spsDeals;
	private CcsControl		ccsControl;

	private List<OmsOrderproduct> omsOrderproducts;		//사은품에 대상이되는 주문상품목록
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}