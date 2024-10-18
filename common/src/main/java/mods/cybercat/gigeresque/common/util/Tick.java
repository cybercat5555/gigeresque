package mods.cybercat.gigeresque.common.util;

public class Tick {

    public static final int PER_SECOND = 20;

    public static final int PER_MINUTE = 60 * PER_SECOND;

    public static final int PER_HOUR = 60 * PER_MINUTE * PER_SECOND;

    public static final int MILLIS_PER_TICK = 1000 / PER_SECOND;

    public static int fromMillis(int millis) {
        if (millis <= 0) {
            throw new IllegalArgumentException("Milliseconds must not be less than zero.");
        }
        return millis / MILLIS_PER_TICK;
    }

    public static int fromSeconds(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Seconds must not be less than zero.");
        }
        return seconds * PER_SECOND;
    }

    public static int fromMinutes(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must not be less than zero.");
        }
        return minutes * PER_MINUTE;
    }

    public static int fromHours(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must not be less than zero.");
        }
        return hours * PER_HOUR;
    }

    private Tick() {
        throw new UnsupportedOperationException();
    }
}
