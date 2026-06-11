package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {

    private final String description;
    private final long value;

    public SleepAnalysisResult(String description, long value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description + ": " + value;
    }
}
