package com.ayubo.life.ayubolife.reports.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.reports.response.Reports
import kotlinx.android.synthetic.main.cell_report_review_add.view.*


const val REPORT_REVIEW_ADDING = 1


class AddReportReviewAdapter(val context: Context, val list: ArrayList<Reports>) :
        RecyclerView.Adapter<AddReportReviewAdapter.BaseHolder>() {




    fun release() {
        notifyDataSetChanged()
    }


    var onitemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onProcessAction(action: String,meta: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            REPORT_REVIEW_ADDING -> {
                return ShowPaymentOption(parent)
            }
            else -> {
                return ShowPaymentOption(parent)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return REPORT_REVIEW_ADDING
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(conversation: Any) = with(itemView) {
        }
        open fun release() {
        }
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.cell_report_review_add)) {

        override fun bind(conversation: Any) = with(itemView) {

            val obj: Reports = conversation as Reports


            txt_report_name.text=obj.reportName
            txt_report_description.text=obj.reportDate

            return@with
        }
    }





}

