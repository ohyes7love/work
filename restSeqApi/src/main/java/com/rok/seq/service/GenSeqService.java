package com.rok.seq.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.rok.seq.redis.RedisLockRepository;
import com.rok.seq.service.dto.SequenceStateDto;
import com.rok.seq.utils.DateUtils;

@Service
public class GenSeqService {

	Logger logger = LoggerFactory.getLogger(getClass());

	private String date = DateUtils.getCurrentDate();
	private long currentSequence = 0L;
	private static final long MAX_SEQUENCE_NUMBER = 9999999999L;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisLockRepository redisLockRepository;

	public synchronized long next(boolean isDayTest, boolean isMaxSeqTest) throws IOException, ClassNotFoundException, InterruptedException {
		
		// TEST
		while (!redisLockRepository.lock("1")) {
			logger.info("lock 획득 실패! 100ms 대기");
	        Thread.sleep(100);
	    } // 락을 획득하기 위해 대기
		
	    try {
	    	ValueOperations<String, Object> vop = redisTemplate.opsForValue();
			SequenceStateDto value = (SequenceStateDto) vop.get("seq");

			if (value != null) {
				logger.info("redisVal: {}", value);
				if (isDayTest) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			        LocalDate localdate = LocalDate.parse(value.getDate(), formatter);
			        LocalDate modifiedDate = localdate.minusDays(1); // 1일 빼기
			        String modifiedDateString = modifiedDate.format(formatter);
					date = modifiedDateString ;
				} else {
					date = value.getDate();
				}
				currentSequence = value.getCurrentSequence();
			} else {
				date = DateUtils.getCurrentDate();
				currentSequence = 0L;
			}

			String now = DateUtils.getCurrentDate();
			if (!date.equals(now)) {
				date = now;
				currentSequence = 0L;
			}
			
			if (isMaxSeqTest) {
				currentSequence = MAX_SEQUENCE_NUMBER - 1;
			}
			if (MAX_SEQUENCE_NUMBER == currentSequence) {
				throw new RuntimeException("시퀀스 제한 수를 초과하였습니다.");
			}
			
			currentSequence++;
			saveStateRedis();

			logger.info("seq: {}", currentSequence);

			return currentSequence;
	    } finally {
	    	logger.info("lock 해제");
	        redisLockRepository.unlock("1");
	        // 락 해제
	    }
	}

	public synchronized long current() throws FileNotFoundException, IOException, ClassNotFoundException {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto value = (SequenceStateDto) vop.get("seq");

		if (value != null) {
			return value.getCurrentSequence();
		} else {
			return 0;
		}

	}

	private void saveStateRedis() throws IOException {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto dto = new SequenceStateDto(date, currentSequence);
		vop.set("seq", dto);
	}

}
