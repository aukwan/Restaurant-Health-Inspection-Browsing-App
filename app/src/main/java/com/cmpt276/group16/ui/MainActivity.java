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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.NewDataHarvester;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;

import java.io.BufferedReader;
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

        NewDataHarvester newDataHarvester = new NewDataHarvester();
        newDataHarvester.checkForPeriodicDataChange(this);

        readRestaurantData();
        readInspectionData();
        registerClickCallback();
        populateListView();


    }

    //==============
    // functions that we use to handle data
    //==============
    private String formatString(String unformatted) {
        String out = unformatted;
        if (unformatted != null)
            out = unformatted.substring(1, unformatted.length() - 1);
        return out;
    }
    //==============
    //==============
    //==============


    private void checkForNewData() {
        final NewDataHarvester newDataHarvester = new NewDataHarvester();
        newDataHarvester.restaurantGetRequest();
        Log.i("resultsOfJSON", newDataHarvester.getLastModified());

    }

    //READ CSV FILE
    private void readRestaurantData() {
        InputStream is = getResources().openRawResource(R.raw.restaurants);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while (((line = reader.readLine()) != null)) {
                String[] tokens = line.split(",");
                Restaurant sample = new Restaurant(formatString(tokens[0]), formatString(tokens[1]), formatString(tokens[2]), formatString(tokens[3]),
                        formatString(tokens[4]), Double.parseDouble(formatString(tokens[5])), Double.parseDouble(formatString(tokens[6])));
                restaurantManager.addRestaurant(sample);
            }
        } catch (IOException e) {
            Log.wtf("MainActivity", "Error reading datafile on line" + line, e);
            e.printStackTrace();
        }
    }

    private void readInspectionData() {
        InputStream is = getResources().openRawResource(R.raw.inspectionreports);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while (((line = reader.readLine()) != null)) {
                String[] tokens = line.split(",");
                String violationLump = "";
                for (int k = 6; k < tokens.length; k++) {
                    violationLump = violationLump + tokens[k];
                    if (k != tokens.length - 1) {
                        violationLump += ",";
                    }
                }
                Issues sample;
                if (tokens.length == 6) {
                    sample = new Issues(formatString(tokens[0]), Integer.parseInt(tokens[1]), formatString(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]), formatString(tokens[5]), null);
                    restaurantManager.addIssues(sample);
                } else {
                    sample = new Issues(formatString(tokens[0]), Integer.parseInt(tokens[1]), formatString(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]), formatString(tokens[5]), formatString(violationLump));
                    restaurantManager.addIssues(sample);
                }
            }
        } catch (IOException e) {
            Log.wtf("MainActivity", "Error reading datafile on line" + line, e);
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
            imageView.setImageResource(R.drawable.dish);
            TextView textView = (TextView) itemView.findViewById(R.id.textViewRestaurant);
            textView.setText(currentRestaurant.getName());
            if (currentRestaurant.getIssuesList().size() != 0) {
                Issues currentIssues = currentRestaurant.getIssuesList().get(0);
                String hazardLevel = currentIssues.getHazardRated();
                if (hazardLevel.equals("Low")) {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    textHazardLevel.setText("Hazard Level: " + hazardLevel);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.greendot);
                } else if (hazardLevel.equals("Moderate")) {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    textHazardLevel.setText("Hazard Level: " + hazardLevel);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.yellowdot);
                } else {
                    TextView textHazardLevel = (TextView) itemView.findViewById(R.id.textHazardLevel);
                    textHazardLevel.setText("Hazard Level: " + hazardLevel);
                    ImageView imageHazardLevel = (ImageView) itemView.findViewById(R.id.imageHazardLevel);
                    imageHazardLevel.setImageResource(R.drawable.reddot);
                }
                int totalIssues = currentIssues.getNumCritical() + currentIssues.getNumNonCritical();
                String info = "# of Issues Found: " + totalIssues;
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

    //LISTVIEW BUTTONS
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

    }

}
