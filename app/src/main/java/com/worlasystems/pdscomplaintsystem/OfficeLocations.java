package com.worlasystems.pdscomplaintsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OfficeLocations extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PROXIMITY_RADIUS = 10000;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    Location currentLocation;
    private Boolean mLocationPermissionsGranted = false;



    private GoogleApiClient googleApiClient;
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_office_locations);
            label = findViewById(R.id.label);
            initMap();
        } catch (Exception e) {
            Toast.makeText(OfficeLocations.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        getLocationPermission();

    }


    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        try {
            if (mLocationPermissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                        } else {
                            Toast.makeText(OfficeLocations.this, "unable to find location", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }

        } catch (SecurityException se) {
            Toast.makeText(OfficeLocations.this, se.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }



    public void nearbyplaces(View v)
    {
        Object[] dataTransfer = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        currentLocation = mMap.getMyLocation();

        switch(v.getId())
        {

            case R.id.office:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(), hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                try{
                    getNearbyPlacesData.execute(dataTransfer);
                }catch (Exception e)
                {
                    Toast.makeText(OfficeLocations.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                label.setText("Nearby Offices");
                Toast.makeText(OfficeLocations.this, "Showing Nearby Offices", Toast.LENGTH_SHORT).show();
                break;

            case R.id.complaint_locations:
                mMap.clear();
                String school = "school";
                url = getUrl(currentLocation.getLatitude(),currentLocation.getLongitude(), school);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                try{
                    getNearbyPlacesData.execute(dataTransfer);
                }catch (Exception e)
                {
                    Toast.makeText(OfficeLocations.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                label.setText("Complainants");
                Toast.makeText(OfficeLocations.this, "Showing CLIENTS nearby", Toast.LENGTH_SHORT).show();
                break;

        }
    }



    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBPNkSm7M2uXvTDDi2ef9pzm4SR0tx5SZo");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            //get ting my current location
            getDeviceLocation();
            //set blue dot on my location
            mMap.setMyLocationEnabled(true);
            //remove default center icon
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private void init() {

//        googleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this,this)
//                .build();
//
//        //placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this,googleApiClient,LAT_LNG_BOUNDS,null);
//
//        mSearchText.setAdapter(placeAutocompleteAdapter);
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId== EditorInfo.IME_ACTION_SEARCH
//                ||actionId==EditorInfo.IME_ACTION_DONE
//                ||event.getAction()==KeyEvent.ACTION_DOWN
//                ||event.getAction()==KeyEvent.KEYCODE_ENTER){
//
//                    //execute my methods for searching
//                    geolocate();
//                }
//                return false;
//            }
//        });

    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;

                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }
}
