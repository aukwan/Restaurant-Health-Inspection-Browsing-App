package com.cmpt276.group16.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.cmpt276.group16.ui.popups.inspectionUpdatePopup;
import com.cmpt276.group16.ui.popups.restaurantUpdatePopup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
        Takes the data from the surrey API with GET method using OkHttp3 (Stories.iteration2)
 */

public class NewDataHarvester {
    private static final String restaurantsQueryUrl = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static final String restaurantInspectionsQueryUrl = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private static final String periodicDataPreft = "periodicDataPreft";
    private static final String lastCheckedHoursForRestaurantsChanged = "lastCheckedHoursForRestaurantsChanged";
    private static final String lastCheckedHoursForInspectionsChanged = "lastCheckedHoursForInspectionsChanged";


    private String lastModified = "none";

    public NewDataHarvester() {
    }

    //Getters and setters
    public String getLastModified() {
        return lastModified;
    }

    private void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }


    //Update and fetch logic functions
    public void restaurantGetRequest() {

    }

    //This functions checks the number of hours since the app last launched
    //enabling us a method to run a remote data fetch for new data every 20 hours
    public void checkForPeriodicDataChangeForRestaurants(Activity activity) {
        SharedPreferences remoteDataPrefs = activity.getSharedPreferences(this.periodicDataPreft, Context.MODE_PRIVATE);
        int lastCheckedHoursForRestaurantsChanged = remoteDataPrefs.getInt(this.lastCheckedHoursForRestaurantsChanged, 0);
        if (lastCheckedHoursForRestaurantsChanged <= 0) {

            //log lastCheckedHoursForRestaurantsChanged for debugging purposes
            Log.i("checkForPeriodicDataChange", "" + lastCheckedHoursForRestaurantsChanged);


            lastCheckedHoursForRestaurantsChanged = 0;

            //get current date
            Date startDate = new Date();
            long seconds = startDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            //put hours since the start of time (big number of hours - 0)
            int putInitialHours = hours - lastCheckedHoursForRestaurantsChanged;
            SharedPreferences.Editor editor = remoteDataPrefs.edit();


            //set the big number of hours in SharedPreferences
            editor.putInt(this.lastCheckedHoursForRestaurantsChanged, putInitialHours).apply();
            checkForNewRestaurantData(activity);


        } else {
            //if SharedPreferences properly exist (running for the 2nd time or more)

            //get the current date
            Date endDate = new Date();
            long seconds = endDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            int period = hours - lastCheckedHoursForRestaurantsChanged;

            Log.i("checkForPeriodicDataChange", "else condition | checkForPeriodicDataChange(): " + lastCheckedHoursForRestaurantsChanged);

            if (period >= 20) {
                checkForNewRestaurantData(activity);
            }
//            SharedPreferences.Editor editor = remoteDataPrefs.edit();
//
//
//            //set the number of hours passed in SharedPreferences
//            editor.putInt(this.lastCheckedHoursForRestaurantsChanged, period).apply();

        }
    }

    public void checkForPeriodicDataChangeForInspections(Activity activity) {
        SharedPreferences remoteDataPrefs = activity.getSharedPreferences(this.periodicDataPreft, Context.MODE_PRIVATE);
        int lastCheckedHoursForInspectionsChanged = remoteDataPrefs.getInt(this.lastCheckedHoursForInspectionsChanged, 0);
        if (lastCheckedHoursForInspectionsChanged <= 0) {

            //log lastCheckedHoursForInspectionsChanged for debugging purposes
            Log.i("checkForPeriodicDataChange", "" + lastCheckedHoursForInspectionsChanged);


            lastCheckedHoursForInspectionsChanged = 0;

            //get current date
            Date startDate = new Date();
            long seconds = startDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            //put hours since the start of time (big number of hours - 0)
            int putInitialHours = hours - lastCheckedHoursForInspectionsChanged;
            SharedPreferences.Editor editor = remoteDataPrefs.edit();


            //set the big number of hours in SharedPreferences
            editor.putInt(this.lastCheckedHoursForInspectionsChanged, putInitialHours).apply();
            checkForNewInspectionData(activity);


        } else {
            //if SharedPreferences properly exist (running for the 2nd time or more)

            //get the current date
            Date endDate = new Date();
            long seconds = endDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            int period = hours - lastCheckedHoursForInspectionsChanged;

            Log.i("checkForPeriodicDataChange", "else condition | checkForPeriodicDataChange(): " + lastCheckedHoursForInspectionsChanged);
            Log.i("checkForPeriodicDataChange", "period " + period);

            if (period >= 20) {
                checkForNewInspectionData(activity);
            }
//            SharedPreferences.Editor editor = remoteDataPrefs.edit();
//
//
//            //set the number of hours passed in SharedPreferences
//            editor.putInt(this.lastCheckedHoursForInspectionsChanged, period).apply();

        }
    }


    private void checkForNewRestaurantData(final Activity activity) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(restaurantsQueryUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("requestEnqueueFailed", "onFailure | checkForNewRestaurantData(): " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        JSONObject mainJsonObject = new JSONObject(myResponse);
                        //mainJsonObject = full response

                        JSONObject ResultJsonObject = mainJsonObject.getJSONObject("result");
                        //ResultJsonObject = full response/results

                        JSONArray ResourcesArrayObject = ResultJsonObject.getJSONArray("resources");
                        //ResourcesArrayObject = full response/results/resources

                        JSONObject CSVResourceObject = ResourcesArrayObject.getJSONObject(0);
                        //CSVResourceObject = full response/results/resources[0]

                        final String lastModified = CSVResourceObject.getString("last_modified");
                        final String url = CSVResourceObject.getString("url");

                        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                        String lastModifiedDateRestaurants = remoteDataPrefs.getString("lastModifiedDateRestaurants", "");

                        if (lastModified != lastModifiedDateRestaurants) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(activity, restaurantUpdatePopup.class);
                                    intent.putExtra("RESTAURANT_CSV_URL", url);
                                    intent.putExtra("RESTAURANT_CSV_LAST_MODIFIED", lastModified);

                                    activity.startActivity(intent);
                                }
                            });
                        } else {
                            Log.i("SuccessfulJSONParsing", "" + lastModified + " == " + lastModifiedDateRestaurants);
                        }


                    } catch (JSONException e) {
                        Log.i("JSONObjectFailed", "mainJsonObject | checkForNewRestaurantData" + e);
                    }
                }
            }
        });
    }

    private void checkForNewInspectionData(final Activity activity) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(restaurantInspectionsQueryUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("requestEnqueueFailed", "onFailure | checkForNewRestaurantData(): " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        JSONObject mainJsonObject = new JSONObject(myResponse);
                        //mainJsonObject = full response

                        JSONObject ResultJsonObject = mainJsonObject.getJSONObject("result");
                        //ResultJsonObject = full response/results

                        JSONArray ResourcesArrayObject = ResultJsonObject.getJSONArray("resources");
                        //ResourcesArrayObject = full response/results/resources

                        JSONObject CSVResourceObject = ResourcesArrayObject.getJSONObject(0);
                        //CSVResourceObject = full response/results/resources[0]

                        final String lastModified = CSVResourceObject.getString("last_modified");
                        final String url = CSVResourceObject.getString("url");


                        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                        String lastModifiedDateInspections = remoteDataPrefs.getString("lastModifiedDateInspections", "");

                        if (lastModified != lastModifiedDateInspections) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(activity, inspectionUpdatePopup.class);
                                    intent.putExtra("INSPECTION_CSV_URL", url);
                                    intent.putExtra("INSPECTION_CSV_LAST_MODIFIED", lastModified);

                                    activity.startActivity(intent);
                                }
                            });
                        } else {
                            Log.i("SuccessfulJSONParsing", "" + lastModified + " == " + lastModifiedDateInspections);
                        }


                    } catch (JSONException e) {
                        Log.i("JSONObjectFailed", "mainJsonObject | checkForNewRestaurantData" + e);
                    }
                }
            }
        });
    }
}