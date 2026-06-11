package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class SleeplessNightsFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(DescriptionConstants.SLEEPLESS_NIGHTS, 0);
        }

        LocalDate firstDate = sessions.stream()
                .map(s -> s.getStartDateTime().toLocalDate())
                .min(LocalDate::compareTo)
                .get();

        LocalDate lastDate = sessions.stream()
                .map(s -> s.getEndDateTime().toLocalDate())
                .max(LocalDate::compareTo)
                .get();

        // Проверяем ВСЕ ночи от firstDate до lastDate ВКЛЮЧИТЕЛЬНО
        long totalNights = ChronoUnit.DAYS.between(firstDate, lastDate) + 1;

        long sleeplessNights = Stream.iterate(firstDate, d -> d.plusDays(1))
                .limit(totalNights)
                .filter(nightDate -> {
                    LocalDateTime nightStart = LocalDateTime.of(nightDate, LocalTime.of(0, 0));
                    LocalDateTime nightEnd = LocalDateTime.of(nightDate, LocalTime.of(6, 0));

                    return sessions.stream().noneMatch(session ->
                            session.getEndDateTime().isAfter(nightStart) &&
                                    session.getStartDateTime().isBefore(nightEnd)
                    );
                })
                .count();

        return new SleepAnalysisResult(DescriptionConstants.SLEEPLESS_NIGHTS, sleeplessNights);
    }
}


