package com.cmpt276.group16.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Restaurant implements Comparable<Restaurant> {
    private final String trackingNumber;
    private final String name;
    private final String physicalAddress;
    private final String physicalCity;
    private final String facType;
    private final Double latitude;
    private final Double longitude;
    private String violationDump;
    private String mostRecentHazardLevels = "Low";

    //private int drawableIndex
    private final ArrayList<Issues> issuesList = new ArrayList<>();

    public Restaurant(String trackingNumber, String name, String physicalAddress, String physicalCity, String facType, Double latitude, Double longitude) {
        this.trackingNumber = trackingNumber;
        this.name = name;
        this.physicalAddress = physicalAddress;
        this.physicalCity = physicalCity;
        this.facType = facType;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getMostRecentHazardLevels(){ return mostRecentHazardLevels;}
    public void setMostRecentHazardLevels(String hazardLevels){mostRecentHazardLevels = hazardLevels;}


    public void addIssue(Issues issue) {
        issuesList.add(issue);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public String getPhysicalCity() {
        return physicalCity;
    }

    public String getFacType() {
        return facType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public ArrayList<Issues> getIssuesList() {
        return issuesList;
    }

    @Override
    public int compareTo(Restaurant o) {
        return Comparators.name.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Restaurant> name = new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
    }
}
