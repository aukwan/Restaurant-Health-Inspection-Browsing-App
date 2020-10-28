package com.cmpt276.group16.model;

import java.util.ArrayList;

public class Restaurant {
    private String trackingNumber;
    private String name;
    private String physicalAddress;
    private String physicalCity;
    private String facType;
    private Double latitude;
    private Double longitude;
    //TODO: add a drawable - drawn out randomly?
    //private int drawableIndex
    private ArrayList<Issues> issuesList = new ArrayList<>();

    public Restaurant(String trackingNumber, String name, String physicalAddress, String physicalCity, String facType, Double latitude, Double longitude){
        this.trackingNumber = trackingNumber;
        this.name = name;
        this.physicalAddress = physicalAddress;
        this.physicalCity = physicalCity;
        this.facType = facType;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void addIssue(Issues issue){
        issuesList.add(issue);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
    public String getName(){
        return name;
    }
    public String getPhysicalAddress(){
        return physicalAddress;
    }
    public String getPhysicalCity(){
        return physicalCity;
    }
    public String getFactype(){
        return facType;
    }
    public Double getLatitude(){
        return latitude;
    }
    public Double getLongitude(){
        return longitude;
    }
}
