package com.weather.australiaweather.model;

/**
 * Created by lucas on 9/21/16.
 */

public class CityWeather {

    private String name;
    private int temp;
    private String weathDesc;
    private int feelsLike;
    private double prec;
    private String urlIcon;
    private int humidity;
    private int barom;
    private int windSpeed;
    private byte[] icon;
    private boolean isFavourite = false;

    public String getName() {
        return name;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getWeathDesc() {
        return weathDesc;
    }

    public void setWeathDesc(String weathDesc) {
        this.weathDesc = weathDesc;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getPrec() {
        return prec;
    }

    public void setPrec(double prec) {
        this.prec = prec;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getBarom() {
        return barom;
    }

    public void setBarom(int barom) {
        this.barom = barom;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

}
