package com.cmpt276.group16.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.cmpt276.group16.ui.inspectionUpdatePopup;
import com.cmpt276.group16.ui.restaurantUpdatePopup;

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

public class NewDataHarvester {
    private static final String restaurantsQueryUrl = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static final String restaurantInspectionsQueryUrl = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private static final String periodicDataPreft = "periodicDataPreft";
    private static final String lastCheckedHours = "lastCheckedHours";
    private static final String lastModifiedDateRestaurants = "lastModifiedDateRestaurants";
    private static final String lastModifiedDateInspections = "lastModifiedDateInspections";




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
    public void checkForPeriodicDataChange(Activity activity) {
        SharedPreferences remoteDataPrefs = activity.getSharedPreferences(this.periodicDataPreft, Context.MODE_PRIVATE);
        int lastCheckedHours = remoteDataPrefs.getInt(this.lastCheckedHours, 0);
        if (lastCheckedHours <= 0) {

            //log lastCheckedHours for debugging purposes
            Log.i("checkForPeriodicDataChange", "" + lastCheckedHours);


            lastCheckedHours = 0;

            //get current date
            Date startDate = new Date();
            long seconds = startDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            //put hours since the start of time (big number of hours - 0)
            int putInitialHours = hours - lastCheckedHours;
            SharedPreferences.Editor editor = remoteDataPrefs.edit();


            //set the big number of hours in SharedPreferences
            editor.putInt(this.lastCheckedHours, putInitialHours).apply();

        } else {
            //if SharedPreferences properly exist (running for the 2nd time or more)

            //get the current date
            Date endDate = new Date();
            long seconds = endDate.getTime() / 1000; //milliseconds to seconds
            int hours = (int) (seconds / 3600); //seconds to hours


            int period = hours - lastCheckedHours;

            Log.i("checkForPeriodicDataChange", "else condition | checkForPeriodicDataChange(): " + lastCheckedHours);

            //TODO: remove commends on line 104 and line 106
//            if (period >= 20){
            checkForNewRestaurantData(activity);
            checkForNewInspectionData(activity);
//            }
//            SharedPreferences.Editor editor = remoteDataPrefs.edit();
//
//
//            //set the number of hours passed in SharedPreferences
//            editor.putInt(this.lastCheckedHours, period).apply();

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

                        String lastModified = CSVResourceObject.getString("last_modified");
                        final String url = CSVResourceObject.getString("url");

                        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                        String lastModifiedDateRestaurants = remoteDataPrefs.getString("lastModifiedDateRestaurants","");

                        if (lastModified != lastModifiedDateRestaurants){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(activity, restaurantUpdatePopup.class);
                                    intent.putExtra("RESTAURANT_CSV_URL", url);
                                    activity.startActivity(intent);
                                }
                            });
                        }
                        else {
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

                        String lastModified = CSVResourceObject.getString("last_modified");

                        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                        String lastModifiedDateInspections = remoteDataPrefs.getString("lastModifiedDateInspections","");

                        if (lastModified != lastModifiedDateInspections){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(activity, inspectionUpdatePopup.class);
                                    activity.startActivity(intent);
                                }
                            });
                        }
                        else {
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

//    URL url = null;
//        try {
//        url = new URL(restaurantsQueryUrl);
//    } catch (MalformedURLException e) {
//        e.printStackTrace();
//    }
//    final URL finalUrl = url;
//    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            try {
//                URLConnection request = finalUrl.openConnection();
//                request.connect();
//
//                JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
//                JsonObject rootObj = root.getAsJsonObject();
//                String results = "tzzzz";
//                results = rootObj.get("results").getAsString();
//                Log.i("rootElement", "" + root);
//                Log.i("rootObject", "" + rootObj);
//                Log.i("resultsObject", "" + results);
//            } catch (Exception e) {
//                Log.i("threadRunnableCatch", "" + e);
//            }
//        }
//    });
//
//        thread.start();
//        try {
//        thread.join();
//    } catch (InterruptedException e) {
//        Log.i("threadJoinCatch", "" + e);
//    }
//}