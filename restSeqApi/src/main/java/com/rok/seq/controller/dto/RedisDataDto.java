package com.rok.seq.controller.dto;

/**
 * Redis 전체 데이터 조회 응답 DTO
 *
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class RedisDataDto {
    private String key;
    private String type;
    private String value;
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
