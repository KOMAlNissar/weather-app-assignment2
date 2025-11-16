package com.example.weatherapp;

import java.util.List;

public class DailyForecast {
    public long dt;
    public Temp temp;
    public List<Weather> weather;

    public static class Temp {
        public float day;
        public float min;
        public float max;
    }

    public static class Weather {
        public String description;
        public String icon;
    }
}
