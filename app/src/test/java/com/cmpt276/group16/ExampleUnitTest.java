package com.cmpt276.group16;

import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addRestaurant() {
        RestaurantList tester = RestaurantList.getInstance();
        String trackingNumber = "SDFO-8HKP7E";
        String name = "Pattullo A&W";
        String physicalAddress = "12808 King George Blvd";
        String physicalCity = "Surrey";
        String facType = "Restaurant";
        Double latitude = 49.20611;
        Double longitude = -122.867;
        Restaurant restaurantTest = new Restaurant(trackingNumber, name, physicalAddress, physicalCity, facType, latitude, longitude);
        tester.addRestaurant(restaurantTest);
        trackingNumber = "SDFO-8HKP7E";
        name = "attullo A&W";
        physicalAddress = "12808 King George Blvd";
        physicalCity = "Surrey";
        facType = "Restaurant";
        latitude = 49.20611;
        longitude = -122.867;
        restaurantTest = new Restaurant(trackingNumber, name, physicalAddress, physicalCity, facType, latitude, longitude);
        tester.addRestaurant(restaurantTest);
        assertEquals(tester.getRestaurant(0).getName(), name);
        int inspectionDate = 20191002;
        String inspectionType = "Routine";
        int NumCritical = 0;
        int NumNonCritical = 0;
        String hazardRated = "Low";
        String violLump = "";
        Issues issue1 = new Issues(trackingNumber, inspectionDate, inspectionType, NumCritical, NumNonCritical, hazardRated, violLump);
        tester.addIssues(issue1);

        inspectionDate = 20181024;
        inspectionType = "Follow-Up";
        NumCritical = 0;
        NumNonCritical = 1;
        hazardRated = "Low";
        Issues issue2 = new Issues(trackingNumber, inspectionDate, inspectionType, NumCritical, NumNonCritical, hazardRated, violLump);
        tester.addIssues(issue2);


    }
}