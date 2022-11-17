package com.example.wanttofly.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanttofly.BuildConfig;
import com.example.wanttofly.network.VolleySingleton;
import com.example.wanttofly.util.InstanceNotFoundException;

import org.json.JSONArray;

import java.util.List;

public class SearchRepository {
    private final String FLIGHT_LABS_API_KEY = BuildConfig.FLIGHT_LABS_API_KEY;
    private MutableLiveData<JSONArray> flights = new MutableLiveData<>();
    String url = "https://app.goflightlabs.com/flights?access_key=" + FLIGHT_LABS_API_KEY;
    JSONArray flightSummary;
    JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            response -> {
                    this.flights.setValue(response);

            }, error -> {
        this.flights.setValue(null);
        Log.d("Error string request:", error.toString());
    });

    public LiveData<JSONArray> getTrendingFlights() {

        return flights;
    }

    public void getFlights() {
        try {
            VolleySingleton.getInstance().addToRequestQueue(stringRequest);

        } catch(InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }
//    public List<FlightSummaryData> getTrendingFlights() {
//        try {
//            VolleySingleton.getInstance().addToRequestQueue(stringRequest);
//        } catch (InstanceNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

}
