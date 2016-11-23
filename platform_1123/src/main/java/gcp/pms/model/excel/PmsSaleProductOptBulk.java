package gcp.pms.model.excel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsSaleProductOptBulk {
	public PmsSaleProductOptBulk(String[] fields, String saleProductId) {
		super();
		this.optionName = fields[0];
		this.optionValue = fields[1];
		this.attributeId = fields[2];
		this.attributeValue = fields[3];
		this.saleproductId = saleProductId;
	}

	@NotEmpty(groups = { ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "단품번호", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String saleproductId; // 단품ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class, ValidationUtil.Update.class, ValidationUtil.Delete.class })
	@Excel(name = "옵션명", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String optionName; // 옵션명 [primary key, not null]

	@Excel(name = "옵션값", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String optionValue; // 옵션값 [null]

	@Excel(name = "속성번호", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String attributeId; // 매핑속성ID [null]

	@Excel(name = "속성값", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String attributeValue; // 매핑속성값 [null]

}
