package gcp.external.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsPaymentsettle;
import intune.gsf.common.utils.Config;

@Service
public class LGUplusSettleService extends BaseService {
	private final Log							logger	= LogFactory.getLog(getClass());

//	private static HttpsURLConnection	connection		= null;
//	private static URL					url				= null;
	private final Map<String, String>	idMap	= new HashMap<String, String>();

//	private static final String[]	com_id_array	= { "0to7_00", "0to7_11", "0to7_33", "0to7_44", "0to7_55", "0to7_77",
//			"0to7_88" };
//	private static final String[][]	asisTobeMidMap	= {
//			// 제로투세븐 :전체
//			{ "maeiluri00", "0to7_00", "e477861040dddb39f48a820563ce24e4", "e477861040dddb39f48a820563ce24e4" }
//			// 구 에스크로:
//			, { "IES0to7com", "0to7_00", "e477861040dddb39f48a820563ce24e4", "e477861040dddb39f48a820563ce24e4" }
//			// 다자녀 경기,경남,충남: 농협BC
//			, { "maeiluri11", "0to7_11", "5a6d9e03752faa51b591bd6a02b63643", "5a6d9e03752faa51b591bd6a02b63643" }
//			// 미클럽: 신한
//			, { "maeiluri22", "", "", "41" }
//			// 직원몰: 전체
//			, { "maeiluri33", "0to7_33", "f6ffffba168b08cc7be6a739e0def069", "f6ffffba168b08cc7be6a739e0def069" }
//			// 다자녀 광주: 광주은행카드
//			, { "maeiluri44", "0to7_44", "5d049ed72d4d528f214ca8ff0bd34689", "5d049ed72d4d528f214ca8ff0bd34689" }
//			// 다자녀 인천,강원: 농협BC, 신한
//			, { "maeiluri55", "0to7_55", "c4e0df1294b51e110b58b7b9d1cb6f6b", "c4e0df1294b51e110b58b7b9d1cb6f6b" }
//			// KB: KB고운맘카드
//			, { "maeiluri66", "", "", "" }
//			// 신한카드포인트몰: 신한
//			, { "maeiluri77", "0to7_77", "f5f4deda7638181b53d3d7a2b17d4260", "f5f4deda7638181b53d3d7a2b17d4260" }
//			// 신한 아이사랑: 신한
//			, { "maeiluri88", "0to7_88", "228f6d825e3b6f6c7cae9b36c7f0e938", "228f6d825e3b6f6c7cae9b36c7f0e938" } };
	//Header: 데이터 구분자(H), 상점ID, 매출일자, 지급예정일자, 거래건수SUM, 거래금액SUM,
	//LG유플러스수수료, VAT, 당기발생액, 지급금액, 미지급액
	private final String[]				adjHeadFieldNm	= { "DATA_TYPE", "MID", "SALES_DATE", "PAYMNT_DATE", "TRX_CNT", "TRX_AMT",
			"LGUP_FEE", "VAT", "TERM_AMT", "PAYMNT_AMT", "UNPAYMNT_AMT" };
	//Body: 데이터 구분자(D), 일련번호, 상점ID, 주문번호, 주문일자, 결제수단, 거래유형,
	//거래금액, 수수료, 수수료무이자, 매출일자, 지급일자
	private final String[]				adjBodyFieldNm	= { "DATA_TYPE", "SEQ_NO", "MID", "OID", "ORD_DT", "PAY_GUBN", "TRX_TYPE",
			"TRX_AMT", "FEE_AMT", "NOINT_FEE_AMT", "SALES_DT", "PAYMNT_DT" };

	/**
	 * @Method Name : insertPgReceiveData
	 * @author : peter
	 * @date : 2016. 7. 22.
	 * @description : LG U+ 로부터 받은 정산자료 입력
	 *
	 * @param storeId
	 * @param userId
	 */
	public void insertLguSettlementData(String storeId, String userId) {
		//상점ID-MertKey Map 저장
		setMidMertkeyPair();

		//job date setting
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date sDate = cal.getTime();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
		String jobDate = simpleDate.format(sDate);

		List<Map<String, String>> adjList = null;
		BufferedReader in = null;
		int seqNum = 0;
		try {
			//ID-인증정보
			for (String mid : idMap.keySet()) {
				String mertKey = idMap.get(mid);
				adjList = new ArrayList<Map<String, String>>();

				//인증키 생성
				StringBuffer sb = new StringBuffer();
				sb.append(mid);
				sb.append(mertKey);
				String trxKey = generateMD5HashData(sb);

				//LG U+ 접속
				String sRtnADJData = callHttpClientReq(mid, jobDate, trxKey);
				logger.info("LG U+ Settle Data: " + sRtnADJData);
				if (sRtnADJData.trim().length() > 0) {
					StringTokenizer stkn = new StringTokenizer(sRtnADJData, "\r\n");
					if (stkn != null) {
						String strTkn = "";
						while (stkn.hasMoreTokens()) {
							strTkn = stkn.nextToken();
							adjList.add(singleLineData(strTkn, "|"));
						}
					}
				}

				if (adjList != null && adjList.size() > 0) {
					//max seq 조회
					seqNum = (int) dao.selectOne("external.pgreceive.getMaxSeq", null);

					Iterator<Map<String, String>> it = adjList.iterator();
					while (it.hasNext()) {
						Map<String, String> hmp = (HashMap<String, String>) it.next();
						if (((String) hmp.get("DATA_TYPE")) != null && "D".equals((String) hmp.get("DATA_TYPE"))) {
							OmsPaymentsettle pgSettle = new OmsPaymentsettle();

							//PG상점ID
							pgSettle.setPgShopId((String) hmp.get("MID"));
							//주문ID
							pgSettle.setOrderId((String) hmp.get("OID"));
							//지급일자: PAY_DATE
							pgSettle.setSettleDt((String) hmp.get("PAYMNT_DT"));
							//결제일자(주문일자): APPROVAL_DATE
							pgSettle.setPaymentDt((String) hmp.get("ORD_DT"));
							//취소일자
							pgSettle.setCancelDt(""); //CANCEL_DATE
							//결제수단코드: PAY_WAY
							pgSettle.setPaymentMethodCd(getPaymentMethodCode((String) hmp.get("PAY_GUBN")));
							//거래유형코드: TRANS_TYPE
							pgSettle.setTransTypeCd((String) hmp.get("TRX_TYPE"));
							//결제금액: TRANS_AMT
							pgSettle.setPaymentAmt(new BigDecimal((String) hmp.get("TRX_AMT")));
							//결제수수료(VAT포함): TRANS_CHARGE
							pgSettle.setPaymentFee(new BigDecimal((String) hmp.get("FEE_AMT")));

//							pgSettle.setSeq(++seqNum); //SEQ
//							pgSettle.setTId(""); //TID
//							pgSettle.setInsertDate(new Date());
//							pgSettle.setInGubun("LGUPLUS");
//							pgSettle.setCalcDate((String) hmp.get("SALES_DT")); //CALC_DATE
//							pgSettle.setTransChargeFree(Integer.parseInt((String) hmp.get("NOINT_FEE_AMT"))); //TRANS_CHARGE_FREE

							dao.insertOneTable(pgSettle);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * @Method Name : generateMD5HashData
	 * @author : peter
	 * @date : 2016. 7. 22.
	 * @description : 상점아이디+MertKey 조합을 MD5로 암호화
	 *
	 * @param sb
	 * @return
	 */
	private String generateMD5HashData(StringBuffer sb) {
		StringBuffer strBuf = new StringBuffer();
		try {
			byte[] bNoti = sb.toString().getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(bNoti);

			for (int i = 0; i < digest.length; i++) {
				int c = digest[i] & 0xff;
				if (c <= 15) {
					strBuf.append("0");
				}
				strBuf.append(Integer.toHexString(c));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strBuf.toString();
	}

	/**
	 * @Method Name : callHttpClientReq
	 * @author : peter
	 * @date : 2016. 7. 22.
	 * @description : LG U+ 의 Http Client 접속
	 *
	 * @param com_id
	 * @param jobDate
	 * @param trx_key
	 * @return
	 */
	private String callHttpClientReq(String com_id, String jobDate, String trx_key) {
		//LG U+ Api 주소(test)
		String sUrl = "http://pgweb.uplus.co.kr/pg/wmp/outerpage/trxdown.jsp?mertid=dacomtest&servicecode=ADJ&trxdate=" + jobDate
				+ "&key=" + trx_key;
		//LG U+ Api 주소(old)
//		String sUrl = "http://pgweb.dacom.net/pg/wmp/outerpage/trxdown.jsp?mertid=" + com_id + "&servicecode=ADJ&trxdate="
//				+ jobDate + "&key=" + trx_key;
		//LG U+ Api 주소(new)
//		String sUrl = "http://pgweb.uplus.co.kr/pg/wmp/outerpage/trxdown.jsp?mertid=" + com_id + "&servicecode=ADJ&trxdate="
//				+ jobDate + "&key=" + trx_key;

		String sRtnStr = "";
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(sUrl);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			}

			byte[] responseBody = method.getResponseBody();
			sRtnStr = new String(responseBody, "euc-kr"); // byte[] -> String
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage());
		} catch (IOException ie) {
			logger.error("Fatal transport error: " + ie.getMessage());
		} finally {
			method.releaseConnection();
		}

		return sRtnStr;
	}

	/**
	 * @Method Name : singleLineData
	 * @author : peter
	 * @date : 2016. 7. 22.
	 * @description : 수신결과 데이터 처리
	 *
	 * @param strLineToken
	 * @param delim
	 * @return
	 */
	private Map<String, String> singleLineData(String strLineToken, String delim) {
		Map<String, String> mp = new HashMap<String, String>();

		if (strLineToken == null || strLineToken.trim().length() <= 0) {
			return mp;
		}

		StringTokenizer stkz = new StringTokenizer(strLineToken, delim);
		int idx = 0;
		String dataType = "";
		while (stkz.hasMoreTokens()) {
			String fieldData = stkz.nextToken();
			if (fieldData == null) {
				continue;
			}

			fieldData = fieldData.trim();
			if (idx == 0) {
				dataType = fieldData;
			}

			if ("H".equals(dataType)) {
				mp.put(adjHeadFieldNm[idx], fieldData);
			}
			if ("D".equals(dataType)) {
				mp.put(adjBodyFieldNm[idx], fieldData);
			}
			idx++;
		}

		return mp;
	}

	private void setMidMertkeyPair() {
		//test
		idMap.put(Config.getString("pg.mid.common"), Config.getString("pg.mertkey.common"));
		//real
//		idMap.put(Config.getString("pg.mid.00"), Config.getString("pg.mertkey.00"));
//		idMap.put(Config.getString("pg.mid.01"), Config.getString("pg.mertkey.01"));
//		idMap.put(Config.getString("pg.mid.10"), Config.getString("pg.mertkey.10"));
//		idMap.put(Config.getString("pg.mid.11"), Config.getString("pg.mertkey.11"));
//		idMap.put(Config.getString("pg.mid.20"), Config.getString("pg.mertkey.20"));
//		idMap.put(Config.getString("pg.mid.21"), Config.getString("pg.mertkey.21"));
//		idMap.put(Config.getString("pg.mid.30"), Config.getString("pg.mertkey.30"));
//		idMap.put(Config.getString("pg.mid.40"), Config.getString("pg.mertkey.40"));
	}

	private String getPaymentMethodCode(String payWay) {
		switch (payWay) {
		case "SC0010": //신용카드
			return "PAYMENT_METHOD_CD.CARD";
		case "SC0030": //계좌이체
			return "PAYMENT_METHOD_CD.TRANSFER";
		case "SC0040": //무통장입금
			return "PAYMENT_METHOD_CD.VIRTUAL";
		case "SC0060": //휴대폰
			return "PAYMENT_METHOD_CD.MOBILE";
		default:
			return "UNKNOWN";
		}
	}
}
