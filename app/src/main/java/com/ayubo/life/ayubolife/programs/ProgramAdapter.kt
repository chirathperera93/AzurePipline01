package com.ayubo.life.ayubolife.programs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.programs.data.model.Program
import com.ayubo.life.ayubolife.programs.data.model.Tag
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.utility.Ram
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listitem_program_date_heading.view.*
import kotlinx.android.synthetic.main.listitem_program_future.view.*
import kotlinx.android.synthetic.main.listitem_program_present.view.*
import kotlinx.android.synthetic.main.listitem_program_present.view.main_cell_bg
import kotlinx.android.synthetic.main.listitem_program_present.view.tags_view
import kotlinx.android.synthetic.main.listitem_program_present.view.txt_description
import kotlinx.android.synthetic.main.listitem_program_present.view.txt_heading
import kotlinx.android.synthetic.main.listitem_program_today.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


const val PROGRAMS_PAST = 1
const val PROGRAMS_PRESENT = 2
const val PROGRAMS_FUTURE = 3
const val PROGRAMS_HEADING_DATE = 4
const val PROGRAMS_HEADING_TODAY = 5
const val PROGRAMS_HEADING_UPNEXT = 6
const val PROGRAMS_SEND_A_KITE = 7

@IntDef(
    PROGRAMS_PAST,
    PROGRAMS_PRESENT,
    PROGRAMS_FUTURE,
    PROGRAMS_HEADING_DATE,
    PROGRAMS_HEADING_TODAY,
    PROGRAMS_HEADING_UPNEXT
)
@Retention(AnnotationRetention.SOURCE)
annotation class ChatViewTypes


class ProgramAdapter(
    val context: Context,
    val list: ArrayList<Any>,
    val complete: Int,
    val fullcount: Int
) :
    RecyclerView.Adapter<ProgramAdapter.BaseHolder>() {


    var isFutureDataAvailable = false
    var onitemClickListener: OnItemClickListener? = null

    fun release() {

        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onProcessActionClick(activityName: String, meta: String)
        fun onUpdateAdapter(activityName: String, meta: String)

//        fun onPostClick(position: String)
//        fun onVideoCallClick(activityName: String,meta: String)
//        fun onMapChallangeClick(activityName: String,meta: String)
//        fun onAskClick(activityName: String,meta: String)
//
//        fun onProgramNewDesignClick(activityName: String,meta: String)
//        fun onProgramPostClick(activityName: String,meta: String)
//        fun onButtonChannelingClick(activityName: String,meta: String)
//        fun onDyanamicQuestionClick(activityName: String,meta: String)
//
//        fun onJanashakthiWelcomeClick(activityName: String,meta: String)
//        fun onJanashakthiReportsClick(activityName: String,meta: String)
//
//        fun onOpenImageClick(activityName: String,meta: String)
//        fun onOpenVideoClick(activityName: String,meta: String)
//        fun onChatQuesClick(activityName: String,meta: String)
//        fun onOpenNativePost(activityName: String,meta: String)


    }

    fun addConversation(conversation: Program) {

        list.add(conversation)
        notifyItemInserted(list.size - 1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, @ChatViewTypes viewType: Int): BaseHolder {

        when (viewType) {
            PROGRAMS_PAST -> {
                return presentANDPastPost(parent)
            }
            PROGRAMS_PRESENT -> {
                return presentANDPastPost(parent)
            }
            PROGRAMS_FUTURE -> {
                return futurePost(parent)
            }
            PROGRAMS_HEADING_DATE -> {
                return dateHeading(parent)
            }
            PROGRAMS_HEADING_TODAY -> {
                return todayHeading(parent)
            }
            PROGRAMS_HEADING_UPNEXT -> {
                return upNextHeading(parent)
            }
            PROGRAMS_SEND_A_KITE -> {
                return todayNoData(parent)
            }
            else -> {
                return todayHeading(parent)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        val objAny = list[position]


        var cellType: Int = 0


        if (objAny is String) {
            // obj is a String
            val todayTimestamp = System.currentTimeMillis()
            var timestampToday: Timestamp = Timestamp(todayTimestamp)

            val sdf = SimpleDateFormat("MM/dd/yyyy")
            var cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 1)
            val tomorrow = sdf.format(cal.getTime())


            var stringData: String = objAny
            var today: String = getDateAsString(timestampToday)

            if (stringData == today) {
                cellType = PROGRAMS_HEADING_TODAY
            } else if (stringData == tomorrow) {
                cellType = PROGRAMS_HEADING_UPNEXT
            } else if (stringData == "kite") {
                cellType = PROGRAMS_SEND_A_KITE
            } else {
                cellType = PROGRAMS_HEADING_DATE
            }

        }
        if (objAny is Program) {

            var conversation: Program = objAny as Program


            val todayTimestamp = System.currentTimeMillis()
            val ts = todayTimestamp.toString()

            var timestampToday: Timestamp = Timestamp(todayTimestamp)
            var timestamp2: Timestamp = Timestamp(conversation.timestamp.toLong() * 1000)

            // timestamp2 is before day

            if (timestampToday.after(timestamp2)) {
                //History
                cellType = PROGRAMS_PAST
                // System.out.println("timestamp2 is before today")
            }
            if (timestampToday.before(timestamp2)) {
                //Future
                cellType = PROGRAMS_FUTURE
                // System.out.println("timestamp1 is before timestamp2")
            }
            var timestamp = Timestamp(conversation.timestamp.toLong() * 1000)

            if (getDateAsString(timestampToday) == getDateAsString(timestamp)) {
                //Present
                cellType = PROGRAMS_PRESENT
            }

        }




        return cellType

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

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(conversation: Any) = with(itemView) {

        }

        open fun release() {

        }
    }

    fun isRead(obj: Program): Boolean {
        return obj.is_read == "0"
    }

    fun isDisabled(obj: Program): Boolean {
        return obj.disabled == "1"
    }

    fun isCompleted(obj: Program): Boolean {
        return obj.progress_status == "complete"
    }

    fun isInProgress(obj: Program): Boolean {
        return obj.progress_status == "inprogress"
    }

    fun isIncomplete(obj: Program): Boolean {
        return obj.progress_status == "incomplete"
    }


    inner class presentANDPastPost(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_present)) {

        override fun bind(objAny: Any) = with(itemView) {

            val pos = adapterPosition

            var conversation: Program = objAny as Program

            Glide.with(context).load(conversation.icon).into(img_main_icon)

            txt_heading.text = conversation.heading
            sub_heading.text = conversation.sub_heading
            txt_description.text = conversation.text


            // Set Defaults
            main_cell_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

            lay_progressview.setBackgroundColor(Color.parseColor("#ffffff"))
            circleView.setBackgroundColor(Color.parseColor("#ffffff"))
            card_main.setCardBackgroundColor(Color.parseColor("#ffffff"))

            main_cell_bg.background.alpha = 0
            txt_heading.alpha = 1.0F
            txt_description.alpha = 1.0F
            img_main_icon.alpha = 1.0F
            sub_heading.alpha = 1.0F
            img_complete_tick.alpha = 1.0F


            //Sub Heading
            if (isCompleted(conversation)) {
                sub_heading.setTextColor(Color.parseColor("#ffffff"))
                sub_heading.setBackgroundResource(R.drawable.program_status_bg_green)
            } else if (isInProgress(conversation)) {
                sub_heading.setTextColor(Color.parseColor("#ffffff"))
                sub_heading.setBackgroundResource(R.drawable.program_status_bg_green)
            } else {
                if (isDisabled(conversation)) {
                    sub_heading.setBackgroundResource(R.drawable.program_status_bg_red)
                    sub_heading.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    sub_heading.setBackgroundResource(R.drawable.program_status_bg_orange_border)
                    sub_heading.setTextColor(Color.parseColor("#FF8E25"))
                }
            }


            //Tick
            if (isCompleted(conversation)) {
                img_complete_tick.visibility = View.VISIBLE
            } else {
                img_complete_tick.visibility = View.GONE
            }


            //Arrow
            if (isIncomplete(conversation)) {
                img_progress_next.visibility = View.VISIBLE
            } else {
                img_progress_next.visibility = View.GONE
            }


            // For Challange
            if ((conversation.progress_status == "inprogress") && (conversation.type == "challenge")) {
                txt_progress_value.visibility = View.VISIBLE
                progressBar_Program_Circle.visibility = View.VISIBLE
                val psernage = conversation.progress_progress + "%"
                img_progress_next.visibility = View.GONE
                img_complete_tick.visibility = View.GONE
                txt_progress_value.text = psernage
            } else {
                txt_progress_value.visibility = View.GONE
                progressBar_Program_Circle.visibility = View.GONE
            }

            //Disabling
            if (isDisabled(conversation)) {
                lay_progressview.setBackgroundColor(Color.parseColor("#e1e1e1"))
                circleView.setBackgroundColor(Color.parseColor("#e1e1e1"))
                card_main.setCardBackgroundColor(Color.parseColor("#e1e1e1"))

                txt_heading.alpha = 0.2F
                txt_description.alpha = 0.2F
                img_main_icon.alpha = 0.2F
                sub_heading.alpha = 0.2F
                img_complete_tick.alpha = 0.2F

                main_cell_bg.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.program_past_cell_bg
                    )
                )
                main_cell_bg.background.alpha = 128
            }

            //Read
            if (isRead(conversation)) {
                main_cell_bg.setBackgroundColor(Color.parseColor("#fef6e5"))
                lay_progressview.setBackgroundColor(Color.parseColor("#fef6e5"))
                circleView.setBackgroundColor(Color.parseColor("#fef6e5"))
                card_main.setCardBackgroundColor(Color.parseColor("#fef6e5"))
            }

            var tagsList: List<Tag>

            tagsList = conversation.tags

            if (tagsList.isEmpty()) {
                tags_view.visibility = View.GONE
            } else {
                tags_view.visibility = View.VISIBLE
            }

            tags_view.removeAllViews()
            for (c in 0 until tagsList.size) {

                val tag: Tag = tagsList.get(c)

                val tagImage: ImageView = ImageView(context)

                var paramsImage: ViewGroup.LayoutParams = ViewGroup.LayoutParams(35, 35)
                tagImage.layoutParams = paramsImage
                Glide.with(context).load(tag.icon).into(tagImage)
                tags_view.addView(tagImage)

                val textview: TextView = TextView(context)
                var params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textview.layoutParams = params
                textview.text = tag.text
                textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8F)
                textview.setTextColor(Color.parseColor("#606060"))
                textview.setTypeface(textview.typeface, Typeface.NORMAL)
                textview.setPadding(5, 0, 10, 0)
                tags_view.addView(textview)

                if (isDisabled(conversation)) {
                    tags_view.alpha = 0.2F
                }

            }

            progressBar_Program_Circle.progress = 0
            progressBar_Program_Circle.max = 100
            progressBar_Program_Circle.progress = Integer.parseInt(conversation.progress_progress)

            main_cell_bg.setOnClickListener {

                Log.d("Status........", conversation.progress_status)
                Log.d("Disable........", conversation.disabled)

                val pos2 = adapterPosition
                Ram.setProgramLastPosition(pos2)

                if (conversation.disabled == "1") {

                } else {

                    conversation.is_read = "1"
                    onitemClickListener?.onUpdateAdapter("", conversation.meta)
                    setReadProgram(Integer.toString(conversation.id))

                    onitemClickListener?.onProcessActionClick(
                        conversation.action,
                        conversation.meta
                    )

                }
            }

            return@with
        }
    }

    inner class todayNoData(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_today_nodata)) {

        @SuppressLint("SetTextI18n")
        override fun bind(conversation: Any) = with(itemView) {

            val pref: PrefManager
            pref = PrefManager(context)
            val username = pref.loginUser["name"]

            txt_description.text =
                "$username, You have no tasks for today but you can view your upcoming tasks below and your past tasks above."

            return@with
        }
    }

    inner class todayHeading(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_today)) {

        override fun bind(objAny: Any) = with(itemView) {

            txt_heading_today.visibility = View.VISIBLE
            txt_heading_today.text = "Today"
            var compledtText1 = Integer.toString(complete)
            var compledtText2 = Integer.toString(fullcount)
            var fullText = "Completed " + compledtText1 + " of " + compledtText2
            txt_heading2.text = fullText

            return@with
        }
    }

    inner class upNextHeading(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_today)) {

        override fun bind(objAny: Any) = with(itemView) {

            txt_heading_today.visibility = View.VISIBLE
            txt_heading_today.text = "Up Next"
            txt_heading2.text = "Tomorrow"
            return@with
        }
    }

    inner class dateHeading(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_date_heading)) {

        override fun bind(objAny: Any) = with(itemView) {
            var dateString: String = objAny as String


            //   var text= dateString.replace('/','-')
            txt_date_text.text = getTimeStringFromInteger(dateString)

            return@with
        }

        fun getTimeStringFromInteger(strDate: String): String {

            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val date2 = sdf.parse(strDate)

            val cal = Calendar.getInstance()
            cal.time = date2

            val date = cal.get(Calendar.DATE)
            val todayDate = Date()


            var formatter: SimpleDateFormat? = null

            formatter = SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z")

            val dateFromDB_forADay = formatter.format(cal.time)
            val parts = dateFromDB_forADay.split("X".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()

            return parts[0]
        }

    }


    inner class futurePost(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_program_future)) {

        override fun bind(objAny: Any) = with(itemView) {

            var conversation: Program = objAny as Program



            txt_heading.text = conversation.heading


            val text_view: TextView = TextView(context)
            Glide.with(context).load(conversation.icon).into(img_main_icon_future)

            txt_description.text = conversation.text

            //    main_cell_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.program_past_cell_bg))
            //   main_cell_bg.background.alpha = 128
            img_main_icon_future.alpha = 0.2F

            return@with
        }


    }


    fun setReadProgram(id: String) {

        val pref: PrefManager
        pref = PrefManager(context)

        val apiService = ApiClient.getNewApiClient().create(ApiInterface::class.java)
        val call = apiService.setReadPrograms(pref.userToken, id)
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val status = response.isSuccessful
                if (status) {
                    if (response.body() != null) {
                        println("=============")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("========t======$t")
            }
        })
    }


}