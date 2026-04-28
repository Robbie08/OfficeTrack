package com.robbie08.officetrack.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.robbie08.officetrack.data.entity.OfficeSessionEntity;

import java.util.List;

@Dao
public interface OfficeSessionDao {

    @Insert
    long insert(OfficeSessionEntity session);

    @Update
    void update(OfficeSessionEntity session);

    @Delete
    void delete(OfficeSessionEntity session);

    @Query("SELECT * FROM office_sessions WHERE id = :id LIMIT 1")
    OfficeSessionEntity getById(long id);

    @Query("SELECT * FROM office_sessions WHERE end_time IS NULL LIMIT 1")
    OfficeSessionEntity getActiveSession();

    @Query("SELECT * FROM office_sessions ORDER BY start_time DESC")
    List<OfficeSessionEntity> getAllSessions();

    @Query("""
            SELECT * FROM office_sessions
            WHERE start_time >= :startMillis
              AND start_time < :endMillis
            ORDER BY start_time DESC
            """)
    List<OfficeSessionEntity> getSessionsBetween(long startMillis, long endMillis);

    @Query("DELETE FROM office_sessions WHERE id = :id")
    void deleteById(long id);
}