package com.ayubo.life.ayubolife.timeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.ContactInfoActivity;
import com.ayubo.life.ayubolife.activity.HealthViewActivityNew;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.goals_extention.DashBoard_Activity;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.goals_extention.models.DashboardDate;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.pojo.timeline.main.Goal;
import com.ayubo.life.ayubolife.pojo.timeline.main.QuickLink;
import com.ayubo.life.ayubolife.pojo.timeline.main.TopTile;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.qrcode.DecoderActivity;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapterCallback;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationScrollListener;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FilteredTimeline_Activity extends AppCompatActivity implements PaginationAdapterCallback {
    LinearLayout errorLayout;
    ProgressDialog proDialog;
    int intWidth, intHeight;
    float hi;
    LayoutInflater inflater;
    public PaginationAdapter adapter;
    public ArrayList<Post> timelinePostlist;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    Button btnRetry;
    TextView txtError;
    private final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    Context contextt;
    int offset = 10;
    PrefManager pref;
    String userid_ExistingUser, hasToken, maxPostId, appToken, created_by_id;
    String type;
    String marketplace_Token_status, marketplace_Token;
    String service_checkpoints, enabled_checkpoints;
    List<Date> dateList = null;
    String selectedDate;
    String currentStrDate;
    String challenge_id, serviceDataStatus, badgesJsonData;
    String cityJsonString, noof_day;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> myTreasure = null;
    int total_steps;
    String tip_icon, tip, tipheading;
    String cards, weekSteps, showpopup, white_lines;
    boolean isClicked = true;
    String tip_header_1, tip_header_2, tip_type, tip_meta;
    SimpleDateFormat formatWith_yyyy_MM_dd;
    LinearLayout lay_btnBack;
    Context context;
    CardView cardview1;
    List<TopTile> serviceTopTileList = null;
    List<QuickLink> serviceBottomTileList = null;
    private List<Date> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    String from_date, to_date, meta, filteredDate;
    int numberOdDays;
    SimpleDateFormat format;
    TextView txt_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_timeline_);

        UIConstructor();
        type = getIntent().getStringExtra("type");
        created_by_id = getIntent().getStringExtra("related_by_id");

        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("end_date");
        meta = getIntent().getStringExtra("meta");
        filteredDate = from_date;
        //  2018-03-20

        // created_by_id="9f4aa4a4-0c80-8bfd-ba41-5894069edb38";
        type = "program";
        created_by_id = "aaa6bd1a-2851-11e9-9776-000d3aa00fd6";

        DataConstructor();

        numberOdDays = getNumberOdDays(from_date, to_date);
        numberOdDays = numberOdDays + 1;


        txt_month = (TextView) findViewById(R.id.txt_month);


        setMonthText(from_date);

        loadMainService(from_date);


    }

    void setMonthText(String dateStr) {
        Calendar c = Calendar.getInstance();
        Date parse = null;
        try {
            parse = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(parse);


        String sMoth = getMonth(c.get(Calendar.MONTH));
        System.out.println("==================motnth" + c.get(Calendar.MONTH));
        txt_month.setText(sMoth);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    int getNumberOdDays(String from, String to) {
        int days = 0;


        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(from);
            d2 = format.parse(to);
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            days = (int) diffDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return days;
    }


    void UIConstructor() {

        format = new SimpleDateFormat("yyyy-MM-dd");

        context = FilteredTimeline_Activity.this;
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Please wait...");
        proDialog.setCancelable(false);
        pref = new PrefManager(this);


        rv = findViewById(R.id.main_recycler);
        rv.setHasFixedSize(false);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        bottom_menu_view = (LinearLayout) findViewById(R.id.bottom_menu_view);


        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                //bottom_menu_view.setVisibility(View.GONE);
                loadNextPage(appToken);

                System.out.println("===========loadNextPage========================");
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage_Posts(appToken);
            }
        });


        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);

        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    void DataConstructor() {

        userid_ExistingUser = pref.getLoginUser().get("uid");
        String name = pref.getLoginUser().get("name");
        hasToken = pref.getLoginUser().get("hashkey");
        contextt = FilteredTimeline_Activity.this;
        appToken = pref.getUserToken();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;

        System.out.println("===========intWidth==================" + intWidth);
        System.out.println("===========intHeight==================" + intHeight);

    }


    void loadMainService(String filterDate) {


        MyProgressLoading.showLoading(FilteredTimeline_Activity.this, "Please wait...");


        String serviceName = "homePageTiles";

        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"filter_related_id\": \"" + meta + "\"," +
                        "\"filter_date\": \"" + filterDate + "\"" +
                        "}";


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call = apiService.callMainService(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> response) {
                MyProgressLoading.dismissDialog();
                if (response.isSuccessful()) {

                    //   response.code()
                    com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse = response.body();
                    System.out.println("====Path n 2kkkk=====================");
                    processMainResults(mainResponse);

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Throwable t) {

                MyProgressLoading.dismissDialog();

                Toast.makeText(FilteredTimeline_Activity.this, "Server is temporarily unavailable", Toast.LENGTH_LONG).show();

            }
        });
    }


    void processMainResults(com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse) {

        List<Goal> goalList = mainResponse.getGoal();

        if ((serviceTopTileList != null) && (serviceTopTileList.size() > 0)) {
            serviceTopTileList.clear();
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            serviceBottomTileList.clear();
        }

        serviceBottomTileList = mainResponse.getQuickLinks();
        serviceTopTileList = mainResponse.getTopTiles();

        if (serviceTopTileList != null) {
            serviceTopTileList.add(0, new TopTile("", "", "", "", "first", "", "", "", "", "", "", ""));
        }

        System.out.println("====Path n 3=====================");
        String deviceName = pref.getDeviceData().get("stepdevice");
        System.out.println("====Path n 4=====================" + deviceName);
        Log.i("Activity type ---0 ", deviceName);
        if (deviceName != null) {
            Log.i("Activity type ---0 ", deviceName);
            if (deviceName.equals("activity_AYUBO")) {
                Log.i("Activity type --- 1", deviceName);
                if (pref.isGoogleFitEnabled().equals("false")) {
                    Log.i("Activity type --- 2", deviceName);
                    if (serviceTopTileList != null) {
                        Log.i("Activity type --- 3", deviceName);
                        System.out.println("====Path n 5=====================" + deviceName);
                        serviceTopTileList.add(1, new TopTile("to view challenges", "Connect GoogleFit", "Connect", MAIN_URL_APPS + "static/hh/google_fit_bg.png", "googlefit", "", MAIN_URL_APPS + "static/hh/connect.png", "", "", "", "", ""));
                    }
                }
            }
        }


        if ((goalList != null) && (goalList != null)) {

//         .................GOALS DATA PRODESSING........................
            for (int i = 0; i < goalList.size(); i++) {
                Goal objc = goalList.get(i);
                String goalId = objc.getUserGoalId();
                String sgoalStatus = objc.getIsAchieved();
                String sgoalName = objc.getGoalName();
                String sgoalImage = objc.getTileImageUrl();
                String slargeImage = objc.getLargeImageUrl();
                String shareImage = objc.getShareImageUrl();
                String sponserLImage = objc.getSponserImageUrl();
                String bgImage = objc.getBgImage();
                String buttonText = "";
                String title = "";

                if (sgoalImage.contains("zoom_level")) {
                    sgoalImage = sgoalImage.replace("zoom_level", "xxxhdpi");
                }

                if (sgoalStatus.equals("pick")) {
                    title = "Pick";
                    buttonText = "Pick";
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
                } else if (sgoalStatus.equals("pending")) {
                    title = "Pending";
                    buttonText = "Let's Do It";
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
                } else if (sgoalStatus.equals("yes")) {
                    title = "Completed";
                    buttonText = "Done";
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
                } else {
                    title = "";
                    buttonText = "";
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
                }
            }
        }


        loadTimelineHeader();
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        movieList= setUpArray();
//        MoviesAdapter  mAdapter = new MoviesAdapter(movieList);
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);


        if ((pref.getUserToken() != null) && (pref.getUserToken().length() > 50)) {
            loadFirstPage_Posts(pref.getUserToken());
        } else {
            Toast.makeText(FilteredTimeline_Activity.this, "Service token unavailable", Toast.LENGTH_LONG).show();
        }


    }


    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<Date> moviesList;
        int strokeColor;
        int fillColor;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_date, year, genre;

            public MyViewHolder(View view) {
                super(view);
                txt_date = (TextView) view.findViewById(R.id.txt_date);
                // genre = (TextView) view.findViewById(R.id.genre);
                // year = (TextView) view.findViewById(R.id.year);
            }
        }


        public MoviesAdapter(List<Date> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.goal_dashboard_dateview, parent, false);


//            View myView = inflater.inflate(R.layout.goal_dashboard_dateview, null);
//
//            RelativeLayout image_container = (RelativeLayout) myView.findViewById(R.id.image_container);
//            TextView txt_date = (TextView) myView.findViewById(R.id.txt_date);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // String sDate = moviesList.get(position);

            String dayName = Integer.toString(dateList.get(position).getDate());

            int dataNum = position - 1;
            Date dobj = dateList.get(position);

            Calendar startDay = Calendar.getInstance();
            Date currentDate = startDay.getTime();

            String strDate = formatWith_yyyy_MM_dd.format(dobj.getTime());
            holder.txt_date.setText(dayName);

            holder.txt_date.setTag(strDate);

            holder.txt_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String obj = (String) v.getTag();
                    filteredDate = obj;

                    setMonthText(filteredDate);

                    loadMainService(obj);

                    System.out.println("Date1 is equal to Date2" + obj);
                }
            });

            if (strDate.equals(selectedDate)) {
                System.out.println("Date1 is equal to Date2");
                strokeColor = Color.parseColor("#f6f7f9");
                fillColor = Color.parseColor("#ff860b");
                holder.txt_date.setTextColor(Color.parseColor("#ffffff"));
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                holder.txt_date.setBackground(gD);
            } else if (dateList.get(position).compareTo(currentDate) > 0) {
                System.out.println("Date1 is after Date2");
                //  No data, make it disable==============================
                strokeColor = Color.parseColor("#c9c9c9");
                fillColor = Color.parseColor("#ffffff");
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                holder.txt_date.setBackground(gD);
                //History data make it black
                holder.txt_date.setTextColor(Color.parseColor("#a1a194"));
            } else if (dateList.get(position).compareTo(currentDate) < 0) {
                System.out.println("Date1 is before Date2");

                strokeColor = Color.parseColor("#f6f7f9");
                fillColor = Color.parseColor("#f6f7f9");
                holder.txt_date.setTextColor(Color.parseColor("#000000"));
                int strokeWidth = 2;
                GradientDrawable gD = new GradientDrawable();
                gD.setColor(fillColor);
                gD.setShape(GradientDrawable.OVAL);
                gD.setStroke(strokeWidth, strokeColor);
                holder.txt_date.setBackground(gD);
                //History data make it black
                holder.txt_date.setTextColor(Color.parseColor("#000000"));
            } else {
                System.out.println("How to get here?");
            }


            // holder.txt_date.setText("Test");
            //   holder.genre.setText(movie.getGenre());
            //   holder.year.setText(movie.getYear());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }


    private List<Date> setUpArray() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = null;
        Date toDate = null;

        try {
            fromDate = format.parse(from_date);
            toDate = format.parse(to_date);

//            dateList = new ArrayList<>();
            Calendar startDay = Calendar.getInstance();
            Date currentDate = startDay.getTime();
//            startDay.add(Calendar.DATE, -numberOdDays);
//            Date oldDate = startDay.getTime();
            formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
            selectedDate = formatWith_yyyy_MM_dd.format(currentDate.getTime());

            currentStrDate = selectedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return dateList = Utility.getDatesBetweenUsingJava7(fromDate, toDate);


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
                Toast.makeText(FilteredTimeline_Activity.this, "No data for future dates ", Toast.LENGTH_LONG).show();
            } else {

                String dateString = formatWith_yyyy_MM_dd.format(date.getTime());
                String dayName = Integer.toString(date.getDate());
                Integer reduceByOne = Integer.parseInt(dayName);
                reduceByOne = reduceByOne - 1;
                //  selectedDate=Integer.toString(reduceByOne);

                selectedDate = dateString;


//                if (catogoryId != null) {
//                    if (catogoryId.length() > 10) {
//                        callWellnessDashboard(appToken, dateString, catogoryId);
//                    } else {
//                        catogoryId = "";
//                        callWellnessDashboard(appToken, dateString, catogoryId);
//                    }
//                } else {
//                    catogoryId = "";
//                    callWellnessDashboard(appToken, dateString, catogoryId);
//                }

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


    void loadTimelineHeader() {
        ArrayList<Post> timelinePostLi = new ArrayList<Post>();
        timelinePostLi.add(new Post(0, "", "", "", 0, false, 0, "", 0, null, null, "", "", "", "", "", "", false, false, "", "", "", "", ""));
        adapter = new PaginationAdapter(contextt, userid_ExistingUser);

        adapter.clear();
        adapter.addAll(timelinePostLi);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieList = setUpArray();
        MoviesAdapter mAdapter = new MoviesAdapter(movieList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    void loadTimeline(ArrayList<Post> timelinePostList) {

        //  hideErrorView();

        adapter.clear();
        adapter.addAll(timelinePostList);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        // setUpArray();

        // initData();
//        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//        else isLastPage = true;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieList = setUpArray();
        MoviesAdapter mAdapter = new MoviesAdapter(movieList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        //  prepareMovieData();
        if (selectedDate.equals(currentStrDate)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    recyclerView.post(new Runnable() {
                        public void run() {
                            recyclerView.smoothScrollToPosition(numberOdDays);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void retryPageLoad() {
        //  loadNextPage(appToken);
    }

    void loadFirstPage_Posts(String appToke) {

        //  proDialog.show();
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);


        Call<AllPostTimeline> call = apiService.getFilteredTimeline(AppConfig.APP_BRANDING_ID, appToke, currentPage, offset, false, filteredDate, maxPostId);

        call.enqueue(new Callback<AllPostTimeline>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<AllPostTimeline> response) {
                com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                //  proDialog.cancel();
                if (response.isSuccessful()) {
                    final com.ayubo.life.ayubolife.pojo.timeline.Data timeline_firstpage_data = obj.getData();

                    if (timeline_firstpage_data != null) {

                        ArrayList<Post> timelinePostLi = (ArrayList<Post>) timeline_firstpage_data.getPosts();
                        timeline_firstpage_data.getPagination().getCount();
                        TOTAL_PAGES = 1;
                        currentPage = 1;
                        maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                        TOTAL_PAGES = timeline_firstpage_data.getPagination().getPages();
                        currentPage = timeline_firstpage_data.getPagination().getPage();
                        maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                        if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                            timelinePostlist.clear();
                        }
                        timelinePostlist = timelinePostLi;
                        // Online Loading Timeline array ...


                        if (Utility_Timeline.isValidList(timelinePostlist)) {
                            adapter = new PaginationAdapter(contextt, userid_ExistingUser);
                            rv.setAdapter(adapter);
                            loadTimeline(timelinePostlist);
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                //  proDialog.cancel();
                Toast.makeText(FilteredTimeline_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                //   showErrorView(t);
            }
        });


    }


    void loadNextPage(String appToke) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);

        if (type.equals("system")) {


            Call<AllPostTimeline> call = apiService.getTimelineSinglePost(AppConfig.APP_BRANDING_ID, appToke, created_by_id, currentPage, offset, maxPostId);

            // Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getTimelineSinglePost(AppConfig.APP_BRANDING_ID,appToke, currentPage, offset, maxPostId);
            call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {

                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                    //  bottom_menu_view.setVisibility(View.VISIBLE);
                    if (response.isSuccessful()) {
                        com.ayubo.life.ayubolife.pojo.timeline.Data data = obj.getData();

                        if (data != null) {
                            ArrayList<Post> timelinePostList = (ArrayList<Post>) data.getPosts();

                            if (timelinePostList.size() > 0) {
                                addAllToArray(timelinePostList);


                                TOTAL_PAGES = data.getPagination().getPages();
                                currentPage = data.getPagination().getPage();
                                maxPostId = data.getPagination().getMaxPostId();

                                adapter.removeLoadingFooter();
                                isLoading = false;

                                System.out.println("============currentPage======================" + currentPage);
                                adapter.addAll(timelinePostList);
                                adapter.notifyDataSetChanged();


//                            if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                            else isLastPage = true;
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    System.out.print(t.toString());
                    //   adapter.showRetry(true, fetchErrorMessage(t));

                }
            });

        }

        if (type.equals("program")) {

            Call<AllPostTimeline> call = apiService.getTimelineProgramPost(AppConfig.APP_BRANDING_ID, appToke, created_by_id, currentPage, offset, maxPostId);
            call.enqueue(new Callback<AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<AllPostTimeline> response) {
                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                    proDialog.cancel();
                    if (response.isSuccessful()) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data data = obj.getData();

                        if (data != null) {

                            if (data != null) {
                                ArrayList<Post> timelinePostList = (ArrayList<Post>) data.getPosts();

                                if (timelinePostList.size() > 0) {


                                    addAllToArray(timelinePostList);

                                    TOTAL_PAGES = data.getPagination().getPages();
                                    currentPage = data.getPagination().getPage();
                                    maxPostId = data.getPagination().getMaxPostId();

                                    adapter.removeLoadingFooter();
                                    isLoading = false;

                                    System.out.println("============currentPage======================" + currentPage);
                                    adapter.addAll(timelinePostList);
                                    adapter.notifyDataSetChanged();


//                                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                                else isLastPage = true;
                                }
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    proDialog.cancel();
                    Toast.makeText(FilteredTimeline_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                    //   showErrorView(t);
                }
            });


        }
    }


    class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        boolean shouldShow_TitlePanel = true;
        boolean shouldShow_ContentPanel = true;
        boolean shouldShow_UserInteractionPanel = true;

        boolean shouldShow_MediaPanel = false;
        boolean shouldShow_ProgramPanel = false;
        boolean shouldShow_OverlayImage = false;
        boolean shouldShow_LinkPreviewPanel = false;


        // View Types
        private static final int ITEM = 0;
        private static final int LOADING = 1;
        private static final int HEADER = 2;


        private List<Post> movieResults;
        private Context context;

        private boolean isLoadingAdded = false;
        private boolean retryPageLoad = false;
        String userid_ExistingUser, cellType;
        private PaginationAdapterCallback mCallback;

        private String errorMsg;

        public PaginationAdapter(Context context, String userid_ExistingUser) {
            this.context = context;
            this.mCallback = (PaginationAdapterCallback) context;
            movieResults = new ArrayList<>();
            this.userid_ExistingUser = userid_ExistingUser;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.native_timeline_cell, parent, false);
                    viewHolder = new PostCell(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                    break;
                case HEADER:
                    View viewHero = inflater.inflate(R.layout.new_home_widgets_layout, parent, false);
                    viewHolder = new ServiceMenuView(viewHero);
                    break;
            }
//
            return viewHolder;

        }


//        public void remove(int position) {
//            movieResults.remove(position);
//            notifyItemRemoved(position);
//        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {

            switch (getItemViewType(position)) {

                case HEADER:
                    //loadDynamicMenus();
                    final ServiceMenuView heroVh = (ServiceMenuView) holde;

                    heroVh.layout_services_menu_horizontal.removeAllViews();
                    heroVh.layout_main_menu_horizontal.removeAllViews();

                    heroVh.txt_whats_on_mind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FilteredTimeline_Activity.this, Timeline_NewPost_Activity.class);
                            startActivity(intent);

                        }
                    });
                    heroVh.whatson_content_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FilteredTimeline_Activity.this, Timeline_NewPost_Activity.class);
                            startActivity(intent);

                        }
                    });

                    String imagepath_db = pref.getLoginUser().get("image_path");
                    String burlImg = MAIN_URL_LIVE_HAPPY + imagepath_db;
                    if (burlImg == null) {
                        heroVh.profilePicture.setVisibility(View.GONE);
                    } else {
                        heroVh.profilePicture.setVisibility(View.VISIBLE);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(FilteredTimeline_Activity.this));
                        Glide.with(FilteredTimeline_Activity.this).load(burlImg)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(heroVh.profilePicture);
                    }

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());

                    int height = displayMetrics.heightPixels;
                    int width = deviceScreenDimension.getDisplayWidth();

                    int singleTileWidth = width / 4;
                    singleTileWidth = singleTileWidth + singleTileWidth / 4;
                    //==========================================================
                    // LOADING SERVICES MENU===================================

                    for (int num = 0; num < serviceTopTileList.size(); num++) {

                        TopTile serviceObject = serviceTopTileList.get(num);
                        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        // View mainSingleTilesView = inflater.inflate(R.layout.newdisign_menu_item, null);

                        View mainSingleTilesView = inflater.inflate(R.layout.card_timeline_new_design_goals, null);
                        View space = inflater.inflate(R.layout.row_item, null);
                        RelativeLayout image_container = (RelativeLayout) mainSingleTilesView.findViewById(R.id.image_container);
                        TextView txt_desc1 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc2);
                        TextView txt_desc2 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc1);
                        //  TextView btn_action = (TextView) mainSingleTilesView.findViewById(R.id.btn_action);

                        // LinearLayout btn_action_lay = (LinearLayout) mainSingleTilesView.findViewById(R.id.btn_action_lay);
                        ImageView top_tile_bg = (ImageView) mainSingleTilesView.findViewById(R.id.img_quicl_link_bg);
                        ImageView img_goal_achive_icon = (ImageView) mainSingleTilesView.findViewById(R.id.img_goal_achive_icon);


                        txt_desc1.setTag(serviceObject);
                        txt_desc2.setTag(serviceObject);

                        top_tile_bg.setTag(serviceObject);

                        txt_desc1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TopTile obj = (TopTile) v.getTag();
                                callOnPickAGoal(obj);
                            }
                        });
                        txt_desc2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TopTile obj = (TopTile) v.getTag();
                                callOnPickAGoal(obj);
                            }
                        });

                        top_tile_bg.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TopTile obj = (TopTile) v.getTag();
                                callOnPickAGoal(obj);
                            }
                        });

                        String smallIcon = serviceObject.getIcon();
                        String mainImage = serviceObject.getImage();

                        if (serviceObject.getImage() != null) {
                            if (mainImage.contains("zoom_level")) {
                                mainImage = mainImage.replace("zoom_level", "xxxhdpi");
                            } else if (mainImage.contains("zoom")) {
                                mainImage = mainImage.replace("zoom", "xxxhdpi");
                            }
                        } else {
                            mainImage = "";
                        }

                        if (serviceObject.getIcon() != null) {
                            if (smallIcon.contains("zoom_level")) {
                                smallIcon = smallIcon.replace("zoom_level", "xxxhdpi");
                            } else if (smallIcon.contains("zoom")) {
                                smallIcon = smallIcon.replace("zoom", "xxxhdpi");
                            }
                        } else {
                            smallIcon = "";
                        }


                        //  int imageSize = Utility.getImageSizeFor_DeviceDensitySize(200);

                        intWidth = deviceScreenDimension.getDisplayWidth();

                        String text = serviceObject.getTitle();

                        System.out.println("==============text================" + text);
                        int tileWidth = 0;
                        int tileHeigth = 0;
                        int myUnit = 0;
                        myUnit = intWidth / 720;
                        tileHeigth = myUnit * 190;

                        if (text.length() < 15) {
                            tileWidth = myUnit * 290;
                        } else if (text.length() < 20) {
                            tileWidth = myUnit * 390;
                        } else if (text.length() > 20) {
                            tileWidth = myUnit * 400;
                        }
                        System.out.println("==============tileWidth================" + tileWidth);
                        System.out.println("==============tileHeigth================" + tileHeigth);


                        RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);

                        // RequestOptions requestOptionsSamll = new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).override(tileWidth, tileHeigth);
                        Glide.with(FilteredTimeline_Activity.this).load(smallIcon)
                                .apply(requestOptionsSamll)
                                .into(img_goal_achive_icon);


                        String my_goal_name = null;
                        if (num != 0) {
                            my_goal_name = serviceObject.getTitle();
                        } else {
                            my_goal_name = pref.getMyGoalData().get("my_goal_name");
                            if (my_goal_name.equals("")) {
                                my_goal_name = "Pick a goal";
                            }
                            System.out.println("===========my_goal_name=============" + my_goal_name);
                        }

                        if (my_goal_name.contains(" ")) {

                            String[] splited = my_goal_name.split("\\s+");

                            String secondName = null;
                            String ste1 = splited[0];

                            boolean isTwoWords = false;
                            if (splited.length > 2) {
                                ste1 = splited[0] + " " + splited[1];
                                if (ste1.length() < 18) {
                                    isTwoWords = true;
                                } else {
                                    ste1 = splited[0];
                                }
                            }
                            txt_desc1.setText(ste1);


                            if (splited.length == 2) {
                                secondName = splited[1];
                            } else if (splited.length == 3) {
                                if (isTwoWords) {
                                    secondName = splited[2];
                                } else {
                                    secondName = splited[1] + " " + splited[2];
                                }

                            } else if (splited.length == 4) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3];
                                }

                            } else if (splited.length == 5) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4];
                                }

                            } else if (splited.length == 6) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                }

                            } else if (splited.length == 7) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                }
                            } else if (splited.length == 8) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                }
                            } else if (splited.length == 9) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                }

                            } else if (splited.length == 10) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                }

                            } else if (splited.length == 11) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }

                            } else if (splited.length == 12) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 13) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 14) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 15) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 16) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 17) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 18) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 19) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 20) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            } else if (splited.length == 21) {
                                if (isTwoWords) {
                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                } else {
                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                }
                            }


                            //  System.out.println("==========secondName=============" + secondName);
                            if (secondName != null) {
                                if (secondName.length() >= 20) {
                                    secondName = secondName.substring(0, Math.min(secondName.length(), 15));
                                    secondName = secondName + "...";
                                }
                                txt_desc2.setText(secondName);
                            }

                            txt_desc1.setText(serviceObject.getSubTitle());
                            txt_desc2.setText(serviceObject.getTitle());

                        } else {
                            txt_desc1.setText(my_goal_name);
                            txt_desc2.setText("");
                        }
                        String te = serviceObject.getActionText();

                        if (num == 0) {

                            String goalStatus = pref.getMyGoalData().get("my_goal_status");
                            String goalImage = pref.getMyGoalData().get("my_goal_image");
                            String goalBgImage = pref.getMyGoalData().get("my_goal_bg");

                            my_goal_name = pref.getMyGoalData().get("my_goal_name");


                            txt_desc1.setText(serviceObject.getSubTitle());
                            txt_desc2.setText(serviceObject.getTitle());


                            RequestOptions requestOption = new RequestOptions().override(tileWidth, tileHeigth).diskCacheStrategy(DiskCacheStrategy.NONE);
                            //  goalImage
                            Glide.with(FilteredTimeline_Activity.this).load(goalBgImage)
                                    .apply(requestOption)
                                    .into(top_tile_bg);

                            Glide.with(FilteredTimeline_Activity.this).load(goalImage)
                                    .apply(requestOptionsSamll)
                                    .into(img_goal_achive_icon);


                            if (goalStatus == null) {

                                Glide.with(FilteredTimeline_Activity.this).load(goalBgImage)
                                        .apply(requestOption)
                                        .into(top_tile_bg);

                                Glide.with(FilteredTimeline_Activity.this).load(goalImage)
                                        .apply(requestOptionsSamll)
                                        .into(img_goal_achive_icon);

                                if (goalStatus.equals("Pending")) {
                                    //   btn_action.setTextColor(Color.parseColor("#ff860b"));
                                    txt_desc1.setText("Let's Do It");

                                    System.out.println("==============Pending===============" + te);
                                } else if (goalStatus.equals("Pick")) {
                                    txt_desc2.setText("Pick a");
                                    txt_desc1.setText("goal");
                                } else if (goalStatus.equals("Completed")) {
                                    System.out.println("==============Completed===============" + te);
                                    txt_desc1.setText("Done");
                                    //  txt_desc1.setTextColor(Color.parseColor("#45D100"));
                                }
//
//                                }//
                            } else {
                                Glide.with(FilteredTimeline_Activity.this).load(goalBgImage)
                                        .apply(requestOption)
                                        .into(top_tile_bg);

                                Glide.with(FilteredTimeline_Activity.this).load(goalImage)
                                        .apply(requestOptionsSamll)
                                        .into(img_goal_achive_icon);

                                if (goalStatus.equals("Pending")) {
                                    txt_desc2.setText(my_goal_name);
                                    txt_desc1.setText("Let's Do It");
                                } else if (goalStatus.equals("Pick")) {
                                    txt_desc2.setText("Pick a");
                                    txt_desc1.setText("goal");
                                } else if (goalStatus.equals("Completed")) {
                                    txt_desc2.setText(my_goal_name);
                                    txt_desc1.setText("Done");
                                }


                            }
                        } else {
                            txt_desc1.setText(serviceObject.getSubTitle());
                            txt_desc2.setText(serviceObject.getTitle());

                            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).override(tileWidth, tileHeigth);
                            Glide.with(FilteredTimeline_Activity.this).load(mainImage)
                                    .apply(requestOptions)
                                    .into(top_tile_bg);

                        }


                        String deviceName = pref.getDeviceData().get("stepdevice");


                        if ((!deviceName.equals("activity_AYUBO"))) {
                            //Hide challenges Titles .................................
                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
                            heroVh.layout_main_menu_horizontal.addView(space);

                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
                        } else if ((pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("join_adv_challenge"))) || (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("view_adv_challenge")))) {
                            //Hide challenges Titles .................................
                            Log.d("NewHomeDesign", "Hiding Tiles bcos not GFit Connected============");
                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
                            heroVh.layout_main_menu_horizontal.addView(space);
                        } else {
                            //Show other  Titles .................................
                            Log.d("NewHomeDesign", "Showing other Tiles============");
                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
                            heroVh.layout_main_menu_horizontal.addView(space);

                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
                        }

                    }


                    //==========================================================
                    // LOADING SERVICES MENU===================================
                    for (int i = 0; i < serviceBottomTileList.size(); i++) {
                        final QuickLink objSecond = serviceBottomTileList.get(i);
                        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        //  View myViewSec = inflater.inflate(R.layout.newdisign_menu_item_footer, null);

                        View myViewSec = inflater.inflate(R.layout.card_timeline_new_design_quicks, null);
                        View space = inflater.inflate(R.layout.row_item, null);

                        final TextView txt_firstline = (TextView) myViewSec.findViewById(R.id.txt_service_name);

                        intWidth = deviceScreenDimension.getDisplayWidth();

                        int tileHieght = 0;
                        int tileWidth = 0;
                        int imgHieght = 0;

                        tileWidth = getQWidth(context);
                        tileHieght = getQHidth(context);

                        imgHieght = getImageHidth(context);


                        myViewSec.setLayoutParams(new FrameLayout.LayoutParams(tileWidth, tileHieght));

                        LinearLayout lay_imgview = myViewSec.findViewById(R.id.lay_imgview);

                        final ImageView quicl_link_icon = (ImageView) myViewSec.findViewById(R.id.footer_remote_image_icon);
                        final ImageView remote_image_icon_quicklink = (ImageView) myViewSec.findViewById(R.id.remote_image_icon_quicklink);

                        lay_imgview.setLayoutParams(new LinearLayout.LayoutParams(imgHieght, imgHieght));

                        quicl_link_icon.setTag(objSecond);
                        txt_firstline.setTag(objSecond);

                        txt_firstline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QuickLink objS = (QuickLink) txt_firstline.getTag();

                                clickedQuickLink(objS);

                            }
                        });

                        quicl_link_icon.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Perform action on click
                                QuickLink objS = (QuickLink) quicl_link_icon.getTag();

                                clickedQuickLink(objS);

                            }
                        });

                        if (objSecond.getIcon() != null) {

                            String img = objSecond.getIcon();

                            RequestOptions requestOption = new RequestOptions().fitCenter().override(tileWidth, tileHieght).diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(FilteredTimeline_Activity.this).load(MAIN_URL_APPS + "static/hh/quick_links2.png")
                                    .apply(requestOption)
                                    .into(remote_image_icon_quicklink);

                            img = img.replace("zoom_level", "xxxhdpi");

                            int imageSize = Utility.getImageSizeFor_DeviceDensitySize(50);
                            if (img == null) {
                                quicl_link_icon.setVisibility(View.GONE);
                            } else {
                                quicl_link_icon.setVisibility(View.VISIBLE);
                                RequestOptions requestOptions = new RequestOptions().fitCenter().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(context).load(img)
                                        .apply(requestOptions)
                                        .into(quicl_link_icon);
                            }


                        }

                        if ((objSecond.getTitle() != null) && ((objSecond.getTitle2() != null) && (objSecond.getTitle().length() > 0) && (objSecond.getTitle2().length() > 0))) {
                            String name = null;
                            if ((objSecond.getTitle().length() > 0) && (objSecond.getTitle2().length() > 0)) {
                                name = objSecond.getTitle() + "\n" + objSecond.getTitle2();
                                txt_firstline.setText(name);
                            }

                        } else if ((objSecond.getTitle() != null) && (objSecond.getTitle().length() > 0)) {
                            txt_firstline.setText(objSecond.getTitle());
                        } else if ((objSecond.getTitle2() != null) && (objSecond.getTitle2().length() > 0)) {
                            txt_firstline.setText(objSecond.getTitle2());
                        }

                        heroVh.layout_services_menu_horizontal.addView(myViewSec);
                        heroVh.layout_services_menu_horizontal.addView(space);

                    }

                    break;


                case ITEM:

                    System.out.println("=========onBindViewHolder======================" + position);

                    final Post obj = movieResults.get(position); // PostCell
                    final PaginationAdapter.PostCell holder = (PaginationAdapter.PostCell) holde;

                    if (obj.getPostId() == null) {
                        //   holder.user_panel.setVisibility(View.GONE);
                        holder.title_panel.setVisibility(View.GONE);
                        holder.content_panel.setVisibility(View.GONE);
                        holder.media_panel.setVisibility(View.GONE);
                        holder.user_link_panel.setVisibility(View.GONE);
                        holder.user_reaction_panel.setVisibility(View.GONE);
                    } else {

                        cellType = obj.getType();
                        if (cellType == null) {
                            cellType = "0";
                        }

                        //======   SETUP CELL STATUS ============================================
                        setupCellStatus(obj);
                        //======   SETUP CELL VIEW ============================================
                        setupCellViewForPost(obj, holder);


                        try {

                            String postedUserId = null;

                            holder.img_btn_close_intop.setTag(obj);
                            holder.img_btn_close_bglayout.setTag(obj);
                            holder.user_reaction_panel_liked_user_list.setTag(obj);
                            holder.user_reaction_panel_like_count.setTag(obj);
                            holder.user_reaction_panel_like_text.setTag(obj);
                            holder.user_reaction_panel_comment_clicked.setTag(obj);
                            holder.user_reaction_panel_comment_second_text.setTag(obj);
                            holder.img_user_picture.setTag(obj);
                            holder.main_background_image.setTag(obj);
                            holder.txt_panel_b_view_button.setTag(obj);

                            holder.img_user_picture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post p = (Post) v.getTag();
                                    System.out.println("===============Post ID===================" + p.getPostId());
                                    System.out.println("======getType=============" + p.getType());
                                    System.out.println("======getBody=============" + p.getBody());
                                    System.out.println("======getTitle=============" + p.getTitle());
                                    System.out.println("======getVideoThumbnail=============" + p.getVideoThumbnail());
                                    System.out.println("======getButtonText=============" + p.getButtonText());
                                    System.out.println("======getSubTitle=============" + p.getSubTitle());
                                    System.out.println("======getPostThumbnailUrl=============" + p.getPostThumbnailUrl());
                                    System.out.println("============================================================");
                                }
                            });


                            //======iS Delete Enable =========================================
                            if (obj.getUser() != null) {
                                postedUserId = obj.getUser().getId().toString();
                                if (userid_ExistingUser.equals(postedUserId)) {
                                    holder.img_btn_close_intop.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Post obj = (Post) holder.img_btn_close_intop.getTag();
                                            showAlert_Add(FilteredTimeline_Activity.this, appToken, "Are you sure you want to delete post?", obj);
                                        }
                                    });
                                    holder.img_btn_close_bglayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Post obj = (Post) holder.img_btn_close_intop.getTag();
                                            showAlert_Add(FilteredTimeline_Activity.this, appToken, "Are you sure you want to delete post?", obj);
                                        }
                                    });
                                }
                            }


                            //======has Likes =========================================
                            if (obj.getIsLiked() != null) {
                                if (obj.getIsLiked()) {
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                    holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                                } else {
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                                }
                            }

                            //======Like Text Clicked =========================================
                            holder.user_reaction_panel_like_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post pobj = (Post) holder.user_reaction_panel_like_text.getTag();

                                    String likedUsersText = obj.getLikedUsersText();
                                    Integer likeCount = obj.getLikeCount();

                                    if (pobj.getIsLiked()) {
                                        String likeCounts = null;
                                        pobj.setIsLiked(false);

                                        Utility_Timeline.sendALike(pobj.getPostId(), 0, appToken);

                                        Integer liked_count = obj.getLikeCount();

                                        String sss = null;
                                        if (likedUsersText.contains("You and ")) {
                                            sss = likedUsersText.substring(8);
                                        } else if (likedUsersText.contains("You, ")) {
                                            sss = likedUsersText.substring(5);
                                        } else if (likedUsersText.contains("You")) {
                                            sss = likedUsersText.substring(3);
                                        }

                                        if (liked_count > 0) {
                                            liked_count = liked_count - 1;
                                        }

                                        if (liked_count > 1) {
                                            likeCounts = Integer.toString(liked_count) + " Likes";
                                        } else if (liked_count == 1) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        } else if (liked_count == 0) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                            holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);

                                        }
                                        pobj.setLikedUsersText(sss);
                                        pobj.setLikeCount(liked_count);

                                        holder.user_reaction_panel_like_count.setText(likeCounts);
                                        holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                                        holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);

                                        holder.user_reaction_panel_liked_user_list.setText(sss);


                                    } else {
                                        holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);

                                        if (likeCount == 0) {
                                            likedUsersText = "You";
                                        } else if (likeCount == 1) {
                                            likedUsersText = "You and " + likedUsersText;
                                        } else {
                                            likedUsersText = "You, " + likedUsersText;
                                        }
                                        pobj.setLikedUsersText(likedUsersText);

                                        String likeCounts = null;
                                        pobj.setIsLiked(true);
                                        Integer liked_count = null;
                                        if (obj.getLikeCount() != null) {
                                            liked_count = obj.getLikeCount();
                                        }

                                        liked_count = liked_count + 1;

                                        if (liked_count > 1) {
                                            likeCounts = Integer.toString(liked_count) + " Likes";
                                        } else if (liked_count == 1) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        } else if (liked_count == 0) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        }

                                        pobj.setLikeCount(liked_count);

                                        holder.user_reaction_panel_like_count.setText(likeCounts);
                                        holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                                        holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                        holder.user_reaction_panel_liked_user_list.setText(likedUsersText);

                                        Utility_Timeline.sendALike(pobj.getPostId(), 1, appToken);
                                    }
                                }
                            });

                            //======Comments Clicked =========================================
                            holder.user_reaction_panel_comment_clicked.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FilteredTimeline_Activity.this, AddComments_Activity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });


                            //======Comments Clicked =========================================
                            holder.user_reaction_panel_comment_second_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(FilteredTimeline_Activity.this, AddComments_Activity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });

                            //======Play Button Clicked =========================================
                            holder.txt_panel_b_play_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (obj.getType().equals("VIDEO_POST")) {
                                        Intent intent = new Intent(FilteredTimeline_Activity.this, TimelineVideo_Activity.class);
                                        intent.putExtra("IURL", obj.getVideoThumbnail());
                                        intent.putExtra("URL", obj.getVideoUrl());
                                        startActivity(intent);
                                    } else if (obj.getType().equals("GIF_POST")) {
                                        Intent intent = new Intent(FilteredTimeline_Activity.this, TimelineGif_Activity.class);
                                        intent.putExtra("URL", obj.getGifUrl());
                                        startActivity(intent);
                                    }
                                }
                            });

                            //======Liked Users Lists Text Clicked =========================================
                            holder.user_reaction_panel_liked_user_list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Post obj = (Post) holder.user_reaction_panel_liked_user_list.getTag();

                                    Integer pID = obj.getPostId();
                                    Integer likeCount = obj.getLikeCount();

                                    Intent intent = new Intent(FilteredTimeline_Activity.this, LikedUsers_Activity.class);
                                    intent.putExtra("postId", Integer.toString(pID));
                                    intent.putExtra("likeCount", Integer.toString(likeCount));
                                    startActivity(intent);
                                }
                            });

                            //======Liked Users Lists Clciked=========================================
                            holder.user_reaction_panel_like_count.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post obj = (Post) holder.user_reaction_panel_like_count.getTag();
                                    Integer pID = obj.getPostId();
                                    Integer likeCount = obj.getLikeCount();

                                    Intent intent = new Intent(FilteredTimeline_Activity.this, LikedUsers_Activity.class);
                                    intent.putExtra("postId", Integer.toString(pID));
                                    intent.putExtra("likeCount", Integer.toString(likeCount));
                                    startActivity(intent);
                                }
                            });


                            //======Setup Profile Picture=========================================
                            if (obj.getUser() == null) {
                                holder.img_user_picture.setVisibility(View.GONE);
                            } else {
                                holder.img_user_picture.setVisibility(View.VISIBLE);
                                RequestOptions options = new RequestOptions()
                                        .override(intWidth, intHeight)
                                        .transform(new CircleTransform(context))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(context).load(obj.getUser().getProfilePicture())
                                        .thumbnail(0.5f)
                                        .apply(options)
                                        .transition(withCrossFade())
                                        .into(holder.img_user_picture);
                            }

                            //======Setup User Name =========================================
                            if (obj.getUser() != null) {
                                holder.txt_user_name.setText(obj.getUser().getName());
                            }

                            //======Set Post time =========================================
                            if (obj.getTimestamp() != null) {
                                holder.txt_time_ago.setText(Utility_Timeline.getTimeStringFromInteger(obj.getTimestamp()));
                            }

                            //======Set Like text =========================================
                            if (obj.getLikeCount() != null) {
                                holder.user_reaction_panel_like_count.setText(Utility_Timeline.getLikesStringFromCount(obj.getLikeCount()));
                            }

                            //======Set Comments count text =========================================
                            if (obj.getCommentCount() != null) {
                                holder.user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));
                            }

                            //======Set Likes Users List text =========================================
                            if (obj.getLikedUsersText() != null) {
                                holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                            }

                            //======Set Post Title =========================================
                            if (obj.getTitle() != null) {
                                holder.title_panel_heading_text.setText(obj.getTitle());
                            }
                            //======Set Post description =========================================
                            if (obj.getBody() != null) {
                                holder.text_panel_text_message.setText(obj.getBody());
                            }


                            //============LINK VIEW===================================================
                            if (shouldShow_LinkPreviewPanel) {
                                if (obj.getLinkInfo() != null) {
                                    holder.link_panel_text_url.setText(obj.getLinkInfo().getLinkUrl());
                                    holder.link_panel_text_desc.setText(obj.getLinkInfo().getTitle());
                                    holder.link_panel_timeline_readme.setTag(obj.getLinkInfo().getLinkUrl());
                                }
                                holder.link_panel_timeline_readme.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = (String) holder.link_panel_timeline_readme.getTag();
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(browserIntent);
                                    }
                                });
                            }


                            //============PROGRAM VIEW===================================================
                            if (shouldShow_ProgramPanel) {
                                if (obj.getSubTitle() != null) {
                                    holder.program_panel_text_normal.setText(obj.getSubTitle());
                                }
                                if (obj.getTitle() != null) {
                                    holder.program_panel_text_heading.setText(obj.getTitle());
                                }
                                if (obj.getButtonText() != null) {
                                    holder.txt_panel_b_view_button.setText(obj.getButtonText());
                                } else {
                                    holder.txt_panel_b_view_button.setText("View");
                                }

                                holder.txt_panel_b_view_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {
                                            Post pot = (Post) holder.txt_panel_b_view_button.getTag();

                                            if (obj.isExternalLink()) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                                startActivity(browserIntent);
                                            } else {
                                                Intent intent = new Intent(FilteredTimeline_Activity.this, CommonWebViewActivity.class);
                                                intent.putExtra("URL", pot.getRedirectUrl());
                                                startActivity(intent);
                                            }


                                        } else {
                                            Post objjj = (Post) holder.txt_panel_b_view_button.getTag();
                                            openZoomImage(position, objjj);

                                        }

                                    }
                                });
                            }


                            //======Main Image click event =========================================
                            holder.main_background_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                        if (obj.isExternalLink()) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                            startActivity(browserIntent);
                                        } else {
                                            Intent intent = new Intent(context, CommonWebViewActivity.class);
                                            intent.putExtra("URL", obj.getRedirectUrl());
                                            startActivity(intent);
                                        }


                                    } else {
                                        Post objjj = (Post) holder.main_background_image.getTag();
                                        openZoomImage(position, objjj);
                                    }
                                }
                            });


                            //======Main Image Setup =========================================
                            String imageURL = Utility_Timeline.getImageUrlFromType(obj);
                            if ((imageURL == null) || (imageURL.equals(""))) {
                                holder.main_background_image.setVisibility(View.GONE);
                            }


                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(imageURL)
                                    .apply(options)
                                    .into(holder.main_background_image);

//                            if ((imageURL == null) || (imageURL.equals(""))) {
//                                holder.main_background_image.setVisibility(View.GONE);
//                            } else {
//                                holder.main_background_image.setVisibility(View.VISIBLE);
//                                holder.main_background_image.layout(0, 0, 0, 0);
//
//                                RequestOptions options = new RequestOptions()
//                                        .override(intWidth, intHeight)
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
//                                Glide.with(context).load(imageURL)
//                                        .apply(options)
//                                        .into(holder.main_background_image);
//                                System.out.println("==================imageURL==========================" + imageURL);
//                            }

                            holder.main_background_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                        if (obj.isExternalLink()) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                            startActivity(browserIntent);
                                        } else {
                                            Intent intent = new Intent(context, CommonWebViewActivity.class);
                                            intent.putExtra("URL", obj.getRedirectUrl());
                                            startActivity(intent);
                                        }


                                    } else {

                                        Post objjj = (Post) holder.main_background_image.getTag();
                                        openZoomImage(position, objjj);

                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case LOADING:
                    // LoadingVH loadingVH = (LoadingVH) holde;
//                final ServiceMenuView heroVh = (ServiceMenuView) holde;
//                final Post obj = movieResults.get(position); // PostCell
                    final PaginationAdapter.LoadingVH loadingVH = (PaginationAdapter.LoadingVH) holde;
                    if (retryPageLoad) {
                        loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                        loadingVH.loadmore_progress.setVisibility(View.VISIBLE);

                        loadingVH.mErrorTxt.setText(
                                errorMsg != null ?
                                        errorMsg :
                                        context.getString(R.string.error_msg_unknown));

                    } else {
                        loadingVH.mErrorLayout.setVisibility(View.GONE);
                        loadingVH.loadmore_progress.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {

            int finalCount;

            finalCount = movieResults == null ? 0 : movieResults.size();

            return finalCount;

        }

        void openZoomImage(Integer pos, Post pobj) {

            Intent intent = new Intent(FilteredTimeline_Activity.this, TimelineImage_Activity.class);
            if (pobj.getPostThumbnailUrl() != null) {
                intent.putExtra("position", pos);
                intent.putExtra("TURL", pobj.getPostThumbnailUrl());
                intent.putExtra("URL", pobj.getPostImageUrl());
                startActivity(intent);
            }

        }


        public int getImageSizeFor_DeviceDensitySize(int imageSize) {
            float density = Resources.getSystem().getDisplayMetrics().density;
            float fval = (imageSize / 2) * density;
            int den3 = (int) Math.round(fval);
            return den3;
        }


        private void setupCellStatus(Post postObj) {
            cellType = postObj.getType();

            if (postObj != null) {

                if (cellType.equals("NORMAL_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = false;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("IMAGE_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("VIDEO_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("GIF_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("LINK_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = true;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("SYSTEM_POST")) {
                    shouldShow_MediaPanel = postObj.hasImage();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_ContentPanel = postObj.hasBody();

                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;

                    if (postObj.isInteractionsEnabled()) {
                        shouldShow_UserInteractionPanel = true;   //E  Optional
                    } else {
                        shouldShow_UserInteractionPanel = false;  //E  Optional
                    }

                } else if (cellType.equals("PROGRAM_POST")) {
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = true;
                    shouldShow_TitlePanel = false;
                    shouldShow_ContentPanel = false;

                    shouldShow_UserInteractionPanel = false;   //E  Optional
                }
            }
        }

        private void setupCellViewForPost(Post postObj, PaginationAdapter.PostCell holder) {

            // Title visibility ..........................
            if (shouldShow_TitlePanel) {
                holder.title_panel_heading_text.setVisibility(View.VISIBLE);
            } else {
                holder.title_panel_heading_text.setVisibility(View.GONE);
            }

            // Content visibility ..........................
            if (shouldShow_ContentPanel) {
                holder.content_panel.setVisibility(View.VISIBLE);
            } else {
                holder.content_panel.setVisibility(View.GONE);
            }

            // //Media panel visibility  ..........................
            if (shouldShow_MediaPanel) {
                holder.media_panel.setVisibility(View.VISIBLE);
            } else {
                holder.media_panel.setVisibility(View.GONE);
            }

            // Program panel visibility  ..........................
            if (shouldShow_ProgramPanel) {
                holder.program_panel_text_heading.setVisibility(View.VISIBLE);
                holder.txt_panel_b_view_button.setVisibility(View.VISIBLE);
                holder.program_panel_text_normal.setVisibility(View.VISIBLE);
            } else {
                holder.program_panel_text_heading.setVisibility(View.GONE);
                holder.txt_panel_b_view_button.setVisibility(View.GONE);
                holder.program_panel_text_normal.setVisibility(View.GONE);

            }

            //Overlay image visibility  ....................
            if (shouldShow_OverlayImage) {
                holder.txt_panel_b_play_button.setVisibility(View.VISIBLE);
            } else {
                holder.txt_panel_b_play_button.setVisibility(View.GONE);

            }

            //User Interaction Panel panel visibiity....................
            if (shouldShow_UserInteractionPanel) {
                holder.user_reaction_panel.setVisibility(View.VISIBLE);
                if ((postObj.getLikedUsersText() == null) || (postObj.getLikedUsersText().equals(""))) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                }


            } else {
                holder.user_reaction_panel.setVisibility(View.GONE);

            }

            //Link preview panel visibiity....................
            if (shouldShow_LinkPreviewPanel) {
                holder.user_link_panel.setVisibility(View.VISIBLE);
            } else {
                holder.user_link_panel.setVisibility(View.GONE);

            }

            // Close button visibility ....................
            if (postObj.getUser() == null) {
            } else {
                if (userid_ExistingUser.equals(postObj.getUser().getId())) {
                    holder.img_btn_close_intop.setVisibility(View.VISIBLE);
                    holder.img_btn_close_bglayout.setVisibility(View.VISIBLE);

                } else {
                    holder.img_btn_close_intop.setVisibility(View.GONE);
                    holder.img_btn_close_bglayout.setVisibility(View.GONE);
                }
            }

            // Liked users text list  visibility ....................
            if (postObj.getLikedUsersText() != null) {
                if (postObj.getLikedUsersText().length() > 1) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                }

            }


        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        //test
        @Override
        public int getItemViewType(int position) {
            // if (isPositionHeader(position)) {
            if (isPositionHeader(position)) {
                return HEADER;
            } else {
                return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
            }
            //  return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }


    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

        /**
         * @param result
         * @return [releasedate] | [2letterlangcode]
         */
        private String formatYearLabel(Post result) {
//        return result.getReleaseDate().substring(0, 4)  // we want the year only
//                + " | "
//                + result.getOriginalLanguage().toUpperCase();

            return "00000000";
        }


//        private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
//            return Glide
//                    .with(context)
//                    .load(posterPath)
//                    .override(intWidth, intHeight)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
//                    .centerCrop()
//                    .crossFade();
//        }


        /*
            Helpers - Pagination
       _________________________________________________________________________________________________
        */
        public void addToTop(Post r) {
            movieResults.add(1, r);
            //  movieResults.add(r);
            notifyItemInserted(1);
        }

        public void add(Post r) {
            movieResults.add(r);
            int count = (movieResults.size() - 1);
            notifyItemInserted(count);
        }

        public void addAll(List<Post> moveResults) {
            for (Post result : moveResults) {
                add(result);
            }
        }

        public void remove(Post r) {
            int position = movieResults.indexOf(r);
            if (position > -1) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Post());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Post result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Post getItem(int position) {
            return movieResults.get(position);
        }

        /**
         * Displays Pagination retry footer view along with appropriate errorMsg
         *
         * @param show
         * @param errorMsg to display if page load fails
         */
        public void showRetry(boolean show, @Nullable String errorMsg) {
            retryPageLoad = show;
            notifyItemChanged(movieResults.size() - 1);

            if (errorMsg != null) this.errorMsg = errorMsg;
        }

        protected class ServiceMenuView extends RecyclerView.ViewHolder {

            TextView txt_desc1;
            TextView txt_desc2;
            TextView btn_action;

            TextView txt_whats_on_mind;
            ImageView profilePicture, whatson_content_camera;
            LinearLayout btn_action_lay;
            ImageView main_services_image_icon;
            LinearLayout layout_main_menu_horizontal, layout_services_menu_horizontal;

            public ServiceMenuView(View itemView) {
                super(itemView);
                layout_main_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal);
                layout_services_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal_footer);

                txt_desc1 = (TextView) itemView.findViewById(R.id.txt_desc1);
                txt_desc2 = (TextView) itemView.findViewById(R.id.txt_desc2);
                btn_action = (TextView) itemView.findViewById(R.id.btn_action);
                //  btn_action_lay = (LinearLayout) itemView.findViewById(R.id.btn_action_lay);
                main_services_image_icon = (ImageView) itemView.findViewById(R.id.remote_image_icon);

                whatson_content_camera = itemView.findViewById(R.id.whatson_content_camera);
                txt_whats_on_mind = (TextView) itemView.findViewById(R.id.lbl_whatsonyourmind);
                profilePicture = (ImageView) itemView.findViewById(R.id.img_timeline_post_click);

            }
        }

        /**
         * Main list's content ViewHolder
         */
        protected class PostCell extends RecyclerView.ViewHolder {

            LinearLayout img_btn_close_bglayout, user_reaction_panel_liked_user_list_layout, user_panel, title_panel, content_panel, user_link_panel, user_reaction_panel;
            ImageView user_reaction_panel_like_clicked_image, user_reaction_panel_liked_user_list_icon, img_user_picture, main_background_image, txt_panel_b_play_button;
            TextView txt_user_name, txt_time_ago, user_reaction_panel_like_text;
            ImageButton img_btn_close_intop, link_panel_timeline_readme;
            ConstraintLayout media_panel;
            Button txt_panel_b_view_button;
            TextView program_panel_text_heading, link_panel_text_url, link_panel_text_desc;
            TextView program_panel_text_normal, title_panel_heading_text, text_panel_text_message;
            TextView user_reaction_panel_liked_user_list, user_reaction_panel_like_count, user_reaction_panel_comment_count, user_reaction_panel_comment_clicked;
            TextView user_reaction_panel_comment_second_text;
            LinearLayout main_cell_view;

            public PostCell(View convertView) {
                super(convertView);
                main_cell_view = (LinearLayout) convertView.findViewById(R.id.main_cell_view);
                //  TYPE TIMELINE
                user_panel = (LinearLayout) convertView.findViewById(R.id.user_panel);
                title_panel = (LinearLayout) convertView.findViewById(R.id.title_panel);
                content_panel = (LinearLayout) convertView.findViewById(R.id.content_panel);
                media_panel = (ConstraintLayout) convertView.findViewById(R.id.media_panel);
                user_link_panel = (LinearLayout) convertView.findViewById(R.id.user_link_panel);
                user_reaction_panel = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel);
                user_reaction_panel_liked_user_list_layout = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

                // panel G
                txt_panel_b_view_button = (Button) convertView.findViewById(R.id.txt_panel_b_view_button);
                program_panel_text_heading = (TextView) convertView.findViewById(R.id.program_panel_text_heading);
                program_panel_text_normal = (TextView) convertView.findViewById(R.id.txt_panel_b_text_day);

                // button Play
                txt_panel_b_play_button = (ImageView) convertView.findViewById(R.id.txt_panel_b_play_button);

                user_reaction_panel_like_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_text);
                user_reaction_panel_like_clicked_image = (ImageView) convertView.findViewById(R.id.user_reaction_panel_like_clicked_image);
                user_reaction_panel_comment_second_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_second_text);

                main_background_image = (ImageView) convertView.findViewById(R.id.imageView2);

                // user_panel
                img_user_picture = (ImageView) convertView.findViewById(R.id.img_user_picture);
                txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
                txt_time_ago = (TextView) convertView.findViewById(R.id.txt_time_ago);
                img_btn_close_intop = (ImageButton) convertView.findViewById(R.id.img_btn_close_intop);
                img_btn_close_bglayout = (LinearLayout) convertView.findViewById(R.id.img_btn_close_bglayout);

                link_panel_timeline_readme = (ImageButton) convertView.findViewById(R.id.link_panel_timeline_readme);

                link_panel_text_url = (TextView) convertView.findViewById(R.id.link_panel_text_url);
                link_panel_text_desc = (TextView) convertView.findViewById(R.id.link_panel_text_desc);

                // viewHolder.user_reaction_panel_like_button = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_clicked);
                user_reaction_panel_like_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_count);
                user_reaction_panel_comment_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_count);
                user_reaction_panel_comment_clicked = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_clicked);
                user_reaction_panel_liked_user_list = (TextView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list);
                user_reaction_panel_liked_user_list_icon = (ImageView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_icon);
                // title_panel
                title_panel_heading_text = (TextView) convertView.findViewById(R.id.title_panel_heading_text);

                // text_panel
                text_panel_text_message = (TextView) convertView.findViewById(R.id.text_panel_text_message);

            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar loadmore_progress;
            private ImageButton mRetryBtn;
            private TextView mErrorTxt;
            private LinearLayout mErrorLayout;

            public LoadingVH(View itemView) {
                super(itemView);

                loadmore_progress = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
                mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
                mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
                mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loadmore_retry:
                    case R.id.loadmore_errorlayout:

                        showRetry(false, null);
                        mCallback.retryPageLoad();

                        break;
                }
            }
        }

    }

    void clickedQuickLink(QuickLink objS) {

        //  FirebaseAnalytics Adding
        String tileName = objS.getTitle().toString();
        Bundle bPromocode_used = new Bundle();
        bPromocode_used.putString("link", tileName);
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Quick_link_clicked", bPromocode_used);
        }

        System.out.println("========getTitle=============" + objS.getTitle());

        if (objS.getType().equals("chanelling")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, DashboardActivity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "chanelling");
            startActivity(intent);
        } else if
        (objS.getType().equals("doc_chanelling")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "my_doctor");
            startActivity(intent);
        } else if (objS.getType().equals("exp_chanelling")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        } else if (objS.getType().equals("contact_us")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, ContactInfoActivity.class);
            Ram.setMapSreenshot(null);
            startActivity(intent);
        } else if (objS.getType().equals("vid_doctor_view")) {

            Intent intent = new Intent(FilteredTimeline_Activity.this, MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("doc_chat")) {
            AyuboChatActivity.Companion.startActivity(FilteredTimeline_Activity.this, objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("doctor_chat")) {
            AyuboChatActivity.Companion.startActivity(FilteredTimeline_Activity.this, objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("phy_chanelling")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "phy_chanelling");
            startActivity(intent);
        } else if (objS.getType().equals("chat")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, AskQuestion_Activity.class);
            startActivity(intent);
        } else if (objS.getType().equals("medicine")) {

            Intent intent = new Intent(FilteredTimeline_Activity.this, FilteredTimeline_Activity.class);
            //  Intent intent = new Intent(getContext(), Medicine_ViewActivity.class);

            startActivity(intent);
        } else if (objS.getType().equals("challenge")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, NewCHallengeActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("reports")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, ReportDetailsActivity.class);
            intent.putExtra("data", "all");
            Ram.setReportsType("fromHome");
            startActivity(intent);
        } else if (objS.getType().equals("healthview")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, HealthViewActivityNew.class);
            startActivity(intent);
        } else if (objS.getType().equals("decoder")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, DecoderActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("store")) {
            openAppStore();
        } else if (objS.getType().equals("commonview")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, CommonWebViewActivity.class);
            intent.putExtra("URL", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("common")) {
            Intent intent = new Intent(FilteredTimeline_Activity.this, CommonWebViewActivity.class);
            intent.putExtra("URL", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("web")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(objS.getMeta()));
            startActivity(browserIntent);
        }
    }

    private int getQWidth(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int added = 0;
        if (density >= 4.0) {
            added = 250;
        } else if (density >= 3.0) {
            added = 200;
        } else if (density >= 2.0) {
            added = 150;
        } else if (density >= 1.5) {
            added = 130;
        } else if (density >= 1.0) {
            added = 100;
        }
        return added;
    }

    private int getImageHidth(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int added = 0;
        if (density >= 4.0) {
            added = 102;
        } else if (density >= 3.0) {
            added = 82;
        } else if (density >= 2.0) {
            added = 60;
        } else if (density >= 1.5) {
            added = 52;
        } else if (density >= 1.0) {
            added = 38;
        }
        return added;
    }

    private int getQHidth(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int added = 0;
        if (density >= 4.0) {
            added = 278;
        } else if (density >= 3.0) {
            added = 223;
        } else if (density >= 2.0) {
            added = 167;
        } else if (density >= 1.5) {
            added = 145;
        } else if (density >= 1.0) {
            added = 100;
        }
        return added;
    }

    String getLikesStringFromCount(Integer liked_count) {
        String likeCounts = null;
        if (liked_count > 1) {
            likeCounts = Integer.toString(liked_count) + " Likes";
        } else if (liked_count == 1) {
            likeCounts = Integer.toString(liked_count) + " Like";
        } else if (liked_count == 0) {
            likeCounts = Integer.toString(liked_count) + " Like";
        }

        return likeCounts;
    }

    String getTimeStringFromInteger(Integer timeInInteger) {

        long number = Long.valueOf(timeInInteger);
        number = number * 1000;
        // Date date=new Date(number);
        Calendar cal = Calendar.getInstance();
        Calendar todayCal = Calendar.getInstance();
        Date d = new Date(number);
        cal.setTime(d);

        int date = cal.get(Calendar.DATE);
        Date todayDate = new Date();

        todayCal.setTime(todayDate);
        int cameDate = cal.get(Calendar.DATE);
        int currDate = todayCal.get(Calendar.DATE);
        SimpleDateFormat formatter = null;
        if (cameDate == currDate) {
            formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
        } else {
            formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
        }
        String dateFromDB_forADay = formatter.format(cal.getTime());
        String[] parts = dateFromDB_forADay.split("X");
        String part1 = parts[0];

        return part1;
    }

    public void addAllToArray(ArrayList<Post> moveResults) {
        for (Post result : moveResults) {
            addOne(result);
        }
    }

    public void addOne(Post r) {
        timelinePostlist.add(r);
    }

    public void showAlert_Add(Context c, String title, String msg, final Post post) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);
        builder.setView(layoutView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);
        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                adapter.remove(post);

                Utility_Timeline.deleteAPost(appToken, post);
            }
        });
        dialog.show();
    }

    private void callOnPickAGoal(TopTile object) {

        if (object != null) {

            //  FirebaseAnalytics Adding
            Bundle bTileParams = new Bundle();
            bTileParams.putString("tile", object.getTitle());
            if (SplashScreen.firebaseAnalytics != null) {
                SplashScreen.firebaseAnalytics.logEvent("Timeline_tile_clicked", bTileParams);
            }

            String type = object.getType();

            if (type.equals("first")) {

                String status = pref.getMyGoalData().get("my_goal_status");

                if (status.equals("Pending")) {
                    Intent intent = new Intent(FilteredTimeline_Activity.this, AchivedGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Pick")) {
                    Intent intent = new Intent(FilteredTimeline_Activity.this, PickAGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Completed")) {
                    showAlert_Deleted(FilteredTimeline_Activity.this, "This goal has been achieved for the day. Please pick another goal tomorrow");
                }
            } else if (type.equals("googlefit")) {

                boolean bb = fitInstalled();
                if (bb) {
                    //  createGoogleClient();
                } else {
                    //   openGoogleFitInPlayStore();
                }


            } else if (type.equals("wellness_dashboard")) {
                Intent intent = new Intent(FilteredTimeline_Activity.this, DashBoard_Activity.class);
                startActivity(intent);
            } else if (type.equals("join_adv_challenge")) {
                Intent intent = new Intent(FilteredTimeline_Activity.this, MapJoinChallenge_Activity.class);
                System.out.println("=============challenge_id====================" + object.getMeta());
                intent.putExtra("challenge_id", object.getMeta());
                startActivity(intent);
            } else if (type.equals("view_adv_challenge")) {
                challenge_id = object.getMeta();
                System.out.println("===============challengeid==================" + challenge_id);
                Service_getChallengeMapData_ServiceCall();
            } else if (type.equals("view_spon_adv_challenge")) {
                challenge_id = object.getMeta();
                showpopup = "showpopup";
                Service_getChallengeMapData_ServiceCall();
            } else if (type.equals("post")) {

                Intent intent = new Intent(FilteredTimeline_Activity.this, OpenPostActivity.class);
                intent.putExtra("postID", object.getMeta());
                startActivity(intent);
            } else if (type.equals("doctor_chat")) {

                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(FilteredTimeline_Activity.this, docId, false, false, "");

            } else if (type.equals("doc_chat")) {

                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(FilteredTimeline_Activity.this, docId, false, false, "");

            } else if (type.equals("commonview")) {
                Intent intent = new Intent(FilteredTimeline_Activity.this, CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("common")) {
                Intent intent = new Intent(FilteredTimeline_Activity.this, CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("web")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getMeta()));
                startActivity(browserIntent);
            }

        }
    }


    private void Service_getChallengeMapData_ServiceCall() {
        MyProgressLoading.showLoading(FilteredTimeline_Activity.this, "Please wait...");

        if (Utility.isInternetAvailable(FilteredTimeline_Activity.this)) {
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
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
            System.out.println("========Service Calling============6");
            MyProgressLoading.dismissDialog();

            String cityFile = loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
            if (cityFile == null) {

                Service_getChallengeJson_ServiceCall();
            } else {

                MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(FilteredTimeline_Activity.this, MapChallengeActivity.class);
                intent.putExtra("challenge_id", challenge_id);
                intent.putExtra("white_lines", white_lines);
                intent.putExtra("noof_day", noof_day);
                intent.putExtra("cards", cards);
                intent.putExtra("showpopup", showpopup);
                intent.putExtra("service_checkpoints", service_checkpoints);
                intent.putExtra("enabled_checkpoints", enabled_checkpoints);
                intent.putExtra("Treatures", treaturesList);
                intent.putExtra("steps", Integer.toString(total_steps));
                intent.putExtra("weekSteps", weekSteps);
                intent.putExtra("tip_icon", tip_icon);
                intent.putExtra("tip", tip);
                intent.putExtra("tipheading", tipheading);

                intent.putExtra("tip_header_1", tip_header_1);
                intent.putExtra("tip_header_2", tip_header_2);
                intent.putExtra("tip_type", tip_type);
                intent.putExtra("tip_meta", tip_meta);


                System.out.println("=========from result Service_getStepsData=====================");
                startActivity(intent);
            }
        }
    }

    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";


        nameValuePair.add(new BasicNameValuePair("method", "getChallengeSteps"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


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

            System.out.println("========Service Calling============0");

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

            String res = jsonObj.optString("result").toString();

            int result = Integer.parseInt(res);

            if (result == 0) {

                try {

                    serviceDataStatus = "0";
                    if (myTreasure != null) {
                        myTreasure.clear();
                    }


                    String data = jsonObj.optString("data");
                    System.out.println("========Service Calling============" + data);
                    JSONObject jsonData = new JSONObject(data);
                    //Map Data====================================
                    service_checkpoints = jsonData.optString("service_checkpoints");
                    if (service_checkpoints.equals("true")) {
                        enabled_checkpoints = jsonData.optString("enabled_checkpoints");
                    } else {
                        service_checkpoints = "";
                        enabled_checkpoints = "";
                    }
                    tip_icon = jsonData.optString("tip_icon");
                    tip = jsonData.optString("tip");
                    tipheading = jsonData.optString("tipheading");

                    tip_header_1 = jsonData.optString("tip_header_1");
                    tip_header_2 = jsonData.optString("tip_header_2");
                    tip_type = jsonData.optString("tip_type");
                    tip_meta = jsonData.optString("tip_meta");


                    white_lines = jsonData.optString("white_lines");
                    weekSteps = jsonData.optString("weekSteps");
                    String counter = jsonData.optString("counter");
                    noof_day = jsonData.optString("day");
                    String treatures = jsonData.optString("treatures");
                    cards = jsonData.optString("cards");
                    total_steps = Integer.parseInt(counter);

                    JSONArray myDataListsAll = null;
                    try {
                        myDataListsAll = new JSONArray(treatures);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < myDataListsAll.length(); i++) {

                        JSONObject childJson = null;
                        try {
                            childJson = (JSONObject) myDataListsAll.get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String latp = childJson.optString("latp");
                        String longp = childJson.optString("longp");
                        String steps = childJson.optString("steps");
                        String objimg = childJson.optString("objimg");
                        String action = childJson.optString("action");
                        String meta = childJson.optString("meta");
                        String auto_hide = childJson.optString("auto_hide");

                        String status = childJson.optString("status");

                        double roadPath_lat = Double.parseDouble(latp);
                        double roadPath_longitude = Double.parseDouble(longp);

                        String bubble_text = null;
                        String bubble_link = null;
                        if (childJson.has("bubble_txt")) {
                            bubble_text = childJson.getString("bubble_txt");
                        } else {
                            bubble_text = "";
                        }
                        if (childJson.has("bubble_link")) {
                            bubble_link = childJson.getString("bubble_link");
                        } else {
                            bubble_link = "";
                        }


                        myTreasure.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, steps, objimg, action, meta, status, "", "", "",
                                "", "", "", "", "", "", bubble_text, bubble_link, auto_hide));

                    }
                    System.out.println(myTreasure.size());

                } catch (Exception e) {
                    serviceDataStatus = "99";
                    e.printStackTrace();
                }


            } else {
                System.out.println("========Service Calling============10");
                serviceDataStatus = "99";
            }


        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }


    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(challenge_id + ".json");
            int size = is.available();
            if (size > 0) {
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //=============================================
    private void Service_getChallengeJson_ServiceCall() {

        if (Utility.isInternetAvailable(FilteredTimeline_Activity.this)) {

            MyProgressLoading.showLoading(FilteredTimeline_Activity.this, "Please wait...");

            Service_getChallengeJson task = new Service_getChallengeJson();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_getChallengeJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            getChallengeJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            MyProgressLoading.dismissDialog();

            writeToFile(cityJsonString);
            MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

            Intent intent = new Intent(FilteredTimeline_Activity.this, MapChallengeActivity.class);

            intent.putExtra("white_lines", white_lines);
            intent.putExtra("challenge_id", challenge_id);
            intent.putExtra("noof_day", noof_day);
            intent.putExtra("cards", cards);
            intent.putExtra("showpopup", showpopup);
            intent.putExtra("service_checkpoints", service_checkpoints);
            intent.putExtra("enabled_checkpoints", enabled_checkpoints);
            intent.putExtra("Treatures", treaturesList);
            intent.putExtra("steps", Integer.toString(total_steps));
            intent.putExtra("weekSteps", weekSteps);

            intent.putExtra("tip_header_1", tip_header_1);
            intent.putExtra("tip_header_2", tip_header_2);
            intent.putExtra("tip_type", tip_type);
            intent.putExtra("tip_meta", tip_meta);


//From Exception Json ===============
            System.out.println("=========from result Service_getChallengeJson=====================");
            startActivity(intent);

        }
    }

    private void writeToFile(String result) {
        try {
            FileOutputStream stream = openFileOutput(challenge_id + ".json", FilteredTimeline_Activity.this.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            try {
                if (result != null) {
                    outputStreamWriter.write(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void getChallengeJson() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;


        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        //   challenge_id

        nameValuePair.add(new BasicNameValuePair("method", "join_adventure_challenge"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


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


            String sc = String.valueOf(response.getStatusLine().getStatusCode());


            if (sc.equals("200")) {

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

                String res = jsonObj.optString("result").toString();

                int result = Integer.parseInt(res);

                if (result == 0) {
                    cityJsonString = jsonObj.optString("json").toString();

                } else {

                    serviceDataStatus = "99";
                }


            }
        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    //=============================================
    private static final String PACKAGE_NAME = "com.google.android.apps.fitness";

    @CheckResult
    public boolean fitInstalled() {
        try {
            getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openAppStore() {
        Service_getMarketPlace_ServiceCall();
    }

    private void Service_getMarketPlace_ServiceCall() {
        if (Utility.isInternetAvailable(FilteredTimeline_Activity.this)) {
            try {
                Service_getMarketPlace task = new Service_getMarketPlace();
                task.execute(new String[]{ApiClient.BASE_URL_live});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private class Service_getMarketPlace extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyProgressLoading.showLoading(FilteredTimeline_Activity.this, "Please wait...");
        }

        @Override
        protected String doInBackground(String... urls) {
            getMarketPlaceToken();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            MyProgressLoading.dismissDialog();

            if (!(marketplace_Token_status == null)) {
                //  userIdFromServiceAPI
                if (marketplace_Token_status.equals("99")) {


                    return;
                }
                if (marketplace_Token_status.equals("11")) {

//                    textt.setText("No access");
//                    toastt.setView(layoutt);
//                    toastt.show();

                    return;
                }
                if (marketplace_Token_status.equals("0")) {

                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Store_clicked", null);
                    }

                    try {
                        String url = "https://store.ayubo.life/index.php/ldap/index/token?token=";
                        System.out.println("===========Market Place Token============" + marketplace_Token);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url + marketplace_Token));
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //  Toast.makeText(getApplicationContext(), "Login Failed !",
                    //        Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void getMarketPlaceToken() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        //Post Data
        marketplace_Token_status = "99";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;

        String jsonStr =
                "{" +

                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getEstoreToken"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        HttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        //making POST request.
        try {
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("========Service Calling============0");

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

            String res = jsonObj.optString("result").toString();

            if (res.equals("0")) {
                marketplace_Token = jsonObj.optString("token").toString();
                marketplace_Token_status = "0";
            } else {
                marketplace_Token_status = "99";
            }
        } catch (Exception e) {
            e.printStackTrace();

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

                Intent intent = new Intent(FilteredTimeline_Activity.this, ShareGoals_Activity.class);
                intent.putExtra("sponserURl", my_goal_sponser_large_image);
                intent.putExtra("shareImageURl", my_goal_share_large_image);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "achived");
                startActivity(intent);


//                Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sendIntent.setAction(Intent.ACTION_SEND);
//                String shareBody = "Sharing goal status";
//                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
//                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                sendIntent.setType("text/plain");
//                startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
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


}
