package com.example.adslle.test;

import android.app.Application;
import android.content.Context;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class MyApplication  extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
