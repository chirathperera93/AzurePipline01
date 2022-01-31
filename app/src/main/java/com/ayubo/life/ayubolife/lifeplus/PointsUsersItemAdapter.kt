package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class PointsUsersItemAdapter(
    val context: Context,
    private val pointUserItemList: ArrayList<PointUser>,
    var clickListener: PointsUsersItemAdapter.OnPointUserItemClickListener
) : RecyclerView.Adapter<PointsUsersItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PointsUsersItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.point_leader_board_item, parent, false)
        return PointsUsersItemAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PointsUsersItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(context, pointUserItemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return pointUserItemList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            context: Context,
            pointUser: PointUser,
            action: PointsUsersItemAdapter.OnPointUserItemClickListener
        ) {

            val mainRelativeLayout =
                itemView.findViewById(R.id.main_relative_layout) as RelativeLayout
            val userLinearLayout = itemView.findViewById(R.id.user_linear_layout) as LinearLayout
            val txtUserPosition = itemView.findViewById(R.id.txt__user_position) as TextView
            val imgPointUser = itemView.findViewById(R.id.img_point_user) as ImageView
            val txtPointUsername = itemView.findViewById(R.id.txt_point_username) as TextView
            val txtPointUserCompany = itemView.findViewById(R.id.txt_point_user_company) as TextView
            val txtUserPointsValue = itemView.findViewById(R.id.txt_user_points_value) as TextView
            val mainLinearLayoutForManualSteps =
                itemView.findViewById(R.id.mainLinearLayoutForManualSteps) as LinearLayout
            val manualStepImageView = itemView.findViewById(R.id.manualStepImageView) as ImageView
            val manual_step_label = itemView.findViewById(R.id.manual_step_label) as TextView

//            if (pointUser.manual_steps) {
            mainLinearLayoutForManualSteps.visibility = View.VISIBLE
//            Glide.with(context).load(pointUser.record_label.icon_url).into(manualStepImageView)


            if (pointUser.record_label.icon_url != "") {
                manualStepImageView.visibility = View.VISIBLE
                Glide.with(context).load(pointUser.record_label.icon_url).into(manualStepImageView)
            } else {
                manualStepImageView.visibility = View.GONE
            }


            if (pointUser.record_label.text != "") {
                manual_step_label.visibility = View.VISIBLE
                manual_step_label.text = pointUser.record_label.text
                if (pointUser.record_label.color != "") {
                    manual_step_label.setTextColor(Color.parseColor(pointUser.record_label.color))
                }
            } else {
                manual_step_label.visibility = View.GONE
            }


//            }

            mainRelativeLayout.setBackgroundColor(context.resources.getColor(R.color.white))
            userLinearLayout.setBackgroundColor(context.resources.getColor(R.color.white))

            if (pointUser.me) {
                mainRelativeLayout.setBackgroundColor(context.resources.getColor(R.color.point_leader_board_me))
                userLinearLayout.setBackgroundColor(context.resources.getColor(R.color.point_leader_board_me))
            }


//            DateUtils.getRelativeTimeSpanString(endDate.getTime(), startDate.getTime(), DateUtils.FORMAT_ABBREV_RELATIVE)
//            val x = DateUtils.getRelativeTimeSpanString(
//                context,
//                pointUser.last_updated_time,
//                true
//            )

            val timeFromNow = DateUtils.getRelativeTimeSpanString(
                pointUser.last_updated_time,
                System.currentTimeMillis(),
                0,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )


            val pattern: String = "###,###,###";
            val decimalFormat: DecimalFormat = DecimalFormat(pattern)

            val stepsValueFormat: String = decimalFormat.format(pointUser.points)


            txtUserPosition.text = pointUser.position.toString()
            Glide.with(context).load(pointUser.image_url).into(imgPointUser)
            txtPointUsername.text = pointUser.first_name + " " + pointUser.last_name
            txtPointUserCompany.text = timeFromNow
//            txtPointUserCompany.text = convertLongToTime(pointUser.last_updated_time)
//            txtUserPointsValue.text = pointUser.points.toString() + " " + pointUser.points_label
            txtUserPointsValue.text = stepsValueFormat + " " + pointUser.points_label

            mainRelativeLayout.setOnClickListener {
                action.onItemClick(pointUser)
            }


        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val simpleDateFormatForDate = SimpleDateFormat("EE, dd MM yyyy hh:mm aaa");
            return simpleDateFormatForDate.format(date)
        }


    }

    interface OnPointUserItemClickListener {
        fun onItemClick(pointUser: PointUser)
    }


}