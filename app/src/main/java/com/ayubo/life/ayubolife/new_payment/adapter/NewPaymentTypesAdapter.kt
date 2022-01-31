package com.ayubo.life.ayubolife.new_payment.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.*
import com.ayubo.life.ayubolife.new_payment.model.NewPaymentMethodTypeItem
import com.ayubo.life.ayubolife.new_payment.model.OtherPaymentImageItem
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.common_paymnet_type.view.*
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.*
import kotlinx.android.synthetic.main.dashboard_welcome_lite_card.view.*
import kotlinx.android.synthetic.main.other_paymnet_type.view.*

/**
 * Created by Chirath Perera on 2021-10-08.
 */

const val COMMON = 0
const val OTHER = 1


@IntDef(COMMON, OTHER)
@Retention(AnnotationRetention.SOURCE)
annotation class NewPaymentTypes


class NewPaymentTypesAdapter(
    val context: Context,
    val newPaymentMethodTypeItemArrayList: ArrayList<NewPaymentMethodTypeItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<NewPaymentTypesAdapter.BaseHolder>() {

    var onPaymentTypeItemClickListener: NewPaymentTypesAdapter.OnPaymentTypeItemClickListener? =
        null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnPaymentTypeItemClickListener {
        fun clickPaymentTypeItem(newPaymentMethodTypeItem: NewPaymentMethodTypeItem)

    }

    override fun getItemViewType(position: Int): Int {

        val paymentMethodType = newPaymentMethodTypeItemArrayList[position]

        return when (paymentMethodType.id) {

            "savenewcard" -> {
                COMMON
            }

            "cashondelivery" -> {
                COMMON
            }

            "cardondelivery" -> {
                COMMON
            }

            "otherpayments" -> {
                OTHER
            }

            else -> {
                COMMON
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @NewPaymentTypes viewType: Int
    ): NewPaymentTypesAdapter.BaseHolder {


        when (viewType) {
            COMMON -> {
                return CommonPaymentType(parent)
            }
            OTHER -> {
                return OtherPaymentType(parent)
            }

            else -> {
                return CommonPaymentType(parent)
            }
        }
    }

    inner class CommonPaymentType(parent: ViewGroup) :
        NewPaymentTypesAdapter.BaseHolder(parent.inflate(R.layout.common_paymnet_type)) {
        override fun bind(newPaymentMethodTypeItem: NewPaymentMethodTypeItem) = with(itemView) {

            print(newPaymentMethodTypeItem)


            if (newPaymentMethodTypeItem.enabled) {
                Glide.with(context).load(newPaymentMethodTypeItem.icon_url)
                    .into(imageViewForSaveNewCardIcon);
                textViewForCommonPaymentTypeTopic.text = newPaymentMethodTypeItem.title
                textViewForCommonPaymentTypeDescription.text = newPaymentMethodTypeItem.description
                cardViewForCommonPaymentSaveNewCard.setOnClickListener {
                    onPaymentTypeItemClickListener!!.clickPaymentTypeItem(newPaymentMethodTypeItem)
                }

                if (newPaymentMethodTypeItem.isSelect) {
                    imageViewForSelectCommonPaymentType.visibility = View.VISIBLE
                    relativeLayoutForCommonPayment.setBackgroundResource(R.drawable.theme_color_outline_with_background)
                }
            } else {
                Glide.with(context).load(newPaymentMethodTypeItem.icon_url)
                    .into(imageViewForSaveNewCardIcon);
                imageViewForSaveNewCardIcon.setColorFilter(resources.getColor(R.color.color_B3B3B3));
                textViewForCommonPaymentTypeTopic.text = newPaymentMethodTypeItem.title
                textViewForCommonPaymentTypeTopic.setTextColor(resources.getColor(R.color.color_B3B3B3))
                textViewForCommonPaymentTypeDescription.text = newPaymentMethodTypeItem.description
                textViewForCommonPaymentTypeDescription.setTextColor(resources.getColor(R.color.color_B3B3B3))


            }


            return@with
        }
    }

    inner class OtherPaymentType(parent: ViewGroup) :
        NewPaymentTypesAdapter.BaseHolder(parent.inflate(R.layout.other_paymnet_type)) {
        override fun bind(newPaymentMethodTypeItem: NewPaymentMethodTypeItem) = with(itemView) {


            if (newPaymentMethodTypeItem.enabled) {

//            textViewForOtherPaymentTypeTopic.text = newPaymentMethodTypeItem.title
//            textViewForOtherPaymentTypeDescription.text = newPaymentMethodTypeItem.description

                mainCardViewForOtherPayment.setOnClickListener {
                    onPaymentTypeItemClickListener!!.clickPaymentTypeItem(newPaymentMethodTypeItem)
                }

                cardViewForOtherPaymentType.setOnClickListener {
                    onPaymentTypeItemClickListener!!.clickPaymentTypeItem(newPaymentMethodTypeItem)
                }

                relativeLayoutForOtherPayment.setOnClickListener {
                    onPaymentTypeItemClickListener!!.clickPaymentTypeItem(newPaymentMethodTypeItem)
                }

                gridview.setOnItemClickListener { adapterView, view, i, l ->
                    onPaymentTypeItemClickListener!!.clickPaymentTypeItem(newPaymentMethodTypeItem)

                }

                val otherPaymentImages = ArrayList<OtherPaymentImageItem>()

                for (c in 0 until newPaymentMethodTypeItem.icon_urls.size) {
                    val img: String = newPaymentMethodTypeItem.icon_urls[c]
                    otherPaymentImages.add(OtherPaymentImageItem(c, img))
                }

                val newPaymentImageViewAdapter: NewPaymentImageViewAdapter =
                    NewPaymentImageViewAdapter(otherPaymentImages, context);
                gridview.adapter = newPaymentImageViewAdapter;




                if (newPaymentMethodTypeItem.isSelect) {
                    imageViewForOtherPaymentType.visibility = View.VISIBLE
                    relativeLayoutForOtherPayment.setBackgroundResource(R.drawable.theme_color_outline_with_background)
//                relativeLayoutForOtherPayment.setBackgroundColor(getResources().getColor(R.color.color_feeedf))
//                cardViewForOtherPaymentType.setCardBackgroundColor(resources.getColor(R.color.color_feeedf))
                }
            } else {
                topic.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                val otherPaymentImages = ArrayList<OtherPaymentImageItem>()

                for (c in 0 until newPaymentMethodTypeItem.icon_urls.size) {
                    val img: String = newPaymentMethodTypeItem.icon_urls[c]
                    otherPaymentImages.add(OtherPaymentImageItem(c, img))
                }

                val newPaymentImageViewAdapter: NewPaymentImageViewAdapter =
                    NewPaymentImageViewAdapter(otherPaymentImages, context);
                gridview.adapter = newPaymentImageViewAdapter;
            }

            return@with
        }

    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(newPaymentMethodTypeItem: NewPaymentMethodTypeItem) = with(itemView) { }

        open fun release() {}
    }

    override fun getItemCount(): Int {
        return newPaymentMethodTypeItemArrayList.size
    }

    override fun onBindViewHolder(holder: NewPaymentTypesAdapter.BaseHolder, position: Int) {
        if (releaseAll) {
            holder.release()
        }

        holder.bind(newPaymentMethodTypeItemArrayList[position])
    }


}