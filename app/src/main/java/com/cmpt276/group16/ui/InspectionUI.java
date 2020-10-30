package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Inspection;
import com.cmpt276.group16.model.RestaurantList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class InspectionUI extends AppCompatActivity {

    private int inspectionIndex;
    private int restaurantIndex;
    private Inspection inspection;
    private RestaurantList restaurantManager = RestaurantList.getInstance();
    public static final String ALL_VIOLATIONS_FILENAME = "AllViolations.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_u_i);

        extractDataFromSharedPref();
        setTextView();
        setHazardRatingTextAndIcon();
        populateListView();
    }

    //Extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
        inspectionIndex = prefs.getInt("Issue List - Index", 0);
        inspection = restaurantManager.getRestaurant(restaurantIndex).getInspectionList().get(inspectionIndex);
    }

    //setTextViews
    private void setTextView() {
        // Format full date of the inspection
        String unformattedDate = Integer.toString(inspection.getInspectionDate());
        LocalDate date = LocalDate.parse(unformattedDate, DateTimeFormatter.BASIC_ISO_DATE);
        String formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(date);

        String inspectionDate = "Inspection Date: " + formattedDate;
        String inspectionType = "Inspection Type: " + inspection.getInspectionType();
        String critical = "# Critical Issues Found: " + inspection.getNumCritical();
        String nonCritical = "# Non-Critical Issues Found: " + inspection.getNumNonCritical();

        TextView inspectionDateView = findViewById(R.id.inspectionDate);
        inspectionDateView.setText(inspectionDate);

        TextView inspectionRoutineView = findViewById(R.id.inspectionRoutine);
        inspectionRoutineView.setText(inspectionType);

        TextView criticalIssuesView = findViewById(R.id.numCriticalIssues);
        criticalIssuesView.setText(critical);

        TextView nonCriticalIssuesView = findViewById(R.id.numNonCriticalIssues);
        nonCriticalIssuesView.setText(nonCritical);
    }

    private void setHazardRatingTextAndIcon() {
        String hazardRating = inspection.getHazardRating();
        ImageView hazardIcon = findViewById(R.id.hazardIcon);
        TextView hazardLevelView = findViewById(R.id.hazardRating);
        hazardLevelView.setText(hazardRating);

        // Change text and icon colour based on hazard level
        if (hazardRating.equals("Low")) {
            hazardLevelView.setTextColor(Color.GREEN);
            hazardIcon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        } else if (hazardRating.equals("Moderate")) {
            hazardLevelView.setTextColor(Color.YELLOW);
            hazardIcon.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        } else {
            hazardLevelView.setTextColor(Color.RED);
            hazardIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
    }

    private void populateListView() {
        ListView violationList = findViewById(R.id.violationList);
    }

}