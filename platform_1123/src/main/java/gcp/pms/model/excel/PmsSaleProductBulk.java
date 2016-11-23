package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsSaleProductBulk {
	public PmsSaleProductBulk(String[] fields, String productId) {
		super();
		this.saleproductId = fields[0];
		this.name = fields[1];
		this.saleproductStateCd = fields[2];
		this.addSalePrice = new BigDecimal(fields[3]);
		this.safeStockQty = new BigDecimal(fields[4]);
		this.realStockQty = new BigDecimal(fields[5]);
		this.businessSaleproductId = fields[6];
		this.sortNo = new BigDecimal(fields[7]);
		this.productId = productId;
	}

	@NotEmpty(groups = { ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "단품번호", target = "gcp.pms.model.PmsSaleproduct")
	private String saleproductId; // 단품ID [primary key, not null]
	
	@NotEmpty(groups = { ValidationUtil.Update.class })
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsSaleproduct")
	private String productId; // 상점ID [not null]

	@Excel(name = "업체단품번호", target = "gcp.pms.model.PmsSaleproduct")
	private String businessSaleproductId; // 업체단품ID [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "단품명", target = "gcp.pms.model.PmsSaleproduct")
	private String name; // 단품명 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "추가판매가", target = "gcp.pms.model.PmsSaleproduct")
	private BigDecimal addSalePrice; // 추가판매가 [not null]

	@NotNull(groups = { ValidationUtil.Insert.class })
	@Digits(fraction = 0, integer = 3)
	@Excel(name = "안전재고수량", target = "gcp.pms.model.PmsSaleproduct")
	private BigDecimal safeStockQty; // 안전재고수량 [not null]

	@NotNull(groups = { ValidationUtil.Insert.class })
	@Digits(fraction = 0, integer = 3)
	@Excel(name = "실재고수량", target = "gcp.pms.model.PmsSaleproduct")
	private BigDecimal realStockQty; // 실재고수량 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "단품상태코드", target = "gcp.pms.model.PmsSaleproduct", codegroup = "SALEPRODUCT_STATE_CD")
	private String saleproductStateCd; // 단품상태코드 [not null]

	@Excel(name = "정렬순서", target = "gcp.pms.model.PmsSaleproduct")
	@Digits(fraction = 0, integer = 3)
	private BigDecimal sortNo; // 정렬순서 [null]
}
