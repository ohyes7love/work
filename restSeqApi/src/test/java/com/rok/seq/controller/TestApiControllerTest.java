package com.rok.seq.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rok.seq.service.GenSeqService;
import com.rok.seq.service.dto.SequenceStateDto;

@WebMvcTest({TestApiController.class, GlobalExceptionHandler.class})
class TestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenSeqService seqService;

    // ── /testApi/setPreDate ──────────────────────────────────────────────────

    @Test
    @DisplayName("날짜를 전날로 변경 후 현재 시퀀스 상태를 반환한다")
    void setPreDate_returns200WithCurrentState() throws Exception {
        doNothing().when(seqService).changePreDate();
        when(seqService.getCurrVal()).thenReturn(new SequenceStateDto("20260308", 10L));

        mockMvc.perform(get("/testApi/setPreDate"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sequence").value(10))
            .andExpect(jsonPath("$.date").value("20260308"));
    }

    @Test
    @DisplayName("날짜 변경 중 오류 시 500을 반환한다")
    void setPreDate_serviceThrows_returns500() throws Exception {
        doThrow(new RuntimeException("이전 날짜 변경 오류")).when(seqService).changePreDate();

        mockMvc.perform(get("/testApi/setPreDate"))
            .andExpect(status().isInternalServerError());
    }

    // ── /testApi/setMaxSeq ───────────────────────────────────────────────────

    @Test
    @DisplayName("시퀀스를 최대값-1로 변경 후 현재 시퀀스 상태를 반환한다")
    void setMaxSeq_returns200WithCurrentState() throws Exception {
        doNothing().when(seqService).changeMaxSeq();
        when(seqService.getCurrVal()).thenReturn(new SequenceStateDto("20260309", 9999999998L));

        mockMvc.perform(get("/testApi/setMaxSeq"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sequence").value(9999999998L))
            .andExpect(jsonPath("$.date").value("20260309"));
    }

    @Test
    @DisplayName("최대값 변경 중 오류 시 500을 반환한다")
    void setMaxSeq_serviceThrows_returns500() throws Exception {
        doThrow(new RuntimeException("최대값 변경 오류")).when(seqService).changeMaxSeq();

        mockMvc.perform(get("/testApi/setMaxSeq"))
            .andExpect(status().isInternalServerError());
    }

    // ── /testApi/setInit ─────────────────────────────────────────────────────

    @Test
    @DisplayName("시퀀스 초기화 후 현재 시퀀스 상태(0)를 반환한다")
    void setInit_returns200WithZeroSequence() throws Exception {
        doNothing().when(seqService).setInit();
        when(seqService.getCurrVal()).thenReturn(new SequenceStateDto("20260309", 0L));

        mockMvc.perform(get("/testApi/setInit"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sequence").value(0))
            .andExpect(jsonPath("$.date").value("20260309"));
    }

    @Test
    @DisplayName("초기화 중 오류 시 500을 반환한다")
    void setInit_serviceThrows_returns500() throws Exception {
        doThrow(new RuntimeException("초기화 오류")).when(seqService).setInit();

        mockMvc.perform(get("/testApi/setInit"))
            .andExpect(status().isInternalServerError());
    }
}
