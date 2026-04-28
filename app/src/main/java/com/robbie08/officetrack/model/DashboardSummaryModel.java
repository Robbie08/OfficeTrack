package com.robbie08.officetrack.model;

public class DashboardSummaryModel {
    private final boolean activeSession;
    private final String statusText;
    private final String currentSessionText;
    private final String weeklyProgressText;
    private final String weeklyRemainingText;
    private final String monthlyProgressText;

    public DashboardSummaryModel(
            boolean activeSession,
            String statusText,
            String currentSessionText,
            String weeklyProgressText,
            String weeklyRemainingText,
            String monthlyProgressText
    ) {
        this.activeSession = activeSession;
        this.statusText = statusText;
        this.currentSessionText = currentSessionText;
        this.weeklyProgressText = weeklyProgressText;
        this.weeklyRemainingText = weeklyRemainingText;
        this.monthlyProgressText = monthlyProgressText;
    }

    public boolean hasActiveSession() {
        return activeSession;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getCurrentSessionText() {
        return currentSessionText;
    }

    public String getWeeklyProgressText() {
        return weeklyProgressText;
    }

    public String getWeeklyRemainingText() {
        return weeklyRemainingText;
    }

    public String getMonthlyProgressText() {
        return monthlyProgressText;
    }
}