package com.rok.seq.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 문자열 관련 처리를 하는 유틸 클래스 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class StringUtils {
	final static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 문자열에서 특정문자의 갯수를 반환한다.
	 * 
	 * <pre>
	 * 1. 문자열에서 특정문자의 갯수를 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param c
	 * @return
	 */
	public static int getCharCnt(String str, char c) {

		int r = 0;
		char[] chars = str.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == c) {
				r++;
			}
		}
		return r;
	}

	/**
	 * 널여부 혹은 공백여부를 체크.<br>
	 *
	 * @param str
	 * @return boolean - true, false 로 리턴한다
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		}
		return isBlank(((CharSequence) (str.trim())));
	}

	/**
	 * 널여부 혹은 공백여부를 체크.<br>
	 *
	 * @param str
	 * @return boolean - true, false 로 리턴한다
	 */
	public static boolean isBlank(CharSequence str) {
		if (null == str) {
			return true;
		}

		int i = 0;
		for (int n = str.length(); i < n; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * String 의 길이여부 체크
	 * 
	 * <pre>
	 * 1. String 의 길이여부 체크
	 * </pre>
	 *
	 * @param str
	 * @return boolean - true, false 로 리턴한다
	 */
	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}

	/**
	 * String 의 길이여부 체크
	 * 
	 * <pre>
	 * 1. String 의 길이여부 체크
	 * </pre>
	 *
	 * @param str
	 * @return boolean - true, false 로 리턴한다
	 */
	public static boolean hasLength(String str) {
		return hasLength(((CharSequence) (str)));
	}

	/**
	 * String값을 int값으로 변환.<br>
	 *
	 * @param str
	 * @return int - int 값으로 변환해서 리턴한다.
	 */
	public static int strToInt(String str) {
		return strToInt(str, ',');
	}

	/**
	 * String값을 long값으로 변환.<br>
	 *
	 * @param str
	 * @return long - long 값으로 변환해서 리턴한다.
	 */
	public static long strToLong(String str) {
		return strToLong(str, ',');
	}

	/**
	 * String값을 short값으로 변환.
	 * 
	 * <pre>
	 * 1. String값을 short값으로 변환.
	 * </pre>
	 *
	 * @param str
	 * @return short - short 값으로 변환해서 리턴한다.
	 */
	public static short strToShort(String str) {
		return strToShort(str, ',');
	}

	/**
	 * 문자열에서 특정문자를 제거한 값을 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 특정문자를 제거한 값을 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static String remove(String str, char delim) {
		if (str == null || str.length() == 0 || delim == 0) {
			return str;
		}
		char[] chars = str.toCharArray();
		StringBuffer buffer = new StringBuffer(str.length());
		int len = chars.length;
		for (int i = 0; i < len; i++) {
			if (chars[i] != delim)
				buffer.append(chars[i]);
		}
		return buffer.toString();

	}

	/**
	 * 주어진 스트링에 특정 문자를 삽입하는 메소드.
	 *
	 * <pre>
	 * 예)
	 * 	String A = "123456";
	 * 	String value = putString(A, "-", 3);
	 * 	out.print(value);	 //	"123-456" 이 출력된다
	 * </pre>
	 *
	 * @param source
	 *            특정문자를 삽입하고자 하는 대상 문자열
	 * @param put
	 *            삽입하고자 하는 문자
	 * @param putIndex
	 *            특정 문자를 삽입할 위치
	 */
	public static String putString(String source, String put, int putIndex) {
		if (putIndex != 0 && (source == null || source.trim().equals("")))
			return "";
		if (putIndex == 0) {
			return put.concat(source);
		}
		if (source.length() > putIndex) {
			String returnValue = source.substring(0, putIndex);
			returnValue = returnValue.concat(put);
			return returnValue.concat(source.substring(putIndex));
		} else {
			return source;
		}
	}

	/**
	 * 주어진 스트링에 특정 문자를 2곳에 삽입하는 메소드.
	 *
	 * <pre>
	 * 예)
	 * 	String A = "20030101";
	 * 	String value = putString(A, "-", 4, 6);
	 * 	out.print(value);	 //	"2003-01-01" 이 출력된다
	 * </pre>
	 *
	 * @param source
	 *            특정문자를 삽입하고자 하는 대상 문자열
	 * @param put
	 *            삽입하고자 하는 문자
	 * @param putIndex_1
	 *            특정 문자를 삽입할 첫번째 위치
	 * @param putIndex_2
	 *            특정 문자를 삽입할 두번째 위치(putIndex_2는 putIndex_1보다 커야함)
	 */
	public static String putString(String source, String put, int putIndex_1, int putIndex_2) {
		if (source == null || source.trim().equals(""))
			return "";
		if (source.length() > putIndex_2) {
			if (putIndex_1 == 0) {
				return put.concat(source.substring(0, putIndex_2)).concat(put)
						.concat(source.substring(putIndex_2));
			} else {
				return source.substring(0, putIndex_1).concat(put)
						.concat(source.substring(putIndex_1, putIndex_2)).concat(put)
						.concat(source.substring(putIndex_2));
			}
		} else {
			return source;
		}
	}

	/**
	 * 주어진 스트링에 접두어를 추가하는 메소드.
	 *
	 * <pre>
	 * 예)
	 * 	String A = "day";
	 * 	String value = putString(A, "best");
	 * 	out.print(value);	 //	"bestday" 가 출력된다
	 * </pre>
	 *
	 * @param source
	 *            특정문자를 삽입하고자 하는 대상 문자열
	 * @param put
	 *            삽입하고자 하는 문자
	 * @return String
	 */
	public static String putString(String source, String put) {
		return putString(source, put, 0);
	}

	/**
	 * unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
	 *
	 * <pre>
	 * convertHexString(null) = null
	 * convertHexString([(byte)1, (byte)255]) = "01ff" 
	 * </pre>
	 *
	 * @param bytes
	 *            unsigned byte's array
	 * @return
	 * @see HexUtils.toString(byte[])
	 */
	public static String convertHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();
		for (byte b : bytes) {
			result.append(Integer.toString((b & 0xF0) >> 4, 16));
			result.append(Integer.toString(b & 0x0F, 16));
		}
		return result.toString();
	}

	/**
	 * 내외국인 여부 확인
	 *
	 * <pre>
	 * 1. 내외국인 여부 확인
	 * </pre>
	 *
	 * @param pid
	 * @return
	 */
	public static String getForgYn(String pid) {
		if (pid == null || pid.length() != 13 || pid.length() < 13)
			return null;

		String bymdCd = pid.substring(6, 7);

		if ("1".equals(bymdCd) || "2".equals(bymdCd) || "3".equals(bymdCd) || "4".equals(bymdCd)
				|| "9".equals(bymdCd) || "0".equals(bymdCd)) {
			// 내국인
			return "N";
		} else if ("5".equals(bymdCd) || "6".equals(bymdCd) || "7".equals(bymdCd)
				|| "8".equals(bymdCd)) {
			// 외국인
			return "Y";
		}
		return null;
	}


	/**
	 * 문자열에서 [,]를 제거하고, int 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 delim 을 제거하고 int 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static int strToInt(String str, char delim) {
		str = remove(str, delim);
		if (isBlank(str)) {
			return 0;
		} else {
			try {
				return Integer.parseInt(str);
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * 문자열에서 [,]를 제거하고, long 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 delim 을 제거하고, long 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static long strToLong(String str, char delim) {
		str = remove(str, delim);
		if (isBlank(str)) {
			return 0;
		} else {
			try {
				return Long.parseLong(str);
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * 문자열에서 [,]를 제거하고, short 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 [,]를 제거하고, short 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static short strToShort(String str, char delim) {
		str = remove(str, delim);
		if (isBlank(str)) {
			return 0;
		} else {
			try {
				return Short.parseShort(str);
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * 문자열에서 [,]를 제거하고, float 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 [,]를 제거하고, float 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static float strToFloat(String str) {
		return strToFloat(str, ',');
	}

	/**
	 * 문자열에서 [,]를 제거하고, float 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 delim 을 제거하고, float 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static float strToFloat(String str, char delim) {
		str = remove(str, delim);
		if (isBlank(str)) {
			return 0;
		} else {
			try {
				return Float.parseFloat(str);
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * 문자열에서 [,]를 제거하고, double 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 [,]를 제거하고, double 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static double strToDouble(String str) {
		return strToDouble(str, ',');
	}

	/**
	 * 문자열에서 [,]를 제거하고, double 형으로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열에서 delim 을 제거하고, double 형으로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param delim
	 * @return
	 */
	public static double strToDouble(String str, char delim) {

		str = remove(str, delim);
		if (isBlank(str)) {
			return 0;
		} else {
			try {
				return Double.parseDouble(str);
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	/**
	 * 문자열의 길이가 지정된 길이보다 작을경우 차이나는 수만큼 특정 문자를 왼쪽에 추가하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열의 길이가 지정된 길이보다 작을경우 차이나는 수만큼 특정 문자를 왼쪽에 추가하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param len
	 * @param inStr
	 * @return
	 */
	public static String leftAddStr(String str, int len, char inStr) {

		StringBuffer sb = new StringBuffer();
		if (str != null) {
			if (str.length() < len) {
				int gapInt = len - str.length();
				for (int i = 0; gapInt > i; i++) {
					sb.append(inStr);
				}
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 문자열의 길이가 지정된 길이보다 작을경우 차이나는 수만큼 특정 문자를 오른쪽에 추가하여 반환한다.
	 *
	 * <pre>
	 * 1. 문자열의 길이가 지정된 길이보다 작을경우 차이나는 수만큼 특정 문자를 오른쪽에 추가하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @param len
	 * @param inStr
	 * @return
	 */
	public static String rghtAddStr(String str, int len, char inStr) {

		StringBuffer sb = new StringBuffer();
		if (str != null) {
			sb.append(str);
			if (str.length() < len) {
				int gapInt = len - str.length();
				for (int i = 0; gapInt > i; i++) {
					sb.append(inStr);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 문자를 바꾸어주는 메소드이다.
	 *
	 * <pre>
	 * 1. 문자를 바꾸어주는 메소드이다.
	 * </pre>
	 *
	 * @param inString
	 *            원본 문자열
	 * @param oldSubstring
	 *            변경 전 문자열
	 * @param newSubstring
	 *            변경 후 문자열
	 * @return
	 */
	public static String replace(String inString, String oldSubstring, String newSubstring) {
		if (!hasLength(inString) || !hasLength(oldSubstring) || newSubstring == null)
			return inString;
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int index = inString.indexOf(oldSubstring);
		int patLen = oldSubstring.length();
		for (; index >= 0; index = inString.indexOf(oldSubstring, pos)) {
			sb.append(inString.substring(pos, index));
			sb.append(newSubstring);
			pos = index + patLen;
		}

		sb.append(inString.substring(pos));
		return sb.toString();
	}

	/**
	 * 10진수 문자열을 16진수 문자열로 변환하여 반환한다.
	 *
	 * <pre>
	 * 1. 10진수 문자열을 16진수 문자열로 변환하여 반환한다.
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String convertHexString(String str) {
		if (str == null) {
			return "0";
		} else {
			return Integer.toHexString(Integer.parseInt(str));
		}
	}

	/**
	 * 숫자면 true
	 *
	 * <pre>
	 * 1. 숫자면 true
	 * </pre>
	 *
	 * @param c
	 * @return
	 */
	public static boolean isNumber(char c) {

		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 대문자이면 true 를 반환한다.
	 *
	 * <pre>
	 * 1. 대문자이면 true 를 반환한다.
	 * </pre>
	 *
	 * @param ch
	 * @return
	 */
	public static boolean isUpperCase(char ch) {
		boolean r = false;
		if (ch >= 'A' && ch <= 'Z') {
			r = true;
		}
		return r;
	}

	/**
	 * 소문자이면 true 를 반환한다.
	 *
	 * <pre>
	 * 1. 소문자이면 true 를 반환한다.
	 * </pre>
	 *
	 * @param ch
	 * @return
	 */
	public static boolean isLowerCase(char ch) {
		boolean r = false;
		if (ch >= 'a' && ch <= 'z') {
			r = true;
		}
		return r;
	}

	/**
	 * 앞 뒤 트림
	 *
	 * <pre>
	 * 1. 앞 뒤 트림
	 * </pre>
	 *
	 * @param replaceAll
	 * @return
	 */
	public static String allTrim(String replaceAll) {
		if (replaceAll != null) {
			return replaceAll.replaceAll("^\\s+", "").replaceAll("\\s$", "");
		} else {
			return null;
		}
	}

	/**
	 * 문자열의 오른쪽 공백제거
	 * 
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static String rightTrim(String str) {

		String out = str;

		char[] val = out.toCharArray();
		int st = 0;
		int len = out.length();

		while (st < len && val[len - 1] <= ' ') {
			len--;
		}

		out = out.substring(0, len);

		return out;
	}

	/**
	 * 문자열의 왼쪽 공백제거
	 * 
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static String leftTrim(String str) {

		String out = str;
		char[] val = out.toCharArray();
		int st = 0;
		int len = out.length();

		while (st < len && val[st] <= ' ') {
			st++;
		}

		out = out.substring(st, len);
		return out;
	}

	/**
	 * 문자열이 null또는 isEmpty이면 ""을 리턴합니다.
	 * 
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static String nvl(String str) {
		return nvl(str, null);
	}

	/**
	 * 문자열이 null또는 isEmpty이면 defs값을 리턴합니다.
	 * 
	 * ksw
	 * 
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static String nvl(String str, String defs) {

		String out = str;
		String defaults = defs;

		if (isEmpty(out)) {

			if (isEmpty(defaults)) {
				out = "";
			} else {
				out = defaults;
			}
		}
		return out;
	}

	/**
	 * 문자열이 null 이거나 값이 없으면 true를 반환합니다.
	 *
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.isEmpty());
	}

	/**
	 * 문자열이 null이 아니고 1바이트 이상의 값이 존재하면 true를 반환합니다.
	 * 
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str) ? true : false;
	}

	
	/**
	 * 언더바(_)가 포함된 문자열을 카멜표기법형식으로 변경하여 리턴합니다.
	 * ex) DB테이블컬럼 REG_USER_MAIL > regUserMail
	 * 
	 * ksw
	 * 
	 * @param str
	 * @return
	 */
	public static String getJavaFieldNaming(String str) {

		StringBuffer out = new StringBuffer();
		String originalStr = (str.isEmpty() ? "" : str);
		String tempStr = originalStr.toLowerCase();

		if (tempStr.indexOf("_") > -1) {
			int idx = 0;
			for (String metaName : tempStr.split("_")) {
				if (idx == 0) {
					out.append(getFirstCharLowerCase(metaName));
				} else {
					out.append(getFirstCharUpperCase(metaName));
				}
				idx++;
			}

		} else {
			out.append(getFirstCharLowerCase(tempStr));
		}

		return out.toString();
	}

	/**
	 * 문자열의 첫번째 문자를 소문자로 변경하여 리턴합니다.
	 * 
	 * ksw
	 * 
	 * @param strWord
	 * @return
	 */
	public static String getFirstCharLowerCase(String strWord) {

		String out = null;

		String word = nvl(strWord, "").trim();
		if (word.length() > 1) {
			out = word.substring(0, 1).toLowerCase() + word.substring(1);
		} else {
			out = word.toLowerCase();
		}

		return out;
	}

	/**
	 * 문자열의 첫번째 문자를 대문자로 변경하여 리턴합니다.
	 * 
	 * ksw
	 * 
	 * @param strWord
	 * @return
	 */
	public static String getFirstCharUpperCase(String strWord) {

		String out = null;

		String word = nvl(strWord, "").trim();
		if (word.length() > 1) {
			out = word.substring(0, 1).toUpperCase() + word.substring(1);
		} else {
			out = word.toUpperCase();
		}

		return out;
	}
	
	/**
	 * 문자열의 바이트 길이를 return 한다.
	 * 한글 바이트 크기는 DefaultCharset에 따른다.
	 * @param str
	 * @return
	 */
	public static int getBytesLength(String str) {
		if( "UTF-8".equals(Charset.defaultCharset().name().toUpperCase())) {
			return getBytesLength(str, 3);
		}
		return getBytesLength(str, 2);
	}

	/**
	 * 문자열의 바이트 길이를 return 한다.
	 * @param str 문자열
	 * @param hangleByte 한글바이트 단위(default=2, UTF-8=3)
	 */
	public static int getBytesLength(String str, int hangleByte) {
		int en= 0;
		int ko= 0;
		int etc= 0;
		
		char[] charArray= str.toCharArray();
		for( int i=0 ; i < charArray.length ; i++) {
			if( charArray[i] >= 'A' && charArray[i] <= 'z') {
				en= en + 1;
			} else if( charArray[i] >= '\uAC00' && charArray[i] <= '\uD7A3') {
				ko= ko + hangleByte;
			} else {
				etc= etc + 1;
			}
		}
		return en + ko + etc;
	}
	
	/**
	 * 바이트 단위로 문자열을 return 한다.
	 * @param str 문자열
	 * @param beginByte 시작바이트
	 * @param endByte 종료바이트
	 * @param hangleByte 한글바이트 단위(default=2, UTF-8=3)
	 * @param isTrim trim() 사용여부
	 */
	public static String getByteSubString(String str, int beginByte, int endByte, int hangleByte, boolean isTrim) {
		if( getBytesLength(str, hangleByte) < endByte) {
			return str;
		}
		
		try {
			int beginIndex= -1;
			int endIndex= -1;
			int accByte= 0;
			
			for( int i=0 ; i<str.length() ; i++) {
				String ch= str.substring( i, i+1);
				if( ch.getBytes().length >=2) {
					accByte= accByte + hangleByte;
				} else {
					accByte= accByte + 1;
				}
				
				if( beginIndex== -1 && accByte>= beginByte) {
					beginIndex= i;
				}
				
				if( endIndex== -1 && accByte>= endByte) {
					if( accByte> endByte) {
						endIndex= i;
					} else {
						endIndex= i + 1;
					}
					break;
				}
			}
			
			if( beginIndex > -1 && endIndex > -1) {
				if( isTrim) {
					return str.substring(beginIndex, endIndex).trim();
				} else {
					return str.substring(beginIndex, endIndex);
				}
			} else {
				return str;
			}
		} catch( Exception e) {
			logger.error("str={} / beginByte={} / endByte={} / hangleByte={} / isTrim={}"
					, new Object[]{str, beginByte, endByte, hangleByte, isTrim});
			throw e;
		}
	}

	/**
	 * 배열에 담긴내용이 모두 NotEmpty 이면 true 아니면 false 를 리턴하여 줍니다.
	 * 
	 * @param arrayStr
	 * @return
	 */
	public static boolean isNotEmptyStringArray(String... arrayString) {

		boolean out = false;
		String[] strAry = arrayString;

		if (strAry != null && strAry.length > 0) {
			out = true;

			for (String str : strAry) {
				if (isEmpty(str)) {
					out = false;
					break;
				}
			}
		}
		return out;
	}

	/**
	 * 바인드된 문자열을 delimiter로 구분하여 문자배열로 리턴합니다.
	 * ksw
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 */
	public static String[] getTokenizeToStringArray(String str, String delimiter) {
		return getTokenizeToStringArray(str, delimiter, true);
	}

	/**
	 * 바인드된 문자열을 delimiter로 구분하여 문자배열로 리턴합니다.
	 * 구분된 문자열 값을 trim할지의 여부를 선택할 수 있습니다.
	 * 
	 * @param str
	 * @param delimiter
	 * @param trimTokens
	 * @param ignoreEmptyTokens
	 * @return
	 */
	public static String[] getTokenizeToStringArray(String str, String delimiter,
			boolean trimTokens) {
		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiter);
		List<String> tokens = new ArrayList<String>();
		String token;
		for (; st.hasMoreTokens(); tokens.add(token)) {
			token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
		}
		return convertStringArray(tokens);
	}

	/**
	 * 콜렉션을 문자 배열로 변환하여 리턴합니다.
	 * 
	 * @param collection
	 * @return
	 */
	public static String[] convertStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * 바인드된 문자열을 delimiter를 기준으로 문자배열로 변환하여 리턴합니다.
	 * 
	 * @param toSplit
	 * @param delimiter
	 * @return
	 */
	public static String[] getSplit(String toSplit, String delimiter) {
		if ((!hasLength(toSplit)) || (!hasLength(delimiter))) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * 바인드된 문자열의 값을 판단하여 boolean형으로 리턴합니다.
	 * 참을 반환하는 경우는 문자열이 대소문자 관계없이 "Y", "YES", "T", "TRUE", "1" 인 경우에 boolean형 true를 반환합니다.
	 * ksw
	 * 
	 * @param value
	 * @return
	 */
	public static boolean getStringBoolean(String value) {
		boolean out = false;
		String str = (value != null ? value.toUpperCase() : "");
		if (str.equals("Y") || str.equals("YES") || str.equals("T") || str.equals("TRUE")
				|| str.equals("1")) {
			out = true;
		}
		return out;
	}

	/**
	 * 바인드된 encodingArray들중 일치하는 인코딩으로 new String하여 리턴합니다.
	 * @param bytes
	 * @param encodingArray
	 * @return
	 */
	public static String toString(byte[] bytes, String[] encodingArray) {
		String out = null;
		try {
			if(encodingArray != null) {
				
				for(String encoding : encodingArray) {
					if(checkCharset(bytes, encoding)) {
						out = new String(bytes, encoding);
						break;
					}
				}
			}
			if(out == null) {
				out = new String(bytes);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return out;
	}
	
	/**
	 * 케릭터셋이 일치하는지 체크한다.
	 * @param bytes
	 * @param encoding
	 * @return
	 */
	public static boolean checkCharset(byte[] bytes, String encoding) {
		
		try {
			Charset charset = Charset.forName(encoding);
			CharsetDecoder decoder = charset.newDecoder();
			decoder.reset();
			//decode후 바로 clear
			decoder.decode(ByteBuffer.wrap(bytes)).clear();
		} catch (CharacterCodingException e) {
			return false;
		}
		
		return true;
	}
}
