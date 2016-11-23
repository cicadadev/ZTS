package gcp.external.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;
import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsPaymentsettle;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import rpt.CnsReportClientVer1;

@Service
public class KakaoSettleService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	public void insertKakaopaySettlement(String storeId, String userId) throws Exception {
		//날짜설정(전일)
		String toDay = DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		String reqDate = DateUtil.getAddDate(DateUtil.FORMAT_3, toDay, new BigDecimal(-1));

		//거래대사
//		String reqDate = "20150323";   // 요청일
//		String reportType = "T";       // 대사파일 구분

		//정산대사
//		String reqDate = "20150323";   // 요청일
		String reportType = "S";          // 대사파일 구분

		//매입청구
//      String reqDate     = "20150324";   // 요청일
//      String reportType  = "AS";         // 대사파일 구분

		//매입청구결과
//      String reqDate     = "20150324";   // 요청일
//      String reportType  = "AR";         // 대사파일 구분

		insertKakaopaySettlement(storeId, userId, reqDate, reportType);
	}

	private void insertKakaopaySettlement(String storeId, String userId, String reqDate, String reportType) throws Exception {
		CnsReportClientVer1 client = new CnsReportClientVer1();

		//Home 경로(미설정시 WAS user home): property 파일 위치
		System.setProperty("user.home", Config.getString("kakao.config.path"));

		//property 파일명
		String confFileName = "cnspay_settle.property";

		//API 호출
		logger.debug("file: " + confFileName + ", date: " + reqDate + ", type: " + reportType);
		String resCode = client.fnReportReq(confFileName, reqDate, reportType);
		logger.debug("Kakao Settle result: " + resCode);

		//대사data 처리
		if ("0000".equals(resCode)) { //success
			//대사파일명
			String fileFullName = Config.getString("kakao.data.path") + File.separator + Config.getString("kakao.mid")
					+ reportType + reqDate + ".csv";
			logger.info("Data File: " + fileFullName);

			int errorCnt = insertSettlementData(fileFullName);

			logger.info("Kakaopay Settlement Insert Error: " + errorCnt);
		} else { //error
			logger.error("Kakaopay File Receive Error: [" + resCode + "]");
			//TO-DO 오류 처리
			throw new Exception("Kakaopay File Receive Error");
		}
	}

	private int insertSettlementData(String fileName) throws Exception {
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName));
			// UTF-8
//			CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"), ",", '"', 1);
			List<String[]> contents = reader.readAll();
			Iterator<String[]> it = contents.iterator();
			while (it.hasNext()) {
				String[] strArr = (String[]) it.next();
				if ("D".equals(strArr[0])) {
					processData(strArr);
					continue;
				} else if ("H".equals(strArr[0])) {
					if (null != strArr[6] && !"".equals(strArr[6])) {
						logger.debug("Kakaopay File Error: " + getErrorMessage(strArr[6]));
						break;
					}
				} else if ("T".equals(strArr[0])) {
					processTrailer(strArr);
					break;
				} else {
					continue;
				}
			}

			reader.close();
		} catch (FileNotFoundException fe) {
			logger.error("insertSettlementData: Kakaopay Settlement File does not Exist");
			throw new Exception(fe.getMessage());
		} catch (IOException ie) {
			logger.error("insertSettlementData: Unknown Exception");
			throw new Exception(ie.getMessage());
		}

		return 0;
	}

	private void processData(String[] data) throws Exception {
		logger.debug("MID: " + data[1]);
		logger.debug("거래구분: " + data[2]);
		logger.debug("승인일: " + data[3]);
		logger.debug("결제서비스: " + data[7]);
		logger.debug("TID: " + data[8]);
		logger.debug("주문번호: " + data[9]);
		logger.debug("거래금액: " + data[11]);
		logger.debug("수수료(VAT포함): " + data[13]);
		logger.debug("지급일: " + data[14]);
		//max seq 조회
//		int seqNum = (int) dao.selectOne("external.pgreceive.getMaxSeq", null);

		OmsPaymentsettle pgSettle = new OmsPaymentsettle();
		//일련번호
//		pgSettle.setSeq(seqNum);
		//CNSPay 거래 ID: TID
//		pgSettle.setTId(data[8]);
		//회사유형
//		pgSettle.setInGubun("Kakaopay");
		//등록일자
//		pgSettle.setInsertDate(new Date());

		//상점ID: MID
		pgSettle.setPgShopId(data[1]);
		//주문ID
		pgSettle.setOrderId(data[9]);
		//지급일자
		pgSettle.setSettleDt(data[14]);
		//승인일자
		pgSettle.setPaymentDt(data[3]);
		//취소일자
		pgSettle.setCancelDt(data[5]);
		//결제수단코드
		pgSettle.setPaymentMethodCd("PAYMENT_METHOD_CD.KAKAO");
		//거래유형코드: 카카오페이는 신용카드만 존재
		String transType = "CA";
		if ("C".equals(data[2])) { //취소
			transType += "02";
		} else if ("P".equals(data[2])) { //부분취소
			transType += "11";
		} else { //그 외는 승인으로 처리
			transType += "01";
		}
		pgSettle.setTransTypeCd(transType);
		//거래금액
		pgSettle.setPaymentAmt(new BigDecimal(data[11]));
		//수수료(VAT포함)
		pgSettle.setPaymentFee(new BigDecimal(data[13]));

		dao.insertOneTable(pgSettle);
	}

	private void processTrailer(String[] trailer) {
		logger.debug("Approval Total Count: " + trailer[5]);
		logger.debug("Approval Total Amount: " + trailer[6]);
		logger.debug("Cancel Total Count: " + trailer[7]);
		logger.debug("Cancel Total Count: " + trailer[8]);
	}

	private String getErrorMessage(String errorCode) {
		String errorMsg = "";

		switch (errorCode) {
		case "8001":
			errorMsg = "유효하지 않은 세션키입니다.";
		break;
		case "8002":
			errorMsg = "유효하지 않은 아이디 구분입니다.";
		break;
		case "8003":
			errorMsg = "유효하지 않은 테스트 구분입니다.";
		break;
		case "8004":
			errorMsg = "유효하지 않은 대사파일 구분입니다.";
		break;
		case "8005":
			errorMsg = "유효하지 않은 날짜입니다.";
		break;
		case "8888":
			errorMsg = "기타 오류가 발생했습니다.";
		break;
		case "9001":
			errorMsg = "RDATE 값이 설정되지 않았습니다.";
		break;
		case "9002":
			errorMsg = "SID 값이 설정되지 않았습니다.";
		break;
		case "9003":
			errorMsg = "IDGUBUN 값이 설정되지 않았습니다.";
		break;
		case "9004":
			errorMsg = "RTYPE 값이 설정되지 않았습니다.";
		break;
		case "9005":
			errorMsg = "TDATETIME 값이 설정되지 않았습니다.";
		break;
		case "9006":
			errorMsg = "RHASH 값이 설정되지 않았습니다.";
		break;
		case "9007":
			errorMsg = "APPVERSION 값이 설정되지 않았습니다.";
		break;
		case "9011":
			errorMsg = "SID 와 상점키가 올바르지 않습니다.";
		break;
		case "9012":
			errorMsg = "요청일의 대사파일이 존재하지 않습니다.";
		break;
		case "9999":
			errorMsg = "기타 오류가 발생했습니다.";
		break;
		default:
			errorMsg = "정해지지 않은 오류코드입니다";
		break;
		}

		return errorMsg;
	}
//	private int insertSettlementData(String fileName) throws Exception {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(fileName));
//			String content = "";
//
//			while ((content = br.readLine()) != null) {
//				logger.debug(content);
//				// -1 옵션은 마지막 "," 이후 빈 공백도 읽기 위한 옵션
//				String[] token = content.split(",", -1);
//			}
//			br.close();
//		} catch (FileNotFoundException fe) {
//			logger.error("insertSettlementData: Kakaopay Settlement File does not Exist");
//			throw new Exception(fe.getMessage());
//		} catch (IOException ie) {
//			logger.error("insertSettlementData: Unknown Exception");
//			throw new Exception(ie.getMessage());
//		}
//
//		return 0;
//	}
}
