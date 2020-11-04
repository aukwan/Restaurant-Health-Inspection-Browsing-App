package com.cmpt276.group16.model;

import java.util.ArrayList;

public class Inspection implements Comparable<Inspection> {
    private final String trackingNumber;
    private final int inspectionDate;
    private final String inspectionType;
    private final int NumCritical;
    private final int NumNonCritical;
    private final String hazardRated;
    private final ArrayList<Violations> violationsList = new ArrayList<>();
    private final String violationLump;

    public Inspection(String trackingNumber, int inspectionDate, String inspectionType, int NumCritical, int NumNonCritical, String hazardRated, String violationLump){
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.NumCritical = NumCritical;
        this.NumNonCritical = NumNonCritical;
        this.hazardRated = hazardRated;
        this.violationLump = violationLump;
        parseViolationLump();
    }

    private void parseViolationLump() {
        if(violationLump != null){
            String[] violations = violationLump.split("\\|");
            for(int k = 0; k < violations.length; k++){
                String[] temp = violations[k].split(",");
                Violations violation = new Violations(Integer.parseInt(temp[0]),temp[1],temp[2],temp[3]);
                violationsList.add(violation);
            }
        }

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
    public ArrayList<Violations> getViolationList() {
        return violationsList;
    }

    @Override
    public int compareTo(Inspection o) {
        return o.getInspectionDate()-this.getInspectionDate();
    }

}

