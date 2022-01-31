package com.ayubo.life.ayubolife.revamp.v1.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.ActionMetaForFragment
import com.ayubo.life.ayubolife.revamp.v1.common.CommonFunction
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetStepSummaryData
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetStepSummaryResponse
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_last_year_step.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [LastYearStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LastYearStepFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mainView: View
    lateinit var mainResponse: V1GetStepSummaryData
    lateinit var appToken: String
    lateinit var pref: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mainView = inflater.inflate(R.layout.fragment_last_year_step, container, false)
        pref = PrefManager(context)
        appToken = pref.userToken
        loadData();

        // Inflate the layout for this fragment
        return mainView
    }

    private fun loadData() {
        mainView.myLastYearStepSummaryLoading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);
        val timeZone: TimeZone = TimeZone.getDefault();
        val timeZoneId = timeZone.id;
        val device_modal: String = Build.MODEL;
        val call: Call<V1GetStepSummaryResponse> = apiService.v1NewGetStepSummary(
            AppConfig.APP_BRANDING_ID,
            appToken,
            "lastyear",
            System.currentTimeMillis(),
            timeZoneId,
            device_modal
        )


        call.enqueue(object : Callback<V1GetStepSummaryResponse> {
            override fun onResponse(
                call: Call<V1GetStepSummaryResponse>,
                response: Response<V1GetStepSummaryResponse>
            ) {
                mainView.myLastYearStepSummaryLoading.visibility = View.GONE

                if (response.isSuccessful) {
                    print(response)
                    mainResponse = response.body()!!.data

                    mainView.distance.text = mainResponse.analysis.distance
                    mainView.calories.text = mainResponse.analysis.calories
                    mainView.total_points.text = mainResponse.analysis.total_points
                    mainView.energy.text = mainResponse.analysis.energy
                    mainView.achivements.text = mainResponse.analysis.achivements
                    mainView.title.text = mainResponse.title
                    mainView.total_steps.text = mainResponse.analysis.circle_centre
                    mainView.summary.text = mainResponse.summary

                    Glide.with(requireContext())
                        .load(mainResponse.analysis.circle_top_img_url)
                        .into(mainView.total_step_icon)

                    mainView.view_step_summary.setOnClickListener {
                        try {
                            ActionMetaForFragment().processAction(
                                requireContext(),
                                mainResponse.detailed_view.action,
                                mainResponse.detailed_view.meta
                            )
                        } catch (e: Exception) {0
                            e.printStackTrace()
                        }
                    }

                    CommonFunction().loadMyStepChart(
                        requireContext(),
                        mainView,
                        mainResponse
                    )


                } else {
                    Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT)
                        .show()
                }


            }

            override fun onFailure(call: Call<V1GetStepSummaryResponse>, t: Throwable) {
                mainView.myLastYearStepSummaryLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        print(isVisibleToUser)

        if (isVisibleToUser) {
            setBarChartData(mainResponse)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "Range")
    private fun setBarChartData(mainResponse: V1GetStepSummaryData) {


        mainView.barLastYearChart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mainView.barLastYearChart.setMaxVisibleValueCount(40)

        // scaling can now only be done on x- and y-axis separately
        mainView.barLastYearChart.setPinchZoom(false)

        mainView.barLastYearChart.setDrawGridBackground(false)
        mainView.barLastYearChart.setDrawBarShadow(false)

        mainView.barLastYearChart.setDrawValueAboveBar(false)
        mainView.barLastYearChart.isHighlightFullBarEnabled = false

        // change the position of the y-labels
        val leftAxis: YAxis = mainView.barLastYearChart.axisLeft

        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        mainView.barLastYearChart.axisRight.isEnabled = false

        val xLabels: XAxis = mainView.barLastYearChart.xAxis
        xLabels.position = XAxis.XAxisPosition.BOTTOM

        val labels = ArrayList<String>()
        for (item in mainResponse.chart_data.me) {
            labels.add(item.day)
        }
        xLabels.valueFormatter = IndexAxisValueFormatter(labels)

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        // setting data
        val values_non_achieved: ArrayList<BarEntry> = ArrayList()
        val values_achieved: ArrayList<BarEntry> = ArrayList()
        val values_target: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until mainResponse.chart_data.me.size) {

            val me = mainResponse.chart_data.me[i]

            values_target.add(
                BarEntry(
                    i.toFloat(),
                    floatArrayOf(mainResponse.target.toFloat())
                )
            )

            if (me.value.toInt() >= mainResponse.target) {
                values_achieved.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(me.value.toFloat())
                    )
                )
                values_non_achieved.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(0F)
                    )
                )
            } else {
                values_achieved.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(0F)
                    )
                )
                values_non_achieved.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(me.value.toFloat())
                    )
                )
            }
        }

        val set2 = BarDataSet(values_non_achieved, "")
        set2.setDrawIcons(false)
        set2.color = Color.parseColor(mainResponse.chart_fail_color)

        val setTarget = BarDataSet(values_target, "")
        setTarget.setDrawIcons(false)
        setTarget.color = requireContext().resources.getColor(R.color.gray)

        val set1 = BarDataSet(values_achieved, "")
        set1.setDrawIcons(false)
        set1.color = Color.parseColor(mainResponse.chart_success_color)

//        set1.stackLabels = arrayOf("Achieved", "Target")

        val dataSets: ArrayList<IBarDataSet> = ArrayList()

        dataSets.add(setTarget)
        dataSets.add(set2)
        dataSets.add(set1)

        val data = BarData(dataSets)
        data.setValueTextColor(Color.WHITE)

        mainView.barLastYearChart.data = data
        mainView.barLastYearChart.setFitBars(true)
        mainView.barLastYearChart.invalidate()


        val l: Legend = mainView.barLastYearChart.legend
        l.isEnabled = false

        // add a nice and smooth animation
        mainView.barLastYearChart.animateY(1500)

//         chart.setDrawLegend(false);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LastYearStepFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LastYearStepFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}