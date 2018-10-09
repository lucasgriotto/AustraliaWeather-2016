package com.weather.australiaweather.main;

import android.database.Cursor;
import android.os.AsyncTask;

import com.weather.australiaweather.SharedManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lucas on 9/21/16.
 */

public class MainPresenterImpl implements MainPresenter, NetworkBroadcastReceiver.NetworkCallback, DatabaseChangeListener.DatabaseChanged {

    //Time to update in miliseconds: 600000 miliseconds = 10 min
    private static final Long timeUpdateMiliseconds = new Long(600000);
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> listCity = Arrays.asList("Sydney", "Melbourne", "Brisbane", "Adelaide", "Perth", "Hobart", "Darwin");
    private MainView mainView;
    private SharedManager sharedManager;
    private UpdateTask updateTask;
    private DatabaseChangeListener databaseChangeListener;
    // As each city has a its own request and all of them are asynchronous, this count is to check
    // when all the requests are done. When countCity == listCity, all the requests are done.
    private int countCity = 0;
    // This is to check if some request have an error and to show errors rightly.
    private boolean hasError = false;

    public MainPresenterImpl(MainView mainView,
                             SharedManager sharedManager,
                             DatabaseChangeListener databaseChangeListener) {
        this.mainView = mainView;
        this.sharedManager = sharedManager;
        this.databaseChangeListener = databaseChangeListener;
        this.databaseChangeListener.setListener(this);
        updateTask = new UpdateTask(this);
    }

    @Override
    public void swipeRefreshAction() {
        updateTask.cancel(true);
        mainView.dismissSnackbar();
        fetchDataAndStartUpdateTimer();
    }

    @Override
    public void snackbarRetryAction() {
        mainView.showProgress();
        fetchDataAndStartUpdateTimer();
    }

    @Override
    public void onResume() {
        long subsInMiliseconds = Calendar.getInstance().getTime().getTime() - sharedManager.getLastTimestamp();
        if (subsInMiliseconds >= timeUpdateMiliseconds) {
            mainView.fetchWeatherData();
            startUpdateTimer(timeUpdateMiliseconds);
        } else {
            startUpdateTimer(timeUpdateMiliseconds - subsInMiliseconds);
        }
    }

    public void onPostExecute() {
        mainView.showToast("Updating");
        fetchDataAndStartUpdateTimer();
    }

    @Override
    public void onPause() {
        if (updateTask.getStatus() == AsyncTask.Status.RUNNING)
            updateTask.cancel(true);
    }

    public void fetchDataAndStartUpdateTimer() {
        mainView.fetchWeatherData();
        startUpdateTimer(timeUpdateMiliseconds);
    }

    public void startUpdateTimer(Long ml) {
        updateTask = new UpdateTask(this);
        updateTask.execute(ml);
    }

    public List<String> getListCity() {
        return listCity;
    }

    public void finished(){
        if(countCity == listCity.size()){
            mainView.hideProgress();
            mainView.stopRefreshing();
            if(hasError){
                mainView.showSnackbarError();
            }
            hasError = false;
            countCity = 1;
        }
    }

    @Override
    public void onLoadFinished(Cursor data) {
        mainView.updateListData(data);
    }

    @Override
    public void onLoaderReset() {
        mainView.updateListData(null);
    }

    @Override
    public void onError() {
        countCity++;
        hasError = true;
        finished();
    }

    @Override
    public void onSuccess() {
        countCity++;
        finished();
    }
}
