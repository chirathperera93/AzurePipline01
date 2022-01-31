package com.ayubo.life.ayubolife.home_group_view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.model.OtherPaymentOptionsData
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.*
import android.widget.LinearLayout.LayoutParams
import kotlinx.android.synthetic.main.recomonded_program_cell.view.img_image_rpc
import kotlinx.android.synthetic.main.recomonded_program_cell.view.offer_container
import kotlinx.android.synthetic.main.recomonded_program_cell.view.txt_offer
import kotlinx.android.synthetic.main.recomonded_program_cell.view.txt_program_button
import kotlinx.android.synthetic.main.recomonded_program_cell.view.txt_program_desc
import kotlinx.android.synthetic.main.recomonded_program_cell.view.txt_program_heading


const val OTHER_PAYMENT_OPTION = 1

class GroupViewAdapter(val context: Context, val list: ArrayList<GroupViewMainDataList>, val screenWidth: Int) :
        RecyclerView.Adapter<GroupViewAdapter.BaseHolder>() {
    var widthInt = screenWidth

    var onitemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onProcessAction(action: String, meta: String)
        fun onPaymentProcess(obj: PriceList)
    }


    fun release() {
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        when (viewType) {
            OTHER_PAYMENT_OPTION -> {
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
        return OTHER_PAYMENT_OPTION
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: Any) = with(itemView) {
        }

        open fun release() {
        }
    }


    inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.programs_store_groupview_cell)) {

        override fun bind(item: Any) = with(itemView) {

            val w = (widthInt * 39) / 100
            val h = (widthInt * 43) / 100

            val layoutParams = ConstraintLayout.LayoutParams(w, h)
            main_constraint.layoutParams = layoutParams

            val secondObj: GroupViewMainDataList = item as GroupViewMainDataList
            card_view_store_group.tag = secondObj

            txt_program_heading.text = secondObj.title
            txt_program_desc.text = secondObj.item_sub_text

            if (secondObj.offer_text.isNotEmpty()) {
                txt_offer.text = secondObj.offer_text
            } else {
                offer_container.visibility = View.GONE
            }

            txt_program_button.text = secondObj.item_sub_category

            var urlImage = secondObj.bg_img
            urlImage = urlImage.replace("zoom_level", "xxxhdpi")


            Glide.with(context).load(urlImage).into(img_image_rpc)
            card_view_store_group.setOnClickListener {
                onitemClickListener!!.onProcessAction(secondObj.action, secondObj.meta)
            }

            return@with
        }
    }


}