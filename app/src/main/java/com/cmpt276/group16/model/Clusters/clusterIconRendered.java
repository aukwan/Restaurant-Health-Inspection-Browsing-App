package com.cmpt276.group16.model.Clusters;

import android.content.Context;


import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class clusterIconRendered extends DefaultClusterRenderer<restaurantItem> {
    private int index;
    public clusterIconRendered(Context context, GoogleMap map, ClusterManager<restaurantItem> clusterManager, int index) {
        super(context, map, clusterManager);
        this.index = index;
    }


    @Override
    protected void onBeforeClusterItemRendered(restaurantItem item, MarkerOptions markerOptions){
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
    }


    @Override
    protected void onClusterItemRendered(@NonNull restaurantItem clusterItem, @NonNull Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        if (index != -1){
            if (index == clusterItem.getTag()){
                getMarker(clusterItem).showInfoWindow();
                index = -1;
            }
        }
    }


}
