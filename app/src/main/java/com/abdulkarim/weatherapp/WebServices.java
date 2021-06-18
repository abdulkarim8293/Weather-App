package com.abdulkarim.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WebServices {

    @GET()
    Call<CurrentWeatherResponse> getCurrentWeatherInfo(@Url String url);

    @GET()
    Call<ForecastWeatherResponse> getForecastWeatherInfo(@Url String url);

}
