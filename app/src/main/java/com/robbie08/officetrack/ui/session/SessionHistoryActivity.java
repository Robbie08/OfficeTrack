package com.robbie08.officetrack.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.robbie08.officetrack.R;
import com.robbie08.officetrack.ui.common.BaseActivity;

public class SessionHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_history);

        initMaterialToolbar("Session History", true);
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
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