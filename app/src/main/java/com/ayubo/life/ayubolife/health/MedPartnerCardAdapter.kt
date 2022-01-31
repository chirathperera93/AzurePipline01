package com.ayubo.life.ayubolife.health

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide

class MedPartnerCardAdapter(
    val baseContext: Context,
    val medPartnerCardItemsArrayList: ArrayList<OMPartner>,
    var releaseAll: Boolean,
    var isOnclickItem: Boolean = true
) : RecyclerView.Adapter<MedPartnerCardAdapter.MyViewHolder>(), Filterable {

    var onOrderMedicineHistoryItemClickListener: MedPartnerCardAdapter.MedPartnerCardItemClickListener? =
        null
    val cardViewList: ArrayList<View> = ArrayList<View>();

    var dataFilterList = ArrayList<OMPartner>()
    var medPartnerCardItemsArrayListFull: ArrayList<OMPartner> =
        ArrayList<OMPartner>(medPartnerCardItemsArrayList)


    init {
        dataFilterList = medPartnerCardItemsArrayList
    }

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }


    interface MedPartnerCardItemClickListener {
        fun medPartnerCardItemClick(medPartnerCardItem: OMPartner)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedPartnerCardAdapter.MyViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_medicine_partner_item, parent, false)
        cardViewList.add(v);

        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(baseContext, medPartnerCardItemsArrayList[position])

    }

    override fun getItemCount(): Int {
        return dataFilterList.size;
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bindItems(context: Context, medPartnerCardItem: OMPartner) {

            val partner_main_card = itemView.findViewById(R.id.partner_main_card) as LinearLayout
            val partner_discount_main =
                itemView.findViewById(R.id.partner_discount_main) as LinearLayout
            val partner_image = itemView.findViewById(R.id.partner_image) as ImageView
            val partner_title = itemView.findViewById(R.id.partner_title) as TextView
            val partner_address = itemView.findViewById(R.id.partner_address) as TextView
            val partner_time = itemView.findViewById(R.id.partner_time) as TextView
            val partner_open_status = itemView.findViewById(R.id.partner_open_status) as TextView
            val partner_discount_text =
                itemView.findViewById(R.id.partner_discount_text) as TextView
            val partner_time_restriction =
                itemView.findViewById(R.id.partner_time_restriction) as TextView
            val partner_payment_method_linear_layout =
                itemView.findViewById(R.id.partner_payment_method_linear_layout) as LinearLayout


            Glide.with(context).load(medPartnerCardItem.logo_url)
                .into(partner_image);

            partner_title.text = medPartnerCardItem.name

            partner_address.text = medPartnerCardItem.address_line


            if (medPartnerCardItem.discount != null && !medPartnerCardItem.discount.equals("")) {
                partner_discount_main.visibility = View.VISIBLE
                partner_discount_text.text = medPartnerCardItem.discount
            } else {
                partner_discount_main.visibility = View.GONE
            }



            partner_open_status.text =
                medPartnerCardItem.availability!!.get("section1").asJsonObject.get("label").asString + " "

            partner_time.text =
                medPartnerCardItem.availability!!.get("section2").asJsonObject.get("label").asString

            partner_open_status.setTextColor(
                Color.parseColor(
                    medPartnerCardItem.availability!!.get(
                        "section1"
                    ).asJsonObject.get("color").asString
                )
            )

            partner_time_restriction.text = medPartnerCardItem.delivery_statement
            partner_payment_method_linear_layout.removeAllViews()


            for (pMethod in medPartnerCardItem.payment_methods!!) {

                val textView: TextView = TextView(context);

                textView.setBackgroundResource(R.drawable.color_b3b3b3_outline_rectangle_boarder)
                textView.setPadding(16, 8, 16, 8)
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 4, 0);
                textView.layoutParams = params;
                textView.text = pMethod.title
                textView.setTextColor(context.resources.getColor(R.color.color_707070))
                textView.textSize = 6F

                val typeface: Typeface? =
                    ResourcesCompat.getFont(baseContext, R.font.montserrat_medium);
                textView.typeface = typeface;
                partner_payment_method_linear_layout.addView(textView)

            }
            partner_main_card.setOnClickListener {


//                for (cardView in cardViewList) {
//                    cardView.findViewById<CardView>(R.id.main_card)
//                        .setCardBackgroundColor(
//                            baseContext.resources.getColor(R.color.white)
//                        );
//                }
//                //The selected card is set to colorSelected
//                main_card.setCardBackgroundColor(
//                    baseContext.resources.getColor(R.color.point_leader_board_me)
//                );

                if (isOnclickItem) {
                    medPartnerCardItemsArrayList.forEachIndexed { index, partner ->
                        partner.isSelected = false
                    }

                    medPartnerCardItem.isSelected = true
                    for (cardView in cardViewList) {
                        cardView.findViewById<LinearLayout>(R.id.partner_main_card).elevation = 1F;
                        cardView.findViewById<LinearLayout>(R.id.partner_main_card)
                            .setBackgroundResource(R.drawable.om_4_rectangle_corner_card)

                    }
                    //The selected card is set to colorSelected
                    partner_main_card.elevation = 12F
                    partner_main_card.setBackgroundResource(R.drawable.om_4_rectangle_corner_theme_color)


                    onOrderMedicineHistoryItemClickListener!!.medPartnerCardItemClick(
                        medPartnerCardItem
                    )
                }


            }

            if (medPartnerCardItem.isSelected != null && medPartnerCardItem.isSelected!!) {
                if (isOnclickItem) {
                    for (cardView in cardViewList) {
                        cardView.findViewById<LinearLayout>(R.id.partner_main_card).elevation = 1F;
                        cardView.findViewById<LinearLayout>(R.id.partner_main_card)
                            .setBackgroundResource(R.drawable.om_4_rectangle_corner_card)

                    }
                    //The selected card is set to colorSelected
                    partner_main_card.elevation = 12F
                    partner_main_card.setBackgroundResource(R.drawable.om_4_rectangle_corner_theme_color)


//                    onOrderMedicineHistoryItemClickListener!!.medPartnerCardItemClick(
//                        medPartnerCardItem
//                    )
                }
            }


        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                var resultList = ArrayList<OMPartner>()
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList = medPartnerCardItemsArrayListFull
                } else {
                    for (row in medPartnerCardItemsArrayListFull) {
                        if (
                            row.name!!.toLowerCase().contains(charSearch.toLowerCase())
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
                dataFilterList.clear();
                val filteredPartners = filterResults.values as ArrayList<OMPartner>
                dataFilterList.addAll(filteredPartners)
                notifyDataSetChanged()
            }

        }
    }


}