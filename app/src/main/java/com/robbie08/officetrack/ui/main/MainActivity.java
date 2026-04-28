package com.robbie08.officetrack.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.robbie08.officetrack.OfficeTrackApp;
import com.robbie08.officetrack.R;
import com.robbie08.officetrack.data.entity.OfficeSessionEntity;
import com.robbie08.officetrack.model.OfficeSessionModel;
import com.robbie08.officetrack.service.ComputationManager;
import com.robbie08.officetrack.service.ComputationService;
import com.robbie08.officetrack.ui.session.SessionHistoryActivity;
import com.robbie08.officetrack.ui.common.BaseActivity;
import com.robbie08.officetrack.util.DateTimeFormatUtils;

public class MainActivity extends BaseActivity {

    private ComputationManager computationManager;

    private ProgressBar weeklyProgressBar;
    private ProgressBar monthlyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        computationManager = ((OfficeTrackApp) getApplication()).getComputationManager();

        initMaterialToolbar("Dashboard");

        initProgressBars();
        loadDashboard();
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        TextView inOfficeSinceTextView = findViewById(R.id.inOfficeSinceTextView);
        TextView currentSessionTextView = findViewById(R.id.currentSessionTextView);
        TextView weeklyProgressTextView = findViewById(R.id.weeklyProgressTextView);
        TextView weeklyRemainingTextView = findViewById(R.id.weeklyRemainingTextView);
        TextView monthlyProgressTextView = findViewById(R.id.monthlyProgressTextView);
        TextView monthlyRemainingTextView = findViewById(R.id.monthlyRemainingTextView);

        Button bStartSession = findViewById(R.id.startSessionButton);
        Button bEndSession = findViewById(R.id.endSessionButton);

        OfficeSessionModel currentSession = computationManager.getCurrentOfficeSession();

        if (currentSession == null) {
            inOfficeSinceTextView.setText("No active session");
            currentSessionTextView.setText("Current session: N/A");

            bStartSession.setEnabled(true);
            bEndSession.setEnabled(false);
        } else {
            inOfficeSinceTextView.setText(
                    "In office since " + DateTimeFormatUtils.formatTime(currentSession.getStartTime())
            );

            long elapsedMinutes = computationManager.getCurrentSessionElapsedMinutes();
            currentSessionTextView.setText(
                    "Current session: " + DateTimeFormatUtils.formatDurationMinutes(elapsedMinutes)
            );

            bStartSession.setEnabled(false);
            bEndSession.setEnabled(true);
        }

        long weekMinutes = computationManager.getCurrentMonthAwareWeekOfficeMinutes();
        long weekTargetMinutes = computationManager.getCurrentMonthAwareWeeklyTargetMinutes();
        long weekRemainingMinutes = Math.max(0, weekTargetMinutes - weekMinutes);

        weeklyProgressTextView.setText(
                DateTimeFormatUtils.formatDurationMinutes(weekMinutes)
                        + " / "
                        + DateTimeFormatUtils.formatDurationMinutes(weekTargetMinutes)
        );

        weeklyRemainingTextView.setText(
                "Remaining: " + DateTimeFormatUtils.formatDurationMinutes(weekRemainingMinutes)
        );

        long monthMinutes = computationManager.getCurrentMonthOfficeMinutes();
        long monthTargetMinutes = computationManager.getEstimatedMonthlyTargetMinutes();
        long monthRemainingMinutes = computationManager.getCurrentMonthRemainingMinutes();

        monthlyProgressTextView.setText(
                DateTimeFormatUtils.formatDurationMinutes(monthMinutes)
                        + " / "
                        + DateTimeFormatUtils.formatDurationMinutes(monthTargetMinutes)
        );

        monthlyRemainingTextView.setText(
                "Remaining: " + DateTimeFormatUtils.formatDurationMinutes(monthRemainingMinutes)
        );

        weeklyProgressBar.setProgress(calculateProgressPercent(weekMinutes, weekTargetMinutes));
        monthlyProgressBar.setProgress(calculateProgressPercent(monthMinutes, monthTargetMinutes));
    }

    private int calculateProgressPercent(long currentMinutes, long targetMinutes) {
        if (targetMinutes <= 0) {
            return 0;
        }

        long percentage = Math.round((currentMinutes * 100.0) / targetMinutes);
        return (int) Math.min(100, Math.max(0, percentage));
    }

    private void initProgressBars() {
        weeklyProgressBar = findViewById(R.id.weeklyProgressBar);
        monthlyProgressBar = findViewById(R.id.monthlyProgressBar);
    }

    private void initButtons() {
        Button bStartSession = findViewById(R.id.startSessionButton);
        Button bEndSession = findViewById(R.id.endSessionButton);
        Button bSessionHistory = findViewById(R.id.sessionHistoryButton);

        bSessionHistory.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, SessionHistoryActivity.class);
                    startActivity(intent);
                }
        );

        bStartSession.setOnClickListener(
                v -> {
                    computationManager.startSession();
                    loadDashboard();
                }
        );

        bEndSession.setOnClickListener(
                v -> {
                    computationManager.endSession();
                    loadDashboard();
                }
        );
    }
}