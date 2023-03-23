package com.rok.seq.service;

import java.io.IOException;
import java.util.Random;
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
	 */
	private static final Random RANDOM = new Random();
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

		StringBuffer guidBuffer = new StringBuffer(30);
		append(guidBuffer, DateUtils.getCurrentDataTimeMillis());
		append(guidBuffer, in.getSendChlCd());
		append(guidBuffer, String.format("%02d", in.getSendSysNodeNo()));
		append(guidBuffer, String.format("%02d", in.getSendSysInstNo()));
		append(guidBuffer, generateRandomString(6));
		
		String guid = guidBuffer.toString() ;
		
		ValueOperations<String, String> vop = redisTemplateString.opsForValue();
		String value = vop.get(guid);
		
		if(StringUtils.isEmpty(value)) {
			vop.set(guid, "dup", 1, TimeUnit.SECONDS) ;
		} else {
			throw new RuntimeException("guid 중복이 발생하였습니다. [" + guid +"]") ;
		}

		return guid;
	}

	/**
	 * append를 위한 메소드
	 */
	private Appendable append(Appendable appendable, CharSequence sequence) {
		return append(appendable, sequence, sequence.length());
	}
	private Appendable append(Appendable appendable, CharSequence sequence, int length) {
		try {
			for (int i = 0; i < length; i++)
				appendable.append(sequence.charAt(i));
		} catch (IOException e) {
		}
		return appendable;
	}
	
	/**
	 * 랜덤 String 6자리 문자열을 만들기 위한 메소드.
	 */
	private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
