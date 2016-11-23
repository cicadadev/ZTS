package gcp.oms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class OmsCartSearch extends BaseSearchCondition {
	
	private String cartId;	//member id or 쿠키 id
	private String cartTypeCd;	//카트유형코드 
	private String productId;	//상품ID
	private String dealId;		//딜ID
	private String channelId;	//채널ID
	private String deviceTypeCd;	
	private String memGradeCd;
	private String dealTypeCds;
	private BigDecimal memberNo;
	private String saleproductId;
	private String cartProductNos;
	
	private String offshopId;
	private String orgOffshopId;
	
	private boolean priceChange = false;	//가격변경
	
	private String isMypage;
}
