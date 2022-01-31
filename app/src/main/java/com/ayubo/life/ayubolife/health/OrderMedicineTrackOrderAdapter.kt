package com.ayubo.life.ayubolife.health

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Chirath Perera on 2021-08-09.
 */
class OrderMedicineTrackOrderAdapter(
    var baseContext: Context,
    var orderMedicineTrackOrderItemArrayList: ArrayList<OMTrackOrderItem>,
    var releaseAll: Boolean = false
) : RecyclerView.Adapter<OrderMedicineTrackOrderAdapter.MyViewHolder>() {


    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderMedicineTrackOrderAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_medicine_track_order_item, parent, false)

        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(baseContext, orderMedicineTrackOrderItemArrayList[position])

    }

    override fun getItemCount(): Int {
        return orderMedicineTrackOrderItemArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bindItems(context: Context, orderMedicineTrackOrderItem: OMTrackOrderItem) {

            System.out.println(orderMedicineTrackOrderItem)

            val completed_image_main =
                itemView.findViewById(R.id.completed_image_main) as LinearLayout

            val completed_image =
                itemView.findViewById(R.id.completed_image) as ImageView

            val delivered_image =
                itemView.findViewById(R.id.delivered_image) as ImageView

            val title =
                itemView.findViewById(R.id.title) as TextView

            val description =
                itemView.findViewById(R.id.description) as TextView

            val time =
                itemView.findViewById(R.id.time) as TextView


            title.text = orderMedicineTrackOrderItem.title

            description.text = orderMedicineTrackOrderItem.description

            if (orderMedicineTrackOrderItem.datetime == 0.0) {
                time.visibility = View.GONE
            }

            val date = Date(orderMedicineTrackOrderItem.datetime!!.toLong())
            val formatter: SimpleDateFormat = SimpleDateFormat("MMM dd, hh:mm aaa");
            val strDate: String = formatter.format(date);

            time.text = strDate


            Glide.with(context).load(orderMedicineTrackOrderItem.status_icon).into(delivered_image);

            if (orderMedicineTrackOrderItem.status.equals("done")) {
                completed_image.visibility = View.VISIBLE
                completed_image_main.setBackgroundResource(R.drawable.green_circle_background)
                title.setTextColor(context.resources.getColor(R.color.color_4B4C4A))
                description.setTextColor(context.resources.getColor(R.color.color_4B4C4A))
                time.setTextColor(context.resources.getColor(R.color.black))

            } else {
                completed_image.visibility = View.GONE

                if (orderMedicineTrackOrderItem.status.equals(
                        "processing"
                    )
                ) {
                    completed_image_main.setBackgroundResource(R.drawable.orange_circle_background)
                    title.setTextColor(context.resources.getColor(R.color.color_4B4C4A))
                    description.setTextColor(context.resources.getColor(R.color.color_4B4C4A))
                    time.setTextColor(context.resources.getColor(R.color.black))


                } else {
                    completed_image_main.setBackgroundResource(R.drawable.grey_circle_background)
                    title.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                    description.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                    time.setTextColor(context.resources.getColor(R.color.color_B3B3B3))
                }
            }


        }


    }

}