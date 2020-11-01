package com.cmpt276.group16.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Inspection;
import com.cmpt276.group16.model.RestaurantList;
import com.cmpt276.group16.model.Violation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class InspectionUI extends AppCompatActivity {

    private int inspectionIndex;
    private int restaurantIndex;
    private Inspection inspection;
    private ArrayList<Violation> violations;
    private RestaurantList restaurantManager = RestaurantList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_u_i);

        extractDataFromSharedPref();
        setTextView();
        setHazardRatingTextAndIcon();
        populateListView();
        registerClickCallback();
    }

    // Extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
        inspectionIndex = prefs.getInt("Inspection List - Index", 0);
        inspection = restaurantManager.getRestaurant(restaurantIndex).getInspectionList().get(inspectionIndex);
        violations = inspection.getViolationList();
    }

    // Set TextViews
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

    // Fill list view with icons and short descriptions of each violation
    private void populateListView() {
        //TODO: Create short descriptions for each violation
        ArrayList<String> shortDescriptionViolations = new ArrayList<>();

        // Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.violation_view,
                shortDescriptionViolations
        );

        // Configure the list view
        ListView listViolations = findViewById(R.id.violationListView);
        listViolations.setAdapter(adapter);
    }

    // Display full violation description as a toast
    private void registerClickCallback() {
        ListView listViolations = findViewById(R.id.violationListView);
        listViolations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Set full violation description
                String longDescription = null;
                Toast.makeText(InspectionUI.this, longDescription, Toast.LENGTH_LONG).show();
            }
        });
    }
}