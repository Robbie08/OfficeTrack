package com.robbie08.officetrack.data.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "office_sessions",
        foreignKeys = {
                @ForeignKey(
                        entity = OfficeLocationEntity.class,
                        parentColumns = "id",
                        childColumns = "location_id",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index("start_time"),
                @Index("end_time"),
                @Index("location_id")
        }
)
public class OfficeSessionEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "start_time")
    public long startTime;

    @Nullable
    @ColumnInfo(name = "end_time")
    public Long endTime;

    @Nullable
    @ColumnInfo(name = "location_id")
    public Long locationId;

    public String source;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public OfficeSessionEntity(
            long startTime,
            @Nullable Long endTime,
            @Nullable Long locationId,
            String source,
            long createdAt,
            long updatedAt
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationId = locationId;
        this.source = source;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}