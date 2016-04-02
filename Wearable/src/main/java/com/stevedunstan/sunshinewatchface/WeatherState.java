package com.stevedunstan.sunshinewatchface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.wearable.Asset;

public class WeatherState {

    private Paint backgroundColor;
    private final double lowTemp;
    private final double highTemp;
    private final Asset weatherIcon;

    public WeatherState(Context ctx, double highTemp, double lowTemp, Asset weatherIcon) {
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
        if (highTemp > 1000.0)
            return "";
        return String.format("%5.1f\u00B0 %5.1f\u00B0", highTemp, lowTemp);
    }


}
