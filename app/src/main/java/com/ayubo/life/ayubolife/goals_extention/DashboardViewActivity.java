package com.ayubo.life.ayubolife.goals_extention;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.ChartData;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.DashDetailsMainResponse;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Data;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Expert;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Goal;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Me;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Others;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.RecommendProgram;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flavors.changes.Constants;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DashboardViewActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    int INT_START = 0;
    int INT_END = 4;
    TextView txt_goal_name, txt_select_user_gruop, tv_favourite_goals, quick_insight_tv, tv_recomonded_programs, quick_insight_error2, quick_insight_error;
    CustomListAdapterLite adapter;
    LinearLayout dateView_chat_expert;
    LinearLayout dateView_chat_fitness;
    LinearLayout layout_dropdown, goalsMainLayout, goalsMainLayout2;
    LinearLayout dateView_programs;

    RadioButton btn_sevendays, btn_30days, btn_90days, btn_year;
    int intWidth;
    LayoutInflater inflater;
    ImageView btn_backImgBtn, img_btn_dropdown;
    String selectedDate;
    String currentStrDate, userid_ExistingUser;
    PrefManager pref;
    String appToken;

    SpannableStringBuilder str2;
    ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Goal> goalsTilesDataList;
    ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight> quickInsightDataList;
    ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.RecommendProgram> programsDataList;
    ChartData chartDataList;
    String catogoryId;
    String dateRange;
    LinearLayout lay_btnBack;
    ProgressDialog mProgressDialog;
    SimpleDateFormat formatWith_yyyy_MM_dd;
    boolean isOthersDataAvailable = false;
    boolean isMyDataAvailable = false;

    List<Date> dateList = null;
    ArrayList<String> xAxisLabel = null;
    String daysCount, profImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#fb8a3c";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }
        if (Constants.type == Constants.Type.LIFEPLUS) {
            String noticolr = "#5b5a5a";
            int whiteInta = Color.parseColor(noticolr);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(whiteInta);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.setSystemUiVisibility(0);
                //   decor.setSystemUiVisibility(0);
                // }
            }
        }
        setContentView(R.layout.activity_dashboard_view);


        daysCount = getIntent().getStringExtra("daysCount");
        // profImage = getIntent().getStringExtra("image");

        pref = new PrefManager(DashboardViewActivity.this);
        appToken = pref.getUserToken();

        mProgressDialog = new ProgressDialog(DashboardViewActivity.this);
        mProgressDialog.setMessage("Please wait...");

        formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");


        if (daysCount != null) {
            dateRange = daysCount;
        } else {
            dateRange = "1";
        }

        catogoryId = pref.getGoalCategory();

        initUI();

        initEvents();

        callWellnessDashboard(appToken, dateRange, catogoryId);

    }

    private HashMap<String, String> fillDataToDaysMap(HashMap<String, String> maps, List<String> myDates, List<String> mySteps) {
        HashMap<String, String> mapsnre = maps;

        for (int i = 0; i < myDates.size(); i++) {
            String serverDate = myDates.get(i);
            mapsnre.put(serverDate, mySteps.get(i));
        }

        return mapsnre;
    }

    private HashMap<String, String> makeEmptyDaysMapForGivenDays(int number) {
        formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, String> hmap = new HashMap<String, String>();
        Calendar todayCalendar = new GregorianCalendar();
        Date result = todayCalendar.getTime();

        Calendar startDay = Calendar.getInstance();
        Date currentDate = startDay.getTime();
        startDay.add(Calendar.DATE, -number);
        Date oldDate = startDay.getTime();
        selectedDate = formatWith_yyyy_MM_dd.format(currentDate.getTime());
        currentStrDate = selectedDate;
        hmap = Utility.makeDaysMapForGivenDays(oldDate, currentDate);
        return hmap;
    }

    public String getMonthNameByNumber(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    String getDateCaption(String strDate) {

        String[] output = strDate.split("-");
        String yesr = output[0];
        String month = output[1];
        String day = output[2];

        String monthsText = getMonthNameByNumber(Integer.parseInt(month));
        String upToNCharacters = monthsText.substring(0, Math.min(monthsText.length(), 3));

        String fullDateString = upToNCharacters + " " + day;

        return fullDateString;
    }


    void drawChart(int typeNumberOfdays, List<String> myDates, List<String> mySteps, List<String> otherDates, List<String> otherSteps) {

        LineChart mChart;
        mChart = findViewById(R.id.chart1);

        mChart.clear();
        mChart.setOnChartValueSelectedListener(this);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately

        if (Constants.type == Constants.Type.LIFEPLUS) {
            mChart.setBackgroundColor(Color.rgb(91, 90, 90));
        } else {
            mChart.setBackgroundColor(Color.rgb(255, 134, 11));
        }

        //  mChart.setBackgroundColor(Color.rgb(91,90,90));
        // setData(20, 30);
        mChart.animateX(2500);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        Legend l = mChart.getLegend();
        ArrayList<Entry> myStepsChartValueList = new ArrayList<>();
        ArrayList<Entry> averageStepsChartValueList = new ArrayList<>();
        int st = 0;
        xAxisLabel = new ArrayList<>();


        if ((myDates != null) && (myDates.size() > 0)) {

            isMyDataAvailable = true;

            HashMap<String, String> hmap1 = null;
            HashMap<String, String> hmap = null;
            List<Date> intialArrayDateList = null;

            hmap1 = makeEmptyDaysMapForGivenDays(typeNumberOfdays);
            hmap = fillDataToDaysMap(hmap1, myDates, mySteps);
            Map<String, String> sortedTreeMap = new TreeMap<String, String>(hmap);
            Set set2 = sortedTreeMap.entrySet();
            Iterator it = set2.iterator();
            int c = 0;
            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();
                String strDate = (String) entry.getKey();
                String strSteps = (String) entry.getValue();

                myStepsChartValueList.add(new Entry(c, Integer.parseInt(strSteps)));
                String currentDateStr = getDateCaption(strDate);
                xAxisLabel.add(currentDateStr);

                if (st < Integer.parseInt(strSteps)) {
                    st = Integer.parseInt(strSteps);
                }

                c++;
            }
        }

        if ((otherDates != null) && (otherDates.size() > 0)) {

            isOthersDataAvailable = true;
            HashMap<String, String> hmap1 = null;
            HashMap<String, String> hmap = null;
            List<Date> intialArrayDateList = null;

            hmap1 = makeEmptyDaysMapForGivenDays(typeNumberOfdays);
            hmap = fillDataToDaysMap(hmap1, otherDates, otherSteps);
            Map<String, String> sortedTreeMap = new TreeMap<String, String>(hmap);
            Set set2 = sortedTreeMap.entrySet();
            Iterator it = set2.iterator();
            int c = 0;
            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();
                String strDate = (String) entry.getKey();
                String strSteps = (String) entry.getValue();

                averageStepsChartValueList.add(new Entry(c, Integer.parseInt(strSteps)));

                if (myDates == null || myDates.size() == 0) {

                    String currentDateStr = getDateCaption(strDate);
                    xAxisLabel.add(currentDateStr);
                }

                if (st < Integer.parseInt(strSteps)) {
                    st = Integer.parseInt(strSteps);
                }

                c++;
            }
            System.out.println("======================");
        }

        //=================================================
        //============PENDINGS =====================================
        //=================================================
        //=================================================

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setGridColor(Color.WHITE);

//        try {
//
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//
//
//                    System.out.println("===========" + xAxisLabel.size()+"============"+value);
//                    String strDate=xAxisLabel.get((int) value);
//                    System.out.println("========================" + strDate);
//
//                    return strDate;
//                }
//            });
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        mChart.getAxisLeft().setEnabled(false);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMaximum(st);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularityEnabled(true);

        LineDataSet set1 = null;
        LineDataSet set2 = null;
        LineData data = null;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            if (isMyDataAvailable) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(myStepsChartValueList);
            }
            if (isOthersDataAvailable) {
                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
                set2.setValues(averageStepsChartValueList);
            }
            if (mChart.getLineData() != null) {
                mChart.getLineData().clearValues();
                mChart.notifyDataSetChanged();
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            }

        } else {
            // create a dataset and give it a type

            if (isMyDataAvailable) {
                set1 = new LineDataSet(myStepsChartValueList, "Me");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                if (Constants.type == Constants.Type.LIFEPLUS) {
                    set1.setColor(Color.GREEN);
                    set1.setCircleColor(Color.GREEN);
                } else {
                    set1.setColor(Color.YELLOW);
                    set1.setCircleColor(Color.YELLOW);
                }

                set1.setLineWidth(2f);

                if (dateRange.equals("1")) {
                    set1.setDrawCircles(true);
                } else {
                    set1.setDrawCircles(false);
                }
                set1.setDrawValues(false);

                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                if (Constants.type == Constants.Type.LIFEPLUS) {
                    set1.setHighLightColor(Color.GREEN);
                    set1.setFillColor(Color.GREEN);
                } else {
                    set1.setHighLightColor(Color.YELLOW);
                    set1.setFillColor(Color.YELLOW);
                }
                set1.setDrawCircleHole(false);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    set1.setFillDrawable(drawable);
                } else {
                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        set1.setFillColor(Color.GREEN);
                    } else {
                        set1.setFillColor(Color.YELLOW);
                    }

                }
            }

            if (isOthersDataAvailable) {
                // create a dataset and give it a type
                set2 = new LineDataSet(averageStepsChartValueList, pref.getGoalCategoryName());
                set2.setColor(Color.WHITE);
                set2.setCircleColor(Color.WHITE);
                set2.setLineWidth(2f);
                if (dateRange.equals("1")) {
                    set2.setDrawCircles(true);
                } else {
                    set2.setDrawCircles(false);
                }
                set2.setDrawValues(false);
                set2.setDrawFilled(true);
                set2.setFillAlpha(90);
                set2.setFillColor(Color.WHITE);
                set2.setDrawCircleHole(false);
                set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            }

            if (isOthersDataAvailable && isMyDataAvailable) {
                data = new LineData(set1, set2);
            } else if (isMyDataAvailable) {
                data = new LineData(set1);
            } else if (isOthersDataAvailable) {
                data = new LineData(set2);
            }

            mChart.getLegend().setEnabled(false);
            // mChart.getDescription().setEnabled(false);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            if (mChart.getLineData() != null) {
                mChart.getLineData().clearValues();
                mChart.notifyDataSetChanged();
            }
            mChart.clear();

            mChart.setData(data);
            mChart.notifyDataSetChanged();


        }
    }


    private void drawChartForDateRange(List<String> myDates, List<String> mySteps, List<String> otherDates, List<String> otherSteps) {

        if (dateRange.equals("1")) {
            //===FOR 7 days==========
            drawChart(6, myDates, mySteps, otherDates, otherSteps);
        } else if (dateRange.equals("2")) {
            //===FOR 30 days==========
            drawChart(30, myDates, mySteps, otherDates, otherSteps);
        } else if (dateRange.equals("3")) {
            //===FOR 90 days==========
            drawChart(90, myDates, mySteps, otherDates, otherSteps);
            //  drawChart(84,myDates,mySteps,otherDates,otherSteps);
        } else if (dateRange.equals("4")) {
            //===FOR Year days==========


            int numberOfDates = getDifferenceDays();

            drawChart(numberOfDates, myDates, mySteps, otherDates, otherSteps);
        }

    }

    public int getDifferenceDays() {

        Calendar startDay = Calendar.getInstance();
        Date currentDate = startDay.getTime();

        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        startDay.add(Calendar.DATE, -dayOfYear);
        Date oldDate = startDay.getTime();

        int daysdiff = 0;
        long diff = currentDate.getTime() - oldDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    public void callWellnessDashboard(String token, String date, String catogoryId) {

        ApiInterface apiService = ApiClient.getGoalExApiClient().create(ApiInterface.class);
        mProgressDialog.show();

        System.out.println("=============date==========================" + date);
        System.out.println("=============catogoryId==========================" + catogoryId);

        Call<DashDetailsMainResponse> call = apiService.getWellnessDashboardDetails(AppConfig.APP_BRANDING_ID, token, date, catogoryId);
        call.enqueue(new Callback<DashDetailsMainResponse>() {
            @Override
            public void onResponse(Call<DashDetailsMainResponse> call, Response<DashDetailsMainResponse> response) {
                mProgressDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getResult() == 0) {


                        Data mainDataObj = response.body().getData();
                        goalsTilesDataList = (ArrayList<Goal>) mainDataObj.getGoals();
                        quickInsightDataList = (ArrayList<QuickInsight>) mainDataObj.getQuickInsights();
                        programsDataList = (ArrayList<RecommendProgram>) mainDataObj.getRecommendPrograms();
                        chartDataList = mainDataObj.getChartData();

                        Me meData = chartDataList.getMe();
                        Others otherData = chartDataList.getOthers();
                        List<String> MyDatesList = meData.getDates();
                        List<String> MyStepsList = meData.getSteps();

                        List<String> OtherDatesList = otherData.getDates();
                        List<String> OtherStepsList = otherData.getSteps();

                        if (MyDatesList.size() > 0) {
                            drawChartForDateRange(MyDatesList, MyStepsList, OtherDatesList, OtherStepsList);
                        }


                        if (goalsTilesDataList != null) {
                            if (goalsTilesDataList.size() > 0) {
                                tv_favourite_goals.setVisibility(View.VISIBLE);
                                initGoalsTiles();
                            } else {
                                tv_favourite_goals.setVisibility(View.GONE);
                            }
                        }

                        if (programsDataList != null) {
                            if (programsDataList.size() > 0) {
                                tv_recomonded_programs.setVisibility(View.VISIBLE);
                                initRecmPrograms();
                            } else {
                                tv_recomonded_programs.setVisibility(View.GONE);
                            }
                        } else {
                            tv_recomonded_programs.setVisibility(View.GONE);
                        }

                        if (quickInsightDataList != null) {
                            if (quickInsightDataList.size() > 0) {
                                quick_insight_tv.setVisibility(View.VISIBLE);
                                quick_insight_error.setVisibility(View.GONE);
                                quick_insight_error2.setVisibility(View.GONE);
                                setupInsightView();
                            } else {
                                quick_insight_error.setVisibility(View.VISIBLE);
                                quick_insight_error2.setVisibility(View.VISIBLE);
                                quick_insight_tv.setVisibility(View.GONE);
                                quick_insight_error.setText(R.string.error_msg_noinnsight);
                                quick_insight_error2.setText(R.string.error_msg_noinnsight2);
                            }
                        } else {
                            quick_insight_error.setVisibility(View.VISIBLE);
                            quick_insight_error2.setVisibility(View.VISIBLE);
                            quick_insight_tv.setVisibility(View.GONE);
                            quick_insight_error.setText(R.string.error_msg_noinnsight);
                            quick_insight_error2.setText(R.string.error_msg_noinnsight2);
                        }


                    } else {
                        Toast.makeText(DashboardViewActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                        System.out.println("==============Fail============");
                    }


                } else {
                    Toast.makeText(DashboardViewActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                    System.out.println("===========response===Fail============");
                }
            }

            @Override
            public void onFailure(Call<DashDetailsMainResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(DashboardViewActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }

    void updateInsight_ListView(ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight> datt) {

        adapter = new CustomListAdapterLite(DashboardViewActivity.this, datt);
        //  list_inside.setItemsCanFocus(true);

        // list_inside.setAdapter(adapter);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    class CustomListAdapterLite extends BaseAdapter {
        private Context activity;
        private ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight> dataNew;
        private LayoutInflater inflater = null;

        private class ViewHolder {
            LinearLayout dateView_chat_expert;
            TextView txt_quick_insight_desc;
            TextView txt_quick_insight_have_chat;
        }

        public CustomListAdapterLite(Context a, ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight> d) {
            activity = a;
            dataNew = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return dataNew.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.QuickInsight obj = dataNew.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.quick_inisght_cell_raw, parent, false);
                holder.txt_quick_insight_desc = (TextView) convertView.findViewById(R.id.txt_quick_insight_desc);
                holder.txt_quick_insight_have_chat = (TextView) convertView.findViewById(R.id.txt_quick_insight_have_chat);
                holder.dateView_chat_expert = (LinearLayout) convertView.findViewById(R.id.dateView_chat_expert);

                convertView.setTag(holder);
                System.out.println("=========getText=====================" + obj.getText());
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.dateView_chat_expert.removeAllViews();

            ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Expert> expertDataList = (ArrayList<Expert>) obj.getExperts();
            for (int num = 0; num < expertDataList.size(); num++) {

                View myView = inflater.inflate(R.layout.goal_dashboard_chatview, null);
                final TextView txt_expert_name = (TextView) myView.findViewById(R.id.txt_expert_name);
                final ImageView txt_expert_image = (ImageView) myView.findViewById(R.id.txt_expert_image);

                txt_expert_name.setText(expertDataList.get(num).getFullName());
                txt_expert_name.setTag(expertDataList.get(num).getId());
                txt_expert_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashboardViewActivity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL", txt_expert_name.getTag().toString());
                        startActivity(intent);
                    }
                });
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(activity));
                Glide.with(DashboardViewActivity.this).load(expertDataList.get(num).getProfilePictureLink())
                        .transition(withCrossFade())
                        .thumbnail(0.5f)
                        .apply(requestOptions)
                        .into(txt_expert_image);

                txt_expert_image.setTag(expertDataList.get(num).getId());
                txt_expert_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashboardViewActivity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL", txt_expert_image.getTag().toString());
                        startActivity(intent);
                    }
                });


                holder.dateView_chat_expert.addView(myView);


            }

            SpannableStringBuilder str = new SpannableStringBuilder(obj.getText());
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan((new RelativeSizeSpan(1.5f)), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txt_quick_insight_desc.setText(str);
            holder.txt_quick_insight_have_chat.setText(obj.getTitle());

            return convertView;
        }
    }

    void initUI() {
        btn_backImgBtn = (ImageView) findViewById(R.id.btn_backImgBtn);
        lay_btnBack = (LinearLayout) findViewById(R.id.lay_btnBack);

        txt_goal_name = findViewById(R.id.txt_goal_name);
        quick_insight_tv = findViewById(R.id.quick_insight_tv);
        quick_insight_error = findViewById(R.id.quick_insight_error);
        quick_insight_error2 = findViewById(R.id.quick_insight_error2);

        tv_recomonded_programs = findViewById(R.id.tv_recomonded_programs);
        tv_favourite_goals = (TextView) findViewById(R.id.tv_favourite_goals);
        txt_select_user_gruop = (TextView) findViewById(R.id.txt_select_user_gruop);
        img_btn_dropdown = findViewById(R.id.img_btn_dropdown);


        layout_dropdown = (LinearLayout) findViewById(R.id.layout_dropdown);


        btn_sevendays = findViewById(R.id.btn_sevendays);
        btn_30days = findViewById(R.id.btn_30days);
        btn_90days = findViewById(R.id.btn_90days);
        btn_year = findViewById(R.id.btn_year);

        goalsMainLayout2 = (LinearLayout) findViewById(R.id.dateView2);
        goalsMainLayout = (LinearLayout) findViewById(R.id.dateView);
        dateView_chat_expert = (LinearLayout) findViewById(R.id.dateView_chat_expert);
        dateView_programs = (LinearLayout) findViewById(R.id.dateView_programs);

        //list_inside = (ListView) findViewById(R.id.list_inside);
        // img_me_image= (ImageView) findViewById(R.id.img_me_image);


        txt_select_user_gruop.setText(pref.getGoalCategoryName());

        tv_favourite_goals.setVisibility(View.GONE);

        String sYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        btn_year.setText(sYear);

        if (dateRange.equals("1")) {
            btn_sevendays.setBackgroundResource(R.drawable.lefttside_rounded_button_bg_orange);
            btn_30days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_90days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_year.setBackgroundResource(R.drawable.rightside_rounded_button_bg_grey);

            btn_sevendays.setTextColor(Color.parseColor("#ff860b"));
            btn_30days.setTextColor(Color.parseColor("#ffffff"));
            btn_90days.setTextColor(Color.parseColor("#ffffff"));
            btn_year.setTextColor(Color.parseColor("#ffffff"));
        } else if (dateRange.equals("2")) {
            btn_sevendays.setBackgroundResource(R.drawable.leftside_rounded_button_bg_grey);
            btn_30days.setBackgroundResource(R.drawable.mid_button_bg_orange);
            btn_90days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_year.setBackgroundResource(R.drawable.rightside_rounded_button_bg_grey);

            btn_sevendays.setTextColor(Color.parseColor("#ffffff"));
            btn_30days.setTextColor(Color.parseColor("#ff860b"));
            btn_90days.setTextColor(Color.parseColor("#ffffff"));
            btn_year.setTextColor(Color.parseColor("#ffffff"));
        } else if (dateRange.equals("3")) {
            btn_sevendays.setBackgroundResource(R.drawable.leftside_rounded_button_bg_grey);
            btn_30days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_90days.setBackgroundResource(R.drawable.mid_button_bg_orange);
            btn_year.setBackgroundResource(R.drawable.rightside_rounded_button_bg_grey);

            btn_sevendays.setTextColor(Color.parseColor("#ffffff"));
            btn_30days.setTextColor(Color.parseColor("#ffffff"));
            btn_90days.setTextColor(Color.parseColor("#ff860b"));
            btn_year.setTextColor(Color.parseColor("#ffffff"));
        } else if (dateRange.equals("4")) {
            btn_sevendays.setBackgroundResource(R.drawable.leftside_rounded_button_bg_grey);
            btn_30days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_90days.setBackgroundResource(R.drawable.mid_button_bg_black);
            btn_year.setBackgroundResource(R.drawable.rightside_rounded_button_bg_orange);

            btn_sevendays.setTextColor(Color.parseColor("#ffffff"));
            btn_30days.setTextColor(Color.parseColor("#ffffff"));
            btn_90days.setTextColor(Color.parseColor("#ffffff"));
            btn_year.setTextColor(Color.parseColor("#ff860b"));
        }


    }

    void nextToDetailsActivity(String number) {
        Intent i = new Intent(DashboardViewActivity.this, DashboardViewActivity.class);
        i.putExtra("daysCount", number);
        i.putExtra("image", "");
        i.putExtra("catogoryId", catogoryId);
        startActivity(i);
        finish();
    }

    void initEvents() {
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

        txt_select_user_gruop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardViewActivity.this, SelecteAverageType_Activity.class);
                i.putExtra("from", "details_view");
                startActivity(i);
                finish();
            }
        });
        layout_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardViewActivity.this, SelecteAverageType_Activity.class);
                i.putExtra("from", "details_view");
                startActivity(i);
                finish();
            }
        });
        img_btn_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardViewActivity.this, SelecteAverageType_Activity.class);
                i.putExtra("from", "details_view");
                startActivity(i);
                finish();
            }
        });


        btn_sevendays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextToDetailsActivity("1");
            }
        });
        btn_30days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txt_sevendays_new_layout.setBackgroundResource(R.drawable.svdays);
//                lay_30days_new_layout.setBackgroundResource(R.drawable.sv30days_active);
//                txt_90days_new_layout.setBackgroundResource(R.drawable.sv90days);
//                txt_18days_new_layout.setBackgroundResource(R.drawable.sv2018);

                nextToDetailsActivity("2");

            }
        });


        btn_90days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txt_sevendays_new_layout.setBackgroundResource(R.drawable.svdays);
//                lay_30days_new_layout.setBackgroundResource(R.drawable.sv30days);
//                txt_90days_new_layout.setBackgroundResource(R.drawable.sv90days_active);
//                txt_18days_new_layout.setBackgroundResource(R.drawable.sv2018);

                nextToDetailsActivity("3");
            }
        });
        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                txt_sevendays_new_layout.setBackgroundResource(R.drawable.svdays);
//                lay_30days_new_layout.setBackgroundResource(R.drawable.sv30days);
//                txt_90days_new_layout.setBackgroundResource(R.drawable.sv90days);
//                txt_18days_new_layout.setBackgroundResource(R.drawable.sv2018_active);

                nextToDetailsActivity("4");
            }
        });

        goalsTilesDataList = new ArrayList<>();

        //  profImage = pref.getLoginUser().get("image_path");
        //profImage = "https://livehappy.ayubo.life/" + profImage;


//        Glide.with(getApplicationContext()).load(profImage)
//                .bitmapTransform(new CircleTransform(DashboardViewActivity.this))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(img_me_image);
//

    }


    private void initGoalsTiles() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        DeviceScreenDimension DeviceScreenDimension = new DeviceScreenDimension(getBaseContext());

        intWidth = DeviceScreenDimension.getDisplayWidth();
        int aWidth = intWidth / 2;
        int aHiegt = (intWidth / 32) * 11;

        goalsMainLayout.removeAllViews();

        for (int num = 0; num < goalsTilesDataList.size(); num++) {

            inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.goal_dashboard_girdview, null);

            myView.setLayoutParams(new LinearLayout.LayoutParams(aWidth, aHiegt));
            myView.setPadding(0, 0, 25, 0);
            TextView txt_goalname = (TextView) myView.findViewById(R.id.txt_goalname);
            TextView txt_goal_achive_level = (TextView) myView.findViewById(R.id.txt_goal_achive_level);
            TextView txt_goal_achive_pesentage = (TextView) myView.findViewById(R.id.txt_goal_achive_pesentage);


            ImageView img_tilebackgruond = (ImageView) myView.findViewById(R.id.img_tilebackgruond);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_tilebackgruond.setClipToOutline(true);
            }

            String goalName = goalsTilesDataList.get(num).getGoalName();
            String goalPesentage = goalsTilesDataList.get(num).getSuccessAmountPrecentage();
            String goalSuccessAmout = goalsTilesDataList.get(num).getSuccessAmount();
            String goalImage = goalsTilesDataList.get(num).getGoalImageLink();

            if (goalImage.contains("zoom_level")) {
                goalImage = goalImage.replace("zoom_level", "xxxhdpi");
            }

            txt_goalname.setText(goalName);
            txt_goal_achive_pesentage.setText(goalPesentage);
            txt_goal_achive_level.setText(goalSuccessAmout);

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(DashboardViewActivity.this).load(goalImage)
                    .transition(withCrossFade())
                    .apply(requestOptions)
                    .into(img_tilebackgruond);

            goalsMainLayout.addView(myView);

        }


    }

    void getFormatedText(String text) {

        //  String test="Here is ur 69% string";
        Pattern p = Pattern.compile("(\\d+%)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            String sss = m.group(1);
            int asdasd = text.indexOf(sss);
            System.out.println("Hello " + m.find());
        }
    }

    private void setupInsightView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        int aWidth = intWidth / 2;
        int aHiegt = (intWidth / 32) * 11;

        goalsMainLayout2.removeAllViews();
        // quickInsightDataList.size(
        for (int num = 0; num < quickInsightDataList.size(); num++) {

            inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.quick_inisght_cell_raw, null);

            //  myView.setLayoutParams(new LinearLayout.LayoutParams(aWidth, aHiegt));
            //  myView.setPadding(0,0,25,0);
            TextView txt_quick_insight_desc = (TextView) myView.findViewById(R.id.txt_quick_insight_desc);
            TextView txt_quick_insight_have_chat = (TextView) myView.findViewById(R.id.txt_quick_insight_have_chat);
            LinearLayout dateView_chat_expert = (LinearLayout) myView.findViewById(R.id.dateView_chat_expert);

            String textString = quickInsightDataList.get(num).getText();

            Pattern p = Pattern.compile("(\\d+%)");
            Matcher m = p.matcher(textString);
            if (m.find()) {
                String sss = m.group(1);
                INT_START = textString.indexOf(sss);
                if (sss.length() == 2) {
                    INT_END = INT_START + 2;
                }
                if (sss.length() == 3) {
                    INT_END = INT_START + 3;
                }
                if (sss.length() == 4) {
                    INT_END = INT_START + 4;
                }


            }

            SpannableStringBuilder str = new SpannableStringBuilder(textString);

            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            str.setSpan((new RelativeSizeSpan(1.5f)), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            txt_quick_insight_desc.setText(str);
            txt_quick_insight_have_chat.setText(quickInsightDataList.get(num).getTitle());


            ArrayList<com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.Expert> expertDataList = (ArrayList<Expert>) quickInsightDataList.get(num).getExperts();
            for (int n = 0; n < expertDataList.size(); n++) {

                View myView2 = inflater.inflate(R.layout.goal_dashboard_chatview, null);
                final TextView txt_expert_name = (TextView) myView2.findViewById(R.id.txt_expert_name);
                final ImageView txt_expert_image = (ImageView) myView2.findViewById(R.id.txt_expert_image);
                //  final ImageView txt_expert_image = (ImageView) myView2.findViewById(R.id.txt_expert_image);

                txt_expert_name.setText(expertDataList.get(n).getFullName());
                txt_expert_name.setTag(expertDataList.get(n).getRedirectLink());
                txt_expert_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashboardViewActivity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL", txt_expert_name.getTag().toString());
                        startActivity(intent);
                    }
                });
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(DashboardViewActivity.this));
                Glide.with(DashboardViewActivity.this).load(expertDataList.get(n).getProfilePictureLink())
                        .transition(withCrossFade())
                        .apply(requestOptions)
                        .into(txt_expert_image);

                txt_expert_image.setTag(expertDataList.get(n).getRedirectLink());
                txt_expert_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashboardViewActivity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL", txt_expert_image.getTag().toString());
                        startActivity(intent);
                    }
                });


                dateView_chat_expert.addView(myView2);


            }

            goalsMainLayout2.addView(myView);

        }


    }


    private void initRecmPrograms() {
        dateView_programs.removeAllViews();

        for (int num = 0; num < programsDataList.size(); num++) {

            inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.goal_dashboard_bottom_grid, null);
            TextView txt_goalname = (TextView) myView.findViewById(R.id.txt_goalname);
            final ImageView img_program_image = (ImageView) myView.findViewById(R.id.img_program_image);

            img_program_image.setTag(programsDataList.get(num).getRedirectLink());
            img_program_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardViewActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", img_program_image.getTag().toString());
                    startActivity(intent);
                }
            });

            txt_goalname.setText(programsDataList.get(num).getTitle());

            String imgUrl = programsDataList.get(num).getImageLink();

            if (imgUrl.contains("zoom_level")) {
                imgUrl = imgUrl.replace("zoom_level", "xxxhdpi");
            }

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(DashboardViewActivity.this).load(imgUrl)
                    .transition(withCrossFade())
                    .apply(requestOptions)
                    .into(img_program_image);
            dateView_programs.addView(myView);
        }
    }


}
