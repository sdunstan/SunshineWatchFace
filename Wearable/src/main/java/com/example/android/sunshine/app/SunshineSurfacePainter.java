package com.example.android.sunshine.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.WindowInsets;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class SunshineSurfacePainter {
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private float xOffset;
    private float yOffset;
    private float xCenter;
    private final Paint textPaint;
    private final Paint ambientTextPaint;
    private final Context ctx;
    private SimpleDateFormat dateFormatter;
    private float timeTextSize;
    private float dateTextSize;
    private float weatherTextSize;
    private boolean isRound = false;
    private Bitmap defaultBackgroundIcon;

    public SunshineSurfacePainter(Context ctx) {
        this.ctx = ctx;
        Resources resources = ctx.getResources();
        this.xOffset = 0.0f; // xOffset;
        this.yOffset = resources.getDimension(R.dimen.digital_y_offset);

        this.textPaint = createTextPaint(ContextCompat.getColor(ctx, R.color.digital_text));
        this.ambientTextPaint = createTextPaint(ContextCompat.getColor(ctx, R.color.digital_text_ambient));

        dateFormatter = new SimpleDateFormat("EEE, MMM d yyyy");

        Drawable defaultBackgroundMipmap = ctx.getDrawable(R.mipmap.ic_launcher);
        defaultBackgroundIcon = ((BitmapDrawable) defaultBackgroundMipmap).getBitmap();
    }

    public void applyInsets(WindowInsets insets) {
        isRound = insets.isRound();
        Resources resources = ctx.getResources();
        xOffset = resources.getDimension(isRound
                ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
        timeTextSize = resources.getDimension(isRound ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);
        dateTextSize = resources.getDimension(isRound ? R.dimen.digital_date_text_size_round : R.dimen.digital_date_text_size);
        weatherTextSize = resources.getDimension(isRound ? R.dimen.digital_weather_text_size_round : R.dimen.digital_weather_text_size);
    }

    public void paint(boolean isAmbient, WeatherState weather, Canvas canvas, Rect bounds, GregorianCalendar time) {
        xCenter = bounds.width() / 2.0f;

        drawBackground(isAmbient, canvas, bounds, weather.getBackgroundColor(), weather.getIcon());

        time.setTime(new java.util.Date());

        drawTime(getTextPaint(isAmbient), canvas, time);
        float dateYOffset = drawDate(getTextPaint(isAmbient), canvas, time);
        drawWeather(getTextPaint(isAmbient), canvas, dateYOffset, weather);
    }

    private Paint getTextPaint(boolean isAmbient) {
        return isAmbient ? ambientTextPaint : textPaint;
    }

    private float getStartForText(String text, Paint paint) {
        if (isRound) {
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            return xCenter - (bounds.width() / 2.0f);
        }
        else {
            return xOffset;
        }
    }

    private void drawTime(Paint modeAdjustedTextPaint, Canvas canvas, GregorianCalendar time) {
        modeAdjustedTextPaint.setTextSize(timeTextSize);
        String text = String.format("%d:%02d", getHour(time), time.get(GregorianCalendar.MINUTE));

        canvas.drawText(text, getStartForText(text, modeAdjustedTextPaint), yOffset, modeAdjustedTextPaint);
    }

    private float drawDate(Paint modeAdjustedTextPaint, Canvas canvas, GregorianCalendar time) {
        modeAdjustedTextPaint.setTextSize(dateTextSize);
        float dateYOffset = yOffset + modeAdjustedTextPaint.getFontSpacing();
        String text = dateFormatter.format(time.getTime());
        canvas.drawText(text, getStartForText(text, modeAdjustedTextPaint), dateYOffset, modeAdjustedTextPaint);
        return dateYOffset;
    }

    private void drawWeather(Paint modeAdjustedTextPaint, Canvas canvas, float dateYOffset, WeatherState weather) {
        modeAdjustedTextPaint.setTextSize(weatherTextSize);
        float weatherYOffset = dateYOffset + modeAdjustedTextPaint.getFontSpacing();
        String text = weather.getWeatherString();
        canvas.drawText(text, getStartForText(text, modeAdjustedTextPaint), weatherYOffset, modeAdjustedTextPaint);
    }

    private void drawBackground(boolean isAmbient, Canvas canvas, Rect bounds, Paint activeBackgroundPaint, Bitmap backgroundIcon) {
        if (isAmbient) {
            canvas.drawColor(Color.BLACK);
        } else {
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), activeBackgroundPaint);

            if (backgroundIcon == null) {
                backgroundIcon = defaultBackgroundIcon;
            }
            Rect srcRect = new Rect(0, 0, backgroundIcon.getWidth(), backgroundIcon.getHeight());
            Rect destRect = new Rect(bounds);

            if (isRound) {
                double diameter = bounds.width();
                double transcribedSide = Math.sqrt((diameter*diameter) / 2.0);
                int inset = (int) ((diameter - transcribedSide)/2.0);
                destRect.inset(inset, inset);
            }
            canvas.drawBitmap(backgroundIcon, srcRect, destRect, activeBackgroundPaint);
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
