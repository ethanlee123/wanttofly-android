package com.example.wanttofly.search;

public class FlightStatusCheck {
    public enum FlightStatus {
        ON_TIME("On Time"),
        DELAYED("Delayed"),
        CANCELLED("Cancelled");

        private final String flightStatus;

        FlightStatus(final String flightStatus) {
            this.flightStatus = flightStatus;
        }

        @Override
        public String toString() {
            return this.flightStatus;
        }
    }

    // We can store these variables in shared preferences for persistent data.
    private Boolean isCheckedOnTime;
    private Boolean isCheckedDelayed;
    private Boolean isCheckedCancelled;

    public FlightStatusCheck(boolean isCheckedOnTime,
                             boolean isCheckedDelayed,
                             boolean isCheckedCancelled) {
        this.isCheckedOnTime = isCheckedOnTime;
        this.isCheckedDelayed = isCheckedDelayed;
        this.isCheckedCancelled = isCheckedCancelled;
    }

    public FlightStatusCheck() {
        this.isCheckedOnTime = true;
        this.isCheckedDelayed = true;
        this.isCheckedCancelled = true;
    }

    public void setIsCheckedOnTime(Boolean isChecked) {
        this.isCheckedOnTime = isChecked;
    }

    public void setIsCheckedDelayed(Boolean isChecked) {
        this.isCheckedDelayed = isChecked;
    }

    public void setIsCheckedCancelled(Boolean isChecked) {
        this.isCheckedCancelled = isChecked;
    }

    public Boolean isCheckedOnTime() {
        return this.isCheckedOnTime;
    }

    public boolean isCheckedDelayed() {
        return this.isCheckedDelayed;
    }

    public boolean isCheckedCancelled() {
        return this.isCheckedCancelled;
    }
}
