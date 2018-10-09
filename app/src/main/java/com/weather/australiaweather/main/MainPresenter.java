package com.weather.australiaweather.main;

/**
 * Created by lucas on 9/21/16.
 */

public interface MainPresenter {

    void onResume();

    void onPause();

    void swipeRefreshAction();

    void snackbarRetryAction();

}
