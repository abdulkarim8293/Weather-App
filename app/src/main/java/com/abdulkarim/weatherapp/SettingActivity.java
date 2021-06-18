package com.abdulkarim.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity {

    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        aSwitch = findViewById(R.id.switch1);
        SharedPreferences sharedPreferences = getSharedPreferences("temp",MODE_PRIVATE);
        boolean temps = sharedPreferences.getBoolean("value",true);
        aSwitch.setChecked(temps);

        if (temps){
            aSwitch.setText("Celsius");
        }else {
            aSwitch.setText("Fahrenheit");
        }

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("temp",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.putString("symbol","C");
                    editor.putString("units","metric");
                    editor.apply();
                    aSwitch.setChecked(true);
                    aSwitch.setText("Celsius");
                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("temp",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.putString("symbol","F");
                    editor.putString("units","imperial");
                    editor.apply();
                    aSwitch.setChecked(false);
                    aSwitch.setText("Fahrenheit");
                }
            }
        });

    }
}