package com.example.wanttofly.flightdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.wanttofly.R;
import com.example.wanttofly.search.FlightStatusCheck;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlightDetails extends AppCompatActivity {
    private static final String ARG_1 = "ARG_1";
    private static final String ARG_2 = "ARG_2";
    private static final String ARG_3 = "ARG_3";
    private static final String ARG_4 = "ARG_4";

    Toolbar myToolbar;
    TextView toolbarTitle;
    TextView flightSummaryInLine;

    // Do not modify
    private String arrivalAirport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        Bundle bundle = getIntent().getExtras();
        this.arrivalAirport = bundle.getString(ARG_2);

        createToolbar();
        assignSummaryValues();
        setupFlightRatingCard();
        setupPieChartCard();
    }

    @SuppressLint("ResourceType")
    private void setupPieChartCard() {
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        Pie pie = AnyChart.pie();

        Bundle bundle = getIntent().getExtras();
        int flightRating = bundle.getInt(ARG_3);

        List<DataEntry> data = new ArrayList<>(3);
        data.add(new ValueDataEntry("On Time", flightRating*1.5));
        data.add(new ValueDataEntry("Delay", (100-flightRating)/2));
        data.add(new ValueDataEntry("Cancel", (100-flightRating)/2));

        pie.data(data);
        pie.labels().position("outside");
        String white = "#" + getResources().getString(R.color.white_200).substring(3);
        pie.labels().fontColor(white);
        pie.labels().fontSize(18);
        String backgroundColor = "#" + getResources().getString(R.color.gray_850).substring(3);
        pie.background().fill(backgroundColor, 1.0);

        anyChartView.setChart(pie);
    }

    @SuppressLint("ResourceType")
    private void setupFlightRatingCard() {
        Bundle bundle = getIntent().getExtras();
        int flightRating = bundle.getInt(ARG_3);
        PieChart pieChart = findViewById(R.id.donut_chart);
        TextView rating = findViewById(R.id.tv_flight_score);
        TextView ratingInfo = findViewById(R.id.tv_status);

        String donutColorMain = getResources().getString(R.color.donut_chart_main);
        String donutColorFill = getResources().getString(R.color.donut_chart_fill);
        pieChart.addPieSlice(new PieModel("", flightRating,
                Color.parseColor(donutColorMain)));
        pieChart.addPieSlice(new PieModel("", 100-flightRating,
                Color.parseColor(donutColorFill)));

        rating.setText(String.valueOf(flightRating));
        ratingInfo.setText(getRatingBlurb());
    }

    private String getRatingBlurb() {
        Bundle bundle = getIntent().getExtras();
        String flightStatus = bundle.getString(ARG_4);
        int flightRating = bundle.getInt(ARG_3);

        if (flightStatus.equalsIgnoreCase(FlightStatusCheck.FlightStatus.DELAYED.toString())) {
            return getResources().getString(R.string.low_flight_score);
        } else if (flightStatus.equalsIgnoreCase(FlightStatusCheck.FlightStatus.CANCELLED.toString())) {
            return getResources().getString(R.string.cancelled_flight_score);
        }

        if (flightRating > 80) {
            return getResources().getString(R.string.very_high_flight_score);
        } else if (flightRating > 60) {
            return getResources().getString(R.string.high_flight_score);
        }
        return getResources().getString(R.string.low_flight_score);
    }

    /**
     * Creating the action menu with the back button and the title for the page.
     */
    public void createToolbar() {
        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        myToolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    /**
     * Assigning the values for Toolbar title, summary and wish.
     */
    public void assignSummaryValues() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        flightSummaryInLine = findViewById(R.id.dt_summary);

        String oneLineSummary = getOneLineSummary();

        toolbarTitle.setText(this.arrivalAirport);
        flightSummaryInLine.setText(oneLineSummary);
    }

    private String getOneLineSummary() {
        Bundle bundle = getIntent().getExtras();
        String flightNumber = bundle.getString(ARG_1);
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

