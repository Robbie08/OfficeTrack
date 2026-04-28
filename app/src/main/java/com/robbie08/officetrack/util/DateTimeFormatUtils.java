package com.robbie08.officetrack.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatUtils {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, MMM d");

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("h:mm a");

    private DateTimeFormatUtils() {}

    public static String formatDate(long epochMillis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneId.systemDefault()
        );

        return dateTime.format(DATE_FORMATTER);
    }

    public static String formatTime(long epochMillis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneId.systemDefault()
        );

        return dateTime.format(TIME_FORMATTER);
    }

    public static String formatDurationMinutes(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return hours + "h " + minutes + "m";
    }
}