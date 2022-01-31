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
import com.ayubo.life.ayubolife.payment.model.Added_list
import com.ayubo.life.ayubolife.payment.model.PaymentSettingsResponseData
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_payment_addtobill_content.view.*
import kotlinx.android.synthetic.main.raw_payment_cell_new.view.*
import kotlin.collections.ArrayList


const val PAYMENT_SETTINGS_OPTION = 1


class PaymentSettingsAdapter(val context: Context, val list: ArrayList<PaymentSettingsResponseData>) :
        RecyclerView.Adapter<PaymentSettingsAdapter.BaseHolder>() {




    fun release() {
        notifyDataSetChanged()
    }


    var onitemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onProcessAction(action: String,meta: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): BaseHolder {
        when (viewType) {
            PAYMENT_SETTINGS_OPTION -> {
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
        return PAYMENT_SETTINGS_OPTION
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(conversation: Any) = with(itemView) {
        }
        open fun release() {
        }
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.raw_payment_cell_new)) {

        override fun bind(conversation: Any) = with(itemView) {

            val optionDetails: PaymentSettingsResponseData = conversation as PaymentSettingsResponseData
            val addedCardList=optionDetails.added_list
            tv_payment_type_heading.text=optionDetails.header


                if(addedCardList.isEmpty()){

                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                    val cell = inflater!!.inflate(R.layout.raw_payment_addtobill_content, null)

                    val imgCardview = cell.findViewById(R.id.img_cardview) as ImageView
                    val tvCardviewHeading = cell.findViewById(R.id.tv_cardview_heading) as TextView

                    tvCardviewHeading.text = optionDetails.header
                    Glide.with(context).load(optionDetails.icon_url).into(imgCardview)

                    val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.setMargins(0, 0, 0, 40)
                    cell.layoutParams = lp

                    lay_card_view.addView(cell, lp)

                    cell.setOnClickListener {
                        onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
                    }
                    img_cardview.setOnClickListener {
                        onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
                    }
                    tv_cardview_heading.setOnClickListener {
                        onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
                    }

                    tv_payment_type_heading.visibility=View.GONE
                    tv_card_addnew.visibility=View.GONE
                    img_card_addnew.visibility=View.GONE
                }


            if(addedCardList.isNotEmpty()) {

                for (i in 0 until addedCardList.size) {

                    val cardData: Added_list = addedCardList[i]
                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                    val cell = inflater!!.inflate(R.layout.raw_payment_addtobill_content, null)

                    val imgCardview = cell.findViewById(R.id.img_cardview) as ImageView
                    val tvCardviewHeading = cell.findViewById(R.id.tv_cardview_heading) as TextView

                    if(cardData.default){
                        cell.img_status.setImageResource(R.drawable.default_payment)
                    }else{
                        cell.img_status.setImageResource(R.drawable.close_payment)
                    }

                    cell.img_status.visibility=View.GONE


                    tvCardviewHeading.text = cardData.number
                    Glide.with(context).load(cardData.icon_url).into(imgCardview)

                    val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.setMargins(0, 0, 0, 40)
                    cell.layoutParams = lp

                    lay_card_view.addView(cell, lp)
                }
            }

            tv_card_addnew.text =context.getString(R.string.add_dialog_number)
            Glide.with(context).load(optionDetails.icon_url).into(img_card_addnew)

            tv_card_addnew.setOnClickListener {
                    onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
            }
            img_card_addnew.setOnClickListener {
                onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
            }
//            cell.setOnClickListener {
//                onitemClickListener?.onProcessAction(optionDetails.action,optionDetails.meta)
//            }
            return@with
        }
    }





}
