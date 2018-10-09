package com.weather.australiaweather.utils;

import com.weather.australiaweather.model.CityWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 9/21/16.
 */

public class JSONParser {

    private static final String NODE_DATA = "data";
    private static final String NODE_CURRENT_CONDITION = "current_condition";
    private static final String NODE_TEMP_C = "temp_C";
    private static final String NODE_WEATHER_DESC = "weatherDesc";
    private static final String NODE_WEATHER_ICON_URL = "weatherIconUrl";
    private static final String NODE_FEELS_LIKE = "FeelsLikeC";
    private static final String NODE_VALUE = "value";
    private static final String NODE_PRECIPMM = "precipMM";
    private static final String NODE_REQUEST = "request";
    private static final String NODE_QUERY = "query";
    private static final String NODE_HUMIDITY = "humidity";
    private static final String NODE_WIND_SPEED_KMH = "windspeedKmph";
    private static final String NODE_PRESSURE = "pressure";

    public static CityWeather getCityFromJSON(String weatherJsonStr) throws JSONException {

        CityWeather city = new CityWeather();

        JSONObject obj = new JSONObject(weatherJsonStr);
        JSONObject o = obj.getJSONObject(NODE_DATA);

        JSONArray w = o.getJSONArray(NODE_CURRENT_CONDITION);
        JSONObject t = w.getJSONObject(0);

        //Temp
        city.setTemp(t.getInt(NODE_TEMP_C));

        //Weather description
        JSONArray weatherDescArray = t.getJSONArray(NODE_WEATHER_DESC);
        city.setWeathDesc(weatherDescArray.getJSONObject(0).getString(NODE_VALUE));

        //Weather icon url
        JSONArray weatherIconArray = t.getJSONArray(NODE_WEATHER_ICON_URL);
        city.setUrlIcon(weatherIconArray.getJSONObject(0).getString(NODE_VALUE));

        city.setFeelsLike(t.getInt(NODE_FEELS_LIKE));
        city.setHumidity(t.getInt(NODE_HUMIDITY));
        city.setWindSpeed(t.getInt(NODE_WIND_SPEED_KMH));
        city.setBarom(t.getInt(NODE_PRESSURE));
        city.setPrec(t.getDouble(NODE_PRECIPMM));

        city.setName(o.getJSONArray(NODE_REQUEST).getJSONObject(0).getString(NODE_QUERY).split(",")[0]);

        return city;
    }

}
