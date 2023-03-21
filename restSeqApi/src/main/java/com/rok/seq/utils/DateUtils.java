package com.rok.seq.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils { 

	/**
	 * 현재 일자, 시간을 1000분의 1초까지 리턴
	 * 
	 * @return yyyyMMddHHmmssSSS 패턴 형태의 날짜
	 */
	public static String getCurrentDataTimeMillis() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = LocalDateTime.now().format(formatter);
		return formattedDateTime ;
	}
	
	public static String getCurrentDate() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
    }
	
	/**
	 * 현재 날짜를 주어진 패턴 형태로 리턴하는 펑션
	 * 
	 * <pre>
	 * 아규먼트로 사용되는 pattern에 대한 자세한 내용은
	 * <a href="http://java.sun.com/j2se/1.3/docs/api/java/text/SimpleDateFormat.html">java.text.
	 * SimpleDateFormat</a>을 참조
	 * </pre>
	 * 
	 * @param pattern
	 *            표현하고자 하는 날짜 표시 패턴.
	 * @return String 패턴 형태의 날짜
	 */
	public static String getDate(String pattern) {
		SimpleDateFormat sd = new SimpleDateFormat(pattern);
		return sd.format(Calendar.getInstance().getTime());
	}

	/**
	 * 오늘날짜에서 이전 혹은 이후 날짜값을 받아온다.
	 * 
	 * <pre>
	 * 0인 경우 현재 날짜값이 반환된다.
	 * </pre>
	 * 
	 * @param addDate
	 *            가감하고자 하는 날짜의 수.
	 * @return String YYYYMMDD 포맷형식의 날짜값을 반환한다.
	 */
	public static String getDate(int addDate) {

		DecimalFormat df = new DecimalFormat("00");

		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.add(Calendar.DATE, addDate);
		String strYear = Integer.toString(currentCalendar.get(Calendar.YEAR));
		String strMonth = df.format(currentCalendar.get(Calendar.MONTH) + 1);
		String strDay = df.format(currentCalendar.get(Calendar.DATE));
		String strDate = strYear + strMonth + strDay;
		return strDate;
	}

	/**
	 * 오늘날짜값을 받아온다.
	 * 
	 * <pre>
	 * 오늘날짜값을 받아온다.
	 * </pre
	 * 
	 * @return String YYYYMMDD 포맷형식의 날짜
	 */
	public static String getDate() {
		return getDate(0);
	}

	/**
	 * 오늘 기준의 년을 반환한다.
	 * 
	 * <pre>
	 * 1. 오늘 기준의 년을 반환한다.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getYear() {
		return getDate("yyyy");
	}

	/**
	 * 오늘 날짜의 달을 반환한다.
	 * 
	 * <pre>
	 * 1. 오늘 날짜의 달을 반환한다.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getMonth() {
		return getDate("MM");
	}

	/**
	 * 오늘날짜의 일을 반환한다.
	 * 
	 * <pre>
	 * 1. 오늘날짜의 일을 반환한다.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getDay() {
		return getDate("dd");
	}

	/**
	 * 달의 마지막 일 계산
	 * 
	 * <pre>
	 * 1. 달의 마지막 일 계산
	 * </pre>
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static int lastDay(int year, int month) {
		int day = 0;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				if ((year % 4) == 0) {
					if ((year % 100) == 0 && (year % 400) != 0) {
						day = 28;
					} else {
						day = 29;
					}
				} else {
					day = 28;
				}
				break;
			default:
				day = 30;
		}
		return day;
	}

	/**
	 * 특정 날짜에 일을 더해서 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짜에 일을 더해서 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public static String addDay(String date, String format, int day) throws Exception {
		Calendar c = getCalendarDate(date, format);
		c.add(Calendar.DAY_OF_MONTH, day);
		return new SimpleDateFormat(format).format(c.getTime());
	}

	/**
	 * 특정 날짜에 달(월)을 더해서 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짜에 달(월)을 더해서 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public static String addMonth(String date, String format, int month) throws Exception {
		Calendar c = getCalendarDate(date, format);
		c.add(Calendar.MONTH, month);
		return new SimpleDateFormat(format).format(c.getTime());
	}

	/**
	 * 날짜 형식을 변환하여 반환한다.
	 * 
	 * <pre>
	 * 1. 날짜 형식을 변환하여 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 * @throws Exception
	 */
	public static String getDate(String date, String fromFormat, String toFormat) throws Exception {

		SimpleDateFormat from = new SimpleDateFormat(fromFormat);
		SimpleDateFormat to = new SimpleDateFormat(toFormat);
		return to.format(from.parse(date));
	}

	/**
	 * 날짜형식을 변환하여 반환한다.
	 * 
	 * <pre>
	 * 1. 날짜형식을 변환하여 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 특정 날짜의 년도를 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짜의 년도를 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String getYear(String date, String format) throws Exception {
		Calendar c = getCalendarDate(date, format);
		return String.valueOf(c.get(Calendar.YEAR));
	}

	/**
	 * 특정 날짜의 달(월)을 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짜의 달(월)을 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String getMonth(String date, String format) throws Exception {
		Calendar c = getCalendarDate(date, format);
		return String.valueOf(c.get(Calendar.MONTH) + 1);
	}

	/**
	 * 특정 날짜의 일을 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짜의 일을 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String getDay(String date, String format) throws Exception {
		Calendar c = getCalendarDate(date, format);
		return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 특정 달(월)의 마지막 날을 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 달(월)의 마지막 날을 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static int getLastDay(String date, String format) throws Exception {

		Calendar c = getCalendarDate(date, format);
		return lastDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
	}

	/**
	 * 특정 날짱와 날짜의 사이의 일수를 반환한다.
	 * 
	 * <pre>
	 * 1. 특정 날짱와 날짜의 사이의 일수를 반환한다.
	 * </pre>
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static int getDifDays(String fromDate, String toDate, String format) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long diff = sd.parse(toDate).getTime() - sd.parse(fromDate).getTime();

		return (int) (diff / (1000 * 60 * 60 * 24)); // 1일의 밀리세컨드 : 1000 * 60 * 60 * 24
	}

	/**
	 * String 날짜를 Date 형 으로 변환하여 반환한다.
	 * 
	 * <pre>
	 * 1. String 날짜를 Date 형 으로 변환하여 반환한다.
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private static Calendar getCalendarDate(String date, String format) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sd = new SimpleDateFormat(format);
		c.setTime(sd.parse(date));
		return c;
	}
}
