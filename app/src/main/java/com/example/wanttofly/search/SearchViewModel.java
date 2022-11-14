package com.example.wanttofly.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanttofly.BuildConfig;
import com.example.wanttofly.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<FlightSummaryData>> trendingFlights;

    private final String FLIGHT_LABS_API_KEY = BuildConfig.FLIGHT_LABS_API_KEY;
    // Instantiate the RequestQueue.

//    String url = "https://app.goflightlabs.com/flights?access_key="+FLIGHT_LABS_API_KEY;
//
//    // Request a string response from the provided URL.
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//            response -> {
//                // Display the first 500 characters of the response string.
//                Log.d("Response is: ", response);
//            }, error -> {
//        // textView.setText("That didn't work!");
//        Log.d("Error string request:", error.toString());
//    });

    public LiveData<List<FlightSummaryData>> getTrendingFlights(Context context) {
        if (trendingFlights == null) {
            trendingFlights = new MutableLiveData<>();
            try {
                trendingFlights.setValue(this.loadTrending(context));
            } catch (IOException e) {
                Log.d("Error loading sample data: ", "check path");
            }
        }
        return trendingFlights;
    }

    private List<FlightSummaryData> loadTrending(Context context) throws IOException {
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(stringRequest);
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
