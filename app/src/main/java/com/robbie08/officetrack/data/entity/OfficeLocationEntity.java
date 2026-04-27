package com.robbie08.officetrack.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "office_locations")
public class OfficeLocationEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public double latitude;

    public double longitude;

    @ColumnInfo(name = "radius_meters")
    public float radiusMeters;

    public boolean active;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public OfficeLocationEntity(
            String name,
            double latitude,
            double longitude,
            float radiusMeters,
            boolean active,
            long createdAt,
            long updatedAt
    ) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}