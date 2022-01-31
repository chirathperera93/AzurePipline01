package com.ayubo.life.ayubolife.new_payment.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.new_payment.model.OtherPaymentImageItem
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by Chirath Perera on 2021-10-11.
 */
class NewPaymentImageViewAdapter(
    val arrayList: ArrayList<OtherPaymentImageItem>,
    val context: Context
) :
    BaseAdapter() {

    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return arrayList.size;
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        var view = view
        if (view == null) {
            view = layoutInflater.inflate(R.layout.other_payment_image_list, viewGroup, false)
        }

        val itemImage = view?.findViewById<ImageView>(R.id.imageViewForOtherPaymentItemIcon)
        if (itemImage != null) {
            Glide.with(context).load(arrayList[position].image).into(itemImage)
            itemImage.setColorFilter(context.resources.getColor(R.color.color_B3B3B3));
        }

        return view!!
    }

}