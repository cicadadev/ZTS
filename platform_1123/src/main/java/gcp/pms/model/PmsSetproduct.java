package gcp.pms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.pms.model.base.BasePmsSetproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsSetproduct extends BasePmsSetproduct {
	// 판매가
	private BigDecimal salePrice;
	// 단품ID
	private String saleproductId;
	// 단품명
	private String saleproductName;
	// 정상가 * 수량
	private BigDecimal sumListPrice;
	// 추가판매가
	private BigDecimal addSalePrice;

	// 구성상품의 단품 목록
	private List<PmsSaleproduct> pmsSaleproducts;
}