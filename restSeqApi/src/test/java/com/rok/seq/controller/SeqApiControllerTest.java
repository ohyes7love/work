package com.rok.seq.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.rok.seq.service.GenGuidService;
import com.rok.seq.service.GenSeqService;
import com.rok.seq.service.dto.SequenceStateDto;

@WebMvcTest({SeqApiController.class, GlobalExceptionHandler.class})
class SeqApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenGuidService guidService;

    @MockBean
    private GenSeqService seqService;

    // ── /seqApi/getGuid ──────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 요청으로 GUID 채번 시 200과 30자리 guid를 반환한다")
    void getGuid_validRequest_returns200() throws Exception {
        when(guidService.generateGuid(any())).thenReturn("20260309160000000ABC010100abcd");

        mockMvc.perform(post("/seqApi/getGuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sendChlCd\":\"ABC\",\"sendSysNodeNo\":1,\"sendSysInstNo\":1}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.guid").value("20260309160000000ABC010100abcd"));
    }

    @Test
    @DisplayName("sendChlCd 누락 시 400을 반환한다")
    void getGuid_missingSendChlCd_returns400() throws Exception {
        mockMvc.perform(post("/seqApi/getGuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sendSysNodeNo\":1,\"sendSysInstNo\":1}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("sendSysNodeNo 누락 시 400을 반환한다")
    void getGuid_missingSendSysNodeNo_returns400() throws Exception {
        mockMvc.perform(post("/seqApi/getGuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sendChlCd\":\"ABC\",\"sendSysInstNo\":1}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("sendSysInstNo 누락 시 400을 반환한다")
    void getGuid_missingSendSysInstNo_returns400() throws Exception {
        mockMvc.perform(post("/seqApi/getGuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sendChlCd\":\"ABC\",\"sendSysNodeNo\":1}"))
            .andExpect(status().isBadRequest());
    }

    // ── /seqApi/getSeq ───────────────────────────────────────────────────────

    @Test
    @DisplayName("시퀀스 채번 시 200과 sequence를 반환한다")
    void getSeq_returns200WithSequence() throws Exception {
        when(seqService.next()).thenReturn(1L);

        mockMvc.perform(get("/seqApi/getSeq"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sequence").value(1));
    }

    @Test
    @DisplayName("시퀀스 서비스 오류 시 500을 반환한다")
    void getSeq_serviceThrows_returns500() throws Exception {
        when(seqService.next()).thenThrow(new RuntimeException("시퀀스 생성 오류"));

        mockMvc.perform(get("/seqApi/getSeq"))
            .andExpect(status().isInternalServerError());
    }

    // ── /seqApi/getCurrentSeq ────────────────────────────────────────────────

    @Test
    @DisplayName("현재 시퀀스 조회 시 200과 sequence/date를 반환한다")
    void getCurrentSeq_returns200WithSequenceAndDate() throws Exception {
        when(seqService.getCurrVal()).thenReturn(new SequenceStateDto("20260309", 42L));

        mockMvc.perform(get("/seqApi/getCurrentSeq"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sequence").value(42))
            .andExpect(jsonPath("$.date").value("20260309"));
    }

    @Test
    @DisplayName("현재 시퀀스 서비스 오류 시 500을 반환한다")
    void getCurrentSeq_serviceThrows_returns500() throws Exception {
        when(seqService.getCurrVal()).thenThrow(new RuntimeException("시퀀스 조회 오류"));

        mockMvc.perform(get("/seqApi/getCurrentSeq"))
            .andExpect(status().isInternalServerError());
    }
}
