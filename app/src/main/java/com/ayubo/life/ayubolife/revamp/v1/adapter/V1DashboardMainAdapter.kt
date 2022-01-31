package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.SliderData
import com.ayubo.life.ayubolife.revamp.v1.model.DashboardCardItem
import java.util.*
import kotlin.collections.ArrayList

class V1DashboardMainAdapter(
    val context: Context,
    val dataList: ArrayList<DashboardCardItem>,
    var releaseAll: Boolean = false,
) : RecyclerView.Adapter<V1DashboardMainAdapter.MyViewHolder>() {


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): V1DashboardMainAdapter.MyViewHolder {
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


        fun bindItems(context: Context, dashboardCardItem: DashboardCardItem) {


            System.out.println(dashboardCardItem)





        }

    }

}
