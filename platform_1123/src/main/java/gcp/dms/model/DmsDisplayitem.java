package gcp.dms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.common.util.CodeUtil;
import gcp.dms.model.base.BaseDmsDisplayitem;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsDisplayitem extends BaseDmsDisplayitem {

	private PmsProduct		pmsProduct;
	private DmsExhibit		dmsExhibit;
	private BigDecimal		salePrice;
	private BigDecimal		stock;
	private String productName;
	private String productTypeCd;
	private String saleStateCd;
	private String brandName;
	private String exhibitName;
	private String exhibitStateCd;
	private String exhibitStartDt;
	private String exhibitEndDt;
	private PmsBrand pmsBrand;

	private String	saveType;		//U,I
	
	public String getExhibitStateName(){
		return CodeUtil.getCodeName("EXHIBIT_STATE_CD", getExhibitStateCd());
	}
	
	public String getProductTypeName(){
		return CodeUtil.getCodeName("PRODUCT_TYPE_CD", getProductTypeCd());
	}

	public String getSaleStateName(){
		return CodeUtil.getCodeName("SALE_STATE_CD", getSaleStateCd());
	}
	
	private List<PmsProduct> pmsProductList;
	
}