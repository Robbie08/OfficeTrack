package com.robbie08.officetrack.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robbie08.officetrack.OfficeTrackApp;
import com.robbie08.officetrack.R;
import com.robbie08.officetrack.model.OfficeSessionModel;
import com.robbie08.officetrack.service.ComputationManager;
import com.robbie08.officetrack.ui.common.BaseActivity;

import java.util.List;

public class SessionHistoryActivity extends BaseActivity {

    private ComputationManager computationManager;
    private OfficeSessionAdapter officeSessionAdapter;
    private RecyclerView sessionRecyclerView;
    private TextView emptyHistoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_history);
        computationManager = ((OfficeTrackApp) getApplication()).getComputationManager();

        initMaterialToolbar("Session History", true);
        initRecyclerView();
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSessions();
    }

    private void initRecyclerView() {
        sessionRecyclerView = findViewById(R.id.sessionRecyclerView);
        emptyHistoryTextView = findViewById(R.id.emptyHistoryTextView);

        officeSessionAdapter = new OfficeSessionAdapter(session -> {
            Intent intent = new Intent(SessionHistoryActivity.this, SessionEntryActivity.class);
            intent.putExtra(SessionEntryActivity.EXTRA_MODE, SessionEntryActivity.MODE_EDIT_SESSION_ENTRY);
            intent.putExtra(SessionEntryActivity.EXTRA_SESSION_ID, session.getId());
            startActivity(intent);
        });

        sessionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionRecyclerView.setAdapter(officeSessionAdapter);
    }

    private void loadSessions() {
        List<OfficeSessionModel> sessions = computationManager.getAllOfficeSessions();

        officeSessionAdapter.setSessions(sessions);

        if (sessions == null || sessions.isEmpty()) {
            sessionRecyclerView.setVisibility(View.GONE);
            emptyHistoryTextView.setVisibility(View.VISIBLE);
        } else {
            sessionRecyclerView.setVisibility(View.VISIBLE);
            emptyHistoryTextView.setVisibility(View.GONE);
        }
    }

    protected void initButtons() {
        Button bAddSession = findViewById(R.id.addSessionButton);

        bAddSession.setOnClickListener(
                v -> {
                    Intent intent = new Intent(SessionHistoryActivity.this, SessionEntryActivity.class);
                    intent.putExtra(SessionEntryActivity.EXTRA_MODE, SessionEntryActivity.MODE_ADD_SESSION_ENTRY);
                    startActivity(intent);
                }
        );
    }
}