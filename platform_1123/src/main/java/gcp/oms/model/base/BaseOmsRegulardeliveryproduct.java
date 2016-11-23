package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsRegulardeliveryproduct;
import gcp.oms.model.OmsRegulardeliveryschedule;
import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.model.OmsRegulardeliveryproduct;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsRegulardeliveryproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal deliveryProductNo; //정기배송상품일련번호		[primary key, primary key, primary key, not null]
	private String regularDeliveryId; //정기배송신청ID		[primary key, primary key, primary key, not null]
	private BigDecimal upperDeliveryProductNo; //상위정기배송상품일련번호		[null]
	private BigDecimal setQty; //세트구성수량		[not null]
	private String deliveryProductTypeCd; //정기배송상품유형코드		[not null]
	private String storeId; //상점ID		[null]
	private String productId; //상품ID		[not null]
	private String saleproductId; //단품ID		[not null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal regularDeliveryPrice; //정기배송가		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private String deliveryFeeFreeYn; //배송비무료여부		[not null]
	private BigDecimal orderQty; //주문수량		[not null]
	private String deliveryPeriodCd; //배송주기코드		[not null]
	private BigDecimal deliveryPeriodValue; //배송주기값		[not null]
	private BigDecimal deliveryCnt; //총배송횟수		[not null]
	private String deliveryProductStateCd; //정기배송상품상태코드		[not null]

	private List<OmsRegulardeliveryproduct> omsRegulardeliveryproducts;
	private List<OmsRegulardeliveryschedule> omsRegulardeliveryschedules;
	private OmsRegulardelivery omsRegulardelivery;
	private OmsRegulardeliveryproduct omsRegulardeliveryproduct;

	public String getDeliveryProductTypeName(){
			return CodeUtil.getCodeName("DELIVERY_PRODUCT_TYPE_CD", getDeliveryProductTypeCd());
	}

	public String getDeliveryPeriodName(){
			return CodeUtil.getCodeName("DELIVERY_PERIOD_CD", getDeliveryPeriodCd());
	}

	public String getDeliveryProductStateName(){
			return CodeUtil.getCodeName("DELIVERY_PRODUCT_STATE_CD", getDeliveryProductStateCd());
	}
}