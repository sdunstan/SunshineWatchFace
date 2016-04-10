package com.example.android.sunshine.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;

public class WeatherState {

    private Paint backgroundColor;
    private final String lowTemp;
    private final String highTemp;
    private final Bitmap weatherIcon;

    public WeatherState(Context ctx, String highTemp, String lowTemp, Bitmap weatherIcon) {
        Resources resources = ctx.getResources();
        backgroundColor = new Paint();
        backgroundColor.setColor(resources.getColor(R.color.background));
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.weatherIcon = weatherIcon;
        Log.d("WeatherState", "CREATED WEATHER STATE FOR HIGH TEMP: " + this.highTemp);
    }

    public Paint getBackgroundColor() {
        return backgroundColor;
    }

    public String getWeatherString() {
        if (highTemp == null)
            return "";
        return String.format("%s\u00B0 %s\u00B0", highTemp, lowTemp);
    }


    public Bitmap getIcon() {
        return weatherIcon;
    }
}
