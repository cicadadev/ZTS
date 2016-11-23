package gcp.mms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class MmsMemberSearch extends BaseSearchCondition {

	private String		memberId;

	private String		rnum;
	private String		name;
	private String		name1;
	private String		name2;
	private String		name3;
	private String		name4;
	private String		memNo;
	private String		memNos;
	private String		gender;
	private String		phone1;
	private String		phone2;
	private String		phone3;
	private String		memBerTypeCd;
	private String		memGradeCd;
	private String		memStateCd;
	private String		memGenderCd;
	private String		regStartDt;
	private String		regEndDt;

	private String		memStateCds;
	private String		memBerTypeCds;
	private String		memGradeCds;

	private BigDecimal	memberNo;
	private String		addressNo;
	private String authNumber;
	private String giftName;
	private String giftPhone;
	
	private String		userId;
	/*FO*/
	private String		saveType;
	private String		pageType;
	private String		inquiryStateCd;			// 문의 상태
	private String		productQnaStateCd;		// 상품QnA 상태
	private BigDecimal	inquiryNo;				// 1:1문의/ 상품qna 번호
	private String		topOffshopYn;			// 대표매장 설정 여부
	private String		allOffshopYn;			// 관심매장/대표매장 나눠서 조회할지 여부
	private String orderId;
	private String quickmenuTypeCd;
	
	private int notReceiveCnt;
	private int giftTotalCnt;
	private String mobileYn;
	private String areaDiv1;
	private String branchName;
	
	private String moHistoryId;	// 모바일 history id
	private String productIds;	// 상품번호
	private String memberShipYn;	// 맴버쉽 여부
	
	private String siteKey;	// tms 제로투세븐 사이트키
	
	private String couponId;
	
	private String isScroll;
}
