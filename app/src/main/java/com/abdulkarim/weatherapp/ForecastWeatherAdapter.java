package com.abdulkarim.weatherapp;

import android.content.Context;
import android.media.Image;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastWeatherAdapter extends RecyclerView.Adapter<ForecastWeatherAdapter.MyViewHolder> {

    private ForecastWeatherResponse forecastWeatherResponse;
    private String sym;

    public static String getDayAndTime(long timeInMillis) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timeInMillis * 1000L);
        String times = DateFormat.format("EEEE \nhh:mm a", cal).toString();
        return times;
    }

    public ForecastWeatherAdapter(ForecastWeatherResponse forecastWeatherResponse,String sym) {
        this.forecastWeatherResponse = forecastWeatherResponse;
        this.sym = sym;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast_weather_recycler_view,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.temperature_text.setText(""+forecastWeatherResponse.getList().get(position).getMain().getTemp()+"°"+sym);
        holder.min_max_temperature.setText("Min "+forecastWeatherResponse.getList().get(position).getMain().getTempMin()+"°"+sym+"\nMax "+forecastWeatherResponse.getList().get(position).getMain().getTempMax()+"°"+sym);
        holder.weather_type.setText(""+forecastWeatherResponse.getList().get(position).getWeather().get(0).getMain());
        holder.weather_description.setText(""+forecastWeatherResponse.getList().get(position).getWeather().get(0).getDescription());
        holder.date_text.setText(""+getDayAndTime(forecastWeatherResponse.getList().get(position).getDt()).toUpperCase());

        switch (forecastWeatherResponse.getList().get(position).getWeather().get(0).getMain()){
            case WeatherInfo.WEATHER_TYPE_CLOUD:
                holder.weather_image.setImageResource(R.drawable.cloudy);
                break;
            case WeatherInfo.WEATHER_TYPE_RAIN:
                holder.weather_image.setImageResource(R.drawable.rainy);
                break;
            case WeatherInfo.WEATHER_TYPE_SNOW:
                holder.weather_image.setImageResource(R.drawable.snow);
                break;
            case WeatherInfo.WEATHER_TYPE_WIND:
                holder.weather_image.setImageResource(R.drawable.wind);
                break;
            default:
                holder.weather_image.setImageResource(R.drawable.sunny);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return forecastWeatherResponse.getList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date_text,temperature_text,min_max_temperature,weather_type,weather_description;
        private ImageView weather_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            weather_image = itemView.findViewById(R.id.item_image_view);
            date_text = itemView.findViewById(R.id.item_date_text_view);
            temperature_text = itemView.findViewById(R.id.item_temperature_text_view);
            min_max_temperature = itemView.findViewById(R.id.item_min_max_temperature_text_view);
            weather_type = itemView.findViewById(R.id.item_weather_type_text_view);
            weather_description = itemView.findViewById(R.id.item_weather_description_text_view);

        }
    }
}
