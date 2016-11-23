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
public class BasePmsBarcode extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String erpBarcode; //ERP단품바코드		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String productId; //상품ID		[null]
	private String saleproductId; //단품ID		[null]

	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
}