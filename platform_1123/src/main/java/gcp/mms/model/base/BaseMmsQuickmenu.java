package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsMembermenu;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsQuickmenu extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String menuId; //메뉴ID		[primary key, primary key, primary key, not null]
	private String quickmenuTypeCd; //퀵메뉴유형코드		[not null]
	private String name; //메뉴명		[not null]
	private String url; //페이지URL		[null]
	private String defaultYn; //디폴트메뉴여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String useYn; //사용여부		[not null]

	private List<MmsMembermenu> mmsMembermenus;

	public String getQuickmenuTypeName(){
			return CodeUtil.getCodeName("QUICKMENU_TYPE_CD", getQuickmenuTypeCd());
	}
}