package com.robbie08.officetrack.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.robbie08.officetrack.data.entity.OfficeLocationEntity;

import java.util.List;

@Dao
public interface OfficeLocationDao {

    @Insert
    long insert(OfficeLocationEntity location);

    @Update
    void update(OfficeLocationEntity location);

    @Query("SELECT * FROM office_locations WHERE id = :id LIMIT 1")
    OfficeLocationEntity getById(long id);

    @Query("SELECT * FROM office_locations WHERE active = 1 ORDER BY id ASC")
    List<OfficeLocationEntity> getActiveLocations();

    @Query("SELECT * FROM office_locations ORDER BY id ASC")
    List<OfficeLocationEntity> getAllLocations();

    @Query("SELECT * FROM office_locations WHERE active = 1 ORDER BY id ASC LIMIT 1")
    OfficeLocationEntity getDefaultActiveLocation();
}