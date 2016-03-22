package com.stevedunstan.sunshinewatchface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.WindowInsets;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class SunshineSurfacePainter {
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private float xOffset;
    private float yOffset;
    private final Paint textPaint;
    private final Context ctx;
    private SimpleDateFormat dateFormatter;
    private float timeTextSize;
    private float dateTextSize;
    private float weatherTextSize;



    public SunshineSurfacePainter(Context ctx) {
        this.ctx = ctx;
        Resources resources = ctx.getResources();
        this.xOffset = 0.0f; // xOffset;
        this.yOffset = resources.getDimension(R.dimen.digital_y_offset);

        this.textPaint = createTextPaint(resources.getColor(R.color.digital_text));
        dateFormatter = new SimpleDateFormat("EEE, MMM d yyyy");
    }

    public void applyInsets(WindowInsets insets) {
        boolean isRound = insets.isRound();
        Resources resources = ctx.getResources();
        xOffset = resources.getDimension(isRound
                ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
        timeTextSize = resources.getDimension(isRound ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);
        dateTextSize = resources.getDimension(isRound ? R.dimen.digital_date_text_size_round : R.dimen.digital_date_text_size);
        weatherTextSize = resources.getDimension(isRound ? R.dimen.digital_weather_text_size_round : R.dimen.digital_weather_text_size);
    }

    public void paint(boolean isAmbient, WeatherState weather, Canvas canvas, Rect bounds, GregorianCalendar time) {
        drawBackground(isAmbient, canvas, bounds, weather.getBackgroundColor());

        time.setTime(new java.util.Date());

        drawTime(canvas, time);
        float dateYOffset = drawDate(canvas, time);
        drawWeather(canvas, dateYOffset, weather);
    }

    private void drawTime(Canvas canvas, GregorianCalendar time) {
        textPaint.setTextSize(timeTextSize);
        String text = String.format("%d:%02d", getHour(time), time.get(GregorianCalendar.MINUTE));
        canvas.drawText(text, xOffset, yOffset, textPaint);
    }

    private float drawDate(Canvas canvas, GregorianCalendar time) {
        textPaint.setTextSize(dateTextSize);
        float dateYOffset = yOffset + textPaint.getFontSpacing();
        canvas.drawText(dateFormatter.format(time.getTime()), xOffset, dateYOffset, textPaint);
        return dateYOffset;
    }

    private void drawWeather(Canvas canvas, float dateYOffset, WeatherState weather) {
        textPaint.setTextSize(weatherTextSize);
        float weatherYOffset = dateYOffset + textPaint.getFontSpacing();
        canvas.drawText(weather.getWeatherString(), xOffset, weatherYOffset, textPaint);
    }

    private void drawBackground(boolean isAmbient, Canvas canvas, Rect bounds, Paint activeBackgroundPaint) {
        if (isAmbient) {
            canvas.drawColor(Color.BLACK);
        } else {
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), activeBackgroundPaint);
        }
    }

    private int getHour(GregorianCalendar time) {
        int hour = time.get(GregorianCalendar.HOUR);
        return (hour == 0) ? 12 : hour;
    }

    private Paint createTextPaint(int textColor) {
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setAntiAlias(true);
        return paint;
    }

}
