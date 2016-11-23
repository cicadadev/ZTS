package gcp.pms.model;

import gcp.ccs.model.CcsBusiness;
import gcp.mms.model.MmsMemberZts;
import gcp.pms.model.base.BasePmsProductqna;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsProductqna extends BasePmsProductqna {
	
//	private String			confirmId;		//문의확인자 ID
	private String			confirmName;	//문의확인자 Name
	private String			insName;		//문의등록자(CS)
//	private String			answerId;		//답변자 ID
	private String			answerName;		//답변자 NAME
	private String			memberId;		//문의자 ID
	private String			memberName;		//문의자NAME
	
	private String			confirmer;		//문의확인자
	private String			answerer;		//답변자
	private String			questioner;		//문의자(회원)
	private String			passTime;		//경과시간
	private MmsMemberZts	mmsMemberzts;
	private CcsBusiness		ccsBusiness;
//	private PmsSaleproduct pmsSaleproduct;
	private String 			businessId;
	private String 			businessName;
	private String			acceptCount;	// 문의접수
	private String			answerCount;	// 답변중
	private String			completeCount;	// 답변완료
	private String			uncompleteCount;	// 미처리
	
	private PmsProductimg	pmsProductimg;
}