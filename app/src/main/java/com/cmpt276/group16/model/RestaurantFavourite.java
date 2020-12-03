package com.cmpt276.group16.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
/*
        Iteration 3 : Class that holds all the restaurants that are favourited
 */


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
                    Log.i("lineExec", line + " is equal to " + TrackingNumber + "????????");
                    if (line.contains(TrackingNumber)){
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
            FileOutputStream fosFav = null;
            FileOutputStream fosIns = null;
            try {
                fosFav = activity.openFileOutput("favourites.csv", Context.MODE_PRIVATE);
                fosIns = activity.openFileOutput("newinspections.csv", Context.MODE_PRIVATE);

                fosFav.close();
                fosIns.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isFavourite;
    }

    public void hasNewInspections(Activity activity) {
        FileInputStream fisFavourites = null;
        FileInputStream fisInspections = null;
        try {
            File favouritesFile = new File(activity.getFilesDir() + "/favourites.csv");
            File inspectionsFile = new File(activity.getFilesDir() + "/inspectionreports.csv");


            fisFavourites = new FileInputStream(favouritesFile);
            InputStreamReader isrFavourites = new InputStreamReader(fisFavourites);

            fisInspections = new FileInputStream(inspectionsFile);
            InputStreamReader isrInspections = new InputStreamReader(fisInspections);

            BufferedReader readerInspections = new BufferedReader(isrInspections);
            BufferedReader readerFavourites = new BufferedReader(isrFavourites);

            String lineFavourite;
            String lineInspection;

            FileWriter fileWriter = new FileWriter(activity.getFilesDir() + "/newinspections.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            try {
                while ((lineFavourite = readerFavourites.readLine()) != null) {
                    String[] lineFavouriteValues = lineFavourite.split(",");
                    while ((lineInspection = readerInspections.readLine()) != null){
                        if (lineInspection.contains(lineFavouriteValues[0])){
                            String [] lineInspectionValues = lineInspection.split(",");
                            if (Integer.parseInt(lineInspectionValues[1]) > Integer.parseInt(lineFavouriteValues[1])){
                                String restaurantName = lineFavouriteValues[2];
                                String hazardLevel = lineInspectionValues[lineInspectionValues.length-1];
                                int date = Integer.parseInt(lineInspectionValues[1]);

                                printWriter.println(date + "," + restaurantName + "," + hazardLevel);
                                printWriter.flush();
                                printWriter.close();
                            }
                        }
                    }
                }
                isrFavourites.close();
                isrInspections.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Log.i("fileNotFound", "" + fileNotFoundException);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToFavourites(Activity activity, String trackingNumber, int latestInspectionDate, String restaurantName) {
        try {
            Log.i("saving to ", activity.getFilesDir() + "/favourites.csv");
            FileWriter fileWriter = new FileWriter(activity.getFilesDir() + "/favourites.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(trackingNumber + "," + latestInspectionDate + "," + '"' + restaurantName + '"');
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
                if (currentLine.contains(trackingNumber)) {
                    continue;
                }
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
