package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.revamp.v1.model.V1DashboardDailyTip
import com.bumptech.glide.Glide

class V1TipCardAdapter(
    val context: Context,
    val dataList: ArrayList<V1DashboardDailyTip>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<V1TipCardAdapter.MyViewHolder>() {

    var onDailyTipItemClickListener: OnDailyTipItemClickListener? = null

    interface OnDailyTipItemClickListener {
        fun dailyTipItemClick(action: String, meta: String)
    }

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): V1TipCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.v1_tip_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("Range")
        fun bindItems(context: Context, v1DashboardDailyTip: V1DashboardDailyTip) {

            print(v1DashboardDailyTip)

            val mainImage = itemView.findViewById(R.id.main_image) as ImageView
            val lableIconUrl = itemView.findViewById(R.id.lable_icon_url) as ImageView
            val corner_click_icon_url =
                itemView.findViewById(R.id.corner_click_icon_url) as ImageView
            val mainCard = itemView.findViewById(R.id.mainCard) as CardView
            val title = itemView.findViewById(R.id.title) as TextView
            val desc = itemView.findViewById(R.id.desc) as TextView
            val bottomText = itemView.findViewById(R.id.bottom_text) as TextView
            val lable_text = itemView.findViewById(R.id.lable_text) as TextView
            val corner_click_text = itemView.findViewById(R.id.corner_click_text) as TextView
            val lableBgCard = itemView.findViewById(R.id.lable_bg_card) as CardView
            val corner_click_card = itemView.findViewById(R.id.corner_click_card) as CardView


            Glide.with(context).load(v1DashboardDailyTip.main_image).into(mainImage)
            Glide.with(context).load(v1DashboardDailyTip.lable.icon_url).into(lableIconUrl)
            Glide.with(context).load(v1DashboardDailyTip.corner_click.icon_url)
                .into(corner_click_icon_url)


            title.text = v1DashboardDailyTip.title
            desc.text = v1DashboardDailyTip.desc
            corner_click_text.text = v1DashboardDailyTip.corner_click.text


            lable_text.text = v1DashboardDailyTip.lable.text
            if (v1DashboardDailyTip.lable.text_color != "") {
                lable_text.setTextColor(Color.parseColor(v1DashboardDailyTip.lable.text_color))
            }


            bottomText.text = v1DashboardDailyTip.bottom_text
            if (v1DashboardDailyTip.bottom_text_color != "") {
                bottomText.setTextColor(Color.parseColor(v1DashboardDailyTip.bottom_text_color))
                bottomText.setOnClickListener {
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Home_Dailytip_Readmore", null)
                    }
                }
            }

            mainCard.setOnClickListener {

                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Home_Timeline", null)
                }

                onDailyTipItemClickListener!!.dailyTipItemClick(
                    v1DashboardDailyTip.action,
                    v1DashboardDailyTip.meta
                )
            }

            lableBgCard.setCardBackgroundColor(Color.parseColor(v1DashboardDailyTip.lable.bg_color))

            corner_click_card.setCardBackgroundColor(Color.parseColor(v1DashboardDailyTip.corner_click.bg_color))

        }


    }
}