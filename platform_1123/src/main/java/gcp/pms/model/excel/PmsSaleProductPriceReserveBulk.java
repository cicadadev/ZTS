package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsSaleProductPriceReserveBulk {

	public PmsSaleProductPriceReserveBulk(String[] fields, String productId) {
		super();
		this.saleproductId = fields[0];
		this.addSalePrice = new BigDecimal(fields[1]);
		this.productId = productId;
	}

	@NotEmpty
	@Size(min = 1, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsPricereserve")
	private String productId; // 상점ID [primary key, not null]

	@NotEmpty
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "단품번호", target = "gcp.pms.model.PmsSaleproductpricereserve")
	private String saleproductId; // 단품ID [primary key, not null]

	@NotNull
	@NumberFormat(style = Style.CURRENCY)
	@Excel(name = "추가판매가", target = "gcp.pms.model.PmsSaleproductpricereserve")
	private BigDecimal addSalePrice; // 추가판매가 [not null]
}
