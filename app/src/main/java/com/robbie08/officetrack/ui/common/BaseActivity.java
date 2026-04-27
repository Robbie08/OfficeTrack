package com.robbie08.officetrack.ui.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.robbie08.officetrack.R;

public class BaseActivity extends AppCompatActivity {
    /**
     * This method sets up the material tool bar.
     */
    protected void initMaterialToolbar(String title, boolean showBackButton) {
        // Set up our toolbar component
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIconTint(getColor(R.color.gt_white));

        if (getSupportActionBar() != null) {
            // manually set the title of the app in case it wasn't set previously
            if (title == null || title.isBlank()) {
                getSupportActionBar().setTitle(R.string.app_name);
            } else {
                getSupportActionBar().setTitle(title);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            getSupportActionBar().setDisplayShowHomeEnabled(showBackButton);
        }

        if (showBackButton) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Default toolbar with no back button.
     */
    protected void initMaterialToolbar(String title) {
        initMaterialToolbar(title, false);
    }

    /**
     * Default toolbar using app name with no back button.
     */
    protected void initMaterialToolbar() {
        initMaterialToolbar(null, false);
    }
}
