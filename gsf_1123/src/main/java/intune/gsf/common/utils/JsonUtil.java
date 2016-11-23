package intune.gsf.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * json data를 domain 객체로 변환
	 * @param className
	 * @param dataType
	 * @param str
	 * @return Object
	 */
	public static Object Json2Object(Class<?> clazz, String dataType, String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Object obj = null;
			if ("str".equals(dataType)) { // 1. url
				obj = mapper.readValue(str, clazz);
			} else if ("file".equals(dataType)) { // 2. file
				obj = mapper.readValue(new File(str), clazz);
			} else { // 3. url
			}

			// Pretty print
//			String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//			logger.debug("######### sysout : " + prettyStaff1);

			return obj;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json data를 domain 객체로 변환 (Url data)
	 * @param clazz response class
	 * @param jsonParam
	 * @param urlStr
	 * @return Object
	 */
	public static Object Json2ObjectUrl(Class<?> clazz, String jsonParam, String urlStr) {

		logger.info("-----------------------------------------------------------------");
		logger.info("-- API Call                                         ---");
		logger.info("-----------------------------------------------------------------");
		logger.info("   URL : " + urlStr);
		logger.info("-----------------------------------------------------------------");
		logger.info("   Param : " + jsonParam);
		logger.info("-----------------------------------------------------------------\n");

		logger.debug(urlStr);
		ObjectMapper mapper = new ObjectMapper();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.write(jsonParam.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			Object obj1 = mapper.readValue(conn.getInputStream(), clazz);

			String jsonResult = mapper.writeValueAsString(obj1);

			logger.info("-----------------------------------------------------------------");
			logger.info("  @@ Response : " + jsonResult);
			logger.info("-----------------------------------------------------------------");
//
			return obj1;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json data를 domain 객체로 변환 (Url data)
	 * @param className
	 * @param jsonParam
	 * @param str
	 * @return Object
	 */
	public static Object JsonString2ObjectUrl(String className, String jsonParam, String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Class<?> clazz = Class.forName(className);
			Object obj = null;
			URL url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.write(jsonParam.toString().getBytes("UTF-8"));
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject newJObject = (JSONObject) parser.parse(decodedString);
				
				if (!"200".equals(newJObject.get("resultCode"))) {
					return null;
				} else {
					final CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
					List<?> asList = mapper.readValue(newJObject.get("resultVO").toString(), javaType);
					
					// Pretty print
					for (int i = 0; i < asList.size(); i++) {
						obj = asList.get(i);
						String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
					}
				}
			}
			in.close();
			return obj;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * json data를 domain 객체의 list 로 변환
	 * @param className
	 * @param dataType
	 * @param str
	 * @return List
	 */
	public static List<?> Json2List(Class<?> clazz, String dataType, String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			final CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

			List<?> asList = new ArrayList<Object>();
			if ("str".equals(dataType)) { // 1. string
				asList = mapper.readValue(str, javaType);
			} else if ("file".equals(dataType)) { // 2. file
				asList = mapper.readValue(new File(str), javaType);
			} else { // 3. url
			}

			// Pretty print
//			for (int i = 0; i < asList.size(); i++) {
//				Object obj = asList.get(i);
//				String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//				logger.debug("######### sysout : " + prettyStaff1);
//			}

			return asList;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * json data를 domain 객체의 list 로 변환 (Url data)
	 * @param className
	 * @param jsonParam
	 * @param str
	 * @return List
	 */
	public static List<?> Json2ListUrl(Class<?> clazz, String jsonParam, String str) {
		List<?> asList = new ArrayList<Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			final CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

			URL url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//			wr.write(jsonParam.toString().getBytes("UTF-8"));
			wr.write(jsonParam.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			asList = mapper.readValue(conn.getInputStream(), javaType);

			// Pretty print
			for (int i = 0; i < asList.size(); i++) {
				Object obj = asList.get(i);
				String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//				logger.debug("######### sysout : " + prettyStaff1);
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return asList;
	}
	
	/**
	 * domain 객체를 josn data로 변환
	 * @param className
	 * @param dataType
	 * @param str
	 * @return
	 */
	public static void Object2Json(String className, String dataType, String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Class<?> clazz = Class.forName(className);
			
			if ("str".equals(dataType)) {
				String jsonString = mapper.writeValueAsString(clazz);			
//				logger.debug("######### sysout : " + jsonString);
			} else if ("file".equals(dataType)) { 		// 3. file
				mapper.writeValue(new File(str), clazz);
			} else {	// 3. byte
				byte[] jsonBytes = mapper.writeValueAsBytes(clazz);
//				logger.debug("######### sysout : " + jsonBytes);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 그리드로 넘길 Domain객체를 JsonData로 변환한다. Paging정보까지 반환한다.
	 * @param list
	 * @param rowSize
	 * @param pageNumber
	 * @param totalCnt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object gridJsonRespone(List<?> list, int pageNumber, int rowSize, int totalCnt){
		
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonList = new JSONArray(); 
		JSONObject obj = new JSONObject();   
		
		try {
			
			for (Object object : list) {
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(object);		
				jsonList.add(jsonParser.parse(jsonString));
			}

			//현재페이지의 목록JsonDate데이터
			obj.put("rows",jsonList);
			//총글목록수
			obj.put("records",totalCnt);
			//현재페이지
			obj.put("page",pageNumber);
			//총페이지수
			obj.put("total",(int) Math.ceil((float)totalCnt/(float)rowSize));
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return obj ;
	}

	/**
	 * json data를 string 으로 반환 (Url data)
	 * @Method Name : Json2StringUrl
	 * @author : peter
	 * @date : 2016. 9. 6.
	 * @description :
	 *
	 * @param jsonParam
	 * @param str
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String Json2StringUrl(String jsonParam, String str) {
		try {
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			com.sun.net.ssl.internal.ssl.Provider provider = new com.sun.net.ssl.internal.ssl.Provider();
			Security.addProvider(provider);

			URL url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.write(jsonParam.toString().getBytes("UTF-8"));
//			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//			wr.write(jsonParam);
			wr.flush();
			wr.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuffer response = new StringBuffer();
				String decodedString = "";
				while ((decodedString = in.readLine()) != null) {
					response.append(decodedString);
				}
				in.close();
				return response.toString();
			} else {
				return null;
			}
		} catch (JsonGenerationException je) {
			je.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
