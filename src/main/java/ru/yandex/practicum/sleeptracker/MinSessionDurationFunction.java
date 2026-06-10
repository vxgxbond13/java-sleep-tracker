package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.OptionalLong;

public class MinSessionDurationFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        OptionalLong optionalMin = sessions.stream().filter(session -> session != null).mapToLong(session -> Duration.between(session.getStartDateTime(), session.getEndDateTime()).toMinutes()).min();

        long minMinutes = optionalMin.orElse(0);

        String description = "Минимальная продолжительность сна (минуты)";
        return new SleepAnalysisResult(description, minMinutes);
    }
}
