package com.cmpt276.group16.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Issues implements Comparable<Issues> {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int NumCritical;
    private int NumNonCritical;
    private String hazardRated;
    private ArrayList<Violations> violationsList = new ArrayList<>();
    private String violationLump;

    public Issues(String trackingNumber, int inspectionDate, String inspectionType, int NumCritical, int NumNonCritical, String hazardRated, String violationLump){
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.NumCritical = NumCritical;
        this.NumNonCritical = NumNonCritical;
        this.hazardRated = hazardRated;
        this.violationLump=violationLump;
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
    @Override
    public int compareTo(Issues o) {
        return o.getInspectionDate()-this.getInspectionDate();
    }

}

    //TODO: create a violations adder


