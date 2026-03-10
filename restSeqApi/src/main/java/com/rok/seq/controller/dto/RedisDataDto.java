package com.rok.seq.controller.dto;

/**
 * Redis 전체 데이터 조회 응답 DTO.
 *
 * <p>{@code /redisAdmin/getAllKeys} API의 응답 항목으로 사용되며,
 * Redis에 저장된 개별 키에 대한 상세 정보를 담는다.</p>
 *
 * <table>
 *   <tr><th>필드</th><th>설명</th></tr>
 *   <tr><td>key</td><td>Redis 키 이름</td></tr>
 *   <tr><td>type</td><td>데이터 타입 (STRING, LIST, SET, ZSET, HASH 등)</td></tr>
 *   <tr><td>value</td><td>키에 저장된 값 (문자열 변환)</td></tr>
 *   <tr><td>ttl</td><td>남은 만료 시간(초). -1이면 만료 없음(영구)</td></tr>
 * </table>
 *
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class RedisDataDto {

    /** Redis 키 이름 */
    private String key;

    /** 데이터 타입 (STRING, LIST, SET, ZSET, HASH 등) */
    private String type;

    /** 키에 저장된 값 — 타입에 따라 문자열로 변환된 결과 */
    private String value;

    /** TTL(Time To Live). 남은 만료 시간(초). -1이면 만료 없음(영구 보관) */
    private Long ttl;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Long getTtl() { return ttl; }
    public void setTtl(Long ttl) { this.ttl = ttl; }
}
