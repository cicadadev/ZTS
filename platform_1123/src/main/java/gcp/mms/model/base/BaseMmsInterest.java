package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsInterest extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal interestNo; //관심정보번호		[primary key, primary key, primary key, not null]
	private String interestTypeCd; //관심정보유형코드		[null]
	private String displayCategoryId; //전시카테고리ID		[null]
	private String brandId; //브랜드ID		[null]
	private String purposeTypeCd; //방문동기유형코드		[null]
	private String styleTypeCd; //스타일유형코드		[null]
	private String interestProductTypeCd; //관심상품유형코드		[null]

	private MmsMemberZts mmsMemberZts;

	public String getInterestTypeName(){
			return CodeUtil.getCodeName("INTEREST_TYPE_CD", getInterestTypeCd());
	}

	public String getPurposeTypeName(){
			return CodeUtil.getCodeName("PURPOSE_TYPE_CD", getPurposeTypeCd());
	}

	public String getStyleTypeName(){
			return CodeUtil.getCodeName("STYLE_TYPE_CD", getStyleTypeCd());
	}

	public String getInterestProductTypeName(){
			return CodeUtil.getCodeName("INTEREST_PRODUCT_TYPE_CD", getInterestProductTypeCd());
	}
}