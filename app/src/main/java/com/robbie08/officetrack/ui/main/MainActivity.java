package com.robbie08.officetrack.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.robbie08.officetrack.R;
import com.robbie08.officetrack.ui.session.SessionHistoryActivity;
import com.robbie08.officetrack.ui.common.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initMaterialToolbar("Office Track");
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initButtons() {
        Button bStartSession = findViewById(R.id.startSessionButton);
        Button bEndSession = findViewById(R.id.endSessionButton);
        Button bSessionHistory = findViewById(R.id.sessionHistoryButton);

        // TODO: add logic to enable buttons when applicable

        bSessionHistory.setOnClickListener(
                v -> {
                    Intent intent = new Intent(MainActivity.this, SessionHistoryActivity.class);
                    startActivity(intent);
                }
        );
    }
}