package com.robbie08.officetrack.service;

import android.util.Log;

import com.robbie08.officetrack.data.dao.AppSettingsDao;
import com.robbie08.officetrack.data.dao.OfficeLocationDao;
import com.robbie08.officetrack.data.dao.OfficeSessionDao;
import com.robbie08.officetrack.data.entity.AppSettingsEntity;
import com.robbie08.officetrack.data.entity.OfficeLocationEntity;
import com.robbie08.officetrack.data.entity.OfficeSessionEntity;
import com.robbie08.officetrack.model.LocationOptionModel;
import com.robbie08.officetrack.model.OfficeSessionMapper;
import com.robbie08.officetrack.model.OfficeSessionModel;
import com.robbie08.officetrack.util.DateRangeUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComputationManager {
    private final ComputationService computationService;
    private final AppSettingsDao appSettingsDao;
    private final OfficeLocationDao officeLocationDao;
    private final OfficeSessionDao officeSessionDao;
    private final String TAG = ComputationManager.class.getSimpleName();
    public ComputationManager(ComputationService computationService, AppSettingsDao appSettingsDao, OfficeLocationDao officeLocationDao, OfficeSessionDao officeSessionDao) {
        this.computationService = computationService;
        this.appSettingsDao = appSettingsDao;
        this.officeLocationDao = officeLocationDao;
        this.officeSessionDao = officeSessionDao;
    }

    public OfficeSessionModel getCurrentOfficeSession() {
        Log.i(TAG, "Fetching Current Office Session");
        OfficeSessionEntity currentSession = officeSessionDao.getActiveSession();
        if (currentSession == null) {
            Log.i(TAG, "No current session was found.");
            return null;
        }
        return OfficeSessionMapper.toModel(currentSession);
    }

    public List<OfficeSessionModel> getAllOfficeSessions() {
        Log.i(TAG, "Fetching all office sessions.");

        List<OfficeSessionEntity> sessions = officeSessionDao.getAllSessions();

        if (sessions == null || sessions.isEmpty()) {
            Log.i(TAG, "No office sessions found.");
            return java.util.Collections.emptyList();
        }

        return OfficeSessionMapper.toModels(sessions);
    }

    public long getCurrentMonthOfficeMinutes() {
        long monthStartMillis = DateRangeUtils.getStartOfCurrentMonthMillis();
        long monthEndMillis = DateRangeUtils.getEndOfCurrentMonthMillis();

        List<OfficeSessionEntity> monthlySessions =
                officeSessionDao.getSessionsBetween(monthStartMillis, monthEndMillis);
        return calculateTotalMinutes(monthlySessions, monthStartMillis, monthEndMillis);
    }

    public long getCurrentWeekOfficeMinutes() {
        long startMillis = DateRangeUtils.getStartOfCurrentWeekWithinCurrentMonthMillis();
        long endMillis = DateRangeUtils.getEndOfCurrentWeekWithinCurrentMonthMillis();

        List<OfficeSessionEntity> sessions =
                officeSessionDao.getSessionsBetween(startMillis, endMillis);

        return calculateTotalMinutes(sessions, startMillis, endMillis);
    }

    private long calculateTotalMinutes(
            List<OfficeSessionEntity> sessions,
            long periodStartMillis,
            long periodEndMillis
    ) {
        if (sessions == null || sessions.isEmpty()) {
            return 0;
        }

        long totalMinutes = 0;
        long now = System.currentTimeMillis();

        for (OfficeSessionEntity session : sessions) {
            long sessionEnd = session.endTime != null ? session.endTime : now;

            long overlapStart = Math.max(session.startTime, periodStartMillis);
            long overlapEnd = Math.min(sessionEnd, periodEndMillis);

            if (overlapEnd > overlapStart) {
                totalMinutes += (overlapEnd - overlapStart) / 60000;
            }
        }

        return totalMinutes;
    }

    public long getWeeklyTargetMinutes() {
        AppSettingsEntity settings = appSettingsDao.getSettings();
        if (settings == null) {
            return 0;
        }

        return Math.round(
                settings.weeklyWorkHours * (settings.requiredOfficePercentage / 100) * 60
        );
    }

    public long getEstimatedMonthlyTargetMinutes() {
        long dailyTargetMinutes = Math.round(getWeeklyTargetMinutes() / 5.0);
        int workdaysInMonth = DateRangeUtils.countWeekdaysInCurrentMonth();

        return dailyTargetMinutes * workdaysInMonth;
    }

    public long getCurrentMonthAwareWeeklyTargetMinutes() {
        long dailyTargetMinutes = Math.round(getWeeklyTargetMinutes() / 5.0);
        int workdays = DateRangeUtils.countWeekdaysInCurrentWeekWithinCurrentMonth();

        return dailyTargetMinutes * workdays;
    }

    public long getCurrentMonthAwareWeekOfficeMinutes() {
        long startMillis = DateRangeUtils.getStartOfCurrentWeekWithinCurrentMonthMillis();
        long endMillis = DateRangeUtils.getEndOfCurrentWeekWithinCurrentMonthMillis();

        List<OfficeSessionEntity> sessions =
                officeSessionDao.getSessionsBetween(startMillis, endMillis);

        return calculateTotalMinutes(sessions, startMillis, endMillis);
    }


    public long getCurrentWeekRemainingMinutes() {
        return Math.max(0, getWeeklyTargetMinutes() - getCurrentWeekOfficeMinutes());
    }

    public long getCurrentMonthRemainingMinutes() {
        return Math.max(0, getEstimatedMonthlyTargetMinutes() - getCurrentMonthOfficeMinutes());
    }
    public long getCurrentSessionElapsedMinutes() {
        OfficeSessionEntity currentSession = officeSessionDao.getActiveSession();

        if (currentSession == null) {
            return 0;
        }

        long now = System.currentTimeMillis();
        return Math.max(0, (now - currentSession.startTime) / 60000);
    }

    public List<LocationOptionModel> getLocationOptions() {
        List<OfficeLocationEntity> locations = officeLocationDao.getActiveLocations();

        if (locations == null || locations.isEmpty()) {
            return Collections.emptyList();
        }

        List<LocationOptionModel> options = new ArrayList<>();

        for (OfficeLocationEntity location : locations) {
            options.add(new LocationOptionModel(location.id, location.name));
        }

        return options;
    }


    public void startSession() {
        Log.i(TAG, "Starting office session.");

        OfficeSessionEntity activeSession = officeSessionDao.getActiveSession();
        if (activeSession != null) {
            Log.i(TAG, "Active session already exists. Skipping start.");
            return;
        }

        long now = System.currentTimeMillis();
        Long locationId = getDefaultLocationId();

        OfficeSessionEntity session = new OfficeSessionEntity(
                now,
                null,
                locationId,
                "MANUAL",
                now,
                now
        );

        officeSessionDao.insert(session);
        Log.i(TAG, "Office session started.");
    }

    public void endSession() {
        Log.i(TAG, "Ending office session.");

        OfficeSessionEntity activeSession = officeSessionDao.getActiveSession();
        if (activeSession == null) {
            Log.i(TAG, "No active session found. Skipping end.");
            return;
        }

        long now = System.currentTimeMillis();

        activeSession.endTime = now;
        activeSession.updatedAt = now;

        officeSessionDao.update(activeSession);
        Log.i(TAG, "Office session ended.");
    }

    private Long getDefaultLocationId() {
        AppSettingsEntity settings = appSettingsDao.getSettings();

        if (settings != null && settings.defaultLocationId != null) {
            return settings.defaultLocationId;
        }

        OfficeLocationEntity defaultLocation = officeLocationDao.getDefaultActiveLocation();

        if (defaultLocation != null) {
            return defaultLocation.id;
        }

        Log.w(TAG, "No default office location found.");
        return null;
    }
    public void addManualSession(long startMillis, long endMillis, Long locationId) {
        long now = System.currentTimeMillis();

        OfficeSessionEntity session = new OfficeSessionEntity(
                startMillis,
                endMillis,
                locationId,
                "MANUAL",
                now,
                now
        );

        officeSessionDao.insert(session);
    }

    public void updateSession(long sessionId, long startMillis, long endMillis, Long locationId) {
        OfficeSessionEntity existingSession = officeSessionDao.getById(sessionId);

        if (existingSession == null) {
            Log.w(TAG, "Cannot update session. Session not found. id=" + sessionId);
            return;
        }

        existingSession.startTime = startMillis;
        existingSession.endTime = endMillis;
        existingSession.locationId = locationId;
        existingSession.source = "EDITED";
        existingSession.updatedAt = System.currentTimeMillis();

        officeSessionDao.update(existingSession);
    }

    public void deleteSession(long sessionId) {
        Log.i(TAG, "Deleting session id=" + sessionId);
        officeSessionDao.deleteById(sessionId);
    }

    public OfficeSessionModel getOfficeSessionById(long sessionId) {
        Log.i(TAG, "Fetching office session by id=" + sessionId);

        OfficeSessionEntity session = officeSessionDao.getById(sessionId);

        if (session == null) {
            Log.w(TAG, "No session found for id=" + sessionId);
            return null;
        }

        return OfficeSessionMapper.toModel(session);
    }
}
