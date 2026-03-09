package com.rok.seq.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rok.seq.controller.dto.GuidInDto;

class GenGuidServiceTest {

    private GenGuidService guidService;

    @BeforeEach
    void setUp() {
        guidService = new GenGuidService();
    }

    @Test
    @DisplayName("GUID는 30자리여야 한다")
    void generateGuid_returns30Chars() {
        GuidInDto in = buildGuidInDto("ABC", 1, 1);

        String guid = guidService.generateGuid(in);

        assertThat(guid).hasSize(30);
    }

    @Test
    @DisplayName("GUID 17~19번째 자리는 채널코드여야 한다")
    void generateGuid_channelCodeAtCorrectPosition() {
        GuidInDto in = buildGuidInDto("SYS", 1, 1);

        String guid = guidService.generateGuid(in);

        assertThat(guid.substring(17, 20)).isEqualTo("SYS");
    }

    @Test
    @DisplayName("GUID 20~21번째 자리는 노드번호(2자리 제로패딩)여야 한다")
    void generateGuid_nodeNumberAtCorrectPosition() {
        GuidInDto in = buildGuidInDto("ABC", 5, 1);

        String guid = guidService.generateGuid(in);

        assertThat(guid.substring(20, 22)).isEqualTo("05");
    }

    @Test
    @DisplayName("GUID 22~23번째 자리는 인스턴스번호(2자리 제로패딩)여야 한다")
    void generateGuid_instanceNumberAtCorrectPosition() {
        GuidInDto in = buildGuidInDto("ABC", 1, 3);

        String guid = guidService.generateGuid(in);

        assertThat(guid.substring(22, 24)).isEqualTo("03");
    }

    @Test
    @DisplayName("GUID 마지막 6자리는 영문/숫자로 구성된 랜덤 문자열이어야 한다")
    void generateGuid_randomPartIsAlphanumeric() {
        GuidInDto in = buildGuidInDto("ABC", 1, 1);

        String guid = guidService.generateGuid(in);
        String randomPart = guid.substring(24);

        assertThat(randomPart).hasSize(6);
        assertThat(randomPart).matches("[A-Za-z0-9]+");
    }

    @Test
    @DisplayName("동시 호출 시 GUID가 중복되지 않아야 한다")
    void generateGuid_uniquenessUnderConcurrentCalls() throws InterruptedException {
        GuidInDto in = buildGuidInDto("ABC", 1, 1);
        int callCount = 100;
        String[] guids = new String[callCount];

        Thread[] threads = new Thread[callCount];
        for (int i = 0; i < callCount; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> guids[idx] = guidService.generateGuid(in));
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        long uniqueCount = java.util.Arrays.stream(guids).distinct().count();
        assertThat(uniqueCount).isEqualTo(callCount);
    }

    private GuidInDto buildGuidInDto(String channelCode, int nodeNo, int instanceNo) {
        GuidInDto in = new GuidInDto();
        in.setSendChlCd(channelCode);
        in.setSendSysNodeNo(nodeNo);
        in.setSendSysInstNo(instanceNo);
        return in;
    }
}
