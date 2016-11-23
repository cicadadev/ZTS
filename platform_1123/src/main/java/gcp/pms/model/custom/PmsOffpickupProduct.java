package gcp.pms.model.custom;

import java.util.List;

import gcp.ccs.model.CcsOffshop;
import intune.gsf.model.BaseEntity;
import lombok.Data;

@Data
public class PmsOffpickupProduct extends BaseEntity{

	private String				productName;
	private String				saleproductName;
	private String				realStockQty;
	private String				saleproductId;
	private String             storeId;

	private List<CcsOffshop>	areaDiv1s;
	private List<CcsOffshop>	areaDiv2s;
	private List<CcsOffshop>   shopList;
	private CcsOffshop 			ccsOffshop;
	private String 				productId;

}
