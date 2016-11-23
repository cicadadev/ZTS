package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsExhibitproduct;
import gcp.dms.model.DmsExhibit;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibitgroup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private BigDecimal groupNo; //상품그룹번호		[primary key, primary key, primary key, not null]
	private String groupTypeCd; //그룹유형코드		[null]
	private String name; //그룹명		[null]
	private String img; //이미지		[null]
	private String url1; //링크URL1		[null]
	private String url2; //링크URL2		[null]
	private String productDisplayType1Cd; //상품노출유형코드1		[not null]
	private String productDisplayType2Cd; //상품노출유형코드2		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String displayYn; //전시여부		[not null]

	private List<DmsExhibitproduct> dmsExhibitproducts;
	private DmsExhibit dmsExhibit;

	public String getGroupTypeName(){
			return CodeUtil.getCodeName("GROUP_TYPE_CD", getGroupTypeCd());
	}

	public String getProductDisplayType1Name(){
			return CodeUtil.getCodeName("PRODUCT_DISPLAY_TYPE1_CD", getProductDisplayType1Cd());
	}

	public String getProductDisplayType2Name(){
			return CodeUtil.getCodeName("PRODUCT_DISPLAY_TYPE2_CD", getProductDisplayType2Cd());
	}
}