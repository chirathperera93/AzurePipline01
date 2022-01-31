package com.ayubo.life.ayubolife.prochat.appointment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.pojo.goals.MainResponse
import com.ayubo.life.ayubolife.prochat.common.convertReadableDate
import com.ayubo.life.ayubolife.prochat.common.convertReadableTime
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.prochat.data.model.Conversation
import com.ayubo.life.ayubolife.prochat.data.model.User
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.listitem_chat_button_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_gif_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_gif_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_image_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_image_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_medical_object_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_medical_object_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_mp4_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_mp4_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_pdf_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_pdf_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_record_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_record_send.view.*
import kotlinx.android.synthetic.main.listitem_chat_system_object.view.*
import kotlinx.android.synthetic.main.listitem_chat_text_receive.view.*
import kotlinx.android.synthetic.main.listitem_chat_text_send.view.*
import kotlinx.android.synthetic.main.raw_challenge_journal.view.*
import org.michaelbel.bottomsheet.BottomSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val SYSTEM = 11

const val MY_TEXT = 0
const val MY_IMAGE = 1
const val MY_AUDIO = 2
const val MY_PDF = 3
const val MY_MP4 = 12
const val MY_GIF = 13
const val MY_MEDICAL = 14
const val MY_BUTTONS = 15
const val YOUR_TEXT = 4
const val YOUR_IMAGE = 5
const val YOUR_AUDIO = 6
const val YOUR_PDF = 7
const val YOUR_MP4 = 8
const val YOUR_GIF = 9
const val YOUR_MEDICAL = 10
const val MP3 = 16


@IntDef(
    MY_TEXT,
    MY_IMAGE,
    MY_AUDIO,
    MY_PDF,
    YOUR_TEXT,
    YOUR_IMAGE,
    YOUR_AUDIO,
    YOUR_PDF,
    MY_BUTTONS,
    MP3
)
@Retention(AnnotationRetention.SOURCE)
annotation class ChatViewTypes


class AdapterAppointment(
    val currentUser: User,
    val list: ArrayList<Conversation>,
    var releaseAll: Boolean = false
) :
    RecyclerView.Adapter<AdapterAppointment.BaseHolder>() {
    var onitemClickListener: OnItemClickListener? = null
    var isVisibleMyProfileIcon: Boolean = true

    fun release() {
        releaseAll = true
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(conversation: Conversation, position: Int)
        fun onDownloadClick(conversation: Conversation, position: Int)
        fun onShareClick(conversation: Conversation, position: Int)
        fun onButtonClick(conversation: Conversation, position: Int)
        fun onProcessActionClick(activityName: String, meta: String)
        fun onRefreshChatClick(activityName: String, meta: String)
        fun onItemLongClick(conversation: Conversation)

//        fun onButtonChannelingClick(activityName: String,meta: String)
//        fun onVideoCallClick(activityName: String,meta: String)
//        fun onMapChallangeClick(activityName: String,meta: String)
//        fun onAskClick(activityName: String,meta: String)
//        fun onProgramPostClick(activityName: String,meta: String)
//        fun onGoalClick(activityName: String,meta: String)
//        fun onReportsClick(activityName: String,meta: String)
//        fun onLifePointsClick(activityName: String,meta: String)
//        fun onHelpClick(activityName: String,meta: String)
//        fun onCommonViewClick(activityName: String,meta: String)
//        fun onBowserClick(activityName: String,meta: String)
//
//        fun onJanashakthiWelcomeClick(activityName: String,meta: String)
//        fun onJanashakthiReportsClick(activityName: String,meta: String)
//        fun onDyanamicQuestionClick(activityName: String,meta: String)
//
//        fun onPostClick(activityName: String,meta: String)
//        fun onRefreshChatClick(activityName: String,meta: String)
//        fun onNativePostClick(activityName: String,meta: String)
//        fun onProgramNewDesignClick(activityName: String,meta: String)

    }

    fun addConversation(conversation: Conversation) {
        releaseAll = false
        list.add(conversation)
        notifyItemInserted(list.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, @ChatViewTypes viewType: Int): BaseHolder {
        when (viewType) {
            MY_IMAGE -> {
                return MyImage(parent)

            }
            MY_AUDIO -> {
                return MyAudio(parent)
            }
            MY_PDF -> {
                return MyPdf(parent)
            }
            MY_TEXT -> {

                return MyText(parent)
            }
            MY_MP4 -> {
                return MyMp4(parent)
            }
            MY_GIF -> {
                return MyGIF(parent)
            }
            MY_MEDICAL -> {
                return MyMedical(parent)
            }

            YOUR_IMAGE -> {
                return YourImage(parent)
            }
            YOUR_AUDIO -> {
                return YourAudio(parent)
            }
            YOUR_PDF -> {
                return YourPdf(parent)
            }
            YOUR_TEXT -> {
                return YourText(parent)
            }
            YOUR_MP4 -> {
                return YourMp4(parent)
            }
            YOUR_GIF -> {
                return YourGIF(parent)
            }
            YOUR_MEDICAL -> {
                return YourMedical(parent)
            }
            SYSTEM -> {
                return SystemMessage(parent)
            }
            MY_BUTTONS -> {
                return YourButtons(parent)
            }
            MP3 -> {
                return MyAudio(parent)
            }
            else -> {
                return MyText(parent)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        if (releaseAll) {
            holder.release()
        }

        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        val conversation = list[position]
//        if (conversation.type == ChatTypes.SYSTEM) {
        if (conversation.type.equals("SYSTEM")) {
            return SYSTEM
        }
        if (currentUser.userId.equals(conversation.user.user_id)) {
            if (conversation.type == null) {
                return MY_TEXT
            }
            return when (conversation.type) {
//                ChatTypes.AUDIO -> {
//                    MY_AUDIO
//                }

                "AUDIO" -> {
                    MY_AUDIO
                }


                "MP3" -> {
                    MP3
                }

//                ChatTypes.IMAGE -> {
//                    MY_IMAGE
//                }

                "IMAGE" -> {
                    MY_IMAGE
                }

//                ChatTypes.PDF -> {
//                    MY_PDF
//                }

                "PDF" -> {
                    MY_PDF
                }

//                ChatTypes.MP4 -> {
//                    MY_MP4
//                }

                "MP4" -> {
                    MY_MP4
                }

//                ChatTypes.GIF -> {
//                    MY_GIF
//                }

                "GIF" -> {
                    MY_GIF
                }

//                ChatTypes.DOCUMENT -> {
//                    MY_MEDICAL
//                }

                "DOCUMENT" -> {
                    MY_MEDICAL
                }


//                ChatTypes.BUTTONS -> {
//                    MY_BUTTONS
//                }

                "BUTTONS" -> {
                    MY_BUTTONS
                }

                else -> {
                    MY_TEXT
                }
            }
        } else {
            if (conversation.type == null) {
                return MY_TEXT
            }
            return when (conversation.type) {
                "AUDIO" -> {
                    MY_AUDIO
                }
                "MP3" -> {
                    MP3
                }

                "IMAGE" -> {
                    MY_IMAGE
                }

                "PDF" -> {
                    MY_PDF
                }

                "MP4" -> {
                    MY_MP4
                }

                "GIF" -> {
                    MY_GIF
                }

                "DOCUMENT" -> {
                    MY_MEDICAL
                }

                "BUTTONS" -> {
                    MY_BUTTONS
                }

//                ChatTypes.AUDIO -> {
//                    YOUR_AUDIO
//                }
//                ChatTypes.IMAGE -> {
//                    YOUR_IMAGE
//                }
//                ChatTypes.PDF -> {
//                    YOUR_PDF
//                }
//                ChatTypes.MP4 -> {
//                    YOUR_MP4
//                }
//                ChatTypes.GIF -> {
//                    YOUR_GIF
//                }
//                ChatTypes.DOCUMENT -> {
//                    YOUR_MEDICAL
//                }
//                ChatTypes.BUTTONS -> {
//                    MY_BUTTONS
//                }
                else -> {
                    YOUR_TEXT
                }
            }
        }
    }

    open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(conversation: Conversation) = with(itemView) {

        }

        open fun release() {

        }
    }

    fun deleteDialog(context: Context, conversation: Conversation) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialog: AlertDialog = builder.setTitle("Delete message for Everyone")
            .setMessage("Do you want to delete this message ?")
            .setPositiveButton("Yes") { dialog, which ->
                onitemClickListener?.onItemLongClick(conversation);
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel();
            }
            .create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).typeface = Typeface.DEFAULT_BOLD
    }

    fun openBottomSlider(context: Context, conversation: Conversation) {
        val builder: BottomSheet.Builder? = context?.let { BottomSheet.Builder(it) };
        if (builder != null) {
            builder.setTitle("Title")
                .setView(R.layout.chat_action_bottom_sheet)
                .setFullWidth(false)
                .show();

            builder.view.findViewById<LinearLayout>(R.id.remove_for_all).setOnClickListener {
                builder.dismiss()
                onitemClickListener?.onItemLongClick(conversation);
            }

            builder.view.findViewById<LinearLayout>(R.id.remove_only_for_me).setOnClickListener {
                builder.dismiss()
            }
        };
    }

    inner class MyText(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_text_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeTextSend.text = conversation.timestamp.convertReadableTime()
//            currentUser.userId.equals(conversation.user.user_id)
//            if (position - 1 > -1 && !list[position - 1].user.user_id.equals(currentUser.userId)) {
//                Glide.with(context).load(list[position].user.profile_picture_url).into(chat_user)
//            } else if (position == 0) {
//                Glide.with(context).load(list[position].user.profile_picture_url).into(chat_user)
//
//            } else {
//                chat_user.setBackgroundResource(0);
//            }


            txtMyText.text = conversation.text
            txtMyText.setMovementMethod(LinkMovementMethod.getInstance())
            txtMyText.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {
                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })
            isVisibleMyProfileIcon = false;
        }
    }

    inner class MyAudio(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_record_send)),
        CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(playButton: CompoundButton?, b: Boolean) {
            if (b) {
                release()
                ayboPlayer = AyuboMediaPlayer(
                    playButton!!.context,
                    playButton!!,
                    seek!!,
                    txtDuration!!,
                    playButton.tag as String
                )
                ayboPlayer!!.playAudio()
            } else {
                ayboPlayer!!.pause()
            }
        }

        private var ayboPlayer: AyuboMediaPlayer? = null
        private var txtDuration: TextView? = null
        private var seek: SeekBar? = null

        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeRecordSend.text = conversation.timestamp.convertReadableTime()
            btnPlaySend.tag = conversation.media_url
            btnPlaySend.setOnCheckedChangeListener(this@MyAudio)
            txtDuration = txtDurationSend
            seek = seekBarSend

            containerSeekBarSend.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })
        }

        override fun release() {
            super.release()
            if (ayboPlayer != null)
                ayboPlayer?.release()
        }
    }

    inner class MyPdf(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_pdf_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimePdfSend.text = conversation.timestamp.convertReadableTime()
//            txtFileNameSend.text = conversation.media_url.substring(conversation.media_url.lastIndexOf("/") + 1)
            imgPdf.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            imgPdf.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })

        }
    }

    inner class MyImage(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_image_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeImageSend.text = conversation.timestamp.convertReadableTime()
            Glide.with(imgMyImage.context).load(conversation.media_url).into(imgMyImage)
            imgMyImage.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            imgMyImage.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })
            return@with
        }
    }

    inner class YourButtons(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_button_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {

            lay_button_holder.removeAllViews()

            for (item in conversation.button_array) {
                val button = Button(itemView.context)
                button.layoutParams = LinearLayout.LayoutParams(350, 100)
                button.text = item.text
                button.setOnClickListener(View.OnClickListener {

                    if (item.refresh.equals("1")) {
                        setButtonPressed(button, item.id.toString(), true)
                    } else {
                        setButtonPressed(button, item.id.toString(), false)
                    }




                    if (item.action.equals("post")) {
                        if (item.meta!!.length > 0) {
                            onitemClickListener?.onProcessActionClick("Post", item.meta!!)
                        }
                    }
                    if (item.action.equals("dynamicquestion")) {
                        if (item.meta!!.length > 0) {
                            onitemClickListener?.onProcessActionClick(
                                "Dynamicquestion",
                                item.meta!!
                            )
                        }
                    }
                    if (item.action.equals("janashakthiwelcome")) {
                        if (item.meta!!.length > 0) {
                            onitemClickListener?.onProcessActionClick(
                                "Janashakthireports",
                                item.meta!!
                            )
                        }
                    }
                    if (item.action.equals("janashakthireports")) {
                        if (item.meta!!.length > 0) {
                            onitemClickListener?.onProcessActionClick(
                                "Janashakthireports",
                                item.meta!!
                            )
                        }
                    }
                    if (item.action.equals("channeling")) {
                        if (item.meta!!.length > 0) {
                            onitemClickListener?.onProcessActionClick("Channeling", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Channeling", "")
                        }
                    }
                    if (item.action.equals("videocall")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Videocall", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Videocall", "")
                        }
                    }
                    if (item.action.equals("challenge")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Challenge", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Challenge", "")
                        }
                    }
                    if (item.action.equals("programtimeline")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Program", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Program", "")
                        }
                    }

                    if (item.action.equals("chat")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Chat", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Chat", "")
                        }
                    }

                    if (item.action.equals("goal")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Goal", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Goal", "")
                        }
                    }
                    if (item.action.equals("reports")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Reports", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Reports", "")
                        }
                    }
                    if (item.action.equals("points")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Points", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Points", "")
                        }
                    }
                    if (item.action.equals("help")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Help", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Help", "")
                        }
                    }
                    if (item.action.equals("native_post")) {
                        onitemClickListener?.onProcessActionClick("native_post", item.meta!!)
                    }
                    if (item.action.equals("programtimeline")) {
                        onitemClickListener?.onProcessActionClick("programtimeline", item.meta!!)
                    }
                    if (item.action.equals("program")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Program", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Program", "")
                        }
                    }
                    if (item.action.equals("web")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Web", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Web", "")
                        }
                    }
                    if (item.action.equals("commonview")) {
                        if (item.meta!!.length > 5) {
                            onitemClickListener?.onProcessActionClick("Commonview", item.meta!!)
                        } else {
                            onitemClickListener?.onProcessActionClick("Commonview", "")
                        }
                    }


                })


                var isClickable = true
                var isVisible = true

                isVisible = item.status != "Hidden"

                if (item.status == "Active") {
                    button.setBackgroundResource(R.drawable.chat_button_type_orange)
                    isClickable = true
                }
                if (item.status == "Disabled") {
                    isClickable = false
                    button.setBackgroundResource(R.drawable.chat_button_type_orange_disable)
                }
                if (item.status == "Answered") {
                    isClickable = false
                    button.setBackgroundResource(R.drawable.chat_button_type_orange_green)
                }

                if (isClickable) {
                    button.setClickable(true)
                } else {
                    button.setClickable(false)
                }




                button.text = item.text

                if (isVisible) {
                    button.setTextColor(Color.WHITE)
                    button.setTag(conversation)
                    val param = button.layoutParams as LinearLayout.LayoutParams
                    param.setMargins(10, 10, 10, 10)
                    button.layoutParams = param
                    lay_button_holder.addView(button)
                } else {

                }


            }
            txtTimeTextSend_New.text = conversation.timestamp.convertReadableDate()

        }
    }

    fun setButtonPressed(v: View, buttonID: String, refresh: Boolean) {
        val pref: PrefManager
        pref = PrefManager(v.context)
        val appToken = pref.getUserToken()


        val apiService = ApiClient.getNewApiClient().create(ApiInterface::class.java)
        val call = apiService.setButtonPressed(AppConfig.APP_BRANDING_ID, appToken, buttonID)
        call.enqueue(object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                val status = response.isSuccessful
                if (status) {

                    if (refresh) {
                        onitemClickListener?.onRefreshChatClick("meta", "")
                    }
                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                println("========t======$t")
            }
        })
    }

    inner class YourText(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_text_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {


            if (position - 1 > -1 && list[position - 1].user.user_id == currentUser.userId) {
                Glide.with(context).load(list[position].user.profile_picture_url)
                    .into(chat_user_receive)
            } else if (position == 0) {
                Glide.with(context).load(list[position].user.profile_picture_url)
                    .into(chat_user_receive)

            } else {
                chat_user_receive.setBackgroundResource(0);
            }


            txtTimeTextRecieve.text = conversation.timestamp.convertReadableTime()
            txtTextReceive.text = conversation.text
            txtTextReceive.setMovementMethod(LinkMovementMethod.getInstance())

        }
    }

    inner class YourAudio(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_record_receive)),
        CompoundButton.OnCheckedChangeListener {
        var seek: SeekBar? = null
        var txtDuration: TextView? = null
        private var ayboPlayer: AyuboMediaPlayer? = null


        override fun onCheckedChanged(playButton: CompoundButton?, b: Boolean) {
            if (b) {
                release()
                ayboPlayer = AyuboMediaPlayer(
                    playButton!!.context,
                    playButton,
                    seek!!,
                    txtDuration!!,
                    playButton.tag as String
                )
                ayboPlayer!!.playAudio()
            } else {
                ayboPlayer!!.pause()
            }
        }


        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeRecordReceive.text = conversation.timestamp.convertReadableTime()
            btnPlay.tag = conversation.media_url
            btnPlay.setOnCheckedChangeListener(this@YourAudio)
            seek = seekBar
            txtDuration = txtDurationRecieve
        }

        override fun release() {
            super.release()
            if (ayboPlayer != null)
                ayboPlayer?.release()
        }
    }

    inner class YourPdf(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_pdf_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimePdfRecieve.text = conversation.timestamp.convertReadableTime()
//            txtFileNameReceive.text = conversation.media_url.substring(conversation.media_url.lastIndexOf("/") + 1)
            imgRPdf.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
        }
    }

    inner class YourImage(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_image_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeImageRecieve.text = conversation.timestamp.convertReadableTime()
            Glide.with(imgImage.context).load(conversation.media_url).into(imgImage)
            imgImage.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            return@with
        }
    }

    inner class MyMp4(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_mp4_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeMp4Send.text = conversation.timestamp.convertReadableTime()
            Glide.with(context).load(conversation.media_thumbnail_url).into(imgSendMp4Thumbnail)
            btnPlaySendMp4.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            btnPlaySendMp4.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })
            return@with
        }
    }

    inner class YourMp4(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_mp4_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeMp4Receive.text = conversation.timestamp.convertReadableTime()
            Glide.with(context).load(conversation.media_thumbnail_url).into(imgReceiveMp4Thumbnail)
            btnPlayReceiveMp4.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            return@with
        }
    }

    inner class MyGIF(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_gif_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeGifSend.text = conversation.timestamp.convertReadableTime()
            Glide.with(context).load(conversation.media_thumbnail_url).into(imgSendGifThumbnail)
            btnPlaySendGif.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            btnPlaySendGif.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })
            return@with
        }
    }

    inner class YourGIF(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_gif_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeGifReceive.text = conversation.timestamp.convertReadableTime()
            Glide.with(context).load(conversation.media_thumbnail_url).into(imgReceiveGifThumbnail)
            btnPlayReceiveGif.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            return@with
        }
    }

    inner class SystemMessage(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_system_object)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeSystem.text = conversation.timestamp.convertReadableTime()
            txtMessage.text = conversation.text

        }
    }

    inner class MyMedical(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_medical_object_send)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeMedicalSend.text = conversation.timestamp.convertReadableTime()
            txtObjectTitleSend.text = conversation.title
            txtObjectHeaderSend.text = conversation.header

            if (conversation.button_ids != null) {
                buttonIdImageSend.visibility = View.VISIBLE
            } else {
                buttonIdImageSend.visibility = View.INVISIBLE
            }


            Log.d("My Medical ...", "")
            if (conversation.user_download) {
                btnDownloadSend.visibility = View.VISIBLE
            } else {
                btnDownloadSend.visibility = View.GONE
            }

            if (conversation.user_share) {
                btnShareSend.visibility = View.VISIBLE
            } else {
                btnShareSend.visibility = View.GONE
            }

            Log.d("Log from Kot", conversation.media_thumbnail_url!!)

            Glide.with(context)
                .load(conversation.media_thumbnail_url!!.replace("zoom_level", "mdpi"))
                .into(imgMedicalObjectPreviewSend)
            imgMedicalObjectPreviewSend.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            btnDownloadSend.setOnClickListener {
                onitemClickListener?.onDownloadClick(list[adapterPosition], adapterPosition)
            }

            btnShareSend.setOnClickListener {
                onitemClickListener?.onShareClick(list[adapterPosition], adapterPosition)
            }

            mediaObjectContainerSend.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {

                    if (v != null) {

                        openBottomSlider(context, conversation);
                    }
                    return true

                }
            })

            return@with
        }
    }

    inner class YourMedical(parent: ViewGroup) :
        BaseHolder(parent.inflate(R.layout.listitem_chat_medical_object_receive)) {
        override fun bind(conversation: Conversation) = with(itemView) {
            txtTimeMedicalReceive.text = conversation.timestamp.convertReadableTime()
            txtObjectTitle.text = conversation.title
            txtObjectHeader.text = conversation.header


            if (conversation.button_ids != null) {
                buttonIdContainer.visibility = View.VISIBLE
                buttonIdImage.visibility = View.VISIBLE

                txtButtonTitle.text = conversation.button_ids.toString().toLowerCase().capitalize()
                //Log.d("Button test .. ",conversation.button_ids.toLowerCase());
                txtButtonTitle.setOnClickListener {
                    onitemClickListener?.onButtonClick(list[adapterPosition], adapterPosition)
                }

                if (conversation.button_ids.equals("DELIVERY")) {
                    buttonIdImage.setImageResource(R.drawable.ic_delivery)
                    buttonIdImage.visibility = View.VISIBLE
                    buttonIdContainer.visibility = View.VISIBLE
                } else if (conversation.button_ids.equals("CHANNELING")) {
                    buttonIdImage.setImageResource(R.drawable.channeling_sm_ic)
                    buttonIdImage.visibility = View.VISIBLE
                    buttonIdContainer.visibility = View.VISIBLE
                } else {
                    buttonIdImage.visibility = View.INVISIBLE
                    buttonIdContainer.visibility = View.INVISIBLE
                }


            } else {
                buttonIdContainer.visibility = View.INVISIBLE
                buttonIdImage.visibility = View.INVISIBLE
            }



            if (conversation.user_download) {
                btnDownload.visibility = View.VISIBLE
            } else {
                btnDownload.visibility = View.GONE
            }

            if (conversation.media_type!!.isEmpty()) {
                btnDownload.visibility = View.VISIBLE
            } else {
                btnDownload.visibility = View.GONE
            }

            if (conversation.user_share) {
                btnShare.visibility = View.VISIBLE
            } else {
                btnShare.visibility = View.GONE
            }

            Glide.with(context)
                .load(conversation.media_thumbnail_url!!.replace("zoom_level", "xxxhdpi"))
                .into(imgMedicalObjectPreview)
            imgMedicalObjectPreview.setOnClickListener {
                onitemClickListener?.onItemClick(list[adapterPosition], adapterPosition)
            }
            btnDownload.setOnClickListener {
                onitemClickListener?.onDownloadClick(list[adapterPosition], adapterPosition)
            }

            btnShare.setOnClickListener {
                onitemClickListener?.onShareClick(list[adapterPosition], adapterPosition)
            }
            return@with
        }
    }


}