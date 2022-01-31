package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsLogger
import com.flavors.changes.Constants
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dashboard_step_summary_card.view.*
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.*
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_center_box_main_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_center_box_sub_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_left_box_main_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_left_box_sub_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_linear_layout
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_right_box_main_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_bottom_right_box_sub_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_description_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_header_layout
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_options_center_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_options_progress_header_type_image
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_options_progress_header_type_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_options_sub_text
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.cards_title
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.circular_progress_bar_dashboard_tracker
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.hydration_tracker_add_btn
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.main_background_image_card
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.*
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.image_view
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.main_background_image
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.welcome_card_main_relative
import kotlinx.android.synthetic.main.dashboard_welcome_lite_card.view.*
import kotlinx.android.synthetic.main.dashboard_wnw_card.view.*
import java.util.*
import kotlin.collections.ArrayList

const val WELCOME_LITE = 0
const val WELCOME = 1
const val WNW_CARD = 2
const val STEP_COUNT = 3
const val WEIGHT_TRACKER = 4
const val DEFAULT_VIEW = 5
const val STEP_SUMMARY = 6

@IntDef(
    WELCOME_LITE,
    WELCOME,
    WNW_CARD,
    STEP_COUNT,
    WEIGHT_TRACKER,
    DEFAULT_VIEW,
    STEP_SUMMARY
)
@Retention(AnnotationRetention.SOURCE)
annotation class DashboardCardViewTypes

class WeeklyRelatedCardTrackerAdapter(
    val context: Context,
    val dataList: ArrayList<WeeklyTrackerCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<WeeklyRelatedCardTrackerAdapter.BaseHolder>() {

    var onItemClickListener: WeeklyRelatedCardTrackerAdapter.OnItemClickListener? = null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun circularProgressBarClick(action: String, meta: String)
        fun plusButtonItemClick(action: String, meta: String)
        fun onItemClick(action: String, meta: String)
//        fun openHydrationTrackerDialog()

    }


    override fun getItemViewType(position: Int): Int {

        val card = dataList[position]

        return when (card.type) {

            "welcome_lite" -> {
                WELCOME_LITE
            }

            "welcome" -> {
                WELCOME
            }

            "wnw_card" -> {
                WNW_CARD
            }

            "step_count" -> {
                STEP_COUNT
            }

            "weight_tracker" -> {
                WEIGHT_TRACKER
            }

            "step_summary" -> {
                STEP_SUMMARY
            }

            else -> {
                DEFAULT_VIEW
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @DashboardCardViewTypes viewType: Int
    ): WeeklyRelatedCardTrackerAdapter.BaseHolder {


        when (viewType) {
            WELCOME_LITE -> {
                return WelcomeLiteCard(parent)
            }
            WELCOME -> {
                return WelcomeCard(parent)
            }
            WNW_CARD -> {
                return WnWCard(parent)
            }
            STEP_COUNT -> {
                return StepCountCard(parent)
            }
            WEIGHT_TRACKER -> {
                return WeightTrackerCard(parent)
            }
            STEP_SUMMARY -> {
                return StepSummaryCard(parent)
            }
            DEFAULT_VIEW -> {
                return DefaultViewCard(parent)
            }
            else -> {
                return DefaultViewCard(parent)
            }
        }
    }


    inner class StepSummaryCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_step_summary_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {

            System.out.println(weeklyTrackerCardItem)


//            val myDates: List<String> = listOf(
//                " ",
//                "Mon",
//                "Tue",
//                "Wed",
//                "Thu",
//                "Fri",
//                "Sat",
//                "Sun",
//                " . "
//            )
//            val mySteps: List<String> = listOf("200", "350", "340", "361", "0", "0", "0", "0", "0");
//            val othersSteps: List<String> =
//                listOf("500", "370", "320", "330", "380", "370", "360", "390", "100");


            val mySteps: ArrayList<String> = ArrayList<String>();
            val myDates: ArrayList<String> = ArrayList<String>();
            val othersSteps: ArrayList<String> = ArrayList<String>();

            val me = weeklyTrackerCardItem.chart_data!!.get("me").asJsonArray
            val average = weeklyTrackerCardItem.chart_data!!.get("average").asJsonArray


            for (item in 0 until me.size()) {
                val myStepDetail: JsonObject = me.get(item) as JsonObject;

                myDates.add(myStepDetail.get("day").asString)
                mySteps.add(myStepDetail.get("value").asDouble.toString())

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



            Glide.with(context)
                .load(weeklyTrackerCardItem.badge_url)
                .into(step_summary_img)

            step_summary_text.setText(weeklyTrackerCardItem.badge_title)

            cards_header_layout.setOnClickListener {
                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get("action").asString,
                    weeklyTrackerCardItem.click!!.get("meta").asString
                )

            }

            chart1.setOnClickListener {
                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get("action").asString,
                    weeklyTrackerCardItem.click!!.get("meta").asString
                )

            }

            card_topic.setText(weeklyTrackerCardItem.title)

            if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
                cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.life_plus_gradient_end));
            } else {
                cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.ayubo_gradient_end));
            }

            var xAxisLabels_ArrayList: ArrayList<String>? = null;
            chart1.clear();
            // no description text
            chart1.description.isEnabled = false;
            // enable touch gestures
            chart1.setTouchEnabled(true);
            chart1.dragDecelerationFrictionCoef = 0.9f;
            // enable scaling and dragging
            chart1.isDragEnabled = true;
            chart1.setScaleEnabled(false);
            chart1.setDrawGridBackground(false);
            chart1.isHighlightPerDragEnabled = true;
            // if disabled, scaling can be done on x- and y-axis separately
//            chart1.setBackgroundColor(resources.getColor(R.color.badge_text_color));


            chart1.animateX(2500);
            chart1.setScaleEnabled(true);
            chart1.setPinchZoom(true);


            val legend: Legend = chart1.legend;
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
                val hmapForSet2Iterator: kotlin.collections.Iterator<*> = hmapForSet2.iterator();


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

                    val entry: Map.Entry<*, *> = hmapForSet2Iterator.next() as Map.Entry<*, *>;
                    val strSteps: String = entry.value as String;
                    val value: Double = strSteps.toDouble();

                    val yval: Int = value.toInt();

                    othersStepsChartValueList.add(Entry(countToOther.toFloat(), yval.toFloat()));

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

            val xAxis: XAxis = chart1.xAxis;
            xAxis.textSize = 11f;
            xAxis.position = XAxis.XAxisPosition.BOTTOM;
            xAxis.textColor = resources.getColor(R.color.chart_x_axis);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.axisMinimum = 0F;
            xAxis.granularity = 1f; // only intervals of 1 day
            xAxis.gridColor = resources.getColor(R.color.grid_color);
            xAxis.gridLineWidth = 0.5F
            xAxis.setAxisMinValue(0F)


//            chart1.getAxisLeft().setEnabled(false);
            // disable dual axis (only use LEFT axis)
            chart1.axisRight.isEnabled = false;


            val yAxis: YAxis = chart1.axisLeft;
            yAxis.textColor = resources.getColor(R.color.color_B3B3B3);

            if (stForOthers > st) {
                yAxis.axisMaximum = stForOthers.toFloat();
                yAxis.setAxisMaxValue(stForOthers.toFloat() + 100);
            } else {
                yAxis.axisMaximum = st.toFloat();
                yAxis.setAxisMaxValue(st.toFloat() + 100);
            }



            yAxis.setDrawGridLines(false);
            yAxis.isGranularityEnabled = true;
            yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            yAxis.setAxisMinValue(0F)
            yAxis.setDrawGridLines(true);
            yAxis.gridColor = resources.getColor(R.color.color_727272);
            yAxis.gridLineWidth = 0.5F
            yAxis.setDrawLimitLinesBehindData(true);


            val set1: LineDataSet;
            val set2: LineDataSet;


            if (chart1.data != null &&
                chart1.data.dataSetCount > 0
            ) {
                set1 = chart1.getData().getDataSetByIndex(0) as LineDataSet;
                set2 = chart1.getData().getDataSetByIndex(0) as LineDataSet;

                set1.values = myStepsChartValueList;
                set2.values = othersStepsChartValueList;

                if (chart1.lineData != null) {
                    chart1.lineData.clearValues();
                    chart1.notifyDataSetChanged();
                }
                chart1.data.notifyDataChanged();
                chart1.notifyDataSetChanged();

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

                set2.color = resources.getColor(R.color.white);

                set1.setCircleColor(R.color.theme_color);
                set2.setCircleColor(R.color.white);

                set1.lineWidth = 2f;
                set2.lineWidth = 1f;

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
                        return chart1.axisLeft.axisMinimum;
                    }


                };

                if (Utils.getSDKInt() >= 18) {


                    // fill drawable only supported on api level 18 and above
                    var drawable: Drawable? = null

                    if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {

                        drawable = ContextCompat.getDrawable(context, R.drawable.life_plus_fade);
                    } else {
                        drawable = ContextCompat.getDrawable(context, R.drawable.ayubo_life_fade);
                    }



                    set1.fillDrawable = drawable;


                } else {
                    set1.fillColor = resources.getColor(R.color.theme_color);
                }

                set1.highLightColor = resources.getColor(R.color.theme_color);
                set2.highLightColor = resources.getColor(R.color.white);

                set1.setDrawCircleHole(false);
                set2.setDrawCircleHole(false);


                val data: LineData = LineData();

//                data = LineData(set1);

                data.addDataSet(set2)
                data.addDataSet(set1)



                chart1.legend.isEnabled = false;

                // mChart.getDescription().setEnabled(false);
                data.setValueTextColor(resources.getColor(R.color.black));
                data.setValueTextSize(9f);

                if (chart1.lineData != null) {
                    chart1.lineData.clearValues();
                    chart1.notifyDataSetChanged();
                }
                chart1.clear();

                chart1.data = data;
                chart1.setViewPortOffsets(0F, 0F, 0F, 0F);
                chart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

                //Set a custom MarkerView
//                chart1.marker = MyCustomMarkView(context);
//                chart1.setDrawMarkers(true)


                chart1.notifyDataSetChanged();


                chart1.invalidate();


            }


//            // create marker to display box when values are selected
//            val mv: MyCustomMarkView = MyCustomMarkView(context, R.layout.custom_marker_view);
//
//            // Set the marker to the chart
//            mv.setChartView(chart1);
//            chart1.setMarker(mv);


            //String setter in x-Axis
            xAxis.valueFormatter =
                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisLabels_ArrayList);


//            xAxis.valueFormatter =
//                TestReportsActivity().MyXAxisValueFormatter(xAxisLabels_ArrayList);


            val weeklyTrackerCardItemOptions: JsonObject =
                weeklyTrackerCardItem.options!!.asJsonObject;
            val weeklyTrackerCardItemBottom: JsonObject =
                weeklyTrackerCardItem.bottom!!.asJsonObject;


            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                cards_bottom_linear_layout.visibility = View.GONE
            } else {
                cards_bottom_linear_layout.visibility = View.VISIBLE
            }

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                cards_bottom_left_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_center_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_right_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
            } else {
                cards_bottom_left_box_main_text.setTextColor(resources.getColor(R.color.white))
                cards_bottom_center_box_main_text.setTextColor(resources.getColor(R.color.white))
                cards_bottom_right_box_main_text.setTextColor(resources.getColor(R.color.white))
            }



            cards_bottom_left_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_left_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_center_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_center_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_right_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_right_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("sub_text").asString else ""


            if (!weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_left_box_sub_text.setTextColor(context.resources.getColor(R.color.white))
                cards_bottom_left_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
//                    )
                }
            }

            if (!weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_center_box_sub_text.setTextColor(context.resources.getColor(R.color.white))

                cards_bottom_center_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }

            if (!weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_right_box_sub_text.setTextColor(context.resources.getColor(R.color.white))


                cards_bottom_right_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }







            return@with
        }


    }

    inner class WelcomeLiteCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_welcome_lite_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {

            println(weeklyTrackerCardItem)

            dashboard_welcome_card_lite.setOnClickListener {

                onItemClickListener!!.onItemClick(
                    weeklyTrackerCardItem.click!!.get("action").asString,
                    weeklyTrackerCardItem.click.get("meta").asString
                )


            }


            Glide.with(context)
                .load(weeklyTrackerCardItem.options!!.get("progress_header_data").asString)
                .centerCrop().into(main_background_image);
            main_topic.text = weeklyTrackerCardItem.title
            main_topic_description.text = weeklyTrackerCardItem.summary

            if (!weeklyTrackerCardItem.options.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options.get("text_color").asString.equals(
                    ""
                )
            ) {
                main_topic.setTextColor(Color.parseColor(weeklyTrackerCardItem.options.get("text_color").asString))
                main_topic_description.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItem.options.get(
                            "text_color"
                        ).asString
                    )
                )
            } else {
                main_topic_description.setTextColor(resources.getColor(R.color.theme_color))
            }

            if (weeklyTrackerCardItem.options.get("background").asJsonObject.get("type").asString.equals(
                    "color"
                )
            ) {
                if (weeklyTrackerCardItem.options.get("background").asJsonObject.get("data").asString.equals(
                        "brandgradient"
                    )
                ) {

                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        card_bottom_linear_layout.setBackgroundResource(R.drawable.bottom_life_plus_rounded_corners)
                    } else {
                        card_bottom_linear_layout.setBackgroundResource(R.drawable.bottom_ayubo_life_rounded_corners)
                    }

                }
            }




            return@with
        }
    }

    inner class WelcomeCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_welcome_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {

            System.out.println(weeklyTrackerCardItem)
            val largeImg =
                weeklyTrackerCardItem.options!!.get("background").asJsonObject.get("data").asString
            val smallImg = weeklyTrackerCardItem.options!!.get("progress_header_data").asString



            header_topic.text = weeklyTrackerCardItem.title
            header_topic_expand.text = weeklyTrackerCardItem.title


            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                header_topic.setTextColor(Color.parseColor(weeklyTrackerCardItem.options!!.get("text_color").asString))
                header_topic_expand.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItem.options!!.get(
                            "text_color"
                        ).asString
                    )
                )

                sub_topic.setTextColor(Color.parseColor(weeklyTrackerCardItem.options!!.get("text_color").asString))
                sub_topic_expand.setTextColor(Color.parseColor(weeklyTrackerCardItem.options!!.get("text_color").asString))

                txt_description.setTextColor(Color.parseColor(weeklyTrackerCardItem.options!!.get("text_color").asString))
                txt_description_expand.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItem.options!!.get(
                            "text_color"
                        ).asString
                    )
                )


                go_to_link.setTextColor(Color.parseColor(weeklyTrackerCardItem.options!!.get("text_color").asString))
                go_to_link_expand.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItem.options!!.get(
                            "text_color"
                        ).asString
                    )
                )
            } else {
                header_topic.setTextColor(resources.getColor(R.color.theme_color));
                header_topic_expand.setTextColor(resources.getColor(R.color.theme_color));

                sub_topic.setTextColor(resources.getColor(R.color.theme_color));
                sub_topic_expand.setTextColor(resources.getColor(R.color.theme_color));


                txt_description.setTextColor(resources.getColor(R.color.theme_color));
                txt_description_expand.setTextColor(resources.getColor(R.color.theme_color));

                go_to_link.setTextColor(resources.getColor(R.color.theme_color));
                go_to_link_expand.setTextColor(resources.getColor(R.color.theme_color));
            }



            sub_topic.text = weeklyTrackerCardItem.summary
            sub_topic_expand.text = weeklyTrackerCardItem.summary


            txt_description.text = weeklyTrackerCardItem.description!!.get("text").asString
            txt_description_expand.text = weeklyTrackerCardItem.description!!.get("text").asString


            go_to_link.text = weeklyTrackerCardItem.options!!.get("center_text").asString
            go_to_link_expand.text = weeklyTrackerCardItem.options!!.get("center_text").asString


            go_to_link.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
            go_to_link_expand.paintFlags = Paint.UNDERLINE_TEXT_FLAG;

            Glide.with(context).load(smallImg).centerCrop().into(main_background_image);
            main_background_image.adjustViewBounds = true
            main_background_image.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(context).load(smallImg).centerCrop().into(image_view);


            Glide.with(context).load(largeImg).centerCrop().into(main_background_image_expand);
            main_background_image_expand.scaleType = ImageView.ScaleType.FIT_XY
            main_background_image_expand.adjustViewBounds = true
            Glide.with(context).load(smallImg).centerCrop().into(image_view_expand);

            show_more.setOnClickListener {
                welcome_card_main_relative.visibility = View.GONE
                welcome_card_main_relative_expand.visibility = View.VISIBLE
//                if (show_more.text == "Show More") {

//                    main_background_image.layoutParams.height = 2300
//                    txt_description.maxLines = Integer.MAX_VALUE;
//                    show_more.text = "Show Less";
//                    image_view.visibility = View.VISIBLE
//                    main_background_image.setImageResource(0);
//                    Glide.with(context).load(largeImg).into(main_background_image);
//                    main_background_image.scaleType = ImageView.ScaleType.CENTER
//                } else {
//                    main_background_image.layoutParams.height = 900
//                    txt_description.maxLines = 4;
//                    show_more.text = "Show More";
//                    image_view.visibility = View.GONE
//                    main_background_image.setImageResource(0);
//                    Glide.with(context).load(smallImg).into(main_background_image);
//                    main_background_image.adjustViewBounds = true
//                    main_background_image.scaleType = ImageView.ScaleType.CENTER_CROP
//                }


            }

            show_more_expand.setOnClickListener {
                welcome_card_main_relative.visibility = View.VISIBLE
                welcome_card_main_relative_expand.visibility = View.GONE
            }




            go_to_link.setOnClickListener {
                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get("action").asString,
                    weeklyTrackerCardItem.click!!.get("meta").asString
                )
            }

            go_to_link_expand.setOnClickListener {
                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get("action").asString,
                    weeklyTrackerCardItem.click!!.get("meta").asString
                )
            }


            return@with
        }
    }

    inner class WnWCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_wnw_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {

            System.out.println(weeklyTrackerCardItem)

            val weeklyTrackerCardItemOptions: JsonObject =
                weeklyTrackerCardItem.options!!.asJsonObject;
            val weeklyTrackerCardItemBottom: JsonObject =
                weeklyTrackerCardItem.bottom!!.asJsonObject;


            cards_title.text = weeklyTrackerCardItem.title
//            cards_title.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_title.setTextColor(getResources().getColor(R.color.white))

            cards_description_text.text =
                weeklyTrackerCardItem.description!!.asJsonObject.get("text").asString;
//            cards_description_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_description_text.setTextColor(getResources().getColor(R.color.white))


            cards_options_sub_text.text = weeklyTrackerCardItemOptions.get("sub_text").asString
//            cards_options_sub_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_sub_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItem.click !== null
                && weeklyTrackerCardItem.click!!.get("action").asString != "" && weeklyTrackerCardItem.click!!.get(
                    "meta"
                ).asString != ""
            ) {
//                circular_progress_bar_dashboard_tracker.setOnClickListener {
//
//                    onItemClickListener?.circularProgressBarClick(
//                        weeklyTrackerCardItem.click!!.get(
//                            "action"
//                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
//                    )
//                }


                cards_header_layout.setOnClickListener {


                    onItemClickListener?.circularProgressBarClick(
                        weeklyTrackerCardItem.click!!.get(
                            "action"
                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                    )


                }
            }

            if (weeklyTrackerCardItem.plusbutton !== null) {

                if (!weeklyTrackerCardItem.plusbutton!!.get("action").asString.equals("") && !weeklyTrackerCardItem.plusbutton!!.get(
                        "meta"
                    ).asString.equals("")
                ) {
                    hydration_tracker_add_btn.visibility = View.VISIBLE
                    hydration_tracker_add_btn.setOnClickListener {
                        onItemClickListener?.plusButtonItemClick(
                            weeklyTrackerCardItem.plusbutton!!.get(
                                "action"
                            ).asString, weeklyTrackerCardItem.plusbutton!!.get("meta").asString
                        )
                    }
                } else {
                    hydration_tracker_add_btn.visibility = View.GONE
                }


            } else {
                hydration_tracker_add_btn.visibility = View.GONE
            }

            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == ""
            ) {
                wnw_cards_bottom_left_box_liner_layout.visibility = View.GONE
            }

            if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                wnw_cards_bottom_right_box_liner_layout.visibility = View.GONE
            }


            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                cards_bottom_linear_layout.visibility = View.GONE
            } else {
                cards_bottom_linear_layout.visibility = View.VISIBLE
            }

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                cards_bottom_left_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_center_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_right_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
            } else {
                cards_bottom_left_box_main_text.setTextColor(resources.getColor(R.color.white))
                cards_bottom_center_box_main_text.setTextColor(resources.getColor(R.color.white))
                cards_bottom_right_box_main_text.setTextColor(resources.getColor(R.color.white))
            }

            cards_bottom_left_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_left_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_center_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_center_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_right_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_right_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("sub_text").asString else ""


            if (!weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_left_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))
                cards_bottom_left_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
//                    )
                }
            }

            if (!weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_center_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))

                cards_bottom_center_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }

            if (!weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_right_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))


                cards_bottom_right_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }



            if (weeklyTrackerCardItemOptions.get("progress_header_type").asString == "icon") {
                cards_options_progress_header_type_text.visibility = View.GONE
                cards_options_progress_header_type_image.visibility = View.VISIBLE

                Glide.with(context)
                    .load(weeklyTrackerCardItemOptions.get("progress_header_data").asString)
                    .into(cards_options_progress_header_type_image)


            } else {
                cards_options_progress_header_type_text.visibility = View.VISIBLE
                cards_options_progress_header_type_image.visibility = View.GONE
                cards_options_progress_header_type_text.text =
                    weeklyTrackerCardItemOptions.get("progress_header_data").asString


                if (!weeklyTrackerCardItem.options!!.get("text_color")
                        .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                        ""
                    )
                ) {
                    cards_options_progress_header_type_text.setTextColor(
                        Color.parseColor(
                            weeklyTrackerCardItemOptions.get("text_color").asString
                        )
                    )
                } else {
                    cards_options_progress_header_type_text.setTextColor(resources.getColor(R.color.theme_color))
                }


            }


            val progressBarDrawable: LayerDrawable =
                circular_progress_bar_dashboard_tracker.getProgressDrawable() as LayerDrawable;
//            val backgroundDrawable: Drawable = progressBarDrawable.getDrawable(0);
            val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.trancperent2), PorterDuff.Mode.SRC_IN);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {

                progressDrawable.setColorFilter(
                    Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString),
                    PorterDuff.Mode.SRC_IN
                );
            } else {
                progressDrawable.setColorFilter(
                    resources.getColor(R.color.theme_color),
                    PorterDuff.Mode.SRC_IN
                );
            }




            circular_progress_bar_dashboard_tracker.setProgress(0);
            startAnimationCounter(
                circular_progress_bar_dashboard_tracker,
                0,
                weeklyTrackerCardItemOptions.get("percentage_completed").asInt
            )

            cards_options_center_text.setText(weeklyTrackerCardItemOptions.get("center_text").asString);
//            cards_options_center_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_center_text.setTextColor(getResources().getColor(R.color.white))


//            if ((weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals("brandgradient")
//                            || weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals("brandcolor"))) {

//                if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals("brandgradient")) {
//                    if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
//                        cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.theme_color));
//                    } else {
//                        cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.theme_color));
//                    }
//                }

//                else {

            cards_bottom_linear_layout.setOnClickListener {
                val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(context);

                if (weeklyTrackerCardItem.title.equals("Daily Challenge")) {
                    loggerFB.logEvent("Daily_Reward")
                }

                if (weeklyTrackerCardItem.title.equals("Weekly Challenge")) {
                    loggerFB.logEvent("Weekly_Reward")
                }

                if (weeklyTrackerCardItem.title.equals("Monthly Challenge")) {
                    loggerFB.logEvent("Monthly_Reward")
                }

                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.bottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                        "action"
                    ).asString,
                    weeklyTrackerCardItem.bottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                        "meta"
                    ).asString
                )
            }

            if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
                cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.life_plus_gradient_end));
//                cards_bottom_linear_layout.setBackgroundResource(R.drawable.life_plus_gradient_rectangle_corners)
            } else {
                cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.ayubo_gradient_end));
//                cards_bottom_linear_layout.setBackgroundResource(R.drawable.ayubo_life_gradient_rectangle_corners)
            }

//            cards_bottom_linear_layout.setBackgroundColor(getResources().getColor(R.color.theme_color));
//                }


//            } 

//            else {
//                cards_header_layout.setBackgroundColor(Color.parseColor(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString))
//            }


            if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("type").asString == "url") {
//                Glide.with(context).load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString).into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        cards_header_layout.setBackgroundDrawable(resource);
//
//                    }
//
//                });
                Glide.with(context)
                    .load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString)
                    .centerCrop().into(main_background_image_card);
            } else {


                if ((weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                        "brandgradient"
                    ) || weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                        "brandcolor"
                    ))
                ) {

                    if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                            "brandgradient"
                        )
                    ) {
                        if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
                            cards_header_layout.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient)
                        } else {
                            cards_header_layout.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient)
                        }
                    } else {
                        cards_header_layout.setBackgroundColor(getResources().getColor(R.color.theme_color));
                    }


                } else {
                    cards_header_layout.setBackgroundColor(
                        Color.parseColor(
                            weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString
                        )
                    )
                }


            }


            return@with
        }
    }

    inner class StepCountCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_tracker_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {
            val weeklyTrackerCardItemOptions: JsonObject =
                weeklyTrackerCardItem.options!!.asJsonObject;
            val weeklyTrackerCardItemBottom: JsonObject =
                weeklyTrackerCardItem.bottom!!.asJsonObject;

            print(weeklyTrackerCardItem)


            cards_title.text = weeklyTrackerCardItem.title
//            cards_title.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_title.setTextColor(getResources().getColor(R.color.white))

            cards_description_text.text =
                weeklyTrackerCardItem.description!!.asJsonObject.get("text").asString;
//            cards_description_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_description_text.setTextColor(getResources().getColor(R.color.white))

            cards_options_sub_text.text = weeklyTrackerCardItemOptions.get("sub_text").asString
//            cards_options_sub_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_sub_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItem.click !== null
                && weeklyTrackerCardItem.click!!.get("action").asString != "" && weeklyTrackerCardItem.click!!.get(
                    "meta"
                ).asString != ""
            ) {
//                circular_progress_bar_dashboard_tracker.setOnClickListener {
//
//                    onItemClickListener?.circularProgressBarClick(
//                        weeklyTrackerCardItem.click!!.get(
//                            "action"
//                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
//                    )
//                }

                cards_header_layout.setOnClickListener {

                    onItemClickListener?.circularProgressBarClick(
                        weeklyTrackerCardItem.click!!.get(
                            "action"
                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                    )
                }
            }

            if (weeklyTrackerCardItem.plusbutton !== null) {

                if (!weeklyTrackerCardItem.plusbutton!!.get("action").asString.equals("") && !weeklyTrackerCardItem.plusbutton!!.get(
                        "meta"
                    ).asString.equals("")
                ) {
                    hydration_tracker_add_btn.visibility = View.VISIBLE
                    hydration_tracker_add_btn.setOnClickListener {
                        onItemClickListener?.plusButtonItemClick(
                            weeklyTrackerCardItem.plusbutton!!.get(
                                "action"
                            ).asString, weeklyTrackerCardItem.plusbutton!!.get("meta").asString
                        )
                    }
                } else {
                    hydration_tracker_add_btn.visibility = View.GONE
                }


            } else {
                hydration_tracker_add_btn.visibility = View.GONE
            }


            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                cards_bottom_linear_layout.visibility = View.GONE
            } else {
                cards_bottom_linear_layout.visibility = View.VISIBLE
            }

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                cards_bottom_left_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_center_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_right_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
            } else {
                cards_bottom_left_box_main_text.setTextColor(resources.getColor(R.color.theme_color));
                cards_bottom_center_box_main_text.setTextColor(resources.getColor(R.color.theme_color));
                cards_bottom_right_box_main_text.setTextColor(resources.getColor(R.color.theme_color));
            }


            cards_bottom_left_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_left_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_center_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_center_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_right_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_right_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("sub_text").asString else ""


            if (!weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_left_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))
                cards_bottom_left_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
//                    )
                }
            }

            if (!weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_center_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))

                cards_bottom_center_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }

            if (!weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_right_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))


                cards_bottom_right_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }



            if (weeklyTrackerCardItemOptions.get("progress_header_type").asString == "icon") {
                cards_options_progress_header_type_text.visibility = View.GONE
                cards_options_progress_header_type_image.visibility = View.VISIBLE

                try {
                    Glide.with(context)
                        .load(weeklyTrackerCardItemOptions.get("progress_header_data").asString)
                        .into(cards_options_progress_header_type_image)
                } catch (e: Exception) {
                    e.printStackTrace();
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
                }


            } else {
                cards_options_progress_header_type_text.visibility = View.VISIBLE
                cards_options_progress_header_type_image.visibility = View.GONE
                cards_options_progress_header_type_text.text =
                    weeklyTrackerCardItemOptions.get("progress_header_data").asString

                if (!weeklyTrackerCardItem.options!!.get("text_color")
                        .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                        ""
                    )
                ) {
                    cards_options_progress_header_type_text.setTextColor(
                        Color.parseColor(
                            weeklyTrackerCardItemOptions.get("text_color").asString
                        )
                    )
                } else {
                    cards_options_progress_header_type_text.setTextColor(resources.getColor(R.color.theme_color))
                }
            }


            val progressBarDrawable: LayerDrawable =
                circular_progress_bar_dashboard_tracker.getProgressDrawable() as LayerDrawable;
//            val backgroundDrawable: Drawable = progressBarDrawable.getDrawable(0);
            val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);


            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                progressDrawable.setColorFilter(
                    Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString),
                    PorterDuff.Mode.SRC_IN
                );
            } else {
                progressDrawable.setColorFilter(
                    resources.getColor(R.color.theme_color),
                    PorterDuff.Mode.SRC_IN
                );
            }




            circular_progress_bar_dashboard_tracker.setProgress(0);
            startAnimationCounter(
                circular_progress_bar_dashboard_tracker,
                0,
                weeklyTrackerCardItemOptions.get("percentage_completed").asInt
            )

            cards_options_center_text.setText(weeklyTrackerCardItemOptions.get("center_text").asString);
//            cards_options_center_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_center_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("type").asString == "url") {
//                Glide.with(context).load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString).into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        cards_header_layout.setBackgroundDrawable(resource);
//                    }
//
//                });


                Glide.with(context)
                    .load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString)
                    .centerCrop().into(main_background_image_card)


            } else {
                cards_header_layout.setBackgroundColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get(
                            "background"
                        ).asJsonObject.get("data").asString
                    )
                )
            }


            return@with
        }
    }

    inner class WeightTrackerCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_tracker_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {
            val weeklyTrackerCardItemOptions: JsonObject =
                weeklyTrackerCardItem.options!!.asJsonObject;
            val weeklyTrackerCardItemBottom: JsonObject =
                weeklyTrackerCardItem.bottom!!.asJsonObject;

            dashboard_main_card_id.setOnClickListener {

                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get(
                        "action"
                    ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                )
            }


            cards_title.text = weeklyTrackerCardItem.title
//            cards_title.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_title.setTextColor(getResources().getColor(R.color.white))

            cards_description_text.text =
                weeklyTrackerCardItem.description!!.asJsonObject.get("text").asString;
//            cards_description_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_description_text.setTextColor(getResources().getColor(R.color.white))


            cards_options_sub_text.text = weeklyTrackerCardItemOptions.get("sub_text").asString
//            cards_options_sub_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_sub_text.setTextColor(getResources().getColor(R.color.white))



            if (weeklyTrackerCardItem.click !== null
                && weeklyTrackerCardItem.click!!.get("action").asString != "" && weeklyTrackerCardItem.click!!.get(
                    "meta"
                ).asString != ""
            ) {
//                circular_progress_bar_dashboard_tracker.setOnClickListener {
//
//                    onItemClickListener?.circularProgressBarClick(
//                        weeklyTrackerCardItem.click!!.get(
//                            "action"
//                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
//                    )
//                }


                cards_header_layout.setOnClickListener {

                    onItemClickListener?.circularProgressBarClick(
                        weeklyTrackerCardItem.click!!.get(
                            "action"
                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                    )
                }
            }

            if (weeklyTrackerCardItem.plusbutton !== null) {

                if (!weeklyTrackerCardItem.plusbutton!!.get("action").asString.equals("") && !weeklyTrackerCardItem.plusbutton!!.get(
                        "meta"
                    ).asString.equals("")
                ) {
                    hydration_tracker_add_btn.visibility = View.VISIBLE
                    hydration_tracker_add_btn.setOnClickListener {
                        onItemClickListener?.plusButtonItemClick(
                            weeklyTrackerCardItem.plusbutton!!.get(
                                "action"
                            ).asString, weeklyTrackerCardItem.plusbutton!!.get("meta").asString
                        )
                    }
                } else {
                    hydration_tracker_add_btn.visibility = View.GONE
                }


            } else {
                hydration_tracker_add_btn.visibility = View.GONE
            }


            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                cards_bottom_linear_layout.visibility = View.GONE
            } else {
                cards_bottom_linear_layout.visibility = View.VISIBLE
            }




            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                cards_bottom_left_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_center_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_right_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
            } else {
                cards_bottom_left_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
                cards_bottom_center_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
                cards_bottom_right_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
            }


            cards_bottom_left_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_left_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_center_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_center_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_right_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_right_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("sub_text").asString else ""


            if (!weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_left_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))
                cards_bottom_left_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
//                    )
                }
            }

            if (!weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_center_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))

                cards_bottom_center_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }

            if (!weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_right_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))


                cards_bottom_right_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }



            if (weeklyTrackerCardItemOptions.get("progress_header_type").asString == "icon") {
                cards_options_progress_header_type_text.visibility = View.GONE
                cards_options_progress_header_type_image.visibility = View.VISIBLE

                try {
                    Glide.with(context)
                        .load(weeklyTrackerCardItemOptions.get("progress_header_data").asString)
                        .into(cards_options_progress_header_type_image)
                } catch (e: Exception) {
                    e.printStackTrace();
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
                }


            } else {
                cards_options_progress_header_type_text.visibility = View.VISIBLE
                cards_options_progress_header_type_image.visibility = View.GONE
                cards_options_progress_header_type_text.text =
                    weeklyTrackerCardItemOptions.get("progress_header_data").asString

                if (!weeklyTrackerCardItem.options!!.get("text_color")
                        .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                        ""
                    )
                ) {
                    cards_options_progress_header_type_text.setTextColor(
                        Color.parseColor(
                            weeklyTrackerCardItemOptions.get("text_color").asString
                        )
                    )
                } else {
                    cards_options_progress_header_type_text.setTextColor(resources.getColor(R.color.theme_color))
                }
            }


            val progressBarDrawable: LayerDrawable =
                circular_progress_bar_dashboard_tracker.getProgressDrawable() as LayerDrawable;
//            val backgroundDrawable: Drawable = progressBarDrawable.getDrawable(0);
            val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.trancperent2), PorterDuff.Mode.SRC_IN);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);


            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                progressDrawable.setColorFilter(
                    Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString),
                    PorterDuff.Mode.SRC_IN
                );
            } else {
                progressDrawable.setColorFilter(
                    resources.getColor(R.color.theme_color),
                    PorterDuff.Mode.SRC_IN
                );
            }



            circular_progress_bar_dashboard_tracker.setProgress(0);
            startAnimationCounter(
                circular_progress_bar_dashboard_tracker,
                0,
                weeklyTrackerCardItemOptions.get("percentage_completed").asInt
            )

            cards_options_center_text.setText(weeklyTrackerCardItemOptions.get("center_text").asString);
//            cards_options_center_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_center_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("type").asString == "url") {
//                Glide.with(context).load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString).into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        cards_header_layout.setBackgroundDrawable(resource);
//
//                    }
//
//                });

                Glide.with(context)
                    .load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString)
                    .centerCrop().into(main_background_image_card)
            } else {
                cards_header_layout.setBackgroundColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get(
                            "background"
                        ).asJsonObject.get("data").asString
                    )
                )
            }


            return@with
        }
    }

    inner class DefaultViewCard(parent: ViewGroup) :
        WeeklyRelatedCardTrackerAdapter.BaseHolder(parent.inflate(R.layout.dashboard_tracker_card)) {
        override fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {
            val weeklyTrackerCardItemOptions: JsonObject =
                weeklyTrackerCardItem.options!!.asJsonObject;
            val weeklyTrackerCardItemBottom: JsonObject =
                weeklyTrackerCardItem.bottom!!.asJsonObject;

            dashboard_main_card_id.setOnClickListener {

                onItemClickListener?.onItemClick(
                    weeklyTrackerCardItem.click!!.get(
                        "action"
                    ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                )
            }


            cards_title.text = weeklyTrackerCardItem.title
//            cards_title.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_title.setTextColor(getResources().getColor(R.color.white))

            cards_description_text.text =
                weeklyTrackerCardItem.description!!.asJsonObject.get("text").asString;
//            cards_description_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_description_text.setTextColor(getResources().getColor(R.color.white))


            cards_options_sub_text.text = weeklyTrackerCardItemOptions.get("sub_text").asString
//            cards_options_sub_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_sub_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItem.click !== null
                && weeklyTrackerCardItem.click!!.get("action").asString != "" && weeklyTrackerCardItem.click!!.get(
                    "meta"
                ).asString != ""
            ) {
//                circular_progress_bar_dashboard_tracker.setOnClickListener {
//
//                    onItemClickListener?.circularProgressBarClick(
//                        weeklyTrackerCardItem.click!!.get(
//                            "action"
//                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
//                    )
//                }

                cards_header_layout.setOnClickListener {

                    onItemClickListener?.circularProgressBarClick(
                        weeklyTrackerCardItem.click!!.get(
                            "action"
                        ).asString, weeklyTrackerCardItem.click!!.get("meta").asString
                    )
                }
            }

            if (weeklyTrackerCardItem.plusbutton !== null) {

                if (!weeklyTrackerCardItem.plusbutton!!.get("action").asString.equals("") && !weeklyTrackerCardItem.plusbutton!!.get(
                        "meta"
                    ).asString.equals("")
                ) {
                    hydration_tracker_add_btn.visibility = View.VISIBLE
                    hydration_tracker_add_btn.setOnClickListener {
                        onItemClickListener?.plusButtonItemClick(
                            weeklyTrackerCardItem.plusbutton!!.get(
                                "action"
                            ).asString, weeklyTrackerCardItem.plusbutton!!.get("meta").asString
                        )
                    }
                } else {
                    hydration_tracker_add_btn.visibility = View.GONE
                }


            } else {
                hydration_tracker_add_btn.visibility = View.GONE
            }


            if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text").asString == "" &&
                weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text").asString == ""
            ) {
                cards_bottom_linear_layout.visibility = View.GONE
            } else {
                cards_bottom_linear_layout.visibility = View.VISIBLE
            }

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {
                cards_bottom_left_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_center_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
                cards_bottom_right_box_main_text.setTextColor(
                    Color.parseColor(
                        weeklyTrackerCardItemOptions.get("text_color").asString
                    )
                )
            } else {
                cards_bottom_left_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
                cards_bottom_center_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
                cards_bottom_right_box_main_text.setTextColor(resources.getColor(R.color.theme_color))
            }

            cards_bottom_left_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_left_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "left_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_center_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_center_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "center_box"
                ).asJsonObject.get("sub_text").asString else ""


            cards_bottom_right_box_main_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("main_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("main_text").asString else ""
            cards_bottom_right_box_sub_text.text =
                if (weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("sub_text") !== null) weeklyTrackerCardItemBottom.get(
                    "right_box"
                ).asJsonObject.get("sub_text").asString else ""


            if (!weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                try {
                    cards_bottom_left_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))
                    cards_bottom_left_box_sub_text.setOnClickListener {
                        onItemClickListener?.onItemClick(
                            weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                                "action"
                            ).asString,
                            weeklyTrackerCardItemBottom.get("left_box").asJsonObject.get("click").asJsonObject.get(
                                "meta"
                            ).asString
                        )
//                    )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
                }

            }

            if (!weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_center_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))

                cards_bottom_center_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("center_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }

            if (!weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "action"
                ).asString.equals("")
                || !weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                    "meta"
                ).asString.equals("")
            ) {
                cards_bottom_right_box_sub_text.setTextColor(context.resources.getColor(R.color.color_377DDD))


                cards_bottom_right_box_sub_text.setOnClickListener {
                    onItemClickListener?.onItemClick(
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "action"
                        ).asString,
                        weeklyTrackerCardItemBottom.get("right_box").asJsonObject.get("click").asJsonObject.get(
                            "meta"
                        ).asString
                    )
                }


            }



            if (weeklyTrackerCardItemOptions.get("progress_header_type").asString == "icon") {
                cards_options_progress_header_type_text.visibility = View.GONE
                cards_options_progress_header_type_image.visibility = View.VISIBLE
                try {
                    Glide.with(context)
                        .load(weeklyTrackerCardItemOptions.get("progress_header_data").asString)
                        .into(cards_options_progress_header_type_image)
                } catch (e: Exception) {
                    e.printStackTrace();
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
                }


            } else {
                cards_options_progress_header_type_text.visibility = View.VISIBLE
                cards_options_progress_header_type_image.visibility = View.GONE
                cards_options_progress_header_type_text.text =
                    weeklyTrackerCardItemOptions.get("progress_header_data").asString


                if (!weeklyTrackerCardItem.options!!.get("text_color")
                        .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                        ""
                    )
                ) {
                    cards_options_progress_header_type_text.setTextColor(
                        Color.parseColor(
                            weeklyTrackerCardItemOptions.get("text_color").asString
                        )
                    )
                } else {
                    cards_options_progress_header_type_text.setTextColor(resources.getColor(R.color.theme_color))
                }


            }


            val progressBarDrawable: LayerDrawable =
                circular_progress_bar_dashboard_tracker.getProgressDrawable() as LayerDrawable;
//            val backgroundDrawable: Drawable = progressBarDrawable.getDrawable(0);
            val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.trancperent2), PorterDuff.Mode.SRC_IN);
//            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);

            if (!weeklyTrackerCardItem.options!!.get("text_color")
                    .equals("brandcolor") && !weeklyTrackerCardItem.options!!.get("text_color").asString.equals(
                    ""
                )
            ) {

                progressDrawable.setColorFilter(
                    Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString),
                    PorterDuff.Mode.SRC_IN
                );
            } else {
                progressDrawable.setColorFilter(
                    resources.getColor(R.color.theme_color),
                    PorterDuff.Mode.SRC_IN
                );
            }




            circular_progress_bar_dashboard_tracker.setProgress(0);
            startAnimationCounter(
                circular_progress_bar_dashboard_tracker,
                0,
                weeklyTrackerCardItemOptions.get("percentage_completed").asInt
            )

            cards_options_center_text.setText(weeklyTrackerCardItemOptions.get("center_text").asString);
//            cards_options_center_text.setTextColor(Color.parseColor(weeklyTrackerCardItemOptions.get("text_color").asString))
            cards_options_center_text.setTextColor(getResources().getColor(R.color.white))


            if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("type").asString == "url") {
//                Glide.with(context).load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString).into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        cards_header_layout.setBackgroundDrawable(resource);
//
//                    }
//
//                });
                Glide.with(context)
                    .load(weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString)
                    .centerCrop().into(main_background_image_card)
            } else {

                if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("type").asString.equals(
                        "color"
                    )
                ) {
                    if ((weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                            "brandgradient"
                        ) || weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                            "brandcolor"
                        ))
                    ) {

                        if (weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString.equals(
                                "brandgradient"
                            )
                        ) {
                            if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
                                cards_header_layout.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient)
                            } else {
                                cards_header_layout.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient)
                            }
                        } else {
                            cards_header_layout.setBackgroundColor(getResources().getColor(R.color.theme_color));
                        }


                    } else {
                        cards_header_layout.setBackgroundColor(
                            Color.parseColor(
                                weeklyTrackerCardItemOptions.get("background").asJsonObject.get("data").asString
                            )
                        )
                    }
                }

            }
            return@with
        }
    }


    fun startAnimationCounter(trackerProgressBar: ProgressBar, start_no: Int, end_no: Int) {
        val animator: ValueAnimator = ValueAnimator.ofInt(start_no, end_no);
        animator.setDuration(2000);

        animator.addUpdateListener {
            trackerProgressBar.setProgress(Integer.parseInt(it.getAnimatedValue().toString()));
        }


        animator.start();
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(weeklyTrackerCardItem: WeeklyTrackerCardItem) = with(itemView) {

        }

        open fun release() {

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (releaseAll) {
            holder.release()
        }

        holder.bind(dataList[position])
    }


//    inner class MyXAxisValueFormatter(values: ArrayList<String>) : IAxisValueFormatter {
//
//        private val mValues: ArrayList<String> = values;
//
//
////            public MyXAxisValueFormatter(ArrayList<String> values)
////            {
////                this.mValues = values;
////            }
//
//
//        override fun getFormattedValue(value: Float, axis: AxisBase): String {
//            // "value" represents the position of the label on the axis (x or y)
//            var data: String = "";
//
//            if (mValues != null) {
//
//                if (mValues.size > 1) {
//                    if (mValues.size > value.toInt()) {
//                        data = mValues[value.toInt()];
//                    }
//                }
//            }
//            return data;
//        }
//
//    }


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
}