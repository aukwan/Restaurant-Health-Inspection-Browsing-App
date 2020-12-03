package com.cmpt276.group16.ui.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import com.cmpt276.group16.R;


public class SearchFilter extends AppCompatDialogFragment {
    //TODO: Save filter selections
    private static SearchFilter instance;
    public static SearchFilter getInstance() {
        if (instance == null) {
            instance = new SearchFilter();
        }
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.filter_popup, null);

        configureRadioButtonsForHazardLevel(v);
        configureInputAndToggleForCritViolations(v);
        configureToggleForFavourite(v);
        configureClearFilters(v);

        // Build the alert dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        alert.setView(v);

        AlertDialog dialog = alert.create();
        dialog.show();

        return dialog;
    }

    private void configureRadioButtonsForHazardLevel(View view) {
        RadioButton lowHazard = view.findViewById(R.id.lowHazard);
        RadioButton moderateHazard = view.findViewById(R.id.moderateHazard);
        RadioButton highHazard = view.findViewById(R.id.highHazard);
        lowHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Hazard", 1);
                editor.apply();
            }
        });

        moderateHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Hazard", 2);
                editor.apply();
            }
        });

        highHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Hazard", 3);
                editor.apply();
            }
        });
    }

    private void configureInputAndToggleForCritViolations(View view) {
        EditText numViolationsInput = view.findViewById(R.id.numCritViolationsLastYear);
        SwitchCompat nCritViolationsSwitch = view.findViewById(R.id.nCritViolationsSwitch);
        //TODO: Filtering functions
    }

    private void configureToggleForFavourite(View view) {
        SwitchCompat favouriteSwitch = view.findViewById(R.id.favouriteSwitch);
        //TODO: Filtering functions
    }

    private void configureClearFilters(View view) {
        Button clearFiltersBtn = view.findViewById(R.id.clearFiltersBtn);
        //TODO: CLear all filters (optional, can remove if not needed)

    }
}
