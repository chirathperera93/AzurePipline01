package com.ayubo.life.ayubolife.book_videocall

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.book_videocall.model.BookVideoCallActivityMainResponseData
import com.ayubo.life.ayubolife.book_videocall.model.BookVideoCallListData
import com.ayubo.life.ayubolife.channeling.model.Expert
import com.ayubo.life.ayubolife.home_group_view.GroupViewAdapter
import com.ayubo.life.ayubolife.home_group_view.GroupViewMainDataList
import com.ayubo.life.ayubolife.lifeplus.ListSP
import com.ayubo.life.ayubolife.lifeplus.ScrollPrograms
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_life_plus_program.*
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.*
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.img_image_rpc
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.offer_container
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.txt_offer
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.txt_program_button
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.txt_program_desc
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.txt_program_heading
import kotlinx.android.synthetic.main.recomonded_program_cell.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import java.util.concurrent.TimeUnit


const val TYPE_DETAILS = 2
const val TYPE_HEADING = 1

class BookVideoCallActivityAdapter(val context: Context, val list: ArrayList<Expert>) :
        RecyclerView.Adapter<BookVideoCallActivityAdapter.BaseHolder>() {

    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onVideoCallAction(action: Expert)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {

            TYPE_DETAILS -> {
                return ShowPaymentOption(parent)
            }
            else -> {
                return ShowPaymentOption(parent)
            }
//            else -> {
//                return ShowHeading(parent)
//            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {

        val objAny = list[position]
        var cellType: Int = 2
//        if (objAny is String) {
//            cellType=1
//        }
//        if (objAny is BookVideoCallActivityMainResponseData) {
//            cellType=2
//        }


        return cellType
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }

    private fun getDateAsString(s: Timestamp): String {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.time)

            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return df.parse(date).time
    }

    fun getInTimeText(strTime: String): String {

        var timingText = ""

        val timestamp = Timestamp(convertDateToLong(strTime))
        //Getting today date string.........
        val currentTimestamp = System.currentTimeMillis()
        val timestampToday: Timestamp = Timestamp(currentTimestamp)
        val today: String = getDateAsString(timestampToday)
        val date1 = getDateAsString(timestamp)


        val deff = com.ayubo.life.ayubolife.utility.Utility.getTimpstampDifferent(convertDateToLong(strTime), currentTimestamp)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(deff)


        if (date1.compareTo(today) == 0) {
            //    System.out.println("Date1 is a Today   "+date1)

            if (minutes > 59) {
                val result = minutes % 60
                val inthour = minutes / 60
                timingText = "in $inthour Hr $result Mins"
            } else {
                timingText = "in $minutes Mins"
            }


            Log.d(".......minits........", minutes.toString())

        } else {


            var inDays = minutes / 1440
            inDays += 1

            if (inDays.toInt() == 1) {
                timingText = "in $inDays Day"
            } else {
                timingText = "in $inDays Days"
            }

            Log.d(".............", strTime + "   " + timingText)
            //    System.out.println("Date1 is a Future   "+date1)

        }

        return timingText
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.programs_store_groupview_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val secondObj: Expert = item as Expert
            card_view_store_group.tag = secondObj

            txt_program_heading.text = secondObj.title + secondObj.name
            txt_program_desc.text = secondObj.speciality

            txt_program_heading.setTextColor(Color.parseColor("#ffffff"))
            txt_program_desc.setTextColor(Color.parseColor("#ffffff"))
            txt_program_button.setTextColor(Color.parseColor("#ffffff"))
            txt_program_button.setBackgroundResource(R.drawable.program_status_bg_green)


            offer_container.visibility = View.GONE

            if (secondObj.next != "Not found") {
                txt_program_button.text = getInTimeText(secondObj.next!!)
            }


            var urlImage = secondObj.picture
            urlImage = urlImage!!.replace("zoom_level", "xxxhdpi")

            img_image_rpc.alpha = 0.6F
            Glide.with(context).load(urlImage).into(img_image_rpc)
            card_view_store_group.setOnClickListener {
                onitemClickListener!!.onVideoCallAction(secondObj)
            }

            return@with
        }
    }


    inner class ShowHeading(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.book_videocall_cell_heading)) {

        override fun bind(item: Any) = with(itemView) {

            //   val data: String = item as String
            //    txt_heading_videocall.text=data

            return@with
        }
    }


}



