package com.ayubo.life.ayubolife.new_payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.new_payment.model.NewPaymentMethodItem

/**
 * Created by Chirath Perera on 2021-09-28.
 */
class NewPaymentsMethodAdapter(
    val context: Context,
    val newPaymentMethodArrayList: ArrayList<NewPaymentMethodItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<NewPaymentsMethodAdapter.MyViewHolder>() {

    val cardViewList: ArrayList<View> = ArrayList<View>();
    var onNewPaymentMethodItemClickListener: OnNewPaymentMethodItemClickListener? =
        null


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnNewPaymentMethodItemClickListener {
        fun onPaymentMethodChangeClick()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewPaymentsMethodAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_payment_method_item, parent, false)
        cardViewList.add(view)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, newPaymentMethodArrayList[position])

    }

    override fun getItemCount(): Int {
        return newPaymentMethodArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, newPaymentMethodItem: NewPaymentMethodItem) {

            val cardViewForMainCard = itemView.findViewById(R.id.cardViewForMainCard) as CardView
            val relativeLayoutForSelectItem =
                itemView.findViewById(R.id.relativeLayoutForSelectItem) as RelativeLayout
            val imageViewForSelectItem =
                itemView.findViewById(R.id.imageViewForSelectItem) as ImageView
            val textViewForPaymentMethodName =
                itemView.findViewById(R.id.textViewForPaymentMethodName) as TextView
            val textViewForNumber = itemView.findViewById(R.id.textViewForNumber) as TextView
            val textViewForChangePaymentAction =
                itemView.findViewById(R.id.textViewForChangePaymentAction) as TextView

            relativeLayoutForSelectItem.setBackgroundResource(R.drawable.theme_color_outline_rectangle_boarder)
            imageViewForSelectItem.visibility = View.VISIBLE
            textViewForPaymentMethodName.setTextColor(context.resources.getColor(R.color.black))
            textViewForNumber.setTextColor(context.resources.getColor(R.color.black))
            textViewForChangePaymentAction.setOnClickListener {
                onNewPaymentMethodItemClickListener?.onPaymentMethodChangeClick()
            }


        }


    }
}