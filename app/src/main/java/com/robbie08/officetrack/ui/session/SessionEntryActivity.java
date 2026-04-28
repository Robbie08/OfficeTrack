package com.robbie08.officetrack.ui.session;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.robbie08.officetrack.OfficeTrackApp;
import com.robbie08.officetrack.R;
import com.robbie08.officetrack.model.LocationOptionModel;
import com.robbie08.officetrack.model.OfficeSessionModel;
import com.robbie08.officetrack.service.ComputationManager;
import com.robbie08.officetrack.ui.common.BaseActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class SessionEntryActivity extends BaseActivity {

    public static final int MODE_ADD_SESSION_ENTRY = 1;
    public static final int MODE_EDIT_SESSION_ENTRY = 2;

    public static final String EXTRA_MODE = "extra_mode";
    public static final String EXTRA_SESSION_ID = "extra_session_id";

    private static final String TITLE_EDIT_SESSION_ENTRY = "Edit Session Entry";
    private static final String TITLE_ADD_SESSION_ENTRY = "Add Session Entry";
    private static final long INVALID_SESSION_ID = -1L;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private int mode;
    private long sessionId;

    private Button bSave;
    private Button bDelete;

    private EditText startDateEditText;
    private EditText startTimeEditText;
    private EditText endDateEditText;
    private EditText endTimeEditText;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Spinner locationSpinner;
    private List<LocationOptionModel> locationOptions = new ArrayList<>();
    private Long selectedLocationId;
    private ComputationManager computationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_entry);

        computationManager = ((OfficeTrackApp) getApplication()).getComputationManager();

        mode = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD_SESSION_ENTRY);
        sessionId = getIntent().getLongExtra(EXTRA_SESSION_ID, INVALID_SESSION_ID);

        String title = mode == MODE_ADD_SESSION_ENTRY
                ? TITLE_ADD_SESSION_ENTRY
                : TITLE_EDIT_SESSION_ENTRY;

        initMaterialToolbar(title, true);
        initViews();
        initDateTimeDefaults();
        initDateTimePickers();
        initLocationSpinner();
        configureMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void configureMode() {
        if (mode == MODE_EDIT_SESSION_ENTRY) {
            if (sessionId == INVALID_SESSION_ID) {
                finish();
                return;
            }
            // Later: load existing session by sessionId and populate fields
            // Example: loadSession(sessionId);
            bDelete.setVisibility(View.VISIBLE);
            loadSession(sessionId);
        } else {
            // Add mode: hide delete button for now
            bDelete.setVisibility(android.view.View.GONE);
        }
    }
    private void initViews() {
        startDateEditText = findViewById(R.id.startDateEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);

        locationSpinner = findViewById(R.id.locationSpinner);

        bSave = findViewById(R.id.saveSessionButton);
        bDelete = findViewById(R.id.deleteSessionButton);

        bSave.setOnClickListener(
                v -> handleSave()
        );

        bDelete.setOnClickListener(v->handleDelete());
    }

    private void handleDelete() {
        if (mode != MODE_EDIT_SESSION_ENTRY) {
            return;
        }

        if (sessionId == INVALID_SESSION_ID) {
            Toast.makeText(this, "Invalid session.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete session?")
                .setMessage("This will permanently remove this office session.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    computationManager.deleteSession(sessionId);
                    Toast.makeText(this, "Session deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }

    private void handleSave() {
        if (selectedLocationId == null) {
            Toast.makeText(this, "Please select a location.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        long startMillis = startDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        long endMillis = endDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        if (endMillis <= startMillis) {
            Toast.makeText(this, "End time must be after start time.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mode == MODE_ADD_SESSION_ENTRY) {
            computationManager.addManualSession(startMillis, endMillis, selectedLocationId);
            Toast.makeText(this, "Session saved.", Toast.LENGTH_SHORT).show();
        } else if (mode == MODE_EDIT_SESSION_ENTRY) {
            if (sessionId == INVALID_SESSION_ID) {
                Toast.makeText(this, "Invalid session.", Toast.LENGTH_SHORT).show();
                return;
            }

            computationManager.updateSession(sessionId, startMillis, endMillis, selectedLocationId);
            Toast.makeText(this, "Session updated.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void initLocationSpinner() {
        locationOptions = computationManager.getLocationOptions();

        if (locationOptions == null || locationOptions.isEmpty()) {
            Toast.makeText(this, "No office location found", Toast.LENGTH_SHORT).show();
            selectedLocationId = null;
            return;
        }

        ArrayAdapter<LocationOptionModel> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        // Default to first active location for MVP
        selectedLocationId = locationOptions.get(0).getId();

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationOptionModel selectedLocation = locationOptions.get(position);
                selectedLocationId = selectedLocation.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLocationId = null;
            }
        });
    }
    private void initDateTimeDefaults() {
        LocalDate today = LocalDate.now();

        startDate = today;
        endDate = today;

        // Simple default: 9:00 AM to 5:00 PM
        startTime = LocalTime.of(9, 0);
        endTime = LocalTime.of(17, 0);

        refreshDateTimeFields();
    }

    private void refreshDateTimeFields() {
        startDateEditText.setText(startDate.format(DATE_FORMATTER));
        startTimeEditText.setText(startTime.format(TIME_FORMATTER));
        endDateEditText.setText(endDate.format(DATE_FORMATTER));
        endTimeEditText.setText(endTime.format(TIME_FORMATTER));
    }

    private void initDateTimePickers() {
        startDateEditText.setOnClickListener(v -> showStartDatePicker());
        startTimeEditText.setOnClickListener(v -> showStartTimePicker());
        endDateEditText.setOnClickListener(v -> showEndDatePicker());
        endTimeEditText.setOnClickListener(v -> showEndTimePicker());
    }
    private void showStartDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    startDate = LocalDate.of(year, month + 1, dayOfMonth);
                    startDateEditText.setText(startDate.format(DATE_FORMATTER));
                },
                startDate.getYear(),
                startDate.getMonthValue() - 1,
                startDate.getDayOfMonth()
        );

        dialog.show();
    }

    private void showEndDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    endDate = LocalDate.of(year, month + 1, dayOfMonth);
                    endDateEditText.setText(endDate.format(DATE_FORMATTER));
                },
                endDate.getYear(),
                endDate.getMonthValue() - 1,
                endDate.getDayOfMonth()
        );

        dialog.show();
    }

    private void showStartTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    startTime = LocalTime.of(hourOfDay, minute);
                    startTimeEditText.setText(startTime.format(TIME_FORMATTER));
                },
                startTime.getHour(),
                startTime.getMinute(),
                false
        );

        dialog.show();
    }

    private void showEndTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    endTime = LocalTime.of(hourOfDay, minute);
                    endTimeEditText.setText(endTime.format(TIME_FORMATTER));
                },
                endTime.getHour(),
                endTime.getMinute(),
                false
        );

        dialog.show();
    }

    private void loadSession(long sessionId) {
        OfficeSessionModel session = computationManager.getOfficeSessionById(sessionId);

        if (session == null) {
            Toast.makeText(this, "Session not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime startDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(session.getStartTime()),
                zoneId
        );

        Long sessionEndTime = session.getEndTime();

        LocalDateTime endDateTime;

        if (sessionEndTime != null) {
            endDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(sessionEndTime),
                    zoneId
            );
        } else {
            // This handles the rare case where someone edits an active session.
            endDateTime = LocalDateTime.now(zoneId);
        }

        startDate = startDateTime.toLocalDate();
        startTime = startDateTime.toLocalTime();

        endDate = endDateTime.toLocalDate();
        endTime = endDateTime.toLocalTime();

        selectedLocationId = session.getLocationId();

        refreshDateTimeFields();
        selectLocationInSpinner(selectedLocationId);
    }

    private void selectLocationInSpinner(Long locationId) {
        if (locationId == null || locationOptions == null || locationOptions.isEmpty()) {
            return;
        }

        for (int i = 0; i < locationOptions.size(); i++) {
            if (locationOptions.get(i).getId() == locationId) {
                locationSpinner.setSelection(i);
                selectedLocationId = locationId;
                return;
            }
        }
    }
}