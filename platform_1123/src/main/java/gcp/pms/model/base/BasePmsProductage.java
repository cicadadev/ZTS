package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductage extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String ageTypeCd; //월령유형코드		[primary key, primary key, primary key, not null]

	private PmsProduct pmsProduct;

	public String getAgeTypeName(){
			return CodeUtil.getCodeName("AGE_TYPE_CD", getAgeTypeCd());
	}
}