package com.example.adslle.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "BOOK_APP_SETTINGS";


    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getSomeStringValue(Context context,String key) {
        return getSharedPreferences(context).getString(key , null);
    }

    public static void setSomeStringValue(Context context, String key,String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key , newValue);
        editor.commit();
    }

    public static boolean isAuthenticated(){
        if (SharedPreferencesManager.getSomeStringValue(MyApplication.getAppContext(),"token")!=null) {
            return true;
        }
        return false;
    }

}
