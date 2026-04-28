package com.robbie08.officetrack.model;

import com.robbie08.officetrack.data.entity.OfficeSessionEntity;
import com.robbie08.officetrack.util.DateTimeFormatUtils;

import java.util.ArrayList;
import java.util.List;

public final class OfficeSessionMapper {
    private OfficeSessionMapper() {}

    public static OfficeSessionModel toModel(OfficeSessionEntity entity) {
        boolean active = entity.endTime == null;
        long end = active ? System.currentTimeMillis() : entity.endTime;

        String displayDate = DateTimeFormatUtils.formatDate(entity.startTime);
        String displayTimeRange = active
                ? DateTimeFormatUtils.formatTime(entity.startTime) + " - Active"
                : DateTimeFormatUtils.formatTime(entity.startTime) + " - " + DateTimeFormatUtils.formatTime(entity.endTime);

        String displayDuration = DateTimeFormatUtils.formatDurationMinutes(
                Math.max(0, (end - entity.startTime) / 60000)
        ) + " · " + formatSource(entity.source);
        

        return new OfficeSessionModel(
                entity.id,
                entity.startTime,
                entity.endTime,
                entity.locationId,
                entity.source,
                displayDate,
                displayTimeRange,
                displayDuration,
                active
        );
    }

    public static List<OfficeSessionModel> toModels(List<OfficeSessionEntity> entities) {
        List<OfficeSessionModel> models = new ArrayList<>();

        for (OfficeSessionEntity entity : entities) {
            models.add(toModel(entity));
        }

        return models;
    }

    private static String formatSource(String source) {
        if (source == null) {
            return "Unknown";
        }

        switch (source) {
            case "MANUAL":
                return "Manual";
            case "GEOFENCE":
                return "Geofence";
            case "EDITED":
                return "Edited";
            default:
                return source;
        }
    }
}