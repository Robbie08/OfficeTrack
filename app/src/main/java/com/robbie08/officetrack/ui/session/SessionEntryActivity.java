package com.robbie08.officetrack.ui.session;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;

import com.robbie08.officetrack.R;
import com.robbie08.officetrack.ui.common.BaseActivity;


public class SessionEntryActivity extends BaseActivity {

    public static final int MODE_ADD_SESSION_ENTRY = 1;
    public static final int MODE_EDIT_SESSION_ENTRY = 2;
    public static final String EXTRA_MODE = "extra_mode";
    private static final String TITLE_EDIT_SESSION_ENTRY = "Edit Session Entry";
    private static final String TITLE_ADD_SESSION_ENTRY = "Add Session Entry";
    private int mode;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_entry);

        mode = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD_SESSION_ENTRY);
        if (mode == MODE_ADD_SESSION_ENTRY) {
            title = TITLE_ADD_SESSION_ENTRY;
        } else {
            title = TITLE_EDIT_SESSION_ENTRY;
        }

        initMaterialToolbar(title, true);

        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initButtons() {
        Button bSave = findViewById(R.id.saveSessionButton);
        Button bDelete = findViewById(R.id.deleteSessionButton);
    }
}