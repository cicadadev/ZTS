package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsDispCategoryBulk {

	public PmsDispCategoryBulk(String[] fields, String productId) {
		super();
		this.displayCategoryId = fields[0];
		this.displayYn = fields[1];
		this.sortNo = new BigDecimal(fields[2]);
		this.productId = productId;
	}

	@NotEmpty(groups = { ValidationUtil.Insert.class, ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "전시카테고리번호", target = "gcp.dms.model.DmsDisplaycategoryproduct")
	private String displayCategoryId; // 전시카테고리ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Excel(name = "상품번호", target = "gcp.dms.model.DmsDisplaycategoryproduct")
	private String productId; // 상품ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class, ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Excel(name = "전시여부", target = "gcp.dms.model.DmsDisplaycategoryproduct")
	private String displayYn; // 전시여부 [not null]

	@Excel(name = "정렬순서", target = "gcp.dms.model.DmsDisplaycategoryproduct")
	private BigDecimal sortNo; // 정렬순서 [null]

}
