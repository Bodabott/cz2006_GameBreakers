package com.example.gamebreakers.user;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.SQL;
import com.example.gamebreakers.entities.Stall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    Button backButton;
    TextView currentLocationTextView;
    private GoogleApiClient googleApiClient;
    private float benchmarkDistance = 5000;
    private LatLng defaultLatLng;
    private Address defaultAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLocationTextView = findViewById(R.id.currentLocation);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if(isInteger(marker.getTitle()) && marker.getTitle().length() == 6)
                    return false;

                Log.e(marker.getTitle(),marker.getPosition().toString());
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                mBuilder.setTitle("Go to Selected Stall:\n" + marker.getTitle());
                mBuilder.setMessage("Confirm?");
                mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("MAP",marker.getTitle());
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
                mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mBuilder.show();

                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void mapReset(){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(defaultAddress.getPostalCode()));
    }

    public boolean isCurrentLocationEmpty(){
        if(currentLocationTextView.getText().toString().length() == 18){
            Toast.makeText(MapsActivity.this,"Current Location Not Set",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public float getDistance(LatLng latLng){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addressList1 = null;
        List<Address> addressList2 = null;
        try{
            addressList1 = geocoder.getFromLocation(defaultLatLng.latitude,defaultLatLng.longitude,1);
            addressList2 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(addressList1 != null && addressList2 != null){
            Address address1 = addressList1.get(0);
            Address address2 = addressList2.get(0);

            Location defaultLocation = new Location("Default");

            defaultLocation.setLatitude(address1.getLatitude());
            defaultLocation.setLongitude(address1.getLongitude());

            Location targetLocation = new Location("Target");

            targetLocation.setLatitude(address2.getLatitude());
            targetLocation.setLongitude(address2.getLongitude());

            return defaultLocation.distanceTo(targetLocation);
        }
        return 0;
    }

    public ArrayList<LatLng> getLocationLatLongs(){
        List<Address> addressList = null;
        ArrayList<LatLng> latLngList = new ArrayList<>();
        ArrayList<String> locations = getLocationPostalCodes();

        for(String temp : locations){
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                addressList = geocoder.getFromLocationName(temp,1);
            }catch(IOException e){
                e.printStackTrace();
            }
            if(addressList != null){
                try{
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    latLngList.add(latLng);
                }catch(IndexOutOfBoundsException e){
                    Toast.makeText(getApplicationContext(),"Location not found!",Toast.LENGTH_LONG).show();
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Location not found.",Toast.LENGTH_LONG).show();
            }
        }
        return latLngList;
    }

    public ArrayList<Address> getLocationAddresses(){
        List<Address> addressList = null;
        ArrayList<Address> addressArrayList = new ArrayList<>();
        ArrayList<String> locations = getLocationPostalCodes();

        for(String temp : locations){
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                addressList = geocoder.getFromLocationName(temp,1);
            }catch(IOException e){
                e.printStackTrace();
            }
            if(addressList != null){
                try{
                    Address address = addressList.get(0);
                    addressArrayList.add(address);
                }catch(IndexOutOfBoundsException e){
                    Toast.makeText(getApplicationContext(),"Location not found!",Toast.LENGTH_LONG).show();
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Location not found.",Toast.LENGTH_LONG).show();
            }
        }
        return addressArrayList;
    }

    public ArrayList<String> getLocationPostalCodes() {
        Stall[] stallList = SQL.getArrayOfStall();
        ArrayList<String> stallNameArrayList = new ArrayList<>();
        ArrayList<String> postalCodeArrayList = new ArrayList<>();
        String temp;
        if (stallList != null) {
            for (Stall stall : stallList) {
                stallNameArrayList.add(stall.getStallName());
            }
            for(String stallName : stallNameArrayList){
                temp = "Singapore " + SQL.getOwnerPostalCode(stallName);
                postalCodeArrayList.add(temp);
            }
        }
        return postalCodeArrayList;
    }

    // Complex On-Click Methods

    public void searchNearbyStalls(View v){
        if(isCurrentLocationEmpty())
            return;

        mapReset();
        ArrayList<LatLng> latLngArrayList = getLocationLatLongs();
        ArrayList<Address> addressArrayList = getLocationAddresses();

        ArrayList<LatLng> filteredLatLngArrayList = new ArrayList<>();
        ArrayList<Address> filteredAddressArrayList = new ArrayList<>();
        ArrayList<String> filteredPostalCodeArrayList = new ArrayList<>();

        for(LatLng latLng : latLngArrayList) {
            if (getDistance(latLng) <= benchmarkDistance) {
                filteredLatLngArrayList.add(latLng);
                for(Address address : addressArrayList){
                    if(address.getLatitude() == latLng.latitude && address.getLongitude() == latLng.longitude){
                        filteredAddressArrayList.add(address);
                    }
                }
            }
        }

        if(filteredLatLngArrayList.size() == 0 || filteredAddressArrayList.size() == 0){
            Toast.makeText(v.getContext(),"No nearby stalls",Toast.LENGTH_LONG).show();
            return;
        }

        for(Address address : filteredAddressArrayList){
            filteredPostalCodeArrayList.add(address.getPostalCode());
        }

        for(LatLng latLng : filteredLatLngArrayList){
            for(Address address : filteredAddressArrayList){
                if(address.getLatitude() == latLng.latitude && address.getLongitude() == latLng.longitude){
                    Stall[] stalls = SQL.getArrayOfStall(Integer.parseInt(address.getPostalCode()));
                    if(stalls != null) {
                        Stall stall = stalls[0];
                        mMap.addMarker(new MarkerOptions().position(latLng).title(stall.getStallName()));
                    }
                }
            }
        }
        Toast.makeText(v.getContext(),filteredPostalCodeArrayList.size() + " Location(s) Found\n(Within 5 KM)",Toast.LENGTH_LONG).show();
    }

    public void setCurrentLocation(View v){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set Current Location:");
        // Set up the input
        final EditText input = new EditText(MapsActivity.this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if(m_Text.length() == 6){
                    m_Text = "Singapore " + input.getText().toString();
                }

                List<Address> addressList = null;

                if(!m_Text.isEmpty()){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try{
                        addressList = geocoder.getFromLocationName(m_Text,1);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    if(addressList != null){
                        ArrayList<String> stringArrayList = getLocationPostalCodes();
                        for(String postalCode : stringArrayList){
                            if(postalCode.matches(m_Text)){
                                Toast.makeText(MapsActivity.this,"Current Location matches an existing Stall.",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        try{
                            defaultAddress = addressList.get(0);
                            defaultLatLng = new LatLng(defaultAddress.getLatitude(),defaultAddress.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(defaultAddress.getPostalCode()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng,17));
                            String temp = "Current Location: Singapore " + defaultAddress.getPostalCode();
                            currentLocationTextView.setText(temp);
                        } catch(IndexOutOfBoundsException e){
                            Toast.makeText(MapsActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MapsActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        mBuilder.show();
    }

    public boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}

