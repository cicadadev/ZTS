package gcp.external.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gcp.ccs.model.CcsMessage;
import gcp.ccs.model.CcsPolicy;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.PolicyService;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentEscrow;
import gcp.oms.model.OmsPaymentif;
import gcp.oms.model.pg.lgu.LguAccountCertify;
import gcp.oms.model.pg.lgu.LguBase;
import gcp.oms.model.pg.lgu.LguCancelPartial;
import gcp.oms.service.PaymentService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;
import lgdacom.XPayClient.XPayClient;

@Service("pgService")
public class PgService extends BaseService {
	private static final Logger	logger	= LoggerFactory.getLogger(PgService.class);

	@Autowired
	private PolicyService		policyService;

	@Autowired
	private PaymentService		paymentService;

	private Map<String, Object>	interestList;

	/**
	 * 
	 * @Method Name : savePayment
	 * @author : dennis
	 * @date : 2016. 8. 1.
	 * @description : 결제 INSERT
	 *
	 * @param payment
	 * @param xpay
	 * @throws Exception
	 */
	public void savePayment(OmsPayment payment, XPayClient xpay) throws Exception {
		payment.setPgShopId(xpay.Response("LGD_MID", 0));	//상점ID
		payment.setPgApprovalNo(xpay.Response("LGD_TID", 0));	//승인번호
		payment.setPaymentBusinessCd(xpay.Response("LGD_FINANCECODE", 0));	//결제사
		payment.setPaymentBusinessNm(xpay.Response("LGD_FINANCENAME", 0));	//결제사명
		payment.setPaymentDt(xpay.Response("LGD_PAYDATE", 0));	//결제일시

		String paymentMethodCd = payment.getPaymentMethodCd();
		
		/////////////// 카드 ////////////////////////
		payment.setCreditcardNo(xpay.Response("LGD_CARDNUM", 0));	//카드번호
		payment.setInterestFreeYn(nvl(xpay.Response("LGD_CARDNOINTYN", 0), "N"));	//무이자여부
		
		String installment = xpay.Response("LGD_CARDINSTALLMONTH", 0);

		//할부개월수
		if (CommonUtil.isNotEmpty(installment)) {
			payment.setInstallmentCnt(new BigDecimal(installment));
		} else {
			payment.setInstallmentCnt(new BigDecimal(0));
		}

		/////////////// 카드 ////////////////////////

		/////////////// 가상계좌 ////////////////////
		payment.setAccountNo(xpay.Response("LGD_ACCOUNTNUM", 0));	//계좌번호		
		payment.setDepositorName(xpay.Response("LGD_PAYER", 0));	//입금자명
		payment.setVirtualAccountDepositOrder(xpay.Response("LGD_CASSEQNO", 0));//가상계좌입금순서
//		payment.setVirtualAccountDepositEndDt(xpay.Response("LGD_CLOSEDATE", 0));
		/////////////// 가상계좌 ////////////////////

		/////////////// 계좌이체 ////////////////////
		payment.setAccountHolderName(xpay.Response("LGD_ACCOUNTOWNER", 0));	//계좌주		
		/////////////// 계좌이체 ////////////////////		

		/////////////// 휴대폰 ////////////////////
		payment.setMobilePhone(xpay.Response("LGD_TELNO", 0));	//휴대폰번호		
		/////////////// 휴대폰 ////////////////////		

		/////////////// 현금영수증 ////////////////////
		payment.setCashReceiptApprovalNo(xpay.Response("LGD_CASHRECEIPTNUM", 0));	//현금영수증승인번호.
		String cashKind = xpay.Response("LGD_CASHRECEIPTKIND", 0);
		String cashReceiptTypecd = "";
		if ("0".equals(cashKind)) {
			cashReceiptTypecd = "CASH_RECEIPT_TYPE_CD.PAY";	//소득공제
		} else if ("1".equals(cashKind)) {
			cashReceiptTypecd = "CASH_RECEIPT_TYPE_CD.COST";	//지출증빙 
		}
		payment.setCashReceiptTypeCd(cashReceiptTypecd);	//현금영수증종료
		/////////////// 현금영수증 ////////////////////

		//escrow
		String escrowYn = nvl(xpay.Response("LGD_ESCROWYN", 0), BaseConstants.YN_N);
		payment.setEscrowYn(escrowYn);	//ESCROW
		
		//부분취소가능여부
		String partialCancel = xpay.Response("LGD_PCANCELFLAG", 0);

		if("PAYMENT_METHOD_CD.CARD".equals(paymentMethodCd)){
			if ("0".equals(partialCancel)) {
				payment.setPartialCancelYn(BaseConstants.YN_N);
			} else if ("1".equals(partialCancel)) {
				payment.setPartialCancelYn(BaseConstants.YN_Y);
			} else {
				payment.setPartialCancelYn(BaseConstants.YN_N);
			}
		}else{
			//에스크로일때 부분취소 불가.
			if (BaseConstants.YN_Y.equals(escrowYn)) {
				payment.setPartialCancelYn(BaseConstants.YN_N);
			} else {
				payment.setPartialCancelYn(BaseConstants.YN_Y);
			}
		}
		
		payment.setEscrowIfYn(BaseConstants.YN_N);	//에스크로 연동여부

		dao.insertOneTable(payment);
	}

	/**
	 * 
	 * @Method Name : insertPaymentTransferNewTx
	 * @author : dennis
	 * @throws Exception
	 * @date : 2016. 7. 27.
	 * @description : 전문 insert
	 *
	 */
	public OmsPaymentif insertPaymentTransferNewTx(OmsPaymentif omsPaymentif) throws Exception {
		logger.debug("====================== request PG =====================");
		logger.debug(omsPaymentif.toString());
		logger.debug("====================== request PG =====================");
		
		omsPaymentif.setPaymentReqData(omsPaymentif.toString());
		dao.insertOneTable(omsPaymentif);		

		return omsPaymentif;
	}
	
	public OmsPaymentif insertPaymentTransferNewTx(OmsPaymentif omsPaymentif, Object object) throws Exception {
		logger.debug("====================== request PG =====================");
		logger.debug(object.toString());
		logger.debug("====================== request PG =====================");

		omsPaymentif.setPaymentReqData(object.toString());
		dao.insertOneTable(omsPaymentif);
		return omsPaymentif;
	}

	/**
	 * 
	 * @Method Name : updatePaymentTransferNewTx
	 * @author : dennis
	 * @date : 2016. 7. 27.
	 * @description : 전문 결과 update
	 *
	 * @param paymentNo
	 * @param xpay
	 * @throws Exception
	 */
	public void updatePaymentTransferNewTx(OmsPaymentif omsPaymentif, XPayClient xpay) throws Exception {

		StringBuffer response = new StringBuffer();
		response.append("[" + xpay.m_szResCode + "] " + xpay.m_szResMsg);
		logger.debug("====================== response PG =====================");
		logger.debug(xpayToString(xpay).toString());
		logger.debug("====================== response PG =====================");

		omsPaymentif.setPaymentIfResult(xpay.m_szResCode);
		omsPaymentif.setPaymentReturnData(xpayToString(xpay).toString());
		dao.updateOneTable(omsPaymentif);
	}

	/**
	 * 
	 * @Method Name : insertCasnoteTransferNewTx
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 무통장입금시 전문insert
	 *
	 * @param omsPaymentif
	 * @return
	 * @throws Exception
	 */
	public boolean insertCasnoteTransferNewTx(OmsPaymentif omsPaymentif) throws Exception {

		logger.debug("====================== casnote response PG =====================");
		logger.debug(omsPaymentif.toString());
		logger.debug("====================== casnote response PG =====================");

		OmsPayment payment = new OmsPayment();
		String oid = omsPaymentif.getLGD_OID();
		String tid = omsPaymentif.getLGD_TID();

		payment.setPaymentNo(new BigDecimal(oid));
		payment.setPgApprovalNo(tid);
		payment.setPaymentMethodCd("PAYMENT_METHOD_CD.VIRTUAL");

		payment = (OmsPayment) dao.selectOneTable(payment);

		if (CommonUtil.isEmpty(payment)) {
			return false;
		}
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());
		omsPaymentif.setPaymentReturnData(omsPaymentif.toString());

		dao.insertOneTable(omsPaymentif);

		return true;
	}

	/**
	 * 
	 * @Method Name : updateCasnotePayment
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 무통장입금 수신 처리.
	 *
	 * @param omsPaymentif
	 * @param type
	 * @throws Exception
	 */
	public void updateCasnotePayment(OmsPaymentif omsPaymentif, String type) throws Exception {
		OmsOrder omsOrder = new OmsOrder();
		omsOrder.setOrderId(omsPaymentif.getOrderId());

		if("R".equals(type)){	//요청
			omsOrder.setOrderStateCd("ORDER_STATE_CD.REQ");
		} else if ("I".equals(type)) {	//입금
			omsOrder.setOrderStateCd("ORDER_STATE_CD.PAYED");
		} else if ("C".equals(type)) {	//당일입금취소
			omsOrder.setOrderStateCd("ORDER_STATE_CD.CANCEL");
			omsOrder.setCancelDt(omsPaymentif.getLGD_PAYDATE());
		}

		Map<String, Object> result = paymentService.saveCasnote(omsOrder);
	}

	private StringBuffer xpayToString(XPayClient xpay) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (int i = 0; i < xpay.ResponseNameCount(); i++) {
			String response = "------------------------";
			response += xpay.ResponseName(i) + " = ";
			for (int j = 0; j < xpay.ResponseCount(); j++) {
				response += "\t" + xpay.Response(xpay.ResponseName(i), j) + "\n";
			}
			sb.append(response);
		}
		return sb;
	}

	public String getMertkey(String mid) {
		String result = "";
		if (Config.getString("pg.mid.00").equals(mid)) {
			return Config.getString("pg.mertkey.00");
		} else if (Config.getString("pg.mid.01").equals(mid)) {
			return Config.getString("pg.mertkey.01");
		} else if (Config.getString("pg.mid.10").equals(mid)) {
			return Config.getString("pg.mertkey.10");
		} else if (Config.getString("pg.mid.11").equals(mid)) {
			return Config.getString("pg.mertkey.11");
		} else if (Config.getString("pg.mid.20").equals(mid)) {
			return Config.getString("pg.mertkey.20");
		} else if (Config.getString("pg.mid.21").equals(mid)) {
			return Config.getString("pg.mertkey.21");
		} else if (Config.getString("pg.mid.30").equals(mid)) {
			return Config.getString("pg.mertkey.30");
		} else if (Config.getString("pg.mid.40").equals(mid)) {
			return Config.getString("pg.mertkey.40");
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : getRequestParam
	 * @author : dennis
	 * @date : 2016. 7. 27.
	 * @description : PG 요청 parameter 생성.
	 *
	 * @param omsPaymentif
	 * @return
	 * @throws Exception
	 */
	public OmsPaymentif getRequestParam(OmsPaymentif omsPaymentif) throws Exception {
		/*
		 * [결제 인증요청 페이지(STEP2-1)]
		 *
		 * 샘플페이지에서는 기본 파라미터만 예시되어 있으며, 별도로 필요하신 파라미터는 연동메뉴얼을 참고하시어 추가 하시기 바랍니다.
		 */

		/*
		 * 1. 기본결제 인증요청 정보 변경
		 *
		 * 기본정보를 변경하여 주시기 바랍니다.(파라미터 전달시 POST를 사용하세요)
		 */
		String CST_PLATFORM = Config.getString("pg.platform");                 //LG유플러스 결제서비스 선택(test:테스트, service:서비스)

		String CST_MID = Config.getString("pg.mid.00");                      //LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요.
		String LGD_MERTKEY = getMertkey(CST_MID);													//상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
																					//상점아이디(자동생성)
		String LGD_OID = omsPaymentif.getLGD_OID();                      //주문번호(상점정의 유니크한 주문번호를 입력하세요)
		String LGD_AMOUNT = omsPaymentif.getLGD_AMOUNT();                   //결제금액("," 를 제외한 결제금액을 입력하세요)
		String LGD_BUYER = omsPaymentif.getLGD_BUYER();                    //구매자명
		String LGD_PRODUCTINFO = omsPaymentif.getLGD_PRODUCTINFO();              //상품명
		String LGD_BUYEREMAIL = omsPaymentif.getLGD_BUYEREMAIL();               //구매자 이메일
		String LGD_TIMESTAMP = omsPaymentif.getLGD_TIMESTAMP();                //타임스탬프
//		String LGD_TIMESTAMP = (DateUtil.getCurrnetTimestamp()).toString();                //타임스탬프
//		String LGD_TIMESTAMP = "1234567890";
		String LGD_CUSTOM_USABLEPAY = omsPaymentif.getLGD_CUSTOM_USABLEPAY();          //상점정의 초기결제수단
		String LGD_CUSTOM_SKIN = Config.getString("pg.skin");                                                //상점정의 결제창 스킨(red, purple, yellow)
		String LGD_WINDOW_VER = Config.getString("pg.window.version");                                                //결제창 버젼정보
		String LGD_BUYERID = omsPaymentif.getLGD_BUYERID();       			//구매자 아이디
		String LGD_BUYERIP = omsPaymentif.getLGD_BUYERIP();       			//구매자IP

		String CST_WINDOW_TYPE = "";
		String LGD_VERSION = omsPaymentif.getLGD_VERSION(); // 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.
		String LGD_CUSTOM_PROCESSTYPE = omsPaymentif.getLGD_CUSTOM_PROCESSTYPE();	//자체창 : AUTHTR, 일반 : TWOTR
		//crossplatform	
		/*
		 * LGD_RETURNURL 을 설정하여 주시기 바랍니다. 반드시 현재 페이지와 동일한 프로트콜 및  호스트이어야 합니다. 아래 부분을 반드시 수정하십시요.
		 */
		String LGD_RETURNURL = Config.getString("pg.return.url");// FOR MANUAL

		String LGD_WINDOW_TYPE = Config.getString("pg.window.type");//수정불가

		String LGD_CUSTOM_SWITCHINGTYPE = Config.getString("pg.custom.switchingtype");	// 신용카드 카드사 인증 페이지 연동 방식

		//Billing
		String LGD_BUYERSSN = omsPaymentif.getLGD_BUYERSSN();				//인증요청자 생년월일 6자리 (YYMMDD) or 사업자번호 10자리
		String LGD_CHECKSSNYN = omsPaymentif.getLGD_CHECKSSNYN();			//인증요청자 생년월일,사업자번호 일치 여부 확인 플래그 ( 'Y'이면 인증창에서 고객이 입력한 생년월일,사업자번호 일치여부를 확인합니다.)

		//ENCODING
		String LGD_ENCODING = "UTF-8";
		String LGD_ENCODING_RETURNURL = "UTF-8";
		String LGD_ENCODING_NOTEURL = "UTF-8";
		
		//계좌이체 선택은행
		String LGD_USABLEBANK = omsPaymentif.getLGD_USABLEBANK();

		/*
		 * 가상계좌(무통장) 결제 연동을 하시는 경우 아래 LGD_CASNOTEURL 을 설정하여 주시기 바랍니다. 
		 */
		String LGD_CASNOTEURL = Config.getString("pg.casnote.url");
		String LGD_CLOSEDATE = "";

		String LGD_DISABLE_AGREE = "Y";	//약관 동의 미표시여부.

		//카드
		if ("SC0010".equals(LGD_CUSTOM_USABLEPAY)) {

		}
		//무통장
		else if ("SC0040".equals(LGD_CUSTOM_USABLEPAY)) {

			//가상계좌 입금기간 정책.
			CcsPolicy ccsPolicy = new CcsPolicy();
			ccsPolicy.setStoreId(omsPaymentif.getStoreId());
			ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_VIRTUAL_END_DT);
			ccsPolicy.setPolicyTypeCd(BaseConstants.POLICY_TYPE_CD_ORDER);
			ccsPolicy = policyService.getPolicy(ccsPolicy);

			if (CommonUtil.isNotEmpty(ccsPolicy)) {
				String closeDate = ccsPolicy.getValue();
				if (CommonUtil.isNotEmpty(closeDate)) {
					LGD_CLOSEDATE = DateUtil.getAddDate(DateUtil.FORMAT_3, DateUtil.getCurrentDate(DateUtil.FORMAT_3),
							new BigDecimal(closeDate));
				} else {	//정책 null이면 7일
					LGD_CLOSEDATE = DateUtil.getAddDate(DateUtil.FORMAT_3, DateUtil.getCurrentDate(DateUtil.FORMAT_3),
							new BigDecimal("7"));
				}
			}

		}
		//TEST를 위해 추가.(풀면 휴대폰 실결제됨)
		//휴대폰일때
//		else if ("SC0060".equals(LGD_CUSTOM_USABLEPAY)) {
//			CST_PLATFORM = Config.getString("pg.platform.service");
//			LGD_MID = CST_MID;
//		}

		////////////////////////////// MOBILE ////////////////////////////////////////
		/*
		* ISP 카드결제 연동을 위한 파라미터(필수)
		*/
		String LGD_KVPMISPWAPURL = "";
		String LGD_KVPMISPCANCELURL = "";

		String LGD_MPILOTTEAPPCARDWAPURL = ""; //iOS 연동시 필수

		/*
		* 계좌이체 연동을 위한 파라미터(필수)
		*/
		String LGD_MTRANSFERWAPURL = "";//Config.getString("pg.mtransfer.wapurl");
		String LGD_MTRANSFERCANCELURL = "";//Config.getString("pg.mtransfer.cancelurl");


		String LGD_KVPMISPAUTOAPPYN = "";	// 신용카드 결제 사용시 필수
		String LGD_MTRANSFERAUTOAPPYN = "";	// 계좌이체 결제 사용시 필수

		String LGD_OSTYPE_CHECK = "P";	//M:모바일, P:PC

		//////////////////////////////MOBILE ////////////////////////////////////////

		String channelId = SessionUtil.getChannelId();

		//용도별 상점id, mertkey 분기.
		String orderTypeCd = omsPaymentif.getOrderTypeCd();
		String deviceTypeCd = omsPaymentif.getDeviceTypeCd();
		if (BaseConstants.DEVICE_TYPE_CD_APP.equals(deviceTypeCd)) {	//app

			CST_WINDOW_TYPE = "submit";
			LGD_WINDOW_TYPE = "submit";
			LGD_CUSTOM_SKIN = Config.getString("pg.skin.mobile");
			LGD_CUSTOM_SWITCHINGTYPE = Config.getString("pg.custom.switchingtype.mobile");
			LGD_VERSION = "JSP_Non-ActiveX_SmartXPay";
			if ("SC0010".equals(LGD_CUSTOM_USABLEPAY)) {
				LGD_VERSION = "JSP_NonActiveX_Mobile_CardApp";
			}

			LGD_KVPMISPAUTOAPPYN = "A";
			LGD_MTRANSFERAUTOAPPYN = "A";

			if ("iPhone".equals(omsPaymentif.getMobileOs()) || "iPod".equals(omsPaymentif.getMobileOs())
					|| "iPad".equals(omsPaymentif.getMobileOs())) {
				LGD_KVPMISPAUTOAPPYN = "A";
				LGD_MTRANSFERAUTOAPPYN = "A";
			} else {
				LGD_KVPMISPAUTOAPPYN = "A";
				LGD_MTRANSFERAUTOAPPYN = "A";
			}

			String appUrl = "zerotoseven://open";
			LGD_KVPMISPWAPURL = appUrl;
			LGD_KVPMISPCANCELURL = appUrl;
			LGD_MPILOTTEAPPCARDWAPURL = appUrl; //iOS 연동시 필수

			/*
			 * 계좌이체 연동을 위한 파라미터(필수)
			 */
			LGD_MTRANSFERWAPURL = appUrl;
			LGD_MTRANSFERCANCELURL = appUrl;

			LGD_OSTYPE_CHECK = "M";

		} else if (BaseConstants.DEVICE_TYPE_CD_MW.equals(deviceTypeCd)) {	//mobile web

			CST_WINDOW_TYPE = "submit";
			LGD_WINDOW_TYPE = "submit";
			LGD_CUSTOM_SKIN = Config.getString("pg.skin.mobile");
			LGD_CUSTOM_SWITCHINGTYPE = Config.getString("pg.custom.switchingtype.mobile");
			LGD_VERSION = "JSP_Non-ActiveX_SmartXPay";
			if ("SC0010".equals(LGD_CUSTOM_USABLEPAY)) {
				LGD_VERSION = "JSP_NonActiveX_Mobile_CardApp";
			}

			/*
			****************************************************
			* 모바일 OS별 ISP(국민/비씨), 계좌이체 결제 구분 값
			****************************************************
			1) Web to Web
			- 안드로이드: A (디폴트)
			- iOS: N
			  ** iOS일 경우, 반드시 N으로 값을 수정
			2) App to Web(반드시 SmartXPay_AppToWeb_연동가이드를 참조합니다.)
			- 안드로이드, iOS: A
			*/
			if ("iPhone".equals(omsPaymentif.getMobileOs()) || "iPod".equals(omsPaymentif.getMobileOs())) {
				LGD_KVPMISPAUTOAPPYN = "N";
				LGD_MTRANSFERAUTOAPPYN = "N";
			} else {
				LGD_KVPMISPAUTOAPPYN = "A";
				LGD_MTRANSFERAUTOAPPYN = "A";
			}

			LGD_OSTYPE_CHECK = "M";
		} else if (BaseConstants.DEVICE_TYPE_CD_PC.equals(deviceTypeCd)) {	//pc			

		}

		//정기배송 아니고 test일때
//		if (!"ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd) && "test".equals(CST_PLATFORM.trim())) {
//			CST_MID = Config.getString("pg.mid.common");
//		}
		CST_MID = getMID(orderTypeCd, deviceTypeCd, channelId);
		LGD_MERTKEY = getMertkey(CST_MID);

		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "") + CST_MID;  //테스트 아이디는 't'를 제외하고 입력하세요.

		/*
		 *************************************************
		 * 2. MD5 해쉬암호화 (수정하지 마세요) - BEGIN
		 *
		 * MD5 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
		 *************************************************
		 *
		 * 해쉬 암호화 적용( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP + LGD_MERTKEY )
		 * LGD_MID          : 상점아이디
		 * LGD_OID          : 주문번호
		 * LGD_AMOUNT       : 금액
		 * LGD_TIMESTAMP    : 타임스탬프
		 * LGD_MERTKEY      : 상점MertKey (mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
		 *
		 * MD5 해쉬데이터 암호화 검증을 위해
		 * LG유플러스에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
		 */
		StringBuffer sb = new StringBuffer();
		sb.append(LGD_MID);
		sb.append(LGD_OID);
		sb.append(LGD_AMOUNT);
		sb.append(LGD_TIMESTAMP);
		sb.append(LGD_MERTKEY);
		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}
		String LGD_HASHDATA = strBuf.toString();
		/*
		 *************************************************
		 * 2. MD5 해쉬암호화 (수정하지 마세요) - END
		 *************************************************
		 */

		omsPaymentif.setCST_MID(CST_MID);
		omsPaymentif.setLGD_MID(LGD_MID);
		omsPaymentif.setCST_PLATFORM(CST_PLATFORM);
//		omsPaymentif.setLGD_MERTKEY(LGD_MERTKEY);
		omsPaymentif.setLGD_VERSION(LGD_VERSION);
		omsPaymentif.setCST_WINDOW_TYPE(CST_WINDOW_TYPE);
		omsPaymentif.setLGD_TIMESTAMP(LGD_TIMESTAMP);
		omsPaymentif.setLGD_CUSTOM_SKIN(LGD_CUSTOM_SKIN);
		omsPaymentif.setLGD_WINDOW_VER(LGD_WINDOW_VER);
		omsPaymentif.setLGD_RETURNURL(LGD_RETURNURL);
		omsPaymentif.setLGD_CASNOTEURL(LGD_CASNOTEURL);
		omsPaymentif.setLGD_HASHDATA(LGD_HASHDATA);
		omsPaymentif.setLGD_CUSTOM_PROCESSTYPE(LGD_CUSTOM_PROCESSTYPE);
		omsPaymentif.setLGD_WINDOW_TYPE(LGD_WINDOW_TYPE);
		omsPaymentif.setLGD_CUSTOM_SWITCHINGTYPE(LGD_CUSTOM_SWITCHINGTYPE);
		omsPaymentif.setLGD_CLOSEDATE(LGD_CLOSEDATE);
		omsPaymentif.setLGD_ENCODING(LGD_ENCODING);
		omsPaymentif.setLGD_ENCODING_RETURNURL(LGD_ENCODING_RETURNURL);
		omsPaymentif.setLGD_ENCODING_NOTEURL(LGD_ENCODING_NOTEURL);

		//////////////// mobile //////////////
		omsPaymentif.setLGD_MPILOTTEAPPCARDWAPURL(LGD_MPILOTTEAPPCARDWAPURL);
		omsPaymentif.setLGD_KVPMISPWAPURL(LGD_KVPMISPWAPURL);
		omsPaymentif.setLGD_KVPMISPCANCELURL(LGD_KVPMISPCANCELURL);
		omsPaymentif.setLGD_MTRANSFERWAPURL(LGD_MTRANSFERWAPURL);
		omsPaymentif.setLGD_MTRANSFERCANCELURL(LGD_MTRANSFERCANCELURL);
		omsPaymentif.setLGD_KVPMISPAUTOAPPYN(LGD_KVPMISPAUTOAPPYN);
		omsPaymentif.setLGD_MTRANSFERAUTOAPPYN(LGD_MTRANSFERAUTOAPPYN);

		omsPaymentif.setLGD_DISABLE_AGREE(LGD_DISABLE_AGREE);
		omsPaymentif.setLGD_OSTYPE_CHECK(LGD_OSTYPE_CHECK);

		logger.debug("===================== PG PARAM =================");
		logger.debug(omsPaymentif.toString());
		return omsPaymentif;
	}

	public String getMID(String orderTypeCd, String deviceTypeCd, String channelId) {
		String CST_MID = "";
		if (BaseConstants.DEVICE_TYPE_CD_APP.equals(deviceTypeCd)) {	//app

			if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
				//정기배송 (X)
				CST_MID = Config.getString("pg.mid.21");
			} else if (CommonUtil.isNotEmpty(channelId)) {
				//제휴 (X)
				CST_MID = Config.getString("pg.mid.20");
			} else {
				//APP
				CST_MID = Config.getString("pg.mid.20");
			}

		} else if (BaseConstants.DEVICE_TYPE_CD_MW.equals(deviceTypeCd)) {	//mobile web

			if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
				//정기배송
				CST_MID = Config.getString("pg.mid.11");
			} else if (CommonUtil.isNotEmpty(channelId)) {
				//제휴몰
				CST_MID = Config.getString("pg.mid.40");
			} else {
				//Mobile Web
				CST_MID = Config.getString("pg.mid.10");
			}

		} else if (BaseConstants.DEVICE_TYPE_CD_PC.equals(deviceTypeCd)) {	//pc
			if ("ORDER_TYPE_CD.REGULARDELIVERY".equals(orderTypeCd)) {
				//기타 제휴몰 정기결제 (01)
				CST_MID = Config.getString("pg.mid.01");
			} else if (CommonUtil.isNotEmpty(channelId)) {
				//기타 제휴몰 (30)
				CST_MID = Config.getString("pg.mid.30");
			} else {
				//일반 Web
				CST_MID = Config.getString("pg.mid.00");
			}

		}
		return CST_MID;
	}

	public Map<String, Object> payres(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsPaymentif omsPaymentif = payment.getOmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());

		if (!omsPaymentif.getLGD_OID().equals(payment.getPaymentNo().toString())) {
			logger.debug("LGD_OID : " + omsPaymentif.getLGD_OID());
			logger.debug("paymentNo : " + payment.getPaymentNo());
			result.put(BaseConstants.RESULT_MESSAGE, "결제번호 불일치!");
			return result;
		}

		//전문 insert
		omsPaymentif = ((PgService) getThis()).insertPaymentTransferNewTx(omsPaymentif);
		PrintStream out = System.out;

		/*
		 * [최종결제요청 페이지(STEP2-2)]
		 *
		 * 매뉴얼 "5.1. XPay 결제 요청 페이지 개발"의 "단계 5. 최종 결제 요청 및 요청 결과 처리" 참조
		 *
		 * LG유플러스으로 부터 내려받은 LGD_PAYKEY(인증Key)를 가지고 최종 결제요청.(파라미터 전달시 POST를 사용하세요)
		 */

		/* ※ 중요
		* 환경설정 파일의 경우 반드시 외부에서 접근이 가능한 경로에 두시면 안됩니다.
		* 해당 환경파일이 외부에 노출이 되는 경우 해킹의 위험이 존재하므로 반드시 외부에서 접근이 불가능한 경로에 두시기 바랍니다. 
		* 예) [Window 계열] C:\inetpub\wwwroot\lgdacom ==> 절대불가(웹 디렉토리)
		*/
		String configPath = Config.getString("pg.config.path");  //LG유플러스에서 제공한 환경파일("/conf/lgdacom.conf,/conf/mall.conf") 위치 지정.

		/*
		 *************************************************
		 * 1.최종결제 요청 - BEGIN
		 *  (단, 최종 금액체크를 원하시는 경우 금액체크 부분 주석을 제거 하시면 됩니다.)
		 *************************************************
		 */

		String CST_PLATFORM = Config.getString("pg.platform");
		String CST_MID = omsPaymentif.getCST_MID();
		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "") + CST_MID;
		String LGD_PAYKEY = omsPaymentif.getLGD_PAYKEY();
		String LGD_ESCROW_USEYN = omsPaymentif.getLGD_ESCROW_USEYN();
		String LGD_CASHRECEIPTYN = omsPaymentif.getLGD_CASHRECEIPTYN();

		//해당 API를 사용하기 위해 WEB-INF/lib/XPayClient.jar 를 Classpath 로 등록하셔야 합니다.
		// (1) XpayClient의 사용을 위한 xpay 객체 생성
		XPayClient xpay = new XPayClient();

		// (2) Init: XPayClient 초기화(환경설정 파일 로드) 
		// configPath: 설정파일
		// CST_PLATFORM: - test, service 값에 따라 lgdacom.conf의 test_url(test) 또는 url(srvice) 사용
		//				- test, service 값에 따라 테스트용 또는 서비스용 아이디 생성
		boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

		if (!isInitOK) {
			//API 초기화 실패 화면처리
			out.println("결제요청을 초기화 하는데 실패하였습니다.<br>");
			out.println("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
			out.println("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
			out.println("문의전화 LG유플러스 1544-7772<br>");
			result.put(BaseConstants.RESULT_MESSAGE, "PG 환경설정 오류.");

		} else {
			try {
				/*
				 *************************************************
				 * 1.최종결제 요청(수정하지 마세요) - END
				 *************************************************
				 */
				xpay.Init_TX(LGD_MID);
				xpay.Set("LGD_TXNAME", "PaymentByKey");
				xpay.Set("LGD_PAYKEY", LGD_PAYKEY);

				//금액을 체크하시기 원하는 경우 아래 주석을 풀어서 이용하십시요.
				//String DB_AMOUNT = "DB나 세션에서 가져온 금액"; //반드시 위변조가 불가능한 곳(DB나 세션)에서 금액을 가져오십시요.
				//xpay.Set("LGD_AMOUNTCHECKYN", "Y");
				//xpay.Set("LGD_AMOUNT", DB_AMOUNT);

			} catch (Exception e) {
				out.println("LG유플러스 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
				out.println("" + e.getMessage());
				result.put(BaseConstants.RESULT_MESSAGE, "PG 환경설정 오류.");
			}
		}

		//escrow data
//		if ("Y".equals(LGD_ESCROW_USEYN)) {
//			xpay.Set("LGD_ESCROW_USEYN", LGD_ESCROW_USEYN);
//			setEscrowParam(xpay, payment);
//		}

		/*
		 *************************************************
		 * 1.최종결제 요청(수정하지 마세요) - END
		 *************************************************
		 */

		/*
		 * 2. 최종결제 요청 결과처리
		 *
		 * 최종 결제요청 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
		 */
		// (4) TX: lgdacom.conf에 설정된 URL로 소켓 통신하여 최종 인증요청, 결과값으로 true, false 리턴
		if (xpay.TX()) {
			//1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
			out.println("결제요청이 완료되었습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			out.println("거래번호 : " + xpay.Response("LGD_TID", 0) + "<br>");
			out.println("상점아이디 : " + xpay.Response("LGD_MID", 0) + "<br>");
			out.println("상점주문번호 : " + xpay.Response("LGD_OID", 0) + "<br>");
			out.println("결제금액 : " + xpay.Response("LGD_AMOUNT", 0) + "<br>");
			out.println("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
			out.println("결과메세지 : " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

			for (int i = 0; i < xpay.ResponseNameCount(); i++) {
				out.println(xpay.ResponseName(i) + " = ");
				for (int j = 0; j < xpay.ResponseCount(); j++) {
					out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
				}
			}
			out.println("<p>");

			if ("0000".equals(xpay.m_szResCode)) {
				//최종결제요청 결과 성공 DB처리
				out.println("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");

				boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.

				try {
					this.savePayment(payment, xpay);
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
				} catch (Exception e) {
					isDBOK = false;
				} finally {
					//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
					if (!isDBOK) {
						xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" + xpay.Response("LGD_TID", 0) + ",MID:"
								+ xpay.Response("LGD_MID", 0) + ",OID:" + xpay.Response("LGD_OID", 0) + "]");

						out.println("TX Rollback Response_code = " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
						out.println("TX Rollback Response_msg = " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

						if ("0000".equals(xpay.m_szResCode)) {
							out.println("자동취소가 정상적으로 완료 되었습니다.<br>");
						} else {
							out.println("자동취소가 정상적으로 처리되지 않았습니다.<br>");
						}
						((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
						result.put(BaseConstants.RESULT_MESSAGE, xpay.Response("LGD_RESPMSG", 0));
					}
				}


			} else {
				//최종결제요청 결과 실패 DB처리
				out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
				((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
				result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
			}
		} else {
			//2)API 요청실패 화면처리
			out.println("결제요청이 실패하였습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			//최종결제요청 결과 실패 DB처리
			out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");

			((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);

			result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);

		}

		return result;
	}


	/**
	 * Null 문자 체크
	 * 
	 * @param str
	 * @return
	 */
	public static String nvl(String str) {
		return nvl(str, "");
	}

	/**
	 * Null 문자 체크
	 * 
	 * @param str
	 * @param replacer
	 * @return
	 */
	public static String nvl(String str, String replacer) {
		if (str == null || "".equals(str))
			return replacer;
		else
			return str;
	}

	/**
	 * 
	 * @Method Name : cardRes
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 카드결제
	 *
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> cardRes(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsPaymentif omsPaymentif = payment.getOmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());

		if (!omsPaymentif.getLGD_OID().equals(payment.getPaymentNo().toString())) {
			logger.debug("LGD_OID : " + omsPaymentif.getLGD_OID());
			logger.debug("paymentNo : " + payment.getPaymentNo());
			result.put(BaseConstants.RESULT_MESSAGE, "결제번호 불일치!");
			return result;
		}

		//전문 insert
		omsPaymentif = ((PgService) getThis()).insertPaymentTransferNewTx(omsPaymentif);
		PrintStream out = System.out;
		/*
		 * [결제승인 요청 페이지]
		 *
		 * 파라미터 전달시 POST를 사용하세요.
		 */

		String CST_PLATFORM = Config.getString("pg.platform");     		//LG유플러스 결제 서비스 선택(test:테스트, service:서비스)
		String CST_MID = nvl(omsPaymentif.getCST_MID());           		//상점아이디(LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요) 테스트 아이디는 't'를 제외하고 입력하세요.
		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "") + CST_MID;  	//상점아이디(자동생성)    
		String LGD_OID = nvl(omsPaymentif.getLGD_OID());           		//주문번호(상점정의 유니크한 주문번호를 입력하세요)
		String LGD_AMOUNT = nvl(omsPaymentif.getLGD_AMOUNT());        		//결제금액("," 를 제외한 결제금액을 입력하세요)
		String LGD_BUYER = nvl(omsPaymentif.getLGD_BUYER());				//구매자명
		String LGD_BUYEREMAIL = nvl(omsPaymentif.getLGD_BUYEREMAIL());			//구매자 이메일
		String LGD_PRODUCTCODE = nvl(omsPaymentif.getLGD_PRODUCTCODE());			  //상품id
		String LGD_PRODUCTINFO = nvl(omsPaymentif.getLGD_PRODUCTINFO());			//상품명
		String LGD_AUTHTYPE = nvl(omsPaymentif.getLGD_AUTHTYPE());        		//인증유형(ISP인경우만  'ISP')
		String LGD_CARDTYPE = nvl(omsPaymentif.getLGD_CARDTYPE());        		//카드사코드
		String LGD_CURRENCY = nvl(omsPaymentif.getLGD_CURRENCY());        		//통화코드

		//안심클릭 또는 해외카드
		String LGD_PAN = nvl(omsPaymentif.getLGD_PAN());           	//카드번호    
		String LGD_INSTALL = nvl(omsPaymentif.getLGD_INSTALL());       	//할부개월수
		String LGD_NOINT = nvl(omsPaymentif.getLGD_NOINT());        	//무이자할부여부('1':상점부담무이자할부,'0':일반할부)    
		String LGD_EXPYEAR = nvl(omsPaymentif.getLGD_EXPYEAR());       	//유효기간년(YY)
		String LGD_EXPMON = nvl(omsPaymentif.getLGD_EXPMON());        	//유효기간월 (MM)
		String VBV_ECI = nvl(omsPaymentif.getVBV_ECI());    			//안심클릭ECI
		String VBV_CAVV = nvl(omsPaymentif.getVBV_CAVV());    		//안심클릭CAVV
		String VBV_XID = nvl(omsPaymentif.getVBV_XID());    			//안심클릭XID
		String VBV_JOINCODE = nvl(omsPaymentif.getVBV_JOINCODE());    	//안심클릭JOINCODE

		//ISP
		String KVP_QUOTA = nvl(omsPaymentif.getKVP_QUOTA());			//할부개월수
		String KVP_NOINT = nvl(omsPaymentif.getKVP_NOINT());			//무이자할부여부('1':상점부담무이자할부,'0':일반할부)
		String KVP_CARDCODE = nvl(omsPaymentif.getKVP_CARDCODE());		//ISP카드코드
		String KVP_SESSIONKEY = nvl(omsPaymentif.getKVP_SESSIONKEY());		//ISP세션키
		String KVP_ENCDATA = nvl(omsPaymentif.getKVP_ENCDATA());	 	//ISP암호화데이터
		
		//card billing
		String LGD_PIN = nvl(omsPaymentif.getLGD_PIN());        		//비밀번호 앞2자리(옵션-주민번호를 넘기지 않으면 비밀번호도 체크 안함)
		String LGD_PRIVATENO = nvl(omsPaymentif.getLGD_PRIVATENO());     	//생년월일 6자리 (YYMMDD) or 사업자번호(옵션)
		String LGD_BUYERPHONE = nvl(omsPaymentif.getLGD_BUYERPHONE());	//고객휴대폰번호 sms발송에 사용.
		String LGD_BUYERID = nvl(omsPaymentif.getLGD_BUYERID());

		String LGD_BUYERIP = nvl(omsPaymentif.getLGD_BUYERIP());

		String configPath = Config.getString("pg.config.path"); 						 				//LG유플러스에서 제공한 환경파일(/conf/lgdacom.conf, /conf/mall.conf)이 위치한 디렉토리 지정 		

		XPayClient xpay = new XPayClient();
		boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

		if (!isInitOK) {
			//API 초기화 실패 화면처리
			out.println("결제요청을 초기화 하는데 실패하였습니다.<br>");
			out.println("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
			out.println("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
			out.println("문의전화 LG유플러스 1544-7772<br>");
			result.put(BaseConstants.RESULT_MESSAGE, "PG 환경설정 오류.");
		}

		xpay.Init_TX(LGD_MID);

		xpay.Log("결제요청을 시작합니다.");

		xpay.Set("LGD_TXNAME", "CardAuth");
		xpay.Set("LGD_OID", LGD_OID);
		xpay.Set("LGD_AMOUNT", LGD_AMOUNT);
		xpay.Set("LGD_BUYER", LGD_BUYER);
		xpay.Set("LGD_PRODUCTCODE", LGD_PRODUCTCODE);
		xpay.Set("LGD_PRODUCTINFO", LGD_PRODUCTINFO);
		xpay.Set("LGD_BUYEREMAIL", LGD_BUYEREMAIL);
		xpay.Set("LGD_BUYERIP", LGD_BUYERIP);
		xpay.Set("LGD_AUTHTYPE", LGD_AUTHTYPE);
		xpay.Set("LGD_CARDTYPE", LGD_CARDTYPE);
		xpay.Set("LGD_CURRENCY", LGD_CURRENCY);

		if (VBV_ECI.equals("010")) { //키인방식인 경우에만 해당(정기결제)
			//필수
			xpay.Set("LGD_PAN", LGD_PAN);
			xpay.Set("LGD_INSTALL", LGD_INSTALL);
			xpay.Set("VBV_ECI", VBV_ECI);

			//옵션
			xpay.Set("LGD_BUYERPHONE", LGD_BUYERPHONE);
			xpay.Set("LGD_BUYERID", LGD_BUYERID);

			//불필요
			xpay.Set("LGD_EXPYEAR", LGD_EXPYEAR);
			xpay.Set("LGD_EXPMON", LGD_EXPMON);
			xpay.Set("LGD_PIN", LGD_PIN);
			xpay.Set("LGD_PRIVATENO", LGD_PRIVATENO);
		} else {

			if (LGD_AUTHTYPE.equals("ISP")) {
				xpay.Set("KVP_QUOTA", KVP_QUOTA);
				xpay.Set("KVP_NOINT", KVP_NOINT);
				xpay.Set("KVP_CARDCODE", KVP_CARDCODE);
				xpay.Set("KVP_SESSIONKEY", KVP_SESSIONKEY);
				xpay.Set("KVP_ENCDATA", KVP_ENCDATA);
			} else {
				xpay.Set("LGD_PAN", LGD_PAN);
				xpay.Set("LGD_INSTALL", LGD_INSTALL);
				xpay.Set("LGD_NOINT", LGD_NOINT);
				xpay.Set("LGD_EXPYEAR", LGD_EXPYEAR);
				xpay.Set("LGD_EXPMON", LGD_EXPMON);
				xpay.Set("VBV_ECI", VBV_ECI);
				xpay.Set("VBV_CAVV", VBV_CAVV);
				xpay.Set("VBV_XID", VBV_XID);
				xpay.Set("VBV_JOINCODE", VBV_JOINCODE);
			}
		}


		if (xpay.TX()) {
			//1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
			out.println("결제요청이 완료되었습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			out.println("거래번호 : " + xpay.Response("LGD_TID", 0) + "<br>");
			out.println("상점아이디 : " + xpay.Response("LGD_MID", 0) + "<br>");
			out.println("상점주문번호 : " + xpay.Response("LGD_OID", 0) + "<br>");
			out.println("결제금액 : " + xpay.Response("LGD_AMOUNT", 0) + "<br>");
			out.println("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
			out.println("결과메세지 : " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

			//아래는 결제요청 결과 파라미터를 모두 찍어 줍니다.
			for (int i = 0; i < xpay.ResponseNameCount(); i++) {
				out.println(xpay.ResponseName(i) + " = ");
				for (int j = 0; j < xpay.ResponseCount(); j++) {
					out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
				}
			}
			out.println("<p>");

			if ("0000".equals(xpay.m_szResCode)) {
				//최종결제요청 결과 성공 DB처리
				out.println("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");

				//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
				boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.
				try {
					this.savePayment(payment, xpay);
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
				} catch (Exception e) {
					isDBOK = false;
				} finally {
					if (!isDBOK) {
						xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" + xpay.Response("LGD_TID", 0) + ",MID:"
								+ xpay.Response("LGD_MID", 0) + ",OID:" + xpay.Response("LGD_OID", 0) + "]");
						
						out.println("TX Rollback Response_code = " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
						out.println("TX Rollback Response_msg = " + xpay.Response("LGD_RESPMSG", 0) + "<p>");
						
						if ("0000".equals(xpay.m_szResCode)) {
							out.println("자동취소가 정상적으로 완료 되었습니다.<br>");
						} else {
							out.println("자동취소가 정상적으로 처리되지 않았습니다.<br>");
						}
						((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
						result.put(BaseConstants.RESULT_MESSAGE, xpay.Response("LGD_RESPMSG", 0));
					}
				}
			} else {
				//최종결제요청 결과 실패 DB처리
				out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
				((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
				result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
			}
		} else {
			//2)API 요청실패 화면처리
			out.println("결제요청이 실패하였습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			//최종결제요청 결과 실패 DB처리
			out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
			((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
			result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
		}

		xpay.Log("결제요청을 종료합니다.");
		return result;
	}

	/**
	 * 
	 * @Method Name : cyberRes
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 가상계좌 결제
	 *
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> cyberRes(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsPaymentif omsPaymentif = payment.getOmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());

		if (!omsPaymentif.getLGD_OID().equals(payment.getPaymentNo().toString())) {
			logger.debug("LGD_OID : " + omsPaymentif.getLGD_OID());
			logger.debug("paymentNo : " + payment.getPaymentNo());
			result.put(BaseConstants.RESULT_MESSAGE, "결제번호 불일치!");
			return result;
		}

		//전문 insert
		omsPaymentif = ((PgService) getThis()).insertPaymentTransferNewTx(omsPaymentif);

		PrintStream out = System.out;
		/*
		 * [가상계좌 발급/변경요청 페이지]
		 *
		 *
		 * 가상계좌 발급 변경(CHANGE)은 금액과 마감일만 변경 할수 있습니다. 
		 */

		/* 필수 */
		String CST_PLATFORM = Config.getString("pg.platform");                 //LG유플러스 결제서비스 선택(test:테스트, service:서비스)
		String CST_MID = omsPaymentif.getCST_MID();                      //LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요.
		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "") + CST_MID;  //테스트 아이디는 't'를 제외하고 입력하세요.
																					//상점아이디(자동생성)          
		String LGD_METHOD = omsPaymentif.getLGD_METHOD();                   //ASSIGN:할당, CHANGE:변경
		String LGD_OID = omsPaymentif.getLGD_OID();					  //주문번호(상점정의 유니크한 주문번호를 입력하세요)
		String LGD_AMOUNT = omsPaymentif.getLGD_AMOUNT();             	  //금액("," 를 제외한 금액을 입력하세요)
		String LGD_PRODUCTCODE = omsPaymentif.getLGD_PRODUCTCODE();			  //상품id
		String LGD_PRODUCTINFO = omsPaymentif.getLGD_PRODUCTINFO();			  //상품정보
		String LGD_BUYER = omsPaymentif.getLGD_BUYER();			  		  //구매자명
		String LGD_ACCOUNTOWNER = omsPaymentif.getLGD_ACCOUNTOWNER();			  //입금자명
		String LGD_ACCOUNTPID = omsPaymentif.getLGD_ACCOUNTPID();			  	  //구매자 개인식별변호 (6자리~13자리)(옵션)
		String LGD_BUYERPHONE = omsPaymentif.getLGD_BUYERPHONE();			  	  //구매자휴대폰번호	
		String LGD_BUYEREMAIL = omsPaymentif.getLGD_BUYEREMAIL();			  	  //구매자이메일(옵션)

		String LGD_BANKCODE = omsPaymentif.getLGD_BANKCODE();				  //입금계좌은행코드
		String LGD_CASHRECEIPTUSE = omsPaymentif.getLGD_CASHRECEIPTUSE();			  //현금영수증 발행구분('1':소득공제, '2':지출증빙)
		String LGD_CASHCARDNUM = omsPaymentif.getLGD_CASHCARDNUM();			  //현금영수증 카드번호
		String LGD_CLOSEDATE = omsPaymentif.getLGD_CLOSEDATE();				  //입금 마감일
		String LGD_TAXFREEAMOUNT = omsPaymentif.getLGD_TAXFREEAMOUNT();		  	  //면세금액
		String LGD_CASNOTEURL = omsPaymentif.getLGD_CASNOTEURL();    		      	  //입금결과 처리를 위한 상점페이지를 반드시 설정해 주세요.

		String LGD_ESCROW_USEYN = omsPaymentif.getLGD_ESCROW_USEYN();

		String configPath = Config.getString("pg.config.path");  										  //LG유플러스에서 제공한 환경파일("/conf/lgdacom.conf") 위치 지정.

		LGD_METHOD = (LGD_METHOD == null) ? "" : LGD_METHOD;
		LGD_OID = (LGD_OID == null) ? "" : LGD_OID;
		LGD_AMOUNT = (LGD_AMOUNT == null) ? "" : LGD_AMOUNT;
		LGD_PRODUCTCODE = (LGD_PRODUCTCODE == null) ? "" : LGD_PRODUCTCODE;
		LGD_PRODUCTINFO = (LGD_PRODUCTINFO == null) ? "" : LGD_PRODUCTINFO;

		LGD_BANKCODE = (LGD_BANKCODE == null) ? "" : LGD_BANKCODE;
		LGD_CASHRECEIPTUSE = (LGD_CASHRECEIPTUSE == null) ? "" : LGD_CASHRECEIPTUSE;
		LGD_CASHCARDNUM = (LGD_CASHCARDNUM == null) ? "" : LGD_CASHCARDNUM;
		LGD_CLOSEDATE = (LGD_CLOSEDATE == null) ? "" : LGD_CLOSEDATE;
		LGD_CASNOTEURL = (LGD_CASNOTEURL == null) ? "" : LGD_CASNOTEURL;
		LGD_TAXFREEAMOUNT = (LGD_TAXFREEAMOUNT == null) ? "" : LGD_TAXFREEAMOUNT;

		LGD_BUYER = (LGD_BUYER == null) ? "" : LGD_BUYER;
		LGD_ACCOUNTOWNER = (LGD_ACCOUNTOWNER == null) ? "" : LGD_ACCOUNTOWNER;
		LGD_ACCOUNTPID = (LGD_ACCOUNTPID == null) ? "" : LGD_ACCOUNTPID;
		LGD_BUYERPHONE = (LGD_BUYERPHONE == null) ? "" : LGD_BUYERPHONE;
		LGD_BUYEREMAIL = (LGD_BUYEREMAIL == null) ? "" : LGD_BUYEREMAIL;


		XPayClient xpay = new XPayClient();
		xpay.Init(configPath, CST_PLATFORM);
		xpay.Init_TX(LGD_MID);
		xpay.Set("LGD_TXNAME", "CyberAccount");
		xpay.Set("LGD_METHOD", LGD_METHOD);
		xpay.Set("LGD_OID", LGD_OID);
		xpay.Set("LGD_AMOUNT", LGD_AMOUNT);
		xpay.Set("LGD_PRODUCTCODE", LGD_PRODUCTCODE);
		xpay.Set("LGD_PRODUCTINFO", LGD_PRODUCTINFO);
		xpay.Set("LGD_BANKCODE", LGD_BANKCODE);
//		xpay.Set("LGD_CASHRECEIPTYN", LGD_CASHRECEIPTYN);
		xpay.Set("LGD_CASHRECEIPTUSE", LGD_CASHRECEIPTUSE);
		xpay.Set("LGD_CASHCARDNUM", LGD_CASHCARDNUM);
		xpay.Set("LGD_CLOSEDATE", LGD_CLOSEDATE);
		xpay.Set("LGD_CASNOTEURL", LGD_CASNOTEURL);
		xpay.Set("LGD_TAXFREEAMOUNT", LGD_TAXFREEAMOUNT);
		xpay.Set("LGD_BUYER", LGD_BUYER);
		xpay.Set("LGD_ACCOUNTOWNER", LGD_ACCOUNTOWNER);
		xpay.Set("LGD_ACCOUNTPID", LGD_ACCOUNTPID);
		xpay.Set("LGD_BUYERPHONE", LGD_BUYERPHONE);
		xpay.Set("LGD_BUYEREMAIL", LGD_BUYEREMAIL);
		xpay.Set("LGD_ESCROW_USEYN", LGD_ESCROW_USEYN);

//		if ("Y".equals(LGD_ESCROW_USEYN)) {
//			setEscrowParam(xpay, payment);
//		}


		/*
		 * 1. 가상계좌 발급/변경 요청 결과처리
		 *
		 * 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
		 */
		if (xpay.TX()) {
			if (LGD_METHOD.equals("ASSIGN")) { //가상계좌 발급의 경우

				//1)가상계좌 발급/변경결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
				out.println("가상계좌 발급 요청처리가 완료되었습니다.  <br>");
				out.println("TX Response_code = " + xpay.m_szResCode + "<br>");
				out.println("TX Response_msg = " + xpay.m_szResMsg + "<p>");

				out.println("거래번호 : " + xpay.Response("LGD_TID", 0) + "<br>");
				out.println("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<p>");

				for (int i = 0; i < xpay.ResponseNameCount(); i++) {
					out.println(xpay.ResponseName(i) + " = ");
					for (int j = 0; j < xpay.ResponseCount(); j++) {
						out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
					}
				}
				if ("0000".equals(xpay.m_szResCode)) {

					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					payment.setVirtualAccountDepositEndDt(omsPaymentif.getLGD_CLOSEDATE());	//가상계좌 입금마감일
					this.savePayment(payment, xpay);

					result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

				} else {
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
				}

			} else {		//가상계좌 변경의 경우

				//가상계좌 변경결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
				out.println("가상계좌 변경 요청처리가 완료되었습니다.  <br>");
				out.println("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
				out.println("주문번호 : " + LGD_OID + "<br>");
				out.println("입금액 : " + LGD_AMOUNT + "<br>");
				out.println("입금마감일 : " + LGD_CLOSEDATE + "<p>");

				if ("0000".equals(xpay.m_szResCode)) {
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					BigDecimal paymentNo = omsPaymentif.getPaymentNo();

					payment.setPaymentNo(paymentNo);
					this.savePayment(payment, xpay);
					result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
				} else {
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
				}
			}

//			out.println("<p>");

		} else {
			//3)API 요청 실패 화면처리
			out.println("가상계좌 발급/변경 요청처리가 실패되었습니다.  <br>");
			out.println("TX Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX Response_msg = " + xpay.m_szResMsg + "<p>");
			((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
			result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : casnote
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 가상계좌 수신
	 *
	 * @param omsPaymentif
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> casnote(OmsPaymentif omsPaymentif) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		PrintStream out = System.out;

		/*
		* [상점 결제결과처리(DB) 페이지]
		*
		* 1) 위변조 방지를 위한 hashdata값 검증은 반드시 적용하셔야 합니다.
		*
		*/

		String LGD_RESPCODE = "";           // 응답코드: 0000(성공) 그외 실패
		String LGD_RESPMSG = "";            // 응답메세지
		String LGD_MID = "";                // 상점아이디 
		String LGD_OID = "";                // 주문번호
		String LGD_AMOUNT = "";             // 거래금액
		String LGD_TID = "";                // LG유플러스에서 부여한 거래번호
		String LGD_PAYTYPE = "";            // 결제수단코드
		String LGD_PAYDATE = "";            // 거래일시(승인일시/이체일시)
		String LGD_HASHDATA = "";           // 해쉬값
		String LGD_FINANCECODE = "";        // 결제기관코드(은행코드)
		String LGD_FINANCENAME = "";        // 결제기관이름(은행이름)
		String LGD_ESCROWYN = "";           // 에스크로 적용여부
		String LGD_TIMESTAMP = "";          // 타임스탬프
		String LGD_ACCOUNTNUM = "";         // 계좌번호(무통장입금) 
		String LGD_CASTAMOUNT = "";         // 입금총액(무통장입금)
		String LGD_CASCAMOUNT = "";         // 현입금액(무통장입금)
		String LGD_CASFLAG = "";            // 무통장입금 플래그(무통장입금) - 'R':계좌할당, 'I':입금, 'C':입금취소 
		String LGD_CASSEQNO = "";           // 입금순서(무통장입금)
		String LGD_CASHRECEIPTNUM = "";     // 현금영수증 승인번호
		String LGD_CASHRECEIPTSELFYN = "";  // 현금영수증자진발급제유무 Y: 자진발급제 적용, 그외 : 미적용
		String LGD_CASHRECEIPTKIND = "";    // 현금영수증 종류 0: 소득공제용 , 1: 지출증빙용
		String LGD_PAYER = "";    			// 임금자명

		/*
		 * 구매정보
		 */
		String LGD_BUYER = "";              // 구매자
		String LGD_PRODUCTINFO = "";        // 상품명
		String LGD_BUYERID = "";            // 구매자 ID
		String LGD_BUYERADDRESS = "";       // 구매자 주소
		String LGD_BUYERPHONE = "";         // 구매자 전화번호
		String LGD_BUYEREMAIL = "";         // 구매자 이메일
		String LGD_BUYERSSN = "";           // 구매자 주민번호
		String LGD_PRODUCTCODE = "";        // 상품코드
		String LGD_RECEIVER = "";           // 수취인
		String LGD_RECEIVERPHONE = "";      // 수취인 전화번호
		String LGD_DELIVERYINFO = "";       // 배송지

		LGD_RESPCODE = omsPaymentif.getLGD_RESPCODE();
		LGD_RESPMSG = omsPaymentif.getLGD_RESPMSG();
		LGD_MID = omsPaymentif.getLGD_MID();
		LGD_OID = omsPaymentif.getLGD_OID();
		LGD_AMOUNT = omsPaymentif.getLGD_AMOUNT();
		LGD_TID = omsPaymentif.getLGD_TID();
		LGD_PAYTYPE = omsPaymentif.getLGD_PAYTYPE();
		LGD_PAYDATE = omsPaymentif.getLGD_PAYDATE();
		LGD_HASHDATA = omsPaymentif.getLGD_HASHDATA();
		LGD_FINANCECODE = omsPaymentif.getLGD_FINANCECODE();
		LGD_FINANCENAME = omsPaymentif.getLGD_FINANCENAME();
		LGD_ESCROWYN = omsPaymentif.getLGD_ESCROWYN();
		LGD_TIMESTAMP = omsPaymentif.getLGD_TIMESTAMP();
		LGD_ACCOUNTNUM = omsPaymentif.getLGD_ACCOUNTNUM();
		LGD_CASTAMOUNT = omsPaymentif.getLGD_CASTAMOUNT();
		LGD_CASCAMOUNT = omsPaymentif.getLGD_CASCAMOUNT();
		LGD_CASFLAG = omsPaymentif.getLGD_CASFLAG();
		LGD_CASSEQNO = omsPaymentif.getLGD_CASSEQNO();
		LGD_CASHRECEIPTNUM = omsPaymentif.getLGD_CASHRECEIPTNUM();
		LGD_CASHRECEIPTSELFYN = omsPaymentif.getLGD_CASHRECEIPTSELFYN();
		LGD_CASHRECEIPTKIND = omsPaymentif.getLGD_CASHRECEIPTKIND();
		LGD_PAYER = omsPaymentif.getLGD_PAYER();

		LGD_BUYER = omsPaymentif.getLGD_BUYER();
		LGD_PRODUCTINFO = omsPaymentif.getLGD_PRODUCTINFO();
		LGD_BUYERID = omsPaymentif.getLGD_BUYERID();
		LGD_BUYERADDRESS = omsPaymentif.getLGD_BUYERADDRESS();
		LGD_BUYERPHONE = omsPaymentif.getLGD_BUYERPHONE();
		LGD_BUYEREMAIL = omsPaymentif.getLGD_BUYEREMAIL();
		LGD_BUYERSSN = omsPaymentif.getLGD_BUYERSSN();
		LGD_PRODUCTCODE = omsPaymentif.getLGD_PRODUCTCODE();
		LGD_RECEIVER = omsPaymentif.getLGD_RECEIVER();
		LGD_RECEIVERPHONE = omsPaymentif.getLGD_RECEIVERPHONE();
		LGD_DELIVERYINFO = omsPaymentif.getLGD_DELIVERYINFO();

		/*
		 * hashdata 검증을 위한 mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다. 
		 * LG유플러스에서 발급한 상점키로 반드시변경해 주시기 바랍니다.
		 */
		String LGD_MERTKEY = Config.getString("pg.mertkey.common"); //mertkey

		StringBuffer sb = new StringBuffer();
		sb.append(LGD_MID);
		sb.append(LGD_OID);
		sb.append(LGD_AMOUNT);
		sb.append(LGD_RESPCODE);
		sb.append(LGD_TIMESTAMP);
		sb.append(LGD_MERTKEY);

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}

		String LGD_HASHDATA2 = strBuf.toString();  //상점검증 해쉬값  

		/*
		 * 상점 처리결과 리턴메세지
		 *
		 * OK  : 상점 처리결과 성공
		 * 그외 : 상점 처리결과 실패
		 *
		 * ※ 주의사항 : 성공시 'OK' 문자이외의 다른문자열이 포함되면 실패처리 되오니 주의하시기 바랍니다.
		 */
		String resultMSG = "결제결과 상점 DB처리(LGD_CASNOTEURL) 결과값을 입력해 주시기 바랍니다.";

		if (LGD_HASHDATA2.trim().equals(LGD_HASHDATA)) { //해쉬값 검증이 성공이면
			if (("0000".equals(LGD_RESPCODE.trim()))) { //결제가 성공이면
				if ("R".equals(LGD_CASFLAG.trim())) {
					/*
					 * 무통장 할당 성공 결과 상점 처리(DB) 부분
					 * 상점 결과 처리가 정상이면 "OK"
					 */
					if (((PgService) getThis()).insertCasnoteTransferNewTx(omsPaymentif)) {
						this.updateCasnotePayment(omsPaymentif, "R");
						result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
						//if( 무통장 할당 성공 상점처리결과 성공 ) 
						resultMSG = "OK";
					}



				} else if ("I".equals(LGD_CASFLAG.trim())) {
					/*
					 * 무통장 입금 성공 결과 상점 처리(DB) 부분
					 * 상점 결과 처리가 정상이면 "OK"
					 */
					if (((PgService) getThis()).insertCasnoteTransferNewTx(omsPaymentif)) {
						this.updateCasnotePayment(omsPaymentif, "I");
						result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
						//if( 무통장 입금 성공 상점처리결과 성공 ) 
						resultMSG = "OK";
					}



				} else if ("C".equals(LGD_CASFLAG.trim())) {
					/*
					 * 무통장 입금취소 성공 결과 상점 처리(DB) 부분
					 * 상점 결과 처리가 정상이면 "OK"
					 */
					if (((PgService) getThis()).insertCasnoteTransferNewTx(omsPaymentif)) {
						this.updateCasnotePayment(omsPaymentif, "C");
						result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

						//if( 무통장 입금취소 성공 상점처리결과 성공 ) 
						resultMSG = "OK";
					}



				}
			} else { //결제가 실패이면
				/*
				 * 거래실패 결과 상점 처리(DB) 부분
				 * 상점결과 처리가 정상이면 "OK"
				 */
				if (((PgService) getThis()).insertCasnoteTransferNewTx(omsPaymentif)) {
					//if( 결제실패 상점처리결과 성공 ) 
					result.put(BaseConstants.RESULT_MESSAGE, "거래실패");
					resultMSG = "OK";
				}

			}
		} else { //해쉬값이 검증이 실패이면
			/*
			 * hashdata검증 실패 로그를 처리하시기 바랍니다. 
			 */
			if (((PgService) getThis()).insertCasnoteTransferNewTx(omsPaymentif)) {
				resultMSG = "결제결과 상점 DB처리(LGD_CASNOTEURL) 해쉬값 검증이 실패하였습니다.";
				result.put(BaseConstants.RESULT_MESSAGE, resultMSG);
			}


		}

		out.println(resultMSG.toString());
		result.put("resultMSG", resultMSG);
		return result;
	}

	/**
	 * 
	 * @Method Name : cardBilling
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 정기결제 카드등록.
	 *
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> cardBilling(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsPaymentif omsPaymentif = payment.getOmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());
		//전문 insert
		omsPaymentif = ((PgService) getThis()).insertPaymentTransferNewTx(omsPaymentif);

		PrintStream out = System.out;

		/*
		 * [결제승인 요청 페이지]
		 *
		 * 파라미터 전달시 POST를 사용하세요.
		 */

		String CST_PLATFORM = nvl(omsPaymentif.getCST_PLATFORM());     	//LG유플러스 결제 서비스 선택(test:테스트, service:서비스)
		String CST_MID = nvl(omsPaymentif.getCST_MID());           	//상점아이디(LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요)
		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "") + CST_MID;  //테스트 아이디는 't'를 제외하고 입력하세요.
																					//상점아이디(자동생성)    
		String LGD_OID = nvl(omsPaymentif.getLGD_OID());           	//주문번호(상점정의 유니크한 주문번호를 입력하세요)
		String LGD_AMOUNT = nvl(omsPaymentif.getLGD_AMOUNT());        	//결제금액("," 를 제외한 결제금액을 입력하세요)
		String LGD_PAN = nvl(omsPaymentif.getLGD_PAN());           	//카드번호
																//Swipe 방식의 경우 track data 입력하세요. 예)4902000000000000=12000011856549300000
		String LGD_INSTALL = nvl(omsPaymentif.getLGD_INSTALL());       	//할부개월수
		String LGD_EXPYEAR = nvl(omsPaymentif.getLGD_EXPYEAR());       	//유효기간년
		String LGD_EXPMON = nvl(omsPaymentif.getLGD_EXPMON());        	//유효기간월    
		String LGD_PIN = nvl(omsPaymentif.getLGD_PIN());        		//비밀번호 앞2자리(옵션-주민번호를 넘기지 않으면 비밀번호도 체크 안함)
		String LGD_PRIVATENO = nvl(omsPaymentif.getLGD_PRIVATENO());     	//생년월일 6자리 (YYMMDD) or 사업자번호(옵션)
		String LGD_BUYERPHONE = nvl(omsPaymentif.getLGD_BUYERPHONE());    	//고객 휴대폰번호(SMS발송:선택) 
		String VBV_ECI = nvl(omsPaymentif.getVBV_ECI());    			//결제방식(KeyIn:010, Swipe:020)
		String LGD_BUYER = nvl(omsPaymentif.getLGD_BUYER());			//구매자
		String LGD_BUYERID = nvl(omsPaymentif.getLGD_BUYERID());			//구매자ID
		String LGD_PRODUCTINFO = nvl(omsPaymentif.getLGD_PRODUCTINFO());		//상품명

		String LGD_BUYERIP = omsPaymentif.getLGD_BUYERIP();       			//구매자IP

		String configPath = Config.getString("pg.config.path");						 				//LG유플러스에서 제공한 환경파일(/conf/lgdacom.conf, /conf/mall.conf)이 위치한 디렉토리 지정 

		XPayClient xpay = new XPayClient();
		boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

		if (!isInitOK) {
			//API 초기화 실패 화면처리
			out.println("결제요청을 초기화 하는데 실패하였습니다.<br>");
			out.println("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
			out.println("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
			out.println("문의전화 LG유플러스 1544-7772<br>");
			result.put(BaseConstants.RESULT_MESSAGE, "PG 환경설정 오류.");
		}

		xpay.Init_TX(LGD_MID);

		xpay.Log("결제요청을 시작합니다.");

		xpay.Set("LGD_TXNAME", "CardAuth");
		xpay.Set("LGD_OID", LGD_OID);
		xpay.Set("LGD_AMOUNT", LGD_AMOUNT);
		xpay.Set("LGD_PAN", LGD_PAN);
		xpay.Set("LGD_INSTALL", LGD_INSTALL);
		xpay.Set("VBV_ECI", VBV_ECI);
		xpay.Set("LGD_BUYERPHONE", LGD_BUYERPHONE);
		xpay.Set("LGD_BUYER", LGD_BUYER);
		xpay.Set("LGD_BUYERID", LGD_BUYERID);
		xpay.Set("LGD_BUYERIP", LGD_BUYERIP);
		xpay.Set("LGD_PRODUCTINFO", LGD_PRODUCTINFO);

		if (VBV_ECI.equals("010")) { //키인방식인 경우에만 해당
			xpay.Set("LGD_EXPYEAR", LGD_EXPYEAR);
			xpay.Set("LGD_EXPMON", LGD_EXPMON);
			xpay.Set("LGD_PIN", LGD_PIN);
			xpay.Set("LGD_PRIVATENO", LGD_PRIVATENO);
		}

		if (xpay.TX()) {
			//1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
			out.println("결제요청이 완료되었습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			out.println("거래번호 : " + xpay.Response("LGD_TID", 0) + "<br>");
			out.println("상점아이디 : " + xpay.Response("LGD_MID", 0) + "<br>");
			out.println("상점주문번호 : " + xpay.Response("LGD_OID", 0) + "<br>");
			out.println("결제금액 : " + xpay.Response("LGD_AMOUNT", 0) + "<br>");
			out.println("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
			out.println("결과메세지 : " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

			for (int i = 0; i < xpay.ResponseNameCount(); i++) {
				out.println(xpay.ResponseName(i) + " = ");
				for (int j = 0; j < xpay.ResponseCount(); j++) {
					out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
				}
			}
			out.println("<p>");

			if ("0000".equals(xpay.m_szResCode)) {
				//최종결제요청 결과 성공 DB처리
				out.println("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");

				//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
				boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.

				try {
					this.savePayment(payment, xpay);
					((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);
					result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
				} catch (Exception e) {
					isDBOK = false;
				} finally {
					//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
					if (!isDBOK) {
						xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" + xpay.Response("LGD_TID", 0) + ",MID:"
								+ xpay.Response("LGD_MID", 0) + ",OID:" + xpay.Response("LGD_OID", 0) + "]");

						out.println("TX Rollback Response_code = " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
						out.println("TX Rollback Response_msg = " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

						if ("0000".equals(xpay.m_szResCode)) {
							out.println("자동취소가 정상적으로 완료 되었습니다.<br>");
						} else {
							out.println("자동취소가 정상적으로 처리되지 않았습니다.<br>");
						}

						((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);

						result.put(BaseConstants.RESULT_MESSAGE, xpay.Response("LGD_RESPMSG", 0));
					}
				}


			} else {
				//최종결제요청 결과 실패 DB처리
				out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");

				((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);

				result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
			}
		} else {
			//2)API 요청실패 화면처리
			out.println("결제요청이 실패하였습니다.  <br>");
			out.println("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
			out.println("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

			//최종결제요청 결과 실패 DB처리
			out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");

			((PgService) getThis()).updatePaymentTransferNewTx(omsPaymentif, xpay);

			result.put(BaseConstants.RESULT_MESSAGE, xpay.m_szResMsg);
		}

		xpay.Log("결제요청을 종료합니다.");

		return result;
	}


	/**
	 * 
	 * @Method Name : cancelAll
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 전체취소
	 *
	 * @param LguBase
	 * @return CcsMessage
	 */
	public CcsMessage cancelAll(LguBase cancel, String paymentMethodCd) {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);

		XPayClient xpay = new XPayClient();
		try {
			boolean isInitOK = xpay.Init(cancel.getConfigPath(), cancel.getCstPlatform());
			if (!isInitOK) {
				// API 초기화 실패 화면처리
				logger.error("결제 취소 요청을 초기화 하는데 실패하였습니다.<br>");
				logger.error("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
				logger.error("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
				logger.error("문의전화 LG유플러스 1544-7772<br>");

				ccsmessage.setResultCode("-1");
				ccsmessage.setResultMessage("결제 취소 요청을 초기화 하는데 실패하였습니다.");
				return ccsmessage;
			}
			xpay.Init_TX(cancel.getLgdMid());
			xpay.Log("결제 취소 요청을 시작합니다.");

			xpay.Set("LGD_TXNAME", "Cancel");
			xpay.Set("LGD_TID", cancel.getLgdTid());

			/*
			 * 1. 결제취소 요청 결과처리
			 *
			 * 취소결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
			 *
			 * [[[중요]]] 고객사에서 정상취소 처리해야할 응답코드 
			 * 1. 신용카드 : 0000, AV11 
			 * 2. 계좌이체 : 0000, RF00, RF10, RF09, RF15, RF19, RF23, RF25 (환불진행중 응답건-> 환불결과코드.xls 참고) 
			 * 3. 나머지 결제수단의 경우 0000(성공) 만 취소성공 처리
			 *
			 */
			if (xpay.TX()) {
				// 1)결제 부분취소결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
				if (logger.isDebugEnabled()) {
					logger.debug("결제 취소 요청이 완료되었습니다.");
					logger.debug("TX Response_code = " + xpay.m_szResCode);
					logger.debug("TX Response_msg = " + xpay.m_szResMsg);

					for (int i = 0; i < xpay.ResponseNameCount(); i++) {
						StringBuffer sb = new StringBuffer();
						sb.append("\t" + xpay.ResponseName(i) + " = ");
						for (int j = 0; j < xpay.ResponseCount(); j++) {
							sb.append(xpay.Response(xpay.ResponseName(i), j) + "\t");
						}
						logger.debug(sb.toString());
					}
				}
				if ("0000".equals(xpay.m_szResCode)) {
					ccsmessage.setSuccess(true);
				} else {
					if ("PAYMENT_METHOD_CD.CARD".equals(paymentMethodCd)) {
						if ("AV11".equals(xpay.m_szResCode)) {
							ccsmessage.setSuccess(true);
						}
					} else if ("PAYMENT_METHOD_CD.TRANSFER".equals(paymentMethodCd)) { // 계좌이체
						if ("RF00, RF10, RF09, RF15, RF19, RF23, RF25".contains(xpay.m_szResCode)) {
							ccsmessage.setSuccess(true);
						}
					}
				}
				if (ccsmessage.isSuccess()) {
					ccsmessage.setId(xpay.Response("LGD_MID", 0));// 상점id (pg_shop_id)
					ccsmessage.setNo(xpay.Response("LGD_TID", 0));// 승인거래번호(pg_approval_no or pg_cancel_no)
				}
			} else {
				// 2)API 요청 실패 화면처리
				logger.error("결제 취소 요청이 실패하였습니다.");
				logger.error("TX Response_code = " + xpay.m_szResCode);
				logger.error("TX Response_msg = " + xpay.m_szResMsg);
				// 취소요청 결과 실패 DB처리
				logger.error("결제 취소 요청 실패를 DB처리하시기 바랍니다.");
			}
			
			ccsmessage.setResultCode(xpay.m_szResCode); // "0000","RF00" 이외에는 실패
			ccsmessage.setResultMessage(xpay.m_szResMsg);
			ccsmessage.setMsg(this.xpayToString(xpay).toString()); // for 전문테이블
			xpay.Log("결제 취소 요청을 종료합니다.");
		} catch (Exception e) {
			ccsmessage.setResultCode("-999");
			ccsmessage.setResultMessage("결제 취소 중 알수 없는 에러가 발생하였습니다."); // TODO - 메세지 프로퍼티 처리
			ccsmessage.setMsg(this.xpayToString(xpay).toString()); // for 전문테이블
		}

		return ccsmessage;
	}
	
	/**
	 * 
	 * @Method Name : cancelPartial
	 * @author : dennis
	 * @date : 2016. 8. 9.
	 * @description : 부분취소
	 *
	 * @param LguCancelPartial
	 * @return CcsMessage
	 */
	public CcsMessage cancelPartial(LguCancelPartial cancel) {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);

		XPayClient xpay = new XPayClient();
		try {
			boolean isInitOK = xpay.Init(cancel.getConfigPath(), cancel.getCstPlatform());
			if (!isInitOK) {
				// API 초기화 실패 화면처리
				logger.error("결제 부분취소 요청을 초기화 하는데 실패하였습니다.<br>");
				logger.error("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
				logger.error("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
				logger.error("문의전화 LG유플러스 1544-7772<br>");

				ccsmessage.setResultCode("-1");
				ccsmessage.setResultMessage("결제 부분취소 요청을 초기화 하는데 실패하였습니다.");
				return ccsmessage;
			}
			xpay.Init_TX(cancel.getLgdMid());
			xpay.Log("결제 부분취소 요청을 시작합니다.");

			// 공통
			xpay.Set("LGD_TXNAME", "PartialCancel");
			xpay.Set("LGD_TID", cancel.getLgdTid());
			xpay.Set("LGD_CANCELAMOUNT", cancel.getLgdCancelAmount());
			xpay.Set("LGD_CANCELTAXFREEAMOUNT", nvl(cancel.getLgdCancelTaxfreeAmount()));
			xpay.Set("LGD_CANCELREASON", nvl(cancel.getLgdCancelReason()));

			xpay.Set("LGD_REMAINAMOUNT", nvl(cancel.getLgdRemainAmount()));// 신용카드
			xpay.Set("LGD_REQREMAIN", nvl(cancel.getLgdReqRemain()));// 신용카드, 휴대폰
			xpay.Set("LGD_PCANCELCNT", nvl(cancel.getLgdPcancelCnt()));// 가상계좌, 계좌이체
			
			if (!StringUtils.isEmpty(cancel.getLgdRfBankCode())) {
				xpay.Set("LGD_RFBANKCODE", nvl(cancel.getLgdRfBankCode()));// 가상계좌(무통장)
				xpay.Set("LGD_RFACCOUNTNUM", nvl(cancel.getLgdRfAccountNum()));// 가상계좌(무통장)
				xpay.Set("LGD_RFCUSTOMERNAME", nvl(cancel.getLgdRfCustomerName()));// 가상계좌(무통장)
				xpay.Set("LGD_RFPHONE", nvl(cancel.getLgdRfPhone()));// 가상계좌(무통장)
			}
			
			xpay.Set("LGD_DIVIDE_YN", nvl(cancel.getLgdDivideYn()));// 분할정산
			xpay.Set("LGD_DIVIDE_MID_SUB", nvl(cancel.getLgdDivideMidSub()));// 분할정산

			if (xpay.TX()) {
				// 1)결제 부분취소결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
				if (logger.isDebugEnabled()) {
					logger.debug("결제 부분취소 요청이 완료되었습니다.");
					logger.debug("TX Response_code = " + xpay.m_szResCode);
					logger.debug("TX Response_msg = " + xpay.m_szResMsg);

					for (int i = 0; i < xpay.ResponseNameCount(); i++) {
						StringBuffer sb = new StringBuffer();
						sb.append("\t" + xpay.ResponseName(i) + " = ");
						for (int j = 0; j < xpay.ResponseCount(); j++) {
							sb.append(xpay.Response(xpay.ResponseName(i), j) + "\t");
						}
						logger.debug(sb.toString());
					}					
				}
				if ("0000".equals(xpay.m_szResCode) || "RF00".equals(xpay.m_szResCode)) {
					ccsmessage.setId(xpay.Response("LGD_MID", 0));
					ccsmessage.setNo(xpay.Response("LGD_TID", 0));
					ccsmessage.setSuccess(true);
				}
			} else {
				// 2)API 요청 실패 화면처리
				logger.error("결제 부분취소 요청이 실패하였습니다.");
				logger.error("TX Response_code = " + xpay.m_szResCode);
				logger.error("TX Response_msg = " + xpay.m_szResMsg);
				// 부분취소 요청 결과 실패 DB처리
				logger.error("결제 부분취소 요청 실패를 DB처리하시기 바랍니다.");
			}
			
			ccsmessage.setResultCode(xpay.m_szResCode); // "0000","RF00" 이외에는 실패
			ccsmessage.setResultMessage(xpay.m_szResMsg);
			ccsmessage.setMsg(this.xpayToString(xpay).toString()); // for 전문테이블
			xpay.Log("결제 부분취소 요청을 종료합니다.");
		} catch (Exception e) {
			ccsmessage.setResultCode("-999");
			ccsmessage.setResultMessage("결제 부분취소 중 알수 없는 에러가 발생하였습니다.");	// TODO - 메세지 프로퍼티 처리
			ccsmessage.setMsg(this.xpayToString(xpay).toString()); // for 전문테이블
		}
		
		return ccsmessage;
	}

	public OmsPaymentif getCasnoteTest(OmsPaymentif omsPaymentif) throws Exception {
		OmsPayment omsPayment = new OmsPayment();
		omsPayment.setOrderId(omsPaymentif.getOrderId());
		omsPayment.setPaymentMethodCd("PAYMENT_METHOD_CD.VIRTUAL");
		omsPayment = (OmsPayment) dao.selectOne("oms.order.getPaymentData", omsPayment);
		omsPaymentif = getRequestParam(omsPaymentif);

		omsPaymentif.setLGD_OID(omsPayment.getPaymentNo().toString());
		omsPaymentif.setLGD_AMOUNT(omsPayment.getPaymentAmt().toString());
		omsPaymentif.setLGD_TIMESTAMP(DateUtil.convertFormat(omsPayment.getPaymentDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10));

		StringBuffer sb = new StringBuffer();
		sb.append(omsPaymentif.getLGD_MID());
		sb.append(omsPaymentif.getLGD_OID());
		sb.append(omsPaymentif.getLGD_AMOUNT());
		sb.append("0000");
		sb.append(omsPaymentif.getLGD_TIMESTAMP());
		sb.append(Config.getString("pg.mertkey.common"));

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}

		omsPaymentif.setLGD_HASHDATA(strBuf.toString());
		return omsPaymentif;
	}

	/**
	 * 
	 * @Method Name : getReceiptparam
	 * @author : dennis
	 * @date : 2016. 8. 16.
	 * @description : 현금영수증 parameter
	 *
	 * @param omsPaymentif
	 * @return
	 * @throws Exception
	 */
	public OmsPaymentif getReceiptparam(OmsPaymentif omsPaymentif) throws Exception {

		String CST_PLATFORM = Config.getString("pg.platform");
		String orderId = omsPaymentif.getOrderId();

		OmsPayment payment = new OmsPayment();
		payment.setOrderId(orderId);
		payment.setPgApprovalNo(omsPaymentif.getLGD_TID());
		payment = (OmsPayment) dao.selectOne("oms.order.getPaymentData", payment);

		String mid = payment.getPgShopId();
		String tid = payment.getPgApprovalNo();
		String paymentMethodCd = payment.getPaymentMethodCd();
		String seqno = CommonUtil.replaceNull(payment.getVirtualAccountDepositOrder(), "000");

		String TTYPE = "";
		if ("PAYMENT_METHOD_CD.VIRTUAL".equals(paymentMethodCd)) {
			String paymentStateCd = payment.getPaymentStateCd();
			if ("PAYMENT_STATE_CD.PAYMENT_READY".equals(paymentStateCd)) {
				TTYPE = "CR";	//무통장입금 등록건
			} else {
				TTYPE = "CAS";	//가상계좌
			}
		} else if ("PAYMENT_METHOD_CD.TRANSFER".equals(paymentMethodCd)) {
			TTYPE = "BANK";	//계좌이체.
		}

		String mertkey = "";
		if ("test".equals(CST_PLATFORM)) {
			mertkey = getMertkey(mid.substring(1));
		} else {
			mertkey = getMertkey(mid);
		}

		StringBuffer sb = new StringBuffer();
		sb.append(mid);
		sb.append(tid);
		sb.append(mertkey);

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}

		omsPaymentif.setLGD_HASHDATA(strBuf.toString());

		omsPaymentif.setLGD_MID(mid);
		omsPaymentif.setLGD_OID(payment.getPaymentNo().toString());
		omsPaymentif.setLGD_TID(tid);
		omsPaymentif.setLGD_CASSEQNO(seqno);	//과오납불가일경우 000
		omsPaymentif.setCST_PLATFORM(CST_PLATFORM);
		omsPaymentif.setTTYPE(TTYPE);

		return omsPaymentif;
	}
	
	/**
	 * 계좌인증
	 * @Method Name : accountCertify
	 * @author : victor
	 * @date : 2016. 8. 25.
	 * @description : 
	 *
	 * @param account
	 */
	public CcsMessage accountCertify(LguAccountCertify account) {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);

		XPayClient xpay = new XPayClient();
		try {
			boolean isInitOK = xpay.Init(account.getConfigPath(), account.getCstPlatform());

			if (!isInitOK) {
				// API 초기화 실패 화면처리
				logger.error("인증요청을 초기화 하는데 실패하였습니다.<br>");
				logger.error("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
				logger.error("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
				logger.error("문의전화 LG유플러스 1544-7772<br>");

				ccsmessage.setResultCode("-1");
				ccsmessage.setResultMessage("인증요청을 초기화 하는데 실패하였습니다.");
				return ccsmessage;
			}
//			account.setCstMid(Config.getString("pg.mid.common"));// 계좌인증상점키 - TODO : 변경
			account.setCstMid("ZTS_00");// 계좌인증상점키 - TODO : 변경
			xpay.Init_TX(account.getLgdMid());
			xpay.Log("인증요청을 시작합니다.");

			xpay.Set("LGD_TXNAME", "AccCert");
			xpay.Set("LGD_GUBUN", account.getLgdGubun());
			xpay.Set("LGD_BANKCODE", nvl(account.getLgdBankCode()));
			xpay.Set("LGD_ACCOUNTNO", nvl(account.getLgdAccountNo()));
			xpay.Set("LGD_NAME", nvl(account.getLgdName()));
			xpay.Set("LGD_PRIVATENO", nvl(account.getLgdPrivateNo()));
			xpay.Set("LGD_BUYERIP", nvl(account.getLgdBuyerIp()));
			xpay.Set("LGD_CHECKNHYN", nvl(account.getLgdCheckNhyn()));

			if (xpay.TX()) {
				// 1)인증결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)
				if (logger.isDebugEnabled()) {
					logger.debug("인증요청이 완료되었습니다.");
					logger.debug("TX 인증요청 Response_code = " + xpay.m_szResCode);
					logger.debug("TX 인증요청 Response_msg = " + xpay.m_szResMsg);
					// 아래는 인증 결과 파라미터를 모두 찍어 줍니다.
					for (int i = 0; i < xpay.ResponseNameCount(); i++) {
						StringBuffer sb = new StringBuffer();
						sb.append("\t" + xpay.ResponseName(i) + " = ");
						for (int j = 0; j < xpay.ResponseCount(); j++) {
							sb.append(xpay.Response(xpay.ResponseName(i), j) + "\t");
						}
						logger.debug(sb.toString());
					}
				}
				if ("0000".equals(xpay.m_szResCode)) {
					ccsmessage.setId(xpay.Response("LGD_MID", 0));
					ccsmessage.setNo(xpay.Response("LGD_TID", 0));
					ccsmessage.setSuccess(true);
				}
			} else {
				// 2)API 요청실패 화면처리
				logger.error("인증요청이 실패하였습니다.");
				logger.error("TX 인증요청 Response_code = " + xpay.m_szResCode);
				logger.error("TX 인증요청 Response_msg = " + xpay.m_szResMsg);

				// 인증요청 결과 실패 DB처리
				logger.error("인증요청 실패를 DB처리하시기 바랍니다.");
			}

			ccsmessage.setResultCode(xpay.m_szResCode);// "0000" 성공, 이외는 실패
			ccsmessage.setResultMessage(xpay.m_szResMsg);
//			result.setMsg(this.xpayToString(xpay).toString()); // for 전문테이블
			xpay.Log("인증요청을 종료합니다.");
		} catch (Exception e) {
			ccsmessage.setResultCode("-999");
			ccsmessage.setResultMessage("인증요청 중 알수 없는 에러가 발생하였습니다.");
		}
		return ccsmessage;
	}

	/**
	 * 
	 * @Method Name : getInterestList
	 * @author : dennis
	 * @date : 2016. 10. 28.
	 * @description : 카드별 무이자 정보조회
	 *
	 * @param cardCode
	 * @param paymentAmt
	 * @return
	 * @throws Exception
	 */
	public String getInterestList(String cardCode, String paymentAmt) throws Exception {
		StringBuffer html = new StringBuffer();

		html.append("<option value=\"\">할부개월수</option>");
		html.append("<option value=\"00\" selected=\"selected\">일시불</option>                         ");
		for (int i = 2; i <= 12; i++) {
			html.append("<option value=\"").append(String.format("%02d", i)).append("\">").append(i).append("개월")
					.append(checkInterest(cardCode, i, paymentAmt))
					.append("</option>");
		}

		return html.toString();
	}
	
	/**
	 * 
	 * @Method Name : getInterest
	 * @author : dennis
	 * @date : 2016. 10. 28.
	 * @description : 무이자정보 조회
	 *
	 * @param orderTypeCd
	 * @param deviceTypeCd
	 * @throws NoSuchAlgorithmException
	 */
	public void getInterest(String orderTypeCd, String deviceTypeCd) throws NoSuchAlgorithmException {

		logger.debug("====================== PG interest ============================");

		String channelId = SessionUtil.getChannelId();

		String CST_MID = getMID(orderTypeCd, deviceTypeCd, channelId);
		String LGD_MERTKEY = getMertkey(CST_MID);

		StringBuffer sb = new StringBuffer();
		sb.append(CST_MID);
		sb.append(LGD_MERTKEY);

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}

		interestList = new HashMap<String, Object>();
		
		//0to7_00 MID만 해당하게 개발되었음. 변경시 LG문의!!!!
		HashMap<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("LGD_MERTID", CST_MID);
//		paramMap.put("LGD_DATE", DateUtil.getCurrentDate(DateUtil.FORMAT_3));
//		paramMap.put("LGD_HASH_DATA", strBuf.toString());
		paramMap.put("LGD_MERTID", "0to7_00");
		paramMap.put("LGD_DATE", DateUtil.getCurrentDate(DateUtil.FORMAT_3));
		paramMap.put("LGD_HASH_DATA",
				"B1A6BC3A2077A3A7CC019C3F2F2071C053C189E89616349301BAF6A166FBFB5E6A3CF23E9C116C28D3EB98DD52679D707DEF8DDC94D3CF367B4DE0741AED9C7E");

		Document xmlDocument = getInterestXml(paramMap);

		if (xmlDocument != null) {
			Element rootElement = xmlDocument.getRootElement();

			List<Element> articleList = rootElement.getChildren("CardInfo");

			for (Element article : articleList) {

				String cardCode = article.getChildTextTrim("CardCode");
				String cardEvent = article.getChildTextTrim("CardEvent");

				logger.debug("cardCode : " + cardCode);
				logger.debug("cardEvent : " + cardEvent);

				List<String> list = null;
				if (CommonUtil.isNotEmpty(cardEvent)) {
					String[] k = cardEvent.split("-");

					if (CommonUtil.isNotEmpty(k)) {
						if (CommonUtil.isNotEmpty(k[0])) {
							interestList.put(cardCode + "_pay", k[0]);
							String[] j = k[1].split(":");
							if (CommonUtil.isNotEmpty(j)) {
								list = Arrays.asList(j);
							}

							if (list != null && list.size() > 0) {
								interestList.put(cardCode, list);
							}
						}
					}
				}

			}

		}
		logger.debug("====================== PG interest ============================\n");
	}

	private String checkInterest(String cardCode, int i, String paymentAmtStr) {
		List<String> list = (List<String>) interestList.get(cardCode);
		String minOrderAmtStr = (String) interestList.get(cardCode + "_pay");

//		logger.debug("paymentAmt : " + paymentAmtStr);
//		logger.debug("minOrderAmt : " + minOrderAmtStr);

		boolean flag = true;
		if (CommonUtil.isNotEmpty(minOrderAmtStr)) {
			if (CommonUtil.isNotEmpty(paymentAmtStr)) {
				BigDecimal minOrderAmt = new BigDecimal(minOrderAmtStr + "0000");
				BigDecimal paymentAmt = new BigDecimal(paymentAmtStr);
				if (minOrderAmt.compareTo(paymentAmt) > 0) {
					flag = false;
				}
			} else {
				flag = false;
			}
		}
		if (flag && list != null && list.size() > 0) {
			for (String chk : list) {
//				logger.debug("chk : " + chk);
//				logger.debug("i : " + i);
				if (chk.equals(Integer.toString(i))) {
					return "(무)";
				}
			}
		}
		return "";
	}

	private Document getInterestXml(HashMap<String, String> paramMap) {

		logger.debug("param : " + paramMap.toString());

		String baseUrl = "http://pgweb.uplus.co.kr/MpFlowCtrl?eventDiv1=nointerestapi&eventDiv2=getNoInterest";

		String parameters = "";

		Set<String> keySet = paramMap.keySet();

		Iterator<String> keys = keySet.iterator();

		while (keys.hasNext()) {
			String paramKey = (String) keys.next();
			String paramValue = (String) paramMap.get(paramKey);

			if (parameters.equals("")) {
				parameters += paramKey + "=" + paramValue;
			} else {
				parameters += "&" + paramKey + "=" + paramValue;
			}
		}

		OutputStream out = null;
		BufferedReader reader = null;
		Document document = null;

		if (CommonUtil.isNotEmpty(parameters)) {
			baseUrl = baseUrl + "&" + parameters;
		}
//		baseUrl = "http://pgweb.uplus.co.kr/MpFlowCtrl?eventDiv1=nointerestapi&eventDiv2=getNoInterest&LGD_MERTID=0to7_00&LGD_DATE=20161027&LGD_HASH_DATA=B1A6BC3A2077A3A7CC019C3F2F2071C053C189E89616349301BAF6A166FBFB5E6A3CF23E9C116C28D3EB98DD52679D707DEF8DDC94D3CF367B4DE0741AED9C7E";
		logger.debug("call url...... : " + baseUrl);
		try {

	         URL targetURL = new URL(baseUrl);

	         URLConnection urlConn = targetURL.openConnection();

			HttpURLConnection httpConnection = (HttpURLConnection) urlConn;
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setDefaultUseCaches(false);

	         out = httpConnection.getOutputStream();
			out.write(parameters.getBytes("euc-kr"));
	         out.flush();
	         out.close();

	         reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

	         // Result XML contents parsing by JDOM Parser

	         SAXBuilder builder = new SAXBuilder();

	         try {
	             document = (Document) builder.build(reader);
			} catch (Exception e) {
	             e.printStackTrace();
			}
		} catch (IOException e) {
	         e.printStackTrace();
		} finally {
	         if (out != null) {
	             try {
	                 out.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	         }
	         if (reader != null) {
	             try {
	                 reader.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	         }
		}

		return document;
	}

	private void setEscrowParam(XPayClient xpay, OmsPayment payment) {

		logger.debug("============ escrow param set ====================");

		List<OmsPaymentEscrow> escrowData = (List<OmsPaymentEscrow>) dao.selectList("oms.payment.getEscrowData", payment);

		for (OmsPaymentEscrow escrow : escrowData) {
//			logger.debug("escrow data : " + escrow.toString());
			Field[] fields = escrow.getClass().getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);

				try {
					String name = field.getName();
					String value = (String) field.get(escrow);
//					logger.debug(name + " = " + value);
					xpay.Set(name, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}