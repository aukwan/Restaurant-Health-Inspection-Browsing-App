package com.cmpt276.group16.model.Clusters;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/*
        Cluster item/object that stores position, title, snippet, icon and tag (Stories.iteration2)
 */

public class restaurantItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final BitmapDescriptor icon;
    private final int tag;

    public restaurantItem(LatLng position, String title, String snippet, BitmapDescriptor icon, int tag) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.icon = icon;
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }


    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public String getSnippet() {
        return snippet;
    }
}
