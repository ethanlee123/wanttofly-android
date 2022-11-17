package com.example.wanttofly.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanttofly.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final SearchRepository repository = new SearchRepository();

    private final int MAX_FLIGHTS_TO_SHOW = 3;
    private MutableLiveData<List<FlightSummaryData>> trendingFlights = new MutableLiveData<>();

    public LiveData<List<FlightSummaryData>> getTrendingFlights(Context context) {
            repository.getFlights();

            repository.getTrendingFlights().observe((LifecycleOwner) context, flightSummaryData -> {
                List<FlightSummaryData> parseJsonResponse = null;

                if (flightSummaryData == null) {
                    try {
                        trendingFlights.setValue(this.loadTrending(context));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    parseJsonResponse = parseJsonResponse(flightSummaryData);
                    trendingFlights.setValue(parseJsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        return trendingFlights;
    }

    private List<FlightSummaryData> loadTrending(Context context) throws IOException {
        StringBuilder text = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.sample_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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

        for (int i = 0; i < MAX_FLIGHTS_TO_SHOW; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String flightNumber = jsonObject.getJSONObject("flight").getString("number");
            String airlineName = jsonObject.getJSONObject("airline").getString("name");
            String destination = jsonObject.getJSONObject("arrival").getString("airport");
            data.add(new FlightSummaryData(0, airlineName, destination, flightNumber));
        }

        return data;
    }
}
