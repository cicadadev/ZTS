package gcp.oms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class LocationMappingBulk {
	
	@NotEmpty(groups = { ValidationUtil.Update.class })
	@Size(min = 0, max = 20)
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsSaleproduct")
	private String productId; // 상점ID [not null]
	
	@Size(min = 0, max = 20)
	@Excel(name = "단품번호", target = "gcp.pms.model.PmsSaleproduct")
	private String saleproductId; // 단품ID [primary key]
	
	@NotEmpty(groups = { ValidationUtil.Update.class })
	@Size(min = 0, max = 20)
	@Excel(name = "로케이션ID", target = "gcp.pms.model.PmsSaleproduct")
	private String locationId; // 단품ID [primary key, not null]
	
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "합배송수량", target = "gcp.pms.model.PmsSaleproduct")
	private BigDecimal deliveryTogetherQty; 

}
