package com.ayubo.life.ayubolife.payment.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.model.*
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_other_payment_options_cell.view.*
import kotlinx.android.synthetic.main.raw_payment_summary_lists.view.*

const val PAYMENT_SUMMARY = 1;


class PaymentSummaryAdapter(val context: Context, val list: PaymentSummaryResponse) : RecyclerView.Adapter<PaymentSummaryAdapter.BaseHolder>() {


    fun release() {
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            PAYMENT_SUMMARY -> {
                return ShowSummaryData(parent)
            }
            else -> {
                return ShowSummaryData(parent)
            }
        }
    }

    override fun getItemCount() = list.data.table.size;

    override fun onBindViewHolder(p0: BaseHolder, p1: Int) {
        p0.bind(list.data.table[p1])
    }

    override fun getItemViewType(position: Int): Int {
        return PAYMENT_SUMMARY
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }


    inner class ShowSummaryData(parent: ViewGroup) : BaseHolder(parent.inflate(R.layout.raw_payment_summary_lists)) {

        override fun bind(item: Any) = with(itemView) {

            var row = item as PaymentSummaryTable;
            raw_payment_summary_payment_name.text = row.label;
            raw_payment_summary_payment_value.text = row.value;

            return@with
        }
    }
}



