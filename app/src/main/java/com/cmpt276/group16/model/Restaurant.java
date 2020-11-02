package com.cmpt276.group16.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Restaurant implements Comparable<Restaurant> {
    private String trackingNumber;
    private String name;
    private String physicalAddress;
    private String physicalCity;
    private String facType;
    private Double latitude;
    private Double longitude;
    private String violationDump;
    //TODO: add a drawable - drawn out randomly?
    //private int drawableIndex
    private ArrayList<Issues> issuesList = new ArrayList<>();

    public Restaurant(String trackingNumber, String name, String physicalAddress, String physicalCity, String facType, Double latitude, Double longitude, String violationDump){
        this.trackingNumber = trackingNumber;
        this.name = name;
        this.physicalAddress = physicalAddress;
        this.physicalCity = physicalCity;
        this.facType = facType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.violationDump = violationDump;
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
    public String getFacType(){
        return facType;
    }
    public Double getLatitude(){
        return latitude;
    }
    public Double getLongitude(){
        return longitude;
    }

    public ArrayList<Issues> getIssuesList(){return issuesList;}

    @Override
    public int compareTo(Restaurant o) {
        return Comparators.name.compare(this, o);
    }
    public static class Comparators{
        public static Comparator<Restaurant> name=new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
    }
}
