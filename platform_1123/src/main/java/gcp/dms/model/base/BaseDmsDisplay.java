package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.DmsTemplateDisplay;
import gcp.ccs.model.CcsStore;
import gcp.dms.model.DmsDisplay;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsDisplay extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String displayId; //전시ID		[primary key, primary key, primary key, not null]
	private String upperDisplayId; //상위전시ID		[null]
	private String name; //전시명		[not null]
	private String displayTypeCd; //전시유형코드		[null]
	private String displayItemTypeCd; //전시대상유형코드		[null]
	private String leafYn; //리프여부		[not null]
	private String displayYn; //전시여부		[not null]
	private String useYn; //사용여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<DmsDisplay> dmsDisplays;
	private List<DmsDisplayitem> dmsDisplayitems;
	private List<DmsTemplateDisplay> dmsTemplateDisplays;
	private CcsStore ccsStore;
	private DmsDisplay dmsDisplay;

	public String getDisplayTypeName(){
			return CodeUtil.getCodeName("DISPLAY_TYPE_CD", getDisplayTypeCd());
	}

	public String getDisplayItemTypeName(){
			return CodeUtil.getCodeName("DISPLAY_ITEM_TYPE_CD", getDisplayItemTypeCd());
	}
}