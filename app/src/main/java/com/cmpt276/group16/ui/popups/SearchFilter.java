package com.cmpt276.group16.ui.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import com.cmpt276.group16.R;
import com.cmpt276.group16.ui.MainActivity;
import com.cmpt276.group16.ui.RestaurantMapsActivity;
/*
        Iteration 3 : Class used to filter the searches
 */


public class SearchFilter extends AppCompatDialogFragment {
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

        SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int hazard = prefs.getInt("Hazard", 0);
        switch (hazard) {
            case 0:
                break;
            case 1:
                lowHazard.setChecked(true);
                break;
            case 2:
                moderateHazard.setChecked(true);
                break;
            case 3:
                highHazard.setChecked(true);
                break;
        }

    }

    private void configureInputAndToggleForCritViolations(View view) {
        EditText numViolationsInput = view.findViewById(R.id.numCritViolationsLastYear);
        SwitchCompat nCritViolationsSwitch = view.findViewById(R.id.nCritViolationsSwitch);
        nCritViolationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if(isChecked==true){
                    editor.putBoolean("ViolationSwitch", true);
                }
                else{
                    editor.putBoolean("ViolationSwitch", false);
                }
                editor.apply();
            }
        });
        numViolationsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if(s.toString().equals(""))
                    editor.putInt("Violations",0);
                else
                    editor.putInt("Violations",Integer.parseInt(s.toString()));
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        numViolationsInput.setText(""+prefs.getInt("Violations",0));
        nCritViolationsSwitch.setChecked(prefs.getBoolean("ViolationSwitch",true));
    }

    private void configureToggleForFavourite(View view) {
        SwitchCompat favouriteSwitch = view.findViewById(R.id.favouriteSwitch);
        favouriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if(isChecked==true){
                    editor.putBoolean("FavouriteSwitch", true);
                }
                else{
                    editor.putBoolean("FavouriteSwitch", false);
                }
                editor.apply();
            }
        });
        SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        favouriteSwitch.setChecked(prefs.getBoolean("FavouriteSwitch",false));
    }

    private void configureClearFilters(final View view) {
        Button clearFiltersBtn = view.findViewById(R.id.clearFiltersBtn);
        clearFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = SearchFilter.this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                RadioGroup radioButtons=view.findViewById(R.id.hazardlevelGroup);
                radioButtons.clearCheck();
                editor.putInt("Hazard",0);
                EditText numViolationsInput = view.findViewById(R.id.numCritViolationsLastYear);
                numViolationsInput.setText("");
                editor.putInt("Violations",0);
                SwitchCompat nCritViolationsSwitch = view.findViewById(R.id.nCritViolationsSwitch);
                nCritViolationsSwitch.setChecked(true);
                editor.putBoolean("ViolationSwitch",true);
                SwitchCompat favouriteSwitch = view.findViewById(R.id.favouriteSwitch);
                favouriteSwitch.setChecked(false);
                editor.putBoolean("FavouriteSwitch",false);
                editor.apply();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof MainActivity){
            ((MainActivity)activity).populateListView();
        }
        else if (activity instanceof RestaurantMapsActivity){
            ((RestaurantMapsActivity)activity).setFilterForRestaurantMaps();
            ((RestaurantMapsActivity)activity).resetClusterItems();
            ((RestaurantMapsActivity)activity).setTheClusterMarkersItems();
        }
    }
}
