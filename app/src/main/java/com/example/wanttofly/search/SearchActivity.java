package com.example.wanttofly.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.wanttofly.R;
import com.example.wanttofly.flightdetails.FlightDetails;

public class SearchActivity extends AppCompatActivity {
    Button testButton = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        testButton = findViewById(R.id.b_testing);
        setupSeachBar();
    }

    private void setupSeachBar() {
        testButton.setOnClickListener(view -> {
            startActivity(FlightDetails.getIntent(this));
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }
}
