package gcp.external.service;



import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lgcns.kmpay.dto.DealApproveDto;
import com.lgcns.kmpay.service.CallWebService;

import gcp.ccs.service.BaseService;
import gcp.external.model.Kakao;
import gcp.external.util.KakaoUtil;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentif;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import kr.co.lgcns.module.lite.CnsPayWebConnector;
import net.sf.json.JSONObject;

@Service("kakaoService")
public class KakaoService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(KakaoService.class);

	public Kakao getTxnId(Kakao kakao) throws Exception {


		//가맹점키 (꼭 해당 가맹점키로 바꿔주세요)
		String EncodeKey = Config.getString("kakao.encodeKey");

		//상품가격 - hash 생성 시 필요하므로 화면로딩시 가지고 있어야 하는 값 
		int Amt = Integer.parseInt(kakao.getAmt());

		//가맹점ID
		String MID = Config.getString("kakao.mid");

		//TXN_ID 호출전용 키값
		String merchantEncKey = Config.getString("kakao.enc.key");
		String merchantHashKey = Config.getString("kakao.hash.key");

		////////위변조 처리/////////
		String EdiDate = KakaoUtil.getyyyyMMddHHmmss(); // 전문생성일시

		//결제요청용 키값
		String md_src = EdiDate + MID + Amt;
		String hash_String = KakaoUtil.SHA256Salt(md_src, EncodeKey);

		// TODO KaKaoPay의 INBOUND 전문 URL SETTING
		String msgName = "merchant/requestDealApprove.dev";

		String webPath = "https://kmpay.lgcns.com:8443/"; //PG사의 인증 서버 주소

		kakao.setMID(MID);
		kakao.setMerchantEncKey(merchantEncKey);
		kakao.setMerchantHashKey(merchantHashKey);
		kakao.setEdiDate(EdiDate);
		kakao.setHash_string(hash_String);
		kakao.setEncryptData(hash_String);

		// 서버로부터 받은 결과값 저장 JSONObject
		JSONObject resultJSONObject = new JSONObject();

		// TXN_ID를 요청하기 위한 PARAMETER
		String PR_TYPE = kakao.getPrType();
		String MERCHANT_ID = kakao.getMID();
		String MERCHANT_TXN_NUM = kakao.getMerchantTxnNum();
		String channelType = kakao.getChannelType();
		//문자열 인코딩 문제 극복을 위해 상품명은 api에 직접 setting
		byte[] PRODUCT_NAME = ("" + kakao.getGoodsName()).getBytes("UTF-8");
		String AMOUNT = kakao.getAmt();
		String serviceAmt = kakao.getServiceAmt();
		String supplyAmt = kakao.getSupplyAmt();
		String goodsVat = kakao.getGoodsVat();

		String CURRENCY = kakao.getCurrency();
		String RETURN_URL = kakao.getReturnUrl();
		// ENC KEY와 HASH KEY는 가맹점에서 생성한 KEY 로 SETTING 한다.
//		String merchantEncKey = kakao.getMerchantEncKey();
//		String merchantHashKey = kakao.getMerchantHashKey();
		String requestDealApproveUrl = webPath + msgName;
		String certifiedFlag = kakao.getCertifiedFlag();

		String requestorName = kakao.getRequestorName();
		String requestorTel = kakao.getRequestorTel();

		String offerPeriod = kakao.getOfferPeriod();
		String offerPeriodFlag = kakao.getOfferPeriodFlag();

		//무이자옵션
		String possiCard = kakao.getPossiCard();
		String fixedInt = kakao.getFixedInt();
		String maxInt = kakao.getMaxInt();
		String noIntYN = kakao.getNoIntYN();
		String noIntOpt = kakao.getNoIntOpt();
		String pointUseYN = kakao.getPointUseYn();
		String blockCard = kakao.getBlockCard();

		String blockBin = kakao.getBlockBin();

		// 결과값을 담는 부분
		String resultString = "";
		String resultCode = "";
		String resultMsg = "";
		String txnId = "";
		String merchantTxnNum = "";
		String prDt = "";

		// 가맹점에서 MPay로 전문을 보내기 위한 객체 생성
		// 타임아웃 설정
		int timeOut = 30;
		CallWebService webService = new CallWebService(timeOut);

		// 전문 Parameter DTO 객체 생성
		DealApproveDto approveDto = new DealApproveDto();

		// 필수값 SETTING
		approveDto.setRequestDealApproveUrl(requestDealApproveUrl); // 결제요청을 위한 URL
		approveDto.setMerchantEncKey(merchantEncKey); // 가맹점의 EncKey
		approveDto.setMerchantHashKey(merchantHashKey); // 가맹점의 HashKey

		approveDto.setCertifiedFlag(certifiedFlag); // WEB결제로 신청할시에 필수 'CN'
		approveDto.setPrType(PR_TYPE);
		approveDto.setChannelType(channelType); // TMS 및 방판 결제시 필수

		approveDto.setRequestorName(requestorName);
		approveDto.setRequestorTel(requestorTel);

		approveDto.setMerchantID(MERCHANT_ID);
		approveDto.setMerchantTxnNum(MERCHANT_TXN_NUM);

		approveDto.setProductName(new String(PRODUCT_NAME, "UTF-8"));

		approveDto.setAmount(AMOUNT);
		approveDto.setServiceAmt(serviceAmt);
		approveDto.setSupplyAmt(supplyAmt);
		approveDto.setGoodsVat(goodsVat);

		approveDto.setCurrency(CURRENCY);
		approveDto.setReturnUrl(RETURN_URL);

		approveDto.setOfferPeriod(offerPeriod);
		approveDto.setOfferPeriodFlag(offerPeriodFlag);

		approveDto.setPossiCard(possiCard);
		approveDto.setFixedInt(fixedInt);
		approveDto.setMaxInt(maxInt);
		approveDto.setNoIntYN(noIntYN);
		approveDto.setNoIntOpt(noIntOpt);
		approveDto.setPointUseYN(pointUseYN);
		approveDto.setBlockCard(blockCard);

		approveDto.setBlockBin(blockBin);

		resultJSONObject = webService.requestDealApprove(approveDto);
		resultString = resultJSONObject.toString();

		if (!resultString.equals("{}")) {
			resultCode = resultJSONObject.getString("RESULT_CODE");
			resultMsg = resultJSONObject.getString("RESULT_MSG");

			if (resultCode.equals("00")) {
				txnId = resultJSONObject.getString("TXN_ID");
				merchantTxnNum = resultJSONObject.getString("MERCHANT_TXN_NUM");
				prDt = resultJSONObject.getString("PR_DT");
			}

			kakao.setResultCode(resultCode);
			kakao.setResultMsg(resultMsg);
			kakao.setTxnId(txnId);
			kakao.setMerchantTxnNum(merchantTxnNum);
			kakao.setPrDt(prDt);
		}


		return kakao;
	}

	public Map<String, Object> kakaoPayRequest(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		// 모듈이 설치되어 있는 경로 설정
		CnsPayWebConnector connector = payment.getConnector();

		//전문 insert
		OmsPaymentif omsPaymentif = new OmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());
		omsPaymentif = ((KakaoService) getThis()).insertPaymentTransferNewTx(omsPaymentif, payment.getConnector());

		// 1. 로그 디렉토리 생성 : cnsPayHome/log 로 생성
//		connector.setLogHome(Config.getString("kakao.log.path"));
//		connector.setCnsPayHome(Config.getString("kakao.config.path"));
		
		// 2. 요청 페이지 파라메터 셋팅 (controller에서 setting)
//		connector.setRequestData(request);

		// 3. 추가 파라메터 셋팅
		connector.addRequestData("actionType", "PY0");              // actionType : CL0 취소, PY0 승인
//		connector.addRequestData("MallIP", request.getRemoteAddr());// 가맹점 고유 ip

		//가맹점키 셋팅 (MID 별로 틀림) 
//		connector.addRequestData("EncodeKey", Config.getString("kakao.encodeKey"));

		// 4. CNSPAY Lite 서버 접속하여 처리
		connector.requestAction();

		// 5. 결과 처리
		String resultCode = connector.getResultData("ResultCode");      // 결과코드 (정상 :3001 , 그 외 에러)
		String resultMsg = connector.getResultData("ResultMsg");        // 결과메시지
		String authDate = connector.getResultData("AuthDate");          // 승인일시 YYMMDDHH24mmss
		String authCode = connector.getResultData("AuthCode");          // 승인번호
		String buyerName = connector.getResultData("BuyerName");        // 구매자명
		String goodsName = connector.getResultData("GoodsName");        // 상품명
		String payMethod = connector.getResultData("PayMethod");        // 결제수단
		String mid = connector.getResultData("MID");                    // 가맹점ID
		String tid = connector.getResultData("TID");                    // 거래ID
		String moid = connector.getResultData("Moid");                  // 주문번호
		String amt = connector.getResultData("Amt");                    // 금액
		String cardCode = connector.getResultData("CardCode");          // 카드사 코드
		String cardName = connector.getResultData("CardName");          // 결제카드사명
		String cardQuota = connector.getResultData("CardQuota");        // 할부개월수 ex) 00:일시불,02:2개월
		String cardInterest = connector.getResultData("CardInterest");  // 무이자 여부 (0:일반, 1:무이자)
		String cardCl = connector.getResultData("CardCl");              // 체크카드여부 (0:일반, 1:체크카드)
		String cardBin = connector.getResultData("CardBin");            // 카드BIN번호
		String cardPoint = connector.getResultData("CardPoint");        // 카드사포인트사용여부 (0:미사용, 1:포인트사용, 2:세이브포인트사용)

		//부인방지토큰값
//		String nonRepToken = request.getParameter("NON_REP_TOKEN");

		boolean paySuccess = false;                                     // 결제 성공 여부

		/** 위의 응답 데이터 외에도 전문 Header와 개별부 데이터 Get 가능 */
		if (payMethod.equals("CARD")) {                                   //신용카드
			if (resultCode.equals("3001"))
				paySuccess = true;            // 결과코드 (정상 :3001 , 그 외 에러)
		}

		if (paySuccess) {
			try {
				// 결제 성공
				this.savePayment(payment, connector); // oms_payment 등록
				((KakaoService) getThis()).updatePaymentTransferNewTx(omsPaymentif, connector);
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			} catch (Exception e) {
				((KakaoService) getThis()).updatePaymentTransferNewTx(omsPaymentif, connector);
				// 결제 취소처리.
				// 취소금액.
				payment.getConnector().addRequestData("CancelAmt", payment.getPaymentAmt().toString());
				payment.getConnector().addRequestData("PartialCancelCode", "0"); // 전체취소
				payment.getConnector().addRequestData("CancelMsg", "상점 결제 DB 오류.");
				// 가맹점ID
				String MID = Config.getString("kakao.mid");
				payment.getKakao().setMID(MID);
				payment.getKakao().setCancelAmt(payment.getPaymentAmt().toString());
				this.kakaopayCancelRequest(payment);
				result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}
		} else {
			// 결제 실패
			((KakaoService) getThis()).updatePaymentTransferNewTx(omsPaymentif, connector);
			result.put(BaseConstants.RESULT_MESSAGE, resultMsg);
		}

		return result;
	}

	public OmsPaymentif insertPaymentTransferNewTx(OmsPaymentif omsPaymentif, CnsPayWebConnector connector) throws Exception {
		logger.debug("====================== request KAKAO =====================");
		logger.debug(connectorToString(connector).toString());
		logger.debug("====================== request KAKAO =====================");
		omsPaymentif.setPaymentReqData(connectorToString(connector).toString());
		dao.insertOneTable(omsPaymentif);

		return omsPaymentif;
	}

	public void updatePaymentTransferNewTx(OmsPaymentif omsPaymentif, CnsPayWebConnector connector) throws Exception {

		logger.debug("====================== response KAKAO =====================");
		logger.debug(connectorToString(connector).toString());
		logger.debug("====================== response KAKAO =====================");

		omsPaymentif.setPaymentIfResult(connector.getResultData("ResultCode"));
		omsPaymentif.setPaymentReturnData(connectorToString(connector).toString());
		dao.updateOneTable(omsPaymentif);
	}

	public void setPaymentData(OmsPayment payment, CnsPayWebConnector connector) throws Exception {
		payment.setPgShopId(connector.getResultData("MID")); // 상점ID
		payment.setPgApprovalNo(connector.getResultData("TID")); // 승인번호
		payment.setPaymentBusinessCd(connector.getResultData("CardCode")); // 결제사
		payment.setPaymentBusinessNm(connector.getResultData("CardName")); // 결제사명
		payment.setPaymentDt("20" + connector.getResultData("AuthDate")); // 결제일시

		/////////////// 카드 ////////////////////////
		payment.setCreditcardNo(connector.getResultData("CardBin") + "********"); // 카드번호
		String interest = connector.getResultData("CardInterest");
		if ("1".equals(interest)) {
			payment.setInterestFreeYn("Y"); // 무이자여부
		} else {
			payment.setInterestFreeYn("N"); // 무이자여부
		}

		// 부분취소가능여부
		String partialCancel = connector.getResultData("CcPartCl").trim();
		if ("0".equals(partialCancel)) {
			payment.setPartialCancelYn(BaseConstants.YN_N);
		} else if ("1".equals(partialCancel)) {
			payment.setPartialCancelYn(BaseConstants.YN_Y);
		} else {
			payment.setPartialCancelYn(BaseConstants.YN_N);
		}

		String installment = connector.getResultData("CardQuota");

		// 할부개월수
		if (CommonUtil.isNotEmpty(installment)) {
			payment.setInstallmentCnt(new BigDecimal(installment));
		} else {
			payment.setInstallmentCnt(new BigDecimal(0));
		}

		payment.setEscrowYn(BaseConstants.YN_N); // ESCROW
		/////////////// 카드 ////////////////////////
	}

	public void savePayment(OmsPayment payment, CnsPayWebConnector connector) throws Exception {
		this.setPaymentData(payment, connector);
		dao.insertOneTable(payment);
	}

	/**
	 * 
	 * @Method Name : kakaopayCancelRequest
	 * @author : dennis
	 * @date : 2016. 9. 19.
	 * @description : KAKAO 결제취소.
	 *
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> kakaopayCancelRequest(OmsPayment payment) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		//전문 insert
		OmsPaymentif omsPaymentif = new OmsPaymentif();
		omsPaymentif.setOrderId(payment.getOrderId());
		omsPaymentif.setPaymentNo(payment.getPaymentNo());
		omsPaymentif = ((KakaoService) getThis()).insertPaymentTransferNewTx(omsPaymentif, payment.getConnector());

		Kakao kakao = payment.getKakao();
		CnsPayWebConnector connector = payment.getConnector();

		String ediDate = KakaoUtil.getyyyyMMddHHmmss(); // 전문생성일시
		String encodeKey = Config.getString("kakao.encodeKey");

		////////위변조 처리/////////
		//결제 취소 요청용 키값
		String md_src = ediDate + kakao.getMID() +kakao.getCancelAmt();
		String hash_String = KakaoUtil.SHA256Salt(md_src, encodeKey);


//		CnsPayWebConnector connector = new CnsPayWebConnector();

		// 1. 로그 디렉토리 생성
//		connector.setLogHome("가맹점의 로그파일 생성 위치");
//		connector.setCnsPayHome("cnspay.properties파일 위치");

		// 2. 요청 페이지 파라메터 셋팅
//		connector.setRequestData(request);

		// 3. 추가 파라메터 셋팅
		connector.addRequestData("actionType", "CL0");
		connector.addRequestData("EdiDate", ediDate);
		connector.addRequestData("EncryptData", hash_String);
		connector.addRequestData("EncodeKey", encodeKey);
//		connector.addRequestData("CancelIP", request.getRemoteAddr());		

		// 4. CNSPAY Lite 서버 접속하여 처리
		connector.requestAction();

		// 5. 결과 처리
		String resultCode = connector.getResultData("ResultCode"); // 결과코드 (정상 :2001(취소성공), 그 외 에러)
		String resultMsg = connector.getResultData("ResultMsg"); // 결과메시지
		String cancelAmt = connector.getResultData("CancelAmt"); // 취소금액
		String cancelDate = connector.getResultData("CancelDate"); // 취소일
		String cancelTime = connector.getResultData("CancelTime"); // 취소시간
		String cancelNum = connector.getResultData("CancelNum"); // 취소번호
		String payMethod = connector.getResultData("PayMethod"); // 취소 결제수단
		String mid = connector.getResultData("MID"); // MID
		String tid = connector.getResultData("TID"); // TID
		String errorCD = connector.getResultData("ErrorCD"); // 에러코드
		String errorMsg = connector.getResultData("ErrorMsg"); // 에러메시지
		String AuthDate = cancelDate + cancelTime; // 취소거래시간
		String stateCD = connector.getResultData("StateCD"); // 거래상태코드 (0: 승인, 1:전취소, 2:후취소)
		String ccPartCl = connector.getResultData("CcPartCl"); // 부분취소 가능여부 (0:부분취소불가, 1:부분취소가능)
		String PreCancelCode = connector.getResultData("PreCancelCode"); // 취소 종류

		boolean cancelSuccess = false;

		if (resultCode.equals("2001")) {
			cancelSuccess = true;            // 결과코드 (정상 :2001 , 그 외 에러)
			
			payment.setPgCancelNo(tid);
			payment.setPgShopId(mid);
		}

		if (cancelSuccess) {
			((KakaoService) getThis()).updatePaymentTransferNewTx(omsPaymentif, connector);
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		} else {
			// 취소 실패
			((KakaoService) getThis()).updatePaymentTransferNewTx(omsPaymentif, connector);
			result.put(BaseConstants.RESULT_MESSAGE, resultMsg);
		}
		return result;
	}

	private StringBuffer connectorToString(CnsPayWebConnector connector) {

		String resultCode = connector.getResultData("ResultCode");      // 결과코드 (정상 :3001 , 그 외 에러)
		String resultMsg = connector.getResultData("ResultMsg");        // 결과메시지
		String authDate = connector.getResultData("AuthDate");          // 승인일시 YYMMDDHH24mmss
		String authCode = connector.getResultData("AuthCode");          // 승인번호
		String buyerName = connector.getResultData("BuyerName");        // 구매자명
		String goodsName = connector.getResultData("GoodsName");        // 상품명
		String payMethod = connector.getResultData("PayMethod");        // 결제수단
		String mid = connector.getResultData("MID");                    // 가맹점ID
		String tid = connector.getResultData("TID");                    // 거래ID
		String moid = connector.getResultData("Moid");                  // 주문번호
		String amt = connector.getResultData("Amt");                    // 금액
		String cardCode = connector.getResultData("CardCode");          // 카드사 코드
		String cardName = connector.getResultData("CardName");          // 결제카드사명
		String cardQuota = connector.getResultData("CardQuota");        // 할부개월수 ex) 00:일시불,02:2개월
		String cardInterest = connector.getResultData("CardInterest");  // 무이자 여부 (0:일반, 1:무이자)
		String cardCl = connector.getResultData("CardCl");              // 체크카드여부 (0:일반, 1:체크카드)
		String cardBin = connector.getResultData("CardBin");            // 카드BIN번호
		String cardPoint = connector.getResultData("CardPoint");        // 카드사포인트사용여부 (0:미사용, 1:포인트사용, 2:세이브포인트사용)

		String acquCardCode = connector.getResultData("AcquCardCode");        // 카드코드(매입사)
		String ccPartCl = connector.getResultData("CcPartCl");        // 부분취소 가능여부

		String cancelMsg = connector.getResultData("CancelMsg");        // 취소사유
		String partialCancelCode = connector.getResultData("PartialCancelCode");        // 부분취소구분
		String cancelAmt = connector.getResultData("CancelAmt");        // 취소금액

		StringBuffer sb = new StringBuffer();
		sb.append("[" + resultCode + "] " + resultMsg);
		sb.append("\n");
//		sb.append("------------------------resultCode = \t" + resultCode + "\n");
//		sb.append("------------------------resultMsg = \t" + resultMsg + "\n");
		sb.append("------------------------authDate = \t" + authDate + "\n");
		sb.append("------------------------authCode = \t" + authCode + "\n");
		sb.append("------------------------buyerName = \t" + buyerName + "\n");
		sb.append("------------------------goodsName = \t" + goodsName + "\n");
		sb.append("------------------------payMethod = \t" + payMethod + "\n");
		sb.append("------------------------mid = \t" + mid + "\n");
		sb.append("------------------------tid = \t" + tid + "\n");
		sb.append("------------------------moid = \t" + moid + "\n");
		sb.append("------------------------amt = \t" + amt + "\n");
		sb.append("------------------------cardCode = \t" + cardCode + "\n");
		sb.append("------------------------cardName = \t" + cardName + "\n");
		sb.append("------------------------cardQuota = \t" + cardQuota + "\n");
		sb.append("------------------------cardInterest = \t" + cardInterest + "\n");
		sb.append("------------------------cardCl = \t" + cardCl + "\n");
		sb.append("------------------------cardBin = \t" + cardBin + "\n");
		sb.append("------------------------cardPoint = \t" + cardPoint + "\n");
		sb.append("------------------------acquCardCode = \t" + acquCardCode + "\n");
		sb.append("------------------------ccPartCl = \t" + ccPartCl + "\n");
		sb.append("------------------------partialCancelCode = \t" + partialCancelCode + "\n");
		sb.append("------------------------cancelAmt = \t" + cancelAmt + "\n");
		sb.append("------------------------cancelMsg = \t" + cancelMsg + "\n");

		return sb;
	}


}
