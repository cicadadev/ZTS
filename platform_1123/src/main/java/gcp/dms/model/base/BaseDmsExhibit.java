package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsExhibitbrand;
import gcp.dms.model.DmsExhibitcoupon;
import gcp.dms.model.DmsExhibitdisplaycategory;
import gcp.dms.model.DmsExhibitgroup;
import gcp.dms.model.DmsExhibitlang;
import gcp.dms.model.DmsExhibitmainproduct;
import gcp.dms.model.DmsExhibitoffshop;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsExhibit extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String exhibitId; //기획전ID		[primary key, primary key, primary key, not null]
	private String name; //기획전명		[not null]
	private String subtitle; //부제		[null]
	private String exhibitTypeCd; //기획전유형코드		[not null]
	private BigDecimal controlNo; //노출제어번호		[null]
	private String startDt; //기획전시작일시		[null]
	private String endDt; //기획전종료일시		[null]
	private String daysWeek; //요일		[null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String text1; //텍스트1		[null]
	private String text2; //텍스트2		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]
	private String exhibitStateCd; //기획전상태코드		[not null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String subHtml1; //하단HTML1		[null]
	private String subHtml2; //하단HTML2		[null]

	private List<DmsExhibitbrand> dmsExhibitbrands;
	private List<DmsExhibitcoupon> dmsExhibitcoupons;
	private List<DmsExhibitdisplaycategory> dmsExhibitdisplaycategorys;
	private List<DmsExhibitgroup> dmsExhibitgroups;
	private List<DmsExhibitlang> dmsExhibitlangs;
	private List<DmsExhibitmainproduct> dmsExhibitmainproducts;
	private List<DmsExhibitoffshop> dmsExhibitoffshops;
	private CcsControl ccsControl;

	public String getExhibitTypeName(){
			return CodeUtil.getCodeName("EXHIBIT_TYPE_CD", getExhibitTypeCd());
	}

	public String getExhibitStateName(){
			return CodeUtil.getCodeName("EXHIBIT_STATE_CD", getExhibitStateCd());
	}
}