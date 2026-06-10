package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    // Константы для типов
    private static final int OWL = 1;
    private static final int LARK = 2;
    private static final int DOVE = 0;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", DOVE);
        }

        // Фильтруем только ночные сессии (которые пересекаются с интервалом 00:00-06:00)
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSession)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", DOVE);
        }

        // Считаем типы
        long owlCount = nightSessions.stream()
                .filter(this::isOwl)
                .count();

        long larkCount = nightSessions.stream()
                .filter(this::isLark)
                .count();

        long doveCount = nightSessions.size() - owlCount - larkCount;

        // Определяем победителя
        int chronotype;
        if (owlCount > larkCount && owlCount > doveCount) {
            chronotype = OWL;
        } else if (larkCount > owlCount && larkCount > doveCount) {
            chronotype = LARK;
        } else {
            chronotype = DOVE;  // при равенстве или если голуби в большинстве
        }

        return new SleepAnalysisResult("Хронотип пользователя", chronotype);
    }

    // Проверка, что сессия ночная (пересекается с 00:00-06:00)
    private boolean isNightSession(SleepingSession session) {
        LocalDateTime nightStart = session.getStartDateTime()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nightEnd = nightStart.withHour(6).withMinute(0);

        // Сессия считается ночной, если она пересекается с ночным интервалом
        // или если она началась после 18:00 и закончилась до 12:00 следующего дня
        LocalTime startTime = session.getStartDateTime().toLocalTime();
        LocalTime endTime = session.getEndDateTime().toLocalTime();

        // Упрощённо: сессия ночная, если она началась между 18:00 и 06:00
        // или закончилась между 00:00 и 12:00
        boolean startsInEvening = startTime.isAfter(LocalTime.of(18, 0)) ||
                startTime.isBefore(LocalTime.of(6, 0));
        boolean endsInMorning = endTime.isBefore(LocalTime.of(12, 0));

        return startsInEvening && endsInMorning;
    }

    // Проверка на сову
    private boolean isOwl(SleepingSession session) {
        LocalTime bedTime = session.getStartDateTime().toLocalTime();
        LocalTime wakeTime = session.getEndDateTime().toLocalTime();

        // Если пробуждение на следующий день, корректируем
        if (wakeTime.isBefore(bedTime)) {
            wakeTime = wakeTime.plusHours(24);
        }

        return bedTime.isAfter(LocalTime.of(23, 0)) &&
                wakeTime.isAfter(LocalTime.of(9, 0));
    }

    // Проверка на жаворонка
    private boolean isLark(SleepingSession session) {
        LocalTime bedTime = session.getStartDateTime().toLocalTime();
        LocalTime wakeTime = session.getEndDateTime().toLocalTime();

        // Если пробуждение на следующий день, корректируем
        if (wakeTime.isBefore(bedTime)) {
            wakeTime = wakeTime.plusHours(24);
        }

        return bedTime.isBefore(LocalTime.of(22, 0)) &&
                wakeTime.isBefore(LocalTime.of(7, 0));
    }
}

