package com.cmpt276.group16.ui.popups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.group16.R;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
        Displays an inspection Update Popup (Stories.iteration2)
 */

public class favouriteInspectionUpdatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_inspection_update_popup);

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

        String newInspects = "New Inspections";

        FileInputStream fis = null;
        try {
            File file = new File(this.getFilesDir() + "/newinspections.csv");
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    newInspects +="\n" + line;
                }
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Log.i("fileNotFoundForFavouritesCSV", "" + fileNotFoundException);
            FileOutputStream fosFav = null;
            FileOutputStream fosIns = null;
            try {
                fosFav = this.openFileOutput("favourites.csv", Context.MODE_PRIVATE);
                fosIns = this.openFileOutput("newinspections.csv", Context.MODE_PRIVATE);

                fosFav.close();
                fosIns.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TextView text = (TextView) findViewById(R.id.newDataTitlefav);
        text.setText(newInspects);
    }
}