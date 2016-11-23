package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsRecommendproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String recommendProductId; //추천상품ID		[primary key, primary key, primary key, not null]
	private String recommendTypeCd; //추천유형코드		[null]
	private BigDecimal sortNo; //정렬순서		[null]

	private PmsProduct pmsProduct;

	public String getRecommendTypeName(){
			return CodeUtil.getCodeName("RECOMMEND_TYPE_CD", getRecommendTypeCd());
	}
}