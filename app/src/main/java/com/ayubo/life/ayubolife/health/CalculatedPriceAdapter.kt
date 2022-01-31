package com.ayubo.life.ayubolife.health

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R

/**
 * Created by Chirath Perera on 2021-07-22.
 */
class CalculatedPriceAdapter(
    val context: Context,
    val dataList: ArrayList<PaymentItems>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<CalculatedPriceAdapter.MyViewHolder>() {


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalculatedPriceAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_medicine_calculated_price_item, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, calculatedPriceItem: PaymentItems) {


            val payment_item_name = itemView.findViewById(R.id.payment_item_name) as TextView
            val payment_item_value = itemView.findViewById(R.id.payment_item_value) as TextView
            val payment_item_currency =
                itemView.findViewById(R.id.payment_item_currency) as TextView

            payment_item_name.setText(calculatedPriceItem.name)
            payment_item_value.setText(calculatedPriceItem.amount)
            payment_item_currency.setText(calculatedPriceItem.currency)


        }


    }


}