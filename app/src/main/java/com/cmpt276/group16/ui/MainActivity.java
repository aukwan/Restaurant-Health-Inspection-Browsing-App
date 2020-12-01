package com.cmpt276.group16.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.NewDataHarvester;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*

App entry point, List of all restaurants (Stories.iteration1.1)

 */
public class MainActivity extends AppCompatActivity {

    private RestaurantList restaurantManager = RestaurantList.getInstance();
    private ArrayAdapter<Restaurant> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerClickCallback();
        populateListView();

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
    private class MyListAdapter extends ArrayAdapter<Restaurant> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.restaurantlistview, restaurantManager.getRestArray());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.restaurantlistview, parent, false);
            }
            Restaurant currentRestaurant = restaurantManager.getRestaurant(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageRestaurant);
            // Set Icons for some restaurants
            if (currentRestaurant.getName().indexOf("7-Eleven") > -1) {
                imageView.setImageResource(R.drawable.logoseven);
            } else if (currentRestaurant.getName().indexOf("A&W") > -1 || currentRestaurant.getName().indexOf("A & W") > -1) {
                imageView.setImageResource(R.drawable.logoaw);
            } else if (currentRestaurant.getName().indexOf("Tim Hortons") > -1) {
                imageView.setImageResource(R.drawable.logotim);
            } else if (currentRestaurant.getName().indexOf("Circle K") > -1) {
                imageView.setImageResource(R.drawable.logocirclek);
            } else if (currentRestaurant.getName().indexOf("Burger King") > -1) {
                imageView.setImageResource(R.drawable.logoburgerking);
            } else if (currentRestaurant.getName().indexOf("Boston Pizza") > -1) {
                imageView.setImageResource(R.drawable.logobp);
            } else if (currentRestaurant.getName().indexOf("COBS") > -1) {
                imageView.setImageResource(R.drawable.logocobs);
            } else if (currentRestaurant.getName().indexOf("McDonald's") > -1) {
                imageView.setImageResource(R.drawable.logomcd);
            } else if (currentRestaurant.getName().indexOf("Starbucks") > -1) {
                imageView.setImageResource(R.drawable.logostarbucks);
            } else if (currentRestaurant.getName().indexOf("Pizza Hut") > -1) {
                imageView.setImageResource(R.drawable.logopizza);
            } else {
                imageView.setImageResource(R.drawable.dish);
            }
            TextView textView = (TextView) itemView.findViewById(R.id.textViewRestaurant);
            textView.setText(currentRestaurant.getName());
            if (currentRestaurant.getIssuesList().size() != 0) {
                Issues currentIssues = currentRestaurant.getIssuesList().get(0);
                String hazardLevel = currentIssues.getHazardRated();
                if (hazardLevel.equals("Low")) {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    String hazardLevelStr = getString(R.string.hazardLevelLow);
                    textHazardLevel.setText(hazardLevelStr);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.greendot);
                } else if (hazardLevel.equals("Moderate")) {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    String hazardLevelStr = getString(R.string.hazardLevelModerate);
                    textHazardLevel.setText(hazardLevelStr);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.yellowdot);
                } else {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    String hazardLevelStr = getString(R.string.hazardLevelHigh);
                    textHazardLevel.setText(hazardLevelStr);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.reddot);
                }
                int totalIssues = currentIssues.getNumCritical() + currentIssues.getNumNonCritical();
                String numIssuesPreStr = getString(R.string.numIssuesPreStr);
                String info = numIssuesPreStr + totalIssues;
                TextView textIssues = (TextView) itemView.findViewById(R.id.textInfo);
                textIssues.setText(info);

                //initialize date related variables
                Date c = Calendar.getInstance().getTime();
                TextView readableDate = (TextView) itemView.findViewById(R.id.textInspectionDate);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                String strDate = df.format(c);
                int intDate = Integer.parseInt(strDate);
                int timeDifference = intDate - currentIssues.getIssueDate();
                String dateOutput;

                //convert int date to readable format
                if (timeDifference <= 30) {
                    dateOutput = timeDifference + " days ago";
                } else if (timeDifference < 365) {
                    String unformatted = "" + currentIssues.getIssueDate();
                    Date date = null;
                    try {
                        date = df.parse(unformatted);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    df = new SimpleDateFormat("MMM d");
                    dateOutput = df.format(date);
                } else {
                    String unformatted = "" + currentIssues.getIssueDate();
                    Date date = null;
                    try {
                        date = df.parse(unformatted);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    df = new SimpleDateFormat("MMM yyyy");
                    dateOutput = df.format(date);

                }
                readableDate.setText(dateOutput);
            } else {
                TextView textInfo = (TextView) itemView.findViewById(R.id.textInfo);
                textInfo.setText("No inspections");
            }
            return itemView;
        }

    }

    //shared preference
    private void saveRestaurantIndex(int restaurantIndex) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Restaurant List - Index", restaurantIndex);
        editor.apply();
    }

    //LISTVIEW and BottomNavigationView BUTTONS
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveRestaurantIndex(position);
                Intent intent = new Intent(MainActivity.this, RestaurantUI.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.restaurant_list_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_maps:
                        finish();
                        Intent intent = new Intent(MainActivity.this, RestaurantMapsActivity.class);
                        startActivity(intent);

                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
