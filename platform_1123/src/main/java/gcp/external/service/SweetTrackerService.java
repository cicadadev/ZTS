package gcp.external.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import gcp.ccs.service.BaseService;
import gcp.external.model.DeliveryTracking;
import gcp.external.model.SweetTracker;
import gcp.external.model.SweetTrackerResult;
import gcp.oms.model.OmsDeliverytracking;
import gcp.oms.model.OmsLogistics;
import gcp.oms.service.LogisticsService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import net.sf.json.JSONObject;

@Service
public class SweetTrackerService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(SweetTrackerService.class);

	 @Autowired
	 private LogisticsService logisticsService;
	
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
	public Map<String, String> callSweetTrackerApi() {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		
//		String	callback_url = Config.getString("sweettracker.callback.url");
		String	callback_url = "http://49.50.33.214/openapi/delivery/tracking";
		String	tier = Config.getString("sweettracker.api.tier");
		String	key = Config.getString("sweettracker.api.key");
		
		String storeId = Config.getString("store.id");
		
		List<OmsLogistics> list = (List<OmsLogistics>) dao.selectListWithHandler("external.sweettracker.getShippingLogistics", storeId);
		
		int totalCnt = list.size();
		int successCnt = 0;
		int failCnt = 0;
		
		StringBuffer resultMsg = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();
		
		Set<String> prevDelNo = new HashSet<>();
		
		for(OmsLogistics omsLogistics : list){
			
			String fid = String.valueOf(omsLogistics.getLogisticsInoutNo());
			String invoiceNo = omsLogistics.getInvoiceNo();
//			String fid = "8823759";
//			String invoiceNo = "409387155925";
			String deliveryServiceCd = omsLogistics.getDeliveryServiceCd();
//			String code = deliveryServiceCd.substring(deliveryServiceCd.indexOf(".") + 1);
			String code = deliveryServiceCd.substring(0);
//			String code = "05";
			
			// 이미 전송한 운송장 번호는 보내지 않는다.
			if(prevDelNo.contains(fid) || "DELIVERY_SERVICE_CD.ETC".equals(deliveryServiceCd)){
				continue;
			}else{
				prevDelNo.add(fid);
			}
			
			SweetTracker sweetTracker = new SweetTracker();
			sweetTracker.setNum(invoiceNo);
			sweetTracker.setCode(code);
			sweetTracker.setFid(fid);
			sweetTracker.setCallback_url(callback_url);
			sweetTracker.setTier(tier);
			sweetTracker.setKey(key);
			
			SweetTrackerResult result = this.sendDeliveryInfo(sweetTracker);
				
			if(!result.isSuccess()){
				String errorCdDesc = "";
				switch (result.getE_code()) {
					case "01":
						errorCdDesc = "필수 파라메터가 빈 값이거나 없음";
						break;
					case "02":
						errorCdDesc = "운송장번호 유효성 검사 실패";
						break;
					case "03":
						errorCdDesc = "이미 등록된 운송장번호";
						break;
					case "04":
						errorCdDesc = "지원하지 않는 택배사 코드";
						break;
					case "99":
						errorCdDesc = "시스템 오류";
						break;	
					default:
						errorCdDesc = result.getE_message();
						break;
				}
				errorMsg.append("\n=========================================");
				errorMsg.append("\n전송결과 : " + result.isSuccess());
				errorMsg.append("\n주문상품입출고일련번호 : " + fid);
				errorMsg.append("\n택배사코드 : " + code);
				errorMsg.append("\n운송장번호 : " + result.getNum());
				errorMsg.append("\n에러코드 : " + result.getE_code());
				errorMsg.append("\n에러DESCRIPTION : " + errorCdDesc);
				errorMsg.append("\n=========================================");
				resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
				failCnt++;
				// 전송 실패시 TRACKING_IF_YN = 'N'로 변경
				this.updateTrackingIfResult(resultMap, fid, "N", errorCdDesc);
				
			}else{
				successCnt++;
				// 전송 성공시 TRACKING_IF_YN = 'Y'로 변경
				this.updateTrackingIfResult(resultMap, fid, "Y", "");
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
	 * @Method Name : updateTrackingIfResult
	 * @author : brad
	 * @date : 2016. 11. 2.
	 * @description : 연동 결과 업데이트
	 *
	 * @param resultMap
	 * @param fid
	 * @param trackingIfYn
	 * @param errorCdDesc
	 */
	private void updateTrackingIfResult(Map<String, String> resultMap, String fid, String trackingIfYn, String errorCdDesc){
		
		OmsLogistics omsLogistics = new OmsLogistics();
		omsLogistics.setUpdId(BaseConstants.DEFAULT_BATCH_USER_ID);
		omsLogistics.setLogisticsInoutNo(new BigDecimal(fid));
		omsLogistics.setTrackingIfYn(trackingIfYn);
		omsLogistics.setTrackingIfResult(errorCdDesc);
		
		try {
			dao.update("external.sweettracker.updateTrackingIfYn", omsLogistics);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			resultMap.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Method Name : sendDeliveryInfo
	 * @author : brad
	 * @date : 2016. 10. 8.
	 * @description : 스윗트래커 외부 API 연동
	 *
	 * @param commission
	 * @throws Exception
	 */
	public SweetTrackerResult sendDeliveryInfo(SweetTracker sweetTracker) {
		String apiURL = Config.getString("sweettracker.api.url");
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		SweetTrackerResult result = null;
		int resCode = 0;
		
		try{
			Gson gson = new Gson();
			String json = gson.toJson(sweetTracker);
			
			URL url = new URL(apiURL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestMethod("POST");
	
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
	
			osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(json);
			osw.flush();
	
			resCode = conn.getResponseCode();
			logger.debug("\n====================================");
			logger.debug("\nSending 'POST' request to URL : " + url.toURI());
			logger.debug("\nPost Parameters : " + json);
			logger.debug("\nResponse Code : " + resCode);
			logger.debug("\n====================================");
	         
			StringBuffer res = new StringBuffer();
			
			try {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					res.append(line);
				}
				br.close();
				conn.disconnect();
			} catch (IOException e) {
				try {
					br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
					String line;
					while ((line = br.readLine()) != null) {
						res.append(line);
					}
				} finally {
					br.close();
					conn.disconnect();
				}
			}
			
			logger.debug("Response :" + res.toString());
			if(resCode == HttpURLConnection.HTTP_OK){
				return gson.fromJson(res.toString(), SweetTrackerResult.class);
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if ( osw != null) osw.close();
                if ( br != null) br.close();
            } catch(Exception e){
            }
        }
		
		result = new SweetTrackerResult();
		result.setSuccess(false);
		result.setE_message("HTTP STATUS CODE : " + resCode);
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name : insertDeliveryTracking
	 * @author : brad
	 * @date : 2016. 10. 10.
	 * @description : 배송 추적 데이터 입력
	 *
	 * @param deliveryTracking
	 * @throws Exception
	 */
	public JSONObject insertDeliveryTracking(DeliveryTracking deliveryTracking) throws Exception {
		
		boolean isValid = true;
		boolean isDeliveryComplete = false;
		String message = "success";
		
		JSONObject Result = new JSONObject();
		
//		DateFormat df = new SimpleDateFormat(DateUtil.FORMAT_1);
		
		BigDecimal logisticsInoutNo = new BigDecimal(deliveryTracking.getFid());
		String level = deliveryTracking.getLevel();
		String invoiceNo = CommonUtil.replaceNull(deliveryTracking.getInvoice_no());
		String deliveryServiceTime = CommonUtil.replaceNull(deliveryTracking.getTime_trans());
		String trackerRegTime = CommonUtil.replaceNull(deliveryTracking.getTime_sweet());
		String deliveryLocation = CommonUtil.replaceNull(deliveryTracking.getWhere());
		String officePhoneNo = CommonUtil.replaceNull(deliveryTracking.getTelno_office());
		String deliverymanMobileNo = CommonUtil.replaceNull(deliveryTracking.getTelno_man());
		String deliveryDetail = CommonUtil.replaceNull(deliveryTracking.getDetails());
		String receiverAddress = CommonUtil.replaceNull(deliveryTracking.getRecv_addr());
		String receiverName = CommonUtil.replaceNull(deliveryTracking.getRecv_name());
		String senderName = CommonUtil.replaceNull(deliveryTracking.getSend_name());
		
		if(CommonUtil.isEmpty(logisticsInoutNo) || CommonUtil.isEmpty(invoiceNo) || CommonUtil.isEmpty(level)){
			isValid = false;
			message = "fail - empty required value";
			
			Result.put("code", isValid);
			Result.put("message", message);
			return Result;
		}
		
		OmsDeliverytracking omsDeliveryTracking = new OmsDeliverytracking();
		omsDeliveryTracking.setLogisticsInoutNo(logisticsInoutNo);
		omsDeliveryTracking.setInvoiceNo(invoiceNo);
		omsDeliveryTracking.setDeliveryServiceTime(deliveryServiceTime);
		omsDeliveryTracking.setTrackerRegTime(trackerRegTime);
		omsDeliveryTracking.setDeliveryLocation(deliveryLocation);
		omsDeliveryTracking.setOfficePhoneNo(officePhoneNo);
		omsDeliveryTracking.setDeliverymanMobileNo(deliverymanMobileNo);
		omsDeliveryTracking.setDeliveryDetail(deliveryDetail);
		omsDeliveryTracking.setReceiverAddress(receiverAddress);
		omsDeliveryTracking.setReceiverName(receiverName);
		omsDeliveryTracking.setSenderName(senderName);
		omsDeliveryTracking.setInsId(BaseConstants.DEFAULT_ADMIN_USER_ID);
		omsDeliveryTracking.setUpdId(BaseConstants.DEFAULT_ADMIN_USER_ID);
		
		switch (level) {
			case "1":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.1");
				break;
			case "2":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.2");
				break;
			case "3":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.3");
				break;
			case "4":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.4");
				break;
			case "5":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.5");
				break;
			case "6":
				omsDeliveryTracking.setDeliveryStepCd("DELIVERY_STEP_CD.6");
				isDeliveryComplete = true;
				break;	
			default:
				isValid = false;
				message = "fail - invalid level";
				break;
		}
			
		if(isValid && dao.insert("external.sweettracker.insertDeliveryTracking", omsDeliveryTracking) < 1){
			isValid = false;
			message = "fail - invalid fid or duplicate key";
		}
		
		if(isValid && isDeliveryComplete){
			// 배송완료시 주문상품과 주문테이블의 상태 업데이트
			logisticsService.updateDeliveryComplete(omsDeliveryTracking);
		}

		Result.put("code", isValid);
		Result.put("message", message);
		return Result;
	}
	
}
