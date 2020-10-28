package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;



public class RestaurantUI extends AppCompatActivity {
    private int restaurantIndex;
    private RestaurantList restaurantManager = RestaurantList.getInstance();
    private ArrayAdapter<Issues> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_u_i);
        extractDataFromIntent();
        setupTextViews();

        populateListView();
        registerClickCallback();
    }

    //setup the textview for UI
    private void setupTextViews(){
        String restaurantName = restaurantManager.getRestArray().get(restaurantIndex).getName();
        String restaurantAddress = restaurantManager.getRestArray().get(restaurantIndex).getPhysicalCity() + ", " +
                restaurantManager.getRestArray().get(restaurantIndex).getPhysicalAddress();
        String restaurantGPS = restaurantManager.getRestArray().get(restaurantIndex).getLatitude() + "," +
                restaurantManager.getRestArray().get(restaurantIndex).getLongitude();
        TextView textView = (TextView) findViewById(R.id.restaurantName);
        textView.setText(restaurantName);
        textView = (TextView) findViewById(R.id.restaurantAddress);
        textView.setText(restaurantAddress);
        textView = (TextView) findViewById(R.id.restaurantGPS);
        textView.setText(restaurantGPS);
    }

    //make intent to know the position in which restaurant was chosen
    public static Intent makeIntent(Context context, int value) {
        Intent intent = new Intent(context, RestaurantUI.class);
        intent.putExtra("Restaurant List - Index", value);
        return intent;
    }
    //Extract intent data
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        restaurantIndex = intent.getIntExtra("Restaurant List - Index", 0);
    }


    //POPULATES THE LIST VIEW
    private void populateListView() {
        adapter = new RestaurantUI.MyListAdapter();
        ListView list = findViewById(R.id.listViewIssues);
        list.setAdapter(adapter);

    }
    //ADAPTER
    private class MyListAdapter extends ArrayAdapter<Issues>{
        public MyListAdapter() {
            super(RestaurantUI.this, R.layout.issueslistview, restaurantManager.getRestArray().get(restaurantIndex).getIssuesList());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.issueslistview, parent, false);
            }
            Issues currentIssues = restaurantManager.getRestArray().get(restaurantIndex).getIssuesList().get(position);
            //TODO: add icons for hazard level, and other stuff

            String numCrit = "Critical issues#: " + Integer.toString(currentIssues.getNumCritical());
            String numNonCrit = "Non - Critical issues#: " + Integer.toString(currentIssues.getNumNonCritical());
            String inspectDate = "Inspection Date: " + Integer.toString(currentIssues.getInspectionDate());
            TextView textView  = (TextView) itemView.findViewById(R.id.criticalIssues);
            textView.setText(numCrit);
            textView  = (TextView) itemView.findViewById(R.id.nonCriticalIssues);
            textView.setText(numNonCrit);
            textView  = (TextView) itemView.findViewById(R.id.inspectionDate);
            textView.setText(inspectDate);
            return itemView;
        }

    }
    //LISTVIEW BUTTONS
    private void registerClickCallback() {

        ListView list = (ListView) findViewById(R.id.listViewIssues);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = RestaurantUI.makeIntent(MainActivity.this, position);
//                startActivity(intent);
            }
        });

    }

}