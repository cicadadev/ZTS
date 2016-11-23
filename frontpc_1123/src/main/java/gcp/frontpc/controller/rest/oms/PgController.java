package gcp.frontpc.controller.rest.oms;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.external.model.Kakao;
import gcp.external.service.KakaoService;
import gcp.external.service.PgService;
import gcp.frontpc.common.util.FrontUtil;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentif;
import gcp.oms.service.OrderService;
import gcp.oms.service.PaymentService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/pg")
public class PgController {

	@Autowired
	private PgService pgService;

	@Autowired
	private KakaoService	kakaoService;

	@Autowired
	private OrderService	orderService;

	@Autowired
	private PaymentService	paymentService;

	@RequestMapping(value = "/param" ,method = RequestMethod.POST)
	public Map<String, Object> getRequestParam(@ModelAttribute OmsPaymentif omsPaymentif, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		CcsUtil.setSessionLoginInfo(omsPaymentif, true);
		omsPaymentif.setLGD_BUYERIP(SessionUtil.getIP());

		if (!CommonUtil.isEmpty(omsPaymentif.getSelectKey())) {
			if ("update".equals(omsPaymentif.getSelectKey())) {
				// 결제수단 변경
				OmsPayment omsPayment = new OmsPayment();
				omsPayment.setPaymentNo(new BigDecimal(omsPaymentif.getLGD_OID())); // 과거 결제번호로 금액 가져옴.
				omsPayment = (OmsPayment) orderService.selectOne("oms.payment.selectOne", omsPayment);
				omsPaymentif.setLGD_AMOUNT(omsPayment.getPaymentAmt().toString());
			} else if ("insert".equals(omsPaymentif.getSelectKey())) {
				// 추가 결제
				String paymentAmt = (String) orderService.selectOne("oms.claim.select.exchangeFee", omsPaymentif);
				omsPaymentif.setLGD_AMOUNT(paymentAmt);
			}
		}

		BigDecimal paymentNo = paymentService.getNewPaymentNo();
		omsPaymentif.setLGD_OID(paymentNo.toString());// 새 결제번호 세팅
		omsPaymentif.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		omsPaymentif.setLGD_TIMESTAMP((DateUtil.getCurrnetTimestamp()).toString());
		omsPaymentif.setMobileOs(FrontUtil.getMobileOs(request));
		omsPaymentif = pgService.getRequestParam(omsPaymentif);

		result.put("omsPaymentif", omsPaymentif);
		result.put("orderId", omsPaymentif.getOrderId());
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	@RequestMapping(value = "/casnote", method = { RequestMethod.POST, RequestMethod.GET })
	public String casnote(HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

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

		LGD_RESPCODE = request.getParameter("LGD_RESPCODE");
		LGD_RESPMSG = request.getParameter("LGD_RESPMSG");
		LGD_MID = request.getParameter("LGD_MID");
		LGD_OID = request.getParameter("LGD_OID");
		LGD_AMOUNT = request.getParameter("LGD_AMOUNT");
		LGD_TID = request.getParameter("LGD_TID");
		LGD_PAYTYPE = request.getParameter("LGD_PAYTYPE");
		LGD_PAYDATE = request.getParameter("LGD_PAYDATE");
		LGD_HASHDATA = request.getParameter("LGD_HASHDATA");
		LGD_FINANCECODE = request.getParameter("LGD_FINANCECODE");
		LGD_FINANCENAME = request.getParameter("LGD_FINANCENAME");
		LGD_ESCROWYN = request.getParameter("LGD_ESCROWYN");
		LGD_TIMESTAMP = request.getParameter("LGD_TIMESTAMP");
		LGD_ACCOUNTNUM = request.getParameter("LGD_ACCOUNTNUM");
		LGD_CASTAMOUNT = request.getParameter("LGD_CASTAMOUNT");
		LGD_CASCAMOUNT = request.getParameter("LGD_CASCAMOUNT");
		LGD_CASFLAG = request.getParameter("LGD_CASFLAG");
		LGD_CASSEQNO = request.getParameter("LGD_CASSEQNO");
		LGD_CASHRECEIPTNUM = request.getParameter("LGD_CASHRECEIPTNUM");
		LGD_CASHRECEIPTSELFYN = request.getParameter("LGD_CASHRECEIPTSELFYN");
		LGD_CASHRECEIPTKIND = request.getParameter("LGD_CASHRECEIPTKIND");
		LGD_PAYER = request.getParameter("LGD_PAYER");

		LGD_BUYER = request.getParameter("LGD_BUYER");
		LGD_PRODUCTINFO = request.getParameter("LGD_PRODUCTINFO");
		LGD_BUYERID = request.getParameter("LGD_BUYERID");
		LGD_BUYERADDRESS = request.getParameter("LGD_BUYERADDRESS");
		LGD_BUYERPHONE = request.getParameter("LGD_BUYERPHONE");
		LGD_BUYEREMAIL = request.getParameter("LGD_BUYEREMAIL");
		LGD_BUYERSSN = request.getParameter("LGD_BUYERSSN");
		LGD_PRODUCTCODE = request.getParameter("LGD_PRODUCTCODE");
		LGD_RECEIVER = request.getParameter("LGD_RECEIVER");
		LGD_RECEIVERPHONE = request.getParameter("LGD_RECEIVERPHONE");
		LGD_DELIVERYINFO = request.getParameter("LGD_DELIVERYINFO");

		OmsPaymentif omsPaymentif = new OmsPaymentif();
		omsPaymentif.setLGD_RESPCODE(LGD_RESPCODE);
		omsPaymentif.setLGD_RESPMSG(LGD_RESPMSG);
		omsPaymentif.setLGD_MID(LGD_MID);
		omsPaymentif.setLGD_OID(LGD_OID);
		omsPaymentif.setLGD_AMOUNT(LGD_AMOUNT);
		omsPaymentif.setLGD_TID(LGD_TID);
		omsPaymentif.setLGD_PAYTYPE(LGD_PAYTYPE);
		omsPaymentif.setLGD_PAYDATE(LGD_PAYDATE);
		omsPaymentif.setLGD_HASHDATA(LGD_HASHDATA);
		omsPaymentif.setLGD_FINANCECODE(LGD_FINANCECODE);
		omsPaymentif.setLGD_FINANCENAME(LGD_FINANCENAME);
		omsPaymentif.setLGD_ESCROWYN(LGD_ESCROWYN);
		omsPaymentif.setLGD_TIMESTAMP(LGD_TIMESTAMP);
		omsPaymentif.setLGD_ACCOUNTNUM(LGD_ACCOUNTNUM);
		omsPaymentif.setLGD_CASTAMOUNT(LGD_CASTAMOUNT);
		omsPaymentif.setLGD_CASCAMOUNT(LGD_CASCAMOUNT);
		omsPaymentif.setLGD_CASFLAG(LGD_CASFLAG);
		omsPaymentif.setLGD_CASSEQNO(LGD_CASSEQNO);
		omsPaymentif.setLGD_CASHRECEIPTNUM(LGD_CASHRECEIPTNUM);
		omsPaymentif.setLGD_CASHRECEIPTSELFYN(LGD_CASHRECEIPTSELFYN);
		omsPaymentif.setLGD_CASHRECEIPTKIND(LGD_CASHRECEIPTKIND);
		omsPaymentif.setLGD_PAYER(LGD_PAYER);
		omsPaymentif.setLGD_BUYER(LGD_BUYER);
		omsPaymentif.setLGD_PRODUCTINFO(LGD_PRODUCTINFO);
		omsPaymentif.setLGD_BUYERID(LGD_BUYERID);
		omsPaymentif.setLGD_BUYERADDRESS(LGD_BUYERADDRESS);
		omsPaymentif.setLGD_BUYERPHONE(LGD_BUYERPHONE);
		omsPaymentif.setLGD_BUYEREMAIL(LGD_BUYEREMAIL);
		omsPaymentif.setLGD_BUYERSSN(LGD_BUYERSSN);
		omsPaymentif.setLGD_PRODUCTCODE(LGD_PRODUCTCODE);
		omsPaymentif.setLGD_RECEIVER(LGD_RECEIVER);
		omsPaymentif.setLGD_RECEIVERPHONE(LGD_RECEIVERPHONE);
		omsPaymentif.setLGD_DELIVERYINFO(LGD_DELIVERYINFO);
		
		result = pgService.casnote(omsPaymentif);

		//가상계좌 입금일때 erp if data 처리
		if ("I".equals(LGD_CASFLAG.trim()) && BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get(BaseConstants.RESULT_FLAG))) {
			//ERPIF			
//			orderService.insertOmsERPIFNewTx(omsOrder);
		}

		String resultMSG = (String) result.get("resultMSG");
		return resultMSG;
	}

	@RequestMapping(value = "/receiptparam", method = { RequestMethod.POST, RequestMethod.GET })
	public OmsPaymentif cashreceiptparam(@RequestBody OmsPaymentif omsPaymentif) throws Exception {
		return pgService.getReceiptparam(omsPaymentif);
	}

	@RequestMapping(value = "/kakao/txnid", method = RequestMethod.POST)
	public Kakao getTxnId(@ModelAttribute Kakao kakao) throws Exception {
		if (!StringUtils.isEmpty(kakao.getSelectKey())) {
			if ("update".equals(kakao.getSelectKey())) {
				// 결제수단 변경
				OmsPayment omsPayment = new OmsPayment();
				omsPayment.setPaymentNo(new BigDecimal(kakao.getMerchantTxnNum())); // 과거 결제번호로 금액 가져옴.
				omsPayment = (OmsPayment) orderService.selectOne("oms.payment.selectOne", omsPayment);
				kakao.setAmt(omsPayment.getPaymentAmt().toString());
			} else if ("insert".equals(kakao.getSelectKey())) {
				// 추가 결제
				String paymentAmt = (String) orderService.selectOne("oms.claim.select.exchangeFee", kakao);
				kakao.setAmt(paymentAmt);
			}
		}
		BigDecimal paymentNo = paymentService.getNewPaymentNo();
		kakao.setMerchantTxnNum(paymentNo.toString()); // 새 결제번호 세팅
		kakao = kakaoService.getTxnId(kakao);
		return kakao;
	}

	/**
	 * 
	 * @Method Name : getInterest
	 * @author : dennis
	 * @date : 2016. 10. 28.
	 * @description : 무이자정보 interface
	 *
	 * @param map
	 */
	@RequestMapping(value = "/interest", method = { RequestMethod.POST, RequestMethod.GET })
	public void getInterest(@RequestBody Map map) {
		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd();
		String orderTypeCd = (String) map.get("orderTypeCd");
		try {
			pgService.getInterest(orderTypeCd, deviceTypeCd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Method Name : getInterestList
	 * @author : dennis
	 * @date : 2016. 10. 28.
	 * @description : 카드별 무이자정보조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/interestList", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "text/plain;charset=UTF-8")
	public String getInterestList(@RequestBody Map map) throws Exception {
		String cardCode = (String)map.get("cardCode");
		String paymentAmt = (String) map.get("paymentAmt");
		return pgService.getInterestList(cardCode,paymentAmt);
	}

}
