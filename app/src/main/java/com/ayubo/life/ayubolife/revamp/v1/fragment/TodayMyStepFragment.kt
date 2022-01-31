package com.ayubo.life.ayubolife.revamp.v1.fragment

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.MyCustomMarkView
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.fragment_today_my_step.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodayMyStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodayMyStepFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var appToken: String
    lateinit var pref: PrefManager
    lateinit var mainView: View;

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

        mainView = inflater.inflate(R.layout.fragment_today_my_step, container, false)
        pref = PrefManager(context)
        appToken = pref.userToken
        loadData();

        // Inflate the layout for this fragment
        return mainView
    }

    private fun loadData() {
        mainView.myStepSummaryLoading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);
        val timeZone: TimeZone = TimeZone.getDefault();
        val timeZoneId = timeZone.id;
        val device_modal: String = Build.MODEL;
        val call: Call<V1GetStepSummaryResponse> = apiService.v1NewGetStepSummary(
            AppConfig.APP_BRANDING_ID,
            appToken,
            "today",
            System.currentTimeMillis(),
            timeZoneId,
            device_modal
        )


        call.enqueue(object : Callback<V1GetStepSummaryResponse> {
            override fun onResponse(
                call: Call<V1GetStepSummaryResponse>,
                response: Response<V1GetStepSummaryResponse>
            ) {
                mainView.myStepSummaryLoading.visibility = View.GONE

                if (response.isSuccessful) {
                    print(response)

                    Glide.with(requireContext())
                        .load(response.body()!!.data.analysis.circle_top_img_url)
                        .into(mainView.circle_top_img_url)
                    mainView.circle_centre.text = response.body()!!.data.analysis.circle_centre
                    mainView.circle_bottom.text = response.body()!!.data.analysis.circle_bottom

                    // circle set left card
                    val progressBarDrawable: LayerDrawable =
                        mainView.myStepCircle.progressDrawable as (LayerDrawable)
                    val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);


                    progressDrawable.setColorFilter(
                        requireContext().resources.getColor(R.color.theme_color),
                        PorterDuff.Mode.SRC_IN
                    )
                    mainView.myStepCircle.progress = 0
                    CommonFunction().startAnimationCounter(
                        mainView.myStepCircle,
                        0,
                        response.body()!!.data.analysis.percentage
                    )

                    mainView.distance.text = response.body()!!.data.analysis.distance
                    mainView.calories.text = response.body()!!.data.analysis.calories
                    mainView.total_points.text = response.body()!!.data.analysis.total_points
                    mainView.energy.text = response.body()!!.data.analysis.energy
                    mainView.achivements.text = response.body()!!.data.analysis.achivements
                    mainView.title.text = response.body()!!.data.title
                    mainView.total_steps.text = response.body()!!.data.analysis.circle_centre
                    Glide.with(requireContext())
                        .load(response.body()!!.data.analysis.circle_top_img_url)
                        .into(mainView.my_steps_icon)
                    mainView.summary.text = response.body()!!.data.summary



                    mainView.viewMyStepSummaryButton.setOnClickListener {
                        try {
                            ActionMetaForFragment().processAction(
                                requireContext(),
                                response.body()!!.data.detailed_view.action,
                                response.body()!!.data.detailed_view.meta
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    CommonFunction().loadMyStepChart(
                        requireContext(),
                        mainView,
                        response.body()!!.data
                    )


                } else {
                    Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT)
                        .show()
                }


            }

            override fun onFailure(call: Call<V1GetStepSummaryResponse>, t: Throwable) {
                mainView.myStepSummaryLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT).show()
            }

        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodayMyStepFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayMyStepFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}