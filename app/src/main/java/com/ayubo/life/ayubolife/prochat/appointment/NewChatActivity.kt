package com.ayubo.life.ayubolife.prochat.appointment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.signalr.SignalRSingleton
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_new_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewChatActivity : AppCompatActivity() {
    var active: Boolean = false
    var messagesArrayList: ArrayList<NewMessage> = ArrayList<NewMessage>()
    private var chatMessagesAdapter: ChatMessagesAdapter? = null
    var sessionId: String = ""
    var currentUserId: String = ""
    lateinit var pref: PrefManager
    lateinit var signalRSingleton: SignalRSingleton
    lateinit var sessionDataInfo: SessionDataInfo
    lateinit var linearLayoutManager: LinearLayoutManager
    var unreadMessageCount: Int = 0
    var isInitialLoad: Boolean = false;


    override fun onStart() {
        super.onStart()
        active = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        active = true
        pref = PrefManager(baseContext)
        signalRSingleton = SignalRSingleton.getInstance(baseContext)
        currentUserId = pref.loginUser["uid"].toString()
        messagesArrayList = ArrayList<NewMessage>()
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        messages_recycler_view.layoutManager = linearLayoutManager
        isInitialLoad = true;

        val currentIntent: Intent = intent
        sessionId = currentIntent.getStringExtra("sessionId").toString()

        getChatMessagesData()

        if (active) {
            initializeSignalRData()
        }

        setButtonClicks()

    }

    private fun setButtonClicks() {
        new_chat_type_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence != null) {
                    val empty = charSequence.replace("\\s".toRegex(), "")
                    if (empty != "") {
                        new_chat_send_btn.setImageDrawable(null)
                        new_chat_send_btn.setBackgroundResource(R.drawable.new_chat_send_image)
                        if (Constants.type == Constants.Type.LIFEPLUS) {
                            layout_for_chat_send_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)
                        } else {
                            layout_for_chat_send_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
                        }


                        val chatUserTypingPayload = ChatUserTypingPayload(
                            pref.loginUser["name"].toString() + " is typing...",
                            "on",
                            sessionId
                        )

                        val jsonObjectOnTextChanged: JsonObject =
                            Gson().toJsonTree(chatUserTypingPayload).asJsonObject

                        signalRSingleton.sendSignal(
                            "typing",
                            jsonObjectOnTextChanged,
                            sessionDataInfo.members
                        );
                    } else {
                        new_chat_send_btn.setImageDrawable(null)
                        new_chat_send_btn.setBackgroundResource(R.drawable.ic_microphone_white_24dp)
                        layout_for_chat_send_btn.setBackgroundResource(R.drawable.disable_shape_rect_round)

                        stopSendTypingSignalR()
                    }


                } else {
                    new_chat_send_btn.setImageDrawable(null);
                    new_chat_send_btn.setBackgroundResource(R.drawable.ic_microphone_white_24dp)
                    layout_for_chat_send_btn.setBackgroundResource(R.drawable.disable_shape_rect_round)
                    stopSendTypingSignalR()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        layout_for_chat_send_btn.setOnClickListener {
            stopSendTypingSignalR();

            val emptyText = new_chat_type_text.text.replace("\\s".toRegex(), "")

            if (emptyText != "") {
                val typedText = new_chat_type_text.text.toString();


                val imageURL = pref.loginUser["image_path"].toString();
                var sendImage = "";

                if (imageURL.contains(".jpg") || imageURL.contains(".jpeg") || imageURL.contains(".png")) {
                    sendImage = pref.loginUser["image_path"].toString()
                } else {
                    sendImage = ApiClient.MAIN_URL_LIVE_HAPPY + imageURL;

                }


                val newMessage = NewMessage(
                    System.currentTimeMillis().toString(),
                    AppConfig.APP_BRANDING_ID,
                    currentUserId,
                    typedText,
                    "text",
                    "user",
                    1,
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    sessionId,
                    NewChatUser(
                        pref.loginUser["name"].toString(),
                        sendImage
                    ),
                    ArrayList<String>(),
                    ArrayList<String>()
                )


                val checkArrayList = ArrayList<NewMessage>();
                checkArrayList.addAll(messagesArrayList)


                checkArrayList.map {
                    if (it.type == "unread_label") {
                        messagesArrayList.remove(it);
                    }
                }


                if (messagesArrayList.size > 1) {
                    val cDate = convertLongToTime(newMessage.create_datetime);
                    val pDate =
                        convertLongToTime(messagesArrayList[messagesArrayList.size - 1].create_datetime)



                    if (cDate != pDate) {
                        addEmptyDateObject(newMessage);
                    }

                } else {
                    addEmptyDateObject(newMessage);
                }

                messagesArrayList.add(newMessage);

                val jsonObjectNewMessage: JsonObject = Gson().toJsonTree(newMessage).asJsonObject;

                signalRSingleton.sendSignal(
                    "send_message",
                    jsonObjectNewMessage,
                    sessionDataInfo.members
                );

                runOnUiThread {
                    messages_recycler_view.removeAllViews()
                    chatMessagesAdapter =
                        ChatMessagesAdapter(
                            baseContext,
                            currentUserId,
                            messagesArrayList,
                            false,
                            sessionDataInfo,
                            true
                        );
                    messages_recycler_view.setHasFixedSize(true);
                    messages_recycler_view.adapter = chatMessagesAdapter;
                }

                new_chat_type_text.setText("");
            }


        }

        new_chat_back_btn.setOnClickListener {
            finish()
        }

        layout_for_unread_msg.setOnClickListener {
            unreadMessageCount = 0;
            runOnUiThread {
                print(messagesArrayList)
                chatMessagesAdapter!!.notifyItemRangeInserted(
                    chatMessagesAdapter!!.itemCount,
                    messagesArrayList.size
                );


                var isScrolledToPosition: Boolean = false;
                var scrollToPosition: Int = messagesArrayList.size;

                messagesArrayList.forEachIndexed { index, currentMessage ->
                    if ((currentMessage.type != "unread_label" && currentMessage.type != "date_label")
                        &&
                        (currentMessage.read_by == null || !currentMessage.read_by!!.contains(
                            currentUserId
                        ))
                        && !isScrolledToPosition
                    ) {
                        print(index)
                        scrollToPosition = index + 1
                        isScrolledToPosition = true;

                    }

                }

                messages_recycler_view.scrollToPosition(scrollToPosition - 1);
                layout_for_unread_msg.visibility = View.GONE
            }


        }
    }

    private fun initializeSignalRData() {


        signalRSingleton.setSignalRObjectListener(object : SignalRSingleton.SignalRObjectListener {


            //Receiving New Message
            override fun onReceiveNewMessageReady(commonPayload: CommonPayload) {

                if (active) {
                    print(commonPayload)

                    val newMessage: NewMessage =
                        Gson().fromJson(
                            commonPayload.payload,
                            NewMessage::class.java
                        );



                    if (newMessage.session_id == sessionId) {
                        runOnUiThread {
                            unreadMessageCount += 1

                            if (newMessage.read_by == null) {
                                newMessage.read_by = ArrayList<String>();
                            }





                            if (messagesArrayList.size > 1) {
                                val cDate = convertLongToTime(newMessage.create_datetime);
                                val pDate =
                                    convertLongToTime(
                                        messagesArrayList[messagesArrayList.size - 1].create_datetime
                                    )



                                if (cDate != pDate) {
                                    addEmptyDateObject(newMessage);
//                                    messagesArrayList.add(
//                                        NewMessage(
//                                            "",
//                                            "",
//                                            "",
//                                            convertTime(newMessage.create_datetime),
//                                            "",
//                                            "date_label",
//                                            0,
//                                            0L,
//                                            0L,
//                                            0L,
//                                            "",
//                                            UserDetail("", "", ""),
//                                            ArrayList<String>(),
//                                            ArrayList<String>()
//                                        )
//                                    )
                                }

                            } else {
                                addEmptyDateObject(newMessage);
//                                messagesArrayList.add(
//                                    NewMessage(
//                                        "",
//                                        "",
//                                        "",
//                                        convertTime(newMessage.create_datetime),
//                                        "",
//                                        "date_label",
//                                        0,
//                                        0L,
//                                        0L,
//                                        0L,
//                                        "",
//                                        UserDetail("", "", ""),
//                                        ArrayList<String>(),
//                                        ArrayList<String>()
//                                    )
//                                )
                            }


                            messagesArrayList.add(newMessage);




                            setRecycleData()
                        }

                    }
                }


            }

            //Typing Message
            override fun onMessageTyping(commonPayload: CommonPayload) {

                if (active) {

                    runOnUiThread {
                        if (commonPayload.payload.get("status").asString == "on") {
                            text_view_for_typing_msg.visibility = View.VISIBLE
                            text_view_for_typing_msg.text =
                                commonPayload.payload.get("text").asString

                        } else {
                            text_view_for_typing_msg.visibility = View.GONE
                            text_view_for_typing_msg.text = ""
                        }
                    }
                }
            }

            //Message Acknowledgement
            override fun onMessageAcknowledgement(commonPayload: CommonPayload) {

                if (active) {

                    print(commonPayload)


                    val commonPayloadMessage: NewMessage =
                        Gson().fromJson(
                            commonPayload.payload,
                            NewMessage::class.java
                        );

                    if (commonPayloadMessage.session_id == sessionId && commonPayload.notification_type != "send_message") {
                        for (i in 0 until messagesArrayList.size) {
                            if (messagesArrayList[i].id == commonPayloadMessage.id) {
                                print(sessionDataInfo)
                                messagesArrayList[i] = commonPayloadMessage;
                            }
                        }

                        runOnUiThread {
                            if (chatMessagesAdapter != null) {
                                messages_recycler_view.post {
                                    chatMessagesAdapter!!.notifyDataSetChanged();
                                }
                            }

                        }
                    }


                }
            }

            override fun onUserStats(jsonObject: JsonObject?) {
            }

        })
    }

    private fun setRecycleData() {

        chatMessagesAdapter!!.notifyItemRangeInserted(
            chatMessagesAdapter!!.itemCount,
            messagesArrayList.size
        );

        val p = linearLayoutManager.findLastCompletelyVisibleItemPosition()

        print(p)

        if (p > (messagesArrayList.size - 6)) {
            messages_recycler_view.scrollToPosition(messagesArrayList.size - 1);
        } else {
            print("display to scroll bottom")
            text_view_for_unread_message.text = unreadMessageCount.toString()
            layout_for_unread_msg.visibility = View.VISIBLE

        }

    }

    private fun getChatMessagesData() {
        progress_for_new_chat.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureChatBaseUrl().create(ApiInterface::class.java);

        val call: Call<ProfileDashboardResponseData> = apiService.getChatDataBySessionId(
            AppConfig.APP_BRANDING_ID,
            pref.userToken,
            sessionId
        );


        call.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                progress_for_new_chat.visibility = View.GONE
                if (response.isSuccessful) {
                    print(response)

                    val messageMainData: JsonObject =
                        Gson().toJsonTree(response.body()!!.data).asJsonObject;

                    sessionDataInfo =
                        Gson().fromJson(
                            messageMainData.get("session_info").asJsonObject,
                            SessionDataInfo::class.java
                        );

                    if (sessionDataInfo.status == "inactive") {
                        new_chat_action_main.visibility = View.GONE
                    }

                    print(sessionDataInfo);
                    new_topic.text = sessionDataInfo.user_details.name
                    new_sub_topic.text = sessionDataInfo.category
                    val messagesArrayListMain = messageMainData.get("messages").asJsonArray


                    var isFirstTimeLabelShowing: Boolean = false;

                    for (index in 0 until messagesArrayListMain.size()) {
                        val currentMessage: NewMessage =
                            Gson().fromJson(
                                messagesArrayListMain.get(index),
                                NewMessage::class.java
                            );


                        if (index > 0) {
                            val previousMessage = Gson().fromJson(
                                messagesArrayListMain.get(index - 1),
                                NewMessage::class.java
                            );


                            val currentMessageDate =
                                convertLongToTime(currentMessage.create_datetime);
                            val previousMessageDate =
                                convertLongToTime(previousMessage.create_datetime)
                            if (currentMessageDate != previousMessageDate) {
                                addEmptyDateObject(currentMessage);
                            }
                        }


                        if (index == 0) {
                            addEmptyDateObject(currentMessage);
                        }

                        if (
                            currentMessage.read_by == null
                            ||
                            (!currentMessage.read_by!!.contains(currentUserId)) && !isFirstTimeLabelShowing
                        ) {

                            isFirstTimeLabelShowing = true;
                            messagesArrayList.add(
                                NewMessage(
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "unread_label",
                                    0,
                                    0L,
                                    0L,
                                    0L,
                                    "",
                                    NewChatUser("", "", ""),
                                    ArrayList<String>(),
                                    ArrayList<String>()
                                )
                            )
                        }




                        messagesArrayList.add(currentMessage)
                    }


                    var scrollToPosition: Int = messagesArrayList.size;
                    var isScrolledToPosition: Boolean = false;

                    messagesArrayList.forEachIndexed { index, newMessage ->

                        if ((newMessage.type != "unread_label" && newMessage.type != "date_label")
                            &&
                            (newMessage.read_by == null || !newMessage.read_by!!.contains(
                                currentUserId
                            ))
                            &&
                            !isScrolledToPosition
                        ) {
                            scrollToPosition = index + 1
                            isScrolledToPosition = true;
                        }
                    }

                    chatMessagesAdapter =
                        ChatMessagesAdapter(
                            baseContext,
                            currentUserId,
                            messagesArrayList,
                            false,
                            sessionDataInfo,
                            false
                        );
                    messages_recycler_view.post {
                        chatMessagesAdapter!!.notifyDataSetChanged();
                    }
                    messages_recycler_view.setHasFixedSize(true);
                    messages_recycler_view.adapter = chatMessagesAdapter;
                    messages_recycler_view.scrollToPosition(scrollToPosition);



                    messages_recycler_view.addOnScrollListener(object :
                        RecyclerView.OnScrollListener() {

                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            val p = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            if (p == (messagesArrayList.size - 1)) {
                                unreadMessageCount = 0
                                layout_for_unread_msg.visibility = View.GONE


                                val checkArrayList = ArrayList<NewMessage>();
                                checkArrayList.addAll(messagesArrayList)

                                if (!isInitialLoad) {
                                    checkArrayList.map {
                                        if (it.type == "unread_label") {
                                            messagesArrayList.remove(it);
                                        }
                                    }
                                }
                                isInitialLoad = false;

                                messages_recycler_view.post {
                                    chatMessagesAdapter!!.notifyDataSetChanged()
                                }

                            }

                        }
                    })
                }
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                progress_for_new_chat.visibility = View.GONE
                throwable.printStackTrace()
            }
        })


    }

    private fun addEmptyDateObject(currentMessage: NewMessage) {
        messagesArrayList.add(
            NewMessage(
                "",
                "",
                "",
                convertTime(currentMessage.create_datetime),
                "",
                "date_label",
                0,
                0L,
                0L,
                0L,
                "",
                NewChatUser("", "", ""),
                ArrayList<String>(),
                ArrayList<String>()
            )
        )
    }

    override fun onStop() {
        super.onStop()
        stopSendTypingSignalR();
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false;
        sessionId = "";
        unreadMessageCount = 0;
    }

    fun stopSendTypingSignalR() {
        val chatUserTypingPayload: ChatUserTypingPayload = ChatUserTypingPayload(
            "",
            "off",
            sessionId
        )

        val jsonObjectAfterTextChanged: JsonObject =
            Gson().toJsonTree(chatUserTypingPayload).asJsonObject;

        signalRSingleton.sendSignal(
            "typing",
            jsonObjectAfterTextChanged,
            sessionDataInfo.members
        );
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormatForDate.format(date)
    }

    fun convertTime(time: Long): String {

        val timeDate = Date(time)


        val timeCalendar = toCalendar(timeDate);

        val today: Calendar = Calendar.getInstance();
        val yesterday: Calendar = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);


        val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
//        val strDate = simpleDateFormatForDate.format(timeDate)

        var dateTimeValue: String = "";


//        val todayDate: Date = Date()
//
//
//        val strTodayDate: String = simpleDateFormatForDate.format(todayDate);
//
//        val comparedValue: Int = strDate.compareTo(strTodayDate);
//
//
//        if (comparedValue == 0) {
//            dateTimeValue = "Today";
//        } else if (comparedValue == -1) {
//            dateTimeValue = "Yesterday";
//        } else {
//            val newFormat = SimpleDateFormat("dd MMMM yyyy");
//            dateTimeValue = newFormat.format(timeDate);
//        }

        if (
            timeCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            &&
            timeCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
        ) {
            dateTimeValue = "Today";
        } else if (
            timeCalendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
            &&
            timeCalendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
        ) {
            dateTimeValue = "Yesterday";
        } else {
            val newFormat = SimpleDateFormat("dd MMMM yyyy");
            dateTimeValue = newFormat.format(timeDate);
        }

        return dateTimeValue;


    }

    fun toCalendar(date: Date): Calendar {
        val cal: Calendar = Calendar.getInstance();
        cal.time = date;
        return cal;
    }

}