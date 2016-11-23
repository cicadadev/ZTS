package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsStore;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsStyleproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal styleProductNo; //스타일상품번호		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[null]
	private String styleProductItemCd; //스타일상품품목코드		[not null]
	private String img; //이미지||스타일상품번호		[null]
	private String styleProductColorCd; //스타일상품컬러코드		[not null]
	private String useYn; //사용여부		[not null]

	private CcsStore ccsStore;
	private PmsProduct pmsProduct;

	public String getStyleProductItemName(){
			return CodeUtil.getCodeName("STYLE_PRODUCT_ITEM_CD", getStyleProductItemCd());
	}

	public String getStyleProductColorName(){
			return CodeUtil.getCodeName("STYLE_PRODUCT_COLOR_CD", getStyleProductColorCd());
	}
}