package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsOptionproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String optionSaleproductId; //추가구성단품ID		[primary key, primary key, primary key, not null]
	private String useYn; //사용여부		[not null]

	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
}