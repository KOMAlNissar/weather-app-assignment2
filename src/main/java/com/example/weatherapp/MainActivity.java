package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private WeatherApi api;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "c3f631a7b6bf644adcf9bb87b2b961ff"; // WeatherAppKey
    private static final String PREFS = "weather_prefs";
    private static final String KEY_LAST_CITY = "last_city";

    private android.widget.EditText etCity;
    private Button btnSearch;
    private ProgressBar progress;
    private TextView tvError;
    private TextView tvCity, tvDesc, tvTemp, tvDetails;
    private ImageView ivIcon;
    private CardView cardCurrent;
    private RecyclerView rvForecast;
    private TextView tvForecastLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.et_city);
        btnSearch = findViewById(R.id.btn_search);
        progress = findViewById(R.id.progress);
        tvError = findViewById(R.id.tv_error);
        tvCity = findViewById(R.id.tv_city);
        tvDesc = findViewById(R.id.tv_description);
        tvTemp = findViewById(R.id.tv_temp);
        tvDetails = findViewById(R.id.tv_details);
        ivIcon = findViewById(R.id.iv_icon);
        cardCurrent = findViewById(R.id.card_current);
        rvForecast = findViewById(R.id.rv_forecast);
        tvForecastLabel = findViewById(R.id.tv_forecast_label);

        rvForecast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(WeatherApi.class);

        // Load last city
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String lastCity = prefs.getString(KEY_LAST_CITY, "");
        if (!TextUtils.isEmpty(lastCity)) {
            etCity.setText(lastCity);
            fetchWeather(lastCity);
        }

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (!TextUtils.isEmpty(city)) {
                fetchWeather(city);
            } else {
                etCity.setError("Enter a city");
            }
        });

        etCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                btnSearch.performClick();
                return true;
            }
            return false;
        });
    }

    private void fetchWeather(String city) {
        showLoading(true);
        tvError.setVisibility(TextView.GONE);

        api.getCurrentWeather(city, API_KEY, "metric").enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrentWeatherResponse cw = response.body();
                    displayCurrent(cw);

                    // Save last city
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_LAST_CITY, city).apply();

                    // Fetch forecast using lat/lon
                    fetchForecast(cw.coord.lat, cw.coord.lon);
                } else {
                    showError("City not found or API error (current).");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void fetchForecast(double lat, double lon) {
        api.getForecast(lat, lon, "minutely,hourly,alerts", API_KEY, "metric").enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<DailyForecast> daily = ForecastUtils.getNext3Days(response.body().daily);
                    if (daily != null && daily.size() > 0) {
                        rvForecast.setAdapter(new ForecastAdapter(daily));
                        rvForecast.setVisibility(RecyclerView.VISIBLE);
                        tvForecastLabel.setVisibility(TextView.VISIBLE);
                    } else {
                        rvForecast.setVisibility(RecyclerView.GONE);
                        tvForecastLabel.setVisibility(TextView.GONE);
                    }
                } else {
                    tvError.setText("Forecast API error.");
                    tvError.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                showLoading(false);
                tvError.setText("Network error: " + t.getMessage());
                tvError.setVisibility(TextView.VISIBLE);
            }
        });
    }

    private void displayCurrent(CurrentWeatherResponse cw) {
        cardCurrent.setVisibility(android.view.View.VISIBLE);
        tvCity.setText(cw.name);
        if (cw.weather != null && cw.weather.size() > 0) {
            tvDesc.setText(cw.weather.get(0).description);
            String icon = cw.weather.get(0).icon;
            String url = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
            Glide.with(this).load(url).into(ivIcon);
        }
        tvTemp.setText(String.format(java.util.Locale.getDefault(), "%.0f°C", cw.main.temp));
        tvDetails.setText("Humidity: " + cw.main.humidity + "% • Wind: " + cw.wind.speed + " m/s");
    }

    private void showError(String message) {
        showLoading(false);
        cardCurrent.setVisibility(android.view.View.GONE);
        rvForecast.setVisibility(RecyclerView.GONE);
        tvForecastLabel.setVisibility(TextView.GONE);
        tvError.setText(message);
        tvError.setVisibility(TextView.VISIBLE);
    }

    private void showLoading(boolean loading) {
        progress.setVisibility(loading ? ProgressBar.VISIBLE : ProgressBar.GONE);
        if (loading) tvError.setVisibility(TextView.GONE);
    }
}
