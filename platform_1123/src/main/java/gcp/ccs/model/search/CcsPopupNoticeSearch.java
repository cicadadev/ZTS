package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsPopupNoticeSearch extends BaseSearchCondition {
	private String	popupTypeCd;
	private String 	popupTypeCds;
	private String 	channelTypeCds;
	private String	displayYnCds;
	private String	searchInsid;
	private String  channelYn;
	private String	pcChannelY;
	private String	mobileChannelY;

	private String title; //제목		[not null]
	private String detail; //내용		[not null]
	private String startDt; //게시시작일시		[not null]
	private String endDt; //게시종료일시		[not null]	
	
	private String 	insInfoId;
	private String 	insInfoName;
	
	// FO 
	private String 	url;  // URL
}

