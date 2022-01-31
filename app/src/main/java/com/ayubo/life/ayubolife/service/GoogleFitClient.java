package com.ayubo.life.ayubolife.service;

import android.content.Context;
import android.util.Log;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.webrtc.App;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class GoogleFitClient {
    private final Context context;
    public static PrefManager pref;
    public static String userid_ExistingUser;
    private GoogleApiClient mApiClient;

    private static GoogleFitClient instance;
    private static final String TAG = GoogleFitClient.class.getSimpleName();


    private GoogleFitClient(Context context) {
        this.context = context;
        pref = new PrefManager(context);
        userid_ExistingUser=pref.getLoginUser().get("uid");
    }

    public static GoogleFitClient getInstance() {
        if (instance == null) {
            instance = new GoogleFitClient(App.getInstance());
        }
        return instance;
    }



    public static DataReadRequest queryFitnessData(int days) {

        Calendar secondDay = Calendar.getInstance();
        secondDay.add(Calendar.DATE, -days);

        SimpleDateFormat format1 = new SimpleDateFormat("dd-M-yyyy");
        String dateString = format1.format(secondDay.getTime());

        long endTime;
        long midNight;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        String startDateString = dateString + " 00:00:01";
        String endDateString = dateString + " 23:59:59";

        Calendar calendarStart = null;
        Calendar calendarEnd = null;
        try {
            //formatting the dateString to convert it into a Date
            Date sdate = sdf.parse(startDateString);
            Date edate = sdf.parse(endDateString);

            calendarStart = Calendar.getInstance();
            calendarEnd = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendarStart.setTime(sdate);
            //  System.out.println("Given Time in milliseconds Staart: "+calendars.getTimeInMillis());
            calendarEnd.setTime(edate);
            //  System.out.println("Given Time in milliseconds End: "+calendare.getTimeInMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }
        midNight = calendarStart.getTimeInMillis();
        endTime = calendarEnd.getTimeInMillis();
        //===================================================

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(24, TimeUnit.HOURS)
                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    public static String printData(DataReadResult dataReadResult) {
        String step = "0";
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    step = dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                step = dumpDataSet(dataSet);
            }

        }
        return step;
        // [END parse_read_data_result]
    }

    private static String dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();
         String stepCount=null;
        if (dataSet.isEmpty()) {
            System.out.println("========Empty======GFit Steps============");
            stepCount = "0";
        } else {
            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());

                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.i(TAG, "\tField: " + field.getName() +
                            "GFit Steps Value: " + dp.getValue(field));
                    stepCount = "0";
                    stepCount = dp.getValue(field).toString();
                    //   Ram.setGoogleFitSteps(stepCount);
                    System.out.println("==============GFit Steps Value:==================" + stepCount);
                }
            }
        }

        return stepCount;
    }




}
