package gcp.sps.model;

import java.math.BigDecimal;

import gcp.sps.model.base.BaseSpsDeal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsDeal extends BaseSpsDeal {
	
	private String restrictYn;
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	private String endDt;		// 상품기준 deal종료일

	private BigDecimal listPrice;
	private BigDecimal salePrice;
	private BigDecimal addSalePrice;
	private BigDecimal totalSalePrice;	//딜적용판매가.
	private BigDecimal saleRate;// 할인율
	private String childrenCardName;
}
