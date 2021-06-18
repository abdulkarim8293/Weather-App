package com.abdulkarim.weatherapp;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherInfo {

    public static final String WEATHER_TYPE_CLOUD = "Clouds";
    public static final String WEATHER_TYPE_CLEAR = "Clear";
    public static final String WEATHER_TYPE_WIND = "Wind";
    public static final String WEATHER_TYPE_RAIN = "Rain";
    public static final String WEATHER_TYPE_SNOW = "Snow";

    public static String getTime(long timeInMillis) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timeInMillis * 1000L);
        String times = DateFormat.format("EEEE, MMMM yyyy ", cal).toString();
        return times;
    }
    public static String getTimes(long timeInMillis) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTimeInMillis(timeInMillis * 1000L);
        String times = DateFormat.format("HH:MM a ", cal).toString();
        return times;
    }

    private String cityName;
    private String countryName;
    private double currentTemperature;
    private double min_temp;
    private double max_temp;
    private double feel_like;
    private String weatherType;
    private String description;
    private String date_text;
    private String times;
    private String wind;
    private String humidity;

    public String getWind() {
        return wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public WeatherInfo(String cityName, String countryName, double currentTemperature, double min_temp, double max_temp, double feel_like, String weatherType, String description, String date_text, String wind, String humidity) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.currentTemperature = currentTemperature;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.feel_like = feel_like;
        this.weatherType = weatherType;
        this.description = description;
        this.date_text = getTime(Integer.valueOf(date_text));
        this.times = getTimes(Integer.valueOf(date_text));
        this.wind = wind;
        this.humidity = humidity;
    }

    public String getTimes(){
        return times;
    }
    public String getDate_text() {
        return date_text;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public double getFeel_like() {
        return feel_like;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public String getDescription() {
        return description;
    }

}
