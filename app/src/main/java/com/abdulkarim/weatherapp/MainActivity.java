package com.abdulkarim.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int LOCATION_REQUEST_CODE = 10001;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private String cityName;
    private String symbol;
    private double lat;
    private double lon;

    private Retrofit retrofit;
    private WebServices webServices;

    String api_key = "51f87ae51e8345217779ddecb8936c28";
    String api_unit = "metric";

    private SharedPreferences sharedPreferences;

    // weather home page view reference
    private TextView dateWithMonth,times,cityWithCountry,weatherTemperature,weatherType,feelsLike,weatherDescription,userAdvice,windFlow,minMaxTemperature,humidity;
    private ImageView weatherImageStatus;

    private TextView forecastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        retrofit = RetrofitInstance.getInstance();
        webServices = retrofit.create(WebServices.class);

        findViewById(R.id.cityTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CityFindActivity.class));
            }
        });

        findViewById(R.id.settingsTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });

        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForecastActivity.class);
                if (cityName!=null){
                    String cityUrl = String.format("forecast?q=%s&units=%s&appid=%s",cityName,api_unit,api_key);
                   intent.putExtra("urls",cityUrl);
                   intent.putExtra("sym",symbol);
                }else {

                    String forecastUrl = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s", lat, lon, api_unit, api_key);
                    intent.putExtra("urls",forecastUrl);
                    intent.putExtra("sym",symbol);
                }


                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("temp", MODE_PRIVATE);
        //tempValue = sharedPreferences.getBoolean("value", true);

        //api_unit = sharedPreferences.getString("units","");
        //Toast.makeText(this, ""+tempValue+" and symbol "+symbol, Toast.LENGTH_SHORT).show();
        //getLastLocation();
        //getData(latitude,longitude);


    }

    private void init() {

        dateWithMonth = findViewById(R.id.day_date_month_text_view);
        times = findViewById(R.id.time_text_view);
        cityWithCountry = findViewById(R.id.city_country_text_view);
        weatherTemperature = findViewById(R.id.weather_temperature_text_view);
        weatherType = findViewById(R.id.weather_type_text_view);
        feelsLike = findViewById(R.id.weather_feels_like_text_view);
        weatherDescription = findViewById(R.id.weather_description_text_view);
        userAdvice = findViewById(R.id.user_advice_description_text_view);
        windFlow = findViewById(R.id.wind_flow_text_view);
        minMaxTemperature = findViewById(R.id.min_max_text_view);
        humidity = findViewById(R.id.humidity_text_view);
        weatherImageStatus = findViewById(R.id.weather_image_status);


        forecastButton = findViewById(R.id.button);

    }

    @Override
    protected void onResume() {
        super.onResume();
        api_unit = sharedPreferences.getString("units","");
        symbol = sharedPreferences.getString("symbol", "C");
        Toast.makeText(this, "" + api_unit+ " and symbol is : "+symbol, Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        cityName = intent.getStringExtra("city");
        if (cityName != null) {
            // get weather by city name
            Toast.makeText(this, "" + cityName, Toast.LENGTH_SHORT).show();
            String cityUrl = String.format("weather?q=%s&units=%s&appid=%s",cityName,api_unit,api_key);
            getWeatherByCityName(cityUrl);

        } else {
            // get current weather by current location
            getWeatherByLastLocation();
            //Toast.makeText(this, "Empty City Name", Toast.LENGTH_SHORT).show();
        }


    }

    private void getWeatherByCityName(String cityUrl) {

        webServices.getCurrentWeatherInfo(cityUrl).enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.isSuccessful()) {

                    CurrentWeatherResponse currentWeatherResponse = response.body();
                    String cityName = currentWeatherResponse.getName();
                    String countryName = currentWeatherResponse.getSys().getCountry();
                    double currentTemperature = currentWeatherResponse.getMain().getTemp();
                    double min_temp = currentWeatherResponse.getMain().getTempMin();
                    double max_temp = currentWeatherResponse.getMain().getTempMax();
                    double feel_like = currentWeatherResponse.getMain().getFeelsLike();
                    String weatherType = currentWeatherResponse.getWeather().get(0).getMain();
                    String description = currentWeatherResponse.getWeather().get(0).getDescription();
                    String dateText = String.valueOf(currentWeatherResponse.getDt());
                    String wind = String.valueOf(currentWeatherResponse.getWind().getSpeed());
                    String humidity = String.valueOf(currentWeatherResponse.getMain().getHumidity());

                    WeatherInfo weatherInfo = new WeatherInfo(cityName,countryName,currentTemperature,min_temp,max_temp,feel_like,weatherType,description,dateText,wind,humidity);
                    updateUI(weatherInfo);

                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.i("TAG", "" + t.getMessage());

            }
        });
    }

    private void getWeatherByLastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    getWeatherData(location.getLatitude(), location.getLongitude());

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }

    }

    private void getWeatherData(double latitude, double longitude) {

        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, api_unit, api_key);

        webServices.getCurrentWeatherInfo(url).enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.isSuccessful()) {

                    CurrentWeatherResponse currentWeatherResponse = response.body();
                    String cityName = currentWeatherResponse.getName();
                    String countryName = currentWeatherResponse.getSys().getCountry();
                    double currentTemperature = currentWeatherResponse.getMain().getTemp();
                    double min_temp = currentWeatherResponse.getMain().getTempMin();
                    double max_temp = currentWeatherResponse.getMain().getTempMax();
                    double feel_like = currentWeatherResponse.getMain().getFeelsLike();
                    String weatherType = currentWeatherResponse.getWeather().get(0).getMain();
                    String description = currentWeatherResponse.getWeather().get(0).getDescription();
                    String dateText = String.valueOf(currentWeatherResponse.getDt());
                    String wind = String.valueOf(currentWeatherResponse.getWind().getSpeed());
                    String humidity = String.valueOf(currentWeatherResponse.getMain().getHumidity());

                    WeatherInfo weatherInfo = new WeatherInfo(cityName,countryName,currentTemperature,min_temp,max_temp,feel_like,weatherType,description,dateText,wind,humidity);
                    updateUI(weatherInfo);
                    Log.i("MMM", "Date is " +WeatherInfo.getTime(Integer.valueOf(dateText)));

                    // set lat lon for forecast activity;
                    lat = latitude;
                    lon = longitude;
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.i("MMM", "" + t.getMessage());

            }
        });

    }

    private void updateUI(WeatherInfo weatherInfo) {

        switch (weatherInfo.getWeatherType()){
            case WeatherInfo.WEATHER_TYPE_CLOUD:
                weatherImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                userAdvice.setText("It's cloudy day please go out side with your umbrella");
                break;
            case WeatherInfo.WEATHER_TYPE_RAIN:
                weatherImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                userAdvice.setText("It's rainy day, Please stay your location and save");
                break;
            case WeatherInfo.WEATHER_TYPE_SNOW:
                weatherImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                userAdvice.setText("It's snow day, Let's go....");
                break;
            case WeatherInfo.WEATHER_TYPE_WIND:
                weatherImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.wind));
                userAdvice.setText("It's wind day, Please stay your home and keep safe yourself");
                break;
            default:
                weatherImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                userAdvice.setText("It's sunny day, Let's go....");
                break;
        }

        times.setText(""+weatherInfo.getTimes());
        dateWithMonth.setText(""+weatherInfo.getDate_text());
        cityWithCountry.setText("" + weatherInfo.getCityName() + " - " + weatherInfo.getCountryName());

        weatherTemperature.setText(""+ weatherInfo.getCurrentTemperature() + "°" + symbol);
        weatherType.setText(""+weatherInfo.getWeatherType());
        feelsLike.setText(""+weatherInfo.getFeel_like()+ "°" + symbol);
        weatherDescription.setText(""+weatherInfo.getDescription());
        minMaxTemperature.setText("Min "+weatherInfo.getMin_temp()+"°"+"\nMax "+weatherInfo.getMax_temp()+"°");

        windFlow.setText("Wind Flow\n"+weatherInfo.getWind()+" km / h");
        humidity.setText("Humidity\n"+weatherInfo.getHumidity()+" %");

    }


}