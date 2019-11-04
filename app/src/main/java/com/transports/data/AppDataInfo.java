package com.transports.data;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AppDataInfo extends Application {


    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppDataInfo.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppDataInfo.context;
    }

    public static List<String> availableTransports = new ArrayList<>();

    public static List<Stop> stops = new ArrayList<>();

    //for test only


}
