package com.ayubo.life.ayubolife.programs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals_extention.Utility;
import com.ayubo.life.ayubolife.goals_extention.models.DashboardDate;
import com.ayubo.life.ayubolife.model.DB4String;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.EngagementType;
import com.ayubo.life.ayubolife.programs.data.model.Entitlement;
import com.ayubo.life.ayubolife.programs.data.model.Goal;
import com.ayubo.life.ayubolife.programs.data.model.MedicalChart;
import com.ayubo.life.ayubolife.programs.data.model.TextChart;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.utility.Ram;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class DashBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_DASHBOARD = 10;
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_CHART = 2;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_GOAL = 3;
    private static final int VIEW_TYPE_ENTITLEMENT = 4;
    private static final int VIEW_TYPE_HEALTH_STATUS = 5;
    private static final int VIEW_TYPE_ENGADGEMENT = 6;
    private Context context;

    private int cellType;
    private String selectedUuserId;
    private List<Object> reportsList;
    int intWidth = 0;
    SimpleDateFormat formatWith_yyyy_MM_dd;
    private String firstReportID;
    private String errorMsg;
    private List<AllRecordsMainResponse.AllRecordsMember> memberList;
    PrefManager pref;
    String selectedDate;
    int width = 0;
    int height = 0;
    int densityDpi = 0;
    int fullWidthScroll;
    String lifePoints;

    public DashBoardAdapter(Context context, List<Object> reportsListt, int w, int densityDp, String lifepon, int h) {
        this.context = context;
        reportsList = new ArrayList<>();
        reportsList = reportsListt;
        width = w;
        densityDpi = densityDp;
        lifePoints = lifepon;
        pref = new PrefManager(context);
        height = h;
    }

    private OnClickEntitilementButtonInterface lickEntitilementButtonInterfaceListner;
    private OnClickBackInterface backClickListner;
    private OnClickHistoryInterface viewHistoryListner;
    private OnClick7DaysGraphInterface OnClick7DaysGraphListner;
    private OnClick30DaysGraphInterface OnClick30DaysGraphListner;
    private OnClick90DaysGraphInterface OnClick90DaysGraphListner;
    private OnClick365DaysGraphInterface OnClick365DaysGraphListner;
    private OnClickReloadStepsInterface OnClickReloadStepsListner;
    private OnClickSelecteAverageTypeInterface OnClickSelecteAverageTypeListner;

    public void OnClickEntitilementButton(OnClickEntitilementButtonInterface listener) {
        this.lickEntitilementButtonInterfaceListner = listener;
    }

    public void OnClickBack(OnClickBackInterface listener) {
        this.backClickListner = listener;
    }

    public void OnClickViewHistory(OnClickHistoryInterface listener) {
        this.viewHistoryListner = listener;
    }

    public void OnClick7DaysViewGraph(OnClick7DaysGraphInterface listener) {
        this.OnClick7DaysGraphListner = listener;
    }

    public void OnClick30DaysViewGraph(OnClick30DaysGraphInterface listener) {
        this.OnClick30DaysGraphListner = listener;
    }

    public void OnClick90DaysViewGraph(OnClick90DaysGraphInterface listener) {
        this.OnClick90DaysGraphListner = listener;
    }

    public void OnClick365DaysViewGraph(OnClick365DaysGraphInterface listener) {
        this.OnClick365DaysGraphListner = listener;
    }

    public void OnClickViewReloadSteps(OnClickReloadStepsInterface listener) {
        this.OnClickReloadStepsListner = listener;
    }

    public void OnClickSelectAverageType(OnClickSelecteAverageTypeInterface listener) {
        this.OnClickSelecteAverageTypeListner = listener;
    }

    public interface OnClickEntitilementButtonInterface {
        void onEntitilementButtonClick(String action, String meta);
    }

    public interface OnClickBackInterface {
        void onBackToPrevious();
    }

    public interface OnClickHistoryInterface {
        void onViewHistory();
    }

    public interface OnClick7DaysGraphInterface {
        void onView7DayGraph();
    }

    public interface OnClick30DaysGraphInterface {
        void onView30DayGraph();
    }

    public interface OnClick90DaysGraphInterface {
        void onView90DayGraph();
    }

    public interface OnClick365DaysGraphInterface {
        void onView365DayGraph();
    }

    public interface OnClickReloadStepsInterface {
        void onViewReloadSteps(String sDate);
    }

    public interface OnClickSelecteAverageTypeInterface {
        void onSelecteAverageType(String sDate);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case VIEW_TYPE_DASHBOARD:
                View viewItemDashboard = inflater.inflate(R.layout.program_dashboard_main_cell, parent, false);
                viewHolder = new Dashboard_ViewHolder(viewItemDashboard);
                break;
            case VIEW_TYPE_CHART:
                View viewItem = inflater.inflate(R.layout.program_dashboard_chart_cell, parent, false);
                viewHolder = new ChartTypeOne_ViewHolder(viewItem);
                break;
            case VIEW_TYPE_TEXT:
                View viewLoading = inflater.inflate(R.layout.program_dashboard_text_cell, parent, false);
                viewHolder = new TextChartViewHolder(viewLoading);
                break;
            case VIEW_TYPE_GOAL:
                View viewGoal = inflater.inflate(R.layout.program_dashboard_goal_cell, parent, false);
                viewHolder = new GoalViewHolder(viewGoal);
                break;
            case VIEW_TYPE_ENGADGEMENT:
                View viewEndgemnt = inflater.inflate(R.layout.program_dashboard_endgement_cell, parent, false);
                viewHolder = new EngadgementViewHolder(viewEndgemnt);
                break;
            case VIEW_TYPE_ENTITLEMENT:
                View viewEntitlement = inflater.inflate(R.layout.program_dashboard_goal_entitlement, parent, false);
                viewHolder = new EntitlementViewHolder(viewEntitlement);
                break;
            case VIEW_TYPE_HEADER:
                View viewHeader = inflater.inflate(R.layout.program_dashboard_goal_header, parent, false);
                viewHolder = new HeaderViewHolder(viewHeader);
                break;
            case VIEW_TYPE_HEALTH_STATUS:
                View viewHealthStatus = inflater.inflate(R.layout.program_dashboard_healthstatus_cell, parent, false);
                viewHolder = new HealthStatusViewHolder(viewHealthStatus);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fromholde, final int position) {
        LayoutInflater inflater = null;


        if (fromholde instanceof ChartTypeOne_ViewHolder) {

            ChartTypeOne_ViewHolder viewHolder = (ChartTypeOne_ViewHolder) fromholde;
            MedicalChart obj = (MedicalChart) reportsList.get(position);

            int bitmapHeight = (250 * height) / 1360;

            Bitmap bitmap2 = Bitmap.createBitmap(width, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap2);
            viewHolder.drawingChartImageView.setImageBitmap(bitmap2);

            viewHolder.txt_report_name.setText(obj.getParameterName());

            double unitSize = width / obj.getBarMax();
            String startConerColor = "";
            String endConerColor = "";
            Double sectionOneGap = unitSize * (obj.getOptimalMax() - obj.getOptimalMin());
            Double sectionOneStartPoint = unitSize * obj.getOptimalMin();


            Float target = (float) unitSize * (float) obj.getTarget();

            if (sectionOneStartPoint > 20) {
                startConerColor = "red";
                // sectionOneStartPoint=10;
            } else {
                startConerColor = "green";
                sectionOneStartPoint = 10.0;
            }


            Double sectionTwoGap = unitSize * (obj.getBorderlineMax() - obj.getBorderlineMin());
            Double sectionTwoStart = unitSize * obj.getBorderlineMin();
            Double sectionTwoEnd = unitSize * obj.getBorderlineMax();
            Double secondSection = sectionTwoStart + sectionTwoGap;

            if ((width - sectionTwoEnd) < 10) {
                endConerColor = "orange";
                secondSection = secondSection - 10;

            } else {
                endConerColor = "red";
            }

            if (sectionTwoGap == 0) {
                if (obj.getOptimalMax().equals(obj.getBarMax())) {
                    endConerColor = "green";
                    sectionOneGap = sectionOneGap - 20;
                }
            }


            BarChart drawChart = new BarChart(context);

            if (obj.getYou().equals("")) {

            } else {
                Float youVal = (float) unitSize * Float.parseFloat(obj.getYou());

                drawChart.drawYourValue(context, Double.valueOf(obj.getYou()).toString(), canvas2, youVal, densityDpi);
            }


            drawChart.drawMainBarInRed(canvas2, width, densityDpi);

            // Green Color chart
            drawChart.initPaintSectionOne(sectionOneStartPoint, (sectionOneGap + sectionOneStartPoint), canvas2, densityDpi);

            // Orange Color chart
            drawChart.initPaintSectionTwo(sectionTwoStart, secondSection, canvas2, densityDpi);

            drawChart.initPaintStartConer(startConerColor, canvas2, densityDpi);

            drawChart.initPaintEndConer(endConerColor, canvas2, width, densityDpi);

            //Target Bubble
            drawChart.initBottomBubbleGreen(context, Double.valueOf(obj.getTarget()).toString(), canvas2, target, densityDpi);


        } else if (fromholde instanceof TextChartViewHolder) {
            TextChart obj = (TextChart) reportsList.get(position);
            TextChartViewHolder viewHolder = (TextChartViewHolder) fromholde;

            int bitmapHeight = (220 * height) / 1360;

            Bitmap bitmap2 = Bitmap.createBitmap(width, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap2);
            viewHolder.drawingTextImageView.setImageBitmap(bitmap2);

            viewHolder.txt_report_name.setText(obj.getParameterName());

            List<String> values = obj.getValues();


            TextChartView drawChart = new TextChartView();

            drawChart.drawMainBarInRed(canvas2, width, densityDpi);

            drawChart.initPaintSectionOne(width / 3, canvas2, densityDpi);

            drawChart.initPaintSectionTwo(width / 3, canvas2, densityDpi);

            drawChart.initPaintStartConer("", canvas2, densityDpi);

            drawChart.initPaintEndConer("", canvas2, width, densityDpi);

            drawChart.initMyStatus(context, width, obj.getCurrent(), canvas2, densityDpi, obj.getValues());

            drawChart.initTargetStatus(context, width, obj.getTarget(), canvas2, densityDpi, obj.getValues());

            drawChart.initCaptionText(context, width, obj.getCurrent(), canvas2, densityDpi, obj.getValues());

        } else if (fromholde instanceof HeaderViewHolder) {
            String obj = (String) reportsList.get(position);
            HeaderViewHolder viewHolder = (HeaderViewHolder) fromholde;
            viewHolder.txt_heading.setText(obj);
        }
        if (fromholde instanceof GoalViewHolder) {
            Goal obj = (Goal) reportsList.get(position);
            GoalViewHolder viewHolder = (GoalViewHolder) fromholde;
            viewHolder.txt_heading.setText(obj.getName());
            viewHolder.txt_description.setText(obj.getText());
            Glide.with(context).load(obj.getIcon()).into(viewHolder.img_main_icon);
        }
        if (fromholde instanceof EngadgementViewHolder) {
            EngagementType obj = (EngagementType) reportsList.get(position);
            EngadgementViewHolder viewHolder = (EngadgementViewHolder) fromholde;
            String text = obj.getValue() + "%";
            viewHolder.txt_progress_value.setText(text);
            viewHolder.progressBar_Program_Circle.setProgress(Integer.parseInt(obj.getValue()));
        }
        if (fromholde instanceof HealthStatusViewHolder) {
            DBString obj = (DBString) reportsList.get(position);
            HealthStatusViewHolder viewHolder = (HealthStatusViewHolder) fromholde;
            viewHolder.txthealthstatusdesc.setText(obj.getName());
        }
        if (fromholde instanceof EntitlementViewHolder) {
            Entitlement obj = (Entitlement) reportsList.get(position);
            EntitlementViewHolder viewHolder = (EntitlementViewHolder) fromholde;
            String countText = obj.getUsedCount().toString() + "/" + obj.getTotalCount().toString();
            viewHolder.txt_heading.setText(obj.getText());
            viewHolder.btn_action.setText(countText);
            final String action = obj.getAction();
            final String meta = obj.getMeta();
            viewHolder.btn_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClickListner != null)
                        lickEntitilementButtonInterfaceListner.onEntitilementButtonClick(action, meta);
                }
            });


            if (obj.getUnlimted()) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.btn_action.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.program_entitlement_bg_blue));
                } else {
                    viewHolder.btn_action.setBackground(ContextCompat.getDrawable(context, R.drawable.program_entitlement_bg_blue));
                }
                viewHolder.btn_action.setText("Unlimited");
            } else {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.btn_action.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.program_entitlement_bg_yelloew));
                } else {
                    viewHolder.btn_action.setBackground(ContextCompat.getDrawable(context, R.drawable.program_entitlement_bg_yelloew));
                }
            }
        }


        //DASHBOARD VIEW........................

        if (fromholde instanceof Dashboard_ViewHolder) {

            final Dashboard_ViewHolder viewHolder = (Dashboard_ViewHolder) fromholde;

            //Still nothing..................

            String appToken = pref.getUserToken();
            formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
            final boolean isFirst = true;

            viewHolder.txt_days_count.setText(lifePoints);
            //======================================
            int year = Calendar.getInstance().get(Calendar.YEAR);
            viewHolder.txt_years_days.setText(Integer.toString(year));

            viewHolder.lay_btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClickListner != null)
                        backClickListner.onBackToPrevious();
                }
            });
            viewHolder.btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClickListner != null)
                        backClickListner.onBackToPrevious();
                }
            });

            viewHolder.cardview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHistoryListner != null)
                        viewHistoryListner.onViewHistory();

                }
            });

            viewHolder.txt_senven_days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnClick7DaysGraphListner != null)
                        OnClick7DaysGraphListner.onView7DayGraph();
                    //nextToDetailsActivity("1");
                }
            });
            viewHolder.txt_thirtydays_days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnClick30DaysGraphListner != null)
                        OnClick30DaysGraphListner.onView30DayGraph();
                    //nextToDetailsActivity("2");
                }
            });
            viewHolder.txt_nintydays_days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnClick90DaysGraphListner != null)
                        OnClick90DaysGraphListner.onView90DayGraph();
                    //nextToDetailsActivity("3");
                }
            });
            viewHolder.txt_years_days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnClick365DaysGraphListner != null)
                        OnClick365DaysGraphListner.onView365DayGraph();
                    //nextToDetailsActivity("4");
                }
            });

            viewHolder.txt_select_user_gruop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (OnClickSelecteAverageTypeListner != null)
                        OnClickSelecteAverageTypeListner.onSelecteAverageType("summery_view");

                }
            });

            //SECOND DAYS SETUP ....................
            //=================================
            List<Date> datesListt = new ArrayList<>();
            Calendar startDay = Calendar.getInstance();
            Date currentDate = startDay.getTime();
            startDay.add(Calendar.DATE, -30);
            Date oldDate = startDay.getTime();


            String currentStrDate = formatWith_yyyy_MM_dd.format(currentDate.getTime());
            //    String currentStrDate = selectedDate;

            datesListt = Utility.getDatesBetweenUsingJava7(oldDate, currentDate);


            int strokeColor;
            int fillColor;

            viewHolder.dateView.removeAllViews();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            //  context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // intWidth = displayMetrics.widthPixels;


            for (int num = 0; num < datesListt.size(); num++) {

                inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.goal_dashboard_dateview, null);

                RelativeLayout image_container = (RelativeLayout) myView.findViewById(R.id.image_container);
                TextView txt_date = (TextView) myView.findViewById(R.id.txt_date);

                String dayName = Integer.toString(datesListt.get(num).getDate());

                txt_date.setText(dayName);
                int dataNum = num - 1;
                Date dobj = datesListt.get(num);
                // String  strDate=Integer.toString(datesList.get(num).getDate());

                Calendar starttDay = Calendar.getInstance();
                Date currenttDate = starttDay.getTime();

                String strDate = formatWith_yyyy_MM_dd.format(dobj.getTime());


                if (selectedDate != null) {

                } else {
                    selectedDate = Ram.getDateWithString();
                }

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
                } else if (datesListt.get(num).compareTo(currenttDate) > 0) {
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
                } else if (datesListt.get(num).compareTo(currentDate) < 0) {
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


                DashBoardEventDateObject oc = new DashBoardEventDateObject(context);
                oc.setDate(dobj);
                myView.setOnClickListener(oc);

                viewHolder.dateView.addView(myView);

                if (Ram.isDashboardFirsttime()) {
                    intWidth = 720 * 6;
                    selectedDate = formatWith_yyyy_MM_dd.format(currentDate.getTime());
                    Ram.setIsDashboardFirsttime(false);
                    if (selectedDate.equals(currentStrDate)) {

//                        runOnUiThread(new Runnable() {
//                            public void run() {
                        viewHolder.hscrollViewMain.post(new Runnable() {
                            public void run() {
                                viewHolder.hscrollViewMain.scrollTo(intWidth, 0);
                            }
                        });
//                            }
//                        });
                    }

                } else {
//                    if (selectedDate.equals(currentStrDate)) {
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                viewHolder.hscrollViewMain.post(new Runnable() {
//                                    public void run() {
//                                        viewHolder.hscrollViewMain.scrollTo(intWidth, 0);
//                                    }
//                                });
//                            }
//                        });
//                    }
                }
            }
            final List<Date> finalDatesListt = datesListt;
            final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                    ViewTreeObserver.OnScrollChangedListener() {

                        @Override
                        public void onScrollChanged() {
                            //do stuff here
                            int ssss = viewHolder.hscrollViewMain.getScrollX();

                            if (isFirst) {
                                int scfullWidthScroll = viewHolder.dateView.getWidth();
                                int cellWidth = scfullWidthScroll - 340;
                                fullWidthScroll = cellWidth / 30;
                            }

                            int cc = ssss / fullWidthScroll;

                            if (cc > 26) {
                                viewHolder.txt_today_text.setText("Today");
                            } else {
                                Date todayy = finalDatesListt.get(cc);
                                int month = todayy.getMonth();
                                String monthName = getMonth(month);
                                viewHolder.txt_today_text.setText(monthName);
                            }
                        }
                    };

            viewHolder.hscrollViewMain.setOnTouchListener(new View.OnTouchListener() {
                private ViewTreeObserver observer;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (observer == null) {
                        observer = viewHolder.hscrollViewMain.getViewTreeObserver();
                        observer.addOnScrollChangedListener(onScrollChangedListener);
                    } else if (!observer.isAlive()) {
                        observer.removeOnScrollChangedListener(onScrollChangedListener);
                        observer = viewHolder.hscrollViewMain.getViewTreeObserver();
                        observer.addOnScrollChangedListener(onScrollChangedListener);
                    }

                    return false;
                }
            });

            //  String name=Ram.getGoalObject().getName();

//            name = mainDataObj.getName();
//            steps = mainDataObj.getSteps();
//            proPic = mainDataObj.getProfilePictureLink();
//
            viewHolder.txt_user_name.setText(Ram.getGoalObject().getName());
            String steps_with_comma = NumberFormat.getIntegerInstance().format(Integer.parseInt(Ram.getGoalObject().getSteps()));
            viewHolder.txt_user_steps.setText(steps_with_comma);
            com.ayubo.life.ayubolife.goals_extention.models.dashboard.AverageCategory stepAverageObj;
            stepAverageObj = Ram.getGoalObject().getAverageCategory();
            //jgkhk set valuev from oobject....
            if (Ram.getGoalObject() != null) {
                String avg_steps_with_comma = NumberFormat.getIntegerInstance().format(Integer.parseInt(stepAverageObj.getSteps()));
                viewHolder.txt_user_avg_steps.setText(avg_steps_with_comma);
                viewHolder.txt_select_user_gruop.setText(stepAverageObj.getName());
                //SET  img_company_logo  ===========================

                String catogoryId = stepAverageObj.getId();

                String img = stepAverageObj.getImage_url();
                String imageURL = img.replace("zoom_level", "xxxhdpi");

                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (activity.isFinishing()) {
                        return;
                    }
                }

                RequestOptions requestOptions = new RequestOptions().transform(new CircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.NONE);

                Glide.with(context).load(imageURL)
                        .transition(withCrossFade())
                        .apply(requestOptions)
                        .into(viewHolder.img_company_logo);

                int imageSize = com.ayubo.life.ayubolife.utility.Utility.getImageSizeFor_DeviceDensitySize(50);

                RequestOptions requestOptions2 = new RequestOptions()
                        .transform(new CircleTransform(context))
                        .override(imageSize, imageSize)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(context).load(Ram.getGoalObject().getProfilePictureLink())
                        .transition(withCrossFade())
                        .apply(requestOptions2)
                        .into(viewHolder.img_user_picture);


            } else {
                viewHolder.txt_user_avg_steps.setText("0");
                viewHolder.txt_select_user_gruop.setText(pref.getGoalCategoryName());
            }

            System.out.println("==========appToken=====================" + appToken);
        }

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
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
                Toast.makeText(c, "No data for future dates ", Toast.LENGTH_LONG).show();
            } else {

                String dateString = formatWith_yyyy_MM_dd.format(date.getTime());

                selectedDate = dateString;
                Ram.setDateWithString(selectedDate);
                // initData();
                String catogoryId = pref.getGoalCategory();

                if (OnClickReloadStepsListner != null)
                    OnClickReloadStepsListner.onViewReloadSteps(dateString);


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


    @Override
    public int getItemCount() {

        return reportsList.size();

    }


    @Override
    public int getItemViewType(int position) {

        if (reportsList.get(position) instanceof DB4String)
            return VIEW_TYPE_DASHBOARD;
        else if (reportsList.get(position) instanceof MedicalChart)
            return VIEW_TYPE_CHART;
        else if (reportsList.get(position) instanceof TextChart) {
            return VIEW_TYPE_TEXT;
        } else if (reportsList.get(position) instanceof Goal) {
            return VIEW_TYPE_GOAL;
        } else if (reportsList.get(position) instanceof EngagementType) {
            return VIEW_TYPE_ENGADGEMENT;
        } else if (reportsList.get(position) instanceof DBString) {
            return VIEW_TYPE_HEALTH_STATUS;
        } else if (reportsList.get(position) instanceof Entitlement) {
            return VIEW_TYPE_ENTITLEMENT;
        } else if (reportsList.get(position) instanceof String) {
            return VIEW_TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }


    class TextChartViewHolder extends RecyclerView.ViewHolder {

        TextView txt_report_name;
        ImageView drawingTextImageView;

        TextChartViewHolder(View itemView) {
            super(itemView);
            drawingTextImageView = (ImageView) itemView.findViewById(R.id.drawingTextImageView);
            txt_report_name = itemView.findViewById(R.id.txt_report_name);

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txt_heading;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_heading = itemView.findViewById(R.id.txt_heading);
        }
    }

    class HealthStatusViewHolder extends RecyclerView.ViewHolder {
        TextView txt_healthstatusheading, txthealthstatusdesc;

        HealthStatusViewHolder(View itemView) {
            super(itemView);
            txt_healthstatusheading = itemView.findViewById(R.id.txt_health_status_heading);
            txthealthstatusdesc = itemView.findViewById(R.id.txt_health_statusdesc);
        }
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {

        TextView txt_heading;
        ImageView img_main_icon;
        TextView txt_description;

        GoalViewHolder(View itemView) {
            super(itemView);
            img_main_icon = (ImageView) itemView.findViewById(R.id.img_main_icon);
            txt_heading = itemView.findViewById(R.id.txt_heading);
            txt_description = itemView.findViewById(R.id.txt_description);

        }
    }

    class EngadgementViewHolder extends RecyclerView.ViewHolder {

        TextView txt_progress_value;
        ProgressBar progressBar_Program_Circle;

        EngadgementViewHolder(View itemView) {
            super(itemView);
            progressBar_Program_Circle = itemView.findViewById(R.id.progressBar_Program_Circle);
            txt_progress_value = itemView.findViewById(R.id.txt_progress_value);
        }
    }

    class EntitlementViewHolder extends RecyclerView.ViewHolder {

        TextView txt_heading;
        Button btn_action;

        EntitlementViewHolder(View itemView) {
            super(itemView);
            btn_action = itemView.findViewById(R.id.btn_action);
            txt_heading = itemView.findViewById(R.id.txt_heading);

        }
    }

    class ChartTypeOne_ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_report_name;
        ImageView drawingChartImageView;

        ChartTypeOne_ViewHolder(View itemView) {
            super(itemView);
            drawingChartImageView = (ImageView) itemView.findViewById(R.id.drawingChartImageView);
            txt_report_name = itemView.findViewById(R.id.txt_report_name);

        }
    }

    class Dashboard_ViewHolder extends RecyclerView.ViewHolder {

        CardView cardview1;
        final HorizontalScrollView hscrollViewMain;
        LinearLayout dateView;
        LinearLayout lay_btnBack;

        RecyclerView programdashboard_recycler;
        TextView txt_select_user_gruop, txt_today_text, txt_senven_days, txt_thirtydays_days, txt_nintydays_days,
                txt_years_days, txt_user_name, txt_user_steps, txt_user_avg_steps, txt_days_count;
        ImageView img_user_picture, img_company_logo, img_summery_image, btn_backImgBtn;


        Dashboard_ViewHolder(View itemView) {
            super(itemView);
            programdashboard_recycler = itemView.findViewById(R.id.programdashboard_recycler);

            txt_select_user_gruop = itemView.findViewById(R.id.txt_select_user_gruop);
            txt_today_text = itemView.findViewById(R.id.txt_today_text);
            txt_senven_days = itemView.findViewById(R.id.txt_senven_days);
            txt_thirtydays_days = itemView.findViewById(R.id.txt_thirtydays_days);
            txt_nintydays_days = itemView.findViewById(R.id.txt_nintydays_days);
            txt_years_days = itemView.findViewById(R.id.txt_years_days);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_user_steps = itemView.findViewById(R.id.txt_user_steps);
            txt_user_avg_steps = itemView.findViewById(R.id.txt_user_avg_steps);
            txt_days_count = itemView.findViewById(R.id.txt_days_count);


            img_user_picture = itemView.findViewById(R.id.img_user_picture);
            img_company_logo = itemView.findViewById(R.id.img_company_logo);
            img_summery_image = itemView.findViewById(R.id.img_summery_image);
            btn_backImgBtn = itemView.findViewById(R.id.btn_backImgBtn);

            hscrollViewMain = itemView.findViewById(R.id.scrollViewMain_dashb);
            dateView = itemView.findViewById(R.id.dateView);
            lay_btnBack = itemView.findViewById(R.id.lay_btnBack);
            cardview1 = itemView.findViewById(R.id.card_one);

        }
    }


//    public void setOnClickDeleteReportListener(com.ayubo.life.ayubolife.reports.adapters.ReportDetailsAdapter.OnClickDeleteReportListner listener) {
//        this.listener_deleteReport = listener;
//    }

//    Interface For Events Handing ...........


}
