package za.ac.cput.pandem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import za.ac.cput.pandem.CovidInformation;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    //widgets
    private SearchView searchView;
    //vars
    private Boolean mLocationPermissionsGranted;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_screen);
        searchView = (SearchView) findViewById(R.id.idSearchView);
        getLocationPermission();

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {



    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }

                    return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(MapActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onMapReady: map is ready");
                mMap = googleMap;





                if (mLocationPermissionsGranted) {
                    getDeviceLocation();

                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                    addHeatMap();

                }


            }
        });


    }




    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device's current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());

        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();


    }



    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
               mLocationPermissionsGranted = true;
               initMap();
            }

            }else{
            ActivityCompat.requestPermissions(this, permissions,LOCATION_PERMISSION_REQUEST_CODE);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsRequest: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize  the map
                    initMap();
                }
            }
        }


    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //heatmap

    int[] colors = {
            Color.GREEN,    // green
            Color.YELLOW,    // yellow
            Color.rgb(255,165,0), //Orange
            Color.RED,              //red
            Color.rgb(153,50,204), //dark orchid
            Color.rgb(165,42,42) //brown
    };

    float[] startpoints = {
            0.1f,   //51-100
            0.2f,   //101-150
            0.3f,   //151-200
            0.4f,    //201-300
            0.6f,     //301-500
            0.7f
    };


    private ArrayList heatmapValues() {
        ArrayList<WeightedLatLng> arr = new ArrayList<>();

        arr.add(new WeightedLatLng(new LatLng(-26.358055, 27.398056),113));
        arr.add(new WeightedLatLng(new LatLng(-29.1, 26.2167),37));
        arr.add(new WeightedLatLng(new LatLng(-29.883333, 31.049999),73));
        arr.add(new WeightedLatLng(new LatLng(-26.266111, 27.865833),105));
        arr.add(new WeightedLatLng(new LatLng(-29.087217, 26.154898),123));
        arr.add(new WeightedLatLng(new LatLng(-33.958252, 25.619022),150));
        arr.add(new WeightedLatLng(new LatLng(-26.563404, 27.844164),123));
        arr.add(new WeightedLatLng(new LatLng(-33.977074, 22.457581),103));
        arr.add(new WeightedLatLng(new LatLng(-26.120134, 27.901464),61));
        arr.add(new WeightedLatLng(new LatLng(-25.872782, 29.255323),73));
        arr.add(new WeightedLatLng(new LatLng(-26.205681, 28.046822),110));
        arr.add(new WeightedLatLng(new LatLng(-26.958405, 24.72986),260));
        arr.add(new WeightedLatLng(new LatLng(-34.41427, 19.248734),141));
        arr.add(new WeightedLatLng(new LatLng(-26.859823, 26.63175),125));
        arr.add(new WeightedLatLng(new LatLng(-26.23259, 28.240967),55));
        arr.add(new WeightedLatLng(new LatLng(-25.853161, 25.640181),58));
        arr.add(new WeightedLatLng(new LatLng(-26.7034212, 27.807695),115));
        arr.add(new WeightedLatLng(new LatLng(-29.851847, 30.993368),420));
        arr.add(new WeightedLatLng(new LatLng(-25.850187, 28.198042),82));
        arr.add(new WeightedLatLng(new LatLng(-33.844166, 18.69861),22));
        arr.add(new WeightedLatLng(new LatLng(-29.967422, 30.882959),489));
        arr.add(new WeightedLatLng(new LatLng(-33.811775, 25.384497),283));
        arr.add(new WeightedLatLng(new LatLng(-25.73134, 28.21837),122));
        arr.add(new WeightedLatLng(new LatLng(-26.107567, 28.056702),157));
        arr.add(new WeightedLatLng(new LatLng(-34.035088, 23.046469),180));
        Log.e("adding heatmap","yes");

        return arr;
    }


    private void addHeatMap(){

        Gradient gradient = new Gradient(colors,startpoints);
        HeatmapTileProvider Provider = new HeatmapTileProvider.Builder()
                .weightedData(heatmapValues())
                .radius(20)
                .opacity(0.7)
                .gradient(gradient)
                .build();
        TileOverlayOptions tileoverlayoptions = new TileOverlayOptions().tileProvider(Provider);
        TileOverlay tileoverlay = mMap.addTileOverlay(tileoverlayoptions);
        tileoverlay.clearTileCache();
        Toast.makeText(MapActivity.this, "Heatmap is ready", Toast.LENGTH_SHORT).show();
    }

}
