package com.example.wanttofly.search;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.wanttofly.R;
import com.example.wanttofly.advancedsearch.FilterBottomSheetFragment;
import com.example.wanttofly.flightdetails.FlightDetails;

public class SearchActivity extends AppCompatActivity {
    Button testButton = null;
    ImageView filterButton = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        testButton = findViewById(R.id.b_testing);
        filterButton = findViewById(R.id.iv_filter);
        setupTestButton();
        setupFilterButton();
    }

    private void setupFilterButton() {
        filterButton.setOnClickListener(view -> {
            FilterBottomSheetFragment addPhotoBottomDialogFragment =
                    new FilterBottomSheetFragment();
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    FilterBottomSheetFragment.TAG);
        });
    }

    private void setupTestButton() {
        testButton.setOnClickListener(view -> {
            startActivity(FlightDetails.getIntent(this));
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }
}
