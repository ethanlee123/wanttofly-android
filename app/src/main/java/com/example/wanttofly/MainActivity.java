package com.example.wanttofly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSearchActivity();
    }

    /**
     * Setting a delay is not best practice but since our app launches so fast,
     * we want to show our splash screen for a couple seconds before launching
     * the search activity.
     */
    private void startSearchActivity() {
        new Handler().postDelayed(() -> {
            startActivity(SearchActivity.getIntent(this));
            finish();
        }, 2000);
    }
}