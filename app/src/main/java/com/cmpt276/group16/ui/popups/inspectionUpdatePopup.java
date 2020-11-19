package com.cmpt276.group16.ui.popups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.cmpt276.group16.R;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
        Displays an inspection Update Popup (Stories.iteration2)
 */

public class inspectionUpdatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_update_popup);

        final InspectionLoadingDialog inspectionLoadingDialog = new InspectionLoadingDialog(this);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.25));

        WindowManager.LayoutParams parameters = getWindow().getAttributes();
        parameters.gravity = Gravity.CENTER;
        parameters.x = 0;
        parameters.y = -20;

        getWindow().setAttributes(parameters);

        Button buttonClose = (Button) findViewById(R.id.inspectionIgnoreButton);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        Button inspectionCancelUpdateButton = (Button) findViewById(R.id.inspectionCancelUpdateButton);
//        inspectionCancelUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                inspectionLoadingDialog.inspectionCancelledUpdateOperation();
//            }
//        });


        Button buttonUpdate = (Button) findViewById(R.id.inspectionUpdateButton);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inspectionLoadingDialog.startLoadingAnimation();
                String url = getIntent().getStringExtra("INSPECTION_CSV_URL");
                final String lastModified = getIntent().getStringExtra("INSPECTION_CSV_LAST_MODIFIED");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(inspectionUpdatePopup.this, "Please check if you were connected to the internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String myResponse = response.body().string();
                            FileOutputStream fos = null;
                            try {
                                SharedPreferences remoteDataPrefs = getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                                boolean inspectionCancelledUpdateOperation = remoteDataPrefs.getBoolean("inspectionCancelledUpdateOperation", false);
                                if (!inspectionCancelledUpdateOperation) {
                                    fos = openFileOutput("inspectionreports.csv", MODE_PRIVATE);
                                    fos.write(myResponse.getBytes());
                                    //reset the value to false for next time
                                    SharedPreferences.Editor editor = remoteDataPrefs.edit();
                                    editor.putBoolean("inspectionCancelledUpdateOperation", false).apply();
                                    //set the last modified of the remote server file for future update checking
                                    editor.putString("lastModifiedDateInspections", lastModified);

                                    //modify when the last time this has been checked for the 20h logic
                                    Date endDate = new Date();
                                    long seconds = endDate.getTime() / 1000; //milliseconds to seconds
                                    int hours = (int) (seconds / 3600); //seconds to hours
                                    editor.putInt("lastCheckedHoursForInspectionsChanged", hours).apply();
                                    inspectionLoadingDialog.dismissDialog();
                                    Log.i("SuccessfulSavedData", "Saved to " + getFilesDir() + "/inspectionreports.csv");
                                } else {
                                    Log.i("SuccessfulSavedDataInterrupted", "Task was interrupted by the user");

                                }


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.close();
                                        finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                });
            }
        });
    }
}