package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gcp.ccs.model.CcsMessage;
import gcp.ccs.service.BaseService;
import gcp.common.util.CodeUtil;
import gcp.external.model.membership.MembershipPointRes;
import gcp.external.model.membership.MembershipUsePointReq;
import gcp.external.service.KakaoService;
import gcp.external.service.MembershipService;
import gcp.external.service.PgService;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.service.MemberService;
import gcp.mms.service.MypageService;
import gcp.oms.model.OmsClaimWrapper;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.pg.lgu.LguAccountCertify;
import gcp.oms.model.search.OmsPaymentSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import kr.co.lgcns.module.lite.CnsPayWebConnector;

@Service("paymentService")
public class PaymentService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private PgService pgService;

	@Autowired
	private KakaoService	kakaoService;

	@Autowired
	private MembershipService	membershipService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private OrderService		orderService;
	
	@Autowired
	private MypageService		mypageService;
	
	@Autowired
	private ClaimTmsService		claimTmsService;

	@SuppressWarnings("unchecked")
	public List<OmsPayment> selectList(String queryId, OmsPaymentSearch omsPaymentSearch) throws Exception {
		return (List<OmsPayment>) dao.selectList(queryId, omsPaymentSearch);
	}

	public OmsPayment selectOne(String orderId) throws Exception {
		orderId = orderId.replace("'", "");
		return (OmsPayment) dao.selectList("oms.payment.selectOne", orderId);
	}

	public Object selectOne(String queryId, Object object) throws Exception {
		return dao.selectOne(queryId, object);
	}
	
	public void createKakaoRequestParam(OmsPayment omsPayment, HttpServletRequest request) throws Exception {
		CnsPayWebConnector connector = new CnsPayWebConnector();
		// 1. 로그 디렉토리 생성 : cnsPayHome/log 로 생성
		connector.setLogHome(Config.getString("kakao.log.path"));
		connector.setCnsPayHome(Config.getString("kakao.config.path"));

		// Map map = FrontUtil.getParameterMap(request);
		// orderPay.getKakao().setRequestParam(map.toString());
		// 2. 요청 페이지 파라메터 셋팅 (controller에서 setting)
		connector.setRequestData(request);
		omsPayment.setConnector(connector);
	}

	/**
	 * 
	 * @Method Name : insertRefundByDeposit
	 * @author : victor
	 * @date : 2016. 11. 2.
	 * @description : 
	 *
	 * @param omsPayment
	 * @return
	 * @throws Exception
	 */
	public int insertRefundByDeposit(OmsPayment omsPayment) throws Exception {

		// 보유예치금 차감.
		Map<String, String> map = (Map<String, String>) mypageService.insertDepositRefundData(omsPayment);

		if (BaseConstants.DEPOSIT_REFUND_REQ_RESULT_SUCCESS.equals(map.get("code"))) {
			omsPayment.setDepositNo(new BigDecimal(map.get("depositNo")));
			omsPayment.setPaymentAmt(new BigDecimal(map.get("balanceAmt")));

			Map<String, Object> result = this.savePayment(omsPayment);
			String resultFlag = (String) result.get(BaseConstants.RESULT_FLAG);
			if (BaseConstants.RESULT_FLAG_FAIL.equals(resultFlag)) { // TODO - error message
				throw new ServiceException("oms.common.error", new Object[] { result.get(BaseConstants.RESULT_MESSAGE) });
			} else {
				return 1;
			}
		} else {
			throw new ServiceException("oms.common.error", new Object[] { map.get("msg") });
		}
	}

	public int update(OmsPayment omsPayment) throws Exception {
		
		if ("PAYMENT_STATE_CD.REFUND_CANCEL".equals(omsPayment.getPaymentStateCd())) {
			if ("REFUND_REASON_CD.REFUNDDEPOSIT".equals(omsPayment.getRefundReasonCd())) {
				// 보유예치금 증감.
				omsPayment.setPaymentAmt(omsPayment.getPaymentAmt().negate());// 부호바꾸기
				Map<String, String> map = (Map<String, String>) mypageService.insertDepositRefundData(omsPayment);
				if (!BaseConstants.DEPOSIT_REFUND_REQ_RESULT_SUCCESS.equals(map.get("code"))) {
					throw new ServiceException("oms.common.error", new Object[] { map.get("msg") });
				}
				omsPayment.setPaymentAmt(omsPayment.getPaymentAmt().negate());// 부호바꾸기
			}
		}
		
		int retVal = dao.update("oms.payment.refundPayment", omsPayment);
		// sms 발송
		if (retVal > 0) {
			if ("PAYMENT_STATE_CD.REFUND".equals(omsPayment.getPaymentStateCd())) {
				if (!StringUtils.isEmpty(omsPayment.getOrderId())) {
					List<OmsPayment> omsPayments = new ArrayList<OmsPayment>();
					List<OmsOrderproduct> omsOrderproducts = new ArrayList<OmsOrderproduct>();
					String claimTypeCd = "CLAIM_TYPE_CD.RETURN";
					String smsCode = "127";

					omsPayments.add(omsPayment);
					if ("REFUND_REASON_CD.CANCEL".equals(omsPayment.getRefundReasonCd())) {
						claimTypeCd = "CLAIM_TYPE_CD.CANCEL";
						smsCode = "104";
						// } else if ("REFUND_REASON_CD.RETURN".equals(omsPayment.getRefundReasonCd())) {
						// claimTypeCd = "CLAIM_TYPE_CD.RETURN";
						// smsCode = "127";
					}

					OmsClaimWrapper omsClaimWrapper = new OmsClaimWrapper();
					omsClaimWrapper.setClaimStateCd("CLAIM_STATE_CD.COMPLETE");
					omsClaimWrapper.setClaimTypeCd(claimTypeCd);
					omsClaimWrapper.setOmsPayments(omsPayments);
					omsClaimWrapper.setOmsOrderproducts(omsOrderproducts);

					claimTmsService.createClaimTms(omsClaimWrapper, smsCode);
				}
			}
		}
		
		return retVal;
	}
	
	public int update(String queryId, Object omsPayment) throws Exception {
		return dao.update(queryId, omsPayment);
	}	
	
	public CcsMessage updatePaymentmethod(OmsPayment omsPayment) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("결제 타입변경 중 알수 없는 에러가 발생하였습니다.");
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		OmsPayment omsPaymentOld = new OmsPayment();
		omsPaymentOld.setPaymentMethodCd("PAYMENT_METHOD_CD.VIRTUAL");
		omsPaymentOld.setPaymentStateCd("PAYMENT_STATE_CD.CANCEL");
		omsPaymentOld.setCancelDt(currentDate);
		omsPaymentOld.setUpdDt(currentDate);
		omsPaymentOld.setUpdId(omsPayment.getUpdId());
		omsPaymentOld.setOrderId(omsPayment.getOrderId());

		// 1. oms_order 상태값 갱신
		// 2. oms_orderproduct 상태값 갱신
		// 3. oms_payment 기존값 취소 : 가상계좌는 PG 와 취소연동을 하지 않는다.
		// 4. oms_payment 새 결제값 등록 : 
		if (!StringUtils.isEmpty(omsPayment.getOrderId())) {
			// 주문상태 갱신
			OmsOrder omsOrder = new OmsOrder();
			omsOrder.setOrderId(omsPayment.getOrderId());
			omsOrder = (OmsOrder) dao.selectOneTable(omsOrder);
			if (this.updateOrderStatus(omsOrder) > 0) {
				// 기존 결제수단(가상계좌) 취소처리
				int updCnt = dao.update("oms.payment.updatePayment", omsPaymentOld);
				if (updCnt > 0) {
					omsPayment.setUpdDt(currentDate);
					omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.PAYMENT");// 결제유형(결제)
					if (!StringUtils.isEmpty(omsOrder.getMemberNo())) {
						omsPayment.setMemberNo(omsOrder.getMemberNo());
					}
					ccsmessage = this.insertNewPayment(omsPayment);
				} else {
					ccsmessage.setResultMessage("결제 타입변경에 실패하였습니다.");
					throw new ServiceException("oms.pg.error", new Object[] { "결제 타입변경에 실패하였습니다." });
				}
			}
		}

		return ccsmessage;
	}
	
	public CcsMessage insertNewPayment(OmsPayment omsPayment) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("결제 연동 중 알수 없는 에러가 발생하였습니다.");

		omsPayment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");// 결제상태(결제완료)
		omsPayment.setCancelDt("");

		omsPayment.setPaymentFee(BigDecimal.ZERO);
		omsPayment.setInterestFreeYn("N");
		omsPayment.setEscrowYn("N");
		omsPayment.setEscrowIfYn("N");
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = this.savePayment(omsPayment);
			String resultFlag = (String) result.get(BaseConstants.RESULT_FLAG);
			if (BaseConstants.RESULT_FLAG_FAIL.equals(resultFlag)) {
				ccsmessage.setResultMessage("PG 연동에 실패하였습니다.");// TODO - error message
				throw new ServiceException("oms.pg.error", new Object[] { result.get(BaseConstants.RESULT_MESSAGE) });
			}
		} catch (Exception e) {
			ccsmessage.setResultMessage("PG 연동에 실패하였습니다.");// TODO - error message
			throw new ServiceException("oms.pg.error", new Object[] { result.get(BaseConstants.RESULT_MESSAGE) });
		}
		ccsmessage.setReturnObject(omsPayment);
		ccsmessage.setSuccess(true);
		ccsmessage.setResultMessage("새로운 결제 연동 추가에 성공하였습니다.");

		return ccsmessage;
	}
	
	/**
	 * 환불계좌 인증, 등록
	 * 
	 * @Method Name : accountCertify
	 * @author : victor
	 * @date : 2016. 8. 25.
	 * @description :
	 *
	 * @param omsPayment
	 * @return CcsMessage
	 */
	public CcsMessage accountCertify(OmsPayment omsPayment) {
		LguAccountCertify accountCertify = new LguAccountCertify();
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);
		try {
			accountCertify.setLgdGubun("2"); // 고정
			accountCertify.setLgdBankCode(omsPayment.getPaymentBusinessCd());
			accountCertify.setLgdAccountNo(omsPayment.getRefundAccountNo());
			accountCertify.setLgdName(omsPayment.getAccountHolderName());
			accountCertify.setLgdCheckNhyn("BANK_CD.012".equals(omsPayment.getPaymentBusinessCd()) ? "Y" : "N"); // 단위농협
			if (StringUtils.isEmpty(omsPayment.getPaymentBusinessCd())) {
				throw new ServiceException("mms.error.refundaccount.parameter", new String[] { "은행정보" });
			} else if (StringUtils.isEmpty(omsPayment.getRefundAccountNo())) {
				throw new ServiceException("mms.error.refundaccount.parameter", new String[] { "환불계좌번호" });
			} else if (StringUtils.isEmpty(omsPayment.getAccountHolderName())) {
				throw new ServiceException("mms.error.refundaccount.parameter", new String[] { "예금주" });
			}
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(e.getMessage());
			logger.error(" {} ", e.getMessage());
		} finally {
			if (ccsmessage.isSuccess()) {
				ccsmessage = pgService.accountCertify(accountCertify);

				try {
					// 회원일 경우 환불계좌 업데이트
					if (ccsmessage.isSuccess() && !StringUtils.isEmpty(omsPayment.getMemberNo())) {
						MmsMemberZts mmsMemberZts = new MmsMemberZts();
						mmsMemberZts.setAccountAuthDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
						mmsMemberZts.setAccountHolderName(omsPayment.getAccountHolderName());
						mmsMemberZts.setAccountNo(omsPayment.getRefundAccountNo());
						mmsMemberZts.setBankCd(omsPayment.getPaymentBusinessCd());
						mmsMemberZts.setBankName(omsPayment.getPaymentBusinessNm());
						mmsMemberZts.setMemberNo(omsPayment.getMemberNo());
						if (dao.update("mms.member.updateAccount", mmsMemberZts) < 1) {
							// throw new ServiceException("mms.error.refundaccount", new String[] { res.getRes_msg() });
							ccsmessage.setSuccess(false);
							ccsmessage.setResultCode("-999");
							ccsmessage.setResultMessage("회원의 환불계좌 등록이 실패하였습니다.");// TODO - 메세지정의
						}
						ccsmessage.setReturnObject(mmsMemberZts);
					}
				} catch (Exception e) {
					ccsmessage.setSuccess(false);
					ccsmessage.setResultCode("-999");
					ccsmessage.setResultMessage("회원 환불계좌 등록중 에러가 발생하였습니다.");// TODO - 메세지정의
					logger.error(" 회원 환불계좌 등록 에러 : {} ", e.getMessage());
				}
			}
		}
		return ccsmessage;
	}

	//결제
	/**
	 * 결제처리
	 * @Method Name : savePayment
	 * @author : victor
	 * @date : 2016. 11. 5.
	 * @description : 
	 *	<pre>1. PG(LGD, KAKAO) 연동 or 포인트/예치금 연동</pre>
	 *	<pre>2. oms_payment 등록</pre>
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> savePayment(OmsPayment payment) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		// 결제수단
		String paymentMethodCd = payment.getPaymentMethodCd();
		BigDecimal paymentNo = payment.getPaymentNo();
		if (CommonUtil.isEmpty(paymentNo)) {
			paymentNo = getNewPaymentNo();
			payment.setPaymentNo(paymentNo);
		}

		// payment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
		// payment.setPaymentTypeCd("PAYMENT_TYPE_CD.PAYMENT");//결제유형(결제)

		payment.setEscrowIfYn(BaseConstants.YN_N);

		boolean pgres = false;

		if ("PAYMENT_METHOD_CD.CARD".equals(paymentMethodCd)) { // 카드
			payment.setMajorPaymentYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);

			pgres = true;
		} else if ("PAYMENT_METHOD_CD.VIRTUAL".equals(paymentMethodCd)) { // 가상계좌
			payment.setMajorPaymentYn(BaseConstants.YN_Y);
			payment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT_READY");
			pgres = true;
		} else if ("PAYMENT_METHOD_CD.TRANSFER".equals(paymentMethodCd)) { // 계좌이체
			payment.setMajorPaymentYn(BaseConstants.YN_Y);
			pgres = true;
		} else if ("PAYMENT_METHOD_CD.MOBILE".equals(paymentMethodCd)) { // 휴대폰
			payment.setMajorPaymentYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);
			pgres = true;
		} else if ("PAYMENT_METHOD_CD.KAKAO".equals(paymentMethodCd)) { // 카카오
			payment.setMajorPaymentYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);
			pgres = true;
		} else if ("PAYMENT_METHOD_CD.DEPOSIT".equals(paymentMethodCd)) { // 예치금
			payment.setMajorPaymentYn(BaseConstants.YN_N);
			payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
			payment.setPartialCancelYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);
			dao.insertOneTable(payment);

			// 예치금 까기.
			MmsDeposit mmsDeposit = new MmsDeposit();
			mmsDeposit.setMemberNo(payment.getMemberNo());
			mmsDeposit.setDepositAmt(payment.getPaymentAmt().negate());
			mmsDeposit.setDepositTypeCd("DEPOSIT_TYPE_CD.PAYMENT");
			mmsDeposit.setOrderId(payment.getOrderId());
			Map resultDeposit = memberService.saveMemberDeposit(mmsDeposit);
			String resultFlag = (String) resultDeposit.get("resultCode");
			if (!BaseConstants.RESULT_FLAG_SUCCESS.equals(resultFlag)) {
				throw new ServiceException("oms.bind.error", new Object[] { resultDeposit.get("resultMsg") });
			}
		} else if ("PAYMENT_METHOD_CD.VOUCHER".equals(paymentMethodCd)) { // 상품권
			payment.setMajorPaymentYn(BaseConstants.YN_N);
			payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
			payment.setPartialCancelYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);
			dao.insertOneTable(payment);
		} else if ("PAYMENT_METHOD_CD.POINT".equals(paymentMethodCd)) { // 포인트
			payment.setMajorPaymentYn(BaseConstants.YN_N);
			payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
			payment.setPartialCancelYn(BaseConstants.YN_Y);
			payment.setEscrowYn(BaseConstants.YN_N);
			dao.insertOneTable(payment);

			MembershipUsePointReq req = new MembershipUsePointReq();
			//// req.setTrsc_biz_dv_cd("31");// 포인트사용
			// req.setMmbCertDvCd("2");// 회원no
			// req.setMmbCertDvVlu(payment.getMemberNo().toString());
			// req.setTrscRsnCd("2001");// 거래사유(상품/서비스 구매사용)
			// req.setUniqRcgnNo(payment.getOrderId()); // 주문번호
			// req.setPintUseTypCd("11");// 포인트사용유형코드 (11:구매)
			String paymentAmt = payment.getPaymentAmt().toString();
			// req.setUsePint(paymentAmt); // 사용포인트

			// 사용공통
			req.setTrscBizDvCd("31");// 21:포인트사용거래, 22:포인트사용거래취소

			// 사용 데이터
			req.setTrscRsnCd("CS01");// 거래사유코드 CS01:0to7 쇼핑몰 상품구매
			req.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
			req.setMmbCertDvVlu(payment.getMemberNo().toString());// 회원번호(필수)
			req.setTotSelAmt(paymentAmt);// 총매출금액 ( 구매금액 )(필수)
			req.setTotDcAmt("0");// 총할인금액 (쿠폰할인금액)(옵션)
			req.setMbrshDcAmt("0");// 멤버십할인금액(옵션)
			req.setPintUseTypCd("11");// 포인트사용유형코드, 일반구매(필수)
			req.setUsePint("0");// 사용포인트 (한거래를 사용요청 후 적립요청을 할 경우 Setting)(옵션)
			req.setUniqRcgnNo(payment.getOrderId());// 고유관리번호, 주문번호/취소번호등(필수)

			req.setPintUseTypCd("11");// 포인트사용유형코드(필수, 11’- 일반, ‘21’- 이벤트)
			req.setUsePint(paymentAmt);// 사용포인트(필수)
			req.setRmk(null);// 비고(옵션)

			MembershipPointRes res = membershipService.useMemberPoint(req);

			if ("00000".equals(res.getResCd())) {
				// String usePoint = res.getUse_pint();
				// if (!usePoint.equals(paymentAmt)) {
				//
				// }
			} else {
				throw new ServiceException("oms.pg.error", new Object[] { res.getResMsg() });
			}
		} else if ("PAYMENT_METHOD_CD.CASH".equals(paymentMethodCd)) { // 현금
			payment.setMajorPaymentYn(BaseConstants.YN_N);
			payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1)); // TODO -
			payment.setPartialCancelYn(BaseConstants.YN_N);
			payment.setInterestFreeYn(BaseConstants.YN_N);
			payment.setEscrowYn(BaseConstants.YN_N);
			payment.setPaymentFee(BigDecimal.ZERO);

			dao.insertOneTable(payment);
		}

		if (pgres) {

			Map<String, Object> pgResult = new HashMap<String, Object>();
			if ("PAYMENT_METHOD_CD.CARD".equals(paymentMethodCd)) {
				pgResult = pgService.cardRes(payment);
			} else if ("PAYMENT_METHOD_CD.VIRTUAL".equals(paymentMethodCd)) {
				pgResult = pgService.cyberRes(payment);
			} else if ("PAYMENT_METHOD_CD.KAKAO".equals(paymentMethodCd)) {
				pgResult = kakaoService.kakaoPayRequest(payment);
			} else {
				pgResult = pgService.payres(payment);
			}
			// pg 요청 실패
			if (BaseConstants.RESULT_FLAG_FAIL.equals(pgResult.get(BaseConstants.RESULT_FLAG))) {
				// throw new ServiceException("oms.pg.error", new Object[] { pgResult.get(BaseConstants.RESULT_MESSAGE) });
				result.put(BaseConstants.RESULT_MESSAGE, pgResult.get(BaseConstants.RESULT_MESSAGE));
				return result;
			} else {
				// 지금선택한결제수단 다음에도 사용.
				if (BaseConstants.YN_Y.equals(payment.getContinuePaymentMethod())) {
					MmsMemberZts memberZts = new MmsMemberZts();
					memberZts.setMemberNo(payment.getMemberNo());
					memberZts.setPaymentMethodCd(paymentMethodCd);
					memberZts.setPaymentBusinessCd(payment.getPaymentBusinessCd());

					dao.updateOneTable(memberZts);
				}
			}
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : updateRollBack
	 * @author : dennis
	 * @date : 2016. 10. 23.
	 * @description : 결제 roll back (예치금, 포인트)
	 *
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateRollBackNewTx(List<OmsPayment> orderPay, String errorMethodCd) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		for (int i = orderPay.size() - 1; i >= 0; i--) {
			OmsPayment payment = orderPay.get(i);
			BigDecimal paymentAmt = payment.getPaymentAmt();
			String paymentMethodCd = payment.getPaymentMethodCd();

			if (paymentAmt.compareTo(new BigDecimal(0)) > 0) {
//				if ("PAYMENT_METHOD_CD.DEPOSIT".equals(paymentMethodCd) && !"PAYMENT_METHOD_CD.DEPOSIT".equals(errorMethodCd)) {		//예치금
//					logger.debug("예치금 복원!!!!!!!!!");
//
//					MmsDeposit mmsDeposit = new MmsDeposit();
//					mmsDeposit.setMemberNo(payment.getMemberNo());
//					mmsDeposit.setDepositAmt(payment.getPaymentAmt());
//					mmsDeposit.setDepositTypeCd("DEPOSIT_TYPE_CD.PAYMENT");
//					mmsDeposit.setOrderId(payment.getOrderId());
//					Map resultDeposit = memberService.saveMemberDeposit(mmsDeposit);
//					String resultFlag = (String) resultDeposit.get("resultCode");
//					if (!BaseConstants.RESULT_FLAG_SUCCESS.equals(resultFlag)) {
//						throw new ServiceException("oms.bind.error", new Object[] { resultDeposit.get("resultMsg") });
//					}
//				} else 

				if ("PAYMENT_METHOD_CD.POINT".equals(paymentMethodCd)
						&& !"PAYMENT_METHOD_CD.POINT".equals(errorMethodCd)) { // 포인트

					logger.debug("포인트 복원!!!!!!!!!");
					//TODO 포인트 복원.
				}
			}
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : saveCasnote
	 * @author : dennis
	 * @date : 2016. 9. 22.
	 * @description : 가상계좌 입금처리
	 *
	 * @param omsOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveCasnote(OmsOrder omsOrder) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String orderId = omsOrder.getOrderId();
		String orderStateCd = omsOrder.getOrderStateCd();

		omsOrder = (OmsOrder) dao.selectOneTable(omsOrder);

		String orgOrderStateCd = omsOrder.getOrderStateCd();

		if ("ORDER_STATE_CD.REQ".equals(orderStateCd)) {
			if ("ORDER_STATE_CD.REQ".equals(orgOrderStateCd)) {

			} else {

			}
		} else if ("ORDER_STATE_CD.PAYED".equals(orderStateCd)) {
			if ("ORDER_STATE_CD.REQ".equals(orgOrderStateCd)) {

				this.updateOrderStatus(omsOrder);

				OmsPayment omsPayment = new OmsPayment();
				omsPayment.setOrderId(orderId);
				omsPayment.setPaymentMethodCd("PAYMENT_METHOD_CD.VIRTUAL");
				omsPayment = (OmsPayment) dao.selectOne("oms.order.getPaymentData", omsPayment);

				OmsPayment omsPayment2 = new OmsPayment();
				omsPayment2.setOrderId(orderId);
				omsPayment2.setPaymentNo(omsPayment.getPaymentNo());
				omsPayment2.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
				omsPayment2.setUpdId("PG");
				dao.updateOneTable(omsPayment2);

			} else {

			}
		} else if ("ORDER_STATE_CD.CANCEL".equals(orderStateCd)) {
			if ("ORDER_STATE_CD.PAYED".equals(orgOrderStateCd)) {
				//TODO 주문취소
			} else {

			}
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}
	
	/**
	 * 주문상태값 갱신
	 * @Method Name : updateOrderStatus
	 * @author : victor
	 * @date : 2016. 11. 5.
	 * @description : 
	 *	<pre>주문상태 : 결제완료</pre>
	 *	<pre>주문배송상태 : 출고지시대기</pre>
	 *	<pre>주문상품상태 : 출고지시대기</pre>
	 * @param omsOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private int updateOrderStatus(OmsOrder omsOrder) throws Exception {
		int retVal = 0;
		if ("ORDER_TYPE_CD.GIFT".equals(omsOrder.getOrderTypeCd())) { // 기프트콘일때 결제완료.
			omsOrder.setOrderStateCd("ORDER_STATE_CD.PAYED");
		} else {
			omsOrder.setOrderStateCd("ORDER_STATE_CD.COMPLETE");
		}
		omsOrder.setOrderDeliveryStateCd("ORDER_DELIVERY_STATE_CD.READY");
		dao.updateOneTable(omsOrder);

		List<OmsOrderproduct> productList = (List<OmsOrderproduct>) dao.selectList("oms.order.getOrderproductList", omsOrder);

		for (OmsOrderproduct omsOrderproduct : productList) {
			omsOrderproduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");
			dao.updateOneTable(omsOrderproduct);

			orderService.updateStockQty(omsOrderproduct, true, "MINUS");

			//딜재고 차감.
			String dealId = omsOrderproduct.getDealId();
			if (CommonUtil.isNotEmpty(dealId)) {
				orderService.updateDealStockQty(omsOrderproduct, true, "");
			}
		}
		retVal = 1;
		return retVal;
	}

	public BigDecimal getNewPaymentNo() {
		return (BigDecimal) dao.selectOne("oms.payment.getNewPaymentNo", null);
	}

	/**
	 * 
	 * @Method Name : cardMallCheck
	 * @author : dennis
	 * @date : 2016. 11. 8.
	 * @description : 카드사몰 유입에따른 card check
	 *
	 * @param channelId
	 * @param payment
	 */
	public void cardMallCheck(String channelId, OmsPayment payment) {

		String paymentMethodCd = payment.getPaymentMethodCd();
		if (CommonUtil.isNotEmpty(channelId) && "PAYMENT_METHOD_CD.CARD".equals(paymentMethodCd)) {
			String paymentBusinessCd = "";
			if (payment.getOmsPaymentif() != null) {
				paymentBusinessCd = payment.getOmsPaymentif().getLGD_CARDTYPE();
			}
			boolean cardMallCheck = true;


			/*
			 * 신한(41) - 0011
			 * 국민(11) - 0013
			 * 삼성(51) - 0010
			 * 롯데(71) - 0009
			 * 외환(21) - 0012
			 */
			String cd = CodeUtil.getCodeValueByNote("PAYMENT_BUSINESS_CD", channelId);
			if ("0011".equals(channelId) || "0013".equals(channelId) || "0010".equals(channelId) || "0009".equals(channelId)
					|| "0012".equals(channelId)) {
				cd = cd.replaceAll("PAYMENT_BUSINESS_CD.", "");
				if (!cd.equals(paymentBusinessCd)) {
					cardMallCheck = false;
				}
			}
			if (!cardMallCheck) {
				logger.error("channel id : " + channelId);
				logger.error("code  : " + cd);
				logger.error("paymentBusinessCd : " + paymentBusinessCd);
				throw new ServiceException("oms.bind.error", new String[] { "카드사몰 카드매핑 오류입니다." });
			}
		}
	}
		
}
