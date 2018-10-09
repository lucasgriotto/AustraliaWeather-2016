package com.weather.australiaweather.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weather.australiaweather.R;
import com.weather.australiaweather.data.WeatherContract;
import com.weather.australiaweather.model.CityWeather;

/**
 * Created by lucas on 9/21/16.
 */

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.CityViewHolder> {

    private Cursor dataCursor;
    private Context context;

    public static final int _ID = 0;
    public static final int CITY_INDEX = 1;
    public static final int TEMP_INDEX = 2;
    public static final int WEATHER_DESC_INDEX = 3;
    public static final int FEELS_LIKE_INDEX = 4;
    public static final int PRECIPITATION_INDEX = 5;
    public static final int DATE_INDEX = 6;
    public static final int ICON_INDEX = 7;
    public static final int HUMIDITY_INDEX = 8;
    public static final int PRESS_INDEX = 9;
    public static final int WIND_INDEX = 10;
    public static final int FAV_INDEX = 11;

    public static String[] PROJECTION = {
            WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_CITY,
            WeatherContract.WeatherEntry.COLUMN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_DESC,
            WeatherContract.WeatherEntry.COLUMN_FEELS_LIKE,
            WeatherContract.WeatherEntry.COLUMN_PRECIPITATION,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_ICON,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESS,
            WeatherContract.WeatherEntry.COLUMN_WIND,
            WeatherContract.WeatherEntry.COLUMN_FAV

    };

    public AdapterRecycler(Context context, Cursor cursor) {
        this.context = context;
        dataCursor = cursor;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather, parent, false);
        CityViewHolder cityViewHolder = new CityViewHolder(v);
        return cityViewHolder;
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, final int position) {

        dataCursor.moveToPosition(position);
        holder.txtCityName.setText(dataCursor.getString(CITY_INDEX));
        holder.txtTemp.setText(String.format("%s°", dataCursor.getString(TEMP_INDEX)));
        holder.txtWeathDesc.setText(dataCursor.getString(WEATHER_DESC_INDEX));
        holder.txtFeelsLike.setText(String.format("%s°", dataCursor.getString(FEELS_LIKE_INDEX)));
        holder.txtPrec.setText(String.format("%smm", dataCursor.getString(PRECIPITATION_INDEX)));
        holder.txtObservedAt.setText(dataCursor.getString(DATE_INDEX));

        byte[] byteArray = dataCursor.getBlob(ICON_INDEX);
        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
        holder.imgWeatherIcon.setImageDrawable(bd);

        if (dataCursor.getInt(FAV_INDEX) == 1) {
            holder.layout.setBackgroundResource(R.drawable.favouriterowback);
            holder.layFavInfo.setVisibility(View.VISIBLE);
            holder.txtHumidity.setText(String.format("%s%%", dataCursor.getString(HUMIDITY_INDEX)));
            holder.txtBarom.setText(String.format("%s", dataCursor.getString(PRESS_INDEX)));
            holder.txtWindSpeed.setText(String.format("%skm/h", dataCursor.getString(WIND_INDEX)));
        } else {
            holder.layout.setBackgroundResource(R.drawable.standardrowback);
            holder.layFavInfo.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityWeather city = getItem(position);
                if (city.isFavourite()) {
                    ContentValues favoriteValue = new ContentValues();
                    favoriteValue.put(WeatherContract.WeatherEntry.COLUMN_FAV, 0);
                    context.getContentResolver().update(WeatherContract.WeatherEntry.CONTENT_URI, favoriteValue, WeatherContract.WeatherEntry.COLUMN_CITY + "=?", new String[]{city.getName()});
                } else {
                    ContentValues favoriteValue = new ContentValues();
                    favoriteValue.put(WeatherContract.WeatherEntry.COLUMN_FAV, 1);
                    context.getContentResolver().update(WeatherContract.WeatherEntry.CONTENT_URI, favoriteValue, WeatherContract.WeatherEntry.COLUMN_CITY + "=?", new String[]{city.getName()});
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public CityWeather getItem(int position) {
        dataCursor.moveToPosition(position);
        CityWeather city = new CityWeather();
        city.setName(dataCursor.getString(CITY_INDEX));
        city.setFavourite(dataCursor.getInt(FAV_INDEX) == 1);
        return city;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView txtCityName;
        ImageView imgWeatherIcon;
        TextView txtTemp;
        TextView txtWeathDesc;
        TextView txtPrec;
        TextView txtFeelsLike;
        TextView txtObservedAt;
        TextView txtHumidity;
        TextView txtBarom;
        TextView txtWindSpeed;
        CardView cardView;
        LinearLayout layFavInfo;

        public CityViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            layout = (RelativeLayout) v.findViewById(R.id.rowwea_lay_rel);
            txtCityName = (TextView) v.findViewById(R.id.rowwea_txt_city);
            imgWeatherIcon = (ImageView) v.findViewById(R.id.rowwea_img_icon);
            txtTemp = (TextView) v.findViewById(R.id.rowwea_txt_temp);
            txtWeathDesc = (TextView) v.findViewById(R.id.rowwea_txt_weathdesc);
            txtPrec = (TextView) v.findViewById(R.id.rowwea_txt_precipmm);
            txtFeelsLike = (TextView) v.findViewById(R.id.rowwea_txt_feelslike);
            txtObservedAt = (TextView) v.findViewById(R.id.rowwea_txt_observedat);
            txtHumidity = (TextView) v.findViewById(R.id.rowwea_txt_humidity);
            txtBarom = (TextView) v.findViewById(R.id.rowwea_txt_barom);
            txtWindSpeed = (TextView) v.findViewById(R.id.rowwea_txt_windspeed);
            layFavInfo = (LinearLayout) v.findViewById(R.id.rowwea_lay_favinfo);
        }

    }

}
