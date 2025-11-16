package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastUtils {

    public static List<DailyForecast> getNext3Days(List<DailyForecast> dailyList) {
        List<DailyForecast> result = new ArrayList<>();
        if (dailyList == null || dailyList.isEmpty()) return result;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());

        for (DailyForecast day : dailyList) {
            String dateStr = sdf.format(new Date(day.dt * 1000L));
            if (!dateStr.equals(todayStr)) result.add(day);
            if (result.size() >= 3) break;
        }

        int i = 0;
        while (result.size() < 3 && i < dailyList.size()) {
            if (!result.contains(dailyList.get(i))) result.add(dailyList.get(i));
            i++;
        }

        return result;
    }
}
