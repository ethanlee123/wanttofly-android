package com.example.wanttofly.flightdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wanttofly.R;
import com.example.wanttofly.search.SearchActivity;

public class FlightDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, FlightDetails.class);
    }
}

