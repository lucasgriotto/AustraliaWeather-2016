package com.weather.australiaweather.main;

import android.database.Cursor;

/**
 * Created by lucas on 9/21/16.
 */

public interface MainView {

    void showSnackbarError();

    void hideProgress();

    void showProgress();

    void stopRefreshing();

    void showToast(String msg);

    void fetchWeatherData();

    void updateListData(Cursor cursor);

    void dismissSnackbar();

    boolean isSnackbarShown();

}
