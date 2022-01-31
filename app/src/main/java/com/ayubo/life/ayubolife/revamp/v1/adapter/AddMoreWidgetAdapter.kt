package com.ayubo.life.ayubolife.revamp.v1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.revamp.v1.model.Widget

class AddMoreWidgetAdapter(
    val context: Context,
    val dataList: ArrayList<Widget>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<AddMoreWidgetAdapter.MyViewHolder>() {

    var onWidgetItemClickListener: OnWidgetItemClickListener? = null

    interface OnWidgetItemClickListener {
        fun widgetItemClick(widget: Widget)

    }


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddMoreWidgetAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.add_more_widget_card, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, addMoreWidgetItem: Widget) {
            val mainAddMoreWidgetCardView =
                itemView.findViewById(R.id.mainAddMoreWidgetCardView) as CardView

            val mainAddMoreWidgetImageView =
                itemView.findViewById(R.id.mainAddMoreWidgetImageView) as ImageView

            val mainAddMoreWidgetTextView =
                itemView.findViewById(R.id.mainAddMoreWidgetTextView) as TextView

            mainAddMoreWidgetTextView.text = addMoreWidgetItem.title

            if (addMoreWidgetItem.activated_status) {
                mainAddMoreWidgetImageView.setBackgroundResource(0)
                mainAddMoreWidgetCardView.setCardBackgroundColor(context.resources.getColor(R.color.color_EAEAEA))
                mainAddMoreWidgetImageView.setBackgroundResource(R.drawable.ic_ok_tick)
            }

            mainAddMoreWidgetCardView.setOnClickListener {

                if (addMoreWidgetItem.activated_status) {
                    mainAddMoreWidgetImageView.setBackgroundResource(0)
                    addMoreWidgetItem.activated_status = false
                    mainAddMoreWidgetCardView.setCardBackgroundColor(context.resources.getColor(R.color.white))
                    mainAddMoreWidgetImageView.setBackgroundResource(R.drawable.grey_circle_background)
                } else {
                    mainAddMoreWidgetImageView.setBackgroundResource(0)
                    mainAddMoreWidgetCardView.setCardBackgroundColor(context.resources.getColor(R.color.color_EAEAEA))
                    addMoreWidgetItem.activated_status = true
                    mainAddMoreWidgetImageView.setBackgroundResource(R.drawable.ic_ok_tick)
                }

                onWidgetItemClickListener?.widgetItemClick(
                    addMoreWidgetItem
                )

            }

        }


    }
}