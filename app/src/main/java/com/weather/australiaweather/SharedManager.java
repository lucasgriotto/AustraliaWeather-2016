package com.weather.australiaweather;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lucas on 9/21/16.
 */

public class SharedManager {

    private SharedPreferences pref;
    private static SharedManager sharedManager = null;
    private SharedPreferences.Editor editor;

    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "australiaWeather";
    private static final String LAST_TIMESTAMP = "lastTS";

    private SharedManager() {
    }

    public static SharedManager getInstance() {
        if (sharedManager == null)
            sharedManager = new SharedManager();
        return sharedManager;
    }

    public void setSharedManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLastTimestamp(long lastTimestamp) {
        editor.putLong(LAST_TIMESTAMP, lastTimestamp);
        editor.commit();
    }

    public long getLastTimestamp() {
        return pref.getLong(LAST_TIMESTAMP, 0);
    }

}
