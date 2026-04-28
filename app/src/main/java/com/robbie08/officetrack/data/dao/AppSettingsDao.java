package com.robbie08.officetrack.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robbie08.officetrack.data.entity.AppSettingsEntity;

@Dao
public interface AppSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(AppSettingsEntity settings);

    @Update
    void update(AppSettingsEntity settings);

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    AppSettingsEntity getSettings();

    @Query("DELETE FROM app_settings")
    void deleteAll();
}