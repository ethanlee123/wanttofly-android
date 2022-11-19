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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchViewModel extends ViewModel {
    private final SearchRepository repository = new SearchRepository();

    private final int MAX_FLIGHTS_TO_SHOW = 3;
    private final MutableLiveData<List<FlightSummaryData>> trendingFlights = new MutableLiveData<>();
//    private final MutableLiveData<List<FlightSummaryData>> allFlights = new MutableLiveData<>();

    public LiveData<List<FlightSummaryData>> getTrendingFlights(InputStream backupFlightData) {
        this.getFlights(backupFlightData);
        return trendingFlights;
    }

    private void getFlights(InputStream backupFlightData) {
        // disposable that will be used to subscribe
        DisposableSubscriber<JSONArray> d = new DisposableSubscriber<JSONArray>() {
            @Override
            public void onNext(JSONArray jsonArray) {
                try {
                    List<FlightSummaryData> parseJsonResponse = parseJsonResponse(jsonArray);
                    trendingFlights.setValue(parseJsonResponse);

                    // set all Flights to same as trending flights
                    // if time permits separate this logic
//                    allFlights.setValue(parseJsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                trendingFlights.setValue(loadTrending(backupFlightData));
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

    public List<FlightSummaryData> searchFlight(String searchString) {
        if (searchString.length() < 3) {
            return null;
        }

        return trendingFlights.getValue().subList(1, 4);
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
