package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    DOVE(0, "Голубь"),
    OWL(1, "Сова"),
    LARK(2, "Жаворонок");

    private final int code;
    private final String displayName;

    Chronotype(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Chronotype fromCode(int code) {
        for (Chronotype c : values()) {
            if (c.code == code) {
                return c;
            }
        }
        return DOVE;
    }
}
