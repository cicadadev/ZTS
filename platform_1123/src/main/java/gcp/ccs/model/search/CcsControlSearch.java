package gcp.ccs.model.search;

import java.math.BigDecimal;
import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsControlSearch extends BaseSearchCondition {
	private BigDecimal controlNo;
	private String applyObjectName;//오류메시지에 들어갈 이름.
	private boolean exceptionFlag = false;	//exception 발생 여부
	private BigDecimal	memberNo;			//회원번호
	private String		memGradeCd;			//회원등급
	private String		memberTypeCd;		//회원유형
	private List<String>		memberTypeCds;		//회원유형
	private String		channelId;			//채널
	private String		deviceTypeCd;		//디바이스
	
	private String controlType;	
	
}
