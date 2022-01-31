package com.ayubo.life.ayubolife.health

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R

class OrderMedicinePaymentMethodAdapter(
    val context: Context,
    val dataList: ArrayList<PaymentMethod>?,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<OrderMedicinePaymentMethodAdapter.MyViewHolder>() {

    var onPaymentMethodItemClickListener: OrderMedicinePaymentMethodAdapter.OnPaymentMethodItemClickListener? =
        null

    val cardViewList: ArrayList<View> = ArrayList<View>();

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }

    interface OnPaymentMethodItemClickListener {
        fun paymentMethodItemClick(paymentMethod: PaymentMethod)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderMedicinePaymentMethodAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_medicine_payment_method_item, parent, false)

        cardViewList.add(v)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList?.get(position))

    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, paymentMethod: PaymentMethod?) {

            println(paymentMethod)

            val payment_method_text =
                itemView.findViewById(R.id.payment_method_text) as TextView

            val payment_method_text_main =
                itemView.findViewById(R.id.payment_method_text_main) as LinearLayout

            payment_method_text.text = paymentMethod!!.title


            payment_method_text_main.setOnClickListener {

                for (cardView in cardViewList) {
                    cardView.findViewById<LinearLayout>(R.id.payment_method_text_main)
                        .setBackgroundResource(R.drawable.color_b3b3b3_outline_rectangle_boarder);
                }

                payment_method_text_main.setBackgroundResource(R.drawable.theme_color_outline_rectangle_boarder)

                if (paymentMethod != null) {
                    onPaymentMethodItemClickListener?.paymentMethodItemClick(
                        paymentMethod
                    )
                }

            }


        }


    }


}
