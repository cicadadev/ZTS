package intune.gsf.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import intune.gsf.common.constants.BaseConstants;

/**
 * CommonUtil Class
 * 
 * <pre>
 * 개발에 도움이 되는 유틸 메서드를 제공한다.
 * </pre>
 * 
 * @author ditto
 *
 */
@SuppressWarnings("rawtypes")
public class CommonUtil extends StringUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static boolean isEmpty(Object paramObject) {
		if (paramObject == null)
			return true;
		Class localClass = paramObject.getClass();
		if (localClass.isPrimitive())
			return true;
		if (localClass.isArray())
			return (Array.getLength(paramObject) == 0);
		if (Collection.class.isAssignableFrom(localClass))
			return ((Collection) paramObject).isEmpty();
		if (Map.class.isAssignableFrom(localClass))
			return ((Map) paramObject).isEmpty();
		return (paramObject.toString().trim().length() == 0);
	}

	public static boolean isNotEmpty(Object paramObject) {
		return !isEmpty(paramObject);
	}

	/**
	 * 문자열 좌측공백 제거
	 * 
	 * @param str
	 * @return
	 */
	public static String ltrim(String str) {
		int len = str.length();
		int idx = 0;

		while ((idx < len) && (str.charAt(idx) <= ' ')) {
			idx++;
		}

		return str.substring(idx, len);
	}

	/**
	 * 문자열 우측공백제거
	 * 
	 * @param str
	 * @return
	 */
	public static String rtrim(String str) {
		int len = str.length();
		while ((0 < len) && (str.charAt(len - 1) <= ' ')) {
			len--;
		}

		return str.substring(0, len);
	}

	/**
	 * 숫자를 통화형으로 변환
	 * 
	 * @param num
	 * @return
	 */
	public static String formatMoney(int num) {
		double iAmount = (new Double(num)).doubleValue();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");

		return df.format(iAmount);
	}

	/**
	 * 문자를 통화형으로 변환
	 * 
	 * @param str
	 * @return
	 */
	public static String formatMoney(String str) {
		double iAmount = (new Double(str)).doubleValue();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");

		return df.format(iAmount);
	}

	/**
	 * 문자열에서 comma(,) 제거
	 * 
	 * @param str
	 * @return
	 */
	public static String removeComma(String str) {
		String rtnValue = str;
		if (isNull(str)) {
			return "";
		}
		rtnValue = replace(rtnValue, ",", "");

		return rtnValue;
	}

	/**
	 * 문자열 null, "", "  " 여부 체크
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return (str == null || (str.trim().length()) == 0);
	}

	/**
	 * 개체의 null 여부 체크
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		String str = null;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			return true;
		}

		return isNull(str);
	}

	/**
	 * String 의 value가 null 인경우 ""를 반환
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceNull(String value) {
		return replaceNull(value, "");
	}

	/**
	 * 파라미터로 넘어온 값이 null 이거나 공백이 포함된 문자라면 defaultValue를 return 아니면 값을 trim해서 넘겨준다.
	 * 
	 * @param value
	 * @param repStr
	 * @return
	 */

	/**
	 * String 의 value가 null or 빈값 이면 default value를 아니면 trim 처리 해서 반환
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String replaceNull(String value, String defaultValue) {
		if (isNull(value)) {
			return defaultValue;
		}

		return value.trim();
	}

	/**
	 * Object를 받아서 String 형이 아니거나 NULL이면 ""를 return, String 형이면 형 변환해서 넘겨준다.
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceNull(Object value) {
		Object rtnValue = value;
		if (rtnValue == null || !"java.lang.String".equals(rtnValue.getClass().getName())) {
			rtnValue = "";
		}

		return replaceNull((String) rtnValue, "");
	}

	/**
	 * String을 int 형으로 변환
	 * 
	 * @param value
	 * @return
	 */
	public static int parseInt(String value) {
		return parseInt(value, 0);
	}

	/**
	 * Object를 int 형으로 변환(default value : 0)
	 * 
	 * @param value
	 * @return
	 */
	public static int parseInt(Object value) {
		String valueStr = replaceNull(value);
		return parseInt(valueStr, 0);
	}

	/**
	 * Object를 int형으로 변환 <br>
	 * Object가 null이면 defaultValue return
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(Object value, int defaultValue) {
		String valueStr = replaceNull(value);
		return parseInt(valueStr, defaultValue);
	}

	/**
	 * String을 int형으로 변환 <br>
	 * String이 숫자 형식이 아니면 defaultValue return
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(String value, int defaultValue) {
		int returnValue = 0;

		if (isNull(value)) {
			returnValue = defaultValue;
		} else if (!isNumeric(value)) {
			returnValue = defaultValue;
		} else {
			returnValue = Integer.parseInt(value);
		}
		return returnValue;
	}

	/**
	 * String을 long형으로 변환 (default value : 0)
	 * 
	 * @param value
	 * @return
	 */
	public static long parseLong(String value) {
		return parseLong(value, 0);
	}

	/**
	 * String을 long형으로 변환 <br>
	 * 잘못된 데이터면 return은 defaultValue
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(String value, long defaultValue) {
		long returnValue = 0;

		if (isNull(value)) {
			returnValue = defaultValue;
		} else if (!isNumeric(value)) {
			returnValue = defaultValue;
		} else {
			returnValue = Long.parseLong(value);
		}

		return returnValue;
	}

	/**
	 * String 배열을 List로 변환
	 * 
	 * @param values
	 * @return
	 */
	public static List<String> changeList(String[] values) {
		List<String> list = new ArrayList<String>();

		if (values == null) {
			return list;
		} else {
			for (int i = 0, n = values.length; i < n; i++) {
				list.add(values[i]);
			}
		}

		return list;
	}

	/**
	 * 백분율을 반환한다
	 * 
	 * @param value
	 * @param total
	 * @return
	 */
	public static String percentValue(int value, int total) {
		double val = Double.parseDouble(String.valueOf(value)) / Double.parseDouble(String.valueOf(total)) * 100;

		DecimalFormat df = new DecimalFormat("##0.0");

		return df.format(val);
	}

	/**
	 * String 앞 또는 뒤를 특정문자로 지정한 길이만큼 채워주는 함수 <BR>
	 * (예) pad("1234","0", 6, 1) --> "123400" <BR>
	 * 
	 * @param src 대상 문자열
	 * @param pad 채울 문자열
	 * @param totLen 총길이
	 * @param mode 앞/위 구분 (-1:front, 1:back)
	 * @return
	 */
	public static String pad(String src, String pad, int totLen, int mode) {
		String paddedString = "";
		if (src == null) {
			return "";
		}

		int srcLen = src.length();
		if ((totLen < 1) || (srcLen >= totLen)) {
			return src;
		}

		for (int i = 0; i < (totLen - srcLen); i++) {
			paddedString += pad;
		}

		if (mode == -1) {
			paddedString += src; // front padding
		} else {
			paddedString = src + paddedString; //back padding
		}

		return paddedString;
	}

	public static <E> Map htmlEscape(Map<E, String> paramMap) {
		for (int i = 0; i < new ArrayList<E>(paramMap.keySet()).size(); ++i)
			paramMap.put((E) new ArrayList<E>(paramMap.keySet()).get(i),
					htmlEscape((String) paramMap.get(new ArrayList<E>(paramMap.keySet()).get(i))));
		return paramMap;
	}

	public static String htmlEscape(String paramString) {
		if (paramString == null)
			return "";
		return paramString.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
				.replaceAll("'", "&#39;");
	}

	public static String htmlUnescape(String paramString) {
		if (paramString == null)
			return "";
		return paramString.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&#39;", "'")
				.replaceAll("&amp;", "&");
	}

	public static String htmlEscapeWithReturn(String paramString) {
		paramString = htmlEscape(paramString);
		paramString = paramString.replaceAll("\\r\\n", "<br>");
		paramString = paramString.replaceAll("\\r", "<br>");
		paramString = paramString.replaceAll("\\n", "<br>");
		return paramString;
	}

	public static String htmlUnescapeWithReturn(String paramString) {
		if (paramString == null)
			return "";
		paramString = paramString.replaceAll("<br>", "\r\n");
		return htmlUnescape(paramString);
	}

	public static String htmlEscapeAlternative(String paramString) {
		if (paramString == null)
			return "";
		paramString = paramString.replaceAll("&", "＆");
		paramString = paramString.replaceAll("\"", "”");
		paramString = paramString.replaceAll("'", "’");
		paramString = paramString.replaceAll("<", "＜");
		paramString = paramString.replaceAll(">", "＞");
		return paramString;
	}

	public static String getLocalDateTime(String pattern) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(pattern);
		long l = System.currentTimeMillis();
		String str = localSimpleDateFormat.format(new Date(l));
		return str;
	}

	public static String getDatewithSpan(String paramString, long paramLong) {
		int i = Integer.parseInt(paramString.substring(0, 4));
		int j = Integer.parseInt(paramString.substring(4, 6)) - 1;
		int k = Integer.parseInt(paramString.substring(6, 8));
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.set(i, j, k);
		localCalendar.add(5, (int) paramLong);
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return localSimpleDateFormat.format(localCalendar.getTime());
	}

	public static String getDatewithSpan(long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.add(5, (int) paramLong);
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return localSimpleDateFormat.format(localCalendar.getTime());
	}

	public static String makeDateType(String paramString) {
		if ((paramString.length() == 0) || (paramString == null))
			return "";
		if (paramString.length() != 8)
			return "invalid length";
		String str1 = paramString.substring(0, 4);
		String str2 = paramString.substring(4, 6);
		String str3 = paramString.substring(6, 8);
		return str1 + "-" + str2 + "-" + str3;
	}

	public static String makeDateTimeType(String paramString) {
		if (paramString.length() == 0)
			return "";
		if (paramString.length() != 14)
			return "invalid length";
		String str1 = paramString.substring(0, 4);
		String str2 = paramString.substring(4, 6);
		String str3 = paramString.substring(6, 8);
		String str4 = paramString.substring(8, 10);
		String str5 = paramString.substring(10, 12);
		String str6 = paramString.substring(12, 14);
		return str1 + "-" + str2 + "-" + str3 + " " + str4 + ":" + str5 + ":" + str6;
	}

	public static String formatDateStr(String str, String deliminate) {
		StringBuffer strbuff = new StringBuffer("");
		if (str == null || str.length() < 8)
			return null;
		else {
			strbuff.append(str.substring(0, 4)).append(deliminate).append(str.substring(4, 6)).append(deliminate)
					.append(str.substring(6, 8));
			return strbuff.toString();
		}
	}

	/**
	 * 카멜표기법으로 convert
	 * 
	 * @param txt
	 * @param flag true이면 첫번째 문자 대문자
	 * @return
	 */
	public static String convertCamel(String txt, boolean flag) {
		txt = txt.toLowerCase();

		String newTxt = "";
		for (int i = 0; i < txt.length(); i++) {

			char ch = txt.charAt(i);

			if (ch == '_') {
				//pass
				flag = true;
			} else if (flag) {
				String st = ch + "";
				newTxt += st.toUpperCase();
				flag = false;
			} else {
				newTxt += txt.charAt(i);
				flag = false;
			}
		}
		return newTxt;
	}


	public static String convertTableNotation(String txt) {

		String newTxt = "";
		for (int i = 0; i < txt.length(); i++) {

			char ch = txt.charAt(i);
			if (i != 0 && ch >= 65 && ch <= 90) {
				newTxt += "_";
			}

			newTxt += String.valueOf(ch).toUpperCase();
		}
		return newTxt;
	}
	/**
	 * 
	 * @Method Name : convertClassFromMap
	 * @author : eddie
	 * @date : 2016. 4. 21.
	 * @description : Map To Object Convert
	 *
	 * @param result
	 * @param clazz
	 * @return
	 */
	public static Object convertMapToObject(Map<String, Object> result, Class<?> clazz) {

		Map<String, Object> newResult = new HashMap<String, Object>();

		for (String key : result.keySet()) {
			String column = CommonUtil.convertCamel(key, false);

			Object o = result.get(key);

//			logger.debug("## Class Name : " + o.getClass().getName());

			if ("oracle.sql.TIMESTAMP".equals(o.getClass().getName())) {
				o = DateUtil.convertTimestampToString(Timestamp.valueOf(o.toString()), DateUtil.FORMAT_1);
			}
			if ("oracle.sql.CLOB".equals(o.getClass().getName())) {
				o = o.toString();
			}
//			logger.debug("##Text : " + o);
			newResult.put(column, o);
		}
		ObjectMapper m = new ObjectMapper();
		return m.convertValue(newResult, clazz);
	}

	public static HashMap<String, Object> convertObjectToMap(Object obj) {

		ObjectMapper m = new ObjectMapper();
		m.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		HashMap<String, Object> map = m.convertValue(obj, HashMap.class);

		return map;
	}


	public static List<String> getColumnListFromBaseModel(Class<?> clazz) {

		Class superClass = clazz.getSuperclass();

		Field[] fields = null;
		if (superClass.getName().contains("java.lang.Object")) {
			fields = clazz.getDeclaredFields();
		} else {
			fields = superClass.getDeclaredFields();
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add("java.lang.String");
		list.add("java.sql.Timestamp");
		list.add("java.math.BigDecimal");
		list.add("java.lang.Integer");

		List<String> columnList = new ArrayList<String>();
		for (int i = 0; i < fields.length; ++i) {
			if (list.contains(fields[i].getType().getName())) {
//				System.out.println("type:" + fields[i].getType().getName() + ",name:" + fields[i].getName());
				columnList.add(fields[i].getName());
			}
		}
		return columnList;
	}


	public static void main(String[] args) throws SQLException {
		ObjectMapper m = new ObjectMapper();

		Map<String, Object> result = new HashMap<String, Object>();
		String time = "2016-04-22 12:12:12";
//		String time2 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
		byte[] ba = time.getBytes();
		oracle.sql.TIMESTAMP ts = new oracle.sql.TIMESTAMP(time);
		result.put("INS_DT", ts);

		TestClass cf = new TestClass();

		convertMapToObject(result, TestClass.class);

	}

	public static String prettyJson(String jsonString) {

		final String INDENT = "    ";
		StringBuffer prettyJsonSb = new StringBuffer();
		int indentDepth = 0;
		String targetString = null;
		for (int i = 0; i < jsonString.length(); i++) {
			targetString = jsonString.substring(i, i + 1);
			if (targetString.equals("{") || targetString.equals("[")) {
				prettyJsonSb.append(targetString).append("\n\t\t\t\t");
				indentDepth++;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			} else if (targetString.equals("}") || targetString.equals("]")) {
				prettyJsonSb.append("\n\t\t\t\t");
				indentDepth--;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
				prettyJsonSb.append(targetString);
			} else if (targetString.equals(",")) {
				prettyJsonSb.append(targetString);
				prettyJsonSb.append("\n\t\t\t\t");
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			} else {
				prettyJsonSb.append(targetString);
			}
		}

		return prettyJsonSb.toString();

	}
	
	public static String prettyHeader(HttpServletRequest request) {
		String headersString = "[";
		Enumeration<String> e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			String headers = e.nextElement();
			if (headers != null) {
				headersString += "\n\t\t\t\t" + headers + " :: " + request.getHeader(headers);
			}
		}
		headersString += "\n\t\t\t\t]";
		return headersString;
	}

	private static String firstChReplace(String value, boolean upper) {
		if (!CommonUtil.isEmpty(value) && value.length() > 0) {
			String fc = "";
			if (upper) {
				fc = (value.substring(0, 1)).toUpperCase();
			} else {
				fc = (value.substring(0, 1)).toLowerCase();
			}
			return fc + value.substring(1);

		} else {
			return value;
		}
	}

	public static String makeJspUrl(HttpServletRequest request, String viewType) {
		return makeJspUrl(request, viewType, false);
	}

	public static String makeJspUrl(HttpServletRequest request, boolean mobilecheck) {
		return makeJspUrl(request, BaseConstants.VIEW_TYPE_COMMON, mobilecheck);
	}

	public static String makeJspUrl(HttpServletRequest request) {
		return makeJspUrl(request, BaseConstants.VIEW_TYPE_COMMON, false);
	}

	public static String makeJspUrl(HttpServletRequest request, String viewType, boolean mobilecheck) {
		String remainingPaths = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String jspUrl = "";

		StringBuffer requestLog = new StringBuffer("\n");
		requestLog.append("\n======================= PAGE Request =============================");
		requestLog.append("\n* RequestURI	\t:: " + request.getRequestURI());
		requestLog.append("\n* HEADERS	\t::  " + CommonUtil.prettyHeader(request));
		requestLog.append("\n* HTTP METHOD	\t:: " + request.getMethod());
		requestLog.append("\n* ContentType	\t:: " + request.getContentType());
		requestLog.append("\n* View Type	\t:: " + viewType);

		String[] arr = remainingPaths.split("\\?");
		//logger.debug("arr[0] : " + arr[0]);

		String[] rps = arr[0].split("/");

		if (BaseConstants.VIEW_TYPE_POPUP_CCS.equals(viewType)) {
			return remainingPaths;
		}

		int i = 0;
		for (String p : rps) {
			if (i == 0) {
				jspUrl += p;
			} else {
				if (i + 1 == rps.length) {
					if (BaseConstants.VIEW_TYPE_POPUP.equals(viewType)) {
						jspUrl += "/" + rps[i - 2] + firstChReplace(p, true);
					} else {
						jspUrl += "/" + rps[i - 1] + firstChReplace(p, true);
					}
				} else {
					jspUrl += "/" + p;
				}
			}

			i++;
		}

		if (mobilecheck) {
			jspUrl += "_mo";
		}

		requestLog.append("\n* Request JSP	\t:: " + jspUrl);
		requestLog.append("\n======================= PAGE Request =============================");
		logger.debug(requestLog.toString());
		return jspUrl;
	}

	public static String convertInParam(String value) {
		String[] values = value.split(",");
		String result = "";
		for (String v : values) {
			result += "'" + v + "',";
		}
		return result.substring(0, result.length() - 1);
	}

	public static String convertInParam(List<String> values) {
		String result = "";
		for (String v : values) {
			result += "'" + v + "',";
		}
		return result.substring(0, result.length() - 1);
	}

	public static String makeCode(int length) {
		String code = "";
		Random r = new Random();
		String[] beforeShuffle = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

		for (int i = 0; i < length; i++) {
			code += beforeShuffle[r.nextInt(beforeShuffle.length)];
		}

		return code;
	}

	public static String makeRandomNumber(int length) {
		String number = "";
		Random r = new Random();
		String[] beforeShuffle = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

		for (int i = 0; i < length; i++) {
			number += beforeShuffle[r.nextInt(beforeShuffle.length)];
		}

		return number;
	}

	public static Object replaceNull(Object value, Object defaultValue) {
		boolean isNull = false;
		if (value == null) {
			isNull = true;
		} else if (value instanceof String) {
			isNull = ((((String) value).trim().length()) == 0);
		} else if (value instanceof Integer) {
			
		} else if (value instanceof BigDecimal) {
			
		}
		if (isNull) {
			return defaultValue;
		}
		return value;
	}

	public static String makeProductErrorName(String productName, String saleproductName) {
		return "상품 : " + productName + " / 단품 : " + saleproductName;
	}

	public static String makeShortUrl(String url) {
		String shortUrl = "";
		if (isNotEmpty(url)) {
			//TODO short url api
			shortUrl = Config.getString("front.domain.url") + url;
		}
		return shortUrl;
	}
	
	/**
	 * @Method Name : checkByte
	 * @author : ian
	 * @date : 2016. 11. 3.
	 * @description : 지정한 크기 보다 대상 text 가 클때 true 
	 *
	 * @param text
	 * @param std
	 * @return
	 */
	public static boolean checkByte(String text, int std) {
		boolean result = true;
		
		int temp = text.getBytes().length;
		logger.debug("\t 쿠키 바이트 크기 확인  >> " + temp);
		if(temp > std) {
			result = true;
		}
		
		return result; 
	}
}