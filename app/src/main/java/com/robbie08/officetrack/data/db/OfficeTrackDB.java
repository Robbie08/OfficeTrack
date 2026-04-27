package com.robbie08.officetrack.data.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.robbie08.officetrack.data.entity.AppSettingsEntity;
import com.robbie08.officetrack.data.entity.OfficeLocationEntity;
import com.robbie08.officetrack.data.entity.OfficeSessionEntity;

/**
 * Singleton Room DB abstract class for our OfficeTracker Application.
 */
@Database(
        // From the documentation, I believe we only need entities and version properties
        // https://developer.android.com/reference/kotlin/androidx/room/Database
        entities = {
                OfficeLocationEntity.class,
                OfficeSessionEntity.class,
                AppSettingsEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class OfficeTrackDB extends RoomDatabase {

    private static volatile OfficeTrackDB officeTrackDBInstance;
    private static final Object lock = new Object();
    private static final String DB_NAME = "office_track_db";
    private static final String TAG = "OfficeTrackDB";
    public static OfficeTrackDB getInstance(Context context) {
        // Only init our db instance once rather than every time getInstance() gets called.
        if (officeTrackDBInstance == null) {
            synchronized (lock) {
                // once inside the critical section let's check again to avoid re-creating the object
                if (officeTrackDBInstance == null) {
                    Log.d(TAG, "Initializing database: " + DB_NAME);
                    officeTrackDBInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            OfficeTrackDB.class,
                            DB_NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return officeTrackDBInstance;
    }
}
