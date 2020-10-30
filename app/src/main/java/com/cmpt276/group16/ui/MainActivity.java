package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private RestaurantList restaurantManager = RestaurantList.getInstance();
    private ArrayAdapter<Restaurant> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readRestaurantData();
        registerClickCallback();
        populateListView();
    }

    private String formatString(String unformatted){
        return unformatted.substring(1,unformatted.length()-1);
    }

    //READ CSV FILE
    private void readRestaurantData() {
        InputStream is= getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader= new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line="";
        try {
            while (((line=reader.readLine())!=null)){
                String[] tokens=line.split(",");
                Restaurant sample=new Restaurant(formatString(tokens[0]),formatString(tokens[1]),formatString(tokens[2]),formatString(tokens[3]),
                        formatString(tokens[4]), Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
                restaurantManager.addRestaurant(sample);
            }
        } catch (IOException e) {
            Log.wtf("MainActivity","Error reading datafile on line"+line,e);
            e.printStackTrace();
        }
    }

    //In case in the future he wants us to manually add a restaurant in the software
//    @Override
//    protected void onResume(){
//        super.onResume();
//        registerClickCallback();
//        populateListView();
//
//    }
    //POPULATES THE LIST VIEW
    private void populateListView() {
        adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }
    //ADAPTER
    private class MyListAdapter extends ArrayAdapter<Restaurant>{
        public MyListAdapter() {
            super(MainActivity.this, R.layout.restaurantlistview, restaurantManager.getRestArray());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.restaurantlistview, parent, false);
            }
            Restaurant currentRestaurant = restaurantManager.getRestaurant(position);
            //TODO: add an image id for the restaurants (can be taken randomly)
            // ImageView imageView = (ImageView) itemView.findViewById(R.id.imageItems) - imageItems is the id for the imageview in restaurantlistview
            // imageView.setImageResource(currentRestaurant.getDrawable)
            TextView textView  = (TextView) itemView.findViewById(R.id.textViewRestaurant);
            textView.setText(currentRestaurant.getName());
            return itemView;
        }

    }
    //shared preference
    private void saveRestaurantIndex(int restaurantIndex){
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Restaurant List - Index", restaurantIndex);
        editor.apply();
    }

    //LISTVIEW BUTTONS
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveRestaurantIndex(position);
                Intent intent = new Intent(MainActivity.this, RestaurantUI.class);
                startActivity(intent);
            }
        });

    }

}