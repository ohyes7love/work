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

@Service
public class GenSeqService {

	Logger logger = LoggerFactory.getLogger(getClass());

	private String date = DateUtils.getCurrentDate();
	private long currentSequence = 0L;
	private static final long MAX_SEQUENCE_NUMBER = 9999999999L;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedissonClient redissonClient;

	public long next(boolean isDayTest, boolean isMaxSeqTest)
			throws IOException, ClassNotFoundException, InterruptedException {

		final String lockName = "seqLock";
		final RLock lock = redissonClient.getLock(lockName);

		// 락점유시도시간, 락사용대기시간, 시간포맷
		if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
			try {
				//
				logger.info("tryLock thread---{}, lock:{}", Thread.currentThread().getId(), lock);

				ValueOperations<String, Object> vop = redisTemplate.opsForValue();
				SequenceStateDto value = (SequenceStateDto) vop.get("seq");

				if (value != null) {
//	    				logger.info("redisVal: {}", value);
					if (isDayTest) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
						LocalDate localdate = LocalDate.parse(value.getDate(), formatter);
						LocalDate modifiedDate = localdate.minusDays(1); // 1일 빼기
						String modifiedDateString = modifiedDate.format(formatter);
						date = modifiedDateString;
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
			} catch (Exception e) {
				logger.error("ERROR!!!", e);
				throw new RuntimeException("");
			} finally {
				//
				if (lock != null && lock.isLocked()) {
					lock.unlock();
				}
			}
		} else {
			throw new RuntimeException("getSequenceErrror(getting lock Error)");
		}
	}
	
	public long getCurrVal()  {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto value = (SequenceStateDto) vop.get("seq");
		return value.getCurrentSequence() ;
	}

	private void saveStateRedis() throws IOException {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		SequenceStateDto dto = new SequenceStateDto(date, currentSequence);
		vop.set("seq", dto);
	}

}
