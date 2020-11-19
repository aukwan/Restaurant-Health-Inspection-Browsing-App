package com.cmpt276.group16.model;

import java.util.ArrayList;
/*

Issues class for issues data (Stories.iteration1)

 */

public class Issues implements Comparable<Issues> {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int numCritical;
    private int numNonCritical;
    private String hazardRated;
    private ArrayList<Violations> violationsList = new ArrayList<>();
    private String violationLump;

    public Issues(String trackingNumber, int inspectionDate, String inspectionType, int numCritical, int numNonCritical, String hazardRated, String violationLump) {
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numCritical = numCritical;
        this.numNonCritical = numNonCritical;
        this.hazardRated = hazardRated;
        this.violationLump = violationLump;
        parseViolationLump();
    }

    private void parseViolationLump() {
        if (violationLump != null) {
            String[] violations = violationLump.split("\\|");
            for (int k = 0; k < violations.length; k++) {
                String[] temp = violations[k].split(",");
                String temp2 = temp[2];
                for (int j = 3; j < temp.length - 1; j++) {
                    temp2 += "," + temp[j];
                }
                Violations violation = new Violations(Integer.parseInt(temp[0]), temp[1], temp2, temp[temp.length - 1]);
                violationsList.add(violation);
            }
        }
    }

    ;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public int getIssueDate() {
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

    public String getHazardRated() {
        return hazardRated;
    }

    public ArrayList<Violations> getViolationList() {
        return violationsList;
    }

    @Override
    public int compareTo(Issues o) {
        return o.getIssueDate() - this.getIssueDate();
    }

}

