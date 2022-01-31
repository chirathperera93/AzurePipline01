package com.ayubo.life.ayubolife.revamp.v1.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.MyCustomMarkView
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetStepSummaryData
import com.github.mikephil.charting.charts.LineChart
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
import java.util.*

class CommonFunction {

    fun startAnimationCounter(trackerProgressBar: ProgressBar, start_no: Int, end_no: Int) {
        val animator: ValueAnimator = ValueAnimator.ofInt(start_no, end_no);
        animator.duration = 2000;
        animator.addUpdateListener {
            trackerProgressBar.progress = Integer.parseInt(it.animatedValue.toString());
        }
        animator.start();
    }

    fun fillDataToDaysMap(
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

    fun loadMyStepChart(context: Context, mainView: View, data: V1GetStepSummaryData) {

        val my_step_cart = mainView.findViewById<LineChart>(R.id.my_step_chart)


        val mySteps: ArrayList<String> = ArrayList<String>()
        val myDates: ArrayList<String> = ArrayList<String>()
        val othersSteps: ArrayList<String> = ArrayList<String>()


        for (i in 0 until data.chart_data.me.size) {
            myDates.add(data.chart_data.me[i].day)
            mySteps.add(data.chart_data.me[i].value)
        }

        for (i in 0 until data.chart_data.average.size) {
            othersSteps.add(data.chart_data.average[i].value)
        }

        val myStepsStart = mySteps[0]
        val myStepsEnd = mySteps[mySteps.size - 1]

        val othersStepsStart = othersSteps[0];
        val othersStepsEnd = othersSteps[othersSteps.size - 1];

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

        var xAxisLabels_ArrayList: ArrayList<String>? = null
        my_step_cart.clear()
        my_step_cart.description.isEnabled = false
        my_step_cart.setTouchEnabled(true);
        my_step_cart.dragDecelerationFrictionCoef = 0.9f
        my_step_cart.isDragEnabled = true
        my_step_cart.setScaleEnabled(false)
        my_step_cart.setDrawGridBackground(false)
        my_step_cart.isHighlightPerDragEnabled = true
        my_step_cart.animateX(2500)
        my_step_cart.setScaleEnabled(true)
        my_step_cart.setPinchZoom(true)


        val legend: Legend = my_step_cart.legend
        val myStepsChartValueList: ArrayList<Entry> = ArrayList<Entry>()
        val othersStepsChartValueList: ArrayList<Entry> = ArrayList<Entry>()
        var st: Int = 0
        var stForOthers: Int = 0
        xAxisLabels_ArrayList = ArrayList<String>()


        if ((myDates != null) && (myDates.size > 0)) {
            var hmap: LinkedHashMap<String, String>? = null
            var hmapForSet2: LinkedHashMap<String, String>? = null
            hmap = CommonFunction().fillDataToDaysMap(myDates, mySteps)
            hmapForSet2 = CommonFunction().fillDataToDaysMap(myDates, othersSteps)

            val it: kotlin.collections.Iterator<*> = hmap.iterator()
            val hmapForSet2Iterator: kotlin.collections.Iterator<*> = hmapForSet2.iterator()
            var count: Int = 0
            var countToOther: Int = 0

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


            while (hmapForSet2Iterator.hasNext()) {
                val entry: Map.Entry<*, *> =
                    hmapForSet2Iterator.next() as Map.Entry<*, *>
                val strSteps: String = entry.value as String
                val value: Double = strSteps.toDouble()
                val yval: Int = value.toInt()
                othersStepsChartValueList.add(
                    Entry(
                        countToOther.toFloat(),
                        yval.toFloat()
                    )
                )
                if (stForOthers < yval) {
                    stForOthers = yval
                }

                countToOther++
            }
        }

        // modify the legend ...
        legend.form = Legend.LegendForm.SQUARE
        legend.textSize = 11f
        legend.textColor = context.resources.getColor(R.color.black)
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(true)

        val xAxis: XAxis = my_step_cart.xAxis
        xAxis.textSize = 11f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = context.resources.getColor(R.color.chart_x_axis)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.axisMinimum = 0F
        xAxis.granularity = 1f
        xAxis.gridColor = context.resources.getColor(R.color.black)
        xAxis.setAxisMinValue(0F)
        my_step_cart.axisRight.isEnabled = false
        val yAxis: YAxis = my_step_cart.axisLeft
        yAxis.textColor = context.resources.getColor(R.color.color_DBDBDB)
        if (stForOthers > st) {
            yAxis.axisMaximum = stForOthers.toFloat() + 100
        } else {
            yAxis.axisMaximum = st.toFloat() + 100
        }
        yAxis.setDrawGridLines(false)
        yAxis.isGranularityEnabled = true
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yAxis.setAxisMinValue(0F)
        yAxis.setDrawGridLines(true)
        yAxis.gridColor = context.resources.getColor(R.color.calendar_week)
        yAxis.gridLineWidth = 0.5F

        val set1: LineDataSet
        val set2: LineDataSet


        if (my_step_cart.data != null &&
            my_step_cart.data.dataSetCount > 0
        ) {
            set1 = my_step_cart.getData().getDataSetByIndex(0) as LineDataSet
            set2 = my_step_cart.getData().getDataSetByIndex(0) as LineDataSet

            set1.values = myStepsChartValueList
            set2.values = othersStepsChartValueList

            if (my_step_cart.lineData != null) {
                my_step_cart.lineData.clearValues()
                my_step_cart.notifyDataSetChanged()
            }
            my_step_cart.data.notifyDataChanged()
            my_step_cart.notifyDataSetChanged()

        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(myStepsChartValueList, "set1")
            set2 = LineDataSet(othersStepsChartValueList, "set2")

            set1.setDrawIcons(false)
            set2.setDrawIcons(false)
            // draw dashed line
            set1.enableDashedLine(10f, 0f, 0f)
            set2.enableDashedLine(10f, 0f, 0f)

            set1.axisDependency = YAxis.AxisDependency.LEFT
            set2.axisDependency = YAxis.AxisDependency.LEFT

            set1.color = context.resources.getColor(R.color.theme_color)

            set2.color = context.resources.getColor(R.color.color_B3B3B3)

            set1.setCircleColor(R.color.theme_color)
            set2.setCircleColor(R.color.color_B3B3B3)

            set1.lineWidth = 2f
            set2.lineWidth = 2f

            set1.setDrawCircles(false)
            set1.setDrawValues(false)

            set2.setDrawCircles(false)
            set2.setDrawValues(false)


            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set2.mode = LineDataSet.Mode.CUBIC_BEZIER

            // set the filled area
            set1.setDrawFilled(true)
            set2.setDrawFilled(false)

            set1.fillFormatter = object : IFillFormatter {
                override fun getFillLinePosition(
                    dataSet: ILineDataSet?,
                    dataProvider: LineDataProvider?
                ): Float {
                    return my_step_cart.axisLeft.axisMinimum;
                }
            }

            if (Utils.getSDKInt() >= 18) {


                // fill drawable only supported on api level 18 and above
                var drawable: Drawable? = null

                if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {

                    drawable =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.life_plus_fade_with_white
                        )
                } else {
                    drawable =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ayubo_life_fade_with_white
                        )
                }

                set1.fillDrawable = drawable

            } else {
                set1.fillColor = context.resources.getColor(R.color.theme_color)
            }

            set1.highLightColor = context.resources.getColor(R.color.theme_color)
            set2.highLightColor = context.resources.getColor(R.color.color_B3B3B3)

            set1.setDrawCircleHole(false)
            set2.setDrawCircleHole(false)

            val data: LineData = LineData()

            data.addDataSet(set2)
            data.addDataSet(set1)

            my_step_cart.legend.isEnabled = false
            data.setValueTextColor(context.resources.getColor(R.color.black))
            data.setValueTextSize(9f)

            if (my_step_cart.lineData != null) {
                my_step_cart.lineData.clearValues()
                my_step_cart.notifyDataSetChanged()
            }
            my_step_cart.clear()
            my_step_cart.data = data
            my_step_cart.setViewPortOffsets(0F, 0F, 0F, 0F)
            my_step_cart.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            my_step_cart.notifyDataSetChanged()
            my_step_cart.invalidate()
        }

        // create marker to display box when values are selected
        val mv: MyCustomMarkView = MyCustomMarkView(context, R.layout.custom_marker_view)

        // Set the marker to the chart
        mv.chartView = my_step_cart
        my_step_cart.marker = mv
        //String setter in x-Axis
        xAxis.valueFormatter =
            com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisLabels_ArrayList)

    }


}