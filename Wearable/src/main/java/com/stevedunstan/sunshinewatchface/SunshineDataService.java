package com.stevedunstan.sunshinewatchface;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class SunshineDataService extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i("SunshineDataService", "Got data!");
        for (DataEvent dataEvent : dataEvents) {
            if(dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Log.i("SunshineDataService", "Got data changed! path is " + dataItem.getUri().getPath());
                if (dataItem.getUri().getPath().equals("/sunshine-weather")) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    double highTemp = dataMap.getDouble("high-temp");
                    double lowTemp = dataMap.getDouble("low-temp");
                    Asset weatherIconAsset = dataMap.getAsset("weather-icon");
                    Log.i("SunshineDataService", "High temp is " + highTemp);
                    sendData(highTemp, lowTemp, weatherIconAsset);
                }
            }
        }
    }

    private void sendData(double high, double low, Asset image) {
        Intent sunshineDataIntent = new Intent("sunshine-data-event");
        sunshineDataIntent.putExtra("high-temp", high);
        sunshineDataIntent.putExtra("low-temp", low);
        sunshineDataIntent.putExtra("image", image);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(sunshineDataIntent);
    }

}
