package com.cmpt276.group16.ui;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.cmpt276.group16.R;
import com.cmpt276.group16.model.Clusters.CustomInfoWindowAdapter;
import com.cmpt276.group16.model.Issues;
import com.cmpt276.group16.model.Restaurant;
import com.cmpt276.group16.model.RestaurantList;
import com.cmpt276.group16.model.Clusters.clusterIconRendered;
import com.cmpt276.group16.model.Clusters.restaurantItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.clustering.ClusterManager;


/*
            Restaurant map activity that displays a Google Map using the Google Map API(Stories.iteration2)
 */

public class RestaurantMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1254;
    private boolean mLocationPermissionGranted = false;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ClusterManager<restaurantItem> clusterManager;

    private final RestaurantList restaurantManager = RestaurantList.getInstance();
    private int mPosition;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private int restaurantIndex = -1;
    private clusterIconRendered mRenderer;

    private boolean toggleCameraFollow = false;


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            setUpClusterer();
            //sets user location:

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //gui textbox
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (toggleCameraFollow == false) {
                        toggleCameraFollow = true;
                        requestLocationUpdates(true);
                    }
                    Toast.makeText(RestaurantMapsActivity.this, "Toggle Camera follow on", Toast.LENGTH_SHORT).show();
                    return true;
                }

            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (toggleCameraFollow == true) {
                        requestLocationUpdates(false);
                        Toast.makeText(RestaurantMapsActivity.this, "Toggle Camera follow off", Toast.LENGTH_SHORT).show();
                        toggleCameraFollow = false;
                    }
                }
            });
            //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(RestaurantMapsActivity.this));
            clusterManager.getMarkerCollection().setInfoWindowAdapter(new CustomInfoWindowAdapter(RestaurantMapsActivity.this));
            //sets the markers for all the locations
            addItem();
            getDeviceLocation();
        }
    }


    private void addItem() {
        for (int i = 0; i < restaurantManager.getRestArray().size(); i++) {
            mPosition = i;
            LatLng latLng = new LatLng(restaurantManager.getRestArray().get(i).getLatitude(), restaurantManager.getRestArray().get(i).getLongitude());
            Restaurant currentRestaurant = restaurantManager.getRestArray().get(i);
            BitmapDescriptor mIcon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("greendot", 100, 100));
            String mSnippet = "City: " + currentRestaurant.getPhysicalCity() + "\n" + "Address: " + currentRestaurant.getPhysicalAddress();
            String mTitle = restaurantManager.getRestArray().get(i).getName();

            if (currentRestaurant.getIssuesList().size() != 0) {
                Issues currentIssues = currentRestaurant.getIssuesList().get(0);
                String hazardLevel = currentIssues.getHazardRated();
                mSnippet = "City: " + currentRestaurant.getPhysicalCity() + "\n" + "Address: " + currentRestaurant.getPhysicalAddress() + "\n" + "Hazard Level: " + hazardLevel;
                if (hazardLevel.equals("High")) {
                    mIcon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("reddot", 100, 100));
                } else if (hazardLevel.equals("Moderate")) {
                    mIcon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("yellowdot", 100, 100));
                }
            }

            restaurantItem item = new restaurantItem(latLng, mTitle, mSnippet, mIcon, mPosition);
            clusterManager.addItem(item);
            clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<restaurantItem>() {
                @Override
                public void onClusterItemInfoWindowClick(restaurantItem item) {
                    int position = item.getTag();
                    saveRestaurantIndex(position);
                    Intent intent = new Intent(RestaurantMapsActivity.this, RestaurantUI.class);
                    startActivity(intent);

                }
            });

        }
    }

    private Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    //shared preference
    private void saveRestaurantIndex(int restaurantIndex) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Restaurant List - Index", restaurantIndex);
        editor.apply();
    }

    private void setUpClusterer() {
        clusterManager = new ClusterManager<restaurantItem>(RestaurantMapsActivity.this, mMap);
        mRenderer = new clusterIconRendered(RestaurantMapsActivity.this, mMap, clusterManager, restaurantIndex);
        clusterManager.setRenderer(mRenderer);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_maps);
        getLocationPermission();

        configureSearchBar();
        registerClickCallback();

        Intent intent = getIntent();
        restaurantIndex = intent.getIntExtra("restaurant index", -1);
        Log.e(TAG, "Restaurant Index: " + restaurantIndex);


    }

    private void requestLocationUpdates(boolean isRequested) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RestaurantMapsActivity.this);
        if (mLocationPermissionGranted) {
            if (isRequested) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                //Toast.makeText(RestaurantMapsActivity.this, "unable to get current location - Lat and Long", Toast.LENGTH_SHORT).show();
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(500);
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                Double wayLatitude = location.getLatitude();
                                Double wayLongitude = location.getLongitude();
                                LatLng currLatiAndLong = new LatLng(wayLatitude, wayLongitude);
                                moveCamera(currLatiAndLong, DEFAULT_ZOOM);
                            } else {
                                Toast.makeText(RestaurantMapsActivity.this, "unable to get current location - Lat and Long", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

            }
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RestaurantMapsActivity.this);
        try {
            if (mLocationPermissionGranted) {
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();

                if (location != null) {
                    location.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "onComplete: found location");
                            if (location != null) {
                                if (restaurantIndex != -1) {
                                    Double latitude = restaurantManager.getRestaurant(restaurantIndex).getLatitude();
                                    Double longitude = restaurantManager.getRestaurant(restaurantIndex).getLongitude();
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    Log.e(TAG, "latitude for index " + latitude + "longitude for index " + longitude);
                                    moveCamera(latLng, DEFAULT_ZOOM);
                                } else {
                                    Double wayLatitude = location.getLatitude();
                                    Double wayLongitude = location.getLongitude();
                                    LatLng currLatiAndLong = new LatLng(wayLatitude, wayLongitude);
                                    moveCamera(currLatiAndLong, DEFAULT_ZOOM);
                                }
                                onRestart();
                            } else {
                                locationRequest = LocationRequest.create();
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationRequest.setInterval(500);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        if (locationResult == null) {
                                            return;
                                        }
                                        for (Location location : locationResult.getLocations()) {
                                            if (location != null) {
                                                Double wayLatitude = location.getLatitude();
                                                Double wayLongitude = location.getLongitude();
                                                LatLng currLatiAndLong = new LatLng(wayLatitude, wayLongitude);
                                                moveCamera(currLatiAndLong, DEFAULT_ZOOM);
                                            } else {
                                                Toast.makeText(RestaurantMapsActivity.this, "unable to get current location - Lat and Long", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                            }
                        }

                    });

                }
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    //moves camera
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to : lat: " + latLng.latitude + " lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //Gets the location permision
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    //Initializes the map
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(RestaurantMapsActivity.this);

        // Get center location on user button
        View mapView = mapFragment.getView();
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // Position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            break;
                        }
                        mLocationPermissionGranted = true;
                        //initialize the map
                        initMap();
                    }
                }
            }
        }
    }

    private void configureSearchBar() {
        SearchView searchView = (SearchView) findViewById(R.id.mapSearchBar);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void registerClickCallback() {
        BottomNavigationView bottomNav = findViewById(R.id.maps_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_restaurant_list:
                        finish();
                        Intent intent = new Intent(RestaurantMapsActivity.this, MainActivity.class);
                        startActivity(intent);

                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}