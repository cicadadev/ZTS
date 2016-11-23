package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsNotice;
import gcp.sps.model.SpsEventbrand;
import gcp.sps.model.SpsEventcoupon;
import gcp.sps.model.SpsEventgift;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.SpsEventlang;
import gcp.sps.model.SpsEventprize;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsStore;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEvent extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private String name; //이벤트명		[not null]
	private String detail; //설명		[null]
	private String eventTypeCd; //이벤트유형코드		[not null]
	private String productId; //체험대상상품ID		[null]
	private String eventDivCd; //이벤트구분코드		[not null]
	private String eventStartDt; //이벤트시작일시		[not null]
	private String eventEndDt; //이벤트종료일시		[not null]
	private String eventJoinStartDt; //이벤트응모시작일시		[not null]
	private String eventJoinEndDt; //이벤트응모종료일시		[not null]
	private String lotteryTypeCd; //추첨유형코드		[null]
	private String joinControlCd; //응모제어코드		[not null]
	private BigDecimal controlNo; //노출제어번호		[null]
	private String displayYn; //전시여부		[not null]
	private String img1; //이미지1		[null]
	private String img2; //이미지2		[null]
	private String text1; //텍스트1		[null]
	private String text2; //텍스트2		[null]
	private String html1; //HTML1		[null]
	private String html2; //HTML2		[null]
	private String winNoticeDate; //당첨자발표일		[not null]
	private BigDecimal winnerNumber; //당첨인원		[null]
	private String winnerShowYn; //당첨자노출여부		[null]
	private String psRegisterEndDt; //후기등록종료일		[null]
	private String eventStateCd; //이벤트상태코드		[not null]

	private List<CcsNotice> ccsNotices;
	private List<SpsEventbrand> spsEventbrands;
	private List<SpsEventcoupon> spsEventcoupons;
	private List<SpsEventgift> spsEventgifts;
	private List<SpsEventjoin> spsEventjoins;
	private List<SpsEventlang> spsEventlangs;
	private List<SpsEventprize> spsEventprizes;
	private CcsControl ccsControl;
	private CcsStore ccsStore;
	private PmsProduct pmsProduct;

	public String getEventTypeName(){
			return CodeUtil.getCodeName("EVENT_TYPE_CD", getEventTypeCd());
	}

	public String getEventDivName(){
			return CodeUtil.getCodeName("EVENT_DIV_CD", getEventDivCd());
	}

	public String getLotteryTypeName(){
			return CodeUtil.getCodeName("LOTTERY_TYPE_CD", getLotteryTypeCd());
	}

	public String getJoinControlName(){
			return CodeUtil.getCodeName("JOIN_CONTROL_CD", getJoinControlCd());
	}

	public String getEventStateName(){
			return CodeUtil.getCodeName("EVENT_STATE_CD", getEventStateCd());
	}
}