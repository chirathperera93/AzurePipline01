package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.flavors.changes.Constants

class SubscriptionCardAdapter(
    val context: Context,
    val dataList: ArrayList<SubscriptionCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<SubscriptionCardAdapter.MyViewHolder>() {

    var onSubscriptionItemClickListener: SubscriptionCardAdapter.OnSubscriptionItemClickListener? =
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
    ): SubscriptionCardAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.subscribe_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, subscriptionCardItem: SubscriptionCardItem) {


            val subscribe_background_card =
                itemView.findViewById(R.id.subscribe_background_card) as CardView
            val rules_liner_layout = itemView.findViewById(R.id.rules_liner_layout) as LinearLayout
            val subscription_topic = itemView.findViewById(R.id.subscription_topic) as TextView
            val subscription_description =
                itemView.findViewById(R.id.subscription_description) as TextView
            val subscription_currency =
                itemView.findViewById(R.id.subscription_currency) as TextView
            val subscription_rate = itemView.findViewById(R.id.subscription_rate) as TextView
            val subscription_frequency =
                itemView.findViewById(R.id.subscription_frequency) as TextView
            val start_now_button = itemView.findViewById(R.id.start_now_button) as Button




            subscription_topic.text = subscriptionCardItem.title
            subscription_description.text = subscriptionCardItem.summary
            subscription_currency.text = subscriptionCardItem.currency
            subscription_rate.text = subscriptionCardItem.rate
            subscription_frequency.text = subscriptionCardItem.frequency
            start_now_button.text = subscriptionCardItem.button_text

            System.out.println(dataList.size)

            if (dataList.size <= 2) {
                val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(context);
                subscribe_background_card.layoutParams.width =
                    (deviceScreenDimension.displayWidth - 150) / 2
            }


            start_now_button.setOnClickListener {

//                val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(context);
//                val paramsNw: Bundle = Bundle();
//                paramsNw.putString(
//                    AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME,
//                    AppConfig.FACEBOOK_EVENT_ID_DISCOVER
//                );
//                paramsNw.putString(AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME, "paynow");
//
//                if (subscriptionCardItem.title.equals("Daily Subscription")) {
//                    paramsNw.putString("Daily", "Daily");
//                } else if (subscriptionCardItem.title.equals("Weekly Subscription")) {
//                    paramsNw.putString("Weekly", "Weekly");
//                } else if (subscriptionCardItem.title.equals("Monthly Subscription")) {
//                    paramsNw.putString("Monthly", "Monthly");
//                } else {
//                    paramsNw.putString(AppConfig.FACEBOOK_EVENT_ID_META, "");
//                }
//
//                loggerFB.logEvent(AppEventsConstants.EVENT_NAME_SUBSCRIBE, paramsNw);


                onSubscriptionItemClickListener?.onSubscriptionItemClick(
                    subscriptionCardItem.action, subscriptionCardItem.meta
                )
            }


            if (Constants.type == Constants.Type.LIFEPLUS) {
                start_now_button.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient)
                subscribe_background_card.setCardBackgroundColor(
                    context.getResources().getColor(R.color.subscribe_card_background)
                );
            } else {
                start_now_button.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient)
                subscribe_background_card.setCardBackgroundColor(
                    context.getResources().getColor(R.color.theme_color)
                );
            }


            rules_liner_layout.removeAllViews()
            if (subscriptionCardItem.list.size() > 0) {

                for (i in 0 until subscriptionCardItem.list.size()) {
                    val subscriptionCardListItem = subscriptionCardItem.list.get(i)
                    val linearLayout: LinearLayout = LinearLayout(context);

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(0, 12, 0, 0)

                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);


//                    val imageView: ImageView = ImageView(context)
                    val bulletPoint: TextView = TextView(context)
                    val textView: TextView = TextView(context)

                    val paramsForBulletPoint: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_VERTICAL
                    };
                    paramsForBulletPoint.setMargins(0, 0, 16, 0)
                    bulletPoint.layoutParams = paramsForBulletPoint
                    bulletPoint.setText("\u2022")
                    bulletPoint.setTextColor(context.getResources().getColor(R.color.black))
                    bulletPoint.textSize = 16F;


//                    val paramsForSubImageView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
//                            8,
//                            8).apply {
//                        gravity = Gravity.CENTER_VERTICAL
//                    };
//                    paramsForSubImageView.setMargins(0, 0, 16, 0);
//                    imageView.layoutParams = paramsForSubImageView;
//                    imageView.setImageResource(R.drawable.nonactive_dot);
                    linearLayout.addView(bulletPoint)


                    val paramsForSubTextView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_VERTICAL
                    };
                    textView.layoutParams = paramsForSubTextView;
                    val face: Typeface? =
                        context?.let { ResourcesCompat.getFont(it, R.font.montserrat_regular) };
                    textView.setTypeface(face);
                    textView.setTextSize(8F)
                    textView.setTextColor(context.getResources().getColor(R.color.color_3B3B3B))
                    textView.setText(subscriptionCardListItem.asString);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.maxLines = 2
                    linearLayout.addView(textView)

                    rules_liner_layout.addView(linearLayout)


                }

            }

        }


    }
}