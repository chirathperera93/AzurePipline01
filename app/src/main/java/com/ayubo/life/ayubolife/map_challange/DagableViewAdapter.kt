package com.ayubo.life.ayubolife.map_challange

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.challenges.Cards
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.map_dragable_leaderboard_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem10_challange_statistics.view.*
import kotlinx.android.synthetic.main.map_dragable_tem1_share_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem2_weather_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem2_weather_layout.view.txt_description
import kotlinx.android.synthetic.main.map_dragable_tem2_weather_layout.view.txt_waiting_days_in
import kotlinx.android.synthetic.main.map_dragable_tem2_weather_layout.view.txt_waiting_days_text
import kotlinx.android.synthetic.main.map_dragable_tem4_banner_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem5_location_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem7_waiting_to_start.view.*
import kotlinx.android.synthetic.main.map_dragable_tem8_next_layout.view.*
import kotlinx.android.synthetic.main.map_dragable_tem_challange_complete.view.*
import java.text.NumberFormat


const val TYPE_LOCATION = 0
const val TYPE_BANNER = 1
const val TYPE_ADD_BOX = 2
const val TYPE_NEXT_BOX = 3
const val TYPE_PLAIN_BANNER = 4
const val TYPE_SHARE_BOX = 5
const val TYPE_MAIN = 6
const val TYPE_WAITING_TO_START = 7
const val TYPE_COMPLETE = 8
const val TYPE_IN_COMPLETE = 9
const val TYPE_STATISTICS = 10
const val TYPE_HTML_VIEW = 11
const val TYPE_MAIN_CARD_TWO = 12
const val TYPE_LEADERBOARD = 13

class DagableViewAdapter(val context: Context, val list: ArrayList<com.ayubo.life.ayubolife.challenges.Cards>) :
        RecyclerView.Adapter<DagableViewAdapter.BaseHolder>() {

    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onProcessAction(action: String, meta: String)
        fun sharePosition(card: Cards)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {

            TYPE_LOCATION -> {
                return LocationVH(parent)
            }
            TYPE_BANNER -> {
                return BannerVH(parent)
            }
            TYPE_ADD_BOX -> {
                return AddBoxVH(parent)
            }
            TYPE_NEXT_BOX -> {
                return NextBoxVH(parent)
            }
            TYPE_PLAIN_BANNER -> {
                return PlainBannerVH(parent)
            }
            TYPE_SHARE_BOX -> {
                return ShareCardVH(parent)
            }
            TYPE_MAIN -> {
                return MainCardVH(parent)
            }
            TYPE_WAITING_TO_START -> {
                return WaitingToStart(parent)
            }
            TYPE_COMPLETE -> {
                return CompletedVH(parent)
            }
            TYPE_IN_COMPLETE -> {
                return IncompletedVH(parent)
            }
            TYPE_STATISTICS -> {
                return StatisticsVH(parent)
            }
            TYPE_HTML_VIEW -> {
                return HTMLVH(parent)
            }
            TYPE_MAIN_CARD_TWO -> {
                return MainCardVH(parent)
            }
            TYPE_LEADERBOARD -> {
                return LeaderboardVH(parent)
            }
            else -> {
                return ShowHeading(parent)
            }


        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {

        val card: com.ayubo.life.ayubolife.challenges.Cards = list[position]

        var cellType: Int = 0


        if (card.type == "main_card") {
            cellType = TYPE_MAIN
        }
        if (card.type == "location") {
            cellType = TYPE_LOCATION
        }
        if (card.type == "banner") {
            cellType = TYPE_BANNER
        }
        if (card.type == "add_box") {
            cellType = TYPE_ADD_BOX
        }
        if (card.type == "next_box") {
            cellType = TYPE_NEXT_BOX
        }
        if (card.type == "plain_banner") {
            cellType = TYPE_PLAIN_BANNER
        }
        if (card.type == "share_box") {
            cellType = TYPE_SHARE_BOX
        }

        if (card.type == "waiting_to_start") {
            cellType = TYPE_WAITING_TO_START
        }
        if (card.type == "complete") {
            cellType = TYPE_COMPLETE
        }
        if (card.type == "incomplete") {
            cellType = TYPE_IN_COMPLETE
        }
        if (card.type == "statistics") {
            cellType = TYPE_STATISTICS
        }
        if (card.type == "html_card") {
            cellType = TYPE_HTML_VIEW
        }
        if (card.type == "main_card_2") {
            cellType = TYPE_MAIN_CARD_TWO
        }
        if (card.type == "leaderboard") {
            cellType = TYPE_LEADERBOARD
        }

        return cellType
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }

    inner class PlainBannerVH(parent: ViewGroup) :
//            BaseHolder(parent.inflate(R.layout.map_dragable_tem3_plan_layout)) {
            BaseHolder(parent.inflate(R.layout.map_dragable_tem7_waiting_to_start)) {

        override fun bind(item: Any) = with(itemView) {

            val data = item as Cards

            val parts = data.heading.split(" ")

            txt_waiting_days_in7.setText(parts.get(1))
            txt_waiting_days7.visibility = View.GONE
            txt_waiting_days_text7.visibility = View.GONE

            return@with
        }
    }

    inner class LocationVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem5_location_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(obj.icon).apply(requestOptionsSamll).into(main_bg_banner5)

            // txt_current_locationcity.setText("Kelaniya")

            return@with
        }
    }

    inner class AddBoxVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem6_addbox_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

//            txt_heading.text=  obj.heading
//            txt_subheading.text=obj.subheading
//            txt_desc.text=obj.text

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            //    Glide.with(context).load(obj.image).apply(requestOptionsSamll).into(main_bg_banner6)


            return@with
        }
    }

    inner class BannerVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem4_banner_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

            txt_heading55.text = obj.name
            txt_description55.text = obj.text

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(obj.image).apply(requestOptionsSamll).into(main_bg_banner4)

            main_bg_banner4.setOnClickListener(View.OnClickListener {
                onitemClickListener!!.onProcessAction(obj.action, obj.meta)
            })

            return@with
        }
    }

    inner class NextBoxVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem8_next_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

            txt_4_city.text = obj.name
            txt_4_steps.text = obj.stepstonext

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            // Glide.with(context).load(obj.icon).apply(requestOptionsSamll).into(img_small_item8)

            return@with
        }
    }

    inner class LeaderboardVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_leaderboard_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

//            txt_heading.text=obj.name
//            txt_description.text=obj.stepstonext

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(obj.icon).apply(requestOptionsSamll).into(main_bg_banner_leaderboard)


            return@with
        }
    }

    inner class HTMLVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem11_html_view)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

//            val webSettings = webView_dragable_htmlview.settings
//            webSettings.domStorageEnabled = true
//            webSettings.javaScriptEnabled = true
//            webSettings.allowFileAccess = true
//            webView_dragable_htmlview.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
//            webView_dragable_htmlview.isLongClickable = true
//            webView_dragable_htmlview.loadData(obj.text, "text/html; charset=utf-8", "UTF-8")

            return@with
        }
    }

    inner class StatisticsVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem10_challange_statistics)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

            var steps_with_comma: String? = null
            if (isStringInt(obj.degree.toString())) {
                val steps = Integer.parseInt(obj.degree.toString())
                steps_with_comma = NumberFormat.getIntegerInstance().format(steps.toLong())
                txt_avg_steps_sta.text = steps_with_comma
            } else {
                txt_avg_steps_sta.text = "0"
            }

            txt_no_of_tresures.text = obj.min_degree.toString()
            txt_raffle_draw_entries.text = obj.max_degree.toString()

//
            return@with
        }
    }


    inner class IncompletedVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem9_challange_incomplete)) {

        override fun bind(item: Any) = with(itemView) {

            val data = item as Cards

            return@with
        }
    }

    inner class CompletedVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem_challange_complete)) {

        override fun bind(item: Any) = with(itemView) {

            val data = item as Cards

            if (item.name == null) {
                txt_reached_city.text = data.text
            } else {
                txt_reached_city.text = data.name
            }

            return@with
        }
    }

    inner class WaitingToStart(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem7_waiting_to_start)) {

        override fun bind(item: Any) = with(itemView) {

            val data = item as Cards

            txt_waiting_days7.text = data.remaining_days.toString()

            return@with
        }
    }


    inner class ShareCardVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem1_share_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as Cards

            txt_6_text.text = obj.status

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(obj.bg_img).apply(requestOptionsSamll).into(main_bg_banner_sharebox)

            main_bg_banner_sharebox.setOnClickListener(View.OnClickListener {
                onitemClickListener!!.sharePosition(obj)
            })


            return@with
        }
    }

    inner class MainCardVH(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.map_dragable_tem2_weather_layout)) {

        override fun bind(item: Any) = with(itemView) {

            val obj = item as com.ayubo.life.ayubolife.challenges.Cards

            val path = obj.weather_icon

            val requestOptionsSamll = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context).load(path).apply(requestOptionsSamll).into(img_weather_icon)

            val completedArray = obj.max_degree.toString()
            val remainingArray = obj.min_degree.toString()



            header_text.text = obj.name
            txt_description.text = obj.description

            txt_value.text = obj.degree.toString()

            var remaining_inhowmany_days = ""
            var intnoofdays: Int = 0

            intnoofdays = obj.remaining_days

            if (intnoofdays == 0) {
                txt_waiting_days_in.text = ""
                txt_waiting_days_text.text = ""
                txt_remaining_inhowmany_days.text = "Today"

            } else if (intnoofdays == 1) {
                txt_waiting_days_in.text = ""
                txt_waiting_days_text.text = ""
                txt_remaining_inhowmany_days.text = "Tomorrow"

            } else if (remaining_inhowmany_days == "0") {
                txt_waiting_days_text.text = ""
                txt_remaining_inhowmany_days.text = "--"
                txt_waiting_days_in.text = ""
//                txt_remaining_inper_day.text = ""
            } else {
                txt_waiting_days_text.text = " days"
                txt_remaining_inhowmany_days.text = remaining_inhowmany_days
                txt_waiting_days_in.text = "In "
            }


            txt_remaining_steps.text = obj.remaining_steps.toString()
            txt_remaining_inhowmany_days.text = obj.remaining_days.toString()
//            txt_remaining_inper_day.text = obj.remaining_avg.toString() + "/Day"

            txt_completed_steps.text = obj.completed_steps.toString()
            txt_completed_inhowmany_days.text = obj.completed_days.toString()
//            txt_completed_inper_day.text = obj.completed_avg.toString() + "/Day"

            return@with
        }
    }

    fun isStringInt(s: String): Boolean {
        try {
            Integer.parseInt(s)
            return true
        } catch (ex: NumberFormatException) {
            return false
        }

    }


    inner class ShowHeading(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.ask_header_cell)) {

        override fun bind(item: Any) = with(itemView) {

            //   val data = item as Cards

//            txt_heading.text=data.heading
//            txt_heading.text

            return@with
        }
    }


}


