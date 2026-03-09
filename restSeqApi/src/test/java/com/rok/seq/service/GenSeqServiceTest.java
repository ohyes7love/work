package com.rok.seq.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.rok.seq.constant.SeqApiConstant;
import com.rok.seq.service.dto.SequenceStateDto;
import com.rok.seq.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenSeqServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private RLock rLock;

    @InjectMocks
    private GenSeqService genSeqService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redissonClient.getLock(SeqApiConstant.LOCK_KEY)).thenReturn(rLock);
    }

    @Test
    @DisplayName("Redis에 데이터 없으면 첫 시퀀스는 1이어야 한다")
    void next_noExistingData_returns1() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isLocked()).thenReturn(true);
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(null);

        long seq = genSeqService.next();

        assertThat(seq).isEqualTo(1L);
    }

    @Test
    @DisplayName("기존 시퀀스가 있으면 1 증가해야 한다")
    void next_existingData_incrementsBy1() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isLocked()).thenReturn(true);
        String today = DateUtils.getCurrentDate();
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(new SequenceStateDto(today, 5L));

        long seq = genSeqService.next();

        assertThat(seq).isEqualTo(6L);
    }

    @Test
    @DisplayName("날짜가 바뀌면 시퀀스가 1로 리셋되어야 한다")
    void next_dateChanged_resetsTo1() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isLocked()).thenReturn(true);
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(new SequenceStateDto("19991231", 9999L));

        long seq = genSeqService.next();

        assertThat(seq).isEqualTo(1L);
    }

    @Test
    @DisplayName("시퀀스가 최대값이면 예외가 발생해야 한다")
    void next_maxSequenceReached_throwsException() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isLocked()).thenReturn(true);
        String today = DateUtils.getCurrentDate();
        when(valueOperations.get(SeqApiConstant.SEQ_KEY))
            .thenReturn(new SequenceStateDto(today, SeqApiConstant.MAX_SEQUENCE_NUMBER));

        assertThatThrownBy(() -> genSeqService.next())
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("락 획득 실패 시 예외가 발생해야 한다")
    void next_lockNotAcquired_throwsException() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(false);

        assertThatThrownBy(() -> genSeqService.next())
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("next() 호출 후 Redis에 시퀀스 상태가 저장되어야 한다")
    void next_savesStateToRedis() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isLocked()).thenReturn(true);
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(null);

        genSeqService.next();

        verify(valueOperations).set(eq(SeqApiConstant.SEQ_KEY), any(SequenceStateDto.class));
    }

    @Test
    @DisplayName("getCurrVal은 Redis에서 현재 시퀀스 상태를 반환해야 한다")
    void getCurrVal_returnsCurrentState() {
        String today = DateUtils.getCurrentDate();
        SequenceStateDto expected = new SequenceStateDto(today, 42L);
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(expected);

        SequenceStateDto result = genSeqService.getCurrVal();

        assertThat(result.getCurrentSequence()).isEqualTo(42L);
        assertThat(result.getDate()).isEqualTo(today);
    }

    @Test
    @DisplayName("changePreDate는 날짜를 하루 이전으로 변경해야 한다")
    void changePreDate_subtractsOneDay() {
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(new SequenceStateDto("20260309", 10L));

        genSeqService.changePreDate();

        verify(valueOperations).set(eq(SeqApiConstant.SEQ_KEY),
            org.mockito.ArgumentMatchers.argThat(obj -> {
                SequenceStateDto dto = (SequenceStateDto) obj;
                return "20260308".equals(dto.getDate()) && dto.getCurrentSequence() == 10L;
            }));
    }

    @Test
    @DisplayName("setInit은 시퀀스를 0으로 초기화해야 한다")
    void setInit_resetsSequenceTo0() {
        genSeqService.setInit();

        verify(valueOperations).set(eq(SeqApiConstant.SEQ_KEY),
            org.mockito.ArgumentMatchers.argThat(obj -> {
                SequenceStateDto dto = (SequenceStateDto) obj;
                return dto.getCurrentSequence() == 0L;
            }));
    }

    @Test
    @DisplayName("changeMaxSeq는 시퀀스를 MAX-1로 변경해야 한다")
    void changeMaxSeq_setsMaxMinus1() {
        String today = DateUtils.getCurrentDate();
        when(valueOperations.get(SeqApiConstant.SEQ_KEY)).thenReturn(new SequenceStateDto(today, 1L));

        genSeqService.changeMaxSeq();

        verify(valueOperations).set(eq(SeqApiConstant.SEQ_KEY),
            org.mockito.ArgumentMatchers.argThat(obj -> {
                SequenceStateDto dto = (SequenceStateDto) obj;
                return dto.getCurrentSequence() == SeqApiConstant.MAX_SEQUENCE_NUMBER - 1;
            }));
    }

    @Test
    @DisplayName("락 획득 실패 시 Redis에 저장되지 않아야 한다")
    void next_lockFailed_doesNotSaveToRedis() throws Exception {
        when(rLock.tryLock(10, 10, TimeUnit.SECONDS)).thenReturn(false);

        assertThatThrownBy(() -> genSeqService.next()).isInstanceOf(RuntimeException.class);

        verify(valueOperations, never()).set(any(), any());
    }
}
