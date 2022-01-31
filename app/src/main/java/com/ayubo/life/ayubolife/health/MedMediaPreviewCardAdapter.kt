package com.ayubo.life.ayubolife.health

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide

class MedMediaPreviewCardAdapter(
    val baseContext: Context,
    var mediaPreviewCardItemsArrayList: ArrayList<OMMediaFiles>,
    var isReleaseAll: Boolean
) : RecyclerView.Adapter<MedMediaPreviewCardAdapter.MyViewHolder>() {

    val cardViewList: ArrayList<View> = ArrayList<View>();

    var onMedMediaPreviewItemClickListener: MedMediaPreviewCardAdapter.OnMedMediaPreviewItemClickListener? =
        null

    fun release() {
        isReleaseAll = true
        notifyDataSetChanged()
    }


    interface OnMedMediaPreviewItemClickListener {
        fun onMedMediaPreviewItemClick(medMediaPreviewCardItem: OMMediaFiles, isNotify: Boolean)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedMediaPreviewCardAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.med_media_preview_card, parent, false)

        cardViewList.add(v);
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.bindItems(
            baseContext,
            mediaPreviewCardItemsArrayList[position]
        )

    }

    override fun getItemCount(): Int {
        return mediaPreviewCardItemsArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(context: Context, medMediaPreviewCardItem: OMMediaFiles) {

            val mainBackgroundCard = itemView.findViewById(R.id.main_background_card) as CardView
            val mainCardViewForImage = itemView.findViewById(R.id.mainCardViewForImage) as CardView
            val mainCardViewForText = itemView.findViewById(R.id.mainCardViewForText) as CardView

            val media_preview_image = itemView.findViewById(R.id.media_preview_image) as ImageView
            val textViewForTextArea = itemView.findViewById(R.id.textViewForTextArea) as TextView


            if (medMediaPreviewCardItem.MediaType != "text/plain") {
                mainCardViewForImage.visibility = View.VISIBLE
                mainCardViewForText.visibility = View.GONE
                media_preview_image.visibility = View.VISIBLE
                Glide.with(context).load(medMediaPreviewCardItem.URL).centerCrop()
                    .into(media_preview_image)
            } else {
                mainCardViewForImage.visibility = View.GONE
                mainCardViewForText.visibility = View.VISIBLE
                media_preview_image.visibility = View.GONE
                textViewForTextArea.text = medMediaPreviewCardItem.note

            }



            if (adapterPosition == 0) {
                medMediaPreviewCardItem.isClicked = true;
                mainBackgroundCard.setCardBackgroundColor(
                    baseContext.resources.getColor(R.color.theme_color)
                );
            } else {

                print(mediaPreviewCardItemsArrayList)

            }

            mainBackgroundCard.setOnClickListener {

                mediaPreviewCardItemsArrayList.forEachIndexed { index, medMediaPreviewCardItem ->
                    medMediaPreviewCardItem.isClicked = false;
                }


                medMediaPreviewCardItem.isClicked = true;


                for (cardView in cardViewList) {
                    cardView.findViewById<CardView>(R.id.main_background_card)
                        .setCardBackgroundColor(
                            baseContext.resources.getColor(R.color.trancperent)
                        );
                }
                //The selected card is set to colorSelected
                mainBackgroundCard.setCardBackgroundColor(
                    baseContext.resources.getColor(R.color.theme_color)
                );

                onMedMediaPreviewItemClickListener?.onMedMediaPreviewItemClick(
                    medMediaPreviewCardItem, false
                )


//                mainBackgroundCard.setCardBackgroundColor(context.resources.getColor(R.color.theme_color))
            }

            mainCardViewForText.setOnClickListener {
                print(medMediaPreviewCardItem)

                mediaPreviewCardItemsArrayList.forEachIndexed { index, medMediaPreviewCardItem ->
                    medMediaPreviewCardItem.isClicked = false;
                }


                medMediaPreviewCardItem.isClicked = true;


                for (cardView in cardViewList) {
                    cardView.findViewById<CardView>(R.id.main_background_card)
                        .setCardBackgroundColor(
                            baseContext.resources.getColor(R.color.trancperent)
                        );
                }
                //The selected card is set to colorSelected
                mainBackgroundCard.setCardBackgroundColor(
                    baseContext.resources.getColor(R.color.theme_color)
                );

                onMedMediaPreviewItemClickListener?.onMedMediaPreviewItemClick(
                    medMediaPreviewCardItem, false
                )

            }

        }


    }

}
