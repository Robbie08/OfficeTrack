package com.robbie08.officetrack.model;

public class OfficeSessionModel {
    private final long id;
    private final long startTime;
    private final Long endTime;
    private final Long locationId;
    private final String source;

    private final String displayDate;
    private final String displayTimeRange;
    private final String displayDuration;
    private final boolean active;

    public OfficeSessionModel(
            long id,
            long startTime,
            Long endTime,
            Long locationId,
            String source,
            String displayDate,
            String displayTimeRange,
            String displayDuration,
            boolean active
    ) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationId = locationId;
        this.source = source;
        this.displayDate = displayDate;
        this.displayTimeRange = displayTimeRange;
        this.displayDuration = displayDuration;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getSource() {
        return source;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public String getDisplayTimeRange() {
        return displayTimeRange;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public boolean isActive() {
        return active;
    }
}