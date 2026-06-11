package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsCountFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(session -> session != null)
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult(DescriptionConstants.BAD_QUALITY_SESSIONS, badCount);
    }
}
