package com.ayubo.life.ayubolife.lifeplus.NewToDo

import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ToDoItemAdapter(
        val context: Context,
        val dateToDoItemList: ArrayList<ToDoItem>,
        var clickListener: ToDoItemAdapter.OnToDoItemCardClickListener,
        var longPressListener: ToDoItemAdapter.OnToDoItemCardLongPressListener) : RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ToDoItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.to_do_data_item, parent, false)
        return ToDoItemAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ToDoItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(context, dateToDoItemList[position], clickListener, longPressListener)
    }

    override fun getItemCount(): Int {
        return dateToDoItemList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, toDoItem: ToDoItem, action: ToDoItemAdapter.OnToDoItemCardClickListener, longPressListenerAction: ToDoItemAdapter.OnToDoItemCardLongPressListener) {

            val toDoMainCard = itemView.findViewById(R.id.to_do_main_card) as CardView
            val toDoItemStatusImage = itemView.findViewById(R.id.to_do_item_status_image) as ImageView
            val toDoItemTitle = itemView.findViewById(R.id.to_do_item_title) as TextView
            val toDoItemSubTitleImage = itemView.findViewById(R.id.to_do_item_sub_title_image) as ImageView
            val toDoItemSubTitleText = itemView.findViewById(R.id.to_do_item_sub_title_text) as TextView
//            val toDoItemTime = itemView.findViewById(R.id.to_do_item_time) as TextView

            toDoItemTitle.text = toDoItem.title


            toDoItemSubTitleText.text = toDoItem.description
            Glide.with(context).load(toDoItem.icon).into(toDoItemSubTitleImage)

            val dateString: String = SimpleDateFormat("hh:mm aaa").format(Date(toDoItem.task_datetime));

//            toDoItemTime.text = dateString;

            Glide.with(context).load(toDoItem.task_icon).into(toDoItemStatusImage)

            if (!toDoItem.status.equals("active") && !toDoItem.status.equals("done")) {
                toDoItemTitle.setTextColor(ContextCompat.getColor(context, R.color.to_do_cancel))
//                toDoItemTime.setTextColor(ContextCompat.getColor(context, R.color.to_do_cancel))
                toDoItemTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                toDoItemTime.text = "Incomplete"
            }

            if (toDoItem.status.equals("done")) {
                toDoItemTitle.setTextColor(ContextCompat.getColor(context, R.color.color_C3C3C3))
                toDoItemSubTitleText.setTextColor(ContextCompat.getColor(context, R.color.color_C3C3C3))
            }


            toDoMainCard.setOnLongClickListener {
                longPressListenerAction.onItemLongPress(toDoItem, adapterPosition)
                return@setOnLongClickListener true;
            }


            toDoMainCard.setOnClickListener {
                action.onItemClick(toDoItem, adapterPosition)
            }


        }


    }

    interface OnToDoItemCardClickListener {
        fun onItemClick(item: ToDoItem, position: Int)
    }

    interface OnToDoItemCardLongPressListener {
        fun onItemLongPress(item: ToDoItem, position: Int)
    }
}