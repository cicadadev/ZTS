package gcp.pms.model.erp;

import java.math.BigDecimal;

import gcp.pms.model.PmsBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsErpItem {

	private BigDecimal	tkrLikelysales;
	private BigDecimal	tkrTagprice;
	private String		itemname;
	private String		tkrOrigin;
	private String		itemid;
	private String		option1Name;
	private String		inventsizeid;
	private String		option2Name;
	private String		inventcolorid;
	private String		tkrBrand;
	private String		brandname;
	private BigDecimal	tkrEstimatecost;
	private String		ztsStdcategory;
	private String		exItemid;
	private BigDecimal	unitvolume;
	private String		itembarcode;
	private String		apxManufacture;
	private String		apxTax;
	private String		apxGoodtype;
	
	// 매핑 브랜드
	private PmsBrand	pmsBrand;
}