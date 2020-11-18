package com.cmpt276.group16.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cmpt276.group16.R;

public class RestaurantLoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    RestaurantLoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingAnimation() {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View exp = inflater.inflate(R.layout.restaurant_loading_custom_dialog, null);
        Button restaurantCancelUpdateButton = (Button) exp.findViewById(R.id.restaurantCancelUpdateButton);
        restaurantCancelUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantCancelledUpdateOperation();
            }
        });
        builder.setView(exp);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

        //if we needed to set on cancel listenenr
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//
//            }
//        });
    }

    void dismissDialog() {
        dialog.dismiss();
    }

    void restaurantCancelledUpdateOperation(){
        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = remoteDataPrefs.edit();
        editor.putBoolean("restaurantCancelledUpdateOperation", true).apply();
        dismissDialog();

    }
}
