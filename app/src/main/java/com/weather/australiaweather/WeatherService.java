package com.weather.australiaweather;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.weather.australiaweather.data.WeatherContract;
import com.weather.australiaweather.model.CityWeather;
import com.weather.australiaweather.utils.JSONParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lucas on 9/21/16.
 */

public class WeatherService extends IntentService {

    public static String API_KEY;
    public static final String CITY_EXTRA = "city_exta";
    public static final String TAG_ACTION = "Action";
    public static final int ACTION_ERROR = 0;
    public static final int ACTION_SUCCESS = 1;
    public final String TAG = this.getClass().getSimpleName();
    private NetworkInfo netInfo;
    private ConnectivityManager cm;
    private LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
    private CityWeather cityWeather;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        API_KEY = getResources().getString(R.string.API_KEY);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            sendResult(ACTION_ERROR);
            return;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String city_name;
        if (intent.getExtras() != null) {
            city_name = intent.getExtras().getString(WeatherService.CITY_EXTRA);
        } else {
            Log.i(TAG, "Please check your internet connection");
            sendResult(ACTION_ERROR);
            return;
        }

        try {

            final String BASE_URL = "http://api.worldweatheronline.com/premium/v1/weather.ashx";
            final String CITY_PARAM = "q";
            final String FORMAT_PARAM = "format";
            final String KEY_PARAM = "key";

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(CITY_PARAM, city_name)
                    .appendQueryParameter(FORMAT_PARAM, "json")
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .build();


            URL url = new URL(uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                sendResult(ACTION_ERROR);
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                sendResult(ACTION_ERROR);
                return;
            }

            cityWeather = JSONParser.getCityFromJSON(buffer.toString());

            ContentValues cityValues = new ContentValues();
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_CITY, cityWeather.getName());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_TEMP, cityWeather.getTemp());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESC, cityWeather.getWeathDesc());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_FEELS_LIKE, cityWeather.getFeelsLike());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_PRECIPITATION, cityWeather.getPrec());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, new SimpleDateFormat("hh:mm a").format(new Date()));

            Bitmap icon = getBitmapFromURL(cityWeather.getUrlIcon());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();

            cityValues.put(WeatherContract.WeatherEntry.COLUMN_ICON, bArray);
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, cityWeather.getHumidity());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_PRESS, cityWeather.getBarom());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_WIND, cityWeather.getWindSpeed());
            cityValues.put(WeatherContract.WeatherEntry.COLUMN_FAV, isFavorite(cityWeather));

            //Try to update city row
            int updatedRows = getContentResolver().update(WeatherContract.WeatherEntry.CONTENT_URI, cityValues, WeatherContract.WeatherEntry.COLUMN_CITY + "=?", new String[]{cityWeather.getName()});
            if (updatedRows == 0) {
                getContentResolver().insert(WeatherContract.WeatherEntry.CONTENT_URI, cityValues);
            }
            sendResult(ACTION_SUCCESS);

        } catch (IOException e) {
            Log.i(TAG, "Could not read from API, perhaps your API_KEY quota expired? ");
            sendResult(ACTION_ERROR);
            return;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            sendResult(ACTION_ERROR);
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int isFavorite(CityWeather city) {
        Cursor c = getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI, new String[]{WeatherContract.WeatherEntry.COLUMN_FAV}, WeatherContract.WeatherEntry.COLUMN_CITY + "=?", new String[]{city.getName()}, null);
        if (c != null && c.getCount() == 0) {
            return 0;
        }

        if (c != null && c.getCount() > 1) {
            Log.e(TAG, "There should not be more than one city with the same name!");
        }

        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i;
    }

    public void sendResult(int action) {
        Intent intent = new Intent("result");
        intent.putExtra(TAG_ACTION, action);
        broadcaster.sendBroadcast(intent);
    }

}
