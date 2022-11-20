package com.example.wanttofly.flightdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wanttofly.R;
import com.example.wanttofly.search.SearchActivity;

import java.util.Objects;

public class FlightDetails extends AppCompatActivity {
    Toolbar myToolbar;
    TextView toolbarTitle;
    TextView flightSummaryInLine;
    TextView flightWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        flightSummaryInLine = (TextView) findViewById(R.id.dt_summary);
        flightWish = (TextView) findViewById(R.id.dt_wish);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        //Setting the title of the toolbar
        String city = "Seattle";
        String oneLineSummary = "Flight #A123FG to Seattle\nlooks good to go.";
        String wish = "We wish you safe travels.";
        toolbarTitle.setText(city);
        flightSummaryInLine.setText(oneLineSummary);
        flightWish.setText(wish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(FlightDetails.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, FlightDetails.class);
    }
}

