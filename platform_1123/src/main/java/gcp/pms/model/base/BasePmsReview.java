package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsReviewrating;
import gcp.mms.model.MmsMember;
import gcp.oms.model.OmsOrder;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsReview extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal reviewNo; //구매후기번호		[primary key, primary key, primary key, not null]
	private BigDecimal memberNo; //회원번호		[null]
	private String orderId; //주문ID		[null]
	private String saleproductId; //단품ID		[null]
	private String eventId; //이벤트ID		[null]
	private String psRegisterEndDt; //후기등록종료일		[null]
	private String title; //제목		[null]
	private String detail; //내용		[not null]
	private BigDecimal rating; //별점수		[not null]
	private String bestYn; //베스트리뷰여부		[null]
	private String displayYn; //전시여부		[not null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String img3; //이미지3		[null]
	private String benefitDt; //혜택부여일시		[null]

	private List<PmsReviewrating> pmsReviewratings;
	private MmsMember mmsMember;
	private OmsOrder omsOrder;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;
}