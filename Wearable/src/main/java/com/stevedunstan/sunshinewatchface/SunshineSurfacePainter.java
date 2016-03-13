package com.stevedunstan.sunshinewatchface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.WindowInsets;

import java.util.GregorianCalendar;

public class SunshineSurfacePainter {
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private float xOffset;
    private float yOffset;
    private final Paint textPaint;
    private final Context ctx;


    public SunshineSurfacePainter(Context ctx) {
        this.ctx = ctx;
        Resources resources = ctx.getResources();
        this.xOffset = 0.0f; // xOffset;
        this.yOffset = resources.getDimension(R.dimen.digital_y_offset);

        this.textPaint = createTextPaint(resources.getColor(R.color.digital_text));
    }

    public void applyInsets(WindowInsets insets) {
        boolean isRound = insets.isRound();
        Resources resources = ctx.getResources();
        xOffset = resources.getDimension(isRound
                ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
        float textSize = resources.getDimension(isRound
                ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);

        textPaint.setTextSize(textSize);
    }

    public void paint(boolean isAmbient, WeatherState weather, Canvas canvas, Rect bounds, GregorianCalendar time) {
        drawBackground(isAmbient, canvas, bounds, weather.getBackgroundColor());

        time.setTime(new java.util.Date());
        String text = String.format("%d:%02d", getHour(time), time.get(GregorianCalendar.MINUTE));
        canvas.drawText(text, xOffset, yOffset, textPaint);
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
