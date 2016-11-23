package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusiness;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsEpexcproduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal excProductNo; //예외상품번호		[primary key, primary key, primary key, not null]
	private String excProductTypeCd; //예외상품유형코드		[not null]
	private String businessId; //업체ID		[null]
	private String productId; //상품ID		[null]

	private CcsBusiness ccsBusiness;
	private PmsProduct pmsProduct;

	public String getExcProductTypeName(){
			return CodeUtil.getCodeName("EXC_PRODUCT_TYPE_CD", getExcProductTypeCd());
	}
}