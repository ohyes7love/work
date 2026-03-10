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
 * Redis 관리자 컨트롤러.
 *
 * <p>Redis에 저장된 모든 키의 데이터(타입·값·TTL)를 한눈에 확인할 수 있는
 * 관리자용 조회 API를 제공한다. 운영·디버깅 시 Redis 상태를 빠르게 파악하기 위한 용도이다.</p>
 *
 * <ul>
 *   <li>GET /redisAdmin/getAllKeys — 전체 키 조회</li>
 * </ul>
 *
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")   // 로컬 개발 편의를 위해 CORS 전체 허용
@RequestMapping("/redisAdmin")
public class RedisAdminController {

    /** 클래스 로거 */
    Logger logger = LoggerFactory.getLogger(getClass());

    /** 문자열 직렬화 기반 RedisTemplate — 키·값을 String으로 읽어오기 위해 사용 */
    @Autowired
    private RedisTemplate<String, String> redisTemplateString;

    /**
     * Redis에 저장된 <b>모든 키</b>와 해당 값을 조회한다.
     *
     * <p>키 목록을 가져온 뒤, 각 키의 데이터 타입(STRING, LIST, SET, ZSET, HASH)에 따라
     * 적절한 Redis 명령어로 값을 읽어온다. TTL 정보도 함께 포함하여 반환한다.</p>
     *
     * <p><b>주의:</b> keys("*") 명령은 키가 매우 많은 운영 환경에서는 성능 이슈가 발생할 수 있다.
     * 개발·테스트 환경 또는 키 수가 적은 환경에서 사용하는 것을 권장한다.</p>
     *
     * @return 키 이름 오름차순으로 정렬된 {@link RedisDataDto} 리스트.
     *         키가 없을 경우 빈 리스트를 반환한다.
     */
    @GetMapping("/getAllKeys")
    public List<RedisDataDto> getAllKeys() {

        // 1. Redis에 존재하는 모든 키 조회 (패턴: "*")
        Set<String> keys = redisTemplateString.keys("*");
        List<RedisDataDto> result = new ArrayList<>();

        // 키가 없으면 빈 리스트 반환
        if (keys == null || keys.isEmpty()) {
            return result;
        }

        // 2. 각 키에 대해 타입·값·TTL 정보를 수집
        for (String key : keys) {
            RedisDataDto dto = new RedisDataDto();
            dto.setKey(key);

            // 키의 데이터 타입 조회 (STRING, LIST, SET, ZSET, HASH 등)
            DataType type = redisTemplateString.type(key);
            dto.setType(type != null ? type.name() : "NONE");

            // TTL 조회 (-1: 만료 없음, -2: 키 없음, 양수: 남은 초)
            Long ttl = redisTemplateString.getExpire(key);
            dto.setTtl(ttl);

            // 3. 데이터 타입별로 적절한 명령어를 사용해 값 조회
            try {
                if (DataType.STRING.equals(type)) {
                    // GET 명령 — 단일 문자열 값
                    dto.setValue(Objects.toString(redisTemplateString.opsForValue().get(key), "(null)"));
                } else if (DataType.LIST.equals(type)) {
                    // LRANGE 0 -1 — 리스트 전체 요소
                    dto.setValue(Objects.toString(redisTemplateString.opsForList().range(key, 0, -1), "(null)"));
                } else if (DataType.SET.equals(type)) {
                    // SMEMBERS — Set 전체 멤버
                    dto.setValue(Objects.toString(redisTemplateString.opsForSet().members(key), "(null)"));
                } else if (DataType.ZSET.equals(type)) {
                    // ZRANGE WITHSCORES — Sorted Set 전체 (점수 포함)
                    dto.setValue(Objects.toString(redisTemplateString.opsForZSet().rangeWithScores(key, 0, -1), "(null)"));
                } else if (DataType.HASH.equals(type)) {
                    // HGETALL — Hash 전체 필드-값 쌍
                    dto.setValue(Objects.toString(redisTemplateString.opsForHash().entries(key), "(null)"));
                } else {
                    // 알 수 없는 타입 (STREAM 등)
                    dto.setValue("(type: " + (type != null ? type.code() : "none") + ")");
                }
            } catch (Exception e) {
                // 개별 키 조회 실패 시 에러 메시지를 값에 담고 로깅 (전체 조회는 계속 진행)
                logger.warn("키 [{}] 값 조회 중 오류: {}", key, e.getMessage());
                dto.setValue("(조회 오류: " + e.getMessage() + ")");
            }

            result.add(dto);
        }

        // 4. 키 이름 기준 오름차순 정렬 후 반환
        result.sort(Comparator.comparing(RedisDataDto::getKey));
        logger.info("Redis 전체 조회 - 총 {} 건", result.size());
        return result;
    }
}
