package com.example.wanttofly.flightdetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class FlightDetailsViewModel extends ViewModel {
    private final FlightDetailsRepository repository = new FlightDetailsRepository();

    private final MutableLiveData<List<String>> recentTweets = new MutableLiveData<>();

    public LiveData<List<String>> getRecentTweets(String keywords) {
        if (recentTweets.getValue() == null) {
            this.fetchRecentTweets(keywords);
        }
        return recentTweets;
    }

    public void fetchRecentTweets(String keywords) {
        DisposableSubscriber<JSONObject> d = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    List<String> parseJsonResponse = parseRecentTweetsResponse(jsonObject);
                    recentTweets.setValue(parseJsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                // do nothing
            }

            @Override
            public void onComplete() {
                // do nothing
            }
        };

        repository.getRecentTweets(keywords)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d);
    }

    private List<String> parseRecentTweetsResponse(JSONObject response) throws JSONException {
        List<String> data = new ArrayList<>(response.length());

        JSONArray dataJsonArray = response.getJSONArray("data");
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = dataJsonArray.getJSONObject(i);

            String text = jsonObject.getString("text"); // Tweet text content

            data.add(text);
        }

        return data;
    }

}
