package com.example.wanttofly.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanttofly.R;
import com.example.wanttofly.advancedsearch.FilterBottomSheetFragment;
import com.example.wanttofly.flightdetails.FlightDetails;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class SearchActivity extends AppCompatActivity
        implements FlightSummaryAdapter.IOnItemClickListener {
    ImageView filterButton;
    TextView tvRecents;
    TextView tvTrending;
    RecyclerView rvRecents;
    RecyclerView rvTrending;
    RecyclerView rvSearches;
    SearchView searchView;
    LinearLayout llNoResultsFound;

    FlightSummaryAdapter trendingFlightsAdapter;
    FlightSummaryAdapter searchesAdapter;
    SearchViewModel viewModel;

    Queue<FlightSummaryData> recentSearches = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        setupObservers();

        filterButton = findViewById(R.id.iv_filter);
        rvRecents = findViewById(R.id.rv_recent);
        rvTrending = findViewById(R.id.rv_trending);
        rvSearches = findViewById(R.id.rv_searches);
        tvRecents = findViewById(R.id.tv_recent);
        tvTrending = findViewById(R.id.tv_trending);
        searchView = findViewById(R.id.sv_search_bar);
        llNoResultsFound = findViewById(R.id.layout_no_results_found);

        setupSearchView();
        setupRecentSearches();
        setupFilterButton();
        setupRecyclerViewSearches();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                setTrendingRecyclerViewVisibility(View.GONE);
                List<FlightSummaryData> results = viewModel.searchFlight(newText);

                searchesAdapter.updateData(results);

                if (searchesAdapter.flightSummariesList.size() == 0) {
                    setSearchRecyclerView(View.GONE);
                    setNoResultsFoundViewVisibility(View.VISIBLE);
                } else {
                    setSearchRecyclerView(View.VISIBLE);
                    setNoResultsFoundViewVisibility(View.GONE);
                }

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && searchesAdapter.flightSummariesList.size() == 0) {
                setNoResultsFoundViewVisibility(View.GONE);
                setTrendingRecyclerViewVisibility(View.VISIBLE);
            }
        });
    }

    private void setTrendingRecyclerViewVisibility(int visibility) {
        tvTrending.setVisibility(visibility);
        rvTrending.setVisibility(visibility);
    }

    private void setSearchRecyclerView(int visibility) {
        rvSearches.setVisibility(visibility);
    }

    private void setNoResultsFoundViewVisibility(int visibility) {
        llNoResultsFound.setVisibility(visibility);
    }

    private void setupObservers() {
        InputStream is = getBaseContext().getResources().openRawResource(R.raw.sample_data);

        viewModel.getTrendingFlights(is).observe(this, flightSummaryData -> {
            // flightSummaryData has more than 100 flights, we only want to display 3
            Random rand = new Random();
            int startIndex = rand.nextInt(flightSummaryData.size() - 1);
            setupRecyclerViewTrending(flightSummaryData.subList(startIndex, startIndex + 3));
        });

        viewModel.getSearchedFlights().observe(this, flightSummaryData -> {
            if (flightSummaryData != null) {
                searchesAdapter.updateData(flightSummaryData);
            }
        });
    }

    private void setupRecyclerViewTrending(List<FlightSummaryData> trendingFlights) {
        trendingFlightsAdapter = new FlightSummaryAdapter(
                getBaseContext(),
                trendingFlights,
                this
        );
        rvTrending.setLayoutManager(new LinearLayoutManager(this));
        rvTrending.setAdapter(trendingFlightsAdapter);

        rvTrending.addItemDecoration(
                new MarginItemDecoration(
                        getResources().getDimensionPixelSize(
                                R.dimen.flight_summary_view_bottom_margin))
        );
    }

    /**
     * Initialize recycler view with empty dataset.
     */
    private void setupRecyclerViewSearches() {
        this.searchesAdapter = new FlightSummaryAdapter(
                getBaseContext(),
                new LinkedList<>(),
                this
        );
        rvSearches.setLayoutManager(new LinearLayoutManager(this));
        rvSearches.setAdapter(this.searchesAdapter);
        rvSearches.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(
                                R.dimen.flight_summary_view_bottom_margin)));
    }

    private void setupRecentSearches() {
        // Show recycler view and Recents title if there are any recent searches
        if (recentSearches.size() == 0) {
            tvRecents.setVisibility(View.GONE);
            rvRecents.setVisibility(View.GONE);
            return;
        }

        setupRecentsRecyclerView();
    }

    private void setupRecentsRecyclerView() {
        FlightSummaryAdapter flightSummaryAdapter = new FlightSummaryAdapter(
                getBaseContext(),
                recentSearches,
                this
        );
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
