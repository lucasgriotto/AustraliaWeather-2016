package com.weather.australiaweather.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.weather.australiaweather.data.WeatherContract;

/**
 * Created by lucas on 9/21/16.
 */

public class DatabaseChangeListener implements LoaderManager.LoaderCallbacks<Cursor>{

    Context context;
    DatabaseChanged databaseChanged;

    public DatabaseChangeListener(Context context){
        this.context = context;
    }

    public void setListener(DatabaseChanged databaseChanged){
        this.databaseChanged = databaseChanged;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, WeatherContract.WeatherEntry.CONTENT_URI, AdapterRecycler.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        databaseChanged.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        databaseChanged.onLoaderReset();
    }

    interface DatabaseChanged {
        void onLoadFinished(Cursor data);
        void onLoaderReset();
    }

}
