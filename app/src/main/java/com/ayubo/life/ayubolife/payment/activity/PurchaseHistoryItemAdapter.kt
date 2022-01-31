package com.ayubo.life.ayubolife.payment.activity

import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PurchaseHistoryItemAdapter(val context: Context, val purchaseHistoryItemArrayList: ArrayList<PurchaseHistoryItem>) : RecyclerView.Adapter<PurchaseHistoryItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PurchaseHistoryItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.purchase_history_item, parent, false)
        return PurchaseHistoryItemAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PurchaseHistoryItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(context, purchaseHistoryItemArrayList[position])
    }

    override fun getItemCount(): Int {
        return purchaseHistoryItemArrayList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, purchaseHistoryItem: PurchaseHistoryItem) {

            val data_title = itemView.findViewById(R.id.data_title) as TextView
            val data_description = itemView.findViewById(R.id.data_description) as TextView
            val data_created_datetime = itemView.findViewById(R.id.data_created_datetime) as TextView
            val data_icon = itemView.findViewById(R.id.data_icon) as ImageView
            val data_currency = itemView.findViewById(R.id.data_currency) as TextView
            val data_cost = itemView.findViewById(R.id.data_cost) as TextView

            data_title.text = purchaseHistoryItem.title
            data_description.text = purchaseHistoryItem.description
            data_created_datetime.text = purchaseHistoryItem.created_datetime.toString()
            Glide.with(context).load(purchaseHistoryItem.icon).into(data_icon)
            data_currency.text = purchaseHistoryItem.currency
            data_cost.text = purchaseHistoryItem.cost.toString()


        }


    }
}