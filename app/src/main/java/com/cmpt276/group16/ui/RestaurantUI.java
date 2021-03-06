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

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.RestaurantFavourite;
import com.cmpt276.group16.model.RestaurantList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*

Display single restaurant UI (Stories.iteration1.2)

*/

public class RestaurantUI extends AppCompatActivity {
    private int restaurantIndex;
    private final RestaurantList restaurantManager = RestaurantList.getInstance();
    private RestaurantFavourite restaurantFavourite = new RestaurantFavourite();
    private ArrayAdapter<Issues> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_u_i);
        extractDataFromSharedPref();
        setupTextViews();
        populateListView();
        populateRestaurantFavourite();
        onStarPress();
        registerClickCallback();
        backArrowPress();
    }

    //setup the TextView for UI
    private void setupTextViews() {
        //get text values
        String restaurantName = restaurantManager.getRestArray().get(restaurantIndex).getName();
        String restaurantAddress = restaurantManager.getRestArray().get(restaurantIndex).getPhysicalCity() + ", " +
                restaurantManager.getRestArray().get(restaurantIndex).getPhysicalAddress();
        String restaurantGPS = restaurantManager.getRestArray().get(restaurantIndex).getLatitude() + ", " +
                restaurantManager.getRestArray().get(restaurantIndex).getLongitude();

        //initialize TextViews
        TextView restaurantNameTextView = (TextView) findViewById(R.id.restaurantName);
        TextView restaurantAddressTextView = (TextView) findViewById(R.id.restaurantAddress);
        TextView restaurantGPSTextView = (TextView) findViewById(R.id.restaurantGPS);

        //set TextViews to the text value
        restaurantNameTextView.setText(restaurantName);
        restaurantAddressTextView.setText(restaurantAddress);
        restaurantGPSTextView.setText(restaurantGPS);
    }

    private void populateRestaurantFavourite() {
        ImageView star = findViewById(R.id.restaurantUIStarButton);
        boolean isFavourite = restaurantFavourite.determineIfFavourite(this, restaurantManager.getRestArray().get(restaurantIndex).getTrackingNumber());
        Log.i("isFavourite", "" + isFavourite);
        if (isFavourite) {
            star.setImageResource(R.drawable.ic_baseline_star_24);
            star.setTag("starFull");
        } else {
            star.setTag("starBorder");
        }

    }

    private void onStarPress() {
        final ImageView star = findViewById(R.id.restaurantUIStarButton);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("star", "" + star.getTag());

                if (star.getTag() == "starBorder") {
                    star.setImageResource(R.drawable.ic_baseline_star_24);
                    ArrayList<Issues> issuesArrayList = restaurantManager.getRestArray().get(restaurantIndex).getIssuesList();
                    restaurantFavourite.addToFavourites(RestaurantUI.this, restaurantManager.getRestArray().get(restaurantIndex).getTrackingNumber(), issuesArrayList.get(0).getIssueDate(), restaurantManager.getRestArray().get(restaurantIndex).getName());
                    star.setTag("starFull");
                } else {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24);
                    restaurantFavourite.removeFromFavourities(RestaurantUI.this, restaurantManager.getRestArray().get(restaurantIndex).getTrackingNumber());
                    star.setTag("starBorder");
                }
            }
        });

    }


    //extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
    }


    //populate listiview
    private void populateListView() {
        adapter = new RestaurantUI.MyListAdapter();
        ListView list = findViewById(R.id.listViewIssues);
        list.setAdapter(adapter);

    }

    //adapter
    private class MyListAdapter extends ArrayAdapter<Issues> {
        public MyListAdapter() {
            super(RestaurantUI.this, R.layout.issueslistview, restaurantManager.getRestArray().get(restaurantIndex).getIssuesList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.issueslistview, parent, false);
            }

            Issues currentInspection = restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(position);



            //set strings from issue details
            String preStrNumCrit = getString(R.string.preStrNumCrit);
            String preStrNonNumCrit =getString(R.string.preStrNonNumCrit);
            String preStrInspectDate =getString(R.string.preStrInspectDate);


            String numCrit = preStrNumCrit + currentInspection.getNumCritical();
            String numNonCrit = preStrNonNumCrit + currentInspection.getNumNonCritical();
            String inspectDate = preStrInspectDate + currentInspection.getIssueDate();

            //initialise single item elements
            TextView criticalIssuesListViewTextView = (TextView) itemView.findViewById(R.id.criticalIssuesListView);
            TextView nonCriticalIssuesListViewTextView = (TextView) itemView.findViewById(R.id.nonCriticalIssuesListView);
            TextView inspectionDateListViewTextView = (TextView) itemView.findViewById(R.id.inspectionDateListView);
            ImageView inspectionDotColorListViewImageView = (ImageView) itemView.findViewById(R.id.inspectionDotColorListView);
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String strDate = df.format(c);
            String dateOutput;

            int intDate = Integer.parseInt(strDate);
            int timeDifference = intDate - currentInspection.getIssueDate();

            //convert int date to readable format
            if (timeDifference <= 30) {
                dateOutput = timeDifference + " days ago";
            } else if (timeDifference < 365) {
                String unformatted = "" + currentInspection.getIssueDate();
                Date date = null;
                try {
                    date = df.parse(unformatted);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                df = new SimpleDateFormat("MMM d");
                dateOutput = df.format(date);
            } else {
                String unformatted = "" + currentInspection.getIssueDate();
                Date date = null;
                try {
                    date = df.parse(unformatted);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                df = new SimpleDateFormat("MMM yyyy");
                dateOutput = df.format(date);
            }
            //set readable date
            inspectionDateListViewTextView.setText(dateOutput);
            //set values to single item elements of the list view
            criticalIssuesListViewTextView.setText(numCrit);
            nonCriticalIssuesListViewTextView.setText(numNonCrit);

            if (currentInspection.getHazardRated().equals("Low")) {
                inspectionDotColorListViewImageView.setImageResource(R.drawable.greendot);
            } else if (currentInspection.getHazardRated().equals("Moderate")) {
                inspectionDotColorListViewImageView.setImageResource(R.drawable.yellowdot);
            } else {
                inspectionDotColorListViewImageView.setImageResource(R.drawable.reddot);
            }

            return itemView;
        }

    }

    //SharedPreference
    private void saveIssueIndex(int issueIndex) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Inspection List - Index", issueIndex);
        editor.apply();
    }

    // ListView buttons and GPS coordinate button
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewIssues);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveIssueIndex(position);
                Intent intent = new Intent(RestaurantUI.this, InspectionUI.class);
                startActivity(intent);
            }
        });

        TextView restaurantGPSTextView = findViewById(R.id.restaurantGPS);
        restaurantGPSTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantUI.this, RestaurantMapsActivity.class);
                intent.putExtra("restaurant index", restaurantIndex);
                startActivity(intent);
            }
        });

    }

    //trigger back press on back arrow press
    private void backArrowPress() {
        ImageView backArrow = (ImageView) findViewById(R.id.restaurantUIBackButton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}