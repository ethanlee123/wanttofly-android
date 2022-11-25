package com.example.wanttofly.flightdetails;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.wanttofly.BuildConfig;
import com.example.wanttofly.network.VolleySingleton;
import com.example.wanttofly.util.InstanceNotFoundException;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;

public class FlightDetailsRepository {
    private final String FLIGHT_LABS_API_KEY = BuildConfig.TWITTER_BEARER_TOKEN;
    private final String BASE_URL = "https://api.twitter.com/2/tweets/search/recent?query=%s";
    /**
     * Get 10 (default) recents tweets based on keywords param.
     *
     * @return JSONObject, unparsed response object.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws InstanceNotFoundException VolleySingleton instance does not exist
     */
    private JSONObject fetchRecentTweets(String keywords)
            throws ExecutionException, InterruptedException, InstanceNotFoundException {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = String.format(BASE_URL, keywords);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                future,
                future) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + FLIGHT_LABS_API_KEY);
                return headers;
            }
        };

        VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
        return future.get();
    }

    public Flowable<JSONObject> getRecentTweets(String keywords) {
        return Flowable.defer(new Callable<Publisher<? extends JSONObject>>() {
            @Override
            public Publisher<? extends JSONObject> call() throws Exception {
                return Flowable.just(fetchRecentTweets(keywords));
            }
        });
    }
}
