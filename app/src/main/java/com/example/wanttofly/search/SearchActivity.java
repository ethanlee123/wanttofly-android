package com.example.wanttofly.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    FlightSummaryAdapter searchesAdapter;
    SearchView searchView;

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
                // If the list contains the search query than filter the adapter
//                // using the filter method with the query as its argument
//                if (viewModel.list.contains(query)) {
//                    adapter.getFilter().filter(query);
//                } else {
//                    // Search query not found in List View
//                    Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_LONG).show();
//                }
                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                setTrendingRecyclerViewVisibility(View.GONE);
                List<FlightSummaryData> results = viewModel.searchFlight(newText);
//                adapter.getFilter().filter(newText);
                if (results != null) {
                    setSearchRecyclerView(View.VISIBLE);
                    searchesAdapter.updateData(results);
                    return true;
                }
                return false;
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

    private void setupObservers() {
        InputStream is = getBaseContext().getResources().openRawResource(R.raw.sample_data);

        viewModel.getTrendingFlights(is).observe(this, flightSummaryData -> {
            // flightSummaryData has more than 100 flights, we only want to display 3
            Random rand = new Random();
            int startIndex = rand.nextInt(flightSummaryData.size() - 1);
            setupRecyclerViewTrending(flightSummaryData.subList(startIndex, startIndex + 3));
        });
    }

    private void setupRecyclerViewTrending(List<FlightSummaryData> trendingFlights) {
        FlightSummaryAdapter flightSummaryAdapter = new FlightSummaryAdapter(
                getBaseContext(),
                trendingFlights,
                this
        );
        rvTrending.setLayoutManager(new LinearLayoutManager(this));
        rvTrending.setAdapter(flightSummaryAdapter);

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
                new LinkedList<FlightSummaryData>(),
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
