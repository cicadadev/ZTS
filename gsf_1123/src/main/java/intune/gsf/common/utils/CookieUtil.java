package intune.gsf.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static final int SECONDS_OF_DAY = 60 * 60 * 24;
	public static final int SECONDS_OF_YEAR = 60 * 60 * 24 * 365;
	public static final int SECONDS_OF_10YEAR = 60 * 60 * 24 * 365 * 10;
	public static final int MAXAGE_SESSION = -1;
	public static final int MAXAGE_TERMINATION = 0;

	public static final String DEFAULT_PATH = "/";

	@SuppressWarnings("rawtypes")
	private static Map cookieMap = new HashMap();

	/**
	 * Cookie 생성
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void createCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie;
		try {
			cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			cookie.setPath(DEFAULT_PATH);
			cookie.setMaxAge(SECONDS_OF_YEAR);
			cookie.setSecure(false);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cookie 생성
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void createCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie;
		try {
			cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			cookie.setPath(DEFAULT_PATH);
			cookie.setMaxAge(maxAge < 0 ? MAXAGE_SESSION : maxAge);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cookie 생성
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 * @param maxAge
	 */
	public static void createCookie(HttpServletResponse response, String name, String value, String domain, String path, int maxAge) {
		Cookie cookie;
		try {
			cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			cookie.setDomain(domain);
			cookie.setPath(path);
			cookie.setMaxAge(maxAge < 0 ? MAXAGE_SESSION : maxAge);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cookie 값 지우기
	 * 
	 * @description : 쿠키 전체 삭제
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie;
		cookie = new Cookie(name, null);
		cookie.setMaxAge(MAXAGE_TERMINATION);
		cookie.setPath(DEFAULT_PATH);                    //쿠키 접근 경로 지정
		response.addCookie(cookie);
	}

	/**
	 * Cookie에 저장된 name 의 value 가져오기
	 * 
	 * @param request
	 * @param name
	 * @return String
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

	/**
	 * @Method Name : createCookieString
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : string으로 쿠키 데이터 저장
	 * ex) createCookieString(request, response, 쿠키 이름, 쿠키 값, 쿠키 유효기간(초 입력), 쿠키 각 데이터 만료일 설정)
	 *
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @throws Exception
	 */
	public static void createCookieString(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge, BigDecimal addDt)
			throws Exception {

		Cookie[] cookies = request.getCookies(); // 기존 저장된 쿠키 호출
		String arrayId = "";
		String type = "";
		
		// 모바일 history 쿠키의 value는 "type","value" 로 구성된다.
		if("moHistory".equals(name)) {
			String[] tempVal = value.split(",");
			type = tempVal[0];
			if("SEARCH".equals(type)) {
				arrayId = URLEncoder.encode(tempVal[1], "UTF-8");
			} else {
				arrayId = tempVal[1];
			}
		} else {
			arrayId = URLEncoder.encode(value, "UTF-8");
		}
		arrayId = arrayId.replaceAll("\\+", "%20"); // 공백 인코딩 처리
			
			boolean existsCookie = false;	// 쿠키 리스트 내 값 존재 유무
			if (cookies != null) {
				String result = "";
				List<String[]> beforeSort = new ArrayList<String[]>();
				
				for (Cookie cookie : cookies) {
					
					/*****
					 * 새로 저장할 쿠키가 기존 리스트에 저장된 쿠키인지 확인
					 * existsCookie : true // 리스트 구분자 '|'
					 * *****/
					if (cookie.getName().equals(name)) {
						existsCookie = true;
						boolean existsData = false;	// 쿠키 리스트 내 배열 값 존재 유무
						
						try {
							String[] list = (cookie.getValue().toString()).split("[|]");
							CommonUtil.checkByte(list.toString(), 1000000000);
							
							/*
							 * 1. 기존 쿠키에 저장된 배열 값 일때 -> 유효기간 갱신 
							 * 2. 기존 쿠키에 저장된 배열 값 아닐때 
							 * 		2-1. 다른 배열 값 중 유효기간 만료 항목 제거 
							 * 		2-2. 새 배열 값 쿠키 저장
							 * 3. 쿠키 배열 데이터 개수 검사
							 * 4. 쿠키 갱신
							 */
							
							/*****
							 * 기존 리스트 내에 저장된 배열 값인지 확인
							 * existsCookie : true // 배열 구분자 ','
							 * *****/
							for (int i = 0; i < list.length; i++) {
								String[] cookieObj = (list[i].toString()).split(",");
								
								// 쿠키 배열 순서 0: id, 1: endDate, 2: type
								
								// 1. 존재 배열 값 기간 갱신
								if ((cookieObj[0]).equals(arrayId)) {
									cookieObj[1] = DateUtil.getAddDateByCookie(DateUtil.FORMAT_10, DateUtil.getCurrentDate(DateUtil.FORMAT_10), addDt);
									if(CommonUtil.isNotEmpty(type)) {
										cookieObj[2] = type;
									}
									
									beforeSort.add(cookieObj);
									
									existsData = true;
								}
								// 2-1. 기존 저장된 다른 배열 값, 유효기간 확인
								else {
									String endDate = (String) cookieObj[1];
									String today = DateUtil.getCurrentDate(DateUtil.FORMAT_10);
									
									// -1 이전, 0 같음, 1 이후
									if (DateUtil.compareToDate(today, endDate, DateUtil.FORMAT_10) != 1) {
										beforeSort.add(cookieObj);
									}
								}
							}
							
							// 2-2. 쿠키 리스트 안에 새 배열 값 추가
							if (!existsData) {
								// 0: id(or keyword) 1: enddate 2: type 
								int columnCnt = 2;	// 기본 : 0=id (검색쿠키 일때 keyword), 1= enddate 
								if(CommonUtil.isNotEmpty(type)) {
									columnCnt = 3; // mohistory
								}
								
								String[] newArray = new String[columnCnt];
								newArray[0] = arrayId;	// id 값
								newArray[1] = DateUtil.getAddDateByCookie(DateUtil.FORMAT_10, DateUtil.getCurrentDate(DateUtil.FORMAT_10), addDt); // 배열 유효기간 값
								
								// mobile history 쿠키 일 때
								if(columnCnt > 2) {
									newArray[2] = type;	// 배열의 유형 값
								}
								beforeSort.add(newArray);
							}

							// 쿠키 배열 시간순 정렬
							if(beforeSort.size() > 0) {
								result = sortCookieArray(beforeSort);
							}
							
							// 5. 쿠키갱신
							cookie.setValue(result);
							cookie.setPath(DEFAULT_PATH);
							cookie.setMaxAge(maxAge < 0 ? MAXAGE_SESSION : maxAge);
							response.addCookie(cookie);
							
						} catch (Exception e) {
							e.printStackTrace();
							existsCookie = false;
							removeCookieAll(request, response, name);
						}
						break;
					}
					
				}	// for end
				
				
				/*****
				 * 새로 저장할 쿠키가 기존 리스트에 저장된 쿠키가 아니라면 새로 쿠키 생성
				 * existsCookie : false
				 * *****/
				if(!existsCookie) {
					String newArray = ""; 
					newArray = arrayId + ",";	// id 값
					newArray += DateUtil.getAddDateByCookie(DateUtil.FORMAT_10, DateUtil.getCurrentDate(DateUtil.FORMAT_10), addDt) + ",";	// 배열 값 유효기간
					
					// mobile history 쿠키 일 때
					if(CommonUtil.isNotEmpty(type)) {
						newArray += type + ",";	// 유형 값
					}

					result = newArray.substring(0, newArray.length()-1);;

					// cookie = new Cookie(name, URLEncoder.encode(newArray.toString(), "UTF-8"));
					Cookie cookie = new Cookie(name, result);
					cookie.setPath(DEFAULT_PATH);
					cookie.setMaxAge(maxAge < 0 ? MAXAGE_SESSION : maxAge);
					response.addCookie(cookie);
				}

			}
	}
	
	/**
	 * @Method Name : getCookieList
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : 쿠키 리스트 json 형식의 string으로 반환. cookieSize 0 입력시 쿠키안에 저장된 모든 데이터 반환
	 *
	 * @param request
	 * @param name
	 * @param cookieSize
	 * @return
	 * @throws Exception
	 */
	public static String getCookieList(HttpServletRequest request, HttpServletResponse response, String name, int cookieSize) throws Exception {
		Cookie[] cookies = request.getCookies();
		String result = "";

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					/***
					 * cookie.getValue().toString().indexOf("[{") == -1
					 * **/ 
					if(CommonUtil.isNotEmpty(cookie.getValue()) && cookie.getValue().toString().indexOf("[{") == -1) {
						String[] list = (cookie.getValue().toString()).split("[|]");
						for (int i = 0; i < list.length; i++) {
							String[] array = (list[i].toString()).split(",");
							
							String endDate = array[1];
							String today = DateUtil.getCurrentDate(DateUtil.FORMAT_10);
							
							// -1 이전, 0 같음, 1 이후
							if (DateUtil.compareToDate(today, endDate, DateUtil.FORMAT_10) != 1) {
								result += array[0] + ",";
							}
						}
						
						// 4. 노출 사이즈 확인
						if(cookieSize != 0) {
							if(list.length > cookieSize) {
								String chgCookieArray = "";
								
								for(int i = 0; i< cookieSize; i++) {
									chgCookieArray += list[i] + "|";
								}
								chgCookieArray.substring(0, chgCookieArray.length()-1);
								
								// 4-1. 쿠키갱신
								cookie.setValue(chgCookieArray);
								cookie.setPath(DEFAULT_PATH);
								response.addCookie(cookie);
							}
						}
						result = result.substring(0, result.length()-1);
					} else {
						removeCookieAll(request, response, name);
					}
				}
			}
		}
		
		return result;

	}
	
	public static List<Map<String,String>> getMoHistoryCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Cookie[] cookies = request.getCookies();
		List<Map<String,String>> resultArr = new ArrayList<Map<String,String>>();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("moHistory")) {
					if(CommonUtil.isNotEmpty(cookie.getValue()) && cookie.getValue().toString().indexOf("\\[\\{") == -1) {
						String[] list = (cookie.getValue().toString()).split("[|]");
						for (int i = 0; i < list.length; i++) {
							String[] array = (list[i].toString()).split(",");
							
							String endDate = array[1];
							String today = DateUtil.getCurrentDate(DateUtil.FORMAT_10);
							
							// -1 이전, 0 같음, 1 이후
							if (DateUtil.compareToDate(today, endDate, DateUtil.FORMAT_10) != 1) {
								try {
									Map<String,String> map = new HashMap<String,String>();
									String type = (String) array[2];
									if("SEARCH".equals(type)) {
										map.put("id",URLDecoder.decode((String) array[0],"UTF-8"));
									} else {
										map.put("id",(String) array[0]);
									}
									map.put("type", type);
									
									resultArr.add(map);
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
						}
						
						// 4. 노출 사이즈 확인
						if(list.length > 50) {
							String chgCookieArray = "";
							for(int i = 0; i< 50; i++) {
								chgCookieArray += list[i] + "|";
							}
							
							chgCookieArray.substring(0, chgCookieArray.length()-1);
							
							// 4-1. 쿠키갱신
							cookie.setValue(chgCookieArray);
							cookie.setPath(DEFAULT_PATH);
							response.addCookie(cookie);
						}
					} else {
						// 데이터가 각기 삭제되어 빈 쿠키만 남아있다면 쿠키 자체를 삭제
						removeCookieAll(request, response, "moHistory");
					}
					
				}	// mobile history 쿠키 get end

			}
		}
		
		return resultArr;
	}

	/**
	 * @Method Name : removeCookieAll
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : 쿠키 삭제
	 *
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void removeCookieAll(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue(null);
					cookie.setPath(DEFAULT_PATH);
					cookie.setMaxAge(MAXAGE_TERMINATION);
					response.addCookie(cookie);
				}
			}
		}
	}
	
	/**
	 * @Method Name : removeCookieData
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : 쿠키 데이터 개별 삭제
	 *
	 * @param response
	 * @param name
	 * @throws UnsupportedEncodingException 
	 */
	public static void removeCookieData(HttpServletRequest request, HttpServletResponse response, String name, String value) throws UnsupportedEncodingException {
		
		Cookie[] cookies = request.getCookies(); // 기존 저장된 쿠키 호출
		String encodingVal = "";
		
		// 검색 쿠키일 때만 decode
		if("searchKeyWord".equals(name)) {
			encodingVal = URLDecoder.decode(value,"UTF-8");
		} else {
			encodingVal = value;
		}
		
		if (cookies != null) {
			String result = new String();
			List<String[]> beforSortList = new ArrayList<String[]>();
			
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					String[] list = (cookie.getValue().toString()).split("[|]");
					
					for (int i = 0; i < list.length; i++) {
						String[] cookieObj = (list[i].toString()).split(",");
						// 쿠키 배열 순서 0: id, 1: endDate, 2: type, 3: name, 4:date-전시기간(이벤트,기획전)
						
						// 삭제 할 Data 제외한 쿠키 저장
//						System.out.println("\t cookie del controller >> " + value);
//						System.out.println("\t cookie encodig >> " + encodingVal);
//						System.out.println("\t cookie data >> " + cookieObj[0]);
						if (!(cookieObj[0]).equals(encodingVal)) {
							beforSortList.add(cookieObj);
						}
					}
					
					// 쿠키 배열 시간순 정렬
					if(beforSortList.size() > 0) {
						result = sortCookieArray(beforSortList);
					}
					
					// 쿠키갱신
					cookie.setValue(result);
					cookie.setPath(DEFAULT_PATH);
					cookie.setMaxAge(cookie.getMaxAge());
					response.addCookie(cookie);
					
				}
				
			} // for end
		}
	}
	
	/**
	 * @Method Name : sortCookieArray
	 * @author : ian
	 * @date : 2016. 11. 14.
	 * @description : 쿠키 배열 최신순 정렬
	 *
	 * @param beforSortList
	 * @return
	 */
	private static String sortCookieArray(List<String[]> beforSortList) {
		String result = "";
		
		// 3. 시간순 정렬
		Collections.sort( beforSortList, new Comparator<String[]>() {
			public int compare(String[] a, String[] b) {
				String valA = new String();
				String valB = new String();
				
				valA = (String) a[1];
				valB = (String) b[1];
				
				return valA.compareTo(valB);
			}
		});
		
		// desc
		for (int i = beforSortList.size()-1; i >= 0; i--) {
			String[] array = beforSortList.get(i);
			String temp = "";
			for(int j=0; j<array.length; j++){
				temp += array[j] + ",";
			}
//			System.out.println("\t before array >> " + temp);
			temp = temp.substring(0, temp.length()-1);
//			System.out.println("\t after array >> " + temp);
			
			result += temp + "|";
		}
		
//		System.out.println("\t before list >> " + result);
		result = result.substring(0, result.length()-1);
//		System.out.println("\t after list >> " + result);
		
		return result;
	}
	
	public boolean exists(String name) {
		return cookieMap.get(name) != null;
	}

}
