package com.example.wanttofly.flightdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wanttofly.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FlightWeatherInfoBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "FlightWeatherInfoBottomSheetFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_weather_info, container, false);
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }
}
