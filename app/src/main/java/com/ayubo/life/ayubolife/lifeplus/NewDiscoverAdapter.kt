package com.ayubo.life.ayubolife.lifeplus

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Chirath Perera on 2021-07-06.
 */
class NewDiscoverAdapter(
    val context: Context,
    val dataList: ArrayList<NewDiscoverCardItem>,
    var releaseAll: Boolean = false,
    var activity: Activity
) : RecyclerView.Adapter<NewDiscoverAdapter.MyViewHolder>() {

    var onClickListener: NewDiscoverAdapter.OnCardClickListener? =
        null


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnCardClickListener {
        fun onCardClickListener(action: String, meta: String)
        fun onBannerCardClickListener(action: String, meta: String)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewDiscoverAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.new_dashboard_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timer: Timer? = null;
        lateinit var my_pager: ViewPager;
        var listItems: ArrayList<SliderData> = ArrayList<SliderData>();
        fun bindItems(context: Context, newDiscoverCardItem: NewDiscoverCardItem) {


            System.out.println(newDiscoverCardItem)

            val main_card =
                itemView.findViewById(R.id.main_card) as CardView

            val banner_main =
                itemView.findViewById(R.id.banner_main) as LinearLayout

            val main_background_image_card =
                itemView.findViewById(R.id.main_background_image_card) as ImageView

            val title =
                itemView.findViewById(R.id.title) as TextView

            val description =
                itemView.findViewById(R.id.description) as TextView

            val icon =
                itemView.findViewById(R.id.icon) as ImageView

            val icon_text =
                itemView.findViewById(R.id.icon_text) as TextView

            val tabdots_programs_discover =
                itemView.findViewById(R.id.tabdots_programs_discover) as TabLayout


            my_pager =
                itemView.findViewById(R.id.my_pager) as ViewPager

            if (newDiscoverCardItem.bannerData == null) {
                main_card.visibility = View.VISIBLE
                banner_main.visibility = View.GONE
                Glide.with(context)
                    .load(newDiscoverCardItem.card_image_url)
                    .centerCrop().into(main_background_image_card);

                title.setText(newDiscoverCardItem.title)

                Glide.with(context)
                    .load(newDiscoverCardItem.icon_url)
                    .centerCrop().into(icon);

                icon_text.setText(newDiscoverCardItem.label)

                description.setText(newDiscoverCardItem.summary)


                main_card.setOnClickListener {
                    onClickListener?.onCardClickListener(
                        newDiscoverCardItem.action, newDiscoverCardItem.meta
                    )
                }
            } else {
                main_card.visibility = View.GONE
                banner_main.visibility = View.VISIBLE

                listItems = ArrayList<SliderData>();
                listItems = newDiscoverCardItem.bannerData!!

                val adapter: SliderAdapter = SliderAdapter(context, listItems);

                my_pager.adapter = adapter;

                adapter.setOnItemClickListener { action, meta ->
                    onClickListener?.onBannerCardClickListener(
                        action, meta
                    )

                }


                try {
                    if (timer == null) {
                        timer = Timer()
                        timer!!.scheduleAtFixedRate(timerTask_timerForAnimation, 2000, 3000);
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                tabdots_programs_discover.setupWithViewPager(my_pager, true);
            }


        }

        var timerTask_timerForAnimation = object : TimerTask() {
            override fun run() {


                activity.runOnUiThread(Runnable {
                    if (my_pager.currentItem < listItems.size - 1) {
                        my_pager.currentItem = my_pager.currentItem + 1;
                    } else
                        my_pager.currentItem = 0;


                })
            }
        }
    }


}


