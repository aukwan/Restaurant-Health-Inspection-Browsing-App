package com.cmpt276.group16.model;

import java.util.ArrayList;
import java.util.Iterator;

public class RestaurantList implements Iterable<Restaurant>{
    private ArrayList<Restaurant> restaurantsList = new ArrayList<>();
    //singleton
    private static RestaurantList instance;
    public static RestaurantList getInstance() {
        if (instance == null){
            instance = new RestaurantList();
        }
        return instance;
    }


    public void addRestaurant(Restaurant res) {
        restaurantsList.add(res);
    }
    public ArrayList<Restaurant> getRestArray() {
        return restaurantsList;
    }
    public Restaurant getRestaurant(int index) {
        return restaurantsList.get(index);
    }

    public boolean addInspection(Inspection issue) {
        for (int i = 0; i < restaurantsList.size(); i++){
            if (issue.getTrackingNumber().equals(restaurantsList.get(i).getTrackingNumber())) {
                restaurantsList.get(i).addInspection(issue);
                return true;
            }
        }

        return false;
    }


    @Override
    public Iterator<Restaurant> iterator() {
        return restaurantsList.iterator();
    }
}
