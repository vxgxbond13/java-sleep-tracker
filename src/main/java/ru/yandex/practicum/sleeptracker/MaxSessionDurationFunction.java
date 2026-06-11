package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.OptionalLong;

public class MaxSessionDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        OptionalLong optionalMax = sessions.stream()
                .filter(session -> session != null)
                .mapToLong(session -> Duration.between(session.getStartDateTime(), session.getEndDateTime()).toMinutes())
                .max();

        long maxMinutes = optionalMax.orElse(0);

        return new SleepAnalysisResult(DescriptionConstants.MAX_DURATION, maxMinutes);
    }
}
