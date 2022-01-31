package com.ayubo.life.ayubolife.lifeplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.walk_and_win.StepObj;
import com.ayubo.life.ayubolife.walk_and_win.WalkWinApiInterface;
import com.ayubo.life.ayubolife.walk_and_win.WalkWinStepsResponse;
import com.flavors.changes.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hihealth.DataController;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.data.SamplePoint;
import com.huawei.hms.hihealth.data.SampleSet;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class GoogleClientForDiscover {

    Context ctx;
    SimpleDateFormat sdf;
    public static GoogleApiClient mApiClient;
    private PrefManager pref;
    private static final String TAG = GoogleClientForDiscover.class.getSimpleName();
    FragmentActivity fragmentActivity;
    public static String stepStatus;
    //    public static JSONArray jsonStepsForWeekArray;
    public static List<StepObj> jsonStepsForWeekArrayForNewSave;
    String userid_ExistingUser, userid_ExistingUser_static, versionName, device_modal;
    String stepsSentToDBStatus;
    //    private ProgressDialog progressDialog;
    private Boolean isLinkedHuawei = false;
    public static List<StepObj> jsonStepsForDailyArrayForNewSave;
    String todayStepsToSendToServer;
    public static JSONArray jsonHuaweiStepsForWeekArray;

//    public static ArrayList<UserStepSyncObject> userStepSyncObjectArray = new ArrayList<UserStepSyncObject>();


    public GoogleClientForDiscover(Context context, FragmentActivity fragment, Boolean isSyncRightNow) {
        ctx = context;
        System.out.println("GoogleClientForDiscoverContext");
        try {
            fragmentActivity = fragment;
            pref = new PrefManager(ctx);
            jsonHuaweiStepsForWeekArray = new JSONArray();
            Long stepSyncTime = pref.getStepSyncTime();
            if (!isSyncRightNow) {
                if (stepSyncTime != null) {
                    long time = (System.currentTimeMillis() - stepSyncTime);

                    long minutes = (time / 1000) / 60;

                    if (minutes > 4) {
                        check_buildFitnessClient();
                    }
                } else {
                    check_buildFitnessClient();
                }
            } else {
                check_buildFitnessClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Syncing steps...");


    }

    private void check_buildFitnessClient() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        pref.setNewServiceLastRunningTime("0");
        pref.setMapReload(false);
//        jsonStepsForWeekArray = new JSONArray();
        jsonStepsForWeekArrayForNewSave = new ArrayList<StepObj>();
        userid_ExistingUser = pref.getLoginUser().get("uid");
        userid_ExistingUser_static = pref.getLoginUser().get("uid");
        versionName = BuildConfig.VERSION_NAME;
        device_modal = Build.MODEL;
        stepStatus = "good";

        GoogleSupportServices googleSupportServices = new GoogleSupportServices(fragmentActivity);

        if (googleSupportServices.isGooglePlayServicesAvailable()) {
            // Create the Google API Client
            if (mApiClient != null) {
                System.out.println("==========mClient not null=====================");
                if (mApiClient.isConnected()) {

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
//                dateFromDB_forADay0 = sdf.format(cal.getTime());


                    try {
                        System.out.println("=========send10Days_GFITSteps====================");
                        send10Days_GFITSteps();

                        // new InsertAndVerifyDataTask00().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    mApiClient.connect();
                    if (mApiClient.isConnected()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
//                    dateFromDB_forADay0 = sdf.format(cal.getTime());


                        try {
                            send10Days_GFITSteps();
                            // new InsertAndVerifyDataTask00().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                    }

                }

            } else {
                System.out.println("========== FIT Not Connected=====================");
                mApiClient = new GoogleApiClient.Builder(ctx)
                        .addApi(Fitness.RECORDING_API)
                        .addApi(Fitness.HISTORY_API)
                        .addApi(Fitness.SENSORS_API)
                        .addScope(Fitness.SCOPE_ACTIVITY_READ)
                        .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                        .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                        .addConnectionCallbacks(
                                new GoogleApiClient.ConnectionCallbacks() {
                                    @Override
                                    public void onConnected(Bundle bundle) {
                                        Log.i(TAG, "Connected!!!");
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(new Date());
//                                    dateFromDB_forADay0 = sdf.format(cal.getTime());
                                        //  Toast.makeText(NewHomeWithSideMenuActivity.this, "Getting GFIT Steps 0", Toast.LENGTH_LONG).show();
                                        //   Toast.makeText(NewHomeWithSideMenuActivity.this, "GoogleFit 0", Toast.LENGTH_LONG).show();

                                        try {
                                            send10Days_GFITSteps();
                                            //  new InsertAndVerifyDataTask00().execute();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onConnectionSuspended(int i) {
                                        // If your connection to the portrait gets lost at some point,
                                        // you'll be able to determine the reason and react to it here.
                                        pref.setGoogleFitEnabled("false");
                                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                            Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                            Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                        }
                                    }
                                }
                        )
                        .enableAutoManage(fragmentActivity, 0, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Google Play services connection failed. Cause: " +
                                        result.toString());
                                pref.setGoogleFitEnabled("false");
//                            Snackbar.make( android.R.id.content,
//                                    "Unable to connect GoogleFit",
//                                    Snackbar.LENGTH_INDEFINITE).setAction("DISMISS",
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                        }
//                                    }).show();

                            }
                        })
                        .build();
            }
        } else {
            send10Days_GFITSteps();

        }


    }

    private void send10Days_GFITSteps() {
        GoogleClientForDiscover.GFitGetSteps_LastWeek task = new GoogleClientForDiscover.GFitGetSteps_LastWeek();
        task.execute();
    }

    private class GFitGetSteps_LastWeek extends AsyncTask<Void, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... arg0) {

            GoogleSupportServices googleSupportServices = new GoogleSupportServices(fragmentActivity);

            if (googleSupportServices.isGooglePlayServicesAvailable()) {
                String ste = "0";

                DataReadRequest readRequest = queryFitnessDataLastWeek();
                DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.MINUTES);

                //This is new Testing one
//                userStepSyncObjectArray = new ArrayList<UserStepSyncObject>();
                printDataNewForLast(dataReadResult);

//                System.out.println(userStepSyncObjectArray);
//
//                JsonArray data = new Gson().toJsonTree(userStepSyncObjectArray).getAsJsonArray();
//
//                pref.saveStepSyncData(data);


                return ste;
            } else {
                HiHealthOptions hiHealthOptions = HiHealthOptions.builder()
                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT, HiHealthOptions.ACCESS_READ)
                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT, HiHealthOptions.ACCESS_WRITE)
                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_READ)
                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_WRITE)
                        .build();
                AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);
                DataController dataController = HuaweiHiHealth.getDataController(fragmentActivity, signInHuaweiId);
                getHueaweiWeeklyStepsAndSave task = new getHueaweiWeeklyStepsAndSave();
                task.execute(dataController);
                return null;

            }


        }

        @Override
        protected void onPostExecute(String steps) {
//            progressDialog.show();


            GoogleSupportServices googleSupportServices = new GoogleSupportServices(fragmentActivity);

            if (googleSupportServices.isGooglePlayServicesAvailable()) {
                send10Days_GFIT();
            }

        }

    }

    private class getHueaweiWeeklyStepsAndSave extends AsyncTask<DataController, Void, String> {

        String step = "0";

        @Override
        protected String doInBackground(DataController... dataController) {
            getHuaweiStepsForWeek(dataController[0]);
            return step;
        }

    }

    public void getHuaweiStepsForWeek(DataController dataController) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTimeInMillis();

        Task<SampleSet> daliySummationTask = dataController.readDailySummation(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, Integer.parseInt(dateFormat.format(startTime)), Integer.parseInt(dateFormat.format(endTime)));
        daliySummationTask.addOnSuccessListener(new OnSuccessListener<SampleSet>() {
            @Override
            public void onSuccess(SampleSet sampleSet) {
                if (sampleSet != null) {
                    showWeeklySampleSet(sampleSet);
                }
            }
        });
        daliySummationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {


//                progressDialog.dismiss();
            }
        });

    }

    private void showWeeklySampleSet(SampleSet sampleSet) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        for (SamplePoint dp : sampleSet.getSamplePoints()) {
            String strSteps = null;

            for (com.huawei.hms.hihealth.data.Field field : dp.getDataType().getFields()) {

                strSteps = dp.getFieldValue(field).toString();
//                jsonStepsForWeekArrayForNewSave.add(createGFitStepsDataArrayForNewSave(
//                        dp.getFieldValue(field).toString(),
//                        dp.getStartTime(TimeUnit.MILLISECONDS),
//                        dp.getEndTime(TimeUnit.MILLISECONDS)));


            }

            jsonStepsForWeekArrayForNewSave.add(createGFitStepsDataArrayForNewSave(strSteps, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS), "11", dp.getEndTime(TimeUnit.MILLISECONDS)));

        }


        send10DaysHuaweiSteps();
    }

    private void send10DaysHuaweiSteps() {
        newServiceFor10DaysHuawei task = new newServiceFor10DaysHuawei();
        task.execute();
    }

    private class newServiceFor10DaysHuawei extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            newServiceSendFor10DaysHuawei();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Date date = new Date();
            long currentTime = date.getTime();
            String str_currentTime = String.valueOf(currentTime);
            pref.setLastDataUpdateTimestamp(str_currentTime);
        }
    }

    private void newServiceSendFor10DaysHuawei() {
        GoogleClientForDiscover.newServiceAPIFor10DaysGFIT task = new GoogleClientForDiscover.newServiceAPIFor10DaysGFIT();
        task.execute(jsonStepsForWeekArrayForNewSave);

//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//
//
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//        ArrayList<String> ar = new ArrayList<String>();
//        JSONArray data = null;
//
//        //  data = collectGFITStepsDatass();
//        data = jsonHuaweiStepsForWeekArray;
//
//
//        //  String summeryData=data.toString().replace("\"","\\\"");
//        System.out.println("========alldata==============" + data);
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"alldata\": " + data + "," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"osType\": \"" + "android" + "\"," +
//                        "\"device_modal\": \"" + device_modal
//                        + "\"" +
//                        "}";
//
//        nameValuePair.add(new BasicNameValuePair("method", "addBulkDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//        HttpResponse response = null;
//        try {
//            response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    public static DataReadRequest queryFitnessDataLastWeek() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTimeInMillis();


        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(24, TimeUnit.HOURS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .enableServerQueries()
                .build();

        return readRequest;
    }

    public void printDataNewForLast(DataReadResult dataReadResult) {
        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    showDataSetForLastWeek(dataSet);
                }
            }
        }
//Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                showDataSetForLastWeek(dataSet);
            }
        }
    }

    public void showDataSetForLastWeek(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();


        String strSteps = null;
        String strDate = null;
        String status = null;
        for (DataPoint dp : dataSet.getDataPoints()) {

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            strDate = format1.format(dp.getStartTime(TimeUnit.MILLISECONDS));
            System.out.println("================Date============================" + strDate);

            String si[] = dp.getOriginalDataSource().getStreamIdentifier().toLowerCase().split(":");

            //     System.out.println("===******AYUBO*********=="+si[si.length - 1]);

            for (Field field : dp.getDataType().getFields()) {
                System.out.println("===dataSet====Not=Empty==========2===");
                stepStatus = "good";

                strSteps = dp.getValue(field).toString();
                status = dp.getOriginalDataSource().getStreamName();


                System.out.println("*** Steps : " + strSteps);
                System.out.println("*** Status : " + status);
                System.out.println("*** Step Start Time : " + dp.getStartTime(TimeUnit.MILLISECONDS));
                System.out.println("*** Step End Time : " + dp.getEndTime(TimeUnit.MILLISECONDS));


//                LongTimeToStringDate longTimeToStringDate = new LongTimeToStringDate();
//                String d = longTimeToStringDate.convertLongToTime(dp.getStartTime(TimeUnit.MILLISECONDS));
//                System.out.println(d);
//
//
//                String today = longTimeToStringDate.convertLongToTime(System.currentTimeMillis());
//
//                if (d.equals(today)) {
//                    userStepSyncObjectArray.add(new UserStepSyncObject(strSteps, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS)));
//
//                }


//                if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
//                    stepStatus = "bad";
//                    System.out.println("======BAD STEPS ===========");
//                }

                if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
                    stepStatus = "bad";
                    System.out.println("======BAD STEPS ===========");
                } else {
                    stepStatus = "good";
                    System.out.println("=====GFIT=======Good Steps================");
                }

//                if ((((si[si.length - 1].contains("soft")) || (si[si.length - 1].contains("step"))) && si[si.length - 1].contains("counter"))) {
//                    stepStatus = "good";
//                    System.out.println("=====GFIT=======Good Steps================");
//                } else {
//                    if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
//                        stepStatus = "bad";
//                        System.out.println("======BAD STEPS ===========");
//                    }
//                }
            }


//            jsonStepsForWeekArray.put(createGFitStepsDataArray(strSteps, strDate, stepStatus));
            jsonStepsForWeekArrayForNewSave.add(
                    createGFitStepsDataArrayForNewSave(
                            strSteps,
                            dp.getStartTime(TimeUnit.MILLISECONDS),
                            dp.getEndTime(TimeUnit.MILLISECONDS),
                            stepStatus,
                            dp.getEndTime(TimeUnit.MILLISECONDS)

                    ));
        }


    }

    public JSONObject createGFitStepsDataArray(String steps, String date, String isStepsGood) {
        float value = Float.parseFloat(steps);
        String sMets, sCals, sDis;
        double metsVal = (value / 130) * 4.5;
        sMets = String.format("%.0f", metsVal);
        double calories = (metsVal * 3.5 * 70) / 200;
        sCals = String.format("%.0f", calories);
        double distance = (value * 78) / (float) 100000;
        sDis = String.format("%.2f", distance);

        JSONObject list1 = new JSONObject();

        String run_count = "11";


        if (isStepsGood.equals("bad")) {
            run_count = "22";
        } else {
            run_count = "11";
        }


        try {
            list1.put("userid", userid_ExistingUser_static);
            list1.put("activity", "activity_AYUBO");
            list1.put("energy", sMets);
            list1.put("steps", steps);
            list1.put("calorie", sCals);
            list1.put("duration", "0");
            list1.put("distance", sDis);
            list1.put("date", date);
            list1.put("walk_count", steps);
            list1.put("run_count", run_count);
            list1.put("version", versionName);
            list1.put("osType", "android");
            list1.put("device_modal", device_modal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list1;
    }

    public StepObj createGFitStepsDataArrayForNewSave(String steps, Long startTime, Long endTime, String stepStatus, Long source_sync_datetime) {

        String energy, calories, distance;
        StepObj stepObj = new StepObj();
        stepObj.setStepCount(Integer.parseInt(steps));

        Long sl = getStartDateString(startTime);
        Long el = getEndDateString(endTime);

        stepObj.setStartDateTime(sl);
        stepObj.setEndDateTime(el);

        String versionName = BuildConfig.VERSION_NAME;
        int value = stepObj.getStepCount();
        double energyVal = (value / 130) * 4.5;
        energy = String.format("%.0f", energyVal);
        double caloriesVal = (energyVal * 3.5 * 70) / 200;
        calories = String.format("%.0f", caloriesVal);
        double distanceVal = (value * 78) / (float) 100000;
        distance = String.format("%.2f", distanceVal);


        stepObj.setActivity("activity_AYUBO");
        stepObj.setEnergy(energy);
        stepObj.setCalorie(calories);
        stepObj.setDuration("0");
        stepObj.setDistance(distance);
        stepObj.setWalk_count(stepObj.getStepCount().toString());
        stepObj.setRun_count(stepStatus);
        stepObj.setVersion(versionName);
        stepObj.setOsType("android");
        stepObj.setDevice_modal(device_modal);
        stepObj.setSource_sync_datetime(source_sync_datetime);

        return stepObj;
    }


    public Long getStartDateString(long startDate) {
        Date subscribedTimeNew = new Date(startDate);
        subscribedTimeNew.setHours(0);
        subscribedTimeNew.setMinutes(0);
        subscribedTimeNew.setSeconds(0);


        Calendar calendar = Calendar.getInstance();

        calendar.setTime(subscribedTimeNew);


        Date resultdate = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

        Date date = new Date(sdf.format(resultdate));

        return date.getTime();
//        return calendar.getTimeInMillis();
    }

    public Long getEndDateString(long endDate) {
        Date subscribedTimeNew = new Date(endDate);
        subscribedTimeNew.setHours(23);
        subscribedTimeNew.setMinutes(59);
        subscribedTimeNew.setSeconds(59);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(subscribedTimeNew);


        Date resultdate = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

        Date date = new Date(sdf.format(resultdate));

        return date.getTime();


//        return calendar.getTimeInMillis();
    }


    //==================GFIT===========================
    private void send10Days_GFIT() {
        GoogleClientForDiscover.newService_For10DaysGFIT task = new GoogleClientForDiscover.newService_For10DaysGFIT();
        task.execute(new String[]{ApiClient.BASE_URL_live});
    }

    private class newService_For10DaysGFIT extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            newService_sendFor10DaysGFIT();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Date date = new Date();
            long currentTime = date.getTime();
            String str_currentTime = String.valueOf(currentTime);
            pref.setLastDataUpdateTimestamp(str_currentTime);
        }
    }

    private void newService_sendFor10DaysGFIT() {
        GoogleClientForDiscover.newServiceAPIFor10DaysGFIT task = new GoogleClientForDiscover.newServiceAPIFor10DaysGFIT();
        task.execute(jsonStepsForWeekArrayForNewSave);

//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//
//
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//        ArrayList<String> ar = new ArrayList<String>();
//        JSONArray data = null;
//
//        //  data = collectGFITStepsDatass();
//        data = jsonStepsForWeekArray;
//
//        //  String summeryData=data.toString().replace("\"","\\\"");
//        System.out.println("========alldata==============" + data);
//
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"alldata\": " + data + "," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"osType\": \"" + "android" + "\"," +
//                        "\"device_modal\": \"" + device_modal
//                        + "\"" +
//                        "}";
//
//        nameValuePair.add(new BasicNameValuePair("method", "addBulkDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//        System.out.println("======data=======" + nameValuePair.toString());
//        stepsSentToDBStatus = "11";
//
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//        HttpResponse response = null;
//        try {
//            response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private class newServiceAPIFor10DaysGFIT extends AsyncTask<List<StepObj>, Void, String> {

        @Override
        protected String doInBackground(List<StepObj>... arrayLists) {

            List<StepObj> stepDetail = arrayLists[0];
            saveWalkWinDailySteps(stepDetail);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Date date = new Date();
            long currentTime = date.getTime();
            String str_currentTime = String.valueOf(currentTime);
            pref.setLastDataUpdateTimestamp(str_currentTime);
        }
    }

    private void saveWalkWinDailySteps(List<StepObj> obj) {


        if (obj.size() == 0) {
//            progressDialog.dismiss();
        } else {
            WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);

            TimeZone timeZone = TimeZone.getDefault();
            String timeZoneId = timeZone.getID();

            System.out.println("saveDailySteps");
//            System.out.println(userStepSyncObjectArray);


//            LongTimeToStringDate longTimeToStringDate = new LongTimeToStringDate();
//            String lastTimeStamp = longTimeToStringDate.convertLongToTime(userStepSyncObjectArray.get(0).endTime);
//            System.out.println(lastTimeStamp);

            Call<WalkWinStepsResponse> walkWinSaveStepsRequestCall = walkWinApiInterface.saveDailySteps(AppConfig.APP_BRANDING_ID, pref.getUserToken(), obj, timeZoneId);


            if (Constants.type == Constants.Type.HEMAS) {
                walkWinSaveStepsRequestCall = walkWinApiInterface.saveDailyStepsForHemas(AppConfig.APP_BRANDING_ID, pref.getUserToken(), obj);
            }


            walkWinSaveStepsRequestCall.enqueue(new retrofit2.Callback<WalkWinStepsResponse>() {

                @Override
                public void onResponse(Call<WalkWinStepsResponse> call, Response<WalkWinStepsResponse> response) {
                    if (response.isSuccessful()) {
                        // successfully save
                        System.out.println(response);
                        pref.saveStepSyncTime(System.currentTimeMillis());

                    } else {
                        // no result
                        System.out.println(response);
                    }

//                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<WalkWinStepsResponse> call, Throwable t) {
//                showToast("Save daily step API failure...");
//                    progressDialog.dismiss();
                }
            });
        }


    }

}
