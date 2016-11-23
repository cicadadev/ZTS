package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquiry;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsBarcode;
import gcp.pms.model.PmsOffshopstock;
import gcp.pms.model.PmsOptionproduct;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsSaleproductlang;
import gcp.pms.model.PmsSaleproductoptionvalue;
import gcp.pms.model.PmsSaleproductpricereserve;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsWarehouselocation;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsSaleproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String saleproductId; //단품ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[null]
	private String erpSaleproductId; //ERP단품ID		[null]
	private String erpColorId; //ERP색상옵션		[null]
	private String erpSizeId; //ERP사이즈옵션		[null]
	private String businessSaleproductId; //업체단품ID		[null]
	private String name; //단품명		[not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]
	private BigDecimal safeStockQty; //안전재고수량		[not null]
	private String warehouseId; //창고ID		[null]
	private String locationId; //로케이션ID		[null]
	private BigDecimal realStockQty; //실재고수량		[not null]
	private BigDecimal deliveryTogetherQty; //합배송수량		[null]
	private String saleproductStateCd; //단품상태코드		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<CcsInquiry> ccsInquirys;
	private List<OmsCart> omsCarts;
	private List<OmsOrderproduct> omsOrderproducts;
	private List<PmsBarcode> pmsBarcodes;
	private List<PmsOffshopstock> pmsOffshopstocks;
	private List<PmsOptionproduct> pmsOptionproducts;
	private List<PmsProductqna> pmsProductqnas;
	private List<PmsReview> pmsReviews;
	private List<PmsSaleproductlang> pmsSaleproductlangs;
	private List<PmsSaleproductoptionvalue> pmsSaleproductoptionvalues;
	private List<PmsSaleproductpricereserve> pmsSaleproductpricereserves;
	private PmsProduct pmsProduct;
	private PmsWarehouselocation pmsWarehouselocation;

	public String getSaleproductStateName(){
			return CodeUtil.getCodeName("SALEPRODUCT_STATE_CD", getSaleproductStateCd());
	}
}