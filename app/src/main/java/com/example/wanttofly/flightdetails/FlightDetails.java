package com.example.wanttofly.flightdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.wanttofly.BuildConfig;
import com.example.wanttofly.R;
import com.example.wanttofly.search.FlightStatusCheck;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlightDetails extends AppCompatActivity {
    private static final String ARG_1 = "ARG_1";
    private static final String ARG_2 = "ARG_2";
    private static final String ARG_3 = "ARG_3";
    private static final String ARG_4 = "ARG_4";
    public static ArrayList<Double> coordinates = new ArrayList<>();
    public static JSONObject WeatherDetailsJSON;
    public final String API_ID = BuildConfig.OPEN_WEATHER_API_KEY;
    public final String GEOLOCATION_BASE_URL = "https://api.openweathermap.org/geo/1.0/direct";
    public final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1/forecast";

    /**
     * To Be Modifies to get weather icons.
     */
    private static final Map<Integer, Integer> WEAHTER_ICONS = new HashMap<Integer, Integer>()
    {
        {
            put(0, R.drawable.wi_sunny);
            put(1, R.drawable.wi_sunny);
            put(2, R.drawable.wi_sunny);
            put(3, R.drawable.wi_cloudy);
            put(45, R.drawable.wi_cloudy);
            put(48, R.drawable.wi_cloudy);
            put(51, R.drawable.wi_rainshower);
            put(53, R.drawable.wi_rainshower);
            put(55, R.drawable.wi_rainshower);
            put(56, R.drawable.ic_snowyrainy);
            put(57, R.drawable.ic_snowyrainy);
            put(61, R.drawable.wi_rainy);
            put(63, R.drawable.wi_rainy);
            put(65, R.drawable.wi_rainy);
            put(66, R.drawable.ic_snowyrainy);
            put(67, R.drawable.ic_snowyrainy);
            put(71, R.drawable.ic_snowy);
            put(73, R.drawable.ic_heavysnow);
            put(75, R.drawable.ic_heavysnow);
            put(77, R.drawable.ic_heavysnow);
            put(80, R.drawable.wi_rainshower);
            put(81, R.drawable.wi_rainshower);
            put(82, R.drawable.wi_rainshower);
            put(85, R.drawable.ic_snowy);
            put(86, R.drawable.ic_snowy);
            put(95, R.drawable.wi_thunder);
            put(96, R.drawable.wi_rainythunder);
            put(99, R.drawable.wi_rainythunder);

        };
    };

    Toolbar myToolbar;
    TextView toolbarTitle;
    TextView flightSummaryInLine;
    TextView recentTweets;
    View twitterView;

    TextView destTemp;
    TextView destTempRange;
    TextView destAirport;
    TextView destWindSpeed;
    ImageView weatherIcon;
    TextView destPrecipitation;

    // Do not modify
    private String arrivalAirport;

    FlightDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        this.viewModel = new ViewModelProvider(this).get(FlightDetailsViewModel.class);

        Bundle bundle = getIntent().getExtras();
        this.arrivalAirport = bundle.getString(ARG_2);

        twitterView = findViewById(R.id.dt_twitter_view);

        createToolbar();
        assignSummaryValues();
        setupFlightRatingCard();
        setupPieChartCard();
        setupTwitterSentimentsCard();
        setupFlightRatingInfoButton();
        setupDelayAndCancellationInfoButton();

        this.arrivalAirport = this.arrivalAirport.replace(" Airport", "");
        this.arrivalAirport = this.arrivalAirport.replace(" International", "");
        this.arrivalAirport = this.arrivalAirport.trim();

        fetchAirportCoordinates(this.arrivalAirport);
    }

    private void setupDelayAndCancellationInfoButton() {
        ImageButton infoButton = findViewById(R.id.delay_cancel_info_button);
        infoButton.setOnClickListener(view -> {
            DelayAndCancelBottomSheetFragment fragment =
                    new DelayAndCancelBottomSheetFragment();
            fragment.show(getSupportFragmentManager(),
                    DelayAndCancelBottomSheetFragment.TAG);
        });
    }

    private void setupFlightRatingInfoButton() {
        ImageButton infoButton = findViewById(R.id.flight_rating_info_button);
        infoButton.setOnClickListener(view -> {
            FlightRatingInfoBottomSheetFragment fragment =
                    new FlightRatingInfoBottomSheetFragment();
            fragment.show(getSupportFragmentManager(),
                    FlightRatingInfoBottomSheetFragment.TAG);
        });
    }

    /**
     * Fetches the coordinates (longitude and latitude) for the destination Airport.
     * @param airportAddress String, destination airport address
     *
     * API call syntax:
     * http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}
     *
     */
    private void fetchAirportCoordinates(String airportAddress) {
        System.out.println("Destination Airport Address: " + airportAddress);

        String APIQuery = "?q=" + airportAddress + "&limit=1&appid=" + API_ID;

        String LocationAPIRequestURL = GEOLOCATION_BASE_URL + APIQuery;
        AsyncTaskForLocation runner = new AsyncTaskForLocation();
        runner.execute(LocationAPIRequestURL);
    }

    public class AsyncTaskForLocation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            coordinates.clear();
            RequestQueue queue = Volley.newRequestQueue(FlightDetails.this);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, strings[0], null, response -> {
                try {
                    if (response.length() == 0) {
                        coordinates.add(-123.113952);
                        coordinates.add(49.2608724);
                        fetchWeatherDetails(coordinates);
                    } else {
                        JSONObject cityCoordinates = response.getJSONObject(0);;
                        double longitude = cityCoordinates.getDouble("lon");
                        double latitude = cityCoordinates.getDouble("lat");
                        coordinates.add(longitude);
                        coordinates.add(latitude);
                        fetchWeatherDetails(coordinates);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                coordinates.add(-123.113952);
                coordinates.add(49.2608724);
                fetchWeatherDetails(coordinates);
                System.out.println("Error occurred in getting the object: " + error);
            });
            queue.add(request);
            return null;
        }
    }

    /**
     * Fetches the weather details from
     * open-mateo API for the coordinates passed into the parameters.
     * @param coordinates, [longitude, latitude]
     */
    private void fetchWeatherDetails(ArrayList<Double> coordinates) {
        double longitude = coordinates.get(0);
        double latitude = coordinates.get(1);
        String suffix = "&timezone=auto&hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_sum&current_weather=True";
        String coord = "?latitude=" + latitude + "&longitude=" + longitude;
        String weatherAPIRequestURL = WEATHER_BASE_URL + coord + suffix;

        AsyncTaskForWeather runner = new AsyncTaskForWeather();
        runner.execute(weatherAPIRequestURL);
    }

    public class AsyncTaskForWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            RequestQueue queue = Volley.newRequestQueue(FlightDetails.this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null, response -> {
                try {
                    WeatherDetailsJSON = response;
                    displayWeatherDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> System.out.println("Error occurred in getting the object: " + error));
            queue.add(request);
            return null;
        }
    }


    private void displayWeatherDetails() throws JSONException {

        System.out.println("DEFAULT: " + WeatherDetailsJSON);
        try {
            JSONObject currentWeather = WeatherDetailsJSON.getJSONObject("current_weather");
            JSONObject hourlyData = WeatherDetailsJSON.getJSONObject("hourly");
            JSONObject dailyData= WeatherDetailsJSON.getJSONObject("daily");

            int weatherCode = currentWeather.getInt("weathercode");
            weatherIcon = findViewById(R.id.wth_logo);
            int drawableID = R.drawable.wi_cloudy;
            if (WEAHTER_ICONS.get(weatherCode) != null) {
                drawableID = WEAHTER_ICONS.get(weatherCode);
            }
            weatherIcon.setImageResource(drawableID);


            destAirport = findViewById(R.id.wth_city);
            destAirport.setText(this.arrivalAirport);

            destWindSpeed = findViewById(R.id.wth_windspeed);
            String WindSpeed = currentWeather.getDouble("windspeed") + " kmh";
            destWindSpeed.setText(WindSpeed);

            destPrecipitation = findViewById(R.id.wth_precipitation);
            JSONArray dailyPrecipitation = dailyData.getJSONArray("precipitation_sum");
            String precipitation = dailyPrecipitation.optDouble(0) + " mm";
            destPrecipitation.setText(precipitation);


            destTemp = findViewById(R.id.wth_current_temperature);
            String temperatureString = currentWeather.getDouble("temperature") + "°";
            destTemp.setText(temperatureString);

            destTempRange = findViewById(R.id.wth_high_low_temp);
            JSONArray dailyMaxTemp = dailyData.getJSONArray("temperature_2m_max");
            JSONArray dailyMinTemp = dailyData.getJSONArray("temperature_2m_min");

            String tempRangeString = "H: " + dailyMaxTemp.optDouble(0) + "° L: " + dailyMinTemp.optDouble(0) + "°";
            destTempRange.setText(tempRangeString);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setupTwitterSentimentsCard() {
        recentTweets = findViewById(R.id.dt_twitter_info);
        setRecentTweetsDate();

        viewModel.getRecentTweets(this.arrivalAirport).observe(this, listOfTweets -> {
            if (listOfTweets.size() != 0) {
                recentTweets.setText(listOfTweets.get(0));
            }
            // Normally inside the above if statement, but for demo purposes always show regardless
            // if any tweets are found.
            twitterView.setVisibility(View.VISIBLE);
        });
    }

    private void setRecentTweetsDate() {
        TextView recentTweetsDate = findViewById(R.id.dt_date);
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = dateObj.format(formatter);
        recentTweetsDate.setText(date);
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

        String white = "#" + getResources().getString(R.color.white_200).substring(3);
        String backgroundColor = "#" + getResources().getString(R.color.gray_850).substring(3);
        pie.data(data);
        pie.labels().position("outside");
        pie.labels().fontColor(white);
        pie.labels().fontSize(14);
        pie.background().fill(backgroundColor, 1.0);
        pie.animation().enabled(true);

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
        pieChart.startAnimation();

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

