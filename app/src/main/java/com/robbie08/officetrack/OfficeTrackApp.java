package com.robbie08.officetrack;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.robbie08.officetrack.data.dao.AppSettingsDao;
import com.robbie08.officetrack.data.dao.OfficeLocationDao;
import com.robbie08.officetrack.data.dao.OfficeSessionDao;
import com.robbie08.officetrack.data.db.OfficeTrackDB;
import com.robbie08.officetrack.data.entity.AppSettingsEntity;
import com.robbie08.officetrack.data.entity.OfficeLocationEntity;
import com.robbie08.officetrack.service.ComputationManager;
import com.robbie08.officetrack.service.ComputationService;

public class OfficeTrackApp extends Application {
    private ComputationManager computationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        OfficeTrackDB db = OfficeTrackDB.getInstance(this); // init the DB
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Set DAOs
        AppSettingsDao appSettingsDao = db.appSettingsDao();
        OfficeLocationDao officeLocationDao = db.officeLocationDao();
        OfficeSessionDao officeSessionDao = db.officeSessionDao();

        seedDefaultDataIfNeeded(officeLocationDao, appSettingsDao);

        ComputationService computationService = new ComputationService();
        computationManager = new ComputationManager(computationService, appSettingsDao, officeLocationDao, officeSessionDao);
    }

    private void seedDefaultDataIfNeeded(
            OfficeLocationDao officeLocationDao,
            AppSettingsDao appSettingsDao
    ) {
        long now = System.currentTimeMillis();

        OfficeLocationEntity defaultLocation = officeLocationDao.getDefaultActiveLocation();

        if (defaultLocation == null) {
            defaultLocation = new OfficeLocationEntity(
                    "U.S. Bank Cupertino",
                    37.318014390881544,
                    -122.03285169815584,
                    100.0f,
                    true,
                    now,
                    now
            );

            long locationId = officeLocationDao.insert(defaultLocation);
            defaultLocation.id = locationId;
        }

        AppSettingsEntity settings = appSettingsDao.getSettings();

        if (settings == null) {
            AppSettingsEntity defaultSettings = new AppSettingsEntity(
                    defaultLocation.id,
                    40.0,
                    60.0,
                    true,
                    now,
                    now
            );

            appSettingsDao.upsert(defaultSettings);
        }
    }


    public ComputationManager getComputationManager() {
        return this.computationManager;
    }
}
