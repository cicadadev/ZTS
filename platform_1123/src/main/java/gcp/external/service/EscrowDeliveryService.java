package gcp.external.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import gcp.ccs.service.BaseService;
import gcp.external.model.EscrowDeliveryInfo;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;

@Service
public class EscrowDeliveryService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(EscrowDeliveryService.class);

	 @Autowired
	 private PgService pgService;
	
	/**
	 * 
	 * @Method Name : callSweettrackerApi
	 * @author : brad
	 * @date : 2016. 10. 10.
	 * @description : 스윗트래커 운송장 추적 API CALL
	 *
	 * @param omsLogistics
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> sendEscrowDeliveryInfo() {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		
		String storeId = Config.getString("store.id");
		
		List<OmsOrder> list = (List<OmsOrder>) dao.selectList("external.escrow.deliveryInfo.getShippingEscrowOrderList", storeId);
		
		int totalCnt = list.size();
		int successCnt = 0;
		int failCnt = 0;
		
		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();
		
		for(OmsOrder omsOrder : list){
			
			EscrowDeliveryInfo escrowDeliveryInfo = this.getEscrowDeliveryInfo(omsOrder);
			
			Map<String, String> subResult = new HashMap<>();
			
			try {
				
				subResult = this.sendEscrowOrderRcvInfo(escrowDeliveryInfo);
				
			} catch (Exception e) {
				e.printStackTrace();
				subResult.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				subResult.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			}
			
			if (BaseConstants.RESULT_FLAG_FAIL.equals(subResult.get(BaseConstants.RESULT_FLAG))) {
				errorMsg.append("\n======================================");
				errorMsg.append("\n에스크로 결제 주문 발송정보 등록 실패!!");
				errorMsg.append("\n결제번호 : " + escrowDeliveryInfo.getOid());
				errorMsg.append("\n운송장번호 : " + escrowDeliveryInfo.getDlvno());
				errorMsg.append("\n오류 : " + subResult.get(BaseConstants.RESULT_MESSAGE));
				errorMsg.append("\n======================================\n");
				resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				
				failCnt++;
				this.updateEscrowIfResult(resultMap, omsOrder, "N", subResult.get(BaseConstants.RESULT_MESSAGE));
			}else{
				logger.debug("\n======================================");
				logger.debug("\n에스크로 결제 주문 발송정보 등록 성공!!");
				logger.debug("\n결제번호 : " + escrowDeliveryInfo.getOid());
				logger.debug("\n운송장번호 : " + escrowDeliveryInfo.getDlvno());
				logger.debug("\n결과 : OK!!!");
				logger.debug("\n======================================\n");
				
				successCnt++;
				this.updateEscrowIfResult(resultMap, omsOrder, "Y", "OK");
			}
		}
		
		resultMsg.append("\n총건수 	: " + totalCnt);
		resultMsg.append("\n성공 	: " + successCnt);
		resultMsg.append("\n실패 	: " + failCnt);
		
		if (BaseConstants.RESULT_FLAG_FAIL.equals(resultMap.get(BaseConstants.RESULT_FLAG))) {
			resultMsg.append(errorMsg);
		}
		
		resultMap.put(BaseConstants.RESULT_MESSAGE, resultMsg.toString());
		return resultMap;
	}
	
	/**
	 * 
	 * @Method Name : getEscrowDeliveryInfoList
	 * @author : brad
	 * @date : 2016. 11. 3.
	 * @description : 
	 *
	 * @param omsOrder
	 * @return
	 */
	private EscrowDeliveryInfo getEscrowDeliveryInfo(OmsOrder omsOrder) {
		
		String orderId = String.valueOf(omsOrder.getPaymentNo());
		String pgShopId = omsOrder.getPgShopId();
		String orderDt = this.replaceDateFormat(omsOrder.getOrderDt());
		String mertkey = pgService.getMertkey(pgShopId.substring(1)); //TODO : 실환경에서는 SUBSTRING 제거
		
		OmsOrderproduct omsOrderproduct = omsOrder.getOmsOrderproducts().get(0); // 출고일이 제일 늦은 상품데이터 
		OmsLogistics omsLogistics = omsOrderproduct.getOmsLogisticss().get(0);   // 출고확정일이 제일 늦은 물류데이터
		String dlvDate = this.replaceDateFormat(omsLogistics.getCompleteDt());
		
		// 에스크로 발송정보 VO
		EscrowDeliveryInfo escrowDeliveryInfo = new EscrowDeliveryInfo();
		escrowDeliveryInfo.setMid(pgShopId);
		escrowDeliveryInfo.setOid(orderId);
		escrowDeliveryInfo.setOrderdate(orderDt);
		escrowDeliveryInfo.setMertkey(mertkey);
		
		String deliveryServiceCd = omsLogistics.getDeliveryServiceCd();
		
		if("DELIVERY_SERVICE_CD.ETC".equals(deliveryServiceCd) || "DELIVERY_SERVICE_CD.12".equals(deliveryServiceCd) || "DELIVERY_SERVICE_CD.13".equals(deliveryServiceCd)){
			escrowDeliveryInfo.setDlvtype("01");
			escrowDeliveryInfo.setRcvdate("");
			escrowDeliveryInfo.setRcvname("");
			escrowDeliveryInfo.setRcvrelation("");
			
			logger.debug("미제휴택배사 배송 : 수신정보 등록");
		}else{
//				String dlvcompcode = CodeUtil.getCodeNote(deliveryServiceCd);
			String dlvcompcode = "HJ";
			String dlvno = omsLogistics.getInvoiceNo();
			
			escrowDeliveryInfo.setDlvtype("03");
			escrowDeliveryInfo.setDlvdate(dlvDate);
			escrowDeliveryInfo.setDlvcompcode(dlvcompcode);
			escrowDeliveryInfo.setDlvno(dlvno);
			
			logger.debug("제휴택배사 배송 : 발신정보 등록");
		}
		
		return escrowDeliveryInfo;
	}
	
	/**
	 * 
	 * @Method Name : sendEscrowOrderRcvInfo
	 * @author : brad
	 * @date : 2016. 11. 3.
	 * @description : 에스크로 발송정보 전송
	 *
	 * @param escrowDeliveryInfo
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> sendEscrowOrderRcvInfo(EscrowDeliveryInfo escrowDeliveryInfo) throws Exception {
		
		String service_url = "";

		// 테스트용
		service_url = "http://pgweb.uplus.co.kr:7085/pg/wmp/mertadmin/jsp/escrow/rcvdlvinfo.jsp"; 

		// 서비스용
		//service_url = "https://pgweb.uplus.co.kr/pg/wmp/mertadmin/jsp/escrow/rcvdlvinfo.jsp"; 
		
		String mid ="";
		String oid ="";
		String productid ="";
		String orderdate ="";
		String dlvtype ="";
		String rcvdate ="";
		String rcvname ="";
		String rcvrelation ="";
		String dlvdate ="";
		String dlvcompcode ="";
//		String dlvcomp ="";
		String dlvno ="";
		String dlvworker ="";
		String dlvworkertel  ="";
		String hashdata ="";
		String mertkey ="";
		
		mid = escrowDeliveryInfo.getMid();				        // 상점ID
		oid = escrowDeliveryInfo.getOid();	         			// 주문번호
		productid = escrowDeliveryInfo.getProductid();			// 상품ID
		orderdate = escrowDeliveryInfo.getOrderdate();		    // 주문일자
		dlvtype = escrowDeliveryInfo.getDlvtype();				// 등록내용구분
		rcvdate = escrowDeliveryInfo.getRcvdate();				// 실수령일자
		rcvname = escrowDeliveryInfo.getRcvname();				// 실수령인명
		rcvrelation = escrowDeliveryInfo.getRcvrelation();		// 관계
		dlvdate = escrowDeliveryInfo.getDlvdate();				// 발송일자
		dlvcompcode = escrowDeliveryInfo.getDlvcompcode();		// 배송회사코드
//		dlvcomp = escrowDeliveryInfo.getDlvcomp();				// 배송회사명
		dlvno = escrowDeliveryInfo.getDlvno();					// 운송장번호
		dlvworker = escrowDeliveryInfo.getDlvworker();			// 배송자명
		dlvworkertel = escrowDeliveryInfo.getDlvworkertel();	// 배송자전화번호

//	    boolean resp = false;
	    mertkey = escrowDeliveryInfo.getMertkey();              //LG유플러스에서 발급한 상점키로 변경해 주시기 바랍니다.
			
		
		//******************************//
		// 보안용 인증키 생성 - 시작
		//******************************//
	    StringBuffer sb = new StringBuffer();
		if("03".equals(dlvtype))
		{
			// 발송정보
			sb.append(mid);
			sb.append(oid);
			sb.append(dlvdate);
			sb.append(dlvcompcode);
			sb.append(dlvno);
			sb.append(mertkey);
			
		}
		else if("01".equals(dlvtype))
		{
			// 수령정보 
			sb.append(mid);
			sb.append(oid);
			sb.append(dlvtype);
			sb.append(rcvdate);		
			sb.append(mertkey);
		}	
	    
	    byte[] bNoti = sb.toString().getBytes();

	    MessageDigest md = null;
		
	    try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    byte[] digest = md.digest(bNoti);

	    StringBuffer strBuf = new StringBuffer();

	    for (int i=0 ; i < digest.length ; i++) 
		{
	        int c = digest[i] & 0xff;
	        if (c <= 15)
			{
	            strBuf.append("0");
	        }
	        strBuf.append(Integer.toHexString(c));
	    }

	    hashdata = strBuf.toString();
		//******************************//
		// 보안용 인증키 생성 - 끝
		//******************************//

		//******************************//
		// 전송할 파라미터 문자열 생성 - 시작
		//******************************//
		String sendMsg = "";
		StringBuffer msgBuf = new StringBuffer();
		msgBuf.append("mid=" + mid + "&" );	
		msgBuf.append("oid=" + oid+"&");
		msgBuf.append("productid=" + productid + "&" );
		msgBuf.append("orderdate=" + orderdate + "&" );
		msgBuf.append("dlvtype=" + dlvtype + "&" );			
		msgBuf.append("rcvdate=" + rcvdate + "&" );
		msgBuf.append("rcvname=" + rcvname + "&" );
		msgBuf.append("rcvrelation=" + rcvrelation + "&" );			
		msgBuf.append("dlvdate=" + dlvdate + "&" );
		msgBuf.append("dlvcompcode=" + dlvcompcode + "&" );	
		msgBuf.append("dlvno=" + dlvno + "&" );
		msgBuf.append("dlvworker=" + dlvworker + "&" );
		msgBuf.append("dlvworkertel=" + dlvworkertel + "&" );
		msgBuf.append("hashdata=" + hashdata );

		sendMsg = msgBuf.toString();

		StringBuffer errmsg = new StringBuffer();
		//******************************//
		// 전송할 파라미터 문자열 생성 - 끝
		//******************************//
		
		//*************************************//
		// HTTP로 배송결과 등록
		//*************************************//
		URL url = new URL(service_url);
		return sendRCVInfo(sendMsg, url, errmsg);
		
	}
	
	//*************************************************
	// 아래부분 절대 수정하지 말것
	//*************************************************
	/**
	 * 
	 * @Method Name : sendRCVInfo
	 * @author : brad
	 * @date : 2016. 11. 1.
	 * @description : 
	 *
	 * @param sendMsg
	 * @param url
	 * @param errmsg
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> sendRCVInfo(String sendMsg, URL url, StringBuffer errmsg) throws Exception{
		Map<String, String> resultMap = new HashMap<String, String>();
		
        OutputStreamWriter wr = null;
        BufferedReader br = null;
        HttpURLConnection conn = null;
        boolean result = false;
		String errormsg = null;

        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(sendMsg);
            wr.flush();
            
            for (int i=0; ; i++) {
                String headerName = conn.getHeaderFieldKey(i);
                String headerValue = conn.getHeaderField(i);

                if (headerName == null && headerValue == null) {
                    break;
                }
                if (headerName == null) {
                    headerName = "Version";
                }

                errmsg.append(headerName + ":" + headerValue + "\n");
            }


            br = new BufferedReader(new InputStreamReader(conn.getInputStream (), "EUC-KR"));

            String in;
            StringBuffer sb = new StringBuffer();
            while(((in = br.readLine ()) != null )){
                sb.append(in);
            }

            errmsg.append(sb.toString().trim());
            if ( sb.toString().trim().equals("OK")){
                result = true;
            }else{
				errormsg = sb.toString().trim();
			}

        } catch ( Exception ex ) {
            errmsg.append("EXCEPTION : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if ( wr != null) wr.close();
                if ( br != null) br.close();
            } catch(Exception e){
            }
        }
        
        if(result){
        	resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
        }else{
        	resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
        	resultMap.put(BaseConstants.RESULT_MESSAGE, errormsg);
        }
        
        return resultMap;

    }
	
	/**
	 * 
	 * @Method Name : updateEscrowIfResult
	 * @author : brad
	 * @date : 2016. 11. 3.
	 * @description : 에스크로 연동 결과 업데이트
	 *
	 * @param resultMap
	 * @param omsOrder
	 * @param escrowIfYn
	 * @param escrowIfResult
	 */
	private void updateEscrowIfResult(Map<String, String> resultMap, OmsOrder omsOrder, String escrowIfYn, String escrowIfResult){
		
		OmsPayment omsPayment = new OmsPayment();
		omsPayment.setUpdId(BaseConstants.DEFAULT_BATCH_USER_ID);
		omsPayment.setOrderId(omsOrder.getOrderId());
		omsPayment.setPaymentNo(omsOrder.getPaymentNo());
		omsPayment.setEscrowIfYn(escrowIfYn);
		omsPayment.setEscrowIfResult(escrowIfResult);
		
		try {
			dao.updateOneTable(omsPayment);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			resultMap.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Method Name : replaceDateFormat
	 * @author : brad
	 * @date : 2016. 11. 3.
	 * @description : 날짜 포맷 변경
	 *
	 * @param dt
	 * @return
	 */
	private String replaceDateFormat(String dt){
		
		SimpleDateFormat sd = new SimpleDateFormat(DateUtil.FORMAT_1);
		SimpleDateFormat sd1 = new SimpleDateFormat(DateUtil.FORMAT_13);
		
		Date date = null;
		String retStr = "";
		
		try {
			date = sd.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(CommonUtil.isNotEmpty(date)){
			retStr = sd1.format(date);
		}
		return retStr;
	}
}
