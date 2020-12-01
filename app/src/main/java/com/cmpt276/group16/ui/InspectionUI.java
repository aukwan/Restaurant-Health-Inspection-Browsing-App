package com.cmpt276.group16.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.RestaurantList;
import com.cmpt276.group16.model.Violations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

/*

Inspection UI class (Stories.iteration1.3)

 */

public class InspectionUI extends AppCompatActivity {

    private int inspectionIndex;
    private int restaurantIndex;
    private Issues inspection;
    private ArrayList<Violations> violations;
    private final RestaurantList restaurantManager = RestaurantList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_u_i);

        extractDataFromSharedPref();
        setTextView();
        setHazardRatingTextAndIcon();
        populateListView();
        registerClickCallback();
        backArrowPress();
    }

    // Extract data from shared pref
    private void extractDataFromSharedPref() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        restaurantIndex = prefs.getInt("Restaurant List - Index", 0);
        inspectionIndex = prefs.getInt("Inspection List - Index", 0);
        inspection = restaurantManager.getRestaurant(restaurantIndex).getIssuesList().get(inspectionIndex);
        violations = inspection.getViolationList();
    }

    // Set TextViews
    private void setTextView() {
        // Format full date of the inspection
        String unformattedDate = Integer.toString(inspection.getIssueDate());
        LocalDate date = LocalDate.parse(unformattedDate, DateTimeFormatter.BASIC_ISO_DATE);
        String formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(date);

        String preStrNumCrit = getString(R.string.preStrNumCrit);
        String preStrNonNumCrit =getString(R.string.preStrNonNumCrit);
        String preStrInspectDate =getString(R.string.preStrInspectDate);
        String preStrInspectType =getString(R.string.preStrInspectType);


        String inspectionDate = preStrInspectDate  + formattedDate;
        String inspectionType = preStrInspectType + inspection.getInspectionType();
        String critical = preStrNumCrit + inspection.getNumCritical();
        String nonCritical = preStrNonNumCrit + inspection.getNumNonCritical();

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
        String hazardRating;
        if (inspection.getHazardRated().equals("High")){
            hazardRating = getString(R.string.High);
        } else if (inspection.getHazardRated().equals("Moderate")){
            hazardRating = getString(R.string.Moderate);
        } else {
            hazardRating = getString(R.string.Low);
        }
        ImageView hazardIcon = findViewById(R.id.hazardIcon);
        TextView hazardLevelText = findViewById(R.id.hazardRating);
        hazardLevelText.setText(hazardRating);

        // Change text and icon colour based on hazard level
        if (hazardRating.equals("Low")) {
            hazardLevelText.setTextColor(Color.GREEN);
            hazardIcon.setImageResource(R.drawable.greendot);
        } else if (hazardRating.equals("Moderate")) {
            hazardLevelText.setTextColor(Color.YELLOW);
            hazardIcon.setImageResource(R.drawable.yellowdot);
        } else {
            hazardLevelText.setTextColor(Color.RED);
            hazardIcon.setImageResource(R.drawable.reddot);
        }
    }

    // Fill list view with icons and short descriptions of each violation
    private void populateListView() {
        // Build Adapter
        ArrayAdapter<Violations> adapter = new ViolationListAdapter();

        // Configure the list view
        ListView listViolations = findViewById(R.id.violationListView);
        listViolations.setAdapter(adapter);
    }

    // Display full violation description as a toast
    private void registerClickCallback() {
        ListView listViolations = findViewById(R.id.violationListView);
        listViolations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Violations currentViolation = violations.get(position);
                String[] violDesc= currentViolation.toString().split(",");
                String longDescription = getViolStringFromXML(violDesc[0]);

                Toast.makeText(InspectionUI.this, longDescription, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getViolStringFromXML(String num){
        String longDescription = getString(R.string.no_description);
        if (num.equals("101")){
            longDescription = getString(R.string.viol_101);
        }else if (num.equals("102")){
            longDescription = getString(R.string.viol_102);
        }else if (num.equals("103")){
            longDescription = getString(R.string.viol_103);
        }else if (num.equals("104")){
            longDescription = getString(R.string.viol_104);
        }else if (num.equals("201")){
            longDescription = getString(R.string.viol_201);
        }else if (num.equals("202")){
            longDescription = getString(R.string.viol_202);
        }else if (num.equals("203")){
            longDescription = getString(R.string.viol_203);
        }
        else if (num.equals("204")){
            longDescription = getString(R.string.viol_204);
        }
        else if (num.equals("205")){
            longDescription = getString(R.string.viol_205);
        }
        else if (num.equals("206")){
            longDescription = getString(R.string.viol_206);
        }
        else if (num.equals("208")){
            longDescription = getString(R.string.viol_208);
        }
        else if (num.equals("209")){
            longDescription = getString(R.string.viol_209);
        }
        else if (num.equals("210")){
            longDescription = getString(R.string.viol_210);
        }
        else if (num.equals("211")){
            longDescription = getString(R.string.viol_211);
        }
        else if (num.equals("212")){
            longDescription = getString(R.string.viol_212);
        }else if (num.equals("301")){
            longDescription = getString(R.string.viol_301);
        }else if (num.equals("302")){
            longDescription = getString(R.string.viol_302);
        }else if (num.equals("303")){
            longDescription = getString(R.string.viol_303);
        }else if (num.equals("304")){
            longDescription = getString(R.string.viol_304);
        }else if (num.equals("305")){
            longDescription = getString(R.string.viol_305);
        }else if (num.equals("306")){
            longDescription = getString(R.string.viol_306);
        }else if (num.equals("307")){
            longDescription = getString(R.string.viol_307);
        }else if (num.equals("308")){
            longDescription = getString(R.string.viol_308);
        }else if (num.equals("309")){
            longDescription = getString(R.string.viol_309);
        }else if (num.equals("310")){
            longDescription = getString(R.string.viol_310);
        }else if (num.equals("311")){
            longDescription = getString(R.string.viol_311);
        }else if (num.equals("312")){
            longDescription = getString(R.string.viol_312);
        }else if (num.equals("314")){
            longDescription = getString(R.string.viol_314);
        }else if (num.equals("315")){
            longDescription = getString(R.string.viol_315);
        }else if (num.equals("401")){
            longDescription = getString(R.string.viol_401);
        }else if (num.equals("402")){
            longDescription = getString(R.string.viol_402);
        }else if (num.equals("404")){
            longDescription = getString(R.string.viol_404);
        }else if (num.equals("501")){
            longDescription = getString(R.string.viol_501);
        }else if (num.equals("502")){
            longDescription = getString(R.string.viol_502);
        }

        return longDescription;
    }

    private class ViolationListAdapter extends ArrayAdapter<Violations> {
        public ViolationListAdapter() {
            super(InspectionUI.this, R.layout.violation_view, violations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.violation_view, parent, false);
            }
            Violations currentViolation = violations.get(position);

            // Set icon based on violation number
            ImageView violationIcon = itemView.findViewById(R.id.violationIcon);
            if (currentViolation.getViolNum() >= 101 && currentViolation.getViolNum() <= 104) {
                violationIcon.setImageResource(R.drawable.operation);
            } else if (currentViolation.getViolNum() >= 201 && currentViolation.getViolNum() <= 212) {
                violationIcon.setImageResource(R.drawable.food);
            } else if (currentViolation.getViolNum() >= 301 && currentViolation.getViolNum() <= 315) {
                violationIcon.setImageResource(R.drawable.sanitation);
            } else if (currentViolation.getViolNum() >= 401 && currentViolation.getViolNum() <= 404) {
                violationIcon.setImageResource(R.drawable.employee);
            } else {
                violationIcon.setImageResource(R.drawable.certification);
            }

            TextView shortDescriptionText = itemView.findViewById(R.id.violationShortDescription);
            String severityStr;
            if (currentViolation.getSeverity().equals("Not Critical")){
                severityStr = getString(R.string.Not_Critical);
            }else {
                severityStr = getString(R.string.Critical);
            }

            shortDescriptionText.setText("" + currentViolation.getViolNum() + ", " + severityStr);

            // Set severity icon colour
            ImageView violationSeverityView = itemView.findViewById(R.id.violationSeverity);
            if (currentViolation.getSeverity().equals("Not Critical")) {
                violationSeverityView.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            } else {
                violationSeverityView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }

            return itemView;
        }
    }

    private void backArrowPress() {
        ImageView backArrow = (ImageView) findViewById(R.id.inspectionUIBackBtn);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}