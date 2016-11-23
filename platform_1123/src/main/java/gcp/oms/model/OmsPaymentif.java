package gcp.oms.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gcp.oms.model.base.BaseOmsPaymentif;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPaymentif extends BaseOmsPaymentif {
	
	private String storeId;	
	private String claimNo;	
	private String orderTypeCd;
	private String deviceTypeCd;
	private String mobileOs;
	private String paymentMethodCd;
	
	private String	CST_PLATFORM;		                //LG유플러스 결제서비스 선택(test:테스트, service:서비스)
	private String	CST_MID;						                     //LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요.
	private String	LGD_MID;					 //테스트 아이디는 't'를 제외하고 입력하세요.
	//상점아이디(자동생성)
	private String	LGD_OID;					                     //주문번호(상점정의 유니크한 주문번호를 입력하세요)
	private String	LGD_AMOUNT;				                  //결제금액("," 를 제외한 결제금액을 입력하세요)
	private String	LGD_MERTKEY;															//상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
	private String	LGD_BUYER;				                   //구매자명
	private String	LGD_PRODUCTINFO;	             //상품명
	private String	LGD_BUYEREMAIL;		              //구매자 이메일
	private String	LGD_TIMESTAMP;		               //타임스탬프
	private String	LGD_CUSTOM_SKIN;	                                               //상점정의 결제창 스킨(red, purple, yellow)
	private String	LGD_WINDOW_VER;		                                               //결제창 버젼정보
	private String	LGD_BUYERID;			      			//구매자 아이디
	private String	LGD_BUYERIP;			      			//구매자IP
	private String LGD_CUSTOM_USABLEPAY;	//상점정의 초기결제수단
	
	private String LGD_ENCODING;	//결제창 호출문자 인코딩방식
	private String LGD_ENCODING_RETURNURL;	//결제창수신페이지 호출문자 인코딩방식.
	private String LGD_ENCODING_NOTEURL;	//가상계좌수신페이지 인코딩방식.
	
	/* 가상계좌(무통장) 결제 연동을 하시는 경우 아래 LGD_CASNOTEURL 을 설정하여 주시기 바랍니다. */ 	 
	private String LGD_CASNOTEURL;
	/*
     * LGD_RETURNURL 을 설정하여 주시기 바랍니다. 반드시 현재 페이지와 동일한 프로트콜 및  호스트이어야 합니다. 아래 부분을 반드시 수정하십시요.
     */
	private String LGD_RETURNURL;		// FOR MANUAL
	
	private String LGD_HASHDATA;
	private String LGD_CUSTOM_PROCESSTYPE;
	
	private String LGD_PAYKEY;	//LG유플러스 인증KEY
	
	////// crossplatform
	private String LGD_WINDOW_TYPE;		//수정불가
	private String LGD_CUSTOM_SWITCHINGTYPE;	// 신용카드 카드사 인증 페이지 연동 방식
	private String LGD_OSTYPE_CHECK;		//값 P: XPay 실행(PC 결제 모듈): PC용과 모바일용 모듈은 파라미터 및 프로세스가 다르므로 PC용은 PC 웹브라우저에서 실행 필요.
	
	////// 계좌이체
	private String LGD_AUTOFILLYN_BUYER;	//구매자명 자동채움.
	private String LGD_USABLEBANK;	//선택은행
	
	
	////// 자체창(신용카드)	
//	private String LGD_CARDTYPE;	//카드종류
//	private String LGD_INSTALL;		//할부개월수
	private String LGD_SELF_CUSTOM;		//자체창
	private String LGD_SP_CHAIN_CODE;	//간편결제사용여부		
	private String LGD_SP_ORDER_USER_ID;	//삼성카드간편결제 key id
	private String LGD_POINTUSE;		//포인트사용여부
	private String LGD_MERTNAME;	//상점명
	
	private String LGD_AUTHTYPE;		//인증유형(ISP인경우만  'ISP')  
	private String LGD_CARDTYPE;        //카드사코드                 
	private String LGD_CURRENCY;        //통화코드                  	
	private String LGD_PAN;				 	//카드번호                                  
	private String LGD_INSTALL;              	//할부개월수                             
	private String LGD_NOINT;             	//무이자할부여부('1':상점부담무이자할부,'0':일반할부)       
	private String LGD_EXPYEAR;              	//유효기간년(YY)                         
	private String LGD_EXPMON;              	//유효기간월 (MM)                            
	private String VBV_ECI;             	//안심클릭ECI                               
	private String VBV_CAVV;            //안심클릭CAVV                                  
	private String VBV_XID;             	//안심클릭XID                               
	private String VBV_JOINCODE;            	//안심클릭JOINCODE                          
	private String KVP_QUOTA;           //할부개월수                               
	private String KVP_NOINT;           //무이자할부여부('1':상점부담무이자할부,'0':일반할부)     
	private String KVP_CARDCODE;        //ISP카드코드                             
	private String KVP_SESSIONKEY;      	//ISP세션키                          
	private String	KVP_ENCDATA;												         //ISP암호화데이터		

	////// 자체창(가상계좌)
	private String LGD_METHOD;	//ASSIGN:할당, CHANGE:변경
	private String LGD_ACCOUNTOWNER;	//입금자명                          
	private String LGD_ACCOUNTPID;     //구매자 개인식별변호 (6자리~13자리)(옵션)     
	private String LGD_BUYERPHONE;     //구매자휴대폰번호	                  
	private String LGD_BANKCODE;			//입금계좌은행코드
	private String LGD_CASHRECEIPTYN;     	  //현금영수증발행여부
	private String LGD_CASHRECEIPTUSE;     	  //현금영수증 발행구분('1':소득공제, '2':지출증빙)       
	private String LGD_CASHCARDNUM;          //현금영수증 카드번호                               
	private String LGD_CLOSEDATE;            //입금 마감일                                   
	private String LGD_TAXFREEAMOUNT;        	  //면세금액
	
	
	////// Billing
	private String LGD_BUYERSSN;//인증요청자 생년월일 6자리 (YYMMDD) or 사업자번호 10자리
	private String LGD_CHECKSSNYN;//인증요청자 생년월일,사업자번호 일치 여부 확인 플래그 ( 'Y'이면 인증창에서 고객이 입력한 생년월일,사업자번호 일치여부를 확인합니다.)
	private String LGD_PAYWINDOWTYPE;		// 인증요청구분
	private String LGD_VERSION;// 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.
	private String LGD_BILLKEY;
	
	private String LGD_PIN;//비밀번호 앞2자리(옵션-주민번호를 넘기지 않으면 비밀번호도 체크 안함)
	private String LGD_PRIVATENO;//생년월일 6자리 (YYMMDD) or 사업자번호(옵션)
	
	private String LGD_TXNAME;	//빌링일때 CardAuth
	
	
	//부분취소
	private String LGD_CANCELAMOUNT;	//부분취소금액
	private String LGD_REMAINAMOUNT;	//남은금액
	private String LGD_CANCELREASON;	//취소사유
	private String LGD_RFBANKCODE;	//환불계좌 은행코드 (가상계좌만 필수)
	private String LGD_RFACCOUNTNUM;	//환불계좌 번호 (가상계좌만 필수)
	private String LGD_RFCUSTOMERNAME;	//환불계좌 예금주 (가상계좌만 필수)
	private String LGD_RFPHONE;	//요청자 연락처 (가상계좌만 필수)
	
	
	////// return //////
	private String LGD_RESPCODE;	
	private String LGD_RESPMSG;		

	private String LGD_TID;		//승인번호
	private String LGD_PAYTYPE;	//
	private String LGD_PAYDATE;
	private String LGD_FINANCECODE;
	private String LGD_FINANCENAME;
	private String LGD_ESCROWYN;
	private String LGD_ESCROW_USEYN;
	private String LGD_ACCOUNTNUM;
	private String LGD_CASTAMOUNT;
	private String LGD_CASCAMOUNT;
	private String LGD_CASFLAG;
	private String LGD_CASSEQNO;
	private String LGD_CASHRECEIPTNUM;
	private String LGD_CASHRECEIPTSELFYN;
	private String LGD_CASHRECEIPTKIND;
	private String LGD_PAYER;
	private String LGD_BUYERADDRESS;
	private String LGD_PRODUCTCODE;
	private String LGD_RECEIVER;
	private String LGD_RECEIVERPHONE;
	private String LGD_DELIVERYINFO;
	
	
	///// mobile //////
	private String CST_WINDOW_TYPE;
	private String LGD_MPILOTTEAPPCARDWAPURL;
	private String LGD_KVPMISPWAPURL;
	private String LGD_KVPMISPCANCELURL;
	private String LGD_MTRANSFERWAPURL;
	private String LGD_MTRANSFERCANCELURL;
	private String LGD_KVPMISPAUTOAPPYN;
	private String LGD_MTRANSFERAUTOAPPYN;
	
	private String LGD_DISABLE_AGREE;	//약관동의 미표시여부
	
	//거래종류(BANK,CAS,CR)
	private String TTYPE;		
	
	private List<OmsPaymentEscrow> omsPaymentEscrows;
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}