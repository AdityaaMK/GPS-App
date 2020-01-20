package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    Location previousLocation;
    ArrayList<Address> addresses;
    Geocoder geocoder;
    TextView lon, lat, address, deltaX;
    double longitude, latitude, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lon = findViewById(R.id.id_longitude);
        lat = findViewById(R.id.id_latitude);
        address = findViewById(R.id.id_address);
        addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.US);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                lon.setText("Longitude: " +longitude);
                lat.setText("Latitude: " +latitude);

                try {
                    addresses.add(geocoder.getFromLocation(latitude, longitude, 1).get(0));
                    address.setText("Address: "+addresses.get(addresses.size()-1).getAddressLine(0));
                    if(previousLocation!=null) {
                        distance += location.distanceTo(previousLocation);
                        deltaX.setText("Distance: "+distance/1609.34+" miles");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                previousLocation = location;

        }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }


            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

}
