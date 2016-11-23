package gcp.oms.model.grid;

import java.math.BigDecimal;

import gcp.common.util.CodeUtil;
import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsOrderGrid extends BaseEntity{

	private String orderId;
	private String orderDt;
	private String orderSaleproductStateCd;
	private String paymentTypeCd;
	private String productName;
	private int orderQty;
	private int cancelQty;
	private int returnQty;
	private int exchangeQty;
	private BigDecimal salePrice;
	private BigDecimal dcAmt;
	private BigDecimal paymentAmt;
	private BigDecimal point;
	private String memId;
	private String name1;
	private String phone1;
	private String phone2;
	private String address1;
	private String address2;
	private String address3;
	private String memberTypeCd;
	
	public String getOrderSaleproductStateName(){
		return CodeUtil.getCodeName("ORDER_SALEPRODUCT_STATE_CD", getOrderSaleproductStateCd());
	}
	
	public String getPaymentTypeName(){
		return CodeUtil.getCodeName("PAYMENT_TYPE_CD", getPaymentTypeCd());
	}
	
	public String getMemberTypeName(){
		return CodeUtil.getCodeName("MEMBER_TYPE_CD", getMemberTypeCd());
	}
	
}
