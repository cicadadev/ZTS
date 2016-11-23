package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsPricereserve;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsPricereserve extends BasePmsPricereserve {
	
	private String itemHistCd;
	
	// 단품 가격 변경 요청 여부
	private String salePriceReserveYn;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
}