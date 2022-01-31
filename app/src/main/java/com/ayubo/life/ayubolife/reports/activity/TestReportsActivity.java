package com.ayubo.life.ayubolife.reports.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.reports.model.ChData;
import com.ayubo.life.ayubolife.reports.model.ChartDataMainResponse;
import com.ayubo.life.ayubolife.reports.model.ChartDatum;
import com.ayubo.life.ayubolife.reports.model.TableData;
import com.ayubo.life.ayubolife.reports.model.TableHeader;
import com.ayubo.life.ayubolife.reports.model.TableRow;
import com.ayubo.life.ayubolife.reports.model.Values;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestReportsActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    LinearLayout layout_no_report_data_view, layout_main_menu_horizontal_single, main_data_view, layout_main_menu_horizontal, layout_top_menu_horizontal, layout_main_menu_horizontal_names;
    LayoutInflater inflater;

    String chartUnit;
    String report_name;
    PrefManager pref;
    List<ChartDatum> chartDataList;
    Gson gson;
    LinearLayout btn_backImgBtn_layout;
    ImageButton btn_back_Button;
    String patient_id, enc_id, report_type;
    TextView txt_activity_heading;
    TextView txt_chart_unit_y;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_reports);

        uiConstructor();

        dataConstructor();

        //APi Calling ...
        getChartData();

    }


    void uiConstructor() {


        txt_chart_unit_y = findViewById(R.id.txt_chart_unit_y);
        layout_no_report_data_view = (LinearLayout) findViewById(R.id.layout_no_report_data_view);
        layout_no_report_data_view.setVisibility(View.INVISIBLE);
        layout_main_menu_horizontal_single = (LinearLayout) findViewById(R.id.layout_main_menu_horizontal_single);
        main_data_view = (LinearLayout) findViewById(R.id.main_data_view);
        layout_main_menu_horizontal = (LinearLayout) findViewById(R.id.layout_main_menu_horizontal);
        layout_top_menu_horizontal = (LinearLayout) findViewById(R.id.layout_top_menu_horizontal);
        layout_main_menu_horizontal_names = (LinearLayout) findViewById(R.id.layout_main_menu_horizontal_names);
        txt_activity_heading = findViewById(R.id.txt_activity_heading);
        layout_no_report_data_view.setVisibility(View.INVISIBLE);
        btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void dataConstructor() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        pref = new PrefManager(this);

        gson = new Gson();

        if (getIntent().getStringExtra("report_detail") == null) {
            report_name = getIntent().getStringExtra("report_name");
            patient_id = getIntent().getStringExtra("patient_id");
            enc_id = getIntent().getStringExtra("enc_id");
            report_type = getIntent().getStringExtra("report_type");
            txt_activity_heading.setText(report_name);
        } else {
            System.out.println(getIntent().getStringExtra("report_detail"));

            String reportDetail = getIntent().getStringExtra("report_detail");

            String reportId = reportDetail.split(":")[0];

            String patientId = "";
            String encId = "";

            try {
                if (reportDetail.split(":").length > 0)
                    patientId = reportDetail.split(":")[1];

                if (reportDetail.split(":").length > 1)
                    encId = reportDetail.split(":")[2];


                report_name = "";
                patient_id = patientId;
                enc_id = encId;
                report_type = "dataReport";
                txt_activity_heading.setText(report_name);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        mProgressDialog = new ProgressDialog(TestReportsActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

    }

    void loadViewFromCash() {

        DBString dataObj = DBRequest.getCashDataByType(TestReportsActivity.this, "reports_chart_data");
        if (dataObj != null) {
            String products_list = null;
            products_list = dataObj.getId();
            if ((products_list != null) && (products_list.length() > 5)) {
                Type type = new TypeToken<ChartDataMainResponse>() {
                }.getType();

                ChartDataMainResponse mainRes = (ChartDataMainResponse) gson.fromJson(products_list, type);

//                displayView(mainRes);

            }
        }
    }

    void displayView(ChartDataMainResponse mainRes) {

        ChData data = mainRes.getData();
        report_name = data.getTitle();
        txt_activity_heading.setText(report_name);


        TableData tableData = data.getTableData();

        if ((chartDataList != null) && (chartDataList.size() > 0)) {
            chartDataList.clear();
        }


        chartDataList = data.getChartData();

        if (chartDataList != null) {

            //Displaying Top Menu Buttons..............
            if (chartDataList.size() == 1) {
                //Dont't show top menu ......
            } else if (chartDataList.size() > 1) {
                setupTopMenu(chartDataList);
            }
            //End Displaying Top Menu Buttons..............

            //Displaying Graph...................
            if (chartDataList.size() > 0) {
                layout_no_report_data_view.setVisibility(View.GONE);
                // mChart.setVisibility(View.VISIBLE);
                txt_chart_unit_y.setVisibility(View.VISIBLE);
                if (chartDataList.get(0).getXAxis().size() > 1) {
                    showChart(chartDataList.get(0));
                }


            } else {
                layout_no_report_data_view.setVisibility(View.VISIBLE);
                //mChart.setVisibility(View.GONE);
                txt_chart_unit_y.setVisibility(View.GONE);
            }
            //End  Displaying Graph...................
        } else {
            layout_no_report_data_view.setVisibility(View.VISIBLE);
            //mChart.setVisibility(View.GONE);
            txt_chart_unit_y.setVisibility(View.GONE);
        }

        //Displaying Report Data Table...................
        if (tableData != null) {
            displayTestData(tableData);
        }
        //End Displaying Report Data Table...................
    }

    public void getChartData() {

        String appToken = pref.getUserToken();

        mProgressDialog.show();
        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<ChartDataMainResponse> call = apiService.getReportsChartData(AppConfig.APP_BRANDING_ID, appToken, patient_id, enc_id, report_type);

        call.enqueue(new Callback<ChartDataMainResponse>() {
            @Override
            public void onResponse(Call<ChartDataMainResponse> call, Response<ChartDataMainResponse> response) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }

                if (response.isSuccessful()) {


                    ChartDataMainResponse mainRes = response.body();

                    // Saving Cashed Data ...............
                    Gson gson1 = new Gson();
                    ChData data = mainRes.getData();
                    TableData tableData = data.getTableData();
                    if (tableData != null) {
                        saveCashed(gson1.toJson(response.body()));
                    }

                    // Loading View  ...............
                    displayView(mainRes);

                }
            }

            @Override
            public void onFailure(Call<ChartDataMainResponse> call, Throwable t) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }

                Toast.makeText(TestReportsActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }


    void saveCashed(String jsonCars) {
        DBRequest.updateDoctorData(TestReportsActivity.this, jsonCars, "reports_chart_data");
    }

    void setupTopMenu(final List<ChartDatum> chartDataList) {

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 0, 20, 0);
        layout_top_menu_horizontal.removeAllViews();


        for (int num = 0; num < chartDataList.size(); num++) {

            View myView = inflater.inflate(R.layout.report_top_menu_button, null);
            myView.setLayoutParams(buttonLayoutParams);

            ChartDatum obj = chartDataList.get(num);

            final Button img_button_bg = myView.findViewById(R.id.img_button_bg);

            if (num == 0) {
                img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_selected);
                img_button_bg.setTextColor(Color.parseColor("#ffffff"));

            } else {
                img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_unselected);
                img_button_bg.setTextColor(Color.parseColor("#ff860b"));

            }


            img_button_bg.setTag(obj);

            String name = obj.getParameterName();

            if (name.length() >= 10) {
                name = name.substring(0, Math.min(name.length(), 8));
                name = name + "...";
            }

            img_button_bg.setText(name);

            img_button_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_selected);
                    img_button_bg.setTextColor(Color.parseColor("#ffffff"));

                    ChartDatum obj = (ChartDatum) img_button_bg.getTag();

                    deselectTopMenuButtons(chartDataList, obj);


                    showChart(obj);

                }
            });

            layout_top_menu_horizontal.addView(myView);
        }


    }


    void deselectTopMenuButtons(final List<ChartDatum> chartDataList, ChartDatum objj) {

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 0, 20, 0);
        layout_top_menu_horizontal.removeAllViews();

        for (int num = 0; num < chartDataList.size(); num++) {

            View myView = inflater.inflate(R.layout.report_top_menu_button, null);
            myView.setLayoutParams(buttonLayoutParams);

            ChartDatum obj = chartDataList.get(num);

            final Button img_button_bg = myView.findViewById(R.id.img_button_bg);

            if (objj.getParameterName().equals(obj.getParameterName())) {
                img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_selected);
                img_button_bg.setTextColor(Color.parseColor("#ffffff"));
            } else {
                img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_unselected);
                img_button_bg.setTextColor(Color.parseColor("#ff860b"));
            }


            img_button_bg.setTag(obj);

            String name = obj.getParameterName();

            if (name.length() >= 10) {
                name = name.substring(0, Math.min(name.length(), 8));
                name = name + "...";
            }

            img_button_bg.setText(name);

            img_button_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((Button)v).setBackgroundResource(R.drawable.reports_button_gradient_selected);
                    //  ((Button)v).setTextColor(Color.parseColor("#000000"));

                    img_button_bg.setBackgroundResource(R.drawable.reports_button_gradient_selected);
                    img_button_bg.setTextColor(Color.parseColor("#ffffff"));

                    ChartDatum obj = (ChartDatum) img_button_bg.getTag();

                    deselectTopMenuButtons(chartDataList, obj);


                    showChart(obj);

                }
            });

            layout_top_menu_horizontal.addView(myView);

        }

    }


    void showChart(ChartDatum obj) {
        if ((chartDataList != null) && (chartDataList.size() > 0)) {

            for (int num = 0; num < chartDataList.size(); num++) {

                ChartDatum chartObject = chartDataList.get(num);
                if (obj.getParameterName().equals(chartObject.getParameterName())) {

                    List<String> xaxisValues = chartObject.getXAxis();
                    List<String> yaxisValues = chartObject.getYAxis();

                    if ((chartObject.getUom() != null && (chartObject.getUom().length() > 0))) {
                        txt_chart_unit_y.setText(chartObject.getUom());
                        chartUnit = chartObject.getUom().toString();
                    }
                    drawChart(xaxisValues.size(), xaxisValues, yaxisValues);

                }
            }
        }
    }


    boolean isLoadSingleReport(TableData chartDataLis) {

        List<TableRow> rawsList = chartDataLis.getTableRows();

        boolean isNotSignleReport = true;
        for (int num = 0; num < rawsList.size(); num++) {

            TableRow raw = rawsList.get(num);
            String reportName = raw.getParameterName();

            List<Values> valObjList = raw.getValues();

            if (valObjList.size() > 1) {
                isNotSignleReport = false;
            }
        }
        return isNotSignleReport;
    }

    void displayTestData(TableData chartDataList) {

        LinearLayout newRaw = new LinearLayout(this);

        newRaw.setOrientation(LinearLayout.VERTICAL);

        layout_main_menu_horizontal.removeAllViews();
        layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);

        List<TableHeader> headerList = chartDataList.getTableHeader();

        LinearLayout newLine5 = new LinearLayout(this);
        newLine5.setOrientation(LinearLayout.HORIZONTAL);


        //Displaying Report Data Table Date Headers...........
        if (headerList != null) {
            for (int num = 0; num < headerList.size(); num++) {

                View myView = inflater.inflate(R.layout.report_data_table_header_cell, null);

                TableHeader raw = headerList.get(num);

                TextView txt_report_owner = (TextView) myView.findViewById(R.id.txt_report_owner);
                TextView txt_days_month = (TextView) myView.findViewById(R.id.txt_days_month);
                TextView txt_day_number = (TextView) myView.findViewById(R.id.txt_day_name);
                TextView txt_days_year = (TextView) myView.findViewById(R.id.txt_days_year);

                ImageButton img_btn_download_file = (ImageButton) myView.findViewById(R.id.img_btn_download_file);
                img_btn_download_file.setTag(raw.getDownloadLink());

                if ((raw.getDownloadLink() != null) && (raw.getDownloadLink().length() > 5)) {
                    img_btn_download_file.setBackgroundResource(R.drawable.ic_download);

                } else {
                    img_btn_download_file.setBackgroundResource(R.drawable.ic_download_no);


                }

                img_btn_download_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String downLink = (String) v.getTag();
                        if (downLink.length() > 5) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downLink));
                            startActivity(browserIntent);
                        }

                    }
                });


                int date = 0;
                String sDate = raw.getDate();
                String getMonth = null;
                String getDate = null;
                String getYear = null;
                try {
                    Date date1 = new SimpleDateFormat("dd-MM-yy").parse(sDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    getYear = Integer.toString(year);
                    date = cal.get(Calendar.DATE);
                    //      public String getMonth(int month) {
                    getMonth = new DateFormatSymbols().getMonths()[month];
                    getMonth = getMonth.length() < 3 ? getMonth : getMonth.substring(0, 3);

                    getDate = Integer.toString(cal.get(Calendar.DATE));
                    //  }

                    int month2 = cal.get(Calendar.MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txt_report_owner.setText(raw.getDownloadText());

                txt_days_month.setText(getMonth);
                txt_day_number.setText(getDate);
                txt_days_year.setText(getYear);

                String suffix = getDayOfMonthSuffix(date);

                TextView txt_days = (TextView) myView.findViewById(R.id.txt_days);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    if (suffix.equals("th")) {
                        txt_days.setText(Html.fromHtml("<sup><small>th</small></sup>", Html.FROM_HTML_MODE_LEGACY));
                    }
                    if (suffix.equals("rd")) {
                        txt_days.setText(Html.fromHtml("<sup><small>rd</small></sup>", Html.FROM_HTML_MODE_LEGACY));
                    }
                    if (suffix.equals("st")) {
                        txt_days.setText(Html.fromHtml("<sup><small>st</small></sup>", Html.FROM_HTML_MODE_LEGACY));
                    }
                    if (suffix.equals("nd")) {
                        txt_days.setText(Html.fromHtml("<sup><small>nd</small></sup>", Html.FROM_HTML_MODE_LEGACY));
                    }

                } else {

                    if (suffix.equals("th")) {
                        txt_days.setText(Html.fromHtml("<sup><small>th</small></sup>"));
                    }
                    if (suffix.equals("rd")) {
                        txt_days.setText(Html.fromHtml("<sup><small>rd</small></sup>"));
                    }
                    if (suffix.equals("st")) {
                        txt_days.setText(Html.fromHtml("<sup><small>st</small></sup>"));
                    }
                    if (suffix.equals("nd")) {
                        txt_days.setText(Html.fromHtml("<sup><small>nd</small></sup>"));
                    }

                }
                newLine5.addView(myView);

            }
            newRaw.addView(newLine5);
        }
        //End Displaying Report Data Table Date Headers...........


        //Displaying Report Table Date ...........
        List<TableRow> rawsList = chartDataList.getTableRows();


        if (rawsList != null) {

            if (isLoadSingleReport(chartDataList)) {

                layout_top_menu_horizontal.setVisibility(View.GONE);
                layout_main_menu_horizontal_names.setVisibility(View.GONE);
                main_data_view.setVisibility(View.GONE);
                layout_main_menu_horizontal_single.setVisibility(View.VISIBLE);
                layout_main_menu_horizontal_single.setOrientation(LinearLayout.HORIZONTAL);

                for (int num = 0; num < rawsList.size(); num++) {

                    TableRow raw = rawsList.get(num);
                    String reportName = raw.getParameterName();

                    List<Values> valObjList = raw.getValues();

                    View myView = inflater.inflate(R.layout.health_report_data_table_singlecell, null);

                    Values ob = valObjList.get(0);


                    TextView txt_test_data_report_name = myView.findViewById(R.id.txt_test_data_report_name);
                    txt_test_data_report_name.setText(reportName);

                    TextView txt_test_data = myView.findViewById(R.id.txt_test_data);
                    txt_test_data.setText(String.valueOf(ob.getValue()));


                    View lay_test_data_level = myView.findViewById(R.id.lay_test_data_level);

                    if (ob.getRisk().equals("Good")) {
                        lay_test_data_level.setBackgroundColor(Color.parseColor("#FF10CC0D"));
                    } else if (ob.getRisk().equals("Higher")) {
                        lay_test_data_level.setBackgroundColor(Color.parseColor("#c94433"));
                    } else {
                        lay_test_data_level.setBackgroundColor(Color.parseColor("#fafafa"));
                    }

                    newRaw.addView(myView);
                }

                layout_main_menu_horizontal_single.addView(newRaw);
            } else {

                //Displaying Report Table Raw Names ...........
                displayTestDataNames(chartDataList);
                //Displaying Report Table Raw Names ...........

                layout_main_menu_horizontal_names.setVisibility(View.VISIBLE);
                main_data_view.setVisibility(View.VISIBLE);
                layout_main_menu_horizontal_single.setVisibility(View.GONE);

                for (int num = 0; num < rawsList.size(); num++) {

                    TableRow raw = rawsList.get(num);
                    List<Values> valObjList = raw.getValues();

                    LinearLayout newLine2 = new LinearLayout(this);
                    newLine2.setOrientation(LinearLayout.HORIZONTAL);
                    for (int i = 0; i < valObjList.size(); i++) {
                        Values ob = valObjList.get(i);
                        View myView = null;

                        myView = inflater.inflate(R.layout.health_report_data_table_cell, null);

                        TextView txt_test_data = (TextView) myView.findViewById(R.id.txt_test_data);
                        View lay_test_data_level = myView.findViewById(R.id.lay_test_data_level);

                        if (ob.getRisk().equals("Good")) {
                            lay_test_data_level.setBackgroundColor(Color.parseColor("#FF10CC0D"));
                        } else if (ob.getRisk().equals("Higher")) {
                            lay_test_data_level.setBackgroundColor(Color.parseColor("#c94433"));
                        } else {
                            lay_test_data_level.setBackgroundColor(Color.parseColor("#fafafa"));
                        }

                        String val = ob.getValue();
                        if (val == null) {
                            val = "0";
                        }
                        txt_test_data.setText(val);
                        newLine2.addView(myView);
                    }
                    View myViewFirstEnd = inflater.inflate(R.layout.report_data_cell_with_dateanddata_end, null);
                    newLine2.addView(myViewFirstEnd);
                    newRaw.addView(newLine2);

                }
                layout_main_menu_horizontal.addView(newRaw);

            }
            //Finishinh else


        }


    }

    public String getDayOfMonthSuffix(int n) {
        //  checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        String th = null;
        if (n >= 11 && n <= 13) {
            th = "th";
        } else {
            switch (n % 10) {
                case 1:
                    th = "st";
                    break;
                case 2:
                    th = "nd";
                    break;
                case 3:
                    th = "rd";
                    break;
                default:
                    th = "th";
                    break;
            }
        }
        return th;
    }

    void displayTestDataNames(TableData chartDataLis) {

        LinearLayout newRaw = new LinearLayout(this);
        newRaw.setOrientation(LinearLayout.VERTICAL);

        layout_main_menu_horizontal_names.removeAllViews();

        List<TableHeader> headerList = chartDataLis.getTableHeader();

        List<TableRow> rawsList = chartDataLis.getTableRows();

        if (rawsList != null) {

            for (int num = 0; num < rawsList.size(); num++) {

                TableRow raw = rawsList.get(num);
                List<Values> valObjList = raw.getValues();

                View myView = inflater.inflate(R.layout.report_data_cell_with_name_layout, null);
                TextView txt_report_name = (TextView) myView.findViewById(R.id.txt_report_name);
                TextView txt_report_unit = (TextView) myView.findViewById(R.id.txt_report_unit);


                String reportName = raw.getParameterName();
                txt_report_unit.setText(raw.getUom());

                txt_report_name.setText(reportName);

                LinearLayout newLine = new LinearLayout(this);

                newLine.setOrientation(LinearLayout.HORIZONTAL);

                newRaw.addView(myView);

            }

            // View myViewFirstEnd = inflater.inflate(R.layout.report_data_cell_with_dateanddata_end, null);
            layout_main_menu_horizontal_names.addView(newRaw);
        }


    }


    private HashMap<String, String> fillDataToDaysMap(List<String> myDates, List<String> mySteps) {

        HashMap<String, String> mapsnre = new HashMap<String, String>();

        for (int i = 0; i < myDates.size(); i++) {
            String serverDate = myDates.get(i);
            mapsnre.put(serverDate, mySteps.get(i));
        }

        return mapsnre;
    }

    void drawChart(int type, List<String> myDates, List<String> mySteps) {

        LineChart mChart = findViewById(R.id.chart1);
        ArrayList<String> xAxisLabels_ArrayList = null;
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
        mChart.setBackgroundColor(Color.rgb(255, 255, 255));


        mChart.animateX(2500);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        Legend l = mChart.getLegend();
        ArrayList<Entry> myStepsChartValueList = new ArrayList<>();
        ArrayList<Entry> averageStepsChartValueList = new ArrayList<>();
        int st = 0;
        xAxisLabels_ArrayList = new ArrayList<>();


        if ((myDates != null) && (myDates.size() > 0)) {
            HashMap<String, String> hmap1 = null;
            HashMap<String, String> hmap = null;
            List<Date> intialArrayDateList = null;

            //  hmap1 = makeEmptyDaysMapForGivenDays(type);

            hmap = fillDataToDaysMap(myDates, mySteps);
            Map<String, String> sortedTreeMap = new TreeMap<String, String>(hmap);
            Set set2 = sortedTreeMap.entrySet();
            Iterator it = set2.iterator();
            int ccccccc = 0;
            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();
                String strDate = (String) entry.getKey();
                String strSteps = (String) entry.getValue();
                double value = Double.parseDouble(strSteps);

                Integer yval = (int) value;

                myStepsChartValueList.add(new Entry(ccccccc, yval));
                String currentDateStr = getDateCaption(strDate);
                xAxisLabels_ArrayList.add(currentDateStr);

                if (st < yval) {
                    st = yval;
                }

                ccccccc++;
            }
        }

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.rgb(0, 0, 0));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(251, 137, 59));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setGridColor(Color.rgb(251, 137, 59));


//        try {
//
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//
//                    System.out.println("========v================" + value);
//
//                    return xAxisLabel.get((int) value);
//                }
//            });
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        mChart.getAxisLeft().setEnabled(false);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.rgb(251, 137, 59));
        rightAxis.setAxisMaximum(st);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularityEnabled(true);

        LineDataSet set1, set2;


        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(myStepsChartValueList);

            if (mChart.getLineData() != null) {
                mChart.getLineData().clearValues();
                mChart.notifyDataSetChanged();
            }
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(myStepsChartValueList, "Me");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.rgb(251, 137, 59));
            set1.setCircleColor(Color.rgb(251, 137, 59));
            set1.setLineWidth(2f);

            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            // set1.setFillColor(Color.rgb(251,137,59));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.rgb(251, 137, 59));
            }

            set1.setHighLightColor(Color.rgb(251, 137, 59));
            set1.setDrawCircleHole(false);
            LineData data = null;

            data = new LineData(set1);


            mChart.getLegend().setEnabled(false);
            // mChart.getDescription().setEnabled(false);
            data.setValueTextColor(Color.rgb(0, 0, 0));
            data.setValueTextSize(9f);

            if (mChart.getLineData() != null) {
                mChart.getLineData().clearValues();
                mChart.notifyDataSetChanged();
            }
            mChart.clear();

            mChart.setData(data);
            mChart.notifyDataSetChanged();


        }

        xAxis.setValueFormatter(new MyXAxisValueFormatter(xAxisLabels_ArrayList));


    }

    public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            String data = "";

            if (mValues != null) {

                if (mValues.size() > 1) {
                    if (mValues.size() > (int) value) {
                        data = mValues.get((int) value);
                    }
                }
            }
            return data;
        }

        /** this is only needed if numbers are returned, else return 0 */

    }

    public String getMonthNameByNumber(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    String getDateCaption(String strDate) {
        //  2017-12-19 00:00
        String[] output = strDate.split("-");
        String yesr = output[0];
        String month = output[1];
        String daywithseconds = output[2];
        String[] splited = daywithseconds.split("\\s+");
        String day = splited[0];

        String monthsText = getMonthNameByNumber(Integer.parseInt(month));
        String upToNCharacters = monthsText.substring(0, Math.min(monthsText.length(), 3));

        String fullDateString = upToNCharacters + " " + day;

        return fullDateString;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void view() {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TestReportsActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }


}
