package com.weather.australiaweather.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weather.australiaweather.WeatherService;

/**
 * Created by lucas on 9/21/16.
 */

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    NetworkCallback listener;

    public NetworkBroadcastReceiver(NetworkCallback listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getExtras().getInt(WeatherService.TAG_ACTION)){
            case WeatherService.ACTION_ERROR:
                listener.onError();
                break;
            case WeatherService.ACTION_SUCCESS:
                listener.onSuccess();
                break;
        }

    }

    interface NetworkCallback {
        void onError();
        void onSuccess();
    }

}
