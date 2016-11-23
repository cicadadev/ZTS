package intune.gsf.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : DateUtil.java
 * @author : eddie
 * @date : 2016. 4. 22.
 * @description :
 */
public class DateUtil {

	public static String	FORMAT_1	= "yyyy-MM-dd HH:mm:ss";
	public static String	FORMAT_2	= "yyyy-MM-dd";
	public static String	FORMAT_3	= "yyyyMMdd";
	public static String	FORMAT_4	= "yyyy.MM.dd";
	public static String	FORMAT_5	= "yyyy";
	public static String	FORMAT_6	= "MMdd";
	public static String	FORMAT_7	= "yyyy/MM/dd";
	public static String	FORMAT_8	= "yyyy/MM/dd HH:mm:ss";
	public static String	FORMAT_9	= "yyyy-MM-DD'T'HH:mm:ss";
	public static String	FORMAT_10	= "yyyyMMddHHmmss";
	public static String	FORMAT_11	= "yyyyMM";
	public static String	FORMAT_12	= "HHmmss";
	public static String	FORMAT_13	= "yyyyMMddHHmm";
	public static String	FORMAT_14	= "yyyy년MM월dd일";
	public static String	FORMAT_15	= "yy/MM/dd";
	/**
	 * 현재 시간을 Timestamp로 리턴
	 * 
	 * @Method Name : getCurrentDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @return
	 */
	public static Timestamp getCurrnetTimestamp() {

		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_1);

		Calendar cal = Calendar.getInstance();
		String today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);

		return ts;

	}

	/**
	 * 스트링 Date를 Timestamp로 변환
	 * 
	 * @Method Name : convertStringToTimestamp
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp convertStringToTimestamp(String stringDate, String format) throws ParseException {

		return DateUtil.convertDateToTimestamp(DateUtil.convertStringToDate(stringDate, format));
	}

	public static String convertFormat(String date1, String format1, String format2) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat(format1);
		SimpleDateFormat formatter2 = new SimpleDateFormat(format2);
		Date date = formatter.parse(date1);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return formatter2.format(cal.getTime());

	}

	/**
	 * 스트링 데이터를 date형으로 변환
	 * 
	 * @Method Name : convertStringToDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param from
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String from, String format) throws ParseException {
		SimpleDateFormat transFormat = new SimpleDateFormat(format);

		Date to = transFormat.parse(from);
		return to;
	}

	/**
	 * Timestamp를 Date로 변환
	 * 
	 * @Method Name : convertTimestampToDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param stamp
	 * @return
	 */
	public static Date convertTimestampToDate(Timestamp stamp) {
		Date date = new Date(stamp.getTime());
		return date;
	}

	/**
	 * Date를 Timestamp로 변환
	 * 
	 * @Method Name : convertDateToTimestamp
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param d
	 * @return
	 */
	public static Timestamp convertDateToTimestamp(Date d) {
		return new Timestamp(d.getTime());
	}

	/**
	 * 현재시간을 format 형식으로 리턴
	 * 
	 * @Method Name : getCurrentDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		String today = formatter.format(cal.getTime());
		return today;
	}

	/**
	 * 일자 계산 format 형식으로 리턴
	 * 
	 * @Method Name : getAddDate
	 * @author : ian
	 * @date : 2016. 6. 13.
	 * @description :
	 *
	 * @param format, inputDate, day
	 * @return
	 * @throws Exception
	 */
	public static String getAddDate(String format, String inputDate, BigDecimal day) {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String resultDay = null;
		try {
			Date date = formatter.parse(inputDate);
			int days = day.intValue();

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, days);

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			resultDay = formatter.format(cal.getTime());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultDay;
	}
	
	public static String getAddDateByCookie(String format, String inputDate, BigDecimal day) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String resultDay = null;
		try {
			Date date = formatter.parse(inputDate);
			int days = day.intValue();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, days);

			resultDay = formatter.format(cal.getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultDay;
	}

	/**
	 * 일자 계산 format 형식으로 리턴
	 * 
	 * @Method Name : getAddMinute
	 * @author : ian
	 * @date : 2016. 10. 22.
	 * @description :
	 *
	 * @param format, inputDate, minute
	 * @return
	 * @throws Exception
	 */
	public static String getAddMinute(String format, String inputDate, BigDecimal minute) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = formatter.parse(inputDate);
		int minutes = minute.intValue();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);

		String resultDay = formatter.format(cal.getTime());

		return resultDay;
	}

	/**
	 * 월 계산 format 형식으로 리턴
	 * 
	 * @Method Name : getAddMonth
	 * @author : ian
	 * @date : 2016. 8. 12.
	 * @description :
	 *
	 * @param format, inputDate, month
	 * @return
	 * @throws Exception
	 */
	public static String getAddMonth(String format, String inputDate, BigDecimal month) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = formatter.parse(inputDate);
		int months = month.intValue();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);

		String resultDay = formatter.format(cal.getTime());

		return resultDay;
	}

	/**
	 * 원하는 월의 마지막 일자 리턴, inputMonth - 1
	 * 
	 * @Method Name : getMonthLastDay
	 * @author : ian
	 * @date : 2016. 9. 5.
	 * @description : inputMonth null 입력시 현재 월 마지막 일자 리턴, ex) 11 - 1 = 11월
	 *
	 * @param format
	 * @param inputMonth
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static String getMonthLastDay(String format, String inputMonth) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		if (CommonUtil.isNotEmpty(inputMonth)) {
			cal.set(Calendar.MONTH, Integer.parseInt(inputMonth) - 1);
		}

		cal.set(Calendar.DATE, (int) cal.getActualMaximum(cal.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		String resultDay = formatter.format(cal.getTime());

		return resultDay;
	}

	/**
	 * @Method Name : getWeekStartEndDay
	 * @author : ian
	 * @date : 2016. 10. 6.
	 * @description : 현재일에 속한 주의 월요일 or 일요일을 구함
	 *
	 * @param format
	 * @param inputDate
	 * @param isStart : true 월요일 / false 일요일
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static String getWeekStartEndDay(String format, String inputDate, boolean isStart) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = formatter.parse(inputDate);
	
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		String temp = (cal.getTime()).toString();
		
		if(isStart) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			if(temp.indexOf("Sun") == 0) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				cal.add(cal.DATE, -6);			
			}
		} else {
			if(temp.indexOf("Sun") == -1) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				cal.add(cal.DATE, 7);
			}
		}
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		String resultDay = formatter.format(cal.getTime());

		return resultDay;
	}
	
	/**
	 * timestamp 를 format 형식으로 리턴
	 * 
	 * @Method Name : getCurrentDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @param format
	 * @return
	 */
	public static String convertTimestampToString(Timestamp t, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
//		Calendar cal = Calendar.getInstance();
		String today = formatter.format(t);
		return today;
	}
	
	

	/**
	 * 현재시간을 Date로 리턴
	 * 
	 * @Method Name : getCurrentDate
	 * @author : eddie
	 * @date : 2016. 4. 22.
	 * @description :
	 *
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * 특정일(시)를 비교대상일(시)와 비교, 파라메터의 날짜형식, 포맷은 같아야 한다.
	 * 
	 * @Method Name : compareToDate
	 * @author : ian
	 * @date : 2016. 7. 15.
	 * @description :
	 *
	 * @param targetDt 비교날짜
	 * @param compareToDt 비교대상날짜
	 * @param format
	 * @return 0 같은, -1 이전, 1 이후
	 */
	public static int compareToDate(String targetDt, String compareToDt, String format) {
		int result = 0;

		try {
			Date target = convertStringToDate(targetDt, format);
			Date compare = convertStringToDate(compareToDt, format);

			if (target.getTime() < compare.getTime()) {
				result = -1;
			} else if (target.getTime() > compare.getTime()) {
				result = 1;
			} else if (target.getTime() == compare.getTime()) {
				result = 0;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * date1, date2사이의 개월 수 계산
	 */
	public static int getMonthPeriod(String strtDate, String endDate) {

		int strtYear = Integer.parseInt(strtDate.substring(0, 4));
		int strtMonth = Integer.parseInt(strtDate.substring(4, 6));

		int endYear = Integer.parseInt(endDate.substring(0, 4));
		int endMonth = Integer.parseInt(endDate.substring(4, 6));

		int month = (endYear - strtYear) * 12 + (endMonth - strtMonth);
		return month;

	}

	/**
	 * date1, date2사이의 일 수 계산
	 * 
	 * @throws ParseException
	 */
	public static long getDayPeriod(String startDate, String endDate, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		Date begin = formatter.parse(startDate);
		Date end = formatter.parse(endDate);

		long diff = end.getTime() - begin.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;

	}

	/**
	 * 생년월일로 나이계산
	 * 
	 * @Method Name : getMyAge
	 * @author : intune
	 * @date : 2016. 10. 4.
	 * @description :
	 *
	 * @param birthday YYYYMMDD
	 * @return
	 */
	public static int getMyAge(String birthday) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(birthday.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(birthday.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(birthday.substring(6, 8)));

		Calendar now = Calendar.getInstance();

		int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH)) || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
			age--;
		}
		return age;
	}

	/**
	 * 개월 수로 일수계산
	 * 
	 * @Method Name : getDayByYear
	 * @author : intune
	 * @date : 2016. 10. 23.
	 * @description :
	 *
	 * @return
	 */
	public static int getDayByYear(int month) {
		return month;
	}

	/**
	 * 요일 계산
	 * 
	 * 1 = "일" 2 = "월" 3 = "화" 4 = "수" 5 = "목" 6 = "금" 7 = "토"
	 * 
	 * @Method Name : getDayByYear
	 * @author : intune
	 * @date : 2016. 10. 23.
	 * @description :
	 *
	 * @return
	 */
	public static BigDecimal getDateDay(String date, String dateType) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
		Date nDate = dateFormat.parse(date);

		Calendar cal = Calendar.getInstance();
		cal.setTime(nDate);

		return new BigDecimal(cal.get(Calendar.DAY_OF_WEEK));
	}
}
