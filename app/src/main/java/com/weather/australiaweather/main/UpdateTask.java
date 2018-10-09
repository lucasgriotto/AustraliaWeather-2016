package com.weather.australiaweather.main;

import android.os.AsyncTask;

/**
 * Created by lucas on 9/21/16.
 */

public class UpdateTask extends AsyncTask<Long, Void, Void> {

    MainPresenterImpl presenter;

    public UpdateTask(MainPresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    protected Void doInBackground(Long... params) {
        try {
            Thread.sleep(params[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        presenter.onPostExecute();
    }
}
