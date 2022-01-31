package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.revamp.v1.model.V1DashboardPointsData
import com.bumptech.glide.Glide

class V1LifePointCardAdapter(
    val context: Context,
    val dataList: ArrayList<V1DashboardPointsData>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<V1LifePointCardAdapter.MyViewHolder>() {


    var onPointsItemClickListener: OnPointsItemClickListener? = null

    interface OnPointsItemClickListener {
        fun pointsItemClick(action: String, meta: String)
    }

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): V1LifePointCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.v1_life_point_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, v1DashboardPointsData: V1DashboardPointsData) {

            val main_card = itemView.findViewById(R.id.main_card) as CardView
            val iconUrl = itemView.findViewById(R.id.icon_url) as ImageView
            val label = itemView.findViewById(R.id.label) as TextView
            val points = itemView.findViewById(R.id.points) as TextView
            val btnTextLearLayout = itemView.findViewById(R.id.btn_text_lear_layout) as LinearLayout
            val btnText = itemView.findViewById(R.id.btn_text) as TextView

            Glide.with(context).load(v1DashboardPointsData.icon_url).into(iconUrl)
            label.text = v1DashboardPointsData.label
            points.text = v1DashboardPointsData.points


            btnText.text = v1DashboardPointsData.btn_text
            if (v1DashboardPointsData.btn_text != "") {
                btnTextLearLayout.visibility = View.VISIBLE
            }

            main_card.setOnClickListener {

                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Home_Redeem", null)
                }


                onPointsItemClickListener!!.pointsItemClick(
                    "redeem_points",
                    v1DashboardPointsData.meta
                )
            }


            btnTextLearLayout.setOnClickListener {
                onPointsItemClickListener!!.pointsItemClick(
                    v1DashboardPointsData.action,
                    v1DashboardPointsData.meta
                )
//                onPointsItemClickListener!!.pointsItemClick(
//                    "redeem_points",
//                    v1DashboardPointsData.meta
//                )
            }


        }


    }
}