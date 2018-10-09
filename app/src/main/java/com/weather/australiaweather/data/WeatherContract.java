package com.weather.australiaweather.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Specifies the structure of the database (tables and columns)
 * Created by lucas on 9/21/16.
 */

public class WeatherContract {
    public static final String CONTENT_AUTHORITY = "com.weather.australiaweather.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_DESC = "description";
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_FEELS_LIKE = "feels";
        public static final String COLUMN_PRECIPITATION = "precipitation";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WIND = "wind";
        public static final String COLUMN_PRESS = "pressure";
        public static final String COLUMN_FAV = "favourite";


        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
