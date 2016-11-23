package gcp.pms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.mms.model.MmsMember;
import gcp.pms.model.base.BasePmsReview;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsReview extends BasePmsReview {
	private String 	permitYn;
	private String 	siteName;
	private String 	siteTypeCd;
	private String 	mdId;
	private String 	mdName;
	
	//private String 	orderId;
	private String 	orderDt;
	//private String 	saleproductId;
	private String 	saleproductName;
	
	private String insName;	  	// 등록자
	private String updName;		// 수정자
	
	private String			memberId;		//회원 ID
	private String			memberName;		//회원 NAME
	private String			memberInfo;		//회원 ID(회원 NAME)
	private MmsMember mmsMember;
	
	private BigDecimal	ratingIdAvg;
	private String		ratingId;
	private String		ratingName;
	
	
	private String		categoryId;			// 카테고리 ID
	
	
	private BigDecimal totalRatingAvg;
	private List<PmsReview>	reviewList;		// 상품의 리뷰목록
	private List<PmsReview>	ratingAvgList;	// 항목별 평균 목록
	
	private String productName;
	private String productUrl;
	private String imgYn;
	
	private String CntAll;
	private String CntImgY;
	private String CntImgN;
	private String CntPermitY;
	
	private String searchType;
}