package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.NewDataHarvester;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUESTS = 100;
    private RestaurantList restaurantManager = RestaurantList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);


        //request external file storage permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSIONS_REQUESTS);
        }

        //check for updates
        NewDataHarvester newDataHarvester = new NewDataHarvester();
        newDataHarvester.checkForPeriodicDataChangeForRestaurants(this);
        newDataHarvester.checkForPeriodicDataChangeForInspections(this);

        readRestaurantData();
        readInspectionData();

        Intent intent = new Intent(ReadActivity.this, RestaurantMapsActivity.class);
        startActivity(intent);
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
    // Function to remove the element
    public static String[] removeTheElement(String[] arr,
                                            int index)
    {
        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        String[] anotherArray = new String[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
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
        FileInputStream fis = null;
        try {
            fis = openFileInput("restaurants.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = "";
            try {
                while (((line = reader.readLine()) != null)) {
                    String[] tokens = line.split(",");
                    //the reason this if statement exists because sometimes the name of the restaurant has a comma in it.
                    //so it is always wrapped with a " signs
                    if (tokens[1].startsWith("\"")){
                        tokens[1] = tokens[1] + tokens[2];
                        tokens[1] = formatString(tokens[1]);
                        tokens = removeTheElement(tokens, 2);
                    }
                    Log.i("lineErrorMainActivity", "Error reading datafile on line" + line);
                    Restaurant sample = new Restaurant(tokens[0].trim(), tokens[1], tokens[2], tokens[3],
                            tokens[4], Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
                    restaurantManager.addRestaurant(sample);
                }
            } catch (IOException e) {
                Log.i("lineErrorMainActivity", "Error reading datafile on line" + line, e);
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            Log.i("fileNotFoundForRestauranstCSV", "" + fileNotFoundException);
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
                    Restaurant sample = new Restaurant(tokens[0].trim(), tokens[1], tokens[2], tokens[3],
                            tokens[4], Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
                    restaurantManager.addRestaurant(sample);
                }
            } catch (IOException e) {
                Log.wtf("MainActivity", "Error reading datafile on line" + line, e);
                e.printStackTrace();
            }
        }
    }

    private void readInspectionData() {
        FileInputStream fis = null;
        try{
            fis = openFileInput("inspectionreports.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
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
                    for (int k = 5; k < tokens.length - 1; k++) {
                        violationLump = violationLump + tokens[k];
                        if (k != tokens.length - 2) {
                            violationLump += ",";
                        }
                    }
                    if(tokens.length!=0) {
                        Issues sample;
                        if (tokens.length == 5) {
                            sample = new Issues(tokens[0], Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]),
                                    Integer.parseInt(tokens[4]), "Low", null);
                        } else if (tokens[5].length() <= 0) {
                            Log.i("lineErrorMainActivity", "2: Error reading datafile on line" + line);
                            sample = new Issues(tokens[0], Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]),
                                    Integer.parseInt(tokens[4]), tokens[tokens.length-1], null);
                        } else {
                            Log.i("lineErrorMainActivity", "1: Error reading datafile on line" + line);
                            sample = new Issues(tokens[0], Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]),
                                    Integer.parseInt(tokens[4]), tokens[tokens.length-1], formatString(violationLump));
                        }
                        restaurantManager.addIssues(sample);
                    }
                }
            } catch (IOException e) {
                Log.wtf("MainActivity", "Error reading datafile on line" + line, e);
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            Log.i("fileNotFoundForInspectionsCSV", "" + fileNotFoundException);

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

    }
}