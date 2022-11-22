package com.example.wanttofly.search;

import java.util.Random;

public class FlightSummaryData {
    String flightStatus;
    int flightRating;
    String airlineName;
    String arrivalAirport;
    String flightNumber;
    int departureDelay; // CAN BE NULL which means no delay

    int low = 60;
    int high = 100;

    public FlightSummaryData(int rating,
                             String airlineName,
                             String arrivalAirport,
                             String flightNumber,
                             String flightStatus,
                             int departureDelay) {
        this.flightRating = randomNumber();
        this.airlineName = airlineName;
        this.arrivalAirport = arrivalAirport;
        this.flightNumber = flightNumber;
        this.flightStatus = flightStatus;
        this.departureDelay = departureDelay;
    }

    private int randomNumber() {
        Random rand = new Random();
        return rand.nextInt(high - low) + low;
    }

    public String getAirlineName() {
        return this.airlineName;
    }

    public String getArrivalAirport() {
        return this.arrivalAirport;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }

    public int getFlightRating() {
        return this.flightRating;
    }

    public String getFlightStatus() {
        if (isOnTime()) {
            return FlightStatusCheck.FlightStatus.ON_TIME.toString();
        } else if (isCancelled()) {
            return FlightStatusCheck.FlightStatus.CANCELLED.toString();
        } else if (isDelayed()) {
            return FlightStatusCheck.FlightStatus.DELAYED.toString();
        }
        return this.flightStatus;
    }

    public boolean isOnTime() {
        return this.departureDelay == 0
                & !this.flightStatus.equalsIgnoreCase("cancelled");
    }

    public boolean isCancelled() {
        return this.flightStatus.equalsIgnoreCase("cancelled");
    }

    public boolean isDelayed() {
        return this.departureDelay != 0;
    }
}
