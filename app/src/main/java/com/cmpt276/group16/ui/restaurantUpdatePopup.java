package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

public class restaurantUpdatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_update_popup);

        final RestaurantLoadingDialog restaurantLoadingDialog = new RestaurantLoadingDialog(this);

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

        Button buttonClose = (Button) findViewById(R.id.restaurantIgnoreButton);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        Button restaurantCancelUpdateButton = (Button) findViewById(R.id.restaurantCancelUpdateButton);
//        restaurantCancelUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                restaurantLoadingDialog.restaurantCancelledUpdateOperation();
//            }
//        });

        Button buttonUpdate = (Button) findViewById(R.id.restaurantUpdateButton);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantLoadingDialog.startLoadingAnimation();
                String url = getIntent().getStringExtra("RESTAURANT_CSV_URL");
                final String lastModified = getIntent().getStringExtra("RESTAURANT_CSV_LAST_MODIFIED");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(restaurantUpdatePopup.this, "Please check if you were connected to the internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            String myResponse = response.body().string();
                            FileOutputStream fos = null;
                            try {
                                SharedPreferences remoteDataPrefs = getSharedPreferences("periodicDataPreft", Context.MODE_PRIVATE);
                                boolean restaurantCancelledUpdateOperation = remoteDataPrefs.getBoolean("restaurantCancelledUpdateOperation", false);
                                if (!restaurantCancelledUpdateOperation){
                                    fos = openFileOutput("restaurants.csv", MODE_PRIVATE);
                                    fos.write(myResponse.getBytes());
                                    //reset the value to false for next time
                                    SharedPreferences.Editor editor = remoteDataPrefs.edit();
                                    editor.putBoolean("restaurantCancelledUpdateOperation", false).apply();
                                    //set the last modified of the remote server file for future update checking
                                    editor.putString("lastModifiedDateRestaurants", lastModified);

                                    //modify when the last time this has been checked for the 20h logic
                                    Date endDate = new Date();
                                    long seconds = endDate.getTime() / 1000; //milliseconds to seconds
                                    int hours = (int) (seconds / 3600); //seconds to hours
                                    editor.putInt("lastCheckedHoursForRestaurantsChanged", hours).apply();
                                    restaurantLoadingDialog.dismissDialog();
                                    Log.i("SuccessfulSavedData", "Saved to " + getFilesDir() + "/restaurants.csv");
                                }
                                else{
                                    Log.i("SuccessfulSavedDataInterrupted", "Task was interrupted by the user");
                                    restaurantLoadingDialog.dismissDialog();

                                }


                            }
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                            finally {
                                if (fos != null){
                                    try{
                                        fos.close();
                                        finish();
                                    }
                                    catch (IOException e){
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