package com.ayubo.life.ayubolife.revamp.v1.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.MyCustomMarkView
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.model.ChartDataItem
import com.ayubo.life.ayubolife.revamp.v1.view_model.MyWeightViewModel
import com.ayubo.life.ayubolife.webrtc.App
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_weight.*
import kotlinx.android.synthetic.main.new_get_started.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set

class MyWeightActivity : BaseActivity() {

    @Inject
    lateinit var myWeightViewModel: MyWeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_weight)
        App.getInstance().appComponent.inject(this)

        mainImageViewForWeightTopic.setOnClickListener {
            finish()
        }



        weightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setWeightChartData(myWeightViewModel.weightSummaryData.chart_2_data)
            } else {
                setWeightChartData(myWeightViewModel.weightSummaryData.chart_1_data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMyWeightData()
    }

    private fun loadMyWeightData() {
        subscription.add(
            myWeightViewModel.getWeightSummary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { myWeightLoading.visibility = View.VISIBLE }
                .doOnTerminate { myWeightLoading.visibility = View.GONE }
                .doOnError { myWeightLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        try {
                            setUIData();
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    myWeightLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }

    @SuppressLint("Range")
    private fun setUIData() {
        subRow1.visibility = View.VISIBLE
        subRow2.visibility = View.VISIBLE
        subRow3.visibility = View.VISIBLE
        subRow4.visibility = View.VISIBLE
        subRow5.visibility = View.VISIBLE

        info_box_left_text.text = myWeightViewModel.weightSummaryData.info_box_left.text
        if (myWeightViewModel.weightSummaryData.info_box_left.text_color != "") {
            info_box_left_text.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_left.text_color))
        }

        info_box_left_value.text = myWeightViewModel.weightSummaryData.info_box_left.value
        if (myWeightViewModel.weightSummaryData.info_box_left.value_color != "") {
            info_box_left_value.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_left.value_color))
        }

        info_box_center_text.text = myWeightViewModel.weightSummaryData.info_box_center.text
        if (myWeightViewModel.weightSummaryData.info_box_center.text_color != "") {
            info_box_center_text.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_center.text_color))
        }

        info_box_center_value.text = myWeightViewModel.weightSummaryData.info_box_center.value
        if (myWeightViewModel.weightSummaryData.info_box_center.value_color != "") {
            info_box_center_value.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_center.value_color))
        }


        info_box_right_text.text = myWeightViewModel.weightSummaryData.info_box_right.text
        if (myWeightViewModel.weightSummaryData.info_box_right.text_color != "") {
            info_box_right_text.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_right.text_color))
        }

        info_box_right_value.text = myWeightViewModel.weightSummaryData.info_box_right.value
        if (myWeightViewModel.weightSummaryData.info_box_right.value_color != "") {
            info_box_right_value.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.info_box_right.value_color))
        }

        center_button.text = myWeightViewModel.weightSummaryData.center_button.text
        if (myWeightViewModel.weightSummaryData.center_button.text_color != "") {
            center_button.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.center_button.text_color))

        }

        if (myWeightViewModel.weightSummaryData.center_button.btn_color != "") {
//            center_button.setBackgroundColor(Color.parseColor(myWeightViewModel.weightSummaryData.center_button.btn_color))

            val drawable: Drawable? = ContextCompat.getDrawable(
                baseContext,
                R.drawable.ayubo_life_all_corners_rounded_gradient
            )
            DrawableCompat.setTint(
                drawable!!.mutate(),
                Color.parseColor(myWeightViewModel.weightSummaryData.center_button.btn_color)
            )

            center_button.setBackgroundDrawable(drawable)
        }

        center_button.setOnClickListener {
            processAction(
                myWeightViewModel.weightSummaryData.center_button.action,
                myWeightViewModel.weightSummaryData.center_button.meta
            )
        }



        data_left_title.text = myWeightViewModel.weightSummaryData.data_left.title
        if (myWeightViewModel.weightSummaryData.data_left.title_color != "") {
            data_left_title.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_left.title_color))
        }
        data_left_value.text = myWeightViewModel.weightSummaryData.data_left.value
        if (myWeightViewModel.weightSummaryData.data_left.value_color != "") {
            data_left_value.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_left.value_color))
        }
        data_left_footer_text.text = myWeightViewModel.weightSummaryData.data_left.footer_text
        if (myWeightViewModel.weightSummaryData.data_left.footer_text_color != "") {
            data_left_footer_text.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_left.footer_text_color))
        }


        data_right_title.text = myWeightViewModel.weightSummaryData.data_right.title
        if (myWeightViewModel.weightSummaryData.data_right.title_color != "") {
            data_right_title.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_right.title_color))
        }
        data_right_value.text = myWeightViewModel.weightSummaryData.data_right.value
        if (myWeightViewModel.weightSummaryData.data_right.value_color != "") {
            data_right_value.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_right.value_color))
        }
        data_right_footer_text.text = myWeightViewModel.weightSummaryData.data_right.footer_text
        if (myWeightViewModel.weightSummaryData.data_right.footer_text_color != "") {
            data_right_footer_text.setTextColor(Color.parseColor(myWeightViewModel.weightSummaryData.data_right.footer_text_color))
        }

        chart_data_1_text.text = myWeightViewModel.weightSummaryData.chart_1_data.name
        chart_data_2_text.text = myWeightViewModel.weightSummaryData.chart_2_data.name

        setWeightChartData(myWeightViewModel.weightSummaryData.chart_1_data)

    }

    private fun setWeightChartData(chartDataItem: ChartDataItem) {
        println(chartDataItem)

        val myWeights: ArrayList<String> = ArrayList<String>()
        val myDates: ArrayList<String> = ArrayList<String>()

        for (i in 0 until chartDataItem.data.size) {
            val weightData = chartDataItem.data[i]
            myDates.add(weightData.date)
            myWeights.add(weightData.value)
        }

        var xAxisLabels_ArrayList: ArrayList<String>? = null
        weight_line_cart.clear()
        // no description text
        weight_line_cart.description.isEnabled = false
        // enable touch gestures
        weight_line_cart.setTouchEnabled(true)
        weight_line_cart.dragDecelerationFrictionCoef = 0.9f
        // enable scaling and dragging
        weight_line_cart.isDragEnabled = true
        weight_line_cart.setScaleEnabled(false)
        weight_line_cart.setDrawGridBackground(false)
        weight_line_cart.isHighlightPerDragEnabled = true
        weight_line_cart.animateX(2500)
        weight_line_cart.setScaleEnabled(true)
        weight_line_cart.setPinchZoom(true)
        val legend: Legend = weight_line_cart.legend
        val myStepsChartValueList: ArrayList<Entry> = ArrayList<Entry>()
        var st: Int = 0
        val stForOthers: Int = 0
        xAxisLabels_ArrayList = ArrayList<String>()

        if ((myDates != null) && (myDates.size > 0)) {
            var hmap: LinkedHashMap<String, String>? = null
            hmap = fillDataToDaysMap(myDates, myWeights)
            val it: kotlin.collections.Iterator<*> = hmap.iterator()
            var count: Int = 0
            while (it.hasNext()) {
                val entry: Map.Entry<*, *> = it.next() as Map.Entry<*, *>
                val strDate: String = entry.key as String
                val strSteps: String = entry.value as String
                val value: Double = strSteps.toDouble()
                val yval: Int = value.toInt()
                myStepsChartValueList.add(Entry(count.toFloat(), yval.toFloat()))
                val currentDateStr: String = strDate
                xAxisLabels_ArrayList.add(currentDateStr)

                if (st < yval) {
                    st = yval
                }

                count++
            }
        }

        // modify the legend ...
        legend.form = Legend.LegendForm.SQUARE
        legend.textSize = 11f
        legend.textColor = resources.getColor(R.color.black)
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(true)
        val xAxis: XAxis = weight_line_cart.xAxis
        xAxis.textSize = 11f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = resources.getColor(R.color.chart_x_axis)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.axisMinimum = 0F
        xAxis.granularity = 1f
        xAxis.gridColor = resources.getColor(R.color.black)
        xAxis.setAxisMinValue(0F)
        weight_line_cart.axisRight.isEnabled = false
        val yAxis: YAxis = weight_line_cart.axisLeft
        yAxis.textColor = resources.getColor(R.color.color_DBDBDB)

        if (stForOthers > st) {
            yAxis.axisMaximum = stForOthers.toFloat() + 10
        } else {
            yAxis.axisMaximum = st.toFloat() + 10
        }
        yAxis.setDrawGridLines(false)
        yAxis.isGranularityEnabled = true
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yAxis.setAxisMinValue(0F)
        yAxis.setDrawGridLines(true)
        yAxis.gridColor = resources.getColor(R.color.calendar_week)
        yAxis.gridLineWidth = 0.5F
        val set1: LineDataSet
        if (weight_line_cart.data != null &&
            weight_line_cart.data.dataSetCount > 0
        ) {
            set1 = weight_line_cart.data.getDataSetByIndex(0) as LineDataSet

            set1.values = myStepsChartValueList

            if (weight_line_cart.lineData != null) {
                weight_line_cart.lineData.clearValues()
                weight_line_cart.notifyDataSetChanged()
            }
            weight_line_cart.data.notifyDataChanged()
            weight_line_cart.notifyDataSetChanged()

        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(myStepsChartValueList, "set1")

            set1.setDrawIcons(false)
            // draw dashed line
            set1.enableDashedLine(10f, 0f, 0f)
            set1.axisDependency = YAxis.AxisDependency.LEFT
            set1.color = resources.getColor(R.color.theme_color)
            set1.setCircleColor(R.color.theme_color)
            set1.lineWidth = 2f
            set1.setDrawCircles(false)
            set1.setDrawValues(false)
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.setDrawFilled(true)
            set1.fillFormatter = object : IFillFormatter {
                override fun getFillLinePosition(
                    dataSet: ILineDataSet?,
                    dataProvider: LineDataProvider?
                ): Float {
                    return weight_line_cart.axisLeft.axisMinimum;
                }
            }
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                var drawable: Drawable? = null
                if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {

                    drawable =
                        ContextCompat.getDrawable(
                            baseContext,
                            R.drawable.life_plus_fade_with_white
                        )
                } else {
                    drawable =
                        ContextCompat.getDrawable(
                            baseContext,
                            R.drawable.ayubo_life_fade_with_white
                        )
                }
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = resources.getColor(R.color.theme_color)
            }
            set1.highLightColor = resources.getColor(R.color.theme_color)
            set1.setDrawCircleHole(false)
            val data: LineData = LineData()
            data.addDataSet(set1)
            weight_line_cart.legend.isEnabled = false
            data.setValueTextColor(resources.getColor(R.color.black))
            data.setValueTextSize(9f)
            if (weight_line_cart.lineData != null) {
                weight_line_cart.lineData.clearValues()
                weight_line_cart.notifyDataSetChanged()
            }
            weight_line_cart.clear()
            weight_line_cart.data = data
            weight_line_cart.setViewPortOffsets(0F, 0F, 0F, 0F)
            weight_line_cart.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            weight_line_cart.notifyDataSetChanged()
            weight_line_cart.invalidate()
        }

        // create marker to display box when values are selected
        val mv: MyCustomMarkView = MyCustomMarkView(baseContext, R.layout.custom_marker_view)
        // Set the marker to the chart
        mv.chartView = weight_line_cart
        weight_line_cart.marker = mv
        xAxis.valueFormatter =
            com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisLabels_ArrayList)
    }

    private fun fillDataToDaysMap(
        myDates: List<String>,
        mySteps: List<String>
    ): LinkedHashMap<String, String> {
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap<String, String>()
        for (i in myDates.indices) {
            val serverDate: String = myDates[i]
            linkedHashMap[serverDate] = mySteps[i]
        }
        return linkedHashMap
    }
}