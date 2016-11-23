package gcp.pms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
@Data
public class PmsProductReviewSearch extends BaseSearchCondition {
	
	private String 		memNo;
	private String 		memIds;
	private String		productId;
	private String		productIds;
	private String 		permitYn;
	private String 		permitYnCds;
	private String 		serchType;
	private String 		mdId;
	private String 		mdName;
	private String 		productName;
	private String  	prdReviewTypeCds;
	private String 		displayYn;
	private String 		memSearchType;
	private String 		memSearchWord;
	private String 		reviewType;
	
	private String		displayYnCds;
	private String 		memberId;
	private String 		memberName;
	
	// FO
	
	private BigDecimal 		memberNo;
	private BigDecimal 		reviewNo;
	private BigDecimal 		imgNo;
	
	private String 		orderId;
	private String 		saleproductId;
	private String 		saleproductName;
	
	private String 		imgYn;
	private String 		detail;
	private String 		orderBy;
	private String 		type;
	
	private String		isScroll;
}
