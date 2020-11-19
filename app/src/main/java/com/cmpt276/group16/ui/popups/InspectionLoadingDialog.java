package com.cmpt276.group16.ui.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cmpt276.group16.R;

/*
        Displays an Inspection Loading Dialog  (Stories.iteration2)
 */

public class InspectionLoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    InspectionLoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingAnimation() {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View exp = inflater.inflate(R.layout.inspection_loading_custom_dialog, null);
        Button inspectionCancelUpdateButton = (Button) exp.findViewById(R.id.inspectionCancelUpdateButton);
        inspectionCancelUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inspectionCancelledUpdateOperation();
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

    void inspectionCancelledUpdateOperation() {
        SharedPreferences remoteDataPrefs = activity.getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = remoteDataPrefs.edit();
        editor.putBoolean("inspectionCancelledUpdateOperation", true).apply();
        dismissDialog();
    }
}
