package com.rok.seq.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository {
	private RedisTemplate<String, String> redisTemplateString;

	public RedisLockRepository(final RedisTemplate<String, String> redisTemplateString) {
		this.redisTemplateString = redisTemplateString;
	}

	public Boolean lock(final String key) {
		return redisTemplateString.opsForValue().setIfAbsent(key, "lock", Duration.ofMillis(3_000));
	}

	public Boolean unlock(final String key) {
		return redisTemplateString.delete(key);
	}

}
