package gcp.pms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.base.BasePmsSaleproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsSaleproduct extends BasePmsSaleproduct {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5161884137055199202L;
	
	private String dealId;	//deal id
	private List<OmsOrderproduct> omsOrderproducts;
	private String businessName;  //공급업체
	
	private String insName;		// 등록자
	private String updName;		// 수정자

	private BigDecimal salePrice; // 판매가
	
	private String stockMinus;
	private String result;
	private String msg;
	
	private BigDecimal erpStockQty;		//ERP 재고수량
	private String optionValues;
	
	private BigDecimal commissionRate;
	private String productName;        // 상품명
	
	private BigDecimal minSalePrice;// 최저가
	private BigDecimal             maxShopStockQty;	// 단품의 매장별 최대 구매 가능 수량
	
	
}