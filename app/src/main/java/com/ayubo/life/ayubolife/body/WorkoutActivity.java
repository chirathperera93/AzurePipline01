package com.ayubo.life.ayubolife.body;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.ayubolifestepcounter.models.StepsDetails;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.footer_menu.WorkoutAdapter;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.model.todaysummery;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Util;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class WorkoutActivity extends BaseActivity {
    ProgressDialog proDialog;
    private static final int GET_FINE_LOCATION_REQUEST_CODE = 505;
    String deviceIcon;
    File newf;
    boolean isNeedCallService = false;
    private PrefManager pref;
    private int FIRST_RAW = 0;
    private int OTHER_RAWS = 1;
    //  ArrayList<OldPrograms> datesListArray2 = null;
    String communityID;
    String image_absolute_path, shareMessage, postType, message;
    long totalSize = 0;
    RecyclerView rv;
    // CustomListAdapterLiteNew adapter;
    String stepsFromDB_forADay0, dateFromDB_forADay0;
    boolean isStepsUpdated = false;
    String userid_ExistingUser, setDeviceID_Success, today, serviceDataStatus;
    public static String userid_ExistingUser_static, versionName, device_modal;
    String total_steps;
    String total_calories;
    String total_distance;
    String total_energy;
    String total_duration;
    String deviceName;
    public static boolean isGoogleFitActivityDetectionON;
    public static String stepStatus;
    public static String stepCount;
    String stepsSentToDBStatus, userStatus;
    // ProgressBar progressBar_Step_Circle;

    ImageButton btn_backImgBtn;
    public static GoogleApiClient mClient = null;
    private boolean isGoogleFitConnected = false;
    SimpleDateFormat sdf;
    String googlefitenabled = null;
    String activityName;
    Button txt_coonectgfit;
    Button workout_one;
    private static final String TAG = WorkoutActivity.class.getSimpleName();
    boolean aaa = true;
    ProgressDialog prgDialog;
    ImageLoader imageLoader;
    ListViewItem[] items;
    private ArrayList<WorkoutProgram> workoutPageDataList;
    CustomAdapterFresh customAdapter;
    WorkoutAdapter adapte;
    String main_service_data, gfit_steps_0, gfit_steps_1, gfit_steps_2, gfit_steps_3, gfit_steps_4, gfit_steps_5, gfit_steps_6, gfit_steps_7, gfit_steps_8, gfit_steps_9;

    String gfit_steps_isgood_0, gfit_steps_isgood_1, gfit_steps_isgood_2, gfit_steps_isgood_3, gfit_steps_isgood_4, gfit_steps_isgood_5, gfit_steps_isgood_6, gfit_steps_isgood_7, gfit_steps_isgood_8, gfit_steps_isgood_9;

    DatabaseHandler db;
    public static JSONArray jsonStepsForWeekArray;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    Integer SR;
    boolean isNeedProgressLoading = false;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onNewStepsUpdateRecived(StepsDetails stepsDetails) {
        if (customAdapter != null) {
            customAdapter.setRealTimeProg(stepsDetails.getLastTotatStepCount() + stepsDetails.getCurrentRealStepsCount());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case GET_FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    click_Workout();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == GET_FINE_LOCATION_REQUEST_CODE) {
                    click_Workout();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermissionLocationService() {
        if (ContextCompat
                .checkSelfPermission(WorkoutActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (WorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(WorkoutActivity.this.findViewById(android.R.id.content),
                        "This app needs location permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            GET_FINE_LOCATION_REQUEST_CODE);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            GET_FINE_LOCATION_REQUEST_CODE);
            }
        } else {
            // write your logic code if permission already granted
            click_Workout();


        }
    }

    public void click_Workout() {

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }

        if (activityName.equals("RUNNING")) {
            String activityId = "1d29596b-9771-3c41-724e-5937c8878a2b";
            activityName = "RUNNING";
            String mets = "7";
            Intent intent = new Intent(WorkoutActivity.this, StartWorkoutActivity.class);
            intent.putExtra("activityName", activityName);
            intent.putExtra("activityId", activityId);
            intent.putExtra("mets", mets);

            Ram.setActivityName(activityName);
            Ram.setActivityId(activityId);
            Ram.setMets(mets);
            startActivity(intent);
        } else if (activityName.equals("BADMINTON")) {
            String activityId = "b5504c46-4109-9724-d368-5948ad558767";
            activityName = "BADMINTON";
            String mets = "4";
            Intent intent = new Intent(WorkoutActivity.this, StartWorkoutActivity.class);
            intent.putExtra("activityName", activityName);
            intent.putExtra("activityId", activityId);
            intent.putExtra("mets", mets);
            Ram.setActivityName(activityName);
            Ram.setActivityId(activityId);
            Ram.setMets(mets);
            startActivity(intent);
        } else if (activityName.equals("CYCLING")) {
            String activityId = "8ceb0f82-9b73-a12a-225f-59478c7063c1";
            activityName = "CYCLING";
            String mets = "8";

            Intent intent = new Intent(WorkoutActivity.this, StartWorkoutActivity.class);
            intent.putExtra("activityName", activityName);
            intent.putExtra("activityId", activityId);
            intent.putExtra("mets", mets);

            Ram.setActivityName(activityName);
            Ram.setActivityId(activityId);
            Ram.setMets(mets);

            startActivity(intent);

        } else {
            activityName = "OTHER";
            Intent intent = new Intent(WorkoutActivity.this, DisplayWorkoutList.class);
            startActivity(intent);
        }
    }

    public void click_Workout_One(View v) {
        activityName = "RUNNING";
        checkPermissionLocationService();
    }

    public void click_Workout_Two(View v) {
        activityName = "BADMINTON";
        checkPermissionLocationService();
    }

    public void click_Workout_Three(View v) {
        activityName = "CYCLING";
        checkPermissionLocationService();
    }

    public void click_Workout_Four(View v) {
        activityName = "OTHER";
        checkPermissionLocationService();

    }


    public class ListViewItem {
        private String text;
        private int type;

        public ListViewItem(String text, int type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }


    //==============================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_workout2);


        proDialog = new ProgressDialog(WorkoutActivity.this);
        proDialog.setCancelable(false);
        proDialog.setMessage("Please wait...");

        pref = new PrefManager(this);

        jsonStepsForWeekArray = new JSONArray();
        userid_ExistingUser = pref.getLoginUser().get("uid");
        userid_ExistingUser_static = pref.getLoginUser().get("uid");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        prgDialog = new ProgressDialog(WorkoutActivity.this);
        prgDialog.setCancelable(false);
        prgDialog.setMessage("Fetching steps. Please wait..");

        versionName = BuildConfig.VERSION_NAME;
        device_modal = Build.MODEL;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        today = dateFormat.format(date);

        Service_Service_getStepsData_ServiceCall();

        System.out.println(dateFormat.format(date));
        setDeviceID_Success = "99";

        rv = (RecyclerView) findViewById(R.id.health_track_list);

        googlefitenabled = pref.isGoogleFitEnabled();
        ArrayList<SImpleListString> data = new ArrayList<SImpleListString>();

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageButton btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        total_steps = pref.getUserData().get("steps");
        total_energy = pref.getUserData().get("energy");
        total_calories = pref.getUserData().get("calories");
        total_distance = pref.getUserData().get("distance");
        total_duration = pref.getUserData().get("duration");

        deviceName = pref.getDeviceData().get("stepdevice");
        deviceIcon = pref.getDeviceData().get("icon");


        inflatert = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(WorkoutActivity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String lastUpdatedDateNow = sdf.format(cal.getTime());

        String lastUpdatedDate = pref.getStepsUpdatedDate();
//


//        if(lastUpdatedDate.equals(lastUpdatedDateNow)){
//            if (deviceName.equals("activity_AYUBO")) {
//                stepStatus="good";
//                check_buildFitnessClient();
//            }
//        }else{
//            //Update steps ....
//            if (deviceName.equals("activity_AYUBO")) {
//                stepStatus = "good";
//                isNeedProgressLoading=false;
//                sendSteps();
//                pref.setStepsUpdatedDate(lastUpdatedDateNow);
//            }
//        }


        String dat = null;
        isNeedCallService = true;
        DBString datObj = DBRequest.getCashDataByType(WorkoutActivity.this, "workoutpage_data_v6");
        if (datObj == null) {
            isNeedCallService = true;
        } else {
            dat = datObj.getId();
            if (dat != null) {

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WorkoutProgram>>() {
                }.getType();
                workoutPageDataList = gson.fromJson(datObj.getId(), type);

                loadView();

            }
            long minits = Util.getTimestampAsMinits(datObj.getName());
        }

        if (pref.getRefreshDataType().equals("workout")) {
            if (pref.getFromSendToBaseView().equals("yes")) {
                //  isNeedCallService=true;
                pref.setFromSendToBaseView("no");
                pref.setRefreshDataType("no");
            }
        }

//        if (isNeedCallService) {
//            if (Utility.isInternetAvailable(this)) {
//                if ((dat != null) && (dat.length() > 10)) {
//                } else {
//                    MyProgressLoading.showLoading(WorkoutActivity.this, "Please wait...");
//                }
//
//                   getWorkoutDataList();
//
////                Service_checkAppVersion task = new Service_checkAppVersion();
////                task.execute(new String[]{ApiClient.BASE_URL_live});
//            } else {
//                textt.setText("Unable to detect an active internet connection");
//                toastt.setView(layoutt);
//                toastt.show();
//            }
//        }

        getWorkoutDataList();

    }


    public class CustomAdapterFresh extends ArrayAdapter<ListViewItem> {

        public static final int TYPE_WORKOUT_SUMMERY = 0;
        public static final int TYPE_WORKOUT_ITEMS = 1;
        public static final int TYPE_WHITE = 2;
        public static final int TYPE_BLACK = 3;

        private ListViewItem[] objects;
        private int realTimeProg;

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            try {
                return objects[position].getType();
            } catch (Exception e) {
                System.out.println("==========getItemViewType==================" + position);
                e.printStackTrace();
                return 80;
            }
        }

        public CustomAdapterFresh(Context context, int resource, ListViewItem[] objects) {
            super(context, resource, objects);
            this.objects = objects;
            isRealTimeUpdate = false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ListViewItem listViewItem = objects[position];
            int listViewItemType = getItemViewType(position);
            View viewHolder = convertView;

            if (viewHolder == null) {

                if (listViewItemType == TYPE_WORKOUT_SUMMERY) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.workout_summery_list_raw_layout, null);
                } else if (listViewItemType == TYPE_WORKOUT_ITEMS) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.workout_intro_raw_layout, null);
                } else if (listViewItemType == TYPE_WHITE) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.workout_image_list_raw_layout, null);
                } else {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.type_black, null);
                }


            } else {
                // viewHolder = (ViewHolder) convertView.getTag();
            }
            if (listViewItemType == 0) {

                Button img_coonectgfit = (Button) viewHolder.findViewById(R.id.img_coonectgfit);
                ImageView img_coonectgfit_icon = (ImageView) viewHolder.findViewById(R.id.img_coonectgfit_icon);


                LinearLayout layout_sysnc_button = (LinearLayout) viewHolder.findViewById(R.id.layout_sysnc_button);

                TextView txt_total_steps = (TextView) viewHolder.findViewById(R.id.txt_total_steps);
                TextView txt_mets = (TextView) viewHolder.findViewById(R.id.txt_mets);
                TextView txt_cals = (TextView) viewHolder.findViewById(R.id.txt_cals);
                TextView txt_dus = (TextView) viewHolder.findViewById(R.id.txt_dus);
                TextView txt_dis = (TextView) viewHolder.findViewById(R.id.txt_dis);
                ProgressBar progressBar_Step_Circle = (ProgressBar) viewHolder.findViewById(R.id.progressBar_Step_Circle);

                progressBar_Step_Circle.setProgress(0);
                progressBar_Step_Circle.setMax(10000);


                total_steps = pref.getUserData().get("steps");

                int steps = Integer.parseInt(total_steps);
                String steps_with_comma = NumberFormat.getIntegerInstance().format(steps);
                txt_total_steps.setText(steps_with_comma);

                double dSteps = (double) Integer.parseInt(total_steps);
                double d78 = (double) 78;
                double dLax = (double) 100000;
                double dDist = (dSteps * d78) / dLax;

                DecimalFormat twoDForm = new DecimalFormat("#.##");
                String numberAsString = twoDForm.format(dDist);

                txt_mets.setText(total_energy);
                txt_cals.setText(total_calories);
                txt_dus.setText(total_duration);
                txt_dis.setText(numberAsString);

                progressBar_Step_Circle.setProgress(Integer.parseInt(total_steps));

                layout_sysnc_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isNeedProgressLoading = true;
                        sendSteps();
                    }
                });

                img_coonectgfit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isNeedProgressLoading = true;
                        sendSteps();
                    }
                });

                if (deviceName != null) {
                    System.out.println("======deviceName====4444=====" + deviceName);
                    if (deviceName.equals("activity_AYUBO")) {

                        Bitmap bitmap55 = BitmapFactory.decodeResource(WorkoutActivity.this.getResources(), R.drawable.gfit_ic);
                        img_coonectgfit_icon.setImageBitmap(bitmap55);

                        if (googlefitenabled.equals("false")) {
                            img_coonectgfit.setText("Connect");

                        } else if (googlefitenabled.equals("true")) {
                            img_coonectgfit.setText("Sync now");
                        }

                    } else {
                        // System.out.println("======deviceIcon========="+deviceIcon);
                        img_coonectgfit.setText(deviceName);

                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(getContext()));
                        Glide.with(WorkoutActivity.this).load(deviceIcon)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(img_coonectgfit_icon);

                    }

                }


            } else if (listViewItemType == 1) {
                //  TextView txt_nme_tv = (TextView) viewHolder.findViewById(R.id.txt_nme_tv);
                //   txt_nme_tv.setText("");
            } else if (listViewItemType == 2) {
                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();
                String imageLink = null;
                String rawName = listViewItem.getText();

                for (WorkoutProgram room : workoutPageDataList) {
                    // access by room. , here room is you item
                    if (room.getName().equals(rawName)) {
                        imageLink = room.getName();
                    }
                }


                // ProgressBar progressNewsList = (ProgressBar) viewHolder.findViewById(R.id.progressNewsList);
                ImageView bg_image = (ImageView) viewHolder.findViewById(R.id.main_bg_banner);
                //  ImageView img_child_image = (ImageView) viewHolder.findViewById(R.id.img_child_image);
                String imgUrl = ApiClient.BASE_URL + imageLink;
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter();
                Glide.with(getContext()).load(imgUrl)
                        .apply(requestOptions)
                        .into(bg_image);


            } else {
                //   TextView textView = (TextView) viewHolder.findViewById(R.id.text);
                //  textView.setText(listViewItem.getText());
            }


            return viewHolder;
        }

        private boolean isRealTimeUpdate;

        public void setRealTimeProg(int realTimeProg) {
            isRealTimeUpdate = true;
            this.realTimeProg = realTimeProg;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder {
        TextView text;

        public ViewHolder(TextView text) {
            this.text = text;
        }

        public TextView getText() {
            return text;
        }

        public void setText(TextView text) {
            this.text = text;
        }

    }

    private String getStepsDataFromDatabase(String date) {
        db = new DatabaseHandler(this);
        todaysummery data = null;
        int step_from_db = 0;
        try {
            data = db.getSelectedDaySummeryFromDB(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (data == null) {
                step_from_db = 0;
            } else {
                step_from_db = data.getStep_tot();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        System.out.println("==========date=========" + date);
        System.out.println("==========steps=========" + step_from_db);
        String steps = Integer.toString(step_from_db);

        return steps;
    }

    private String getDateForHistory(int days) {
        String day = null;
        Calendar calObj = Calendar.getInstance();
        calObj.setTime(new Date());
        calObj.add(Calendar.DATE, -days);
        day = sdf.format(calObj.getTime());
        // System.out.println("=========day=========="+day);
        return day;
    }

    private JSONArray collectNativeStepsFor10Days() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(0)), getDateForHistory(0)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(1)), getDateForHistory(1)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(2)), getDateForHistory(2)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(3)), getDateForHistory(3)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(4)), getDateForHistory(4)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(5)), getDateForHistory(5)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(6)), getDateForHistory(6)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(7)), getDateForHistory(7)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(8)), getDateForHistory(8)));
        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(9)), getDateForHistory(9)));
        return jsonArray;
    }


    private static String dumpDataSetNew(DataSet dataSet) {


        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();
        stepStatus = "good";
        if (dataSet.isEmpty()) {
            System.out.println("========Empty======GFit Steps============");
            stepCount = "0";
        } else {
            System.out.println("===dataSet====Not=Empty=============");
            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                System.out.println("===dataSet====Not=Empty==========1===");
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

                String si[] = dp.getOriginalDataSource().getStreamIdentifier().toLowerCase().split(":");


                for (Field field : dp.getDataType().getFields()) {
                    System.out.println("===dataSet====Not=Empty==========2===");

                    stepCount = dp.getValue(field).toString();
                    Log.i(TAG, "\tField: " + field.getName() +
                            "GFit Steps Value: " + dp.getValue(field));
                    stepCount = dp.getValue(field).toString();

                    if ((((si[si.length - 1].contains("soft")) || (si[si.length - 1].contains("step"))) && si[si.length - 1].contains("counter"))) {
                        stepStatus = "good";
                        System.out.println("=====GFIT=======Good Steps================");
                        System.out.println("===dataSet====Not=Empty==========3===");
                    } else {

                        if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
                            stepStatus = "bad";
                            System.out.println("======BAD STEPS ===========");
                            System.out.println("===dataSet====Not=Empty=========4===");
                        }
                    }
                }
            }
        }

        return stepCount;
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
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    JSONObject createStepsDataArray(String steps, String date) {

        float value = Float.parseFloat(steps);
        String sMets, sCals, sDis;
        double metsVal = (value / 130) * 4.5;
        sMets = String.format("%.0f", metsVal);
        double calories = (metsVal * 3.5 * 70) / 200;
        sCals = String.format("%.0f", calories);
        double distance = (value * 78) / (float) 100000;
        sDis = String.format("%.2f", distance);

        JSONObject list1 = new JSONObject();
        try {
            list1.put("userid", userid_ExistingUser);
            list1.put("activity", "activity_AYUBO");
            list1.put("energy", sMets);
            list1.put("steps", steps);
            list1.put("calorie", sCals);
            list1.put("duration", "0");
            list1.put("distance", sDis);
            list1.put("date", date);
            list1.put("walk_count", steps);
            list1.put("run_count", "2");
            list1.put("version", versionName);
            list1.put("osType", "android");
            list1.put("device_modal", device_modal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list1;
    }

    public static JSONObject createGFitStepsDataArray(String steps, String date, String isStepsGood) {

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


        if (isStepsGood == null) {
            run_count = "11";
        } else {
            if (isStepsGood.equals("bad")) {
                run_count = "22";
            } else {
                run_count = "11";
            }
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

    private static String dumpDataSetOld(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

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

    public static String printDataNew(DataReadResult dataReadResult) {
        String step = "0";
        System.out.println("===In printDataNew================================");
        if (dataReadResult.getBuckets().size() > 0) {
            System.out.println("=0==buckets not empty=============================");
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    step = dumpDataSetNew(dataSet);
                    System.out.println("=0==buckets not empty==========step===================" + step);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            System.out.println("===buckets not empty=============================");
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                step = dumpDataSetNew(dataSet);
                System.out.println("===buckets not empty==========step===================" + step);
            }

        }
        return step;
        // [END parse_read_data_result]
    }

    private class GetSteps1 extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(1);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_1 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {

            gfit_steps_1 = steps;
            Log.i("gfit_steps_1", steps);
            GetSteps2 task = new GetSteps2();
            task.execute();
        }

    }

    private class GetSteps2 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(2);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_2 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_2 = steps;
            Log.i("gfit_steps_2", steps);
            GetSteps3 task = new GetSteps3();
            task.execute();
        }

    }

    private class GetSteps3 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(3);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_3 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_3 = steps;
            Log.i("gfit_steps_3", steps);
            GetSteps4 task = new GetSteps4();
            task.execute();
        }

    }

    private class GetSteps4 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(4);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_4 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_4 = steps;
            Log.i("gfit_steps_4", steps);
            GetSteps5 task = new GetSteps5();
            task.execute();
        }

    }

    private class GetSteps5 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(5);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_5 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_5 = steps;
            Log.i("gfit_steps_5", steps);
            GetSteps6 task = new GetSteps6();
            task.execute();
        }

    }

    private class GetSteps6 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(6);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_6 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_6 = steps;
            Log.i("gfit_steps_6", steps);
            GetSteps7 task = new GetSteps7();
            task.execute();
        }

    }

    private class GetSteps7 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(7);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);


            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one

            gfit_steps_isgood_7 = stepStatus;

            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_7 = steps;
            Log.i("gfit_steps_7", steps);
            GetSteps8 task = new GetSteps8();
            task.execute();
        }

    }

    private class GetSteps8 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(8);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one


            gfit_steps_isgood_8 = stepStatus;
            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_8 = steps;
            Log.i("gfit_steps_8", steps);
            GetSteps9 task = new GetSteps9();
            task.execute();
        }

    }

    private class GetSteps9 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";
            DataReadRequest readRequest = queryFitnessData(9);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

            //This is old worked one
            //ste = printDataOld(dataReadResult);
            //This is old worked one

            //This is new Testing one
            ste = printDataNew(dataReadResult);
            //This is new Testing one


            gfit_steps_isgood_9 = stepStatus;
            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {
            gfit_steps_9 = steps;
            Log.i("gfit_steps_9", steps);

            send10Days_GFIT();
        }

    }

    //==================GFIT====Sending Start=======================
    private void send10Days_GFIT() {

        newService_For10DaysGFIT task = new newService_For10DaysGFIT();
        task.execute(new String[]{ApiClient.BASE_URL_live});
    }

    private class newService_For10DaysGFIT extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isFinishing()) {
                prgDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            newService_sendFor10DaysGFIT();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (!isFinishing()) {
                if (prgDialog != null) {
                    prgDialog.cancel();
                }
            }

            Service_Service_getStepsData_ServiceCall();

            Date date = new Date();
            long currentTime = date.getTime();
            String str_currentTime = String.valueOf(currentTime);
            pref.setLastDataUpdateTimestamp(str_currentTime);
        }
    }

    private void newService_sendFor10DaysGFIT() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        ArrayList<String> ar = new ArrayList<String>();
        JSONArray data = null;

        // data = collectGFITStepsDatass();

        System.out.println("=====Bulk data sending to Server =======");
        data = jsonStepsForWeekArray;

        //  String summeryData=data.toString().replace("\"","\\\"");

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"alldata\": " + data + "," +
                        "\"version\": \"" + versionName + "\"," +
                        "\"osType\": \"" + "android" + "\"," +
                        "\"device_modal\": \"" + device_modal
                        + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "addBulkDailyActivitySummary"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
        System.out.println("=====Bulk data sending to Server =======" + nameValuePair.toString());
        stepsSentToDBStatus = "11";

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            System.out.println("========Steps Sented Fidish=====response===============" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //==================GFIT====Sending Finished=======================

    private void newService_sendFor10DaysNative() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        ArrayList<String> ar = new ArrayList<String>();
        JSONArray data = null;

        data = collectNativeStepsFor10Days();

        // String summeryData=data.toString().replace("\"","\\\"");

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"alldata\": " + data + "," +
                        "\"version\": \"" + versionName + "\"," +
                        "\"osType\": \"" + "android" + "\"," +
                        "\"device_modal\": \"" + device_modal
                        + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "addBulkDailyActivitySummary"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
        System.out.println("======data=======" + nameValuePair.toString());
        stepsSentToDBStatus = "11";

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void sendSteps() {
        isNeedProgressLoading = true;
        if (deviceName.equals("activity_AYUBO")) {

            new_check_buildFitnessClient();

        } else {

            Service_Service_getStepsData_ServiceCall();
        }


    }

    private class Service_checkAppVersion extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            userStatus = "999";
        }

        @Override
        protected String doInBackground(String... urls) {
            checkAppVersion();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (proDialog != null) {
                proDialog.dismiss();
            }

            if (main_service_data.length() > 10) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showLoadedView(main_service_data);
                    }
                });


            }


        }
    }


    void loadView() {

        if (adapte == null) {
            adapte = new WorkoutAdapter(WorkoutActivity.this, workoutPageDataList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkoutActivity.this);
            rv.setLayoutManager(linearLayoutManager);
            rv.setAdapter(adapte);

            adapte.setOnVideoSessionListener(new WorkoutAdapter.OnSendStepsListener() {
                @Override
                public void onClicksendSteps() {
                    sendSteps();
                }
            });
            adapte.setOnImageClickListener(new WorkoutAdapter.OnImageClickListener() {
                @Override
                public void onClickProcessAction(String action, String meta) {
                    processAction(action, meta);
                }
            });
        } else {
            adapte.notifyDataSetChanged();
        }
    }


    void showLoadedView(String dataString) {

        if (dataString.length() > 10) {

            workoutPageDataList = new ArrayList<WorkoutProgram>();
            JSONObject jsonObj2 = null;
            try {
                jsonObj2 = new JSONObject(dataString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String notification_count = jsonObj2.optString("programs").toString();
            JSONArray myServiceInfoLists = null;
            try {
                myServiceInfoLists = new JSONArray(notification_count);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            workoutPageDataList.add(new WorkoutProgram("firstraw", "firstraw", "firstraw", "", "", "", "", "", ""));
            workoutPageDataList.add(new WorkoutProgram("sec", "sec", "sec", "", "", "", "", "", ""));
            int c = 2;
            for (int i = 0; i < myServiceInfoLists.length(); i++) {
                JSONObject jsonMainNode3 = null;
                try {
                    jsonMainNode3 = (JSONObject) myServiceInfoLists.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String name = jsonMainNode3.optString("name");
                String image = jsonMainNode3.optString("image");
                String link = jsonMainNode3.optString("link");

                String action = jsonMainNode3.optString("action");
                String meta = jsonMainNode3.optString("meta");

                workoutPageDataList.add(new WorkoutProgram(name, image, link, "", "", "", "", action, meta));
            }

            if (adapte == null) {
                adapte = new WorkoutAdapter(WorkoutActivity.this, workoutPageDataList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkoutActivity.this);

                rv.setLayoutManager(linearLayoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(adapte);

                adapte.setOnVideoSessionListener(new WorkoutAdapter.OnSendStepsListener() {
                    @Override
                    public void onClicksendSteps() {
                        sendSteps();
                    }
                });
                adapte.setOnImageClickListener(new WorkoutAdapter.OnImageClickListener() {
                    @Override
                    public void onClickProcessAction(String action, String meta) {

                        processAction(action, meta);
                    }
                });
            } else {

                adapte.notifyDataSetChanged();

            }


        }

    }


    public void getWorkoutDataList() {

        if (isNeedCallService) {
            if (proDialog != null) {
                proDialog.show();
            }
        }
        //

        workoutPageDataList = new ArrayList<WorkoutProgram>();
        workoutPageDataList.add(new WorkoutProgram("firstraw", "firstraw", "firstraw", "", "", "", "", "", ""));
        workoutPageDataList.add(new WorkoutProgram("sec", "sec", "sec", "", "", "", "", "", ""));

        pref = new PrefManager(this);

        String serviceName = "getCategoryBodyList";

        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<WorkoutMainResponse> call = apiService.getWorkoutDataList(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<WorkoutMainResponse>() {
            @Override
            public void onResponse(@NonNull Call<WorkoutMainResponse> call, @NonNull Response<WorkoutMainResponse> response) {
                if (proDialog != null) {
                    proDialog.dismiss();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 0) {
                            WorkoutMain dataList = response.body().getData();

                            //   ArrayList<WorkoutProgram> workoutPageDatat;
                            // workoutPageDatat=new ArrayList<WorkoutProgram>();
                            List<WorkoutProgram> workoutPageDatat = dataList.getPrograms();

                            workoutPageDataList.addAll(workoutPageDatat);

                            Gson gson1 = new Gson();
                            String jsonCars = gson1.toJson(workoutPageDataList);
                            DBRequest.updateDoctorData(WorkoutActivity.this, jsonCars, "workoutpage_data_v6");


                            loadView();
                        }

                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<WorkoutMainResponse> call, @NonNull Throwable t) {
                if (proDialog != null) {
                    proDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }

    private void checkAppVersion() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);

        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStree =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getCategoryBodyList"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStree));
        System.out.println(".............get Category Body List................" + jsonStree);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            userStatus = "17";
            e.printStackTrace();
        }
        //making POST request.
        try {
            int r = 0;
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);

            } catch (IOException e) {
                userStatus = "17";
                e.printStackTrace();
            }

            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {

                    String responseString = null;
                    try {
                        responseString = EntityUtils.toString(response.getEntity());
                        System.out.println("......Workout..response..........." + responseString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(responseString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String res = jsonObj.optString("result").toString();

                    if (res.equals("0")) {
                        userStatus = "0";
                        try {
                            main_service_data = jsonObj.optString("data");

                            DBRequest.updateDoctorData(WorkoutActivity.this, main_service_data, "workoutpage_data_v6");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        userStatus = "17";
                        //  errorData = jsonObj.optString("error").toString();
                    }


                }

            }

        } catch (Exception e) {
            userStatus = "999";

            e.printStackTrace();
        }

    }

    private static final String PACKAGE_NAME = "com.google.android.apps.fitness";

    @CheckResult
    public boolean fitInstalled() {
        try {
            WorkoutActivity.this.getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        if (dataSet.isEmpty()) {
            System.out.println("========Empty======GFit Steps============");
            stepCount = "0";
            System.out.println("====isGoogleFitActivityDetectionON===========False");
            isGoogleFitActivityDetectionON = false;
        } else {
            System.out.println("====isGoogleFitActivityDetectionON===========On");
            isGoogleFitActivityDetectionON = true;

            //  Ram.setIsGoogleFitActivityDetectionON(true);


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
                    System.out.println("==============GFit Steps Value:==================" + stepCount);
                    //sfsdf setGoogleFitEnabled

                    //  Ram.setGoogleFitSteps(stepCount);


                }
            }
        }
    }

    public static DataReadRequest queryFitnessData_0() {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        // today
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        long midNight = date.getTimeInMillis();


        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(midNight));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                // .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                // .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                // .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    public static DataReadRequest queryFitnessDataLastWeek() {


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -2);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTimeInMillis();


        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }


    private class GFitGetSteps_LastWeek extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String ste = "0";

            DataReadRequest readRequest = queryFitnessDataLastWeek();
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

            //This is new Testing one
            printDataNewForLast(dataReadResult);
            return ste;
        }

        @Override
        protected void onPostExecute(String steps) {

            send10Days_GFIT();
            Log.i("gfit_steps_0", steps);
            // GetSteps1 task = new GetSteps1();
            // task.execute();
        }

    }

    public static void showDataSetForLastWeek(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();


        String strSteps = null;
        String strDate = null;
        for (DataPoint dp : dataSet.getDataPoints()) {
            System.out.println("===========GOOGLE FIT STEPS testing is BAD ====================");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            strDate = format1.format(dp.getStartTime(TimeUnit.MILLISECONDS));
            System.out.println("================Date============================" + strDate);

            String si[] = dp.getOriginalDataSource().getStreamIdentifier().toLowerCase().split(":");
            stepStatus = "good";
            for (Field field : dp.getDataType().getFields()) {
                System.out.println("===dataSet====Not=Empty==========2===");

                strSteps = dp.getValue(field).toString();

                if ((((si[si.length - 1].contains("soft")) || (si[si.length - 1].contains("step"))) && si[si.length - 1].contains("counter"))) {
                    stepStatus = "good";
                    System.out.println("=====GFIT=======Good Steps================");
                    System.out.println("===dataSet====Not=Empty==========3===");
                } else {

                    if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
                        stepStatus = "bad";
                        System.out.println("======BAD STEPS ===========");
                        System.out.println("===dataSet====Not=Empty=========4===");
                    }
                }
            }


            jsonStepsForWeekArray.put(createGFitStepsDataArray(strSteps, strDate, stepStatus));
        }


    }

    public static void printDataNewForLast(DataReadResult dataReadResult) {
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

    private void send10Days_GFITSteps() {

        GFitGetSteps_LastWeek task = new GFitGetSteps_LastWeek();
        task.execute();
    }


    private void new_check_buildFitnessClient() {
        // Create the Google API Client

        if (mClient != null) {
            System.out.println("==========mClient not null=====================");
            if (mClient.isConnected()) {
                isGoogleFitConnected = true;
                try {

                    boolean bb = fitInstalled();
                    if (bb) {
                        isGoogleFitConnected = true;
                        pref.setGoogleFitEnabled("true");
                        googlefitenabled = pref.isGoogleFitEnabled();

                        send10Days_GFITSteps();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
                        LayoutInflater inflater = (LayoutInflater) WorkoutActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View layoutView = inflater.inflate(R.layout.alert_install_googlefit, null, false);
                        if (Constants.type == Constants.Type.AYUBO) {
                            TextView textView = layoutView.findViewById(R.id.lbl_app_version);
                            textView.setText("AyuboLife needs to fetch your step count from GoogleFit and other Google services.");
                        }
                        builder.setView(layoutView);

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")));

                            }
                        });

                        AlertDialog dialog = builder.create();
                        //    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.rgb(255,147,4));
                        dialog.show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                mClient.connect();

                if (mClient.isConnected()) {
                    pref.setGoogleFitEnabled("true");
                    googlefitenabled = pref.isGoogleFitEnabled();

                    send10Days_GFITSteps();
                }
                //  Toast.makeText(WorkoutActivity.this, "GoogleFit connecting error", Toast.LENGTH_LONG).show();
            }

        } else {
            System.out.println("========== FIT Not Connected=====================");
            mClient = new GoogleApiClient.Builder(this)
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
                                    isGoogleFitConnected = true;
                                    try {

                                        boolean bb = fitInstalled();
                                        if (bb) {
                                            pref.setGoogleFitEnabled("true");
                                            pref.setAfterGFitEnabled("true");

                                            googlefitenabled = pref.isGoogleFitEnabled();

                                            send10Days_GFITSteps();

                                        } else {
                                            pref.setGoogleFitEnabled("false");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
                                            LayoutInflater inflater = (LayoutInflater) WorkoutActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View layoutView = inflater.inflate(R.layout.alert_install_googlefit, null, false);
                                            if (Constants.type == Constants.Type.AYUBO) {
                                                TextView textView = layoutView.findViewById(R.id.lbl_app_version);
                                                textView.setText("AyuboLife needs to fetch your step count from GoogleFit and other Google services.");
                                            }
                                            builder.setView(layoutView);

                                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")));

                                                }
                                            });

                                            AlertDialog dialog = builder.create();
                                            //    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.rgb(255,147,4));
                                            dialog.show();

                                        }

                                    }

//                                try {
//                                    new InsertAndVerifyDataTask0().execute();
                                    catch (Exception e) {
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
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.i(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                            pref.setGoogleFitEnabled("false");
                            Snackbar.make(WorkoutActivity.this.findViewById(android.R.id.content),
                                    "Unable to connect GoogleFit",
                                    Snackbar.LENGTH_INDEFINITE).setAction("DISMISS",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();

                        }
                    })
                    .build();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    private void Service_sendAyuboStepsToServer_ForTenDays0() {
        sendAyuboStepsToServer_ForTenDays0 task = new sendAyuboStepsToServer_ForTenDays0();
        task.execute(new String[]{ApiClient.BASE_URL_live});
    }


    private class sendAyuboStepsToServer_ForTenDays0 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if(isNeedProgressLoading){
//                prgDialog.setCancelable(false);
//                prgDialog.show();
//                prgDialog.setMessage("Syncing steps...");
//            }


        }

        @Override
        protected String doInBackground(String... urls) {
            sendAyuboSteps_ForTenDays0();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

//            if(!isFinishing()) {
//                if(prgDialog!=null){
//                    prgDialog.cancel();
//                }
//            }

            Service_Service_getStepsData_ServiceCall();


        }
    }

    private void sendAyuboSteps_ForTenDays0() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        float value = Float.parseFloat(stepsFromDB_forADay0);
        String sMets, sCals, sDis;
        double metsVal = (value / 130) * 4.5;
        sMets = String.format("%.0f", metsVal);
        double calories = (metsVal * 3.5 * 70) / 200;
        sCals = String.format("%.0f", calories);
        double distance = (value * 78) / (float) 100000;
        sDis = String.format("%.2f", distance);

        String run_count = null;

        if (stepStatus.equals("bad")) {
            run_count = "22";
        } else {
            run_count = "11";
        }

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"activity\": \"" + "activity_AYUBO" + "\"," +
                        "\"energy\": \"" + sMets + "\"," +
                        "\"steps\": \"" + stepsFromDB_forADay0 + "\"," +
                        "\"calorie\": \"" + sCals + "\", " +
                        "\"duration\": \"" + "0" + "\"," +
                        "\"distance\": \"" + sDis + "\"," +
                        "\"date\": \"" + dateFromDB_forADay0 + "\"," +
                        "\"walk_count\": \"" + stepsFromDB_forADay0 + "\"," +
                        "\"run_count\": \"" + run_count + "\"," +
                        "\"version\": \"" + versionName + "\"," +
                        "\"osType\": \"" + "android" + "\"," +
                        "\"device_modal\": \"" + device_modal
                        + "\"" +
                        "}";

//        Parameter #0 [  $userid ]
//        Parameter #1 [  $alldata ]
//        Parameter #2 [  $version ]
//        Parameter #3 [  $osType ]

        nameValuePair.add(new BasicNameValuePair("method", "addDailyActivitySummary"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
        stepsSentToDBStatus = "99";

        System.out.println("....Today......addDailyActivitySummary....." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("..........response..........." + response);

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String number = jsonObj.optString("number").toString();

            if (number.isEmpty()) {

                String res = jsonObj.optString("result").toString();

                int result = Integer.parseInt(res);


                if (result == 11) {
                    stepsSentToDBStatus = "11";
                }
                if (result == 0) {
                    pref.setLastStepsData(Integer.parseInt(stepsFromDB_forADay0));
                    stepsSentToDBStatus = "0";

                    System.out.println("..........stepsFromDB_forADay0................" + stepsFromDB_forADay0);
                }

            } else {

                stepsSentToDBStatus = "99";

            }


//                        if (number.equals("10")) {
//                            statusFromServiceAPI_db = "11";
//                            System.out.println("=========================LOGIN FAIL=========>" + number);
//                        } else {


        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }


    private void Service_Service_getStepsData_ServiceCall() {

        if (Utility.isInternetAvailable(WorkoutActivity.this)) {
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }
    }

    private class Service_getStepsData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            makeSetDefaultDevice();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("========Service Success===========0");
        }

    }

    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"date\": \"" + today + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getActivityDetails"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("........getActivityDetails..response..........." + response);
            System.out.println("========Service Calling============0");

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObj = new JSONObject(responseString);

            String res = jsonObj.optString("result").toString();

            int result = Integer.parseInt(res);

            if (result == 0) {
                System.out.println("========Service Calling============4");
                try {

                    serviceDataStatus = "0";
                    String data = jsonObj.optString("data").toString();
                    JSONObject jsonData = new JSONObject(data);

                    total_steps = jsonData.optString("total_steps").toString();
                    total_calories = jsonData.optString("total_calories").toString();
                    total_distance = jsonData.optString("total_distance").toString();

                    total_duration = jsonData.optString("total_duration").toString();
                    total_energy = jsonData.optString("total_energy").toString();

                    pref.createUserData(total_steps, total_calories, total_energy, total_distance, total_duration);


                } catch (Exception e) {
                    serviceDataStatus = "99";
                    e.printStackTrace();
                }
            } else {
                System.out.println("========Service Calling============10");
                serviceDataStatus = "99";
            }


        } catch (Exception e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        }
    }


}
