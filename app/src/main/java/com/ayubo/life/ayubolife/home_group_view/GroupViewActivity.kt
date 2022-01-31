package com.ayubo.life.ayubolife.home_group_view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_META
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_group_view.*
import kotlinx.android.synthetic.main.activity_payment_options.*
import javax.inject.Inject


class GroupViewActivity : BaseActivity(), GroupViewAdapter.OnItemClickListener {

    var windowWidth: Int = 0;
    var meta: String = "";

    override fun onProcessAction(action: String, meta: String) {
        Log.d("GroupViewActivity", "================" + action)
        processAction(action, meta)
    }

    override fun onPaymentProcess(obj: PriceList) {
        onPaymentProcessed(obj)
    }


    @Inject
    lateinit var groupViewActivityVM: GroupViewActivityVM

    private var groupViewAdapter: GroupViewAdapter? = null

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext);

        windowWidth = deviceScreenDimension.displayWidth



        subscription.add(groupViewActivityVM.getAllGroupItems(meta).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar_groupview.visibility = View.VISIBLE }
            .doOnTerminate { progressBar_groupview.visibility = View.GONE }
            .doOnError { progressBar_groupview.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    //  val mlayoutManager = LinearLayoutManager(this@GroupViewActivity)
                    liststoregroup.removeAllViews();

                    val moviewList2 =
                        groupViewActivityVM.dataList.list as ArrayList<GroupViewMainDataList>

                    val newGroupViewMainData =
                        groupViewActivityVM.groupViewMainDataListNew as ArrayList<GroupViewMainData>
                    group_title.setText(groupViewActivityVM.dataList.title)

                    System.out.println(newGroupViewMainData)




                    for (c in 0 until newGroupViewMainData.size) {
                        val innerScrolledList: GroupViewMainData = newGroupViewMainData[c]
                        val contentViewMain = LinearLayout(this)
                        contentViewMain.orientation = LinearLayout.VERTICAL

                        val sectionHeaderText = TextView(this)
                        sectionHeaderText.text = innerScrolledList.title
                        sectionHeaderText.setPadding(0, 20, 5, 5)
                        sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
                        val typeFace: Typeface? =
                            ResourcesCompat.getFont(this.applicationContext, R.font.roboto_bold)
                        sectionHeaderText.typeface = typeFace
                        sectionHeaderText.setTextColor(Color.parseColor("#000000"))
                        contentViewMain.addView(sectionHeaderText)


                        val groupViewMainDataArrayList =
                            innerScrolledList.list as ArrayList<GroupViewMainDataList>
                        val recyclerView = RecyclerView(baseContext)
                        groupViewAdapter = GroupViewAdapter(
                            this@GroupViewActivity,
                            groupViewMainDataArrayList,
                            windowWidth
                        )

                        val linearLayoutManager =
                            LinearLayoutManager(baseContext, LinearLayout.HORIZONTAL, false)

                        groupViewAdapter!!.onitemClickListener = this@GroupViewActivity
                        recyclerView.apply {
                            layoutManager = linearLayoutManager
                            adapter = groupViewAdapter
                        }
                        contentViewMain.addView(recyclerView)


                        liststoregroup.addView(contentViewMain)


                    }


//                        groupViewAdapter = GroupViewAdapter(this@GroupViewActivity, moviewList2, windowWidth)

//                        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
//                        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayout.HORIZONTAL, false)

//                        liststoregroup.layoutManager = linearLayoutManager

//                        groupViewAdapter!!.onitemClickListener = this@GroupViewActivity

//                        liststoregroup.apply {
//                            layoutManager = linearLayoutManager
//                            adapter = groupViewAdapter
//                        }


                    edt_search_value_store_gr!!.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                        }

                        @SuppressLint("WrongConstant")
                        override fun onTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {

                            liststoregroup.removeAllViews();
                            val recyclerView = RecyclerView(baseContext)

                            if (edt_search_value_store_gr.text.isNotEmpty()) {
                                val longerThan3 = moviewList2.filter {
                                    it.title.contains(
                                        edt_search_value_store_gr.text,
                                        true
                                    ) || it.item_sub_text.contains(
                                        edt_search_value_store_gr.text,
                                        true
                                    )
                                }

                                val timelineAdapte3 = GroupViewAdapter(
                                    this@GroupViewActivity,
                                    longerThan3 as ArrayList<GroupViewMainDataList>,
                                    windowWidth
                                )
                                timelineAdapte3!!.onitemClickListener = this@GroupViewActivity

                                val linearLayoutManager = LinearLayoutManager(
                                    baseContext,
                                    LinearLayout.HORIZONTAL,
                                    false
                                )


//                                    liststoregroup.apply {
//                                        adapter = timelineAdapte3
//                                    }

                                recyclerView.apply {
                                    layoutManager = linearLayoutManager
                                    adapter = timelineAdapte3
                                }

                                liststoregroup.addView(recyclerView)


                            } else {
//                                    val moviewList22 = groupViewActivityVM.dataList.list

                                liststoregroup.removeAllViews();

                                val newGroupViewMainData =
                                    groupViewActivityVM.groupViewMainDataListNew as ArrayList<GroupViewMainData>


//                                    val timelineAdapter22 = GroupViewAdapter(this@GroupViewActivity, newGroupViewMainData, windowWidth)
//                                    timelineAdapter22!!.onitemClickListener = this@GroupViewActivity


//                                    liststoregroup.apply {
//                                        layoutManager = linearLayoutManager
//                                        adapter = timelineAdapter22
//                                    }

                                for (c in 0 until newGroupViewMainData.size) {
                                    val innerScrolledList: GroupViewMainData =
                                        newGroupViewMainData[c]
                                    val contentViewMain = LinearLayout(baseContext)
                                    contentViewMain.orientation = LinearLayout.VERTICAL

                                    val sectionHeaderText = TextView(baseContext)
                                    sectionHeaderText.text = innerScrolledList.title
                                    sectionHeaderText.setPadding(0, 20, 5, 5)
                                    sectionHeaderText.setTextSize(
                                        TypedValue.COMPLEX_UNIT_SP,
                                        13F
                                    )
                                    val typeFace: Typeface? = ResourcesCompat.getFont(
                                        applicationContext,
                                        R.font.roboto_bold
                                    )
                                    sectionHeaderText.typeface = typeFace
                                    sectionHeaderText.setTextColor(Color.parseColor("#000000"))
                                    contentViewMain.addView(sectionHeaderText)


                                    val groupViewMainDataArrayList =
                                        innerScrolledList.list as ArrayList<GroupViewMainDataList>
                                    val recyclerView = RecyclerView(baseContext)
                                    groupViewAdapter = GroupViewAdapter(
                                        this@GroupViewActivity,
                                        groupViewMainDataArrayList,
                                        windowWidth
                                    )

                                    val linearLayoutManager = LinearLayoutManager(
                                        baseContext,
                                        LinearLayout.HORIZONTAL,
                                        false
                                    )

                                    groupViewAdapter!!.onitemClickListener = this@GroupViewActivity
                                    recyclerView.apply {
                                        layoutManager = linearLayoutManager
                                        adapter = groupViewAdapter
                                    }
                                    contentViewMain.addView(recyclerView)


                                    liststoregroup.addView(contentViewMain)


                                }

                            }


                        }

                        override fun afterTextChanged(editable: Editable) {}
                    })

                } else {
                    progressBar_groupview.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                }
            }, {
                progressBar_groupview.visibility = View.GONE
                showMessage(R.string.service_loading_fail)
            })
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_view)
        App.getInstance().appComponent.inject(this)
        readExtras()
        initView()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


    }


    private fun readExtras() {

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PAYMENT_META)) {
            meta = bundle.getSerializable(EXTRA_PAYMENT_META) as String
//            return meta
        }

//        else {
////            return meta
//        }

    }

    companion object {
        fun startActivity(context: Activity, meta: String) {
            val intent = Intent(context, GroupViewActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_META, meta)
            context.startActivity(intent)
        }
    }

    private fun initView() {
        // BACK BUTTON EVENT START
        back_layout_groupviewprograms.setOnClickListener { finish() }
        img_backBtn_groupviewprograms.setOnClickListener { finish() }
        // BACK BUTTON EVENT END
    }

}