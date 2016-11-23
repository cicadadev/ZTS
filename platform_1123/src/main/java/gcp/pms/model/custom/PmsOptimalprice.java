package gcp.pms.model.custom;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper=true)
public class PmsOptimalprice{

	//parameter
	private BigDecimal memberNo;
	private String productId;
	private String saleproductId;
	private BigDecimal targetAmt;	//salePrice + addSalePrice
	private String storeId;
	private String channelId;
	private String deviceTypeCd;
	private String memGradeCd;
	private String childrenDealId;
	private List<String> memberTypeCds;
	private BigDecimal commissionRate;	//수수료율
	private String paramDealId;		//상품의 최적deal Id
	private String paramCouponId;	//상품의 최적쿠폰 ID
	private String optimalFlag;	//최적가 계산 or 적용 최적가
	
	//result
	private BigDecimal seq;
	private String couponId;
	private String dealId;
	private BigDecimal listPrice;
	private BigDecimal salePrice;// pms_product의 salePrice
	private BigDecimal addSalePrice;	
	private BigDecimal totalSalePrice;//최적가(쿠폰제외가)
	private String singleApplyYn;	//쿠폰 1개적용여부
	private BigDecimal couponDcAmt; //쿠폰할인가
	private BigDecimal pointSaveRate; // 
	private String deliveryFeeFreeYn;
	
	private String dcApplyTypeCd;// 쿠폰 할인 유형
	private BigDecimal dcValue;//쿠폰 할인값
	
	private String dealTypeCd;
	
	private BigDecimal dealStockQty;	//딜수량
	
	private String controlType;
	

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
