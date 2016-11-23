package gcp.pms.model.custom;

import java.math.BigDecimal;

import gcp.common.util.CodeUtil;
import lombok.Data;

@Data
public class PmsOptionvalue {

	private String	optionValue;
	private String optionDispValue;
	private String	optionName;
	private String	saleproductStateCd;
	private String realStockQty;
	private BigDecimal addSalePrice;
	private String saleProductId;

	
	private String selected;
	
	public String getSaleproductStateName(){
		return CodeUtil.getCodeName("SALEPRODUCT_STATE_CD", getSaleproductStateCd());
}
}
