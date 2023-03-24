package com.rok.seq.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.rok.seq.service.dto.SequenceStateDto;
import com.rok.seq.utils.DateUtils;

/**
 * 시퀀스 채번 업무 로직 처리를 위한 서비스 클래스
 * 
 * @author ohyes7love@naver.com
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class GenSeqService {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 시퀀스 최대값
	 */
	private static final long MAX_SEQUENCE_NUMBER = 9999999999L;
	/**
	 * 동시접근제어를 위한 lock key값
	 */
	private static final String lockName = "seqLock";
	/**
	 * redis에 저장하는 시퀀스정보의 key
	 */
	private static final String seqKey = "seq";

	/**
	 * redis 연결을 위한 RedisTemplate
	 */
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 * redis 분산 Lock을 위한 redisson 접근 객체
	 */
	@Autowired
	private RedissonClient redissonClient;
	
	/**
	 * 중복체크를 위한 redis 연결을 위한 RedisTemplate
	 */
//	@Autowired
//	private RedisTemplate<String, String> redisTemplateString;

	/**
	 * 시퀀스를 채번하여 리턴한다.
	 * <p>
	 * 1. 동시접근 제어를 위해 redis lock을 획득한다.(분산 락) 2. redis에 시퀀스 정보를 업데이트한다. 3. 날짜별,
	 * 최대시퀀스값 별 체크를 수행한다. 4. 채번된 시퀀스 번호를 리턴하고 락을 해제한다.
	 * <p>
	 *
	 * @return long 다음 시퀀스 번호
	 */
	public long next() throws IOException, ClassNotFoundException, InterruptedException {

		// 레디스 락 생성
		final RLock lock = redissonClient.getLock(lockName);
		String date;
		long currentSequence = 0L;

		if (lock.tryLock(10, 10, TimeUnit.SECONDS)) { // 10초 동안 분산락을 얻을 수 있도록 시도한다.
			try {
				//
				logger.info("tryLock thread---{}, lock:{}", Thread.currentThread().getId(), lock);

				ValueOperations<String, Object> vop = redisTemplate.opsForValue();
				SequenceStateDto value = (SequenceStateDto) vop.get(seqKey); // Redis에서 시퀀스 정보를 조회

				if (value != null) { // Redis에 시퀀스 정보가 저장되어 있으면
					date = value.getDate(); // 날짜 정보를 가져온다.
					currentSequence = value.getCurrentSequence(); // 시퀀스 번호를 가져온다.
				} else { // Redis에 시퀀스 정보가 저장되어 있지 않으면
					date = DateUtils.getCurrentDate(); // 현재 날짜를 가져온다.
					currentSequence = 0L; // 시퀀스 번호를 0으로 초기화한다.
				}
				String now = DateUtils.getCurrentDate();
				if (!date.equals(now)) { // 현재 날짜와 Redis에 저장된 날짜가 다르면
					date = now; // 날짜를 현재 날짜로 변경한다.
					currentSequence = 0L; // 시퀀스 번호를 0으로 초기화한다.
				}

				if (MAX_SEQUENCE_NUMBER == currentSequence) { // 시퀀스 번호가 최대값을 초과하면
					throw new RuntimeException("시퀀스 제한 수를 초과하였습니다."); // 오류를 발생시킨다.
				}

				currentSequence++; // 시퀀스 값을 증가시킨다.
				
				/** 중복 테스트 로직! */
				// redis를 이용하여 중복 테스트
//				ValueOperations<String, String> vopTest = redisTemplateString.opsForValue();
//				String seqString = Long.toString(currentSequence) ;
//			    String checkVal = vopTest.get(seqString);
//			    if(StringUtils.isEmpty(checkVal)) {
//			    	// 중복아님
//			    	vopTest.set(seqString, "dupCheck", 1, TimeUnit.SECONDS) ;
//			    } else {
//			    	throw new RuntimeException("seq 중복이 발생하였습니다. [" + seqString +"]") ;
//			    }
			    /** 중복 테스트 로직! */
				
				saveStateRedis(date, currentSequence); // 시퀀스 정보를 redis에 저장한다.

				logger.info("seq: {}", currentSequence);

				return currentSequence;
			} catch (Exception e) {
				logger.error("ERROR!!!", e);
				throw new RuntimeException("");
			} finally {
				//
				if (lock != null && lock.isLocked()) { // 락 정보가 있는경우
					lock.unlock(); // 락을 해제한다.
				}
			}
		} else { // lock을 획득하지 못하면 오류처리한다.
			throw new RuntimeException("getSequenceErrror(getting lock Error)");
		}
	}

	/**
	 * 현재 시퀀스정보를 조회하여 리턴한다.
	 * <p>
	 * redis에 저장되어있는 현재 시퀀스 정보를 조회한 후 리턴한다.
	 * <p>
	 *
	 * @return SequenceStateDto - 시퀀스 정보 객체
	 */
	public SequenceStateDto getCurrVal() {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto value = (SequenceStateDto) vop.get(seqKey);
		logger.info("현재 seq 번호: {}", value.getCurrentSequence());

		return value;
	}

	/**
	 * 시퀀스정보의 날짜정보를 이전일자로 저장한다.
	 * <p>
	 * 1. redis에 저장되어있는 현재 시퀀스 정보를 조회한 후 조회된 정보의 날짜를 이전날짜로 변경하여 저장한다.
	 * 2. 날짜가 변경되면 시퀀스가 다시 채번되는지 테스트를 위해 작성한 메소드이다.
	 * <p>
	 *
	 * @return void
	 */
	public void changePreDate() {

		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto value = (SequenceStateDto) vop.get(seqKey);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localdate = LocalDate.parse(value.getDate(), formatter);
		LocalDate modifiedDate = localdate.minusDays(1); // 1일 빼기
		String modifiedDateString = modifiedDate.format(formatter);

		try {
			saveStateRedis(modifiedDateString, value.getCurrentSequence());
		} catch (IOException e) {
			throw new RuntimeException("이전 날짜로 변경 중 오류가 발생하였습니다.");
		}
	}

	/**
	 * 시퀀스 정보를 초기화한다.
	 * <p>
	 * 1. redis에 시퀀스번호는 0, 날짜는 현재 날짜로 저장하여 초기화한다.
	 * <p>
	 *
	 * @return void
	 */
	public void setInit() {
		try {
			saveStateRedis(DateUtils.getCurrentDate(), 0L);
		} catch (IOException e) {
			throw new RuntimeException("초기화 중 오류가 발생하였습니다.");
		}
	}

	/**
	 * 시퀀스 정보를 최대값으로 변경하여 저장한다.
	 * <p>
	 * 1. redis에 시퀀스번호를 최대값-1로 변경하여 저장한다.
	 * 2. 시퀀스 조회 시 최대값에 도달하면 어떻게 처리되는지 테스트를 위해 작성.
	 * <p>
	 *
	 * @return void
	 */
	public void changeMaxSeq() {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto value = (SequenceStateDto) vop.get(seqKey);

		try {
			saveStateRedis(value.getDate(), MAX_SEQUENCE_NUMBER - 1);
		} catch (IOException e) {
			throw new RuntimeException("최대 시퀀스 값 변경 중 오류가 발생하였습니다.");
		}
	}

	/**
	 * 시퀀스 정보를 저장한다.
	 * <p>
	 * 날짜와 시퀀스번호를 받아 redis에 저장한다.
	 * <p>
	 *
	 * @param String dateString : 문자열 날짜(yyyyMMdd)
	 * @param long currSeq : 시퀀스 번호
	 * @return void
	 */
	private void saveStateRedis(String dateString, long currSeq) throws IOException {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto dto = new SequenceStateDto(dateString, currSeq);
		vop.set(seqKey, dto);
	}

}
