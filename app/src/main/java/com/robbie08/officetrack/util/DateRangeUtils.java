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

    public static int countWeekdaysInCurrentMonth() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        LocalDate current = today.withDayOfMonth(1);
        LocalDate endExclusive = current.plusMonths(1);

        int count = 0;

        while (current.isBefore(endExclusive)) {
            DayOfWeek day = current.getDayOfWeek();

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                count++;
            }

            current = current.plusDays(1);
        }

        return count;
    }

    public static int countWeekdaysInCurrentWeekWithinCurrentMonth() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEndExclusive = weekStart.plusWeeks(1);

        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        int count = 0;
        LocalDate current = weekStart;

        while (current.isBefore(weekEndExclusive)) {
            DayOfWeek day = current.getDayOfWeek();

            boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
            boolean isInCurrentMonth =
                    current.getMonthValue() == currentMonth &&
                            current.getYear() == currentYear;

            if (isWeekday && isInCurrentMonth) {
                count++;
            }

            current = current.plusDays(1);
        }

        return count;
    }

    public static long getStartOfCurrentWeekWithinCurrentMonthMillis() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate monthStart = today.withDayOfMonth(1);

        LocalDate start = weekStart.isAfter(monthStart) ? weekStart : monthStart;

        return start.atStartOfDay(zoneId).toInstant().toEpochMilli();
    }

    public static long getEndOfCurrentWeekWithinCurrentMonthMillis() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEndExclusive = weekStart.plusWeeks(1);

        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);

        LocalDate end = weekEndExclusive.isBefore(nextMonthStart)
                ? weekEndExclusive
                : nextMonthStart;

        return end.atStartOfDay(zoneId).toInstant().toEpochMilli();
    }
}
