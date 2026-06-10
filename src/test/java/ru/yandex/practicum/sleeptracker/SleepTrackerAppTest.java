package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private final CountSessionsFunction function = new CountSessionsFunction();

    @Test
    void shouldReturnZeroForEmptyList() {
        // given
        List<SleepingSession> emptyList = List.of();

        // when
        SleepAnalysisResult result = function.apply(emptyList);

        // then
        assertEquals(0, result.getValue());
        assertEquals("Общее количество сессий сна", result.getDescription());
    }

    @Test
    void shouldReturnCorrectCountForThreeSessions() {
        // given
        LocalDateTime now = LocalDateTime.now();
        SleepingSession session1 = new SleepingSession(now, now.plusHours(8), SleepQuality.GOOD);
        SleepingSession session2 = new SleepingSession(now, now.plusHours(7), SleepQuality.NORMAL);
        SleepingSession session3 = new SleepingSession(now, now.plusHours(6), SleepQuality.BAD);
        List<SleepingSession> sessions = List.of(session1, session2, session3);

        // when
        SleepAnalysisResult result = function.apply(sessions);

        // then
        assertEquals(3, result.getValue());
    }

}