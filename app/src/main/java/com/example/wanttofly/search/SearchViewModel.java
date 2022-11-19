package com.example.wanttofly.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchViewModel extends ViewModel {
    private final SearchRepository repository = new SearchRepository();

    private SearchTrie searchTrie = new SearchTrie();

    private final MutableLiveData<List<FlightSummaryData>> trendingFlights = new MutableLiveData<>();

    public LiveData<List<FlightSummaryData>> getTrendingFlights(InputStream backupFlightData) {
        if (trendingFlights.getValue() == null) {
            this.getFlights(backupFlightData);
        }
        return trendingFlights;
    }

    private void buildSearchTrie(List<FlightSummaryData> flights) {
        for (int i = 0; i < flights.size(); i++) {
            searchTrie.insert(flights.get(i), i);
        }
    }

    private void getFlights(InputStream backupFlightData) {
        DisposableSubscriber<JSONArray> d = new DisposableSubscriber<JSONArray>() {
            @Override
            public void onNext(JSONArray jsonArray) {
                try {
                    List<FlightSummaryData> parseJsonResponse = parseJsonResponse(jsonArray);
                    trendingFlights.setValue(parseJsonResponse);

                    buildSearchTrie(parseJsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                List<FlightSummaryData> data = loadTrending(backupFlightData);
                trendingFlights.setValue(data);
                buildSearchTrie(data);
            }

            @Override
            public void onComplete() {
                // do nothing
            }
        };

        repository.getFlightsFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d);
    }

    /**
     * Currently trending flights is the list being searched. Trending flights is created from
     * sample data due to limited API calls. If API is activated, buildSearchTrie() needs minor
     * refactor as well as this for loop
     * @param searchString
     * @return List<FlightSummaryData>
     */
    public List<FlightSummaryData> searchFlight(String searchString) {
        SearchTrie node = searchTrie.search(searchString);
        List<FlightSummaryData> results = new ArrayList<>(5);

        if (node == null) {
            return results;
        }

        Set<Integer> indexes = node.getIndexes();
        // Used to limit the number of results displayed
        int MAX_RESULTS_RETURN = 8;
        int count = 0;
        for (int i: indexes) {
            results.add(trendingFlights.getValue().get(i));
            count++;
            if (count >= MAX_RESULTS_RETURN) {
                break;
            }
        }
        return results;
    }


    private List<FlightSummaryData> loadTrending(InputStream backupFlightData) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(backupFlightData));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line);
            }

            return this.parseJsonResponse(text.toString());
        } catch (IOException e) {
            // try to make actual API query
        } catch (JSONException e) {
            // try to make actual API query
        }
        Log.d("text: ", String.valueOf(text));
        return new ArrayList<>(0);
    }

    private List<FlightSummaryData> parseJsonResponse(String flightData) throws JSONException {
        JSONArray jsonArray = new JSONArray(flightData);
        return this.parseJsonResponse(jsonArray);
    }

    private List<FlightSummaryData> parseJsonResponse(JSONArray jsonArray) throws JSONException {
        List<FlightSummaryData> data = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String flightNumber = jsonObject.getJSONObject("flight").getString("number");
            String airlineName = jsonObject.getJSONObject("airline").getString("name");
            String destination = jsonObject.getJSONObject("arrival").getString("airport");
            data.add(new FlightSummaryData(0, airlineName, destination, flightNumber));
        }

        return data;
    }
}
