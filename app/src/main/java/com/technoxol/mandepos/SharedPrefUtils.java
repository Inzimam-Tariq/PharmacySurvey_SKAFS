package com.technoxol.mandepos;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefUtils {

    private static final String PREFS_NAME = "pref";
    private Context mContext;
    private Utils utils;


    public SharedPrefUtils(Context mContext) {
        this.mContext = mContext;
        utils = new Utils(mContext);
    }


    public String getSharedPrefValue(String sharedPrefTitle) {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(sharedPrefTitle, null);
    }

    public boolean getActivityPrefValue(String sharedPrefTitle) {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(sharedPrefTitle, false);
    }

    public void saveSharedPrefValue(String sharedPrefTitle, String sharedPrefValue) {
        try {
            SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString(sharedPrefTitle, sharedPrefValue);
            editor.apply();
        } catch (Exception e) {
//            utils.printStackTrace(e);
        }
    }

    public void saveActivityPrefValue(String sharedPrefTitle, boolean sharedPrefValue) {
        try {
            SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(sharedPrefTitle, sharedPrefValue);
            editor.apply();
        } catch (Exception e) {
//            utils.printStackTrace(e);
        }
    }


    public void removeSharedPrefValue(String sharedPrefTitle)
    {
        try
        {
            SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(sharedPrefTitle);
            editor.apply();
        }
        catch (Exception e)
        {
//            utils.printStackTrace(e);
        }
    }

    public void clearSharedPref() {
        try {
            SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
        } catch (Exception e) {
//            utils.printStackTrace(e);
        }
    }

}
