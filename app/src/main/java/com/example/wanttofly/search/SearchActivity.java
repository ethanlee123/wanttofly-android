package com.example.wanttofly.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanttofly.R;
import com.example.wanttofly.advancedsearch.FilterBottomSheetFragment;
import com.example.wanttofly.flightdetails.FlightDetails;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements FlightSummaryAdapter.IOnItemClickListener {
    ImageView filterButton;
    RecyclerView rvRecents;
    RecyclerView rvTrending;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        filterButton = findViewById(R.id.iv_filter);
        rvRecents = findViewById(R.id.rv_recent);
        rvTrending = findViewById(R.id.rv_trending);

        setupFilterButton();
        setupRecyclerViewRecents();
        setupRecyclerViewTrending();
    }

    private void setupRecyclerViewTrending() {
        List<String> recentsList = new ArrayList<>();
        recentsList.add("test1");
        recentsList.add("test2");
        recentsList.add("test3");

        FlightSummaryAdapter flightSummaryAdapter = new FlightSummaryAdapter(recentsList, this);
        rvTrending.setLayoutManager(new LinearLayoutManager(this));
        rvTrending.setAdapter(flightSummaryAdapter);

        rvTrending.addItemDecoration(
                new MarginItemDecoration(
                        getResources().getDimensionPixelSize(
                                R.dimen.flight_summary_view_bottom_margin))
        );
    }

    private void setupRecyclerViewRecents() {
        List<String> recentsList = new ArrayList<>();
        recentsList.add("test1");
        recentsList.add("test2");
        recentsList.add("test3");

        FlightSummaryAdapter flightSummaryAdapter = new FlightSummaryAdapter(recentsList, this);
        rvRecents.setLayoutManager(new LinearLayoutManager(this));
        rvRecents.setAdapter(flightSummaryAdapter);

        rvRecents.addItemDecoration(
                new MarginItemDecoration(
                        getResources().getDimensionPixelSize(
                                R.dimen.flight_summary_view_bottom_margin))
        );
    }

    private void setupFilterButton() {
        filterButton.setOnClickListener(view -> {
            FilterBottomSheetFragment addPhotoBottomDialogFragment =
                    new FilterBottomSheetFragment();
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    FilterBottomSheetFragment.TAG);
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public void onItemClick() {
        startActivity(FlightDetails.getIntent(this));
    }
}
