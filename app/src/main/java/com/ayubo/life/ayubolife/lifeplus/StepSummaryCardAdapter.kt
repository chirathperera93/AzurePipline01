package com.ayubo.life.ayubolife.lifeplus

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dashboard_tracker_card.view.*
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.*

class StepSummaryCardAdapter(
    val context: Context,
    val dataList: ArrayList<StepSummaryCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<StepSummaryCardAdapter.MyViewHolder>() {

    var onStepSummaryCardAdapterItemClickListener: StepSummaryCardAdapter.OnSubscriptionItemClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnSubscriptionItemClickListener {
        fun onSubscriptionItemClick(action: String, meta: String)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StepSummaryCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.step_summary_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, subscriptionCardItem: StepSummaryCardItem) {


            System.out.println(subscriptionCardItem)
            val circular_progress_bar_step_summary =
                itemView.findViewById(R.id.circular_progress_bar_step_summary) as ProgressBar
            val main_background_color_border =
                itemView.findViewById(R.id.main_background_color_border) as CardView

            val main_topic =
                itemView.findViewById(R.id.main_topic) as TextView


            val circle_top =
                itemView.findViewById(R.id.circle_top) as TextView

            val remaining_status =
                itemView.findViewById(R.id.remaining_status) as TextView
            val circle_centre =
                itemView.findViewById(R.id.circle_centre) as TextView
            val circle_bottom =
                itemView.findViewById(R.id.circle_bottom) as TextView

            val frequency =
                itemView.findViewById(R.id.frequency) as TextView

            val step_icon_linear_layout =
                itemView.findViewById(R.id.step_icon_linear_layout) as LinearLayout

            val remaining_value =
                itemView.findViewById(R.id.remaining_value) as TextView

            val time =
                itemView.findViewById(R.id.time) as TextView

            val target =
                itemView.findViewById(R.id.target) as TextView

            val step_icon =
                itemView.findViewById(R.id.step_icon) as ImageView

            var gColorValue1 = context.resources.getColor(R.color.theme_color);
            var gColorValue2 = context.resources.getColor(R.color.theme_color);
            if (subscriptionCardItem.title == "Daily") {
                gColorValue1 = context.resources.getColor(R.color.daily_g_color1);
                gColorValue2 = context.resources.getColor(R.color.daily_g_color2);
            }

            if (subscriptionCardItem.title == "Weekly") {
                gColorValue1 = context.resources.getColor(R.color.weekly_g_color1);
                gColorValue2 = context.resources.getColor(R.color.weekly_g_color2);
            }

            if (subscriptionCardItem.title == "Monthly") {
                gColorValue1 = context.resources.getColor(R.color.monthly_g_color1);
                gColorValue2 = context.resources.getColor(R.color.monthly_g_color2);
            }


            val gdCommon = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(gColorValue1, gColorValue2)
            )
            gdCommon.gradientType = GradientDrawable.LINEAR_GRADIENT
            gdCommon.cornerRadius = 4f



            main_background_color_border.setCardBackgroundColor(gColorValue2)

            main_topic.setText(subscriptionCardItem.title)

            val progressBarDrawable: LayerDrawable =
                circular_progress_bar_step_summary.getProgressDrawable() as LayerDrawable;
            val progressDrawable: Drawable = progressBarDrawable.getDrawable(1);
            progressDrawable.setColorFilter(
                gColorValue2,
                PorterDuff.Mode.SRC_IN
            );
            circular_progress_bar_step_summary.setProgress(0);

            startAnimationCounter(
                circular_progress_bar_step_summary,
                0,
                subscriptionCardItem.percentage.toInt()
            )

            circle_top.setText(subscriptionCardItem.circle_top)

            circle_centre.setText(subscriptionCardItem.circle_centre)
            circle_centre.setTextColor(gColorValue2)

            Glide.with(context).load(subscriptionCardItem.icon_url).centerCrop().into(step_icon);


            step_icon_linear_layout.setBackgroundDrawable(gdCommon)

            circle_bottom.setText(subscriptionCardItem.circle_bottom)

            val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(gColorValue1, gColorValue2)
            )
            gd.gradientType = GradientDrawable.LINEAR_GRADIENT
            gd.cornerRadius = 20f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gd.setPadding(8, 1, 8, 1)
            }


            val stringArray = subscriptionCardItem.summary.split(" ");

            target.setText(stringArray[0] + " ")

            frequency.setBackgroundDrawable(gd);
            frequency.setText(stringArray[1] + " " + stringArray[2])

            var timeValue = "";
            stringArray.forEachIndexed { index, s ->
                if (index > 2) {
                    timeValue = timeValue + " " + s;
                }
            }

            System.out.println(timeValue)
            time.setText(timeValue)

            remaining_value.setText(subscriptionCardItem.remaining_value)
            remaining_status.setText(subscriptionCardItem.remaining_label)


        }


    }

    fun startAnimationCounter(trackerProgressBar: ProgressBar, start_no: Int, end_no: Int) {
        val animator: ValueAnimator = ValueAnimator.ofInt(start_no, end_no);
        animator.setDuration(2000);

        animator.addUpdateListener {
            trackerProgressBar.setProgress(Integer.parseInt(it.getAnimatedValue().toString()));
        }


        animator.start();
    }
}