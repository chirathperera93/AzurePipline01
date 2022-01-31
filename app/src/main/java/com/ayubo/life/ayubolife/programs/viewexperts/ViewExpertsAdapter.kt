package com.ayubo.life.ayubolife.programs.viewexperts

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.fragments.CircleTransform
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.reports.getareview.ReviewExperts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.ask_header_cell.view.*
import kotlinx.android.synthetic.main.ask_main_cell.view.*


const val TYPE_EXPERT =1


class ViewExpertsAdapter (val context: Context, val list: ArrayList<Experts>) :
        RecyclerView.Adapter<ViewExpertsAdapter.BaseHolder>() {

    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onProcessAction(action: String,meta: String)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {

            TYPE_EXPERT -> {
                return ShowPrograms(parent)
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
        return TYPE_EXPERT
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }
        open fun release() {
        }
    }





    inner class ShowPrograms(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.ask_main_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val data: Experts = item as Experts

            txt_expertName.text=data.full_name
            txt_specility.text=data.speciality

            val requestOptions =
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(CircleTransform(context))

            Glide.with(context).load(data.profile_picture).apply(requestOptions).into(img_expert_image)
            main_view.setOnClickListener {
                    onitemClickListener!!.onProcessAction(data.action!!,data.meta!!)

            }
            return@with
        }
    }
    inner class ShowHeading(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.ask_header_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val data: String = item as String

            txt_heading_ask.text=data



            return@with
        }
    }


}



