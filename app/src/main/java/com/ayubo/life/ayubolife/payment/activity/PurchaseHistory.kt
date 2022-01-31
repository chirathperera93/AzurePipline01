package com.ayubo.life.ayubolife.payment.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.health.OMCreatedOrderObj
import com.ayubo.life.ayubolife.payment.EXTRA_PREVIOUS_ACTIVITY
import com.ayubo.life.ayubolife.payment.fragment.MedicineHistory
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_purshase_history.*

class PurchaseHistory : BaseActivity(), MedicineHistory.ChangeFragmentInterface {

    lateinit var appToken: String;
    lateinit var pref: PrefManager;

    lateinit var tabLayout: TabLayout;
    lateinit var viewPager: ViewPager;
    var previousActivity: String = "";


    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PREVIOUS_ACTIVITY)) {
            previousActivity = bundle.getSerializable(EXTRA_PREVIOUS_ACTIVITY) as String


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purshase_history)
        readExtras()
        pref = PrefManager(baseContext);
        appToken = pref.userToken;




        imageViewForMainPaymentBackBtn.setOnClickListener {
            finish()
        }

//        getPurchaseData()


        tabLayout = findViewById<TabLayout>(R.id.mainHistoryTabLayout);
        viewPager = findViewById<ViewPager>(R.id.mainHistoryViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Doc Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
        tabLayout.addTab(tabLayout.newTab().setText("Programs"));
        tabLayout.addTab(tabLayout.newTab().setText("Medicine"));
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL;


        print(previousActivity)


        var tabPosition = -1

        if (previousActivity == "order_medicine") {
            tabPosition = 3
        }

        val historyFragmentPagerAdapter: HistoryFragmentPagerAdapter =
            HistoryFragmentPagerAdapter(
                this,
                supportFragmentManager,
                tabLayout.tabCount,
                tabPosition
            );



        viewPager.adapter = historyFragmentPagerAdapter;
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val b = pref.disableAllTabs;
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        if (previousActivity == "order_medicine") {
            viewPager.currentItem = 3
        }


    }

    override fun onMedicineHistoryOrderButtonClick(oMCreatedOrderObj: OMCreatedOrderObj) {
        print(oMCreatedOrderObj)



        processAction("payment", "prescriptionorder:${oMCreatedOrderObj.id}")
    }

}

//    private fun getPurchaseData() {
//        purchase_history_loading.visibility = View.VISIBLE
//        val apiService: ApiInterface =
//            ApiClient.getAzureApiClientV1().create(ApiInterface::class.java);
//        val call: Call<ProfileDashboardResponseData> = apiService.getUserPurchaseHistoryData(
//            AppConfig.APP_BRANDING_ID,
//            appToken,
//            "user_dashboard",
//            System.currentTimeMillis()
//        );
//        call.enqueue(object : Callback<ProfileDashboardResponseData> {
//            @SuppressLint("WrongConstant")
//            override fun onResponse(
//                call: Call<ProfileDashboardResponseData>,
//                response: Response<ProfileDashboardResponseData>
//            ) {
//                purchase_history_loading.visibility = View.GONE
//                if (response.isSuccessful) {
//                    val userPurchaseHistoryData: JsonArray =
//                        Gson().toJsonTree(response.body()!!.data).asJsonArray;
//
//                    println(userPurchaseHistoryData)
//
//                    purchase_history_recycler_view.layoutManager =
//                        LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)
//                    val purchaseHistoryItemArrayList = ArrayList<PurchaseHistoryItem>()
//                    for (i in 0 until userPurchaseHistoryData.size()) {
//                        val purchaseHistoryItem = userPurchaseHistoryData.get(i)
//
//                        purchaseHistoryItemArrayList.add(
//                            PurchaseHistoryItem(
//                                purchaseHistoryItem.asJsonObject.get("id").asString,
//                                purchaseHistoryItem.asJsonObject.get("app_id").asString,
//                                purchaseHistoryItem.asJsonObject.get("user_id").asString,
//                                purchaseHistoryItem.asJsonObject.get("title").asString,
//                                purchaseHistoryItem.asJsonObject.get("description").asString,
//                                purchaseHistoryItem.asJsonObject.get("icon").asString,
//                                purchaseHistoryItem.asJsonObject.get("status").asString,
//                                purchaseHistoryItem.asJsonObject.get("currency").asString,
//                                purchaseHistoryItem.asJsonObject.get("cost").asInt,
//                                purchaseHistoryItem.asJsonObject.get("created_datetime").asLong,
//                                purchaseHistoryItem.asJsonObject.get("updated_datetime").asLong
//
//                            )
//
//                        )
//
//
//                    }
//
//                    val purchaseHistoryItemAdapter =
//                        PurchaseHistoryItemAdapter(baseContext, purchaseHistoryItemArrayList)
//                    purchase_history_recycler_view.adapter = purchaseHistoryItemAdapter
//
//                }
//            }
//
//            override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
//                purchase_history_loading.visibility = View.GONE
//            }
//
//        })
//
//    }
