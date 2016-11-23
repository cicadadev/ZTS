package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsPopupurl;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsPopup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal popupNo; //팝업번호		[primary key, primary key, primary key, not null]
	private String popupTypeCd; //팝업유형코드		[not null]
	private String pcDisplayYn; //PC노출여부		[not null]
	private String mobileDisplayYn; //모바일노출여부		[not null]
	private String title; //제목		[not null]
	private String detail1; //내용1		[null]
	private String detail2; //내용2		[null]
	private String startDt; //게시시작일시		[not null]
	private String endDt; //게시종료일시		[not null]
	private String position; //팝업위치||x,y,width,height		[null]
	private String displayYn; //전시여부		[not null]

	private List<CcsPopupurl> ccsPopupurls;
	private CcsStore ccsStore;

	public String getPopupTypeName(){
			return CodeUtil.getCodeName("POPUP_TYPE_CD", getPopupTypeCd());
	}
}