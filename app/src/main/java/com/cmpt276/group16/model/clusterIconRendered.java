package com.cmpt276.group16.model;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class clusterIconRendered extends DefaultClusterRenderer<restaurantItem>{
    public clusterIconRendered(Context context, GoogleMap map, ClusterManager<restaurantItem> clusterManager) {
        super(context, map, clusterManager);
    }
    @Override
    protected void onBeforeClusterItemRendered(restaurantItem item, MarkerOptions markerOptions){
        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
