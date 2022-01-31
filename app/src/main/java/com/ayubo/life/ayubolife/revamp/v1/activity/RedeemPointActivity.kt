package com.ayubo.life.ayubolife.revamp.v1.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.adapter.LifePointCardAdapter
import com.ayubo.life.ayubolife.revamp.v1.adapter.RedeemCategoryAdapter
import com.ayubo.life.ayubolife.revamp.v1.model.DoRedeemRewardBody
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointData
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointDataRewards
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointRewardDataFilterTypes
import com.ayubo.life.ayubolife.revamp.v1.view_model.RedeemPointsViewModel
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.google.gson.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_redeem_point.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RedeemPointActivity : BaseActivity(),
    RedeemCategoryAdapter.OnItemClickListener,
    LifePointCardAdapter.OnLifePointCardClickListener {

    private var redeemCategoryAdapter: RedeemCategoryAdapter? = null
    private var lifePointCardAdapter: LifePointCardAdapter? = null

    lateinit var pref: PrefManager

    var selectedCategoryType: RedeemPointRewardDataFilterTypes? = null
    var cardsData: ArrayList<RedeemPointData> = ArrayList<RedeemPointData>()

    @Inject
    lateinit var redeemPointsViewModel: RedeemPointsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem_point)
        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)

        goBackLabel.setOnClickListener {
            finish()
        }

        page_back_btn.setOnClickListener {
            finish()
        }

        loadMainData()
    }

    private fun loadMainData() {
        subscription.add(
            redeemPointsViewModel.getAllRewards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainRedeemPointPageLoading.visibility = View.VISIBLE }
                .doOnTerminate { mainRedeemPointPageLoading.visibility = View.GONE }
                .doOnError { mainRedeemPointPageLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        try {
                            feed_back_topic_layout.visibility = View.VISIBLE
                            pageTitle.text = redeemPointsViewModel.redeemPointRewardData.Page_Header


                            if (redeemPointsViewModel.redeemPointRewardData.data.size > 0) {
                                mainScrollView.visibility = View.VISIBLE
                                emptyMessageLinearLayout.visibility = View.GONE
                            } else {
                                mainScrollView.visibility = View.GONE
                                emptyMessageLinearLayout.visibility = View.VISIBLE

                                if (redeemPointsViewModel.redeemPointRewardData.empty.image != "")
                                    Glide.with(this)
                                        .load(redeemPointsViewModel.redeemPointRewardData.empty.image)
                                        .into(emptyImageView)

                                emptyMainText.text =
                                    redeemPointsViewModel.redeemPointRewardData.empty.main_text
                                emptySubText.text =
                                    redeemPointsViewModel.redeemPointRewardData.empty.sub_text

                            }




                            cardsData = ArrayList<RedeemPointData>()
                            cardsData.addAll(redeemPointsViewModel.redeemPointRewardData.data)


                            val gson = GsonBuilder().create()
                            val customArray: JsonArray = gson.toJsonTree(cardsData).asJsonArray

                            if (redeemPointsViewModel.redeemPointRewardData.filter_types.size > 0) {
                                selectedCategoryType =
                                    redeemPointsViewModel.redeemPointRewardData.filter_types[0]
                            }

                            pref.savePointCards(customArray)

                            setFilter()

                            if (redeemPointsViewModel.redeemPointRewardData.points != null) {
                                Glide.with(this)
                                    .load(redeemPointsViewModel.redeemPointRewardData.points!!.icon_url)
                                    .into(icon_url)
                                label.text =
                                    redeemPointsViewModel.redeemPointRewardData.points!!.label
                                points.text =
                                    redeemPointsViewModel.redeemPointRewardData.points!!.points
                                btn_click_here.setOnClickListener {
                                    processAction(
                                        redeemPointsViewModel.redeemPointRewardData.points!!.action,
                                        redeemPointsViewModel.redeemPointRewardData.points!!.meta
                                    )
                                }
                            }


                            setCategoryTypes(redeemPointsViewModel.redeemPointRewardData.filter_types)

                            loadPointCard(redeemPointsViewModel.redeemPointRewardData.data)

                            disclaimer.text = redeemPointsViewModel.redeemPointRewardData.disclaimer

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    mainRedeemPointPageLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }

    @SuppressLint("Range")
    private fun loadPointCard(data: ArrayList<RedeemPointData>) {
        mainLinearLayoutForRecyclerView.removeAllViews()
        data.map { element ->
            val subLinearLayout: LinearLayout = LinearLayout(baseContext)

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            params.setMargins(0, 40, 0, 0)

            subLinearLayout.layoutParams = params

            subLinearLayout.orientation = LinearLayout.VERTICAL

            val textView: TextView = TextView(baseContext)
            textView.text = element.category_name
            textView.setTextColor(Color.parseColor(element.category_text_color))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F);
            val typeface: Typeface? =
                ResourcesCompat.getFont(baseContext, R.font.montserrat_medium);
            textView.typeface = typeface
            textView.visibility = View.GONE

            if (element.rewards.isNotEmpty()) {
                textView.visibility = View.VISIBLE
            }

            subLinearLayout.addView(textView)


            val recyclerView: RecyclerView = RecyclerView(baseContext)

            recyclerView.layoutManager =
                LinearLayoutManager(
                    baseContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )



            lifePointCardAdapter = LifePointCardAdapter(
                baseContext,
                element.rewards as ArrayList<RedeemPointDataRewards>,
                false,
                windowManager
            )

            try {
                lifePointCardAdapter!!.onLifePointCardClickListener =
                    this@RedeemPointActivity
            } catch (e: Exception) {
                e.printStackTrace()
            }

            recyclerView.adapter = lifePointCardAdapter

            subLinearLayout.addView(recyclerView)

            mainLinearLayoutForRecyclerView.addView(subLinearLayout)
        }
    }

    private fun setCategoryTypes(filterTypes: ArrayList<RedeemPointRewardDataFilterTypes>) {
        recyclerViewForCategories.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)

        val redeemCategoryItemArrayList = ArrayList<RedeemPointRewardDataFilterTypes>()

        for (i in 0 until filterTypes.size) {
            redeemCategoryItemArrayList.add(filterTypes[i])
        }

        redeemCategoryAdapter = RedeemCategoryAdapter(
            baseContext,
            redeemCategoryItemArrayList,
            redeemPointsViewModel.redeemPointRewardData
        )
        redeemCategoryAdapter!!.onItemClickListener = this@RedeemPointActivity

        recyclerViewForCategories.adapter = redeemCategoryAdapter
    }


    override fun onLifePointCardClick(action: String, meta: String) {

        if (action == "button") {
            doRedeemReward()
        }
    }

    private fun doRedeemReward() {

        val doRedeemRewardBody = DoRedeemRewardBody("")
        subscription.add(
            redeemPointsViewModel.doRedeemRewards(doRedeemRewardBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mainRedeemPointPageLoading.visibility = View.VISIBLE }
                .doOnTerminate { mainRedeemPointPageLoading.visibility = View.GONE }
                .doOnError { mainRedeemPointPageLoading.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        try {
                            showMessage("Successfully done the redeem")

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    mainRedeemPointPageLoading.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }

    override fun onCategoryTypeItemClick(redeemPointRewardDataFilterTypes: RedeemPointRewardDataFilterTypes) {


        selectedCategoryType = redeemPointRewardDataFilterTypes

        setFilter()


    }

    private fun setFilter() {
        val s = pref.pointCards
        val parser: JsonParser = JsonParser()
        val tradeElement: JsonElement = parser.parse(s)
        val jsonArray: JsonArray = tradeElement.asJsonArray
        val arrayList: ArrayList<RedeemPointData> = ArrayList<RedeemPointData>()

        for (c in 0 until jsonArray.size()) {
            arrayList.add(Gson().fromJson(jsonArray[c], RedeemPointData::class.java))
        }


        if (selectedCategoryType!!.type != "All") {
            arrayList.map { redeemPointData ->
                redeemPointsViewModel.redeemPointRewardData.data.map { item ->

                    if (item.category_name == redeemPointData.category_name) {

                        item.rewards =
                            redeemPointData.rewards.filter { point -> point.type == selectedCategoryType!!.type }

                    }
                }
            }

        } else {

            redeemPointsViewModel.redeemPointRewardData.data = arrayList

        }

        loadPointCard(redeemPointsViewModel.redeemPointRewardData.data)
    }


}