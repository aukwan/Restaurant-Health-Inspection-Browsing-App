package com.cmpt276.group16.model;

import java.util.ArrayList;

public class Inspection {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int numCritical;
    private int numNonCritical;
    private String hazardRating;
    private ArrayList<Violations> violationsList = new ArrayList<>();

    public Inspection(String trackingNumber, int inspectionDate, String inspectionType, int NumCritical, int NumNonCritical, String hazardRating){
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numCritical = NumCritical;
        this.numNonCritical = NumNonCritical;
        this.hazardRating = hazardRating;
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
        return numCritical;
    }

    public int getNumNonCritical() {
        return numNonCritical;
    }

    public String getHazardRating() {
        return hazardRating;
    }

    public ArrayList<Violations> getViolationList() {
        return violationsList;
    }

    //TODO: create a violations adder

}
