package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsCountFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(session -> session != null)
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        String description = "Количество сессий с плохим качеством сна";
        return new SleepAnalysisResult(description, badCount);
    }
}
