package com.ayubo.life.ayubolife.huawei_hms;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GoogleSupportServices {

    Context currentContext;

    public GoogleSupportServices(Context context) {
        this.currentContext = context;
    }

    public boolean isGooglePlayServicesAvailable() {

        try {
            int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.currentContext);

//        System.out.println(b);

//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.currentContext);
            if (resultCode == ConnectionResult.SUCCESS) {
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}

