package gcp.sps.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class SpsPresentSearch extends BaseSearchCondition {

	private String		storeId;		 //primary key, not null
	private String		presentId;		 //primary key, not null
	private String		name;			 //not null
	private String		presentTypeCd;	 //not null
	private String		presentStateCd;	 //not null
	private String		startDt;		 //null
	private String		endDt;			 //null

	private String		presentTypeCds;
	private String		presentStateCds;
	private String		presentIds;

	private String		infoType;

	// 적용대상 중복체크
	private String[]	idArray;

	private String		productId;			//상품ID
	private String		brandId;				//브랜드ID
	private String		categoryId;			//표준카테고리ID
	private String dealId;	//딜ID
	private BigDecimal qty;	//사은품 재고 check 수량
}
