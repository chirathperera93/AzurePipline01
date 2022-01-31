package com.ayubo.life.ayubolife.goals_extention;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.fragments.HomePage_Utility;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.goals_extention.models.DashboardDate;
import com.ayubo.life.ayubolife.goals_extention.models.dashboard.DashboardMainResponse;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.model.DB4String;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.EngagementType;
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.programs.DashBoardAdapter;

import com.ayubo.life.ayubolife.programs.data.model.Entitlement;
import com.ayubo.life.ayubolife.programs.data.model.Goal;
import com.ayubo.life.ayubolife.programs.data.model.MedicalChart;
import com.ayubo.life.ayubolife.programs.data.model.NewDashboardMainData;
import com.ayubo.life.ayubolife.programs.data.model.NewDashboardMainResponse;
import com.ayubo.life.ayubolife.programs.data.model.TextChart;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity;
import com.ayubo.life.ayubolife.timeline.OpenPostActivity;
import com.ayubo.life.ayubolife.utility.Ram;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class DashBoard_Activity extends AppCompatActivity {
    HorizontalScrollView hscrollViewMain;
    int intWidthGoal, intHeightGoal, intWidth;

    TextView txt_thirtydays_days, txt_nintydays_days, txt_years_days;
    TextView txt_today_text, txt_senven_days, txt_select_user_gruop, txt_user_name, txt_user_steps, txt_user_avg_steps;
    ImageView img_me_image, img_company_logo, img_user_picture, btn_backImgBtn, img_summery_image;
    PrefManager pref;
    LinearLayout dateView;
    LayoutInflater inflater;
    List<Date> dateList = null;
    String selectedDate;
    String currentStrDate, userid_ExistingUser;
    int fullWidth;

    String appToken;
    int fullWidthScroll;
    String name, steps;
    String proPic;
    boolean isFirst;
    com.ayubo.life.ayubolife.goals_extention.models.dashboard.Goal goalObj;
    com.ayubo.life.ayubolife.goals_extention.models.dashboard.AverageCategory stepAverageObj;
    String catogoryId;
    boolean isToday = false;
    String today;
    SimpleDateFormat formatWith_yyyy_MM_dd;
    LinearLayout lay_btnBack;
    Context context;
    CardView cardview1;
    RecyclerView programdashboard_recycler;
    DashBoardAdapter adapter;

    com.ayubo.life.ayubolife.goals_extention.models.dashboard.Data mainDataObj = null;

    public com.ayubo.life.ayubolife.goals_extention.models.dashboard.Data getGoalObj() {
        return mainDataObj;
    }

    public void setGoalObj(com.ayubo.life.ayubolife.goals_extention.models.dashboard.Data goalObj) {
        this.mainDataObj = goalObj;
    }

    com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo progressBar;
    String meta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_);

        programdashboard_recycler = findViewById(R.id.programdashboard_recycler);
        progressBar = findViewById(R.id.progressBar);

        meta = getIntent().getStringExtra("meta");

        constructor();

        Ram.setIsDashboardFirsttime(true);

//        initUI();
//
//        initEvents();
//
//        setUpArray();
//
//        initData();

    }


    @Override
    protected void onResume() {
        super.onResume();

        catogoryId = pref.getGoalCategory();

        if (catogoryId != null) {
            if (catogoryId.length() > 0) {
                callWellnessDashboard(appToken, today, catogoryId);
            } else {
                catogoryId = "";
                callWellnessDashboard(appToken, today, catogoryId);
            }
        } else {
            catogoryId = "";
            callWellnessDashboard(appToken, today, catogoryId);
        }
    }

    void constructor() {

        pref = new PrefManager(DashBoard_Activity.this);
        appToken = pref.getUserToken();
        formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
        isFirst = true;
        context = DashBoard_Activity.this;
        Calendar todayCal = Calendar.getInstance();
        today = formatWith_yyyy_MM_dd.format(todayCal.getTime());
    }

    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            //do stuff here
            int ssss = hscrollViewMain.getScrollX();

            if (isFirst) {
                int scfullWidthScroll = dateView.getWidth();
                int cellWidth = scfullWidthScroll - 340;
                fullWidthScroll = cellWidth / 30;
            }
            System.out.println("================ssss====" + ssss);
            //  int width =  hscrollViewMain.getMaxScrollAmount();
            System.out.println("================fullWidthScroll====" + fullWidthScroll);
            int cc = ssss / fullWidthScroll;
            System.out.println("================cc====" + cc);

            if (cc > 26) {
                txt_today_text.setText("Today");
            } else {
                Date todayy = dateList.get(cc);
                int month = todayy.getMonth();
                String monthName = getMonth(month);
                txt_today_text.setText(monthName);
            }


        }
    };

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    //   final ScrollView scrollView = (ScrollView) findViewById(R.id.scroller);

    private void initUI() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        int originalintWidth = deviceScreenDimension.getDisplayWidth();
        intHeightGoal = (originalintWidth / 100) * 51;
        intWidthGoal = (originalintWidth / 100) * 94;

        cardview1 = (CardView) findViewById(R.id.card_one);
        cardview1.setLayoutParams(new RelativeLayout.LayoutParams(intWidthGoal, intHeightGoal));


        hscrollViewMain = (HorizontalScrollView) findViewById(R.id.scrollViewMain_dashb);
        dateView = (LinearLayout) findViewById(R.id.dateView);
        lay_btnBack = (LinearLayout) findViewById(R.id.lay_btnBack);

        hscrollViewMain.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (observer == null) {
                    observer = hscrollViewMain.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                } else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = hscrollViewMain.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }

                return false;
            }
        });


        programdashboard_recycler = findViewById(R.id.programdashboard_recycler);
        txt_select_user_gruop = (TextView) findViewById(R.id.txt_select_user_gruop);
        txt_today_text = (TextView) findViewById(R.id.txt_today_text);

        txt_senven_days = (TextView) findViewById(R.id.txt_senven_days);
        txt_thirtydays_days = (TextView) findViewById(R.id.txt_thirtydays_days);
        txt_nintydays_days = (TextView) findViewById(R.id.txt_nintydays_days);
        txt_years_days = (TextView) findViewById(R.id.txt_years_days);

        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        img_user_picture = (ImageView) findViewById(R.id.img_user_picture);

        //img_show_icon= (ImageView) findViewById(R.id.img_show_icon);
        img_company_logo = (ImageView) findViewById(R.id.img_company_logo);

        txt_user_steps = (TextView) findViewById(R.id.txt_user_steps);
        txt_user_avg_steps = (TextView) findViewById(R.id.txt_user_avg_steps);


        img_summery_image = (ImageView) findViewById(R.id.img_summery_image);
        btn_backImgBtn = (ImageView) findViewById(R.id.btn_backImgBtn);

    }

    private void initEvents() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        txt_years_days.setText(Integer.toString(year));

        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, StepHistory_Activity.class);
                startActivity(i);
            }
        });

        txt_senven_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextToDetailsActivity("1");
            }
        });

        txt_thirtydays_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextToDetailsActivity("2");
            }
        });
        txt_nintydays_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextToDetailsActivity("3");
            }
        });
        txt_years_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextToDetailsActivity("4");
            }
        });

        txt_select_user_gruop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard_Activity.this, SelecteAverageType_Activity.class);
                i.putExtra("from", "summery_view");
                i.putExtra("meta", getIntent().getStringExtra("meta"));
                startActivity(i);
                finish();
            }
        });


    }

    void nextToDetailsActivity(String number) {
        Intent i = new Intent(DashBoard_Activity.this, DashboardViewActivity.class);
        i.putExtra("daysCount", number);
        i.putExtra("image", proPic);
        pref.setGoalCategory(catogoryId);
        //   i.putExtra("catogoryId", catogoryId);
        startActivity(i);
    }

    void clickOnGoals() {

        String status = pref.getMyGoalData().get("my_goal_status");

        if (status.equals("Pending")) {
            Intent intent = new Intent(DashBoard_Activity.this, AchivedGoal_Activity.class);
            startActivity(intent);
        } else if (status.equals("Pick")) {
            Intent intent = new Intent(DashBoard_Activity.this, PickAGoal_Activity.class);
            startActivity(intent);
        } else if (status.equals("Completed")) {
            showAlert_Deleted(DashBoard_Activity.this, "This goal has been achieved for the day. Please pick another goal tomorrow");
        }
    }

    public void showAlert_Deleted(Context c, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_and_share, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_share = (TextView) layoutView.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                pref.setIsGoalAdded("yes");

                String my_goal_sponser_large_image = pref.getMyGoalData().get("my_goal_sponser_large_image");
                String my_goal_share_large_image = pref.getMyGoalData().get("my_goal_share_large_image");
                String goalName = pref.getMyGoalData().get("my_goal_name");

                Intent intent = new Intent(DashBoard_Activity.this, ShareGoals_Activity.class);
                intent.putExtra("sponserURl", my_goal_sponser_large_image);
                intent.putExtra("shareImageURl", my_goal_share_large_image);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "no");
                startActivity(intent);

            }
        });


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });

        dialog.show();
    }

    void setupGoalObject(com.ayubo.life.ayubolife.goals_extention.models.dashboard.Goal obj) {

        if (obj == null) {

        } else {
            String goalId = obj.getUserGoalId();
            String sgoalStatus = obj.getIsAchieved();
            String sgoalName = obj.getGoalName();
            String sgoalImage = obj.getTileImageUrl();
            String slargeImage = obj.getLargeImageUrl();
            String shareImage = obj.getShareImage();
            String sponserLImage = obj.getSponserLargeImageUrl();
            String buttonText = "";
            String title = "";

            if (sgoalImage.contains("zoom_level")) {
                sgoalImage = sgoalImage.replace("zoom_level", "xxxhdpi");
            }

            if (sgoalStatus.equals("pick")) {
                title = "Pick";
                buttonText = "Pick";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, "");
            } else if (sgoalStatus.equals("pending")) {
                title = "Pending";
                buttonText = "Let's Do It";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, "");
            } else if (sgoalStatus.equals("yes")) {
                title = "Completed";
                buttonText = "Done";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, "");
            } else {
                pref.setMyGoalData("", "", "", "", "", "", "", "", "");
            }
        }
    }

    private void initData() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());

        intWidth = deviceScreenDimension.getDisplayWidth();
        intWidth = intWidth * 6;

        int strokeColor;
        int fillColor;

        dateView.removeAllViews();

        for (int num = 0; num < dateList.size(); num++) {

            inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.goal_dashboard_dateview, null);

            RelativeLayout image_container = (RelativeLayout) myView.findViewById(R.id.image_container);
            TextView txt_date = (TextView) myView.findViewById(R.id.txt_date);

            String dayName = Integer.toString(dateList.get(num).getDate());

            txt_date.setText(dayName);
            int dataNum = num - 1;
            Date dobj = dateList.get(num);
            //  String  strDate=Integer.toString(dateList.get(num).getDate());

            Calendar startDay = Calendar.getInstance();
            Date currentDate = startDay.getTime();

            String strDate = formatWith_yyyy_MM_dd.format(dobj.getTime());

            if (strDate.equals(selectedDate)) {
                System.out.println("Date1 is equal to Date2");
                strokeColor = Color.parseColor("#f6f7f9");
                fillColor = Color.parseColor("#000000");
                txt_date.setTextColor(Color.parseColor("#ffffff"));
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                txt_date.setBackground(gD);
            } else if (dateList.get(num).compareTo(currentDate) > 0) {
                System.out.println("Date1 is after Date2");
                //  No data, make it disable==============================
                strokeColor = Color.parseColor("#c9c9c9");
                fillColor = Color.parseColor("#ffffff");
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                txt_date.setBackground(gD);
                //History data make it black
                txt_date.setTextColor(Color.parseColor("#a1a194"));
            } else if (dateList.get(num).compareTo(currentDate) < 0) {
                System.out.println("Date1 is before Date2");

                strokeColor = Color.parseColor("#f6f7f9");
                fillColor = Color.parseColor("#f6f7f9");
                txt_date.setTextColor(Color.parseColor("#000000"));
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                txt_date.setBackground(gD);
                //History data make it black
                txt_date.setTextColor(Color.parseColor("#000000"));
            } else {
                System.out.println("How to get here?");
            }


            DashBoardEventDateObject oc = new DashBoardEventDateObject(getApplicationContext());
            oc.setDate(dobj);
            myView.setOnClickListener(oc);

            dateView.addView(myView);

            if (selectedDate.equals(currentStrDate)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        hscrollViewMain.post(new Runnable() {
                            public void run() {
                                hscrollViewMain.scrollTo(intWidth, 0);
                            }
                        });
                    }
                });
            }


        }


    }

    private void setUpArray() {
        dateList = new ArrayList<>();
        Calendar startDay = Calendar.getInstance();
        Date currentDate = startDay.getTime();
        startDay.add(Calendar.DATE, -30);
        Date oldDate = startDay.getTime();

        selectedDate = formatWith_yyyy_MM_dd.format(currentDate.getTime());

        currentStrDate = selectedDate;

        dateList = Utility.getDatesBetweenUsingJava7(oldDate, currentDate);

    }

    class DashBoardEventDateObject implements View.OnClickListener {
        Context c;

        Date date;

        DashboardDate favourite;

        public DashBoardEventDateObject(Context c) {
            this.c = c;
        }

        @Override
        public void onClick(View v) {

            Date dateToday = new Date();

            if (dateToday.compareTo(date) < 0) {
                Toast.makeText(DashBoard_Activity.this, "No data for future dates ", Toast.LENGTH_LONG).show();
            } else {

                String dateString = formatWith_yyyy_MM_dd.format(date.getTime());
                String dayName = Integer.toString(date.getDate());
                Integer reduceByOne = Integer.parseInt(dayName);
                reduceByOne = reduceByOne - 1;
                //  selectedDate=Integer.toString(reduceByOne);

                selectedDate = dateString;

                initData();


                if (catogoryId != null) {
                    if (catogoryId.length() > 10) {
                        callWellnessDashboard(appToken, dateString, catogoryId);
                    } else {
                        catogoryId = "";
                        callWellnessDashboard(appToken, dateString, catogoryId);
                    }
                } else {
                    catogoryId = "";
                    callWellnessDashboard(appToken, dateString, catogoryId);
                }

            }
        }

        public void setFavourite(DashboardDate favourite) {
            this.favourite = favourite;
        }


        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public void getGoalImages(String policy_user_id) {


        String appToken = pref.getUserToken();
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<NewDashboardMainResponse> call = apiService.getDashBoardData(AppConfig.APP_BRANDING_ID, appToken, policy_user_id);
        call.enqueue(new Callback<NewDashboardMainResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewDashboardMainResponse> call, Response<NewDashboardMainResponse> response) {
                progressBar.setVisibility(View.GONE);
                boolean status = response.isSuccessful();
                if (status) {
                    if (response.body() != null) {
                        System.out.println("==============");
                        NewDashboardMainData data = response.body().getData();
                        List<Object> dataList = new ArrayList<Object>();

                        if (data != null) {


                            if (data.getDashboardAvailable().equals("1")) {
                                DB4String dbData = new DB4String("Show Dashboard", "", "", "");
                                dataList.add(dbData);
                            }
                            if (data.getEngagement().equals("1")) {
                                EngagementType dbData = new EngagementType("Show Engagemnt", data.getEngagementLevel());
                                dataList.add(dbData);
                            }


                            // Ram.setGoalObject(mainDataObj);
                            String healthText = data.getHealthStatText();
                            String lifePoints = data.getLifePoints();

                            if ((healthText != null) && (healthText.length() > 0)) {
                                DBString dbData = new DBString("Health Status", healthText);
                                dataList.add(dbData);
                            }


                            List<MedicalChart> chartList = null;
                            if (data.getMedicalCharts() != null) {
                                chartList = data.getMedicalCharts();
                                dataList.addAll(chartList);
                            }

                            List<TextChart> textChartList = data.getTextChart();
                            List<Goal> gaolList = data.getGoals();
                            List<Entitlement> entitledList = data.getEntitlements();


                            if (textChartList != null) {
                                dataList.addAll(textChartList);
                            }


                            if (gaolList.size() > 0) {
                                dataList.add("Goals");
                                dataList.addAll(gaolList);
                            }
                            if (entitledList.size() > 0) {
                                dataList.add("Entitlements");
                                dataList.addAll(entitledList);
                            }
                            DisplayMetrics metrics = getResources().getDisplayMetrics();
                            int den1 = metrics.densityDpi;
                            int densityDpi = (int) metrics.density;

                            Display disply = getWindowManager().getDefaultDisplay();
                            int height = disply.getHeight(); //1360 1920
                            int wee = disply.getWidth();//720    1080


                            Display display = getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int width = size.x;

                            adapter = new DashBoardAdapter(DashBoard_Activity.this, dataList, (width), densityDpi, lifePoints, height);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashBoard_Activity.this, LinearLayoutManager.VERTICAL, false);

                            programdashboard_recycler.setLayoutManager(linearLayoutManager);
                            programdashboard_recycler.setItemAnimator(new DefaultItemAnimator());
                            programdashboard_recycler.setAdapter(adapter);

                            adapter.OnClickBack(new DashBoardAdapter.OnClickBackInterface() {
                                @Override
                                public void onBackToPrevious() {
                                    finish();
                                }
                            });

                            adapter.OnClickViewHistory(new DashBoardAdapter.OnClickHistoryInterface() {
                                @Override
                                public void onViewHistory() {
                                    Intent i = new Intent(context, StepHistory_Activity.class);
                                    startActivity(i);
                                }
                            });

                            adapter.OnClickEntitilementButton(new DashBoardAdapter.OnClickEntitilementButtonInterface() {
                                @Override
                                public void onEntitilementButtonClick(String a, String m) {

                                    processAction(a, m);
                                }
                            });
                            adapter.OnClick7DaysViewGraph(new DashBoardAdapter.OnClick7DaysGraphInterface() {
                                @Override
                                public void onView7DayGraph() {
                                    nextToDetailsActivity("1");
                                }
                            });
                            adapter.OnClick30DaysViewGraph(new DashBoardAdapter.OnClick30DaysGraphInterface() {
                                @Override
                                public void onView30DayGraph() {
                                    nextToDetailsActivity("2");
                                }
                            });
                            adapter.OnClick90DaysViewGraph(new DashBoardAdapter.OnClick90DaysGraphInterface() {
                                @Override
                                public void onView90DayGraph() {
                                    nextToDetailsActivity("3");
                                }
                            });
                            adapter.OnClick365DaysViewGraph(new DashBoardAdapter.OnClick365DaysGraphInterface() {
                                @Override
                                public void onView365DayGraph() {
                                    nextToDetailsActivity("4");
                                }
                            });
                            adapter.OnClickSelectAverageType(new DashBoardAdapter.OnClickSelecteAverageTypeInterface() {
                                @Override
                                public void onSelecteAverageType(String sDate) {
                                    Intent i = new Intent(DashBoard_Activity.this, SelecteAverageType_Activity.class);
                                    i.putExtra("from", "summery_view");
                                    i.putExtra("meta", getIntent().getStringExtra("meta"));
                                    startActivity(i);
                                    finish();
                                }
                            });

                            adapter.OnClickViewReloadSteps(new DashBoardAdapter.OnClickReloadStepsInterface() {
                                @Override
                                public void onViewReloadSteps(String dateString) {

                                    String catogoryId = pref.getGoalCategory();
                                    String appToken = pref.getUserToken();

                                    if (catogoryId != null) {
                                        if (catogoryId.length() > 10) {
                                            callWellnessDashboard(appToken, dateString, catogoryId);
                                        } else {
                                            catogoryId = "";
                                            callWellnessDashboard(appToken, dateString, catogoryId);
                                        }
                                    } else {
                                        catogoryId = "";
                                        callWellnessDashboard(appToken, dateString, catogoryId);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(DashBoard_Activity.this, "No data found", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<NewDashboardMainResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }

    void processAction(String action, String meta) {

        if (action.equals("challenge")) {
            onMapChallangeClick(meta);
        }
        if (action.equals("chat")) {
            onAskClick(meta);
        }
        if (action.equals("program")) {
            onProgramPostClick(meta);
        }
        if (action.equals("commonview")) {
            onCommonViewClick(meta);
        }
        if (action.equals("web")) {
            onBowserClick(meta);
        }
        if (action.equals("help")) {
            onHelpClick(meta);
        }
        if (action.equals("reports")) {
            onReportsClick(meta);
        }
        if (action.equals("goal")) {
            onGoalClick(meta);
        }
        if (action.equals("videocall")) {
            onVideoCallClick(meta);
        }
        if (action.equals("channeling")) {
            onButtonChannelingClick(meta);
        }
        if (action.equals("janashakthiwelcome")) {
            onJanashakthiWelcomeClick(meta);
        }
        if (action.equals("janashakthireports")) {
            onJanashakthiQuestionClick(meta);
        }
        if (action.equals("dynamicquestion")) {
            onDyanamicQuestionClick(meta);
        }
        if (action.equals("post")) {
            onPostClick(meta);
        }
        if (action.equals("native_post")) {
            onNativePostClick(meta);
        }
        if (action.equals("prescription")) {
            Intent intent = new Intent(this, Medicine_ViewActivity.class);
            startActivity(intent);
        }


    }

    public void callWellnessDashboard(String token, String date, String catogoryId) {

        ApiInterface apiService = ApiClient.getGoalExApiClient().create(ApiInterface.class);
        progressBar.setVisibility(View.VISIBLE);
        Call<DashboardMainResponse> call = apiService.getWellnessDashboard(AppConfig.APP_BRANDING_ID, token, date, catogoryId);
        call.enqueue(new Callback<DashboardMainResponse>() {
            @Override
            public void onResponse(Call<DashboardMainResponse> call, Response<DashboardMainResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {


                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(DashBoard_Activity.this, LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            mainDataObj = response.body().getData();
                            setGoalObj(mainDataObj);

                            name = mainDataObj.getName();
                            steps = mainDataObj.getSteps();
                            proPic = mainDataObj.getProfilePictureLink();
                            goalObj = mainDataObj.getGoal();
                            stepAverageObj = mainDataObj.getAverageCategory();
                            // setSummeryViewData();
                            Ram.setGoalObject(mainDataObj);
                            getGoalImages(meta);

                        }
                    }

                } else {
                    System.out.println("===========response===Fail============");
                }
            }

            @Override
            public void onFailure(Call<DashboardMainResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DashBoard_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }

    void startDoctorsActivity(String doctorID) {
        DocSearchParameters parameters;
        parameters = new DocSearchParameters();
        PrefManager pref = new PrefManager(this);
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }

    void onButtonChannelingClick(String meta) {
        if (meta.length() > 0) {
            startDoctorsActivity(meta);
        } else {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    void onVideoCallClick(String meta) {
        if (meta.length() > 0) {
            String activity = "my_doctor";
            Intent intent = new Intent(this, MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", meta);
            intent.putExtra("activityName", activity);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MyDoctor_Activity.class);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        }


    }

    void onMapChallangeClick(String meta) {

        if (meta.length() > 0) {
            Intent intent = new Intent(this, MapChallengeActivity.class);
            intent.putExtra("challenge_id", meta);
            startActivity(intent);
//            MapChallangesServices serviceObj =new MapChallangesServices(this, meta);
//            serviceObj.Service_getChallengeMapData_ServiceCall();
        } else {
            Intent intent = new Intent(this, NewCHallengeActivity.class);
            startActivity(intent);
        }
    }

    void onAskClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, AyuboChatActivity.class);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AskQuestion_Activity.class);
            startActivity(intent);
        }
    }

    void onProgramPostClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, SingleTimeline_Activity.class);
            intent.putExtra("related_by_id", meta);
            intent.putExtra("type", "program");
            startActivity(intent);
        }
    }

    void onGoalClick(String meta) {

        if (meta.length() > 0) {
            PrefManager prefManager = new PrefManager(this);
            String status = prefManager.getMyGoalData().get("my_goal_status");

            if (status == "Pending") {
                Intent intent = new Intent(this, AchivedGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Pick") {
                Intent intent = new Intent(this, PickAGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Completed") {
                HomePage_Utility serviceObj = new HomePage_Utility(this);
                serviceObj.showAlert_Deleted(this, "This goal has been achieved for the day. Please pick another goal tomorrow");
            }
        }
    }

    void onNativePostClick(String meta) {
        Intent intent = new Intent(this, NativePostActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onPostClick(String meta) {
        Intent intent = new Intent(this, OpenPostActivity.class);
        intent.putExtra("postID", meta);
        startActivity(intent);
    }

    void onDyanamicQuestionClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setRelateID(meta);
        pref.setIsJanashakthiDyanamic(true);

        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    void onJanashakthiWelcomeClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setRelateID(meta);
        pref.setIsJanashakthiDyanamic(true);

        Intent intent = new Intent(this, JanashakthiWelcomeActivity.class);
        startActivity(intent);
    }

    void onJanashakthiQuestionClick(String meta) {
        MedicalUpdateActivity.startActivity(this);
    }

    void onReportsClick(String meta) {
        Intent intent = new Intent(this, ReportDetailsActivity.class);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    void onLifePointsClick(String activityName, String meta) {
        Intent intent = new Intent(this, LifePointActivity.class);
        startActivity(intent);
    }

    void onHelpClick(String meta) {
        Intent intent = new Intent(this, HelpFeedbackActivity.class);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    void onBowserClick(String meta) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    void onCommonViewClick(String meta) {
        Intent intent = new Intent(this, CommonWebViewActivity.class);
        intent.putExtra("URL", meta);
        startActivity(intent);
    }

}
