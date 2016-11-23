package gcp.dms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.dms.model.DmsDisplayitemlang;
import gcp.ccs.model.CcsControl;
import gcp.dms.model.DmsDisplay;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseDmsDisplayitem extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String displayId; //전시ID		[primary key, primary key, primary key, not null]
	private BigDecimal displayItemNo; //전시대상번호		[primary key, primary key, primary key, not null]
	private String displayItemDivId; //전시대상구분ID		[null]
	private String displayItemId; //전시대상ID		[null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String text1; //텍스트1		[null]
	private String text2; //텍스트2		[null]
	private String url1; //링크URL1		[null]
	private String url2; //링크URL2		[null]
	private String title; //타이틀		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]
	private String addValue; //부가정보		[null]
	private BigDecimal controlNo; //노출제어번호||상품,기획전 이외 적용		[null]
	private String displayYn; //전시여부		[not null]
	private String startDt; //전시시작일시		[not null]
	private String endDt; //전시종료일시		[not null]
	private BigDecimal sortNo; //정렬순서		[null]

	private List<DmsDisplayitemlang> dmsDisplayitemlangs;
	private CcsControl ccsControl;
	private DmsDisplay dmsDisplay;
}