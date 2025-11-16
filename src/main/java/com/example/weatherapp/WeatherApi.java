package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // Current weather by city name
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    // Forecast by lat/lon using One Call API
    @GET("onecall")
    Call<ForecastResponse> getForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("exclude") String exclude, // e.g., "minutely,hourly,alerts"
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
