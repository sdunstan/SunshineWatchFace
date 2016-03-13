package com.stevedunstan.sunshinewatchface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;

/**
 * CDGB Created by steve on 3/13/16.
 */
public class WeatherState {

    private Paint backgroundColor;

    public WeatherState(Context ctx) {
        Resources resources = ctx.getResources();
        backgroundColor = new Paint();
        backgroundColor.setColor(resources.getColor(R.color.background));
    }

    public Paint getBackgroundColor() {
        return backgroundColor;
    }
}
