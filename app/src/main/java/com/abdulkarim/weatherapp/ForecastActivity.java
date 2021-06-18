package com.abdulkarim.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private WebServices webServices;

    private RecyclerView recyclerView;
    private String sym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forecast);

        retrofit = RetrofitInstance.getInstance();
        webServices = retrofit.create(WebServices.class);

        recyclerView = findViewById(R.id.recycler_view);

        String urls = getIntent().getStringExtra("urls");
        sym = getIntent().getStringExtra("sym");
        Log.i("TAG","Main activity send urls : "+urls);

        webServices.getForecastWeatherInfo(urls).enqueue(new Callback<ForecastWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastWeatherResponse> call, Response<ForecastWeatherResponse> response) {
                if (response.isSuccessful()){

                    ForecastWeatherAdapter forecastWeatherAdapter = new ForecastWeatherAdapter(response.body(),sym);

                    Log.i("TAG",""+response.body().getList().get(1).getMain().getTemp()
                            + ""+response.body().getList().get(1).getMain().getTempMax()+""
                            +response.body().getList().get(1).getWeather().get(0).getDescription()+"List size"
                            +response.body().getList().size());
                    recyclerView.setAdapter(forecastWeatherAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);

                    //Toast.makeText(ForecastActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastWeatherResponse> call, Throwable t) {

                Log.i("MMM",""+t.getMessage());
            }
        });


    }
}