package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsCart;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsOffshop;
import gcp.oms.model.OmsCart;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsDeal;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsCart extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal cartProductNo; //카트상품번호		[primary key, primary key, primary key, not null]
	private BigDecimal upperCartProductNo; //상위카트상품번호		[null]
	private BigDecimal setQty; //세트구성수량		[not null]
	private String cartTypeCd; //카트유형코드		[not null]
	private String cartProductTypeCd; //카트상품유형코드		[not null]
	private String cartId; //카트ID||쿠키or회원ID		[not null]
	private String offshopId; //매장ID		[null]
	private String channelId; //채널ID		[null]
	private String productId; //상품ID		[not null]
	private String saleproductId; //단품ID		[null]
	private String dealId; //딜ID		[null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal addSalePrice; //추가판매가		[not null]
	private BigDecimal totalSalePrice; //총판매가||개당		[not null]
	private BigDecimal regularDeliveryPrice; //정기배송가		[not null]
	private BigDecimal qty; //수량		[not null]
	private String keepYn; //보관여부		[not null]
	private String cartStateCd; //카트상태코드		[not null]
	private String endDt; //유효기간종료일시		[null]
	private String couponId; //최적가적용쿠폰ID		[null]
	private BigDecimal deliveryCnt; //배송회수		[null]
	private String deliveryPeriodCd; //배송주기코드		[null]
	private BigDecimal deliveryPeriodValue; //배송주기값		[null]
	private BigDecimal styleNo; //스타일번호		[null]

	private List<OmsCart> omsCarts;
	private CcsChannel ccsChannel;
	private CcsOffshop ccsOffshop;
	private OmsCart omsCart;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
	private SpsCoupon spsCoupon;
	private SpsDeal spsDeal;

	public String getCartTypeName(){
			return CodeUtil.getCodeName("CART_TYPE_CD", getCartTypeCd());
	}

	public String getCartProductTypeName(){
			return CodeUtil.getCodeName("CART_PRODUCT_TYPE_CD", getCartProductTypeCd());
	}

	public String getCartStateName(){
			return CodeUtil.getCodeName("CART_STATE_CD", getCartStateCd());
	}

	public String getDeliveryPeriodName(){
			return CodeUtil.getCodeName("DELIVERY_PERIOD_CD", getDeliveryPeriodCd());
	}
}