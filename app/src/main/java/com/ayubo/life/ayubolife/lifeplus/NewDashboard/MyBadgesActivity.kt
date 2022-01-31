package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.my_badges.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBadgesActivity : AppCompatActivity() {
    lateinit var appToken: String;
    lateinit var pref: PrefManager;
    lateinit var dashboardMainData: JsonArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_badges)
        pref = PrefManager(baseContext);
        appToken = pref.getUserToken();



        img_back_btn_badges.setOnClickListener {
            finish();
        }
    }

    override fun onResume() {
        super.onResume()
        val task = getMyBadges();
        task.execute();
    }

    internal inner class getMyBadges : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {


//            val myBadgeLoading = findViewById<ProgressAyubo>(R.id.my_badges_loading)


            this@MyBadgesActivity.runOnUiThread(java.lang.Runnable {
                my_badges_loading.visibility = View.VISIBLE
            })


            val apiService: ApiInterface = ApiClient.getAzureApiClientV1ForDashBoard().create(ApiInterface::class.java);
            val getAllBadgesByUser: Call<ProfileDashboardResponseData> = apiService.getAllBadgesByUser(AppConfig.APP_BRANDING_ID, appToken);
            getAllBadgesByUser.enqueue(object : Callback<ProfileDashboardResponseData> {
                override fun onResponse(call: Call<ProfileDashboardResponseData>, response: Response<ProfileDashboardResponseData>) {
                    my_badges_loading.visibility = View.GONE

                    if (response.isSuccessful) {
                        System.out.println(response)
                        layout_badges_linear_layout.removeAllViews()
                        dashboardMainData = Gson().toJsonTree(response.body()!!.data).asJsonArray;
                        System.out.println(dashboardMainData)
                        val badgesTypesDataList: ArrayList<String>? = ArrayList<String>()
                        val badgesDataList = ArrayList<MyBadgeObject>()

                        for (c in 0 until dashboardMainData.size()) {
                            val data = dashboardMainData[c]
                            if (badgesTypesDataList!!.size > 0) {
                                if (!badgesTypesDataList.contains(data.asJsonObject.get("type").asString)) {
                                    badgesTypesDataList.add(data.asJsonObject.get("type").asString)
                                }
                            } else {
                                badgesTypesDataList.add(data.asJsonObject.get("type").asString)

                            }
                        }

                        for (n in 0 until badgesTypesDataList!!.size) {
                            val badgeType = badgesTypesDataList[n]
                            val myBadgeDataList: ArrayList<MyBadge> = ArrayList<MyBadge>()
                            for (i in 0 until dashboardMainData.size()) {
                                val data = dashboardMainData[i]
                                if (dashboardMainData[i].asJsonObject.get("type").asString == badgeType) {
                                    val consultant = MyBadge(
                                            data.asJsonObject.get("id").asString,
                                            data.asJsonObject.get("title").asString,
                                            data.asJsonObject.get("description").asString,
                                            data.asJsonObject.get("type").asString,
                                            data.asJsonObject.get("image_url_active").asString,
                                            data.asJsonObject.get("image_url_inactive").asString,
                                            data.asJsonObject.get("app_id").asString,
                                            data.asJsonObject.get("status").asString);

                                    myBadgeDataList.add(consultant)
                                }
                            }

                            val singleCategoryBadgeList = MyBadgeObject(badgeType, myBadgeDataList)
                            badgesDataList.add(singleCategoryBadgeList)
                        }


                        System.out.println(badgesDataList)

                        for (c in 0 until badgesDataList.size) {
                            val innerScrolledList: MyBadgeObject = badgesDataList[c]
                            val badgeTypeMainView = LinearLayout(baseContext)
                            badgeTypeMainView.orientation = LinearLayout.VERTICAL
                            val params = ViewGroup.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT)
                            badgeTypeMainView.layoutParams = params
                            badgeTypeMainView.setPadding(0, 0, 0, 16)
                            val sectionHeaderText = TextView(baseContext)
                            sectionHeaderText.setText(innerScrolledList.type)
                            sectionHeaderText.setTextSize(18F)
                            sectionHeaderText.setTextColor(getResources().getColor(R.color.color_3B3B3B))
                            sectionHeaderText.setPadding(0, 0, 0, 8)

                            val typeface: Typeface? = ResourcesCompat.getFont(baseContext, R.font.montserrat_bold);
                            sectionHeaderText.setTypeface(typeface);

                            badgeTypeMainView.addView(sectionHeaderText)


//                        val linearLayoutForImageView = LinearLayout(baseContext)
//                        val prams = ViewGroup.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT)
//                        linearLayoutForImageView.layoutParams = prams
//                        linearLayoutForImageView.orientation = LinearLayout.HORIZONTAL
//                        val displayMetrics = DisplayMetrics()
//                        windowManager.defaultDisplay.getMetrics(displayMetrics)
//                        val width = displayMetrics.widthPixels

//                        val layparam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width / 3, width / 3);
//                        layparam.setMargins(0, 0, 0, 0);
                            val series = ArrayList<MyBadge>()
                            val recyclerView = RecyclerView(baseContext)
                            for (i in 0 until innerScrolledList.myBadgeList!!.size) {

                                val secondObj: MyBadge = innerScrolledList.myBadgeList[i]
//                            val imageView = ImageView(baseContext)

//                            Glide.with(baseContext).load(secondObj.image_url).into(imageView)
//                            imageView.layoutParams = layparam;
//                            imageView.setOnClickListener {
//                                System.out.println(secondObj.id)
//                            }
                                series.add(secondObj)
//                            linearLayoutForImageView.addView(imageView)


                            }


                            val myBadgeAdapter = MyBadgeAdapter(this@MyBadgesActivity, series, windowManager)
                            val gridLayoutManager = GridLayoutManager(applicationContext, 3)
                            recyclerView.layoutManager = gridLayoutManager

                            recyclerView.apply {
                                layoutManager = gridLayoutManager
                                adapter = myBadgeAdapter
                            }



                            badgeTypeMainView.addView(recyclerView)
                            layout_badges_linear_layout.addView(badgeTypeMainView)

                        }

                        Handler(Looper.getMainLooper()).postDelayed({
                            for (c in 0 until dashboardMainData.size()) {

                                openBadgeDataPopUp(dashboardMainData[c]);
                            }
                        }, 2500)

                    } else {
                    }
                }

                override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                    my_badges_loading.visibility = View.GONE
                }

            });
            return null;
        }


        fun openBadgeDataPopUp(badgeElement: JsonElement) {

            if (badgeElement.asJsonObject.get("read_badge") != null && !badgeElement.asJsonObject.get("read_badge").asBoolean) {


                val progressDialogForSubscribe: ProgressDialog;
                progressDialogForSubscribe = ProgressDialog(this@MyBadgesActivity);
                progressDialogForSubscribe.show();
                progressDialogForSubscribe.window?.setGravity(Gravity.CENTER);
                progressDialogForSubscribe.setContentView(R.layout.badge_pop_up);

                progressDialogForSubscribe.window?.setBackgroundDrawableResource(
                        android.R.color.transparent
                );


                var actualImageString = "";
                var instructionDetail = "";
                var congratulationText = "";

                if (badgeElement.asJsonObject.get("status").asString.equals("active")) {
                    actualImageString = badgeElement.asJsonObject.get("image_url_active").asString
                    instructionDetail = "Continue good works"
                    congratulationText = "Congratulations !"
                    progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility = View.VISIBLE


                } else {
                    instructionDetail = ""
                    congratulationText = ""
                    actualImageString = badgeElement.asJsonObject.get("image_url_inactive").asString;
                    progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility = View.GONE
                }
                progressDialogForSubscribe.window?.findViewById<ImageView>(R.id.badgeImageView)?.let { it1 ->
                    baseContext?.let {
                        Glide.with(it).load(actualImageString).into(it1)
                    }
                }
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para1)?.setText(badgeElement.asJsonObject.get("description").asString)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para2)?.setText(badgeElement.asJsonObject.get("title").asString)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_congratulation_textview)?.setText(congratulationText)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para3)?.setText(instructionDetail)
                progressDialogForSubscribe.window?.findViewById<TextView>(R.id.view_my_badges)?.setOnClickListener {

                    this@MyBadgesActivity?.let {
                        val intent = Intent(it, MyBadgesActivity::class.java)
                        it.startActivity(intent)

                    }
                }

                progressDialogForSubscribe.setOnDismissListener {
                    System.out.println(badgeElement)

                    val task = updateBadgeStatusDataAsync(
                            AppConfig.APP_BRANDING_ID,
                            appToken,
                            badgeElement.asJsonObject.get("id").asString);
                    task.execute();


                }
            }

        }

        internal inner class updateBadgeStatusDataAsync(brandingAppId: String, appToken: String, badgeId: String) :
                AsyncTask<Void, Void, Void>() {

            val brandingAppId = brandingAppId;
            val appToken = appToken;
            val badgeId = badgeId;


            override fun doInBackground(vararg p0: Void?): Void? {
                val apiService: ApiInterface = ApiClient.getAzureApiClientV1().create(ApiInterface::class.java);
                val profileDashboardResponseDataCall: Call<ProfileDashboardResponseData> = apiService.updateBadgeStatus(
                        brandingAppId,
                        appToken,
                        badgeId);

                profileDashboardResponseDataCall.enqueue(object : Callback<ProfileDashboardResponseData> {
                    override fun onResponse(call: Call<ProfileDashboardResponseData>, response: Response<ProfileDashboardResponseData>) {
                        if (response.isSuccessful()) {
                            System.out.println(response)
                        }
                    }

                    override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {

                    }
                });
                return null;
            }

        }
    }
}