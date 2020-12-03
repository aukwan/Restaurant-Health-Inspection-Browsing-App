package com.cmpt276.group16.model;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Trace;
import android.util.Log;
import android.view.View;

import com.cmpt276.group16.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class RestaurantFavourite {

    public RestaurantFavourite() {

    }

    public boolean determineIfFavourite(Activity activity, String TrackingNumber) {
        FileInputStream fis = null;
        boolean isFavourite = false;
        try {
            File file = new File(activity.getFilesDir() + "/favourites.csv");
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    Log.i("lineExec", line + " is equal to " + TrackingNumber + "?");
                    if (line.equals(TrackingNumber)){
                        Log.i("lineExec", line + " is equal to " + TrackingNumber);
                        isFavourite = true;
                    }
                }
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Log.i("fileNotFoundForFavouritesCSV", "" + fileNotFoundException);
            FileOutputStream fos = null;
            try {
                fos = activity.openFileOutput("favourites.csv", Context.MODE_PRIVATE);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isFavourite;
    }

    public void addToFavourites(Activity activity, String trackingNumber) {
        try {
            Log.i("saving to ", activity.getFilesDir() + "/favourites.csv");
            FileWriter fileWriter = new FileWriter(activity.getFilesDir() + "/favourites.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(trackingNumber);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFromFavourities(Activity activity, String trackingNumber) {
        File inputFile = new File(activity.getFilesDir() + "/favourites.csv");
        File tempFile = new File(activity.getFilesDir() + "/tempfavourites.csv");


        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            PrintWriter printWriter = new PrintWriter(writer);

            String currentLine;

            while ((currentLine = reader.readLine()) != null){
                if (currentLine.equals(trackingNumber)) continue;
                printWriter.println(currentLine);
            }
            reader.close();
            printWriter.flush();
            printWriter.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
