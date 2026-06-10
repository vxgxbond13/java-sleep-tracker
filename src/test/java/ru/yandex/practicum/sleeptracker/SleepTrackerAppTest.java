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
    private final SleeplessNightsFunction sleeplessNightsFunction = new SleeplessNightsFunction();
    private final ChronotypeFunction function = new ChronotypeFunction();

    // Вспомогательный метод для создания сессии
    private SleepingSession session(long minutes, SleepQuality quality) {
        LocalDateTime now = LocalDateTime.now();
        return new SleepingSession(now, now.plusMinutes(minutes), quality);
    }


    // Вспомогательный метод для создания сессии
    private SleepingSession session(int year, int month, int day, int startHour, int startMinute,
                                    int endHour, int endMinute, SleepQuality quality) {
        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(year, month, day, endHour, endMinute);
        // Если время окончания меньше времени начала - значит, переход на следующий день
        if (end.isBefore(start)) {
            end = end.plusDays(1);
        }
        return new SleepingSession(start, end, quality);
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

    // ========== SleeplessNightsFunction ==========

    @Test
    void sleeplessNights_shouldReturnZeroForEmptyList() {
        assertEmptyListResult(sleeplessNightsFunction, "Количество бессонных ночей");
    }

    @Test
    void sleeplessNights_shouldReturnOneWhenFirstNightIsSleepless() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 7, 0, SleepQuality.GOOD),
                session(2025, 10, 2, 23, 0, 7, 0, SleepQuality.GOOD),
                session(2025, 10, 3, 23, 0, 7, 0, SleepQuality.GOOD)
        );

        SleepAnalysisResult result = sleeplessNightsFunction.apply(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    void sleeplessNights_shouldCountOneSleeplessNightInMiddle() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 0, 7, 0, SleepQuality.GOOD),   // ночь 1-2: спит
                // ночь 2-3: БЕССОННАЯ
                session(2025, 10, 3, 1, 0, 8, 0, SleepQuality.NORMAL)     // ночь 3-4: спит
        );

        SleepAnalysisResult result = sleeplessNightsFunction.apply(sessions);
        assertEquals(1, result.getValue());
    }

    // ========== ChronotypeFunction ==========

    @Test
    void shouldReturnDoveForEmptyList() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals(0, result.getValue()); // 0 = Голубь
    }

    @Test
    void shouldReturnOwlWhenOnlyOwls() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 30, 9, 30, SleepQuality.GOOD),
                session(2025, 10, 2, 23, 45, 9, 15, SleepQuality.GOOD),
                session(2025, 10, 3, 23, 15, 10, 0, SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1, result.getValue()); // 1 = Сова
    }

    @Test
    void shouldReturnLarkWhenOnlyLarks() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 21, 30, 6, 30, SleepQuality.GOOD),
                session(2025, 10, 2, 21, 45, 6, 45, SleepQuality.GOOD),
                session(2025, 10, 3, 21, 15, 6, 15, SleepQuality.GOOD)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(2, result.getValue()); // 2 = Жаворонок
    }

    @Test
    void shouldReturnOwlWhenOwlsMajority() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 30, 9, 30, SleepQuality.GOOD), // сова
                session(2025, 10, 2, 23, 45, 9, 15, SleepQuality.GOOD), // сова
                session(2025, 10, 3, 21, 30, 6, 30, SleepQuality.GOOD)  // жаворонок
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1, result.getValue()); // Сова
    }

    @Test
    void shouldReturnDoveWhenTie() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 23, 30, 9, 30, SleepQuality.GOOD), // сова
                session(2025, 10, 2, 23, 45, 9, 15, SleepQuality.GOOD), // сова
                session(2025, 10, 3, 21, 30, 6, 30, SleepQuality.GOOD), // жаворонок
                session(2025, 10, 4, 21, 15, 6, 45, SleepQuality.GOOD), // жаворонок
                session(2025, 10, 5, 22, 30, 8, 0, SleepQuality.GOOD)    // голубь
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0, result.getValue()); // Голубь (при равенстве)
    }

    @Test
    void shouldIgnoreDaySession() {
        List<SleepingSession> sessions = List.of(
                session(2025, 10, 1, 14, 0, 15, 0, SleepQuality.NORMAL), // дневная
                session(2025, 10, 1, 23, 30, 9, 30, SleepQuality.GOOD)     // ночная
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1, result.getValue()); // Только сова
    }
}
