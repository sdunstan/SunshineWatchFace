package com.example.android.sunshine.app.sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;

public class WearableDataSender {

    private GoogleApiClient googleApiClient;

    public WearableDataSender(Context ctx) {
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.i("SunshineWatchFace", "Connected to Wearable API");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.i("SunshineWatchFace", "Connection to Wearable API has been suspended.");
                    }
                })
                .build();
        googleApiClient.connect();
    }

    private Asset createAssetFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Asset.createFromBytes(byteArrayOutputStream.toByteArray());
    }

    public void sendWeatherData(double high, double low, Bitmap bitmap) {
        Log.i("SunshineWatchFace", "SENDING DATA TO WEARABLE: " + high);

        Asset weatherIcon = createAssetFromBitmap(bitmap);

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/sunshine-weather");
        putDataMapRequest.getDataMap().putDouble("high-temp", high);
        putDataMapRequest.getDataMap().putDouble("low-temp", low);
//        putDataMapRequest.getDataMap().putAsset("weather-icon", weatherIcon);

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
    }

}
