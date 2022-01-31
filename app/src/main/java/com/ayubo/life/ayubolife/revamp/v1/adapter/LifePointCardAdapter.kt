package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointDataRewards
import com.bumptech.glide.Glide

class LifePointCardAdapter(
    val context: Context,
    val dataList: ArrayList<RedeemPointDataRewards>,
    var releaseAll: Boolean = false,
    val windowManager: WindowManager
) : RecyclerView.Adapter<LifePointCardAdapter.MyViewHolder>() {

    var onLifePointCardClickListener: LifePointCardAdapter.OnLifePointCardClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnLifePointCardClickListener {
        fun onLifePointCardClick(action: String, meta: String)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LifePointCardAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.life_point_card, parent, false)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(context);

        val width = deviceScreenDimension.displayWidth
        v.layoutParams = ViewGroup.LayoutParams((width - 100) / 2, (width - 380))


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
        fun bindItems(context: Context, redeemPointDataRewards: RedeemPointDataRewards) {

            val mainFrameLayout = itemView.findViewById<FrameLayout>(R.id.mainFrameLayout)
            val image = itemView.findViewById<ImageView>(R.id.image)
            val row4SubRow2LinearLayoutImageView =
                itemView.findViewById<ImageView>(R.id.row4SubRow2LinearLayoutImageView)
            val title = itemView.findViewById<TextView>(R.id.title)
            val row4SubRow2LinearLayoutTextView =
                itemView.findViewById<TextView>(R.id.row4SubRow2LinearLayoutTextView)
            val discount = itemView.findViewById<TextView>(R.id.discount)
            val description1 = itemView.findViewById<TextView>(R.id.description1)
            val center_description = itemView.findViewById<TextView>(R.id.center_description)
            val row4SubRow2CardViewRelativeLayoutTextView =
                itemView.findViewById<TextView>(R.id.row4SubRow2CardViewRelativeLayoutTextView)
            val row4SubRow2CardViewRelativeLayout =
                itemView.findViewById<RelativeLayout>(R.id.row4SubRow2CardViewRelativeLayout)
            val row4SubRow2CardView = itemView.findViewById<CardView>(R.id.row4SubRow2CardView)

            val cardViewForDiscount = itemView.findViewById<CardView>(R.id.cardViewForDiscount)


            val row4SubRow2LinearLayout =
                itemView.findViewById<LinearLayout>(R.id.row4SubRow2LinearLayout)


            if (redeemPointDataRewards.tile_image != "")
                Glide.with(context).load(redeemPointDataRewards.tile_image).centerCrop().into(image)

            if (redeemPointDataRewards.bottom_icon != "")
                Glide.with(context).load(redeemPointDataRewards.bottom_icon).centerCrop()
                    .into(row4SubRow2LinearLayoutImageView)

            title.text = redeemPointDataRewards.title

            if (redeemPointDataRewards.title_color != "")
                title.setTextColor(Color.parseColor(redeemPointDataRewards.title_color))

            row4SubRow2LinearLayoutTextView.text = redeemPointDataRewards.bottom_text

            if (redeemPointDataRewards.bottom_text_color != "")
                row4SubRow2LinearLayoutTextView.setTextColor(Color.parseColor(redeemPointDataRewards.bottom_text_color))

            description1.text = redeemPointDataRewards.sub_title

            if (redeemPointDataRewards.sub_title_color != "")
                description1.setTextColor(Color.parseColor(redeemPointDataRewards.sub_title_color))

            center_description.text = redeemPointDataRewards.center_description

            if (redeemPointDataRewards.center_description_color != null && redeemPointDataRewards.center_description_color != "")
                center_description.setTextColor(Color.parseColor(redeemPointDataRewards.center_description_color))

            row4SubRow2CardViewRelativeLayoutTextView.text = redeemPointDataRewards.button.text

            if (redeemPointDataRewards.button.text_color != "")
                row4SubRow2CardViewRelativeLayoutTextView.setTextColor(
                    Color.parseColor(
                        redeemPointDataRewards.button.text_color
                    )
                )

            if (redeemPointDataRewards.button.bg_color != "")
                row4SubRow2CardViewRelativeLayout.setBackgroundColor(
                    Color.parseColor(
                        redeemPointDataRewards.button.bg_color
                    )
                )

            row4SubRow2CardView.setOnClickListener {
                try {
                    onLifePointCardClickListener!!.onLifePointCardClick("button", "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            discount.text = redeemPointDataRewards.lable.text

            if (redeemPointDataRewards.lable.text_color != "")
                discount.setTextColor(Color.parseColor(redeemPointDataRewards.lable.text_color))

            if (redeemPointDataRewards.lable.bg_color != "")
                cardViewForDiscount.setCardBackgroundColor(Color.parseColor(redeemPointDataRewards.lable.bg_color))

            if (redeemPointDataRewards.bottom_icon == "") {
                row4SubRow2CardView.visibility = View.VISIBLE
                row4SubRow2LinearLayout.visibility = View.GONE
            } else {
                row4SubRow2CardView.visibility = View.GONE
                row4SubRow2LinearLayout.visibility = View.VISIBLE
            }

            if (redeemPointDataRewards.lable.text != "") {
                cardViewForDiscount.visibility = View.VISIBLE
            } else {
                cardViewForDiscount.visibility = View.GONE
            }

        }


    }
}