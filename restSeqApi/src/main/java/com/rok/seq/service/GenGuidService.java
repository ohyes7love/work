package com.rok.seq.service;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.utils.DateUtils;
import com.rok.seq.utils.StringUtils;

/**
 * GUID 채번 업무 로직 처리를 위한 서비스 클래스
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@Service
public class GenGuidService {

	/**
	 * 랜덤 값을 처리하기위해 정의한 변수
	 */
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	/**
	 * 랜덤 값을 처리하기 위해 정의한 RANDOM 변수
	 * ThreadLocalRandom은 동시성 문제를 해결하기 위해 각 쓰레드마다 생성된 인스턴스에서 각각 난수를 반환한다. 따라서 Random과 같은 경합 문제가 발생하지 않아 안전하며, 성능상 이점이 있어 사용
	 */
	private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
	/**
	 * redis 연결을 위한 RedisTemplate
	 */
	@Autowired
	private RedisTemplate<String, String> redisTemplateString;

	/**
	 * guid를 생성한다.
	 * GUID생성룰: yyyyMMddHHmmssSSS(17) + 시스템코드(3) + 노드번호(2) + 인스턴스번호(2) + 랜덤String(6) = 30자리
	 * <p>
	 * 생성룰에 따라 guid를 생성 후 생성 문자열을 리턴한다.
	 * 중복검증: redis에 guid를 key로 입력하여 redis에 데이터가 존재하면 중복발생으로 오류를 처리한다.
	 * redis에 입력된 데이터는 TTL이 1초로 설정되어 1초 뒤 삭제된다.
	 * <p>
	 *
	 * @param GuidInDto 
	 * 		String sendChlCd : guid채번요청 시스템코드 
	 * 		Integer sendSysNodeNo : guid채번요청 시스템의 노드번호 
	 * 		Integer sendSysInstNo : guid채번요청 시스템의 인스턴스번호
	 * @return String guid : 생성된 guid
	 */
	public String generateGuid(GuidInDto in) {

	    StringBuffer guidBuffer = new StringBuffer(30); // StringBuffer를 사용하여 guidBuffer 생성
	    append(guidBuffer, DateUtils.getCurrentDataTimeMillis()); // guidBuffer에 현재 시간(yyyyMMddHHmmssSSS)을 추가하여 guid 생성
	    append(guidBuffer, in.getSendChlCd()); // guidBuffer에 guid채번요청 시스템코드를 추가하여 guid 생성
	    append(guidBuffer, String.format("%02d", in.getSendSysNodeNo())); // guidBuffer에 guid채번요청 시스템 노드번호를 두 자리로 변환하여 추가하여 guid 생성
	    append(guidBuffer, String.format("%02d", in.getSendSysInstNo())); // guidBuffer에 guid채번요청 시스템 인스턴스번호를 두 자리로 변환하여 추가하여 guid 생성
	    append(guidBuffer, generateRandomString(6)); // guidBuffer에 6자리 랜덤 문자열 추가하여 guid 생성
	    
	    String guid = guidBuffer.toString() ; // guidBuffer를 String 형태로 변환하여 guid 변수에 할당

	    // Redis에 guid를 key로 가지는 value 값이 있는지 확인
	    ValueOperations<String, String> vop = redisTemplateString.opsForValue();
	    String value = vop.get(guid);
	    
	    // 기존 중복 테스트를 위한 코드로 작성하였으나, 중복 체크가 의미가 있다고 생각되어 로직 추가.
	    if(StringUtils.isEmpty(value)) { // value 값이 없으면 guid를 key로 하는 value 값을 1초 동안 저장하여 중복 체크
	        vop.set(guid, "dup", 1, TimeUnit.SECONDS) ;
	    } else { // value 값이 있으면 guid 중복으로 RuntimeException 발생
	        throw new RuntimeException("guid 중복이 발생하였습니다. [" + guid +"]") ;
	    }

	    return guid;
	}

	/**
	 * 문자열 추가 메소드
	 * 
	 * @param appendable
	 * @param sequence
	 * @return appendable
	 */
	private Appendable append(Appendable appendable, CharSequence sequence) {
	    // append 메소드를 호출하여 문자열을 추가
	    return append(appendable, sequence, sequence.length());
	}
	/**
	 * 길이에 따른 문자열 추가 메소드
	 * 
	 * @param appendable
	 * @param sequence
	 * @param length
	 * @return appendable
	 */
	private Appendable append(Appendable appendable, CharSequence sequence, int length) {
	    try {
	        // for문을 이용하여 추가할 길이만큼 문자열을 추가
	        for (int i = 0; i < length; i++)
	            appendable.append(sequence.charAt(i));
	    } catch (IOException e) {
	    }
	    // 추가된 문자열을 포함한 appendable 객체 반환
	    return appendable;
	}
	
	/**
	 * 지정된 길이의 랜덤 문자열 생성 메소드
	 *
	 * @param length
	 * @return 랜덤 문자열
	 */
	private String generateRandomString(int length) {
	    char[] buffer = new char[length];
	    for (int i = 0; i < length; i++) {
	        buffer[i] = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
	    }
	    return new String(buffer);
	}
}
