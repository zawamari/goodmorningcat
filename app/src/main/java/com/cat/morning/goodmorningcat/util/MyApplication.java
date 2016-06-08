package com.cat.morning.goodmorningcat.util;

import android.app.Activity;

public class MyApplication {

    public static boolean isTablet(Activity activity) {
        return activity.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
}
