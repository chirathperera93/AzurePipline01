package com.ayubo.life.ayubolife.webrtc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import com.ayubo.life.ayubolife.R;


import java.util.List;

/**
 * QuickBlox team
 */
public class SettingsUtil {

    private static final String TAG = SettingsUtil.class.getSimpleName();









    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int strResKey, int strResDefValue) {
        return sharedPref.getString(context.getString(strResKey), context.getString(strResDefValue));
    }

    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int strResKey, String strResDefValue) {
        return sharedPref.getString(context.getString(strResKey), strResDefValue);
    }

    public static int getPreferenceInt(SharedPreferences sharedPref, Context context, int strResKey, int strResDefValue) {
        return sharedPref.getInt(context.getString(strResKey), Integer.valueOf(context.getString(strResDefValue)));
    }

    private static boolean getPreferenceBoolean(SharedPreferences sharedPref, Context context, int StrRes, int strResDefValue) {
        return sharedPref.getBoolean(context.getString(StrRes), Boolean.valueOf(context.getString(strResDefValue)));
    }
}
