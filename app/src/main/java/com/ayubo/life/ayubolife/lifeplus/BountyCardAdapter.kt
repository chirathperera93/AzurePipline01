package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide

class BountyCardAdapter(
    val context: Context,
    val dataList: ArrayList<BountyCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<BountyCardAdapter.MyViewHolder>() {

    var onBountyCardItemClickListener: BountyCardAdapter.OnBountyCardItemClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnBountyCardItemClickListener {
        fun onBountyCardItemClick(action: String, meta: String)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BountyCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.bounty_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, bountyCardItem: BountyCardItem) {

            val topic = itemView.findViewById(R.id.topic) as TextView
            val description = itemView.findViewById(R.id.description) as TextView
            val click_link = itemView.findViewById(R.id.click_link) as TextView
            val image_view = itemView.findViewById(R.id.image_view) as ImageView

            topic.setText(bountyCardItem.title)
            description.setText(bountyCardItem.description)
            click_link.setText(bountyCardItem.action_meta.get("title").asString)

            Glide.with(context).load(bountyCardItem.icon_url).centerCrop().into(image_view);

            click_link.setOnClickListener {
                onBountyCardItemClickListener?.onBountyCardItemClick(
                    bountyCardItem.action_meta.get("action").asString,
                    bountyCardItem.action_meta.get("meta").asString
                )
            }


        }


    }


}