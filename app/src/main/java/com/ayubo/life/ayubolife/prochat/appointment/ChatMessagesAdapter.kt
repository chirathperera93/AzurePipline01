package com.ayubo.life.ayubolife.prochat.appointment

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.signalr.SignalRSingleton
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_for_date_label.view.*
import kotlinx.android.synthetic.main.listitem_chat_text_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_text_send.view.*
import kotlinx.android.synthetic.main.new_receiver_chat_layout.view.*
import kotlinx.android.synthetic.main.new_sender_chat_layout.view.*
import kotlinx.android.synthetic.main.new_system_chat_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Chirath Perera on 2021-08-23.
 */

const val SENDER = 1
const val RECEIVER = 2
const val SYSTEM_MSG = 3
const val UNREAD_LABEL = 4
const val DATE_LABEL = 5

@IntDef(SENDER, RECEIVER, SYSTEM_MSG, UNREAD_LABEL, DATE_LABEL)
@Retention(AnnotationRetention.SOURCE)
annotation class MessageTypes


class ChatMessagesAdapter(
    val context: Context,
    val currentUserId: String,
    var messagesArrayList: ArrayList<NewMessage>,
    var releaseAll: Boolean = false,
    var sessionDataInfo: SessionDataInfo?,
    var isTriggeredNewMessage: Boolean = false
) :
    RecyclerView.Adapter<ChatMessagesAdapter.BaseHolder>() {

    var signalRSingleton: SignalRSingleton = SignalRSingleton.getInstance(context);
    var onChatMessageClickListener: OnChatMessageClickListener? = null

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }

    interface OnChatMessageClickListener {
        fun messageItemClick(newMessage: NewMessage)

    }


    override fun getItemViewType(position: Int): Int {

        val message: NewMessage = messagesArrayList[position]

        if (message.type != "system" && message.type != "unread_label" && message.type != "date_label") {
            if (currentUserId == message.user_id) {
                message.type = "user"
            } else {
                message.type = "doctor"
            }
        }


        return when (message.type) {

            "user" -> {
                SENDER
            }
            "system" -> {
                SYSTEM_MSG
            }
            "date_label" -> {
                DATE_LABEL
            }
            "unread_label" -> {
                UNREAD_LABEL
            }
            else -> {
                RECEIVER
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @MessageTypes viewType: Int
    ): ChatMessagesAdapter.BaseHolder {


        return when (viewType) {
            SENDER -> {
                SenderChatLayout(parent)
            }
            SYSTEM_MSG -> {
                SystemChatLayout(parent)
            }
            DATE_LABEL -> {
                DateLabelLayout(parent)
            }
            UNREAD_LABEL -> {
                UnreadLabelLayout(parent)
            }
            else -> {
                ReceiverChatLayout(parent)
            }
        }
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(newMessage: NewMessage) = with(itemView) {

        }

        open fun release() {

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (releaseAll) {
            holder.release()
        }

        holder.bind(messagesArrayList[position])
    }

    inner class SenderChatLayout(parent: ViewGroup) :
        ChatMessagesAdapter.BaseHolder(parent.inflate(R.layout.new_sender_chat_layout)) {
        @SuppressLint("SimpleDateFormat")
        override fun bind(newMessage: NewMessage) = with(itemView) {

            print(newMessage)

            val date = Date(newMessage.create_datetime)

            val formatter = SimpleDateFormat("HH:mm a")
            val strDate: String = formatter.format(date)

            time_of_sender_message.text = strDate
            sender_message.text = newMessage.message


            delivered_image.setBackgroundDrawable(null)
            if (isTriggeredNewMessage && newMessage.delivered_to.size == 0 && newMessage.read_by!!.size == 0) {
                delivered_image.setBackgroundResource(R.drawable.new_chat_loading_image)
            } else if (newMessage.delivered_to.size == sessionDataInfo!!.members.size) {
                delivered_image.setBackgroundResource(R.drawable.double_tick_image)
                if (newMessage.read_by!!.size == sessionDataInfo!!.members.size) {
                    delivered_image.setBackgroundDrawable(null)
                    delivered_image.setBackgroundResource(R.drawable.double_tick_blue_image)
                }
            } else {
                delivered_image.setBackgroundResource(R.drawable.single_tick_image)
            }



            return@with
        }

    }

    inner class ReceiverChatLayout(parent: ViewGroup) :
        ChatMessagesAdapter.BaseHolder(parent.inflate(R.layout.new_receiver_chat_layout)) {
        override fun bind(newMessage: NewMessage) = with(itemView) {


            if ((newMessage.read_by == null || !newMessage.read_by!!.contains(
                    currentUserId
                ))
            ) {
                doMessageRead(newMessage)
            }

            print(newMessage)
            image_for_new_chat_receiver.setBackgroundResource(0);
            image_for_new_chat_receiver.visibility = View.GONE
            if (position > 0) {
                if (newMessage.user_id != messagesArrayList[position - 1].user_id) {

                    image_for_new_chat_receiver.visibility = View.VISIBLE
                    Glide.with(context).load(newMessage.user.image_url)
                        .into(image_for_new_chat_receiver)


                } else {
                    image_for_new_chat_receiver.visibility = View.GONE
                }


            } else if (position == 0) {
                image_for_new_chat_receiver.visibility = View.VISIBLE
                Glide.with(context).load(newMessage.user.image_url)
                    .into(image_for_new_chat_receiver)
            }


            val date = Date(newMessage.create_datetime)

            val formatter: SimpleDateFormat = SimpleDateFormat("HH:mm a");
            val strDate: String = formatter.format(date);
            time_of_receiver_message.text = strDate
            receiver_message.text = newMessage.message



            return@with
        }

    }

    inner class SystemChatLayout(parent: ViewGroup) :
        ChatMessagesAdapter.BaseHolder(parent.inflate(R.layout.new_system_chat_layout)) {
        override fun bind(newMessage: NewMessage) = with(itemView) {

            print(newMessage)
            if ((newMessage.read_by == null || !newMessage.read_by!!.contains(
                    currentUserId
                ))
            ) {
                doMessageRead(newMessage)
            }
            val date = Date(newMessage.create_datetime)

            val formatter: SimpleDateFormat = SimpleDateFormat("HH:mm a");
            val strDate: String = formatter.format(date);
            time_of_system_message.text = strDate

            new_chat_system_message.text = newMessage.message





            return@with
        }

    }


    inner class UnreadLabelLayout(parent: ViewGroup) :
        ChatMessagesAdapter.BaseHolder(parent.inflate(R.layout.layout_for_unread_message_label)) {
        override fun bind(newMessage: NewMessage) = with(itemView) {
            return@with
        }
    }

    inner class DateLabelLayout(parent: ViewGroup) :
        ChatMessagesAdapter.BaseHolder(parent.inflate(R.layout.layout_for_date_label)) {
        override fun bind(newMessage: NewMessage) = with(itemView) {
            date_type_text.text = newMessage.message
            return@with
        }
    }

    fun doMessageRead(newMessage: NewMessage) {
        val currentUserArrayList: ArrayList<String> = ArrayList<String>();
        currentUserArrayList.add(currentUserId)
        signalRSingleton.sendSignal(
            "message_read",
            Gson().toJsonTree(newMessage).asJsonObject,
            currentUserArrayList
        )

    }

}