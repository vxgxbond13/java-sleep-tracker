package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;


public class AvgSessionDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(DescriptionConstants.AVG_DURATION, 0);
        }

        // Сумма длительностей всех сессий в минутах
        long sumMinutes = sessions.stream()
                .filter(session -> session != null)
                .mapToLong(session -> Duration.between(session.getStartDateTime(), session.getEndDateTime()).toMinutes())
                .sum();

        long averageMinutes = sumMinutes / sessions.size();

        return new SleepAnalysisResult(DescriptionConstants.AVG_DURATION, averageMinutes);
    }
}
