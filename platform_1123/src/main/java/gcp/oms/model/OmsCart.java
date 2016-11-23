package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gcp.ccs.model.CcsOffshop;
import gcp.common.util.CodeUtil;
import gcp.oms.model.base.BaseOmsCart;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.custom.PmsOptimalprice;
import gcp.sps.model.SpsPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsCart extends BaseOmsCart {
	private boolean cartTF = false;	//장바구니에서 넘어온 data	
	private String newSaleproductId;	//새로운 단품ID
	
	private BigDecimal memberNo;
	
	private String productName;		//상품명
	private String brandName;		//브랜드명
	private String saleproductName;	//단품명
	private BigDecimal deliveryPolicyNo;	//배송정책번호
	private BigDecimal deliveryFee;	//배송비
	private BigDecimal minDeliveryFreeAmt;	//배송비무료최소주문금액
	private String deliveryFeeFreeYn;
	private String saleStateCd;		//상품상태
	private BigDecimal orgTotalSalePrice;	//원판매가.
	private BigDecimal cartTotalSalePrice;	//카트판매가	
	
	private String cartProductNos;
	private List<SpsPresent> spsPresents;	//사은품
	private List<PmsSaleproduct> pmsSaleproducts;	//단품
	
	private int generalCnt = 0;
	private int pickupCnt = 0;
	private int regulardeliveryCnt = 0;
	private int totalCnt = 0;	//총장바구니 개수
	private BigDecimal cartDeliveryCnt;	//장바구니 배송상품수
	
	private CcsOffshop ccsOffshop;	//매장
	private String wrapYn;
	
	private String memGradeCd;	//회원등급
	private String dealTypeCds;	//딜유형코드
	private String deviceTypeCd;	//	
	private PmsOptimalprice optimalprice;
	private List<PmsOptimalprice> optimalprices;	//최적가격 목록
	private String singleApplyYn;	//1개적용여부
	private BigDecimal couponDcAmt;
	
	private BigDecimal regularDeliveryMinCnt;
	private BigDecimal regularDeliveryMaxCnt;
	private BigDecimal regularDeliveryMaxQty;
	private BigDecimal minQty;	//주문가능 최소수량
	private String optionYn;
	
	private BigDecimal policyTotalPrice;
	private String policyDeliveryFeeFreeYn;
	
	private String businessId;	//업체ID
	
	private BigDecimal commissionRate;
	
	private BigDecimal realStockQty;
	
	private BigDecimal maxStockQty;		
	private String orgOffshopId;
	
	public String getSaleStateName(){
		return CodeUtil.getCodeName("SALE_STATE_CD", getSaleStateCd());
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}