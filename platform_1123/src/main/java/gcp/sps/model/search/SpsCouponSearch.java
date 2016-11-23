package gcp.sps.model.search;

import java.math.BigDecimal;
import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Pagckage Name : gcp.sps.model.search
 * @FileName : SpsCouponSearch.java
 * @author : paul
 * @date : 2016. 4. 21.
 * @description : 쿠폰 search
 */
@EqualsAndHashCode
@ToString
@Data
public class SpsCouponSearch extends BaseSearchCondition {

	private String		termStartDt;
	private String		termEndDt;
	private String		couponId;
	private String		couponIds;
	private String		productId;
	private String		name;
	private String		couponStateCd;
	private String		couponTypeCd;

	private String		couponIssueStateCd;
	private String		couponIssueStateCds;
	private String		applyNo;
	private String		controlNo;
	private String		targetTypeCd;

	private String		couponStateCds;
	private String		couponTypeCds;
	private String		couponDivCds;

	// 적용대상 중복체크
	private String[]	productIdArray;
	private String		applyType;

//	private String		memberNo;

	private String		infoType;

	private String		dealId;
	private BigDecimal	memberNo;
	private String		brandId;
	private String		categoryId;

	private String		businessId;		// 자사 로그인시
	private String		poBusinessId;	// po 로그인시
	
	private String channelId;
	private String deviceTypeCd;
	private List<String> memberTypeCds;
	private String memGradeCd;
	private String saleproductId;
	private BigDecimal targetAmt;
	private String downShowYn;
	private String memberCouponYn;
	private BigDecimal commissionRate;	//상품수수료율
	
	private String controlType;
}
