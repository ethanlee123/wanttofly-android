package com.example.wanttofly.search;

import java.util.Random;

public class FlightSummaryData {
    String flightStatus = "On Time";
    int flightRating;
    String airlineName;
    String destination;
    String flightNumber;

    int low = 60;
    int high = 100;

    public FlightSummaryData(int rating, String airlineName, String destination, String flightNumber){
        this.flightRating = randomNumber();
        this.airlineName = airlineName;
        this.destination = destination;
        this.flightNumber = flightNumber;
    }

    private int randomNumber() {
        Random rand = new Random();
        return rand.nextInt(high - low) + low;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public void setFlightRating(int flightRating) {
        this.flightRating = flightRating;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirlineName() {
        return this.airlineName;
    }

    public String getFlightStatus() {
        return this.flightStatus;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }

    public int getFlightRating() {
        return this.flightRating;
    }
}
