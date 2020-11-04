package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.cmpt276.group16.model.RestaurantList;


public class RestaurantUI extends AppCompatActivity {
    private int restaurantIndex;
    private final RestaurantList restaurantManager = RestaurantList.getInstance();
    private ArrayAdapter<Issues> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_u_i);
        extractDataFromSharedPref();
        setupTextViews();
        populateListView();
        registerClickCallback();
        backArrowPress();
    }
    //In case in the future he wants us to manually add a restaurant in the software
//    @Override
//    protected void onResume(){
//        super.onResume();
//        extractDataFromSharedPref();
//        setupTextViews();
//        populateListView();
//        registerClickCallback();
//
//    }

    //setup the textview for UI
    private void setupTextViews(){
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


    //Extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
    }


    //POPULATES THE LIST VIEW
    private void populateListView() {
        adapter = new RestaurantUI.MyListAdapter();
        ListView list = findViewById(R.id.listViewIssues);
        list.setAdapter(adapter);

    }
    //ADAPTER
    private class MyListAdapter extends ArrayAdapter<Issues>{
        public MyListAdapter() {
            super(RestaurantUI.this, R.layout.issueslistview, restaurantManager.getRestArray().get(restaurantIndex).getInspectionList());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;

            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.issueslistview, parent, false);
            }

            Issues currentInspection = restaurantManager.getRestArray().get(restaurantIndex).getInspectionList().get(position);

            //set strings from issue details
            String numCrit = "Critical issues #: " + currentInspection.getNumCritical();
            String numNonCrit = "Non - Critical issues #: " + currentInspection.getNumNonCritical();
            String inspectDate = "Inspection Date: " + currentInspection.getInspectionDate();

            //initialise single item elements
            TextView criticalIssuesListViewTextView  = (TextView) itemView.findViewById(R.id.criticalIssuesListView);
            TextView nonCriticalIssuesListViewTextView  = (TextView) itemView.findViewById(R.id.nonCriticalIssuesListView);
            TextView inspectionDateListViewTextView  = (TextView) itemView.findViewById(R.id.inspectionDateListView);
            ImageView inspectionDotColorListViewImageView = (ImageView) itemView.findViewById(R.id.inspectionDotColorListView);

            //set values to single item elements of the list view
            criticalIssuesListViewTextView.setText(numCrit);
            nonCriticalIssuesListViewTextView.setText(numNonCrit);
            inspectionDateListViewTextView.setText(inspectDate);
            if (currentInspection.getHazardRated().equals("Low")){
                inspectionDotColorListViewImageView.setImageResource(R.drawable.greendot);
            }
            else if (currentInspection.getHazardRated().equals("Moderate")){
                inspectionDotColorListViewImageView.setImageResource(R.drawable.yellowdot);
            }
            else {
                inspectionDotColorListViewImageView.setImageResource(R.drawable.reddot);
            }

            return itemView;
        }

    }

    //shared preference
    private void saveIssueIndex(int issueIndex){
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Inspection List - Index", issueIndex);
        editor.apply();
    }
    //LISTVIEW BUTTONS
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listViewIssues);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveIssueIndex(position);
                Intent intent = new Intent(RestaurantUI.this, InspectionUI.class);
                startActivity(intent);
            }
        });

    }

    //trigger back press on back arrow press
    private void backArrowPress(){
        ImageView backArrow = (ImageView) findViewById(R.id.restaurantUIBackButton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}