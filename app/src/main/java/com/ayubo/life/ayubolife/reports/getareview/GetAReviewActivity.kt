package com.ayubo.life.ayubolife.reports.getareview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.upload.UploadReportActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsLogger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ask.proDialog_ask
import kotlinx.android.synthetic.main.activity_ask_get_a_review.*
import java.util.*
import javax.inject.Inject


class GetAReviewActivity : BaseActivity(), GetAReviewAdapter.OnItemClickListener {

    override fun onNextScreen(action: ReviewExperts) {
        UploadReportActivity.startActivity(
            this,
            action.consultant_name,
            action.profile_picture,
            action.contact_id,
            action.review_charge,
            action.speciality,
            ""
        )
    }

    lateinit var getAReviewAdapter: GetAReviewAdapter

    lateinit var dataList: ArrayList<ReviewData>
    var filtereddataList: ArrayList<Any>? = null
    private var moviewList: ArrayList<ReviewExperts>? = null
    var width: Int = 0
    private var dummyMainList: ArrayList<ReviewData>? = null

    @Inject
    lateinit var getAReviewVM: GetAReviewVM

    //    companion object {
//        fun startActivity(context: Context?){
//            context!!.startActivity(Intent(context, GetAReviewActivity::class.java))
//        }
//    }
    companion object {
        fun startActivity(context: Context?, filtereKey: String) {
            val intent = Intent(context, GetAReviewActivity::class.java)
            intent.putExtra("filtereKey", filtereKey)
            context!!.startActivity(intent)
        }
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("filtereKey")) {
            val filtereKey = bundle.getSerializable("filtereKey") as String
            val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(baseContext);

            if (filtereKey.equals("general")) {
                loggerFB.logEvent("Doc_Service_RR")
                SplashScreen.firebaseAnalytics.logEvent(
                    "Doc_Service_RR",
                    null
                );
            } else if (filtereKey.equals("nutri")) {
                loggerFB.logEvent("Nutri_Service_RR")
                SplashScreen.firebaseAnalytics.logEvent(
                    "Nutri_Service_RR",
                    null
                );
            }

            getExpertsForReview(filtereKey)

        } else {
            getExpertsForReview("")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_get_a_review)

        App.getInstance().appComponent.inject(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        UIinit()

        readExtras()

    }

    private fun updateView(dataList: ArrayList<ReviewData>) {


        layout_recomonded_programs_ingar.removeAllViews()

        for (c in 0 until dataList.size) {

            val reviewMainObj: ReviewData = dataList[c]


            val conetentViewMain = LinearLayout(this)
            conetentViewMain.orientation = LinearLayout.VERTICAL

            val horizontalScrollView = HorizontalScrollView(this)

            val conetentViewSub = LinearLayout(this)
            conetentViewSub.orientation = LinearLayout.HORIZONTAL

            val sectionHeaderText = TextView(this)
            sectionHeaderText.text = reviewMainObj.category
            sectionHeaderText.setPadding(0, 20, 5, 5)
            sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
            val typeFace: Typeface? =
                ResourcesCompat.getFont(this.applicationContext, R.font.roboto_bold)
            sectionHeaderText.typeface = typeFace
            sectionHeaderText.setTextColor(Color.parseColor("#000000"))
            horizontalScrollView.addView(conetentViewSub)

            conetentViewMain.addView(sectionHeaderText)
            conetentViewMain.addView(horizontalScrollView)

            for (i in 0 until reviewMainObj.experts.size) {


                val linearLayout = LinearLayout(this)
                val prams = ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = prams
                linearLayout.orientation = LinearLayout.HORIZONTAL

                val secondObj: ReviewExperts = reviewMainObj.experts[i]
                var inflater: LayoutInflater? = null
                inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                assert(inflater != null)
                val cell = inflater!!.inflate(R.layout.recomonded_program_cell, null)
                val imgImage = cell.findViewById(R.id.img_image_rpc) as ImageView
                val txt_program_button = cell.findViewById(R.id.txt_program_button) as TextView
                val txtProgramHeading = cell.findViewById(R.id.txt_program_heading) as TextView
                val txtProgramDesc = cell.findViewById(R.id.txt_program_desc) as TextView
                val txt_offer = cell.findViewById(R.id.txt_offer) as TextView
                val offer_container = cell.findViewById(R.id.offer_container) as LinearLayout

                txtProgramHeading.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setTextColor(Color.parseColor("#ffffff"))
                txtProgramDesc.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setBackgroundResource(R.drawable.program_status_bg_green)

                txtProgramHeading.text = secondObj.consultant_name

                offer_container.visibility = View.GONE
                txt_program_button.visibility = View.GONE

                txtProgramDesc.text = secondObj.speciality

                var urlImage = secondObj.profile_picture

                urlImage = urlImage.replace("zoom_level", "xxxhdpi")

                Log.d("=======Img=====", urlImage)
                Glide.with(this).load(urlImage).into(imgImage)
                imgImage.alpha = 0.6F
                val w = (width * 39) / 100
                val h = (width * 43) / 100
                Log.d("wwwwww", width.toString())
                Log.d("Sub Tile", w.toString())
                val lp = LinearLayout.LayoutParams(w, h)
                lp.setMargins(0, 0, 4, 0)
                cell.layoutParams = lp

                cell.tag = secondObj
                cell.setOnClickListener {
                    val obj = cell.tag as ReviewExperts
                    onNextScreen(obj)
                }
                conetentViewSub.addView(cell)
            }


            layout_recomonded_programs_ingar.addView(conetentViewMain)
        }
    }

    private fun getExpertsForReview(filterKey: String) {

        subscription.add(getAReviewVM.getExpertsForReview(filterKey).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog_ask.visibility = View.VISIBLE }
            .doOnTerminate { proDialog_ask.visibility = View.GONE }
            .doOnError { proDialog_ask.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {


                    dataList = getAReviewVM.askDataList!!
                    if (dataList.size > 0) {
                        moviewList = ArrayList<ReviewExperts>()


                        for (i in 0 until dataList.size) {
                            val datalis = dataList[i].experts
                            moviewList!!.addAll(datalis)
                        }

                        updateView(dataList)
                    } else {
                        layout_scrolling_programs_ingar.visibility = View.GONE
                        edt_search_value_gar.visibility = View.GONE
                        report_review_empty_linear_layout.visibility = View.VISIBLE
                    }


//                        }
                } else {
                    proDialog_ask.visibility = View.GONE
                    showMessage("Failed loading data")
                }
            }, {
                proDialog_ask.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }

    fun UIinit() {

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext);
        width = deviceScreenDimension.displayWidth

        edt_search_value_gar!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (edt_search_value_gar.text.isNotEmpty()) {

                    var filteredList = ArrayList<ReviewExperts>();

//                    val longerThan3 = moviewList!!
//                            .filter {
//
//                                it.consultant_name.contains(edt_search_value_gar.text, true) || it.speciality.contains(edt_search_value_gar.text, true)
//
//                            }


                    moviewList?.forEach { it ->
                        if (it.consultant_name != null && it.speciality != null) {
                            if (it.consultant_name.contains(
                                    edt_search_value_gar.text,
                                    true
                                ) || it.speciality.contains(edt_search_value_gar.text, true)
                            ) {
                                filteredList.add(it);
                            }
                        } else if (it.consultant_name != null) {
                            if (it.consultant_name.contains(edt_search_value_gar.text, true)) {
                                filteredList.add(it);
                            }
                        } else if (it.speciality != null) {
                            if (it.speciality.contains(edt_search_value_gar.text, true)) {
                                filteredList.add(it);
                            }
                        }

                    }

                    filteredList.sortedBy { it.consultant_name }

                    dummyMainList = ArrayList<ReviewData>()
                    var obj: ReviewData =
                        ReviewData("Results", filteredList as ArrayList<ReviewExperts>)
                    dummyMainList!!.add(obj)
                    updateView(dummyMainList!!)
                } else {
                    updateView(getAReviewVM.askDataList!!)
                }
                // updateView(dataList)
            }

            override fun afterTextChanged(editable: Editable) {}
        })


        back_layout_Health.setOnClickListener {
            finish()
        }
        img_backBtn_Health.setOnClickListener {
            finish()
        }

        Ram.setSelectedReportIDList(null)
    }


}
