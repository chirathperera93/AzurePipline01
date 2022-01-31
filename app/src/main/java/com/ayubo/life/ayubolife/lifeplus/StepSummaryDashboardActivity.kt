package com.ayubo.life.ayubolife.lifeplus

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.goals_extention.StepHistory_Activity
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.activity.TestReportsActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.bumptech.glide.Glide
import com.flavors.changes.Constants
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_step_summary_dashboard.*
import kotlinx.android.synthetic.main.dashboard_step_summary_card.view.*
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.TimeZone
import kotlin.collections.ArrayList
import kotlin.collections.set

class StepSummaryDashboardActivity :
        BaseActivity(), BountyCardAdapter.OnBountyCardItemClickListener {

    private var stepSummaryCardAdapter: StepSummaryCardAdapter? = null
    private var bountyCardAdapter: BountyCardAdapter? = null
    lateinit var pref: PrefManager;
    lateinit var appToken: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_summary_dashboard)
        pref = PrefManager(baseContext);
        appToken = pref.getUserToken();

        setMainData()



        go_to_steps_detail_page.setOnClickListener {
            val i: Intent = Intent(this, StepHistory_Activity::class.java);
            startActivity(i);
        }

        feed_back_topic_layout.setOnClickListener {
            finish()
        }
    }


    private fun setMainData() {

        step_summary_loading.visibility = View.VISIBLE
        val getBaseApiClientNew: ApiInterface =
                ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);


        val timeZone: TimeZone = TimeZone.getDefault();
        val timeZoneId = timeZone.id;
        val device_modal: String = Build.MODEL;

        val callGetBaseApiClientNew: Call<ProfileDashboardResponseData> =
                getBaseApiClientNew.getStepSummary(
                        AppConfig.APP_BRANDING_ID, appToken, "daily",
                        System.currentTimeMillis(),
                        timeZoneId,
                        device_modal
                );


        callGetBaseApiClientNew.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
            ) {
                if (response.isSuccessful) {
                    step_summary_loading.visibility = View.GONE
                    System.out.println(response)


                    val summaryDashboardData: JsonObject =
                            Gson().toJsonTree(response.body()!!.data).asJsonObject;

                    val title = summaryDashboardData.get("title").asString
                    val achievements = summaryDashboardData.get("achievements").asString
                    val achievement_label = summaryDashboardData.get("achievement_label").asString
                    val badge_url = summaryDashboardData.get("badge_url").asString
                    val badge_title = summaryDashboardData.get("badge_title").asString
                    val analysis_title = summaryDashboardData.get("analysis_title").asString
                    val avg_achievement = summaryDashboardData.get("avg_achievement").asString
                    val chartData: JsonObject = summaryDashboardData.get("chart_data").asJsonObject
                    val analysis: JsonObject = summaryDashboardData.get("analysis").asJsonObject
                    val bounties: JsonArray = summaryDashboardData.get("bounties").asJsonArray


                    summary_dashboard_topic.setText(title);
                    summary_dashboard_steps.setText(achievements);
                    summary_dashboard_point_text.setText(achievement_label);



                    Glide.with(baseContext).load(badge_url).into(badge_image);
                    badge_title_text.setText(badge_title);

                    analysis_title_text.setText(analysis_title);
                    avg_achievement_text.setText(avg_achievement);


                    var gColorValue1 = resources.getColor(R.color.daily_g_color1);
                    var gColorValue2 = resources.getColor(R.color.daily_g_color2);
                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        gColorValue1 = resources.getColor(R.color.daily_g_color1);
                        gColorValue2 = resources.getColor(R.color.daily_g_color2);
                    } else {
                        gColorValue1 = resources.getColor(R.color.ayubo_gradient_start);
                        gColorValue2 = resources.getColor(R.color.ayubo_gradient_end);
                    }


                    val gdCommon = GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM,
                            intArrayOf(gColorValue1, gColorValue2)
                    )
                    gdCommon.gradientType = GradientDrawable.LINEAR_GRADIENT
                    gdCommon.cornerRadius = 4f

                    step_icon_image_linear_layout.setBackgroundDrawable(gdCommon)

                    setChartData(chartData)

                    addStepSummaryCards(analysis);

                    addBounties(bounties);


                } else {
                    step_summary_loading.visibility = View.GONE
                    Toast.makeText(
                            baseContext,
                            "There is an issue when loading data, Please contact admin.",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                step_summary_loading.visibility = View.GONE
                Toast.makeText(
                        baseContext,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                ).show()
            }

        })


    }


    private fun setChartData(chartData: JsonObject) {

        System.out.println(chartData)

//        val mySteps: List<String> = listOf("350", "340", "361", "0", "0", "0", "0");

//        val myDates: List<String> = listOf(
//            "Mon",
//            "Tue",
//            "Wed",
//            "Thu",
//            "Fri",
//            "Sat",
//            "Sun"
//        )
//        val othersSteps: List<String> =
//            listOf("370", "320", "330", "380", "370", "360", "390");


        val mySteps: ArrayList<String> = ArrayList<String>();
        val myDates: ArrayList<String> = ArrayList<String>();
        val othersSteps: ArrayList<String> = ArrayList<String>();

        val me = chartData.get("me").asJsonArray
        val average = chartData.get("average").asJsonArray


        for (item in 0 until me.size()) {
            val myStepDetail: JsonObject = me.get(item) as JsonObject;

            myDates.add(myStepDetail.get("day").asString)
            mySteps.add(myStepDetail.get("value").asDouble.toString())

            System.out.println(myStepDetail)

        }

        for (item in 0 until average.size()) {
            val averageStepDetail: JsonObject = average.get(item) as JsonObject;
            othersSteps.add(averageStepDetail.get("value").asDouble.toString())

        }

        val myStepsStart = mySteps.get(0);
        val myStepsEnd = mySteps.get(mySteps.size - 1);

        val othersStepsStart = othersSteps.get(0);
        val othersStepsEnd = othersSteps.get(othersSteps.size - 1);

        val myStepsStartDouble = (myStepsStart.toDouble() * 25) / 100
        val myStepsEndDouble = (myStepsEnd.toDouble() * 25) / 100

        val othersStepsStartDouble = (othersStepsStart.toDouble() * 25) / 100
        val othersStepsEndDouble = (othersStepsEnd.toDouble() * 25) / 100



        myDates.add(0, " ")
        myDates.add("  ")


        mySteps.add(0, myStepsStartDouble.toString())
        mySteps.add(myStepsEndDouble.toString())


        othersSteps.add(0, othersStepsStartDouble.toString())
        othersSteps.add(othersStepsEndDouble.toString())


        var xAxisLabels_ArrayList: ArrayList<String>? = null;
        step_cart.clear();
        // no description text
        step_cart.description.isEnabled = false;
        // enable touch gestures
        step_cart.setTouchEnabled(true);
        step_cart.dragDecelerationFrictionCoef = 0.9f;
        // enable scaling and dragging
        step_cart.isDragEnabled = true;
        step_cart.setScaleEnabled(false);
        step_cart.setDrawGridBackground(false);
        step_cart.isHighlightPerDragEnabled = true;
        // if disabled, scaling can be done on x- and y-axis separately
//            chart1.setBackgroundColor(resources.getColor(R.color.badge_text_color));


        step_cart.animateX(2500);
        step_cart.setScaleEnabled(true);
        step_cart.setPinchZoom(true);


        val legend: Legend = step_cart.legend;
        val myStepsChartValueList: ArrayList<Entry> = ArrayList<Entry>();
        val othersStepsChartValueList: ArrayList<Entry> = ArrayList<Entry>();
        var st: Int = 0;
        var stForOthers: Int = 0;
        xAxisLabels_ArrayList = ArrayList<String>();


        if ((myDates != null) && (myDates.size > 0)) {
            var hmap: LinkedHashMap<String, String>? = null;
            var hmapForSet2: LinkedHashMap<String, String>? = null;

            hmap = fillDataToDaysMap(myDates, mySteps);



            hmapForSet2 = fillDataToDaysMap(myDates, othersSteps);

            val it: kotlin.collections.Iterator<*> = hmap.iterator();
            val hmapForSet2Iterator: kotlin.collections.Iterator<*> =
                    hmapForSet2.iterator();


            var count: Int = 0;
            var countToOther: Int = 0;

            while (it.hasNext()) {

                val entry: Map.Entry<*, *> = it.next() as Map.Entry<*, *>;
                val strDate: String = entry.key as String;
                val strSteps: String = entry.value as String;
                val value: Double = strSteps.toDouble();

                val yval: Int = value.toInt();

                myStepsChartValueList.add(Entry(count.toFloat(), yval.toFloat()));
                val currentDateStr: String = strDate;
                xAxisLabels_ArrayList.add(currentDateStr);

                if (st < yval) {
                    st = yval;
                }

                count++;
            }


            while (hmapForSet2Iterator.hasNext()) {

                val entry: Map.Entry<*, *> =
                        hmapForSet2Iterator.next() as Map.Entry<*, *>;
                val strSteps: String = entry.value as String;
                val value: Double = strSteps.toDouble();

                val yval: Int = value.toInt();

                othersStepsChartValueList.add(
                        Entry(
                                countToOther.toFloat(),
                                yval.toFloat()
                        )
                );

                if (stForOthers < yval) {
                    stForOthers = yval;
                }

                countToOther++;
            }
        }

        // modify the legend ...
        legend.form = Legend.LegendForm.SQUARE;
        legend.textSize = 11f;
        legend.textColor = resources.getColor(R.color.black);
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        legend.orientation = Legend.LegendOrientation.HORIZONTAL;
        legend.setDrawInside(true);

        val xAxis: XAxis = step_cart.xAxis;
        xAxis.textSize = 11f;
        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        xAxis.textColor = resources.getColor(R.color.chart_x_axis);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.axisMinimum = 0F;
        xAxis.granularity = 1f; // only intervals of 1 day
        xAxis.gridColor = resources.getColor(R.color.black);
        xAxis.setAxisMinValue(0F)


//            chart1.getAxisLeft().setEnabled(false);
        // disable dual axis (only use LEFT axis)
        step_cart.axisRight.isEnabled = false;


        val yAxis: YAxis = step_cart.axisLeft;
        yAxis.textColor = resources.getColor(R.color.color_DBDBDB);

        if (stForOthers > st) {
            yAxis.axisMaximum = stForOthers.toFloat() + 100;
        } else {
            yAxis.axisMaximum = st.toFloat() + 100;
        }



        yAxis.setDrawGridLines(false);
        yAxis.isGranularityEnabled = true;
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yAxis.setAxisMinValue(0F)
        yAxis.setDrawGridLines(true);
        yAxis.gridColor = resources.getColor(R.color.calendar_week);
        yAxis.gridLineWidth = 0.5F

        val set1: LineDataSet;
        val set2: LineDataSet;


        if (step_cart.data != null &&
                step_cart.data.dataSetCount > 0
        ) {
            set1 = step_cart.getData().getDataSetByIndex(0) as LineDataSet;
            set2 = step_cart.getData().getDataSetByIndex(0) as LineDataSet;

            set1.values = myStepsChartValueList;
            set2.values = othersStepsChartValueList;

            if (step_cart.lineData != null) {
                step_cart.lineData.clearValues();
                step_cart.notifyDataSetChanged();
            }
            step_cart.data.notifyDataChanged();
            step_cart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(myStepsChartValueList, "set1");
            set2 = LineDataSet(othersStepsChartValueList, "set2");

            set1.setDrawIcons(false);
            set2.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 0f, 0f);
            set2.enableDashedLine(10f, 0f, 0f);


            set1.axisDependency = YAxis.AxisDependency.LEFT;
            set2.axisDependency = YAxis.AxisDependency.LEFT;


            set1.color = resources.getColor(R.color.theme_color);

            set2.color = resources.getColor(R.color.color_B3B3B3);

            set1.setCircleColor(R.color.theme_color);
            set2.setCircleColor(R.color.color_B3B3B3);

            set1.lineWidth = 2f;
            set2.lineWidth = 2f;

            set1.setDrawCircles(false);
            set1.setDrawValues(false);

            set2.setDrawCircles(false);
            set2.setDrawValues(false);


            set1.mode = LineDataSet.Mode.CUBIC_BEZIER;
            set2.mode = LineDataSet.Mode.CUBIC_BEZIER;

            // set the filled area
            set1.setDrawFilled(true);
            set2.setDrawFilled(false);

            set1.fillFormatter = object : IFillFormatter {
                override fun getFillLinePosition(
                        dataSet: ILineDataSet?,
                        dataProvider: LineDataProvider?
                ): Float {
                    return step_cart.axisLeft.axisMinimum;
                }


            };

            if (Utils.getSDKInt() >= 18) {


                // fill drawable only supported on api level 18 and above
                var drawable: Drawable? = null

                if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {

                    drawable =
                            ContextCompat.getDrawable(
                                    baseContext,
                                    R.drawable.life_plus_fade_with_white
                            );
                } else {
                    drawable =
                            ContextCompat.getDrawable(
                                    baseContext,
                                    R.drawable.ayubo_life_fade_with_white
                            );
                }



                set1.fillDrawable = drawable;


            } else {
                set1.fillColor = resources.getColor(R.color.theme_color);
            }

            set1.highLightColor = resources.getColor(R.color.theme_color);
            set2.highLightColor = resources.getColor(R.color.color_B3B3B3);

            set1.setDrawCircleHole(false);
            set2.setDrawCircleHole(false);

            val data: LineData = LineData();

//                data = LineData(set1);

            data.addDataSet(set2)
            data.addDataSet(set1)



            step_cart.legend.isEnabled = false;

            // mChart.getDescription().setEnabled(false);
            data.setValueTextColor(resources.getColor(R.color.black));
            data.setValueTextSize(9f);

            if (step_cart.lineData != null) {
                step_cart.lineData.clearValues();
                step_cart.notifyDataSetChanged();
            }
            step_cart.clear();

            step_cart.data = data;
            step_cart.setViewPortOffsets(0F, 0F, 0F, 0F);
            step_cart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);



            step_cart.notifyDataSetChanged();
            step_cart.invalidate();


        }

        // create marker to display box when values are selected
        val mv: MyCustomMarkView = MyCustomMarkView(baseContext, R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(step_cart);
        step_cart.setMarker(mv);


//        xAxis.valueFormatter = TestReportsActivity().MyXAxisValueFormatter(xAxisLabels_ArrayList);

        //String setter in x-Axis
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisLabels_ArrayList);
    }


    @SuppressLint("WrongConstant")
    private fun addStepSummaryCards(analysis: JsonObject) {

        try {

            System.out.println(analysis)
            card_types_recycler_view.layoutManager =
                    LinearLayoutManager(this, LinearLayout.VERTICAL, false)


            val stepCardItemsArrayList = ArrayList<StepSummaryCardItem>()


            val dailySummaryCard = analysis.get("daily").asJsonObject
            val weeklySummaryCard = analysis.get("weekly").asJsonObject
            val monthlySummaryCard = analysis.get("monthly").asJsonObject


            if (dailySummaryCard.get("show_card").asBoolean) {
                stepCardItemsArrayList.add(
                        StepSummaryCardItem(
                                dailySummaryCard.get("title").asString,
                                dailySummaryCard.get("summary").asString,
                                dailySummaryCard.get("percentage").asDouble,
                                dailySummaryCard.get("circle_top").asString,
                                dailySummaryCard.get("circle_centre").asString,
                                dailySummaryCard.get("circle_bottom").asString,
                                dailySummaryCard.get("remaining_value").asString,
                                dailySummaryCard.get("remaining_label").asString,
                                dailySummaryCard.get("icon_url").asString


                        )

                )
            }


            if (weeklySummaryCard.get("show_card").asBoolean) {
                stepCardItemsArrayList.add(
                        StepSummaryCardItem(
                                weeklySummaryCard.get("title").asString,
                                weeklySummaryCard.get("summary").asString,
                                weeklySummaryCard.get("percentage").asDouble,
                                weeklySummaryCard.get("circle_top").asString,
                                weeklySummaryCard.get("circle_centre").asString,
                                weeklySummaryCard.get("circle_bottom").asString,
                                weeklySummaryCard.get("remaining_value").asString,
                                weeklySummaryCard.get("remaining_label").asString,
                                weeklySummaryCard.get("icon_url").asString


                        )

                )
            }

            if (monthlySummaryCard.get("show_card").asBoolean) {
                stepCardItemsArrayList.add(
                        StepSummaryCardItem(
                                monthlySummaryCard.get("title").asString,
                                monthlySummaryCard.get("summary").asString,
                                monthlySummaryCard.get("percentage").asDouble,
                                monthlySummaryCard.get("circle_top").asString,
                                monthlySummaryCard.get("circle_centre").asString,
                                monthlySummaryCard.get("circle_bottom").asString,
                                monthlySummaryCard.get("remaining_value").asString,
                                monthlySummaryCard.get("remaining_label").asString,
                                monthlySummaryCard.get("icon_url").asString


                        )

                )
            }




            stepSummaryCardAdapter = StepSummaryCardAdapter(this, stepCardItemsArrayList, false)

            card_types_recycler_view.adapter = stepSummaryCardAdapter


        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }


    }

    @SuppressLint("WrongConstant")
    private fun addBounties(bounties: JsonArray) {

        try {
            bounty_card_recycler_view.layoutManager =
                    LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            val bountyCardItemsArrayList = ArrayList<BountyCardItem>()

            if (bounties.size() > 0) {
                bounty_topic.visibility = View.VISIBLE
            } else {
                bounty_topic.visibility = View.GONE
            }

            for (i in 0 until bounties.size()) {
                val bounty = bounties.get(i)
                bountyCardItemsArrayList.add(
                        BountyCardItem(
                                bounty.asJsonObject.get("title").asString,
                                bounty.asJsonObject.get("description").asString,
                                bounty.asJsonObject.get("icon_url").asString,
                                bounty.asJsonObject.get("action_meta").asJsonObject
                        )

                )
            }
            bountyCardAdapter = BountyCardAdapter(this, bountyCardItemsArrayList, false)

            try {
                bountyCardAdapter!!.onBountyCardItemClickListener =
                        this@StepSummaryDashboardActivity
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bounty_card_recycler_view.adapter = bountyCardAdapter

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }


    }


    private fun fillDataToDaysMap(
            myDates: List<String>,
            mySteps: List<String>
    ): LinkedHashMap<String, String> {

        val mapsnre: LinkedHashMap<String, String> = LinkedHashMap<String, String>();

        for (i in myDates.indices) {

            val serverDate: String = myDates[i]
            mapsnre[serverDate] = mySteps[i];

        }



        return mapsnre;
    }

//    inner class MyXAxisValueFormatter(values: ArrayList<String>) : IAxisValueFormatter {
//
//        private val mValues: ArrayList<String> = values;
//
//        override fun getFormattedValue(value: Float, axis: AxisBase): String {
//            // "value" represents the position of the label on the axis (x or y)
//            var data: String = "";
//
//            if (mValues != null) {
//
//                if (mValues.size > 1) {
//                    if (mValues.size > value.toInt()) {
//                        data = mValues.get(value.toInt());
//                    }
//                }
//            }
//            return data;
//        }
//
//    }

    override fun onBountyCardItemClick(action: String, meta: String) {
        processAction(action, meta)
    }


}