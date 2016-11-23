package gcp.external.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import intune.gsf.common.utils.Config;

/**
 * @Pagckage Name : gcp.external.util
 * @FileName : TerminalUtil.java
 * @author : peter
 * @date : 2016. 10. 25.
 * @description : 사방넷과 통신하는 Util
 */
public class TerminalUtil {
	public static String sendPostSbn(String mode) {
		return sendPostSbn(mode, null, null);
	}

	public static String sendPostSbn(String mode, String sdt, String edt) {
		String targetUrl = Config.getString("sbn.url");

		BufferedReader bufReader = null;
		InputStreamReader inStreamReader;
		String full_buf = null;
		String buf = null;
		String msg = null;
		int statusCode = 0;

		try {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(targetUrl);

			//parameter1
			NameValuePair param1 = new NameValuePair("mode", mode);
			postMethod.addParameter(param1);

			//parameter2
			if (sdt != null && !"".equals(sdt)) {
				NameValuePair param2 = new NameValuePair("sdt", sdt);
				postMethod.addParameter(param2);
			}

			//parameter3
			if (edt != null && !"".equals(edt)) {
				NameValuePair param3 = new NameValuePair("edt", edt);
				postMethod.addParameter(param3);
			}

			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3600000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(3600000);

			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				/*
				 * statusCode 값
				 * 404 : 호출 페이지를 찾을 수 없을 때
				 * 503 : WAS 서버 다운 상태
				 */
				switch (statusCode) {
				case 404:
					msg = "ERROR:404(호출하는 페이지가 없음) 호출 페이지 : [ " + targetUrl + "]";
				break;
				case 503:
					msg = "ERROR:503(WAS 서버 다운) 호출 URL : [ " + targetUrl + "]";
				break;
				}
			} else {
				try {
					inStreamReader = new InputStreamReader(postMethod.getResponseBodyAsStream());
					bufReader = new BufferedReader(inStreamReader);
					while (true) {
						buf = bufReader.readLine();
						if (buf == null) {
							break;
						}
						full_buf = full_buf + buf;
					}

					//읽어들인 Contents에서 null 제거
					full_buf = full_buf.replaceAll("null", "").trim();

					//최종 Contents 값
					msg = full_buf;
				} catch (IOException e) {
					if (statusCode != 200) {
						msg = "ERROR:IOException 발생( httpclient.execute(httpget)호출 오류 ): [내부오류]";
					} else {
						//페이지 호출은 성공했으나 결과 코드를 받지 못한 경우 => WAS는 살아있으므로 성공으로 간
						msg = "ERROR:200";
					}
				}
			}
		} catch (IOException ioe) {
			String exceptionName = ioe.getClass().getName();
			if (exceptionName.equals("java.net.UnknownHostException")) {
				msg = "ERROR:UnknownHostException 발생(호출 서버의 도메인 정보 또는 IP정보 오류) 오류 도메인 : [ " + targetUrl + "]";
			} else if (exceptionName.equals("org.apache.http.conn.HttpHostConnectException")) {
				msg = "ERROR:HttpHostConnectException 발생(웹서버 다운) 호출 URL : [ " + targetUrl + "]";
			} else if (exceptionName.equals("java.net.ClientProtocolException")) {
				msg = "ERROR:ClientProtocolException 발생( httpclient.execute(httpget)호출 오류 ): [내부오류]";
			} else if (exceptionName.equals("java.net.ConnectTimeoutException")) {
				msg = "ERROR:ConnectTimeoutException 발생: [사방넷통신오류]";
			} else if (exceptionName.equals("java.io.IOException")) {
				msg = "ERROR:IOException 발생( httpclient.execute(httpget)호출 오류 ): [내부오류]";
			}
		} catch (IllegalStateException ie) {
			msg = "ERROR:IllegalStateException 발생(URL 포멧 오류) 오류 URL : [ " + targetUrl + "]";
		}

		return msg;
	}

	@SuppressWarnings("restriction")
	public static String makeHttpConnection(String apiUrl, String params) {
//		String httpUrl = apiUrl + "?" + params;
//		System.out.println("httpUrl: " + httpUrl);
//		StringBuilder sbUrl = new StringBuilder();
//		sbUrl.append(apiUrl).append("?");
//		for (String key : paramMap.keySet()) {
//			sbUrl.append(key).append("=").append(paramMap.get(key)).append("&");
//		}

		String result = "";
		StringBuilder response = new StringBuilder();
		try {
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			com.sun.net.ssl.internal.ssl.Provider provider = new com.sun.net.ssl.internal.ssl.Provider();
			Security.addProvider(provider);

			URL obj = new URL(apiUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//con.setRequestProperty("Accept-Charset", "UTF-8");
//			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//			con.setRequestProperty("Content-Length", String.valueOf(jsonParam.length()));
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
//			con.setUseCaches(false);
//			con.setDefaultUseCaches(false);

//			OutputStream os = con.getOutputStream();
//			os.write(params.toString().getBytes("UTF-8"));
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(params);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine = "";
				while ((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
				br.close();

				result = response.toString();
			} else {
//				logger.debug("response: " + responseCode);
//				logger.error("response: " + responseCode);
				result = "ERR";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			result = "EXP";
		} catch (Exception e) {
			e.printStackTrace();
			result = "EXP";
		}

		return result;
	}
}
