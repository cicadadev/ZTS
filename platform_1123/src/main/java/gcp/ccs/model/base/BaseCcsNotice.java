package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsNoticelang;
import gcp.ccs.model.CcsNoticeproduct;
import gcp.ccs.model.CcsNoticeBrand;
import gcp.ccs.model.CcsStore;
import gcp.sps.model.SpsEvent;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsNotice extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal noticeNo; //공지사항번호		[primary key, primary key, primary key, not null]
	private String noticeTypeCd; //공지사항유형코드		[not null]
	private String title; //제목		[not null]
	private String detail; //내용		[not null]
	private String startDt; //게시시작일시		[not null]
	private String endDt; //게시종료일시		[not null]
	private String displayYn; //전시여부		[not null]
	private String topNoticeYn; //Top공지여부		[not null]
	private String eventId; //이벤트ID		[null]
	private String eventTargetDivCd; //이벤트대상구분코드		[null]
	private BigDecimal readCnt; //조회수		[null]

	private List<CcsNoticelang> ccsNoticelangs;
	private List<CcsNoticeproduct> ccsNoticeproducts;
	private List<CcsNoticeBrand> ccsNoticeBrands;
	private CcsStore ccsStore;
	private SpsEvent spsEvent;

	public String getNoticeTypeName(){
			return CodeUtil.getCodeName("NOTICE_TYPE_CD", getNoticeTypeCd());
	}

	public String getEventTargetDivName(){
			return CodeUtil.getCodeName("EVENT_TARGET_DIV_CD", getEventTargetDivCd());
	}
}