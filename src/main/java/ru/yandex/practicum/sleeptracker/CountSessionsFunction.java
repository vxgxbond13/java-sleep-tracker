package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class CountSessionsFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.size(); // size() — не цикл, это свойство коллекции

        return new SleepAnalysisResult("Общее количество сессий сна", count);
    }
}
