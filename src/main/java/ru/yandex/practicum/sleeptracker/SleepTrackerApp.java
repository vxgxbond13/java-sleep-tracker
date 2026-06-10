package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {

    public static void main(String[] args) {

        String filePath = args[0];
        Path path = Paths.get(filePath);

        // Чтение файла и парсинг в список SleepingSession
        List<SleepingSession> sessions = loadSessions(path);

        // Пока просто выводим результат для проверки
        System.out.println("Загружено сессий: " + sessions.size());
        sessions.forEach(session -> {
            System.out.println(session.getStartDateTime() + " -> " +
                    session.getEndDateTime() + " [" +
                    session.getQuality() + "]");
        });
    }


    private static List<SleepingSession> loadSessions(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> line != null && !line.trim().isEmpty()) // пропускаем пустые строки
                    .map(SleepingSession::fromLine)   // строку -> объект SleepingSession
                    .collect(Collectors.toList());    // собираем в List
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return List.of(); // возвращаем пустой список при ошибке
        }
    }
}