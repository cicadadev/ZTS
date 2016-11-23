package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductnoticefield;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductnotice extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String productNoticeTypeCd; //상품고시유형코드		[primary key, primary key, primary key, not null]
	private String productNoticeFieldId; //상품고시항목ID		[primary key, primary key, primary key, not null]
	private String detail; //내용		[not null]

	private PmsProduct pmsProduct;
	private PmsProductnoticefield pmsProductnoticefield;

	public String getProductNoticeTypeName(){
			return CodeUtil.getCodeName("PRODUCT_NOTICE_TYPE_CD", getProductNoticeTypeCd());
	}
}