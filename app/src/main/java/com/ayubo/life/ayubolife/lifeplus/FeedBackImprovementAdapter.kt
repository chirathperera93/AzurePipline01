package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ayubo.life.ayubolife.R

class FeedBackImprovementAdapter(
        val context: Context,
        val feedBackImprovementsItemList: ArrayList<FeedBackImprovementItem>,
        var clickListener: FeedBackImprovementAdapter.OnToDoItemCardClickListener) : RecyclerView.Adapter<FeedBackImprovementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FeedBackImprovementAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feed_back_improvement_item, parent, false)
        return FeedBackImprovementAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FeedBackImprovementAdapter.ViewHolder, position: Int) {
        holder.bindItems(context, feedBackImprovementsItemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return feedBackImprovementsItemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, feedBackImprovementItem: FeedBackImprovementItem, FeedBackImprovementAdapterAction: FeedBackImprovementAdapter.OnToDoItemCardClickListener) {

            val feedbackImpvCardView = itemView.findViewById(R.id.feedback_impv_card_view) as CardView
            val feedbackImpvTextView = itemView.findViewById(R.id.feedback_impv_text_view) as TextView
            var isFirstTimeClicked: Boolean = false;

            feedbackImpvTextView.setText(feedBackImprovementItem.category);

            feedbackImpvCardView.setOnClickListener {

                if (!isFirstTimeClicked) {
                    feedbackImpvCardView.setCardBackgroundColor(context.resources.getColor(R.color.theme_color))
                    feedbackImpvTextView.setTextColor(context.resources.getColor(R.color.white))
                    isFirstTimeClicked = true;
                    feedBackImprovementItem.isSelected = true;
                } else {
                    feedbackImpvCardView.setCardBackgroundColor(context.resources.getColor(R.color.color_EAEAEA))
                    feedbackImpvTextView.setTextColor(context.resources.getColor(R.color.color_3B3B3B))
                    isFirstTimeClicked = false;
                    feedBackImprovementItem.isSelected = false;
                }


                FeedBackImprovementAdapterAction.onItemClick(feedBackImprovementItem)
            }


        }


    }

    interface OnToDoItemCardClickListener {
        fun onItemClick(item: FeedBackImprovementItem)
    }
}