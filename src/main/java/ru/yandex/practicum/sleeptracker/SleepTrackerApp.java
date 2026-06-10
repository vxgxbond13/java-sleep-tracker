package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final List<SleepAnalysisFunction> analysisFunctions = new ArrayList<>();

    static {
        // Регистрируем функции. Позже здесь добавятся новые
        analysisFunctions.add(new CountSessionsFunction());
        analysisFunctions.add(new BadQualitySessionsCountFunction());
        analysisFunctions.add(new MinSessionDurationFunction());
        analysisFunctions.add(new MaxSessionDurationFunction());
        analysisFunctions.add(new AvgSessionDurationFunction());
        analysisFunctions.add(new SleeplessNightsFunction());
        analysisFunctions.add(new ChronotypeFunction());
    }

    public static void main(String[] args) {


        if (args.length == 0) {
            System.err.println("Ошибка: укажите путь к файлу с логом сна");
            System.err.println("Пример: java SleepTrackerApp sleep_log.txt");
            System.exit(1);
        }


        String filePath = args[0];
        Path path = Paths.get(filePath);

        // Чтение файла и парсинг в список SleepingSession
        List<SleepingSession> sessions = loadSessions(path);

        if (sessions.isEmpty()) {
            System.out.println("Нет данных для анализа.");
            return;
        }

        analysisFunctions.stream()
                .map(function -> function.apply(sessions))
                .forEach(result -> {
                    if (result.getDescription().equals("Хронотип пользователя")) {
                        String chronotypeName;
                        switch ((int) result.getValue()) {
                            case 1:
                                chronotypeName = "Сова";
                                break;
                            case 2:
                                chronotypeName = "Жаворонок";
                                break;
                            default:
                                chronotypeName = "Голубь";
                        }
                        System.out.println(result.getDescription() + ": " + chronotypeName);
                    } else {
                        System.out.println(result);
                    }
                });
    }


    private static List<SleepingSession> loadSessions(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .map(SleepingSession::fromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return List.of();
        }
    }

    // Метод для добавления новых функций (если понадобится динамически)
    public static void addFunction(SleepAnalysisFunction function) {
        analysisFunctions.add(function);
    }
}