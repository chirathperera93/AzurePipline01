package com.ayubo.life.ayubolife.health

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager

class OrderMedicineLocationAdapter(
    var baseContext: Context,
    var orderMedicineLocationItemsArrayList: ArrayList<OMAddress>,
    var orderMedicineFavLocationItemsArrayList: ArrayList<OMAddress>,
    var releaseAll: Boolean = false,
    var isEditActionShow: Boolean = false,
    var isOnClickItem: Boolean = true
) : RecyclerView.Adapter<OrderMedicineLocationAdapter.MyViewHolder>() {
    lateinit var prefManager: PrefManager;

    var onOrderMedicineLocationItemClickListener: OrderMedicineLocationAdapter.OnOrderMedicineLocationItemClickListener? =
        null

    val cardViewList: ArrayList<View> = ArrayList<View>();

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface OnOrderMedicineLocationItemClickListener {
        fun orderMedicineLocationItemClick(OrderMedicineLocationItem: OMAddress)
        fun orderMedicineFavLocationItemClick(OrderMedicineLocationItem: OMAddress)
        fun orderMedicineLocationEditItemClick(OrderMedicineLocationItem: OMAddress)
        fun orderMedicineLocationLongPress(OrderMedicineLocationItem: OMAddress)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderMedicineLocationAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_medicine_location_item, parent, false)
        cardViewList.add(v)

        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(baseContext, orderMedicineLocationItemsArrayList[position])

    }

    override fun getItemCount(): Int {
        return orderMedicineLocationItemsArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bindItems(context: Context, orderMedicineLocationItem: OMAddress) {

            System.out.println(orderMedicineLocationItem)

            val om_location_main_card =
                itemView.findViewById(R.id.om_location_main_card) as RelativeLayout
            val on_location_sub =
                itemView.findViewById(R.id.on_location_sub) as LinearLayout
            val title = itemView.findViewById(R.id.title) as TextView
            val address = itemView.findViewById(R.id.address) as TextView
            val addFavouriteInactive =
                itemView.findViewById(R.id.add_favourite_inactive) as ImageView
            val addFavouriteActive = itemView.findViewById(R.id.add_favourite_active) as ImageView
            val imageViewForSelectItem =
                itemView.findViewById(R.id.imageViewForSelectItem) as ImageView
            val modify_address = itemView.findViewById(R.id.modify_address) as LinearLayout

            if (isEditActionShow) {
                addFavouriteInactive.visibility = View.VISIBLE
                addFavouriteActive.visibility = View.VISIBLE
                modify_address.visibility = View.VISIBLE

                if (orderMedicineLocationItem.fav_address == true) {
                    addFavouriteInactive.visibility = View.GONE
                    addFavouriteActive.visibility = View.VISIBLE
                } else {
                    addFavouriteInactive.visibility = View.VISIBLE
                    addFavouriteActive.visibility = View.GONE
                }

                om_location_main_card.setOnLongClickListener {
                    onOrderMedicineLocationItemClickListener?.orderMedicineLocationLongPress(
                        orderMedicineLocationItem
                    )
                    return@setOnLongClickListener true;
                }

            } else {
                addFavouriteInactive.visibility = View.GONE
                addFavouriteActive.visibility = View.GONE
                modify_address.visibility = View.GONE
            }

            title.text = orderMedicineLocationItem.display_name

            val hNum =
                if (orderMedicineLocationItem.house_number == null) "" else orderMedicineLocationItem.house_number;
            val street =
                if (orderMedicineLocationItem.street == null) "" else orderMedicineLocationItem.street;



            address.text =
                hNum + " " + street + " " + orderMedicineLocationItem.city


            if (isOnClickItem) {
                if (orderMedicineLocationItem.isSelected) {
                    for (cardView in cardViewList) {
//                        cardView.findViewById<LinearLayout>(R.id.om_location_main_card).elevation =
//                            1F
//                        cardView.findViewById<LinearLayout>(R.id.om_location_main_card)
//                            .setBackgroundResource(R.drawable.om_4_rectangle_corner_card)

                        cardView.findViewById<ImageView>(R.id.imageViewForSelectItem).visibility =
                            View.GONE

                        cardView.findViewById<LinearLayout>(R.id.on_location_sub)
                            .setBackgroundColor(context.resources.getColor(R.color.white));
                    }

                    imageViewForSelectItem.visibility = View.VISIBLE

                    on_location_sub.setBackgroundResource(R.drawable.theme_color_outline_with_background)
//
//                    om_location_main_card.setBackgroundColor(
//                        baseContext.resources.getColor(R.color.point_leader_board_me)
//                    );

//                    om_location_main_card.elevation = 8F

                    onOrderMedicineLocationItemClickListener?.orderMedicineLocationItemClick(
                        orderMedicineLocationItem
                    )
                }

                modify_address.setOnClickListener {
                    onOrderMedicineLocationItemClickListener?.orderMedicineLocationEditItemClick(
                        orderMedicineLocationItem
                    )
                }

                addFavouriteInactive.setOnClickListener {


                    addFavouriteInactive.visibility = View.GONE
                    addFavouriteActive.visibility = View.VISIBLE

                    orderMedicineLocationItem.fav_address = true

                    onOrderMedicineLocationItemClickListener?.orderMedicineFavLocationItemClick(
                        orderMedicineLocationItem
                    )


                }

                addFavouriteActive.setOnClickListener {

                    if (orderMedicineFavLocationItemsArrayList.size == 1) {
                        Toast.makeText(
                            context,
                            "You can't remove all location from favourites",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        addFavouriteInactive.visibility = View.VISIBLE
                        addFavouriteActive.visibility = View.GONE

                        orderMedicineLocationItem.fav_address = false

                        onOrderMedicineLocationItemClickListener?.orderMedicineFavLocationItemClick(
                            orderMedicineLocationItem
                        )
                    }
                }

                om_location_main_card.setOnClickListener {

                    orderMedicineLocationItemsArrayList.forEachIndexed { index, omAddress ->
                        omAddress.isSelected = false
                    }

                    orderMedicineLocationItem.isSelected = true

                    for (cardView in cardViewList) {
//                        cardView.findViewById<LinearLayout>(R.id.om_location_main_card).elevation =
//                            1F
//                        cardView.findViewById<LinearLayout>(R.id.om_location_main_card)
//                            .setBackgroundResource(R.drawable.om_4_rectangle_corner_card)
                        cardView.findViewById<ImageView>(R.id.imageViewForSelectItem).visibility =
                            View.GONE

                        cardView.findViewById<LinearLayout>(R.id.on_location_sub)
                            .setBackgroundColor(context.resources.getColor(R.color.white));

                    }
                    //The selected card is set to colorSelected
//                    om_location_main_card.setBackgroundColor(
//                        baseContext.resources.getColor(R.color.point_leader_board_me)
//                    );
//
//                    om_location_main_card.elevation = 8F

                    imageViewForSelectItem.visibility = View.VISIBLE

                    on_location_sub.setBackgroundResource(R.drawable.theme_color_outline_with_background)

                    onOrderMedicineLocationItemClickListener?.orderMedicineLocationItemClick(
                        orderMedicineLocationItem
                    )
                }
            }

        }


    }


}
