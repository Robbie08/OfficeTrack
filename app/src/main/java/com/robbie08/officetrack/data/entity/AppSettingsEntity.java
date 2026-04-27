package com.robbie08.officetrack.data.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "app_settings",
        foreignKeys = {
                @ForeignKey(
                        entity = OfficeLocationEntity.class,
                        parentColumns = "id",
                        childColumns = "default_location_id",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index("default_location_id")
        }
)
public class AppSettingsEntity {

    @PrimaryKey
    public int id = 1;

    @Nullable
    @ColumnInfo(name = "default_location_id")
    public Long defaultLocationId;

    @ColumnInfo(name = "weekly_work_hours")
    public double weeklyWorkHours;

    @ColumnInfo(name = "required_office_percentage")
    public double requiredOfficePercentage;

    @ColumnInfo(name = "geofence_enabled")
    public boolean geofenceEnabled;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public AppSettingsEntity(
            @Nullable Long defaultLocationId,
            double weeklyWorkHours,
            double requiredOfficePercentage,
            boolean geofenceEnabled,
            long createdAt,
            long updatedAt
    ) {
        this.id = 1;
        this.defaultLocationId = defaultLocationId;
        this.weeklyWorkHours = weeklyWorkHours;
        this.requiredOfficePercentage = requiredOfficePercentage;
        this.geofenceEnabled = geofenceEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}