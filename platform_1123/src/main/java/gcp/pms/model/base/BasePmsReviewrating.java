package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsReview;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsReviewrating extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal reviewNo; //구매후기번호		[primary key, primary key, primary key, not null]
	private String ratingId; //별점ID		[primary key, primary key, primary key, not null]
	private BigDecimal rating; //별점수		[not null]

	private PmsReview pmsReview;
}