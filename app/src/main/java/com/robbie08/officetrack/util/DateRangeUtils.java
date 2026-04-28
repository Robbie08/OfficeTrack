package com.robbie08.officetrack.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

public final class DateRangeUtils {
    private DateRangeUtils() {}

    public static long getStartOfCurrentWeekMillis() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);

        return weekStart
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
    }

    public static long getEndOfCurrentWeekMillis() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate nextWeekStart = weekStart.plusWeeks(1);

        return nextWeekStart
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
    }

    public static long getStartOfCurrentMonthMillis() {
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate today = LocalDate.now(zoneId);
        LocalDate monthStart = today.withDayOfMonth(1);

        return monthStart
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
    }

    public static long getEndOfCurrentMonthMillis() {
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate today = LocalDate.now(zoneId);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);

        return nextMonthStart
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
    }
}
