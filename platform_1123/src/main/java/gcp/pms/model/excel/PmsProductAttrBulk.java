package gcp.pms.model.excel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsProductAttrBulk {
	public PmsProductAttrBulk(String[] fields, String productId) {
		super();
		this.attributeId = fields[0];
		this.attributeValue = fields[1];
		this.productId = productId;
	}

	@NotEmpty(groups = { ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsProduct")
	private String productId; // 상품ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class, ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Excel(name = "속성번호", target = "gcp.pms.model.PmsProduct")
	private String attributeId; // 속성ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "속성값", target = "gcp.pms.model.PmsProduct")
	private String attributeValue; // 속성값||복수 [not null]

}
