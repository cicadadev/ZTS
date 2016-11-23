package gcp.sps.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsDealSearch extends BaseSearchCondition {

	private String dealId;
	private String dealGroupNo;
	private String productId;
	private BigDecimal memberNo;
	private String saleproductId;
	private String memGradeCd;
	private String dealTypeCds;
	private String dealTypeCd;
	private String childrencardTypeCd;
	
	private String upperDealGroupNo;
	private String oneItem;					// 구분타이틀별 1개식(신규 등록 상품으로)
	private String sortType;				// MO sort box : 01-인기상품순, 02-상품평순, 03-높은가격순, 04-낮은가격순, 05-최근등록순
	
	// 임직원 상세검색
	private String attributeId;
	private String color;
	private String material;
	private String benefit;
	private String ageTypeCd;
	private String genderTypeCd;
	private BigDecimal prePrice;
	private BigDecimal postPrice;
	
}
