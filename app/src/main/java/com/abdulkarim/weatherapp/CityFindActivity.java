package com.abdulkarim.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class CityFindActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String > adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_find);

        findViewById(R.id.backIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.lv1);

        list = new ArrayList<>();
        list.add("Dhaka");
        list.add("Chittagong");
        list.add("Khulna");
        list.add("Sylhet");
        list.add("Mymensingh");
        list.add("Rajshahi");
        list.add("Tongi");
        list.add("Bogra");
        list.add("Barisal");
        list.add("Rangpur");
        list.add("Savar");
        list.add("Jhenaidah");
        list.add("Thakurgaon");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //String cityName = query.toLowerCase();

                if(list.contains(query)){
                    adapter.getFilter().filter(query);
                    //Toast.makeText(CityFindActivity.this, ""+query,Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(CityFindActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("city",query.toLowerCase());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(list.get(position),false);

                String selectedCity = (String) (listView.getItemAtPosition(position));
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("city",selectedCity.toLowerCase());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }
}