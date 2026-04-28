package com.robbie08.officetrack;

import android.app.Application;

import com.robbie08.officetrack.data.dao.AppSettingsDao;
import com.robbie08.officetrack.data.dao.OfficeLocationDao;
import com.robbie08.officetrack.data.dao.OfficeSessionDao;
import com.robbie08.officetrack.data.db.OfficeTrackDB;
import com.robbie08.officetrack.service.ComputationManager;
import com.robbie08.officetrack.service.ComputationService;

public class OfficeTrackApp extends Application {
    private ComputationManager computationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        OfficeTrackDB db = OfficeTrackDB.getInstance(this); // init the DB

        // Set DAOs
        AppSettingsDao appSettingsDao = db.appSettingsDao();
        OfficeLocationDao officeLocationDao = db.officeLocationDao();
        OfficeSessionDao officeSessionDao = db.officeSessionDao();

        ComputationService computationService = new ComputationService();
        computationManager = new ComputationManager(computationService, appSettingsDao, officeLocationDao, officeSessionDao);
    }

    public ComputationManager getComputationManager() {
        return this.computationManager;
    }
}
