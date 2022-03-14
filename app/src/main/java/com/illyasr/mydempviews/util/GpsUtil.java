package com.illyasr.mydempviews.util;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

public class GpsUtil {
    public static boolean isGpsEnabled(Context context) {
        final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if ( mgr == null )
            return false;
        final List<String> providers = mgr.getAllProviders();
        if ( providers == null )
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
//        return false;
    }

}
