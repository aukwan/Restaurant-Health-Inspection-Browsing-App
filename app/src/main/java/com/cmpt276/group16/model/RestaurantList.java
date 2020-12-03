package com.cmpt276.group16.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/*

Restaurant List class with singleton implementation (Stories.iteration1)

 */


public class RestaurantList implements Iterable<Restaurant> {
    private final ArrayList<Restaurant> restaurantsList = new ArrayList<>();
    private ArrayList<Restaurant> filteredList;
    //singleton
    private static RestaurantList instance;

    public static RestaurantList getInstance() {
        if (instance == null) {
            instance = new RestaurantList();
        }
        return instance;
    }


    public void addRestaurant(Restaurant res) {
        restaurantsList.add(res);
        Collections.sort(restaurantsList);
    }

    public ArrayList<Restaurant> getRestArray() {
        if(filteredList==null)
            setFilteredList();
        ArrayList<Restaurant> restaurantList = new ArrayList<>(filteredList);
        return restaurantList;
    }

    public void setFilteredList(){
        if(filteredList==null)
            filteredList=new ArrayList<>(restaurantsList);
        else
            filteredList=restaurantsList;
    }

    public void setFilteredList(ArrayList<Restaurant> list){
        if(filteredList==null)
            filteredList=new ArrayList<>(list);
        else
            filteredList=list;
    }


    public Restaurant getRestaurant(int index) {
        if(filteredList==null)
            setFilteredList();
        return filteredList.get(index);
    }

    public boolean addIssues(Issues issue) {
        for (int i = 0; i < restaurantsList.size(); i++) {
            if (issue.getTrackingNumber().equals(restaurantsList.get(i).getTrackingNumber())) {
                restaurantsList.get(i).addIssue(issue);
                Collections.sort(restaurantsList.get(i).getIssuesList());
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
