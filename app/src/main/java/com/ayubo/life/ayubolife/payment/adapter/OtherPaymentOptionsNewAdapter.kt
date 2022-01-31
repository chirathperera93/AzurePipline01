package com.ayubo.life.ayubolife.payment.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.model.*

import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_other_payment_options_cell.view.*


const val OTHER_PAYMENT_OPTION_NEW = 1

class OtherPaymentOptionsNewAdapter(val context: Context, val list: PaymentMethodsResponseDataNew) :
        RecyclerView.Adapter<OtherPaymentOptionsNewAdapter.BaseHolder>() {


    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onProcessAction(action: String,meta: String)
        fun onPaymentProcess(obj: PriceList)
    }


    fun release() {
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            OTHER_PAYMENT_OPTION_NEW -> {
                return ShowPaymentOption(parent)
            }
            else -> {
                return ShowPaymentOption(parent)
            }
        }
    }

    override fun getItemCount() = list.other_payments.size;

    override fun onBindViewHolder(p0: BaseHolder, p1: Int) {
        p0.bind(list.other_payments[p1])
    }

    override fun getItemViewType(position: Int): Int {
        return OTHER_PAYMENT_OPTION_NEW
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }
        open fun release() {
        }
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.raw_other_payment_options_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val methodDetails: OtherMethodLists = item as OtherMethodLists

            tv_card_number_or_name.text=methodDetails.header

            if(methodDetails.price_list.isNotEmpty()) {
                for (c in 0 until methodDetails.price_list.size) {

                    val obj = methodDetails;
                    Glide.with(context).load(obj.icon_url).into(img_card_image)

                    tv_payment_type.text =obj.price_list[0].text
                    tv_price_value.text =obj.price_list[0].amount
                    card_main_price.tag = obj.price_list[0]

                    card_main_price.setOnClickListener {
                        val data = card_main_price.tag as PriceList
                        // val objTaged = card_main_price.tag
                        // val data = objTaged as? PriceList
                        onitemClickListener?.onPaymentProcess(data)
                        //onitemClickListener?.onProcessAction(data!!.action, data.meta)
                    }


                }
            }

            return@with
        }
    }




}