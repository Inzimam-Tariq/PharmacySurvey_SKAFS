package com.technoxol.mandepos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/**
 * Created by ArdSoft on 4/11/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public CustomDialogs customDialogs;
    public SharedPrefUtils sharedPrefUtils;
    public HttpService httpService;
    public Utils utils;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initUtils() {
        utils = new Utils(this);
        httpService = new HttpService(this);
        sharedPrefUtils = new SharedPrefUtils(this);
        customDialogs = new CustomDialogs(this);
        GPSTracker tracker = new GPSTracker(this);
        String lat = String.valueOf(tracker.getLatitude());
        String lng = String.valueOf(tracker.getLongitude());
        Log.e("LocationInBase=", tracker.getLocation()
                + "\nLat = " + lat + "\nLong"+lng);
    }
}

