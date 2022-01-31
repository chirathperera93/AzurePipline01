package com.ayubo.life.ayubolife.health


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Chirath Perera on 2021-07-19.
 */
class OrderMedicineHistoryAdapter(
        val context: Context,
        val dataList: ArrayList<OMCreatedOrderObj>,
        var releaseAll: Boolean = false,
        var isReduceUIElements: Boolean = false
) : RecyclerView.Adapter<OrderMedicineHistoryAdapter.MyViewHolder>(), Filterable {

    var dataFilterList = ArrayList<OMCreatedOrderObj>()
    var dataListFull: ArrayList<OMCreatedOrderObj> = ArrayList<OMCreatedOrderObj>(dataList)

    var onOrderMedicineHistoryItemClickListener: OrderMedicineHistoryAdapter.OrderMedicineHistoryItemClickListener? =
            null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }

    init {
        dataFilterList = dataList
    }


    interface OrderMedicineHistoryItemClickListener {
        fun orderMedicineHistoryItemClick(commonHistoryItem: OMCreatedOrderObj)
        fun orderMedicineHistoryMainCardClick(commonHistoryItem: OMCreatedOrderObj)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): OrderMedicineHistoryAdapter.MyViewHolder {
        val v =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.order_medicine_history_item, parent, false)


        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, dataList[position])

    }

    override fun getItemCount(): Int {

        return dataFilterList.size;
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(context: Context, commonHistoryItem: OMCreatedOrderObj) {

            System.out.println(commonHistoryItem)

            val om_history_main_card =
                    itemView.findViewById(R.id.om_history_main_card) as LinearLayout
            val title = itemView.findViewById(R.id.title) as TextView
            val image_linear_layout =
                    itemView.findViewById(R.id.image_linear_layout) as LinearLayout
            val order_back_btn_main =
                    itemView.findViewById(R.id.order_back_btn_main) as RelativeLayout
            val order_number = itemView.findViewById(R.id.order_number) as TextView
            val time = itemView.findViewById(R.id.time) as TextView
            val image = itemView.findViewById(R.id.image) as ImageView
            val status = itemView.findViewById(R.id.status) as TextView
            val address = itemView.findViewById(R.id.address) as TextView
            val cost = itemView.findViewById(R.id.cost) as TextView
            val order_back_btn = itemView.findViewById(R.id.order_back_btn) as TextView


            val date = Date(commonHistoryItem.lastupdated_datetime!!.toLong())

            val formatter: SimpleDateFormat = SimpleDateFormat("E, dd MM yyyy");
            val strDate: String = formatter.format(date);



            if (isReduceUIElements) {
//                order_back_btn.visibility = View.GONE
                order_number.visibility = View.GONE
                time.visibility = View.GONE
                image_linear_layout.visibility = View.GONE
//                order_back_btn_main.visibility = View.GONE
            }

            if (commonHistoryItem.partner !== null) {
                Glide.with(context).load(commonHistoryItem.partner!!.logo_url)
                        .into(image);
                title.text = commonHistoryItem.partner!!.name
            }


            order_number.text = "Order No : " + "#" + commonHistoryItem.order_id
            time.text = strDate


            val hNum =
                    if (commonHistoryItem.address!!.house_number == null) "" else commonHistoryItem.address!!.house_number;
            val street =
                    if (commonHistoryItem.address!!.street == null) "" else commonHistoryItem.address!!.street;


            address.text =
                    "Delivery Location : " + hNum + " " + street + " " + commonHistoryItem.address!!.city


            val gd: GradientDrawable = GradientDrawable();
            gd.cornerRadius = 8F;
            gd.setStroke(2, Color.parseColor(commonHistoryItem.status_color));

            status.setTextColor(Color.parseColor(commonHistoryItem.status_color));
            status.setBackgroundDrawable(gd)

            status.text = commonHistoryItem.status


            if (
                    commonHistoryItem.status.equals("Order Confirmed")
                    ||
                    commonHistoryItem.status.equals("Payment Completed")
                    ||
                    commonHistoryItem.status.equals("Dispatched")
                    ||
                    commonHistoryItem.status.equals("On the way")
                    ||
                    commonHistoryItem.status.equals("Processing")
            ) {
                order_back_btn.text = "Track Order"
                order_back_btn.setBackgroundResource(R.drawable.orange_all_corners_rounded_gradient)
//                cost.setTextColor(context.resources.getColor(R.color.color_FEBC2C))
                cost.setTextColor(context.resources.getColor(R.color.black))

            } else if (commonHistoryItem.status.equals("Payment Pending")) {

            } else if (commonHistoryItem.status.equals("Payment Updated")) {
                order_back_btn.text = "Proceed"
                order_back_btn.setBackgroundResource(R.drawable.green_all_corners_rounded_gradient)
//                cost.setTextColor(context.resources.getColor(R.color.color_41D38A))
                cost.setTextColor(context.resources.getColor(R.color.black))
            } else if (commonHistoryItem.status.equals("Delivered") || commonHistoryItem.status.equals(
                            "Cancelled"
                    )
            ) {
                order_back_btn.text = "Re-Order"
                order_back_btn.setBackgroundResource(R.drawable.green_all_corners_rounded_gradient)
//                cost.setTextColor(context.resources.getColor(R.color.color_41D38A))
                cost.setTextColor(context.resources.getColor(R.color.black))
            } else {
                order_back_btn.text = "Edit"
                order_back_btn.setBackgroundResource(R.drawable.green_all_corners_rounded_gradient)
//                cost.setTextColor(context.resources.getColor(R.color.color_41D38A))
                cost.setTextColor(context.resources.getColor(R.color.black))
            }



            if (commonHistoryItem.payment !== null) {

                if (commonHistoryItem.status.equals("Processing")) {
                    cost.text = "Calculating"
                } else {

                    val removeDigit = commonHistoryItem.payment!!.total_amount!!.split(".");

                    print(removeDigit)

                    cost.text = removeDigit[0] + " " + commonHistoryItem.payment!!.currency

                }
            }


            order_back_btn.setOnClickListener {
                onOrderMedicineHistoryItemClickListener?.orderMedicineHistoryItemClick(
                        commonHistoryItem
                )
            }

            om_history_main_card.setOnClickListener {
                onOrderMedicineHistoryItemClickListener?.orderMedicineHistoryMainCardClick(
                        commonHistoryItem
                )

            }

        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var resultList = ArrayList<OMCreatedOrderObj>()
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList = dataListFull
                } else {

                    for (row in dataListFull) {
                        if (
                                row.order_id!!.toLowerCase().contains(charSearch.toLowerCase())
                                ||
                                row.partner!!.name!!.toLowerCase().contains(charSearch.toLowerCase())
                                ||
                                row.status!!.toLowerCase().contains(charSearch.toLowerCase())
                        ) {
                            resultList.add(row)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults) {
                dataFilterList.clear()
                dataFilterList.addAll(filterResults.values as ArrayList<OMCreatedOrderObj>)
                notifyDataSetChanged()
            }

        }
    }
}