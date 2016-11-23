package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsInquiry;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsMemberZts;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsInquiry extends BaseCcsInquiry {
//	private String memberInfo;
//	private String answerInfo;
//	private String confirmInfo;
//	private String insInfo;
//	private String answerYn;
//	private String passTime;
//	private String insName;
//	private String answerName;
//	private String confirmName;
//	private String businessName;
//	private String confirmYn;
//	private String answerConfirmYn;
	
	private String			memberYn;		// 회원/비회원
//	private String			confirmId;		//문의확인자 ID
	private String			confirmName;	//문의확인자 Name
	private String			insName;		//문의등록자(CS)
//	private String			answerId;		//답변자 ID
	private String			answerName;		//답변자 NAME
	private String			memberId;		//문의자 ID
	private String			memberName;		//문의자NAME
	private String			passTime;		//경과시간
	private MmsMemberZts	mmsMemberzts;
//	private MmsMember 		mmsMember;
//	private CcsBusiness		ccsBusiness;
	private String 			businessName;
	private PmsBrand 		pmsBrand;
	private OmsOrderproduct omsOrderproduct;
	
	private String			acceptCount;	// 문의접수
	private String			answerCount;	// 답변중
	private String			completeCount;	// 답변완료
	private String			uncompleteCount;	// 미처리
	
	//detail
	private String			confirmer;		//문의확인자
	private String			creator;		//문의등록자(CS)
	private String			answerer;		//답변자
	private String			questioner;		//문의자(회원)
}