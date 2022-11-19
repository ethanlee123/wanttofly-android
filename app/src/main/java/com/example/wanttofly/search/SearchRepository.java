package com.example.wanttofly.search;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.wanttofly.BuildConfig;
import com.example.wanttofly.network.VolleySingleton;
import com.example.wanttofly.util.InstanceNotFoundException;

import org.json.JSONArray;
import org.reactivestreams.Publisher;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;

public class SearchRepository {
    private final String FLIGHT_LABS_API_KEY = BuildConfig.FLIGHT_LABS_API_KEY;

    /**
     * Get basic flight data (see sample_data.txt) from Go Flight Labs. ***LIMITED TO 100 CALLS***
     * Converts async. volley request into a sync. future because RxJava observables require
     * blocking/sync functions.
     * @return JSONArray flight data, see sample_data.txt
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws InstanceNotFoundException VolleySingleton instance does not exist
     */
    private JSONArray getFlights()
            throws ExecutionException, InterruptedException, InstanceNotFoundException {
        throw new InstanceNotFoundException("Intentional error: 100 API call limit");
        // Code commented out due to 100 API call LIMIT
//        RequestFuture<JSONArray> future = RequestFuture.newFuture();
//        String url = "https://app.goflightlabs.com/flights?access_key=" + FLIGHT_LABS_API_KEY;
//        JsonArrayRequest stringRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                future,
//                future);
//
//        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
//        return future.get();
    }

    public Flowable<JSONArray> getFlightsFlowable() {
        return Flowable.defer(new Callable<Publisher<? extends JSONArray>>() {
            @Override
            public Publisher<? extends JSONArray> call() throws Exception {
                return Flowable.just(getFlights());
            }
        });
    }
}
