package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsTemplateDisplay;
import gcp.pms.model.PmsBrand;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsTemplate extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String templateId; //템플릿ID		[primary key, primary key, primary key, not null]
	private String templateTypeCd; //템플릿유형코드		[not null]
	private String name; //템플릿명		[not null]
	private String url; //템플릿URL		[not null]
	private String useYn; //사용여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<DmsDisplaycategory> dmsDisplaycategorys;
	private List<DmsTemplateDisplay> dmsTemplateDisplays;
	private List<PmsBrand> pmsBrands;
	private CcsStore ccsStore;

	public String getTemplateTypeName(){
			return CodeUtil.getCodeName("TEMPLATE_TYPE_CD", getTemplateTypeCd());
	}
}