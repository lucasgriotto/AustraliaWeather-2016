package com.weather.australiaweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for weather data.
 * Created by lucas on 9/21/16.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "auweather.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +

                WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeatherContract.WeatherEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_TEMP + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_DESC + " TEXT NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_FEELS_LIKE + " REAL NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_PRECIPITATION + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_ICON + " BLOB NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_PRESS + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WIND + " REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_FAV + " INTEGER DEFAULT 0);";


        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
