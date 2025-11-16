package com.example.weatherapp;

import java.util.List;

public class ForecastResponse {
    public double lat;
    public double lon;
    public String timezone;
    public List<DailyForecast> daily;
}
