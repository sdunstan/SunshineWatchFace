package com.example.android.sunshine.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class WeatherState {

    private Paint backgroundColor;
    private final String lowTemp;
    private final String highTemp;
    private final Asset weatherIconAsset;
    private final WeakReference<GoogleApiClient> mGoogleApiClientWeakReference;
    private Bitmap weatherIcon;

    public WeatherState(String backgroundColorString) {
        backgroundColor = new Paint();
        backgroundColor.setColor(Color.parseColor(backgroundColorString));
        this.lowTemp = null;
        this.highTemp = null;
        this.weatherIconAsset = null;
        this.mGoogleApiClientWeakReference = null;
    }

    public WeatherState(String highTemp, String lowTemp, String backgroundColorString, Asset weatherIconAsset, WeakReference<GoogleApiClient> googleApiClientWeakReference) {
        backgroundColor = new Paint();
        backgroundColor.setColor(Color.parseColor(backgroundColorString));
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.weatherIconAsset = weatherIconAsset;
        this.mGoogleApiClientWeakReference = googleApiClientWeakReference;

        loadBitmap();
    }

    private void loadBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                if (weatherIconAsset == null) {
                    Log.i("SunshineWatchFace", "Can't convert null asset to bitmap.");
                    return;
                }
                ConnectionResult result = mGoogleApiClientWeakReference.get().blockingConnect(1500, TimeUnit.MILLISECONDS);
                if (!result.isSuccess()) {
                    Log.w("SunshineWatchFace", "Could not connect to google api to download asset.");
                    return;
                }
                InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClientWeakReference.get(), weatherIconAsset)
                        .await().getInputStream();
                if (assetInputStream == null) {
                    Log.w("SunshineWatchFace", "Requested an unknown Asset.");
                    return;
                }
                weatherIcon = BitmapFactory.decodeStream(assetInputStream);
            }
        }).start();
    }

    public Paint getBackgroundColor() {
        return backgroundColor;
    }

    public String getWeatherString() {
        if (highTemp == null)
            return "";
        return lowTemp + " " + highTemp;
    }


    public Bitmap getIcon() {
        return weatherIcon;
    }
}
