package com.ayubo.life.ayubolife.reports.upload

import android.content.Context
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.ask.AskData
import com.ayubo.life.ayubolife.fragments.CircleTransform
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.ask_header_cell.view.*
import kotlinx.android.synthetic.main.ask_main_cell.view.*
import kotlinx.android.synthetic.main.report_upload_thum_layout.view.*


const val TYPE_EXPERT =1
const val TYPE_HEADING =2

class UploadReportAdapter (val context: Context, val list: ArrayList<AddedReportsEntity>) :
        RecyclerView.Adapter<UploadReportAdapter.BaseHolder>() {

    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onRemoveReport(action: Int,item: AddedReportsEntity)
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
        open fun bind(item: AddedReportsEntity) = with(itemView) {
        }
        open fun release() {
        }
    }





    inner class ShowPrograms(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.report_upload_thum_layout)) {

        override fun bind(item: AddedReportsEntity) = with(itemView) {

            img_report_thumbnail.setImageBitmap(item.image)

            System.out.println("========================"+item.id)

            img_close.setOnClickListener {
             val pos= adapterPosition

                    onitemClickListener!!.onRemoveReport(pos,item)

            }
            return@with
        }
    }
    inner class ShowHeading(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.ask_header_cell)) {

        override fun bind(item: AddedReportsEntity) = with(itemView) {

//            val data: String = item as String
//
//            txt_heading_ask.text=data



            return@with
        }
    }


}

