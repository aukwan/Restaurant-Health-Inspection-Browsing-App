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

        //TODO: remove after csv file reader implemented
        readRestaurantData();
       // DEBUG_TEST_POPULATE();
        ///////////////////////////////////////////////

        registerClickCallback();
        populateListView();
    }

    private void readRestaurantData() {
        InputStream is= getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader= new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line="";
            try {
                if (((line=reader.readLine())!=null)){
                    String[] tokens=line.split(",");
                    Restaurant sample=new Restaurant(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],
                            Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
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
    //TODO: remove after implementing csv file reader  --------------------------------------------
    //SIMPLE ADDING OF THE RESTAURANTS -- DEBUGGING PURPOSES
   /* private void DEBUG_TEST_POPULATE(){
        String trackingNumber = "SDFO-8HKP7E";
        String name = "Pattullo A&W";
        String physicalAddress = "12808 King George Blvd";
        String physicalCity = "Surrey";
        String facType = "Restaurant";
        Double latitude = 49.20611;
        Double longitude = -122.867;
        Restaurant restaurantTest = new Restaurant(trackingNumber, name, physicalAddress, physicalCity, facType, latitude, longitude);
        restaurantManager.addRestaurant(restaurantTest);

        int inspectionDate = 20191002;
        String inspectionType = "Routine";
        int NumCritical = 0;
        int NumNonCritical = 0;
        String hazardRated = "Low";
        Issues issue1 = new Issues(trackingNumber, inspectionDate, inspectionType, NumCritical, NumNonCritical, hazardRated);
        restaurantManager.addIssues(issue1);

        inspectionDate = 20181024;
        inspectionType = "Follow-Up";
        NumCritical = 0;
        NumNonCritical = 1;
        hazardRated = "Low";
        Issues issue2 = new Issues(trackingNumber, inspectionDate, inspectionType, NumCritical, NumNonCritical, hazardRated);
        restaurantManager.addIssues(issue2);

    }
    */
    //-------------------------------------------------------------------------------------------------

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
            Restaurant currentRestaurant = restaurantManager.getRestArray().get(position);
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