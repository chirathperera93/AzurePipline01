package com.ayubo.life.ayubolife.payment.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.model.PaymentMethodsResponseData
import com.ayubo.life.ayubolife.payment.model.PaymentMethodsResponseDataNew
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_payment_addtobill_content.view.*
import kotlinx.android.synthetic.main.raw_payment_method_cell_new.view.*

const val PAYMENT_OPTIONS = 1


class PaymentActivityAdapter(
    val context: Context,
    val paymentMethodsResponseList: PaymentMethodsResponseDataNew
) :
    RecyclerView.Adapter<PaymentActivityAdapter.BaseHolder>() {


    fun release() {
        notifyDataSetChanged()
    }

    var onitemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onPaymentProcess(obj: PriceList)
        fun onProcessAction(action: String, meta: String)
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

    override fun getItemCount() = paymentMethodsResponseList.save_payments.size;

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(paymentMethodsResponseList.save_payments[position])
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


        BaseHolder(parent.inflate(R.layout.raw_payment_method_cell_new)) {

        override fun bind(item: Any) = with(itemView) {

            val methodDetails: PaymentMethodsResponseData = item as PaymentMethodsResponseData

            val methodList = methodDetails.list;
            tv_payment_method_heading.text = methodDetails.header;


            if (methodList.isNotEmpty()) {

                for (i in 0 until methodList.size) {


                    var inflater: LayoutInflater? = null
                    inflater =
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                    assert(inflater != null)
                    val cellMain = inflater!!.inflate(R.layout.lay_cards_view_payments, null)

                    val tv_card_number_or_name =
                        cellMain.findViewById(R.id.tv_card_number_or_name) as TextView
                    val img_card_image = cellMain.findViewById(R.id.img_card_image) as ImageView


                    tv_card_number_or_name.text = methodList[i].number
                    Glide.with(context).load(methodList[i].icon_url).into(img_card_image)
                    cellMain.tag = methodDetails
                    cellMain.setOnClickListener {
                        val obj = cellMain.tag
                        val data = obj as? PaymentMethodsResponseData
                        // onitemClickListener?.onPaymentProcess("4444")
                    }

                    val layout2 = LinearLayout(context)
                    layout2.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layout2.orientation = LinearLayout.VERTICAL

                    lay_cards_view.addView(cellMain)

                    for (c in 0 until methodList[i].price_list.size) {
                        val priceObj = methodList[i].price_list.get(c)

                        var inflater: LayoutInflater? = null
                        inflater =
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                        assert(inflater != null)
                        val cell = inflater!!.inflate(R.layout.raw_payment_prices_types, null)

                        val tvPaymentType = cell.findViewById(R.id.tv_payment_type) as TextView
                        val tvPriceValue = cell.findViewById(R.id.tv_price_value) as TextView

                        tvPaymentType.text = priceObj.text
                        tvPriceValue.text = priceObj.amount

                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.setMargins(0, 0, 0, 30)
                        cell.layoutParams = lp
                        cell.tag = priceObj

                        // priceObj.meta
                        cell.setOnClickListener {
                            val obj = cell.tag as PriceList
                            onitemClickListener?.onPaymentProcess(obj)
                        }

                        // cellMain.addView(cell, lp)
                        lay_cards_view.addView(cell)

                    }


                }

            } else {
                tv_payment_method_heading.visibility = View.GONE
                lay_price_card_view.visibility = View.GONE
                tv_payment_method_heading.visibility = View.GONE

                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                val cell = inflater!!.inflate(R.layout.lay_cards_view_payments, null)

                val tv_card_number_or_name =
                    cell.findViewById(R.id.tv_card_number_or_name) as TextView
                val img_card_image = cell.findViewById(R.id.img_card_image) as ImageView

                tv_card_number_or_name.text = methodDetails.header
                Glide.with(context).load(methodDetails.icon_url).into(img_card_image)

                cell.setOnClickListener {
                    onitemClickListener?.onProcessAction(methodDetails.action, methodDetails.meta)
                }
                img_card_image.setOnClickListener {
                    onitemClickListener?.onProcessAction(methodDetails.action, methodDetails.meta)
                }
                tv_card_number_or_name.setOnClickListener {
                    onitemClickListener?.onProcessAction(methodDetails.action, methodDetails.meta)
                }


                lay_cards_view.addView(cell)


            }
//            if(methodDetails.action=="other_payment"){
//                tv_card_number_or_name.text="Other Payment Options"
//                img_card_image.setImageResource(R.drawable.dialog_card)
//                card_main.setOnClickListener {
//                    val obj = card_main.tag
//                    val data= obj as? PaymentMethodsResponseData
//
//                     onitemClickListener?.onProcessAction(methodDetails.action,methodDetails.meta)
//                }
//            }
            return@with
        }
    }


}
