package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointRewardData
import com.ayubo.life.ayubolife.revamp.v1.model.RedeemPointRewardDataFilterTypes
import com.bumptech.glide.Glide

class RedeemCategoryAdapter(
    private val context: Context,
    private val redeemCategoryItemArrayList: ArrayList<RedeemPointRewardDataFilterTypes>,
    private val redeemPointRewardData: RedeemPointRewardData

) : RecyclerView.Adapter<RedeemCategoryAdapter.ItemViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onCategoryTypeItemClick(redeemPointRewardDataFilterTypes: RedeemPointRewardDataFilterTypes)
    }

    val cardViewList: ArrayList<View> = ArrayList<View>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedeemCategoryAdapter.ItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.redeem_category_item, parent, false)

        cardViewList.add(v)

        return ItemViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RedeemCategoryAdapter.ItemViewHolder, position: Int) {
        holder.bindItems(context, redeemCategoryItemArrayList[position])
    }

    override fun getItemCount(): Int {
        return redeemCategoryItemArrayList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("Range")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(context: Context, redeemCategoryItem: RedeemPointRewardDataFilterTypes) {
            val card_view = itemView.findViewById<CardView>(R.id.card_view)
            val mainImageView = itemView.findViewById<ImageView>(R.id.mainImageView)
            val categoryTextView = itemView.findViewById<TextView>(R.id.categoryTextView)
            val mainRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.mainRelativeLayout)

            Glide.with(context).load(redeemCategoryItem.icon).into(mainImageView)

//            mainImageView.setColorFilter(
//                ContextCompat.getColor(context, R.color.color_707070)
//            )

            categoryTextView.text = redeemCategoryItem.type

            categoryTextView.setTextColor(Color.parseColor(redeemPointRewardData.filter_inactive_text_color))
            mainRelativeLayout.setBackgroundColor(Color.parseColor(redeemPointRewardData.filter_inactive_bg_color))

            if (position == 0) {
                setDecoForSelectedCategory(itemView, context)
            }


            card_view.setOnClickListener {
                for (cardView in cardViewList) {
                    cardView.findViewById<RelativeLayout>(R.id.mainRelativeLayout)
                        .setBackgroundResource(0)
                    cardView.findViewById<TextView>(R.id.categoryTextView)
                        .setTextColor(context.resources.getColor(R.color.black))
                    cardView.findViewById<TextView>(R.id.categoryTextView).typeface =
                        context.resources.getFont(R.font.montserrat_regular)

                    cardView.findViewById<TextView>(R.id.categoryTextView)
                        .setTextColor(Color.parseColor(redeemPointRewardData.filter_inactive_text_color))
                    cardView.findViewById<RelativeLayout>(R.id.mainRelativeLayout)
                        .setBackgroundColor(Color.parseColor(redeemPointRewardData.filter_inactive_bg_color))

                    cardView.findViewById<ImageView>(R.id.mainImageView).colorFilter = null

                }

                setDecoForSelectedCategory(itemView, context)

                onItemClickListener!!.onCategoryTypeItemClick(redeemCategoryItem)
            }

        }


    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDecoForSelectedCategory(itemView: View, context: Context) {
        val mainRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.mainRelativeLayout)
        val categoryTextView = itemView.findViewById<TextView>(R.id.categoryTextView)
        val mainImageView = itemView.findViewById<ImageView>(R.id.mainImageView)


        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(redeemPointRewardData.filter_active_bg_color),
                Color.parseColor(redeemPointRewardData.filter_active_bg_color)
            )
        )
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.cornerRadius = 0f
        mainRelativeLayout.setBackgroundDrawable(gd)
        categoryTextView.setTextColor(Color.parseColor(redeemPointRewardData.filter_active_text_color))

        val typeface: Typeface = context.resources.getFont(R.font.montserrat_semi_bold)
        categoryTextView.typeface = typeface


        val animSet = AnimationSet(true)
        animSet.interpolator = DecelerateInterpolator()
        animSet.fillAfter = true
        animSet.isFillEnabled = true

        val animRotate: RotateAnimation = RotateAnimation(
            0.0f, -360.0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )

        animRotate.duration = 1000
        animRotate.fillAfter = true
        animSet.addAnimation(animRotate)


        mainImageView.colorFilter = null
        mainImageView.setColorFilter(ContextCompat.getColor(context, R.color.white))

        mainImageView.startAnimation(animSet)
    }

    fun dpToPx(dp: Int): Int {
        val px = dp * context.resources.displayMetrics.density
        return px.toInt()
    }


}