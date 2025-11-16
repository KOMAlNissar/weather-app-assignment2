package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.VH> {

    private final List<DailyForecast> items;

    public static class VH extends RecyclerView.ViewHolder {

        TextView date, tempRange;
        ImageView icon;

        public VH(View v) {
            super(v);

            // Match EXACT xml ids
            date = v.findViewById(R.id.tvDate);
            tempRange = v.findViewById(R.id.tvForecastTemp);
            icon = v.findViewById(R.id.imgForecastIcon);
        }
    }

    public ForecastAdapter(List<DailyForecast> items) {
        this.items = items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DailyForecast f = items.get(position);

        // Format & display date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
        holder.date.setText(sdf.format(new Date(f.dt * 1000L)));

        // Temperature
        if (f.temp != null) {
            holder.tempRange.setText(
                    String.format(Locale.getDefault(), "%.0f° / %.0f°", f.temp.min, f.temp.max)
            );
        }

        // Icon
        if (f.weather != null && !f.weather.isEmpty()) {
            DailyForecast.Weather w = f.weather.get(0);
            String iconUrl = "https://openweathermap.org/img/wn/" + w.icon + "@2x.png";
            Glide.with(holder.icon.getContext()).load(iconUrl).into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
