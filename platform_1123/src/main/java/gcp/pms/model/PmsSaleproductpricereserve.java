package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsSaleproductpricereserve;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsSaleproductpricereserve extends BasePmsSaleproductpricereserve {
	private String insName;		// 등록자
}