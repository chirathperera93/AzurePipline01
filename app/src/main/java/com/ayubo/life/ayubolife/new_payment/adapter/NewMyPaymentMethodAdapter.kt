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
import com.ayubo.life.ayubolife.new_payment.model.NewMyCardItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.common_paymnet_type.view.*

/**
 * Created by Chirath Perera on 2021-09-28.
 */
class NewMyPaymentMethodAdapter(
    val context: Context,
    val myNewMyCardItemArrayList: ArrayList<NewMyCardItem>,
    var releaseAll: Boolean = false,
    var isVisibleChangeCard: Boolean = true
) : RecyclerView.Adapter<NewMyPaymentMethodAdapter.MyViewHolder>() {

    val cardViewList: ArrayList<View> = ArrayList<View>();
    var onNewMyPaymentMethodItemClickListener: OnNewMyPaymentMethodItemClickListener? = null


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnNewMyPaymentMethodItemClickListener {
        fun onMyPaymentMethodClick(newMyCardItem: NewMyCardItem)
        fun onClickChangeCard()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewMyPaymentMethodAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_new_payment_method_item, parent, false)
        cardViewList.add(view)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, myNewMyCardItemArrayList[position])

    }

    override fun getItemCount(): Int {
        return myNewMyCardItemArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, newMyCardItem: NewMyCardItem) {

            val cardViewForMyPaymentCard =
                itemView.findViewById(R.id.cardViewForMyPaymentCard) as CardView
            val imageViewForMyPaymentCard =
                itemView.findViewById(R.id.imageViewForMyPaymentCard) as ImageView
            val relativeLayoutForMyPaymentCard =
                itemView.findViewById(R.id.relativeLayoutForMyPaymentCard) as RelativeLayout
            val relativeLayoutForChangeCard =
                itemView.findViewById(R.id.relativeLayoutForChangeCard) as RelativeLayout
            val imageViewForSelectMyCard =
                itemView.findViewById(R.id.imageViewForSelectMyCard) as ImageView
            val textViewForMyPaymentCardName =
                itemView.findViewById(R.id.textViewForMyPaymentCardName) as TextView
            val textViewForMyPaymentCardNumber =
                itemView.findViewById(R.id.textViewForMyPaymentCardNumber) as TextView
            val textViewForMyPaymentCardHolderName =
                itemView.findViewById(R.id.textViewForMyPaymentCardHolderName) as TextView
            val textViewForChangeCard =
                itemView.findViewById(R.id.textViewForChangeCard) as TextView


            if (newMyCardItem.enabled) {
                if (isVisibleChangeCard) {
                    relativeLayoutForChangeCard.visibility = View.VISIBLE
                    textViewForChangeCard.setOnClickListener {
                        onNewMyPaymentMethodItemClickListener?.onClickChangeCard()
                    }
                }

                print(newMyCardItem)

//            if (newMyCardItem.card_type == "VISA") {
//                imageViewForMyPaymentCard.setImageResource(R.drawable.visa_card_icon)
//                textViewForMyPaymentCardName.text = "Visa Card"
//            } else if (newMyCardItem.card_type == "MASTER") {
//                imageViewForMyPaymentCard.setImageResource(R.drawable.master_card_icon)
//                textViewForMyPaymentCardName.text = "Master Card"
//            } else {
//                imageViewForMyPaymentCard.setImageResource(R.drawable.visa_master_card_icon)
//                textViewForMyPaymentCardName.text = "Other Card"
//            }

                Glide.with(context).load(newMyCardItem.icon_url).into(imageViewForMyPaymentCard);

                textViewForMyPaymentCardName.text = newMyCardItem.card_name
                textViewForMyPaymentCardNumber.text = newMyCardItem.card_no
                textViewForMyPaymentCardHolderName.text = newMyCardItem.card_expiry



                cardViewForMyPaymentCard.setOnClickListener {

                    myNewMyCardItemArrayList.map { item -> item.isSelect = false }

                    for (cardView in cardViewList) {
                        cardView.findViewById<RelativeLayout>(R.id.relativeLayoutForMyPaymentCard)
                            .setBackgroundColor(context.resources.getColor(R.color.white));
                        cardView.findViewById<ImageView>(R.id.imageViewForSelectMyCard).visibility =
                            View.GONE
                        cardView.findViewById<TextView>(R.id.textViewForMyPaymentCardName)
                            .setTextColor(context.resources.getColor(R.color.color_3B3B3B))
                        cardView.findViewById<TextView>(R.id.textViewForMyPaymentCardNumber)
                            .setTextColor(context.resources.getColor(R.color.color_3B3B3B))
                    }


                    relativeLayoutForMyPaymentCard.setBackgroundResource(R.drawable.theme_color_outline_with_background)
                    imageViewForSelectMyCard.visibility = View.VISIBLE
                    textViewForMyPaymentCardName.setTextColor(context.resources.getColor(R.color.black))
                    textViewForMyPaymentCardNumber.setTextColor(context.resources.getColor(R.color.black))

                    onNewMyPaymentMethodItemClickListener?.onMyPaymentMethodClick(
                        newMyCardItem
                    )

                }


                if (newMyCardItem.isSelect) {
                    relativeLayoutForMyPaymentCard.setBackgroundResource(R.drawable.theme_color_outline_with_background)
                    imageViewForSelectMyCard.visibility = View.VISIBLE
                    textViewForMyPaymentCardName.setTextColor(context.resources.getColor(R.color.black))
                    textViewForMyPaymentCardNumber.setTextColor(context.resources.getColor(R.color.black))
                }
            } else {
                Glide.with(context).load(newMyCardItem.icon_url).into(imageViewForMyPaymentCard);
                imageViewForMyPaymentCard.setColorFilter(context.resources.getColor(R.color.color_B3B3B3));
                textViewForMyPaymentCardName.text = newMyCardItem.card_name
                textViewForMyPaymentCardNumber.text = newMyCardItem.card_no
                textViewForMyPaymentCardHolderName.text = newMyCardItem.card_expiry


                textViewForMyPaymentCardName.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                textViewForMyPaymentCardNumber.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                textViewForMyPaymentCardHolderName.setTextColor(context.resources.getColor(R.color.color_B3B3B3))

            }


        }


    }
}