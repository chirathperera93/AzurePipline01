package com.ayubo.life.ayubolife.webrtc;


import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.model.Booking;
import com.ayubo.life.ayubolife.channeling.model.Session;
import com.ayubo.life.ayubolife.prochat.di.DaggerMyComponent;
import com.ayubo.life.ayubolife.prochat.di.MyComponent;
import com.ayubo.life.ayubolife.prochat.di.module.MainModule;
import com.ayubo.life.ayubolife.reports.model.ChartDataMainResponse;
import com.ayubo.life.ayubolife.utility.LruBitmapCache;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import static com.facebook.GraphRequest.TAG;

//import com.facebook.appevents.AppEventsLogger;

/**
 * Created by appdev on 2/21/2017.
 */

public class App extends Application {
    private MyComponent appComponent;

    private static com.ayubo.life.ayubolife.webrtc.App instance;
    // private QBResRequestExecutor qbResRequestExecutor;

    public static GoogleApiClient mClient = null;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    public static com.ayubo.life.ayubolife.webrtc.App getInstance() {
        return instance;
    }

    public void setClient(GoogleApiClient client) {
        mClient = client;
    }

    public static GoogleApiClient getClient() {
        return mClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initApplication();

        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppEventsLogger.activateApp(this);

        appComponent = DaggerMyComponent.builder().mainModule(new MainModule(this, "AyuboLife", BuildConfig.BASE_URL))
                .build();

        appComponent.inject(this);
        ViewTarget.setTagId(R.id.glide_tag);

    }

    List<Session> channelDoctors;
    private Booking booking;
    private ChartDataMainResponse testReportMainData;

    public List<Session> getChannelDoctors() {
        return channelDoctors;
    }

    public void setChannelDoctors(List<Session> channelDoctors) {
        this.channelDoctors = channelDoctors;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ChartDataMainResponse getTestReportMainData() {
        return testReportMainData;
    }

    public void setTestReportMainData(ChartDataMainResponse testReportMainData) {
        this.testReportMainData = testReportMainData;
    }

    private void initApplication() {
        instance = this;
    }

//    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
//        return qbResRequestExecutor == null
//                ? qbResRequestExecutor = new QBResRequestExecutor()
//                : qbResRequestExecutor;
//    }
//
//    public <T> void addToRequestQueueSMS(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }


    public MyComponent getAppComponent() {
        return appComponent;
    }


}
