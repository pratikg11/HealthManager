package com.fitbit.sampleandroidoauth2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {
    private static final String TAG = NetworkCheck.class.getSimpleName();

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            return false;
        } else {
            if (info.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
