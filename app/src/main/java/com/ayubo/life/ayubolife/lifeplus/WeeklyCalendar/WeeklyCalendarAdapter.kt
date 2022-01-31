package com.ayubo.life.ayubolife.lifeplus.WeeklyCalendar


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.lifeplus.NewToDo.NotificationDateObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeeklyCalendarAdapter(
    val context: Context,
    val dateList: ArrayList<WeeklyItem>,
    val windowManager: WindowManager,
    var clickListner: WeeklyCalendarAdapter.OnWeeklyCalendarItemClickListner,
    var notificationWeeklyDateObjectList: ArrayList<NotificationDateObject>
) : RecyclerView.Adapter<WeeklyCalendarAdapter.ViewHolder>() {

    var viewArrayList: ArrayList<View> = ArrayList<View>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeeklyCalendarAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_week_card_item, parent, false)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(context);
        val width = deviceScreenDimension.displayWidth
        v.layoutParams = ViewGroup.LayoutParams((width - 48) / 7, 220)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WeeklyCalendarAdapter.ViewHolder, position: Int) {
        holder.bindItems(
            context,
            dateList[position],
            dateList,
            viewArrayList,
            notificationWeeklyDateObjectList,
            clickListner
        )
//        holder.initialize(dateList[position], clickListner)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            context: Context,
            weeklyItem: WeeklyItem,
            dateList: ArrayList<WeeklyItem>,
            viewArrayList: ArrayList<View>,
            notificationWeeklyDateObjectList: ArrayList<NotificationDateObject>,
            action: WeeklyCalendarAdapter.OnWeeklyCalendarItemClickListner


        ) {
            val textViewName = itemView.findViewById(R.id.weekly_name) as TextView
            val textViewAddress = itemView.findViewById(R.id.weekly_date) as TextView
            val weeklyEventIndicator = itemView.findViewById(R.id.weeklyEventIndicator) as ImageView
            textViewName.text = weeklyItem.weekName
            textViewAddress.text = weeklyItem.weekDate
            viewArrayList.add(itemView);

            System.out.println(notificationWeeklyDateObjectList)
            System.out.println(notificationWeeklyDateObjectList)


            for (i in 0 until notificationWeeklyDateObjectList.size) {
                val notificationsForWeekItem = notificationWeeklyDateObjectList.get(i)
                val v1 = convertLongToTime(notificationsForWeekItem.timestamp)
                val v2 = convertLongToTime(weeklyItem.longDate)
                if (v1.compareTo(v2) == 0) {
                    val unwrappedDrawable: Drawable? = AppCompatResources.getDrawable(
                        context,
                        R.drawable.orange_circle_background
                    );
                    val wrappedDrawable: Drawable? =
                        unwrappedDrawable?.let { DrawableCompat.wrap(it) };
                    if (wrappedDrawable != null) {
                        DrawableCompat.setTint(
                            wrappedDrawable,
                            Color.parseColor(notificationsForWeekItem.color)
                        )
                    };
                    weeklyEventIndicator.setImageDrawable(context.resources.getDrawable(R.drawable.orange_circle_background))
                }
            }


            if (weeklyItem.isSelected) {
                val weekly_name = itemView.findViewById<TextView>(R.id.weekly_name)
                val weekly_date = itemView.findViewById<TextView>(R.id.weekly_date)
                itemView.setBackgroundResource(R.drawable.dashboard_date_selector)
                weekly_name.setTextColor(context.resources.getColor(R.color.white))
                weekly_date.setTextColor(context.resources.getColor(R.color.white))
            }





            itemView.setOnClickListener {

                for (item in dateList) {
                    item.isSelected = false;
                }

                for (viewItem in viewArrayList) {
                    val weekly_name_black = viewItem.findViewById<TextView>(R.id.weekly_name)
                    val weekly_date_black = viewItem.findViewById<TextView>(R.id.weekly_date)
                    viewItem.setBackgroundResource(R.drawable.dashboard_date_none_selector)
                    weekly_name_black.setTextColor(context.resources.getColor(R.color.menu_bg_grey))
                    weekly_date_black.setTextColor(context.resources.getColor(R.color.black))
                }

                weeklyItem.isSelected = true;
                itemView.setBackgroundResource(R.drawable.dashboard_date_selector)
                val weekly_name_new = itemView.findViewById<TextView>(R.id.weekly_name)
                val weekly_date_new = itemView.findViewById<TextView>(R.id.weekly_date)
                weekly_name_new.setTextColor(context.resources.getColor(R.color.white))
                weekly_date_new.setTextColor(context.resources.getColor(R.color.white))

                action.onItemClick(weeklyItem, adapterPosition)
            }
        }

//        fun initialize(item: WeeklyItem, action: WeeklyCalendarAdapter.OnWeeklyCalendarItemClickListner) {
//
//
//            itemView.setOnClickListener {
//                action.onItemClick(item, adapterPosition)
//
//            }
//
//        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
            return simpleDateFormatForDate.format(date)
        }
    }


    interface OnWeeklyCalendarItemClickListner {
        fun onItemClick(item: WeeklyItem, position: Int)
    }


}