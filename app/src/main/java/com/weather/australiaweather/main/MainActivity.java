package com.weather.australiaweather.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.weather.australiaweather.R;
import com.weather.australiaweather.SharedManager;
import com.weather.australiaweather.WeatherService;
import com.weather.australiaweather.data.WeatherContract;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int WEATHER_LOADER = 0;

    // UI
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AdapterRecycler adapterRecycler;

    private SharedManager sharedManager;
    private MainPresenterImpl presenter;
    private NetworkBroadcastReceiver receiver;

    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initShared();

        DatabaseChangeListener databaseChangeListener = new DatabaseChangeListener(getApplicationContext());

        presenter = new MainPresenterImpl(
                this,
                sharedManager,
                databaseChangeListener
        );

        receiver = new NetworkBroadcastReceiver(presenter);
        Cursor c = getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI, null, null, null, null);
        getSupportLoaderManager().initLoader(WEATHER_LOADER, null, databaseChangeListener);

        relativeLayout = (RelativeLayout) findViewById(R.id.actmain_lay_rel);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.actmain_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        progressBar = (ProgressBar) findViewById(R.id.actmain_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.swipeRefreshAction();
            }
        });

        recyclerView.setHasFixedSize(true);
        adapterRecycler = new AdapterRecycler(this, c);
        recyclerView.setAdapter(adapterRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter("result"));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void showSnackbarError() {
        snackbar = Snackbar.make(relativeLayout, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.snackbarRetryAction();
                    }
                });
        snackbar.show();
    }

    @Override
    public boolean isSnackbarShown(){
        return snackbar != null? snackbar.isShown() : false;
    }

    @Override
    public void dismissSnackbar(){
        if(snackbar != null) snackbar.dismiss();
    }

    @Override
    public void updateListData(Cursor data){
        adapterRecycler.swapCursor(data);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void fetchWeatherData() {
        Intent i = new Intent(this, WeatherService.class);
        for (String city : presenter.getListCity()) {
            i.putExtra(WeatherService.CITY_EXTRA, city);
            this.startService(i);
        }
    }

    public void initShared() {
        sharedManager = SharedManager.getInstance();
        sharedManager.setSharedManager(this);
        sharedManager.setLastTimestamp(new Long(0));
    }

}
