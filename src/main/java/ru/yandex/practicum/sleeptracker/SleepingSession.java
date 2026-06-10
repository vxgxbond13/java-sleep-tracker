package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final SleepQuality quality;

    // Формат из файла: 01.10.25 22:15
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    //Конструктор
    public SleepingSession(LocalDateTime startDateTime, LocalDateTime endDateTime, SleepQuality quality) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.quality = quality;
    }

    //Геттеры для доступа к полям для анализа
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    // Статический фабричный метод для создания из строки файла
    public static SleepingSession fromLine(String line) {
        String[] parts = line.split(";");
        // parts[0] = "01.10.25 22:15"
        // parts[1] = "02.10.25 08:00"
        // parts[2] = "GOOD"

        LocalDateTime start = LocalDateTime.parse(parts[0], FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1], FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2]);

        return new SleepingSession(start, end, quality);
    }
}
