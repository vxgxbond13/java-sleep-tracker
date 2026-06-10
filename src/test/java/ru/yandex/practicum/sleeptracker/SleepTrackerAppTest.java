package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private final CountSessionsFunction countFunction = new CountSessionsFunction();
    private final BadQualitySessionsCountFunction badFunction = new BadQualitySessionsCountFunction();
    private final MinSessionDurationFunction minFunction = new MinSessionDurationFunction();
    private final MaxSessionDurationFunction maxFunction = new MaxSessionDurationFunction();
    private final AvgSessionDurationFunction avgFunction = new AvgSessionDurationFunction();

    // Вспомогательный метод для создания сессии
    private SleepingSession session(long minutes, SleepQuality quality) {
        LocalDateTime now = LocalDateTime.now();
        return new SleepingSession(now, now.plusMinutes(minutes), quality);
    }

    // Вспомогательный метод для пустого списка
    private void assertEmptyListResult(SleepAnalysisFunction function, String expectedDescription) {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals(0, result.getValue());
        assertEquals(expectedDescription, result.getDescription());
    }

    // ========== CountSessionsFunction ==========
    @Test
    void countSessions_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(countFunction, "Общее количество сессий сна");
    }

    @Test
    void countSessions_shouldReturnCorrectCount() {
        List<SleepingSession> sessions = List.of(
                session(480, SleepQuality.GOOD),
                session(420, SleepQuality.NORMAL),
                session(360, SleepQuality.BAD)
        );
        assertEquals(3, countFunction.apply(sessions).getValue());
    }

    // ========== BadQualitySessionsCountFunction ==========
    @Test
    void badSessions_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(badFunction, "Количество сессий с плохим качеством сна");
    }

    @Test
    void badSessions_shouldCountCorrectly() {
        List<SleepingSession> sessions = List.of(
                session(480, SleepQuality.GOOD),
                session(420, SleepQuality.BAD),
                session(360, SleepQuality.BAD),
                session(300, SleepQuality.NORMAL)
        );
        assertEquals(2, badFunction.apply(sessions).getValue());
    }

    // ========== MinSessionDurationFunction ==========
    @Test
    void minDuration_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(minFunction, "Минимальная продолжительность сна (минуты)");
    }

    @Test
    void minDuration_shouldFindMinimum() {
        List<SleepingSession> sessions = List.of(
                session(540, SleepQuality.BAD),
                session(30, SleepQuality.NORMAL),
                session(420, SleepQuality.GOOD)
        );
        assertEquals(30, minFunction.apply(sessions).getValue());
    }

    // ========== MaxSessionDurationFunction ==========
    @Test
    void maxDuration_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(maxFunction, "Максимальная продолжительность сна (минуты)");
    }

    @Test
    void maxDuration_shouldFindMaximum() {
        List<SleepingSession> sessions = List.of(
                session(30, SleepQuality.NORMAL),
                session(540, SleepQuality.BAD),
                session(420, SleepQuality.GOOD)
        );
        assertEquals(540, maxFunction.apply(sessions).getValue());
    }

    // ========== AvgSessionDurationFunction ==========
    @Test
    void avgDuration_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(avgFunction, "Средняя продолжительность сна (минуты)");
    }

    @Test
    void avgDuration_shouldCalculateCorrectly() {
        List<SleepingSession> sessions = List.of(
                session(480, SleepQuality.GOOD),
                session(360, SleepQuality.NORMAL),
                session(300, SleepQuality.BAD)
        );
        assertEquals(380, avgFunction.apply(sessions).getValue());
    }

    @Test
    void avgDuration_shouldTruncateFraction() {
        List<SleepingSession> sessions = List.of(
                session(60, SleepQuality.GOOD),   // 1 час
                session(360, SleepQuality.NORMAL), // 6 часов
                session(301, SleepQuality.BAD)     // 5 часов 1 минута
        );
        // (60 + 360 + 301) / 3 = 721 / 3 = 240.333 → 240
        assertEquals(240, avgFunction.apply(sessions).getValue());
    }
}