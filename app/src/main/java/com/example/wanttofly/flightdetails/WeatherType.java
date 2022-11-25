package com.example.wanttofly.flightdetails;

import java.time.LocalDateTime;

public class WeatherType {
    private final String weatherDesc;
    private final int iconRes;
    private LocalDateTime time;
    private Double temperatureCelsius;
    private Double pressure;
    private Double windSpeed;
    private Double humidity;
    private Double weatherType;

    public WeatherType(String weatherDesc, int iconRes) {
        this.weatherDesc = weatherDesc;
        this.iconRes = iconRes;
    }

    public final String getWeatherDesc() {
        return this.weatherDesc;
    }
    public final int getIconRes() {
        return this.iconRes;
    }

}
