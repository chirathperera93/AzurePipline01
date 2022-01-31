package com.ayubo.life.ayubolife.lifeplus.NewDashboard

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
import com.bumptech.glide.Glide

/**
 * Created by Chirath Perera on 2021-10-14.
 */
class AnnouncementCardAdapter(
    val context: Context,
    val dataList: ArrayList<AnnouncementCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<AnnouncementCardAdapter.MyViewHolder>() {


    var onAnnouncementItemClickListener: AnnouncementCardAdapter.OnAnnouncementItemClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnAnnouncementItemClickListener {
        fun onAnnouncementCardItemClick(action: String, meta: String)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dashboard_announcement_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, announcementCardItem: AnnouncementCardItem) {

            println(announcementCardItem)


            val mainCardView = itemView.findViewById(R.id.mainCardView) as CardView
            val textViewForTile = itemView.findViewById(R.id.textViewForTile) as TextView
            val textViewForDescription =
                itemView.findViewById(R.id.textViewForDescription) as TextView
            val textViewForRemainingTimeLabel =
                itemView.findViewById(R.id.textViewForRemainingTimeLabel) as TextView
            val textViewForRemainingTimeValue =
                itemView.findViewById(R.id.textViewForRemainingTimeValue) as TextView
            val imageViewForIcon = itemView.findViewById(R.id.imageViewForIcon) as ImageView

            mainCardView.setCardBackgroundColor(Color.parseColor(announcementCardItem.bg_color))

            textViewForTile.text = announcementCardItem.title
            textViewForDescription.text = announcementCardItem.description
            textViewForRemainingTimeLabel.text = announcementCardItem.label
            textViewForRemainingTimeValue.text = announcementCardItem.content

            Glide.with(context).load(announcementCardItem.icon_url).into(imageViewForIcon);

            mainCardView.setOnClickListener {
                onAnnouncementItemClickListener!!.onAnnouncementCardItemClick(
                    announcementCardItem.action,
                    announcementCardItem.meta
                )
            }


        }


    }
}