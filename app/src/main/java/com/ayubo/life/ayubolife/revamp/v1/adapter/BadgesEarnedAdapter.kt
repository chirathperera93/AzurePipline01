package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.revamp.v1.model.YTDBadges
import com.bumptech.glide.Glide

class BadgesEarnedAdapter(
    val context: Context,
    val dataList: ArrayList<YTDBadges>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<BadgesEarnedAdapter.MyViewHolder>() {

    var onBadgeItemClickListener: BadgesEarnedAdapter.OnBadgeItemClickListener? = null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnBadgeItemClickListener {
        fun onBadgeItemClick(action: String, meta: String)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BadgesEarnedAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.badges_earned_item, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, myBadge: YTDBadges) {
            val badgeIcon = itemView.findViewById(R.id.badge_icon) as ImageView
            val badgeIconLinearLayout =
                itemView.findViewById(R.id.badge_icon_linear_layout) as LinearLayout

            Glide.with(context).load(myBadge.icon).into(badgeIcon)

            badgeIconLinearLayout.setOnClickListener {
                onBadgeItemClickListener!!.onBadgeItemClick(
                    myBadge.action,
                    myBadge.meta
                )
            }


        }


    }
}