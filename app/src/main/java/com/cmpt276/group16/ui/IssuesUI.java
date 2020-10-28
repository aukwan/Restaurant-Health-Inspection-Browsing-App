package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.RestaurantList;

public class IssuesUI extends AppCompatActivity {
    private int issuesIndex;
    private int restaurantIndex;
    private RestaurantList restaurantManager = RestaurantList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_u_i);
        extractDataFromSharedPref();
        setTestView();
    }


    //setTextViews
    private void setTestView(){
        String inspectionDate = "Inspection date: " + Integer.toString(restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(issuesIndex).getInspectionDate());
        String inspectionRoute = "Inspection Routine: " + restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(issuesIndex).getInspectionType();
        String critical = "Critical report#: " + Integer.toString(restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(issuesIndex).getNumCritical());
        String nonCritical = "Non - Critical report#: " + Integer.toString(restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(issuesIndex).getNumNonCritical());

        TextView textView = (TextView) findViewById(R.id.inspectionDate);
        textView.setText(inspectionDate);
        textView = (TextView) findViewById(R.id.inspectionRoutine);
        textView.setText(inspectionRoute);
        textView = (TextView) findViewById(R.id.numCriticalIssues);
        textView.setText(critical);
        textView = (TextView) findViewById(R.id.numNonCriticalIssues);
        textView.setText(nonCritical);
    }

    //Extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
        issuesIndex = prefs.getInt("Issue List - Index", 0);
    }

}