package com.cmpt276.group16.model;

import java.util.ArrayList;

public class Issues {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int NumCritical;
    private int NumNonCritical;
    private String hazardRated;
    private ArrayList<Violations> violationsList = new ArrayList<>();

    public Issues(String trackingNumber, int inspectionDate, String inspectionType, int NumCritical, int NumNonCritical, String hazardRated){
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.NumCritical = NumCritical;
        this.NumNonCritical = NumNonCritical;
        this.hazardRated = hazardRated;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public int getInspectionDate() {
        return inspectionDate;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public int getNumCritical() {
        return NumCritical;
    }

    public int getNumNonCritical() {
        return NumNonCritical;
    }

    public String getHazardRated() {
        return hazardRated;
    }


    //TODO: create a violations adder


}
