package com.example.weatherapp;

import java.util.List;

public class CurrentWeatherResponse {
    public Coord coord;
    public Main main;
    public Wind wind;
    public List<Weather> weather;
    public String name; // city name

    public static class Coord {
        public double lon;
        public double lat;
    }

    public static class Main {
        public float temp;
        public int humidity;
    }

    public static class Wind {
        public float speed;
    }

    public static class Weather {
        public String description;
        public String icon;
    }
}
