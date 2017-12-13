package com.technoxol.mandepos;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;

/**
 * Created by Jawad Zulqarnain on 9/28/2017.
 */

public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 3; // 1 minute
    public Timer timer1;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    //public GetLoc getLoc;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private Context mContext;


    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
        getCurrentLocation();
    }

    public Location getLocation() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.e("GPS_Status = ", "Is GPS Enabled = " + isGPSEnabled
                    + "\nIs Network Enabled = " + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
//                Toast.makeText(getApplicationContext(), "no availability of isGPSEnabled && isNetworkEnabled" , Toast.LENGTH_LONG).show();
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                //timer1=new Timer();
                // timer1.schedule(new GetLoc(), 10000);

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = getLatitude();
                            longitude = getLongitude();
                        }
                    } else {
//
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//                        Toast.makeText(getApplicationContext(), "gps longitude: "+location.getLongitude() , Toast.LENGTH_LONG).show();

                    }
                }
////                else {
//////                    Toast.makeText(mContext, "no gps " , Toast.LENGTH_LONG).show();
////                }
            }

        } catch (Exception e) {
//            Toast.makeText(mContext, "No Location Name Found\nPlease Check Internet Connection" , Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to open settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void getCurrentLocation() {
        LocationManager locationManager;
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setBearingRequired(false);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crta, true);

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            Log.e("WithinPermission","Permission Required");
//            ActivityCompat.requestPermissions((AppCompatActivity)mContext,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    1);
//        }else if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            Log.e("WithinPermission","Permission Required");
//            ActivityCompat.requestPermissions((AppCompatActivity)mContext,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    1);
        }
        locationManager.requestLocationUpdates(provider, 1000, 0,
                new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            System.out.println("WE GOT THE LOCATION");
                            Log.e("InSideLocationChange", "WE GOT THE LOCATION");
                            if (lat != 0.0 && lng != 0.0) {
                                System.out.println("WE GOT THE LOCATION");
                                System.out.println(lat);
                                System.out.println(lng);
                            }
                        }

                    }

                });
    }
}

