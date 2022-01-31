package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import java.util.concurrent.TimeUnit

class NotificationCardAdapter(
    val context: Context,
    val dataList: ArrayList<NotificationCardItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<NotificationCardAdapter.MyViewHolder>() {

    var onNotificationItemClickListener: NotificationCardAdapter.OnNotificationItemClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnNotificationItemClickListener {
        fun onNotificationItemClick(action: String, meta: String)
        fun onAddToButtonClick(notificationCardItem: NotificationCardItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, notificationCardItem: NotificationCardItem) {

            println(notificationCardItem)


            val currentDateTime = System.currentTimeMillis();

            val milliseconds = (notificationCardItem.task_datetime - currentDateTime);

            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);


            val notificationMainCard = itemView.findViewById(R.id.notificationMainCard) as CardView
            val imageViewLinearLayoutForNotificationIcon =
                itemView.findViewById(R.id.imageViewLinearLayoutForNotificationIcon) as LinearLayout
            val textViewForNotificationTile =
                itemView.findViewById(R.id.textViewForNotificationTile) as TextView
            val textViewForNotificationDescription =
                itemView.findViewById(R.id.textViewForNotificationDescription) as TextView

            val remaining_time_value = itemView.findViewById(R.id.remaining_time_value) as TextView

//            val hoursValue = itemView.findViewById(R.id.hoursValue) as TextView

//            val minutesValue = itemView.findViewById(R.id.minutesValue) as TextView

//            val secondsValue = itemView.findViewById(R.id.secondsValue) as TextView
//            val icon = itemView.findViewById(R.id.icon) as ImageView
//            val add_to_calendar_btn = itemView.findViewById(R.id.add_to_calendar_btn) as Button

//            if (Constants.type == Constants.Type.LIFEPLUS) {
//                add_to_calendar_btn.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient)
//            } else {
//                add_to_calendar_btn.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient)
//            }


//            hoursValue.text = hours.toString()
//            minutesValue.text = minutes.toString()
//            secondsValue.text = seconds.toString()

            val timeFromNow = DateUtils.getRelativeTimeSpanString(
                notificationCardItem.task_datetime,
                System.currentTimeMillis(),
                0,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )

            print(timeFromNow)

            remaining_time_value.text = timeFromNow

            textViewForNotificationTile.text = notificationCardItem.title
            textViewForNotificationDescription.text = notificationCardItem.description


            notificationMainCard.setOnClickListener {
                onNotificationItemClickListener?.onNotificationItemClick(
                    notificationCardItem.action, notificationCardItem.meta
                )
            }

            imageViewLinearLayoutForNotificationIcon.setOnClickListener {
                onNotificationItemClickListener?.onAddToButtonClick(notificationCardItem);
            }


        }


    }
}