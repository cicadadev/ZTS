package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsNoticeSearch extends BaseSearchCondition {
	private String	dateType;
	private String	noticeNos;
	private String	noticeTypeCd;
	private String 	noticeTypeCds;
	private String	useYn;
	private String	fixYn;
	private String	displayYnCds;
	private String	topNoticeYnCds;

	private String title; //제목		[not null]
	private String detail; //내용		[not null]
	private String startDt; //게시시작일시		[not null]
	private String endDt; //게시종료일시		[not null]
	
	private String[] productIds;
	
	private String 	insInfoId;
	private String 	insInfoName;
	
	private String 	businessId; // 업체ID
	
	// FO 공지사항 검색
	private String  type;		//  검색 종류
	private String  info;		//  검색어
	
	private String  productId;
	
	private String  newDt;		// 신규 공지
	private String  brandId;
	private String  isScroll;
}
