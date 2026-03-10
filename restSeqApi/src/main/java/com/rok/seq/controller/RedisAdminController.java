package com.rok.seq.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.RedisDataDto;

/**
 * Redis 전체 데이터 조회를 위한 관리자 컨트롤러
 *
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/redisAdmin")
public class RedisAdminController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplateString;

    /**
     * Redis에 저장된 모든 키와 값을 조회한다.
     *
     * @return List<RedisDataDto> key, type, value, ttl 목록
     */
    @GetMapping("/getAllKeys")
    public List<RedisDataDto> getAllKeys() {

        Set<String> keys = redisTemplateString.keys("*");
        List<RedisDataDto> result = new ArrayList<>();

        if (keys == null || keys.isEmpty()) {
            return result;
        }

        for (String key : keys) {
            RedisDataDto dto = new RedisDataDto();
            dto.setKey(key);

            DataType type = redisTemplateString.type(key);
            dto.setType(type != null ? type.name() : "NONE");

            Long ttl = redisTemplateString.getExpire(key);
            dto.setTtl(ttl);

            try {
                if (DataType.STRING.equals(type)) {
                    dto.setValue(Objects.toString(redisTemplateString.opsForValue().get(key), "(null)"));
                } else if (DataType.LIST.equals(type)) {
                    dto.setValue(Objects.toString(redisTemplateString.opsForList().range(key, 0, -1), "(null)"));
                } else if (DataType.SET.equals(type)) {
                    dto.setValue(Objects.toString(redisTemplateString.opsForSet().members(key), "(null)"));
                } else if (DataType.ZSET.equals(type)) {
                    dto.setValue(Objects.toString(redisTemplateString.opsForZSet().rangeWithScores(key, 0, -1), "(null)"));
                } else if (DataType.HASH.equals(type)) {
                    dto.setValue(Objects.toString(redisTemplateString.opsForHash().entries(key), "(null)"));
                } else {
                    dto.setValue("(type: " + (type != null ? type.code() : "none") + ")");
                }
            } catch (Exception e) {
                logger.warn("키 [{}] 값 조회 중 오류: {}", key, e.getMessage());
                dto.setValue("(조회 오류: " + e.getMessage() + ")");
            }

            result.add(dto);
        }

        result.sort(Comparator.comparing(RedisDataDto::getKey));
        logger.info("Redis 전체 조회 - 총 {} 건", result.size());
        return result;
    }
}
