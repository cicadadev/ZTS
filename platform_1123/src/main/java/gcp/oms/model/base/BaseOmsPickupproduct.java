package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsOffshop;
import gcp.oms.model.OmsPickup;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPickupproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String pickupId; //매장픽업신청ID		[primary key, primary key, primary key, not null]
	private BigDecimal productNo; //주문상품일련번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String offshopId; //매장ID		[null]
	private String brandId; //브랜드ID		[not null]
	private String categoryId; //표준카테고리ID		[not null]
	private String productTypeCd; //상품유형코드		[not null]
	private String productId; //상품ID		[not null]
	private String erpProductId; //ERP상품ID		[null]
	private String productName; //상품명		[not null]
	private String saleproductId; //단품ID		[null]
	private String erpSaleproductId; //ERP단품ID		[null]
	private String erpColorId; //ERP색상옵션		[null]
	private String erpSizeId; //ERP사이즈옵션		[null]
	private String saleproductName; //단품명		[not null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]
	private BigDecimal totalSalePrice; //총판매가||개당		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[null]
	private BigDecimal orderAmt; //최종결제가||상품당		[not null]
	private BigDecimal orderQty; //주문수량		[not null]
	private String posMid; //POS트랜잭션ID		[null]
	private String pickupProductStateCd; //픽업상태코드		[not null]
	private String pickupReqDt; //픽업신청일시		[null]
	private String pickupReserveDt; //픽업예정일시		[null]
	private String pickupDeliveryDt; //픽업수령일시		[null]
	private String pickupCancelDt; //픽업취소일시		[null]

	private CcsOffshop ccsOffshop;
	private OmsPickup omsPickup;

	public String getProductTypeName(){
			return CodeUtil.getCodeName("PRODUCT_TYPE_CD", getProductTypeCd());
	}

	public String getPickupProductStateName(){
			return CodeUtil.getCodeName("PICKUP_PRODUCT_STATE_CD", getPickupProductStateCd());
	}
}