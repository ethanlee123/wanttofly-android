package com.example.wanttofly.flightdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wanttofly.R;
import com.example.wanttofly.search.FlightStatusCheck;

import java.util.Objects;

public class FlightDetails extends AppCompatActivity {
    private static final String ARG_1 = "ARG_1";
    private static final String ARG_2 = "ARG_2";
    private static final String ARG_3 = "ARG_3";
    private static final String ARG_4 = "ARG_4";

    Toolbar myToolbar;
    TextView toolbarTitle;
    TextView flightSummaryInLine;
    TextView flightWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        createToolbar();
        assignSummaryValues();
    }

    /**
     * Creating the action menu with the back button and the title for the page.
     */
    public void createToolbar() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        myToolbar.setNavigationOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    /**
     * Assigning the values for Toolbar title, summary and wish.
     */
    public void assignSummaryValues() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        flightSummaryInLine = (TextView) findViewById(R.id.dt_summary);
        flightWish = (TextView) findViewById(R.id.dt_wish);

        Bundle bundle = getIntent().getExtras();
        String flightNumber = bundle.getString(ARG_1);
        String arrivalAirport = bundle.getString(ARG_2);
        String oneLineSummary = getOneLineSummary();
        String wish = "We wish you safe travels.";

        toolbarTitle.setText(arrivalAirport);
        flightSummaryInLine.setText(oneLineSummary);
        flightWish.setText(wish);
    }

    private String getOneLineSummary() {
        Bundle bundle = getIntent().getExtras();
        String flightNumber = bundle.getString(ARG_1);
        String arrivalAirport = bundle.getString(ARG_2);
        String flightStatus = bundle.getString(ARG_4);

        if (flightStatus.equalsIgnoreCase(FlightStatusCheck.FlightStatus.ON_TIME.toString())) {
            return getResources()
                    .getString(R.string.one_line_summary_on_time, flightNumber, arrivalAirport);
        } else if (flightStatus.equalsIgnoreCase(FlightStatusCheck.FlightStatus.DELAYED.toString())) {
            getResources()
                    .getString(R.string.one_line_summary_delayed, flightNumber, arrivalAirport);
        }
        return getResources()
                .getString(R.string.one_line_summary_cancelled, flightNumber, arrivalAirport);
    }

    public static Intent getIntent(Context context,
                                   String flightNumber,
                                   String arrivalAirport,
                                   int flightRating,
                                   String flightStatus) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_1, flightNumber);
        bundle.putString(ARG_2, arrivalAirport);
        bundle.putInt(ARG_3, flightRating);
        bundle.putString(ARG_4, flightStatus);

        Intent intent = new Intent(context, FlightDetails.class);
        intent.putExtras(bundle);

        return intent;
    }
}

