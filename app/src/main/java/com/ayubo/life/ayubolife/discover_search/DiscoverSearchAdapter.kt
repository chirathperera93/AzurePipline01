package com.ayubo.life.ayubolife.discover_search

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import kotlinx.android.synthetic.main.raw_main_search_cell.view.*


const val PAYMENT_OPTIONS = 1


class DiscoverSearchAdapter(val context: Context, val list: List<DiscoverSearchData>) :
    RecyclerView.Adapter<DiscoverSearchAdapter.BaseHolder>() {


    fun release() {
        notifyDataSetChanged()
    }

    var onitemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onDiscoverSearch(id: String, name: String, type: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            PAYMENT_OPTIONS -> {
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
        return PAYMENT_OPTIONS
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.raw_main_search_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val searchData: DiscoverSearchData = item as DiscoverSearchData
            txt_searched_tag.text = searchData.name

            txt_searched_tag.setOnClickListener {
                onitemClickListener!!.onDiscoverSearch(
                    searchData.id.toString(),
                    searchData.name,
                    searchData.type
                )
            }

            return@with
        }
    }


}
