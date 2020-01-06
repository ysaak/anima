package ysaak.anima.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class DateUtils {
    private DateUtils() { /**/ }

    /**
     * Convert an unix timestamp to a LocalDateTime
     * @param unixTime Timestamp
     * @return LocalDateTime
     */
    public static LocalDateTime unixTimeToLocalDate(long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 0, ZoneOffset.UTC);
    }
}
