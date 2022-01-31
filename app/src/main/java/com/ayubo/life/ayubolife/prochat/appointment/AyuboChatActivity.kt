package com.ayubo.life.ayubolife.prochat.appointment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.captureimages.ChooseImageDialog
import com.ayubo.life.ayubolife.prochat.captureimages.ImageCompresser
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_CHAT
import com.ayubo.life.ayubolife.prochat.data.model.Conversation
import com.ayubo.life.ayubolife.prochat.data.model.OtherUser
import com.ayubo.life.ayubolife.prochat.util.CallPhone
import com.ayubo.life.ayubolife.prochat.util.CommonUtils
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.activity_new_discover_program.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class AyuboChatActivity : BaseActivity(), AdapterAppointment.OnItemClickListener {

    override fun onProcessActionClick(activityName: String, meta: String) {
        processAction(activityName, meta)
    }

    private var media_url: String? = null
    private var external_expert_id: String = ""
    private var button_type: String = ""
    private var params: DocSearchParameters? = null
    private var prefManager: PrefManager? = null

    private var REQUEST_GALLERY: Int = 7;
    private var REQUEST_CODE_PERMISSION_STORAGE: Int = 1;

    private var otheruser: OtherUser? = null
    private var doctorId: String? = null
    private var isAppointmentHistory = false
    private var isNewAPI: Boolean = false
    private var sessionId: String? = null

    @Inject
    lateinit var appointmentVM: AppointmentVM

    private var adapterMy: AdapterAppointment? = null


    var chatByDateObjectDataList: ArrayList<ChatByDateObject> = ArrayList<ChatByDateObject>();


    override fun onButtonClick(conversation: Conversation, position: Int) {
        if (conversation.button_ids.equals("DELIVERY")) {
            val intent = Intent(this, HealthNetBuy_Activity::class.java)
            intent.putExtra("media_url", conversation.media_url)
            intent.putExtra("button_type", conversation.button_ids.toString())
            intent.putExtra("external_expert_id", conversation.external_expert_id)
            startActivity(intent)
        } else if (conversation.button_ids.equals("CHANNELING")) {
            val intent = Intent(this, SearchActivity::class.java)
            params = DocSearchParameters()
            prefManager = PrefManager(this)
            params!!.user_id = prefManager!!.getLoginUser().get("uid")
            params!!.date = ""
            params!!.specializationId = ""
            params!!.doctorId = conversation.external_expert_id
            params!!.locationId = ""
            intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(params))
            startActivity(intent)
        }
    }

    override fun onDownloadClick(conversation: Conversation, position: Int) {
        if (conversation.media_type!!.isEmpty()) {
            downloadFile(conversation.media_url!!)
        }
    }

    //
    override fun onShareClick(conversation: Conversation, position: Int) {
        if (conversation.media_type!!.isEmpty()) {
            CommonUtils.shareLink(this@AyuboChatActivity, conversation.media_url!!)
        } else {
            if (!conversation.media_type.equals("link")) {
                downloadFile(conversation.media_url!!, true)
            } else {
                CommonUtils.shareLink(this@AyuboChatActivity, conversation.media_url!!)
            }
        }
    }

    override fun onRefreshChatClick(activityName: String, meta: String) {
        refreshListView()
    }

    override fun onItemLongClick(conversation: Conversation) {
        deleteChat(conversation);


    }

    fun deleteChat(conversation: Conversation) {

        subscription.add(appointmentVM.deleteChat(conversation.conversation_id.toString())!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    refreshListView();
                } else {
                    showMessage(MSG_FAILED_REQUEST)
                }
            }, {
                showMessage(MSG_FAILED_REQUEST)
            })
        )
    }

    override fun onItemClick(conversation: Conversation, position: Int) {
        previewContainer.visibility = View.VISIBLE
        btnClosePreview.visibility = View.VISIBLE

        pdfView.visibility = View.GONE
        imagePreview.visibility = View.GONE
        simpleExoPlayerView.visibility = View.GONE
        webView.visibility = View.GONE

        when (conversation.type) {


//            ChatTypes.PDF -> {
//                downloadAndViewPdf(conversation.media_url)
//            }

            "PDF" -> {
                downloadAndViewPdf(conversation.media_url!!)
            }


//            ChatTypes.IMAGE -> {
//                imagePreview.visibility = View.VISIBLE
//                imagePreview.scaleType = ImageView.ScaleType.FIT_CENTER
//                Glide.with(this).load(conversation.media_url).into(imagePreview)
//            }

            "IMAGE" -> {
                imagePreview.visibility = View.VISIBLE
                imagePreview.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(this).load(conversation.media_url).into(imagePreview)
            }


//            ChatTypes.MP4 -> {
//                simpleExoPlayerView.visibility = View.VISIBLE
//                playVideo(conversation.media_url)
//            }

            "MP4" -> {
                simpleExoPlayerView.visibility = View.VISIBLE
                playVideo(conversation.media_url!!)
            }


//            ChatTypes.GIF -> {
//            progressBar.visibility = View.VISIBLE
//            imagePreview.visibility = View.VISIBLE
//            imagePreview.scaleType = ImageView.ScaleType.CENTER_INSIDE
//            Glide.with(this).load(conversation.media_url)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                                e: GlideException?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                isFirstResource: Boolean
//                        ): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//
//                        override fun onResourceReady(
//                                resource: Drawable?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                dataSource: com.bumptech.glide.load.DataSource?,
//                                isFirstResource: Boolean
//                        ): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//
//                    })
//                    .into(imagePreview)
//        }


            "GIF" -> {
                progressBar.visibility = View.VISIBLE
                imagePreview.visibility = View.VISIBLE
                imagePreview.scaleType = ImageView.ScaleType.CENTER_INSIDE
                Glide.with(this).load(conversation.media_url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(imagePreview)
            }


//            ChatTypes.DOCUMENT -> {
//                webView.visibility = View.VISIBLE
//                if (!conversation.media_type.isEmpty()) {
//
//                    if (conversation.media_type == "LINK") {
//                        loadUrlInWebView(conversation.media_url)
//                    } else {
//                        downloadAndViewPdf(conversation.media_url)
//
//                    }
//                } else {
//                    loadUrlInWebView(conversation.media_url)
//                }
//            }

            "DOCUMENT" -> {
                webView.visibility = View.VISIBLE
                if (!conversation.media_type!!.isEmpty()) {

                    if (conversation.media_type == "LINK") {
                        loadUrlInWebView(conversation.media_url!!)
                    } else {
                        downloadAndViewPdf(conversation.media_url!!)

                    }
                } else {
                    loadUrlInWebView(conversation.media_url!!)
                }
            }
        }
    }


//    private var player: SimpleExoPlayer? = null

    private fun playVideo(url: String) {
        simpleExoPlayerView.requestFocus()
        simpleExoPlayerView.setVideoURI(Uri.parse(url))
        simpleExoPlayerView.start();

//        val bandwidthMeter = DefaultBandwidthMeter()
//
//
//        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//
//        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
//
//        this.player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
//
//        simpleExoPlayerView.player = player
//
//        player!!.playWhenReady = true
//
//
//        val mediaDataSourceFactory = DefaultDataSourceFactory(
//            this,
//            Util.getUserAgent(this, "mediaPlayerSample"),
//            bandwidthMeter as TransferListener<in DataSource>
//        )
//        val sampleSource =
//            ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url))
//        player!!.prepare(sampleSource)


    }


    private fun viewPdf(file: File) {
        progressBar.visibility = View.VISIBLE
        pdfView.fromFile(file)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .spacing(5)
            .scrollHandle(DefaultScrollHandle(this))
            .enableAntialiasing(true)
            .onLoad {
                progressBar.visibility = View.GONE
            }
            .load()
    }

    private fun downloadFile(url: String, share: Boolean = false) {
//        subscription.add(
//                appointmentVM.download(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
//                .doOnTerminate { progressBar.visibility = View.GONE }
//                .doOnError { progressBar.visibility = View.GONE }
//                .subscribe({
//                    if (!share) {
//                        CommonUtils.showNotification(this@AyuboChatActivity, appointmentVM.getFile()!!)
//                    } else {
//                        CommonUtils.shareFiles(this@AyuboChatActivity, appointmentVM.getFile()!!)
//                    }
//
//                }, {
//                    progressBar.visibility = View.GONE
//                    showMessage("Failed downloading the file")
//                })
//        )


        appointmentVM.openNewTabWindow(url, this@AyuboChatActivity);
        progressBar.visibility = View.GONE;
//        val myWebView: WebView = findViewById(R.id.webView)
//        myWebView.visibility = View.VISIBLE
//        myWebView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                view?.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, request)
//            }
//        }
//        myWebView.loadUrl(url);

    }

    private fun downloadAndViewPdf(url: String) {
        subscription.add(appointmentVM.download(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                pdfView.visibility = View.VISIBLE
                viewPdf(appointmentVM.getFile()!!)
            }, {
                showMessage("Failed downloading pdf")
            })
        )
    }

    private fun loadUrlInWebView(url: String, isGif: Boolean = false) {
        webView.clearView()
        progressBar.visibility = View.VISIBLE
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                view!!.clearHistory()
            }
        }
        if (isGif) {
            webView.setBackgroundColor(Color.TRANSPARENT)
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
        }
    }

    companion object {
        fun startActivity(
            context: Activity,
            doctorId: String,
            isAppointmentHistory: Boolean,
            isNewAPI: Boolean,
            sessionId: String
        ) {
            val intent = Intent(context, AyuboChatActivity::class.java)
            intent.putExtra("doctorId", doctorId)
            intent.putExtra("isAppointmentHistory", isAppointmentHistory)
            intent.putExtra("isNewAPI", isNewAPI)
            intent.putExtra("sessionId", sessionId)

            context.startActivityForResult(intent, REQUEST_CODE_CHAT)
        }
    }

    override fun onPause() {
        super.onPause()
        if (adapterMy != null)
            adapterMy!!.release()
    }

    private lateinit var callPhone: CallPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        App.getInstance().appComponent.inject(this)


        // if (Constants.type == Constants.Type.LIFEPLUS) {
        val noticolr = "#000000"
        val whiteInta = Color.parseColor(noticolr)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = whiteInta

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView

            //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            decor.systemUiVisibility = 0
            //   decor.setSystemUiVisibility(0);
            // }
        }
        //   }
        requestPermissions()

        callPhone = CallPhone(this)
        doctorId = intent.getStringExtra("doctorId")
        isAppointmentHistory = intent.getBooleanExtra("isAppointmentHistory", false)
        isNewAPI = intent.getBooleanExtra("isNewAPI", false)
        sessionId = intent.getStringExtra("sessionId")



        if (isAppointmentHistory) {
            btnComplete.isEnabled = false
            btnReviewLater.isEnabled = false
            btnCreatePrescription.isEnabled = false
            btnDialNow.isEnabled = false
            btnAttachment.isEnabled = false
            btnCamera.isEnabled = false
            etChat.isEnabled = false
            btnRecord.isEnabled = false
        }
        refreshListView()

        initUI()
    }

    fun refreshListView() {


        if (!isNewAPI) {
            subscription.add(appointmentVM.getChatAll(doctorId!!)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        setData();
//                        adapterMy = AdapterAppointment(appointmentVM.getCurrentUser(), appointmentVM.conversations!!, false, chatByDateObjectDataList)
//                        adapterMy!!.onitemClickListener = this@AyuboChatActivity
//
//
//                        listConversations.apply {
//                            layoutManager = mlayoutManager
//                            adapter = adapterMy
//
//                            otheruser = appointmentVM.otherUser
//                            txtName.text = otheruser!!.full_name
//                            txtPhoneNumber.text = otheruser!!.speciality
//                        }


                    } else {
                        showMessage(MSG_FAILED_REQUEST)
                    }
                }, {
                    showMessage(MSG_FAILED_REQUEST)
                })
            )
        } else {
            // call new getchat API
            System.out.println(isNewAPI)
            System.out.println(sessionId)

        }

    }

    fun setData() {
        val dateTypesDataList: ArrayList<String>? = ArrayList<String>()
        chatByDateObjectDataList = ArrayList<ChatByDateObject>()

        System.out.println(appointmentVM.conversations!!)
        listConversationsLinearLayout.removeAllViews()


        for (c in 0 until appointmentVM.conversations!!.size) {
            val data = appointmentVM.conversations!![c]
            if (dateTypesDataList!!.size > 0) {
                if (!dateTypesDataList.contains(convertLongToTime(data.timestamp))) {
                    dateTypesDataList.add(convertLongToTime(data.timestamp))
                }
            } else {
                dateTypesDataList.add(convertLongToTime(data.timestamp))

            }
        }


        for (n in 0 until dateTypesDataList!!.size) {
            val chatByDate = dateTypesDataList[n]
            val chatByDateDataList: ArrayList<Conversation> = ArrayList<Conversation>()


            for (i in 0 until appointmentVM.conversations!!.size) {
                val data = appointmentVM.conversations!![i]
                if (convertLongToTime(appointmentVM.conversations!![i].timestamp) == chatByDate) {
                    chatByDateDataList.add(data)
                }
            }

            val singleCategoryBadgeList = ChatByDateObject(chatByDate, chatByDateDataList)
            chatByDateObjectDataList.add(singleCategoryBadgeList)
        }

        System.out.println(chatByDateObjectDataList)

        for (c in 0 until chatByDateObjectDataList.size) {
            val innerScrolledList: ChatByDateObject = chatByDateObjectDataList[c]

//            val chatByDateMainView = LinearLayout(baseContext)
//            chatByDateMainView.orientation = LinearLayout.VERTICAL

//            chatByDateMainView.setPadding(0, 0, 0, 16)

            val sectionHeaderText = TextView(baseContext)

            val paramsForTextView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            paramsForTextView.setMargins(0, 8, 0, 24)
            sectionHeaderText.layoutParams = paramsForTextView;

            val simpleDateFormatForDate: SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy");

            val dateForDay: Date = simpleDateFormatForDate.parse(innerScrolledList.dateLong);
            val simpleDateFormatForDate1: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy");

            val newDateString = simpleDateFormatForDate1.format(dateForDay);


            sectionHeaderText.setText(newDateString)
            sectionHeaderText.setTextSize(14F)
            sectionHeaderText.setTextColor(getResources().getColor(R.color.color_727272))
            sectionHeaderText.setBackgroundResource(R.drawable.chat_date_background)
            val typeface: Typeface? =
                ResourcesCompat.getFont(baseContext, R.font.montserrat_regular);
            sectionHeaderText.setTypeface(typeface);
            sectionHeaderText.setPadding(20, 20, 20, 20)
//            val params = ViewGroup.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT)
//            chatByDateMainView.layoutParams = params

//            chatByDateMainView.addView(sectionHeaderText)
            listConversationsLinearLayout.addView(sectionHeaderText)


            val recyclerView = RecyclerView(baseContext)

            adapterMy = AdapterAppointment(
                appointmentVM.getCurrentUser(),
                innerScrolledList.conversations!!,
                false
            )
            adapterMy!!.onitemClickListener = this@AyuboChatActivity
            val mlayoutManager =
                LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            mlayoutManager.stackFromEnd = true
            val paramsRecyclerView: RecyclerView.LayoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.layoutParams = paramsRecyclerView
            recyclerView.apply {
                layoutManager = mlayoutManager
                adapter = adapterMy

                otheruser = appointmentVM.otherUser
                txtName.text = otheruser!!.full_name
                txtPhoneNumber.text = otheruser!!.speciality
            }

//            chatByDateMainView.addView(recyclerView)
            listConversationsLinearLayout.addView(recyclerView)
//            listConversationsLinearLayout.addView(chatByDateMainView)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            layout_chat_scroll_view.isSmoothScrollingEnabled = true
            layout_chat_scroll_view.fullScroll(View.FOCUS_DOWN);

//            layout_chat_scroll_view.smoothScrollTo(0, View.FOCUS_DOWN)
        }, 1000);


    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time * 1000)
        val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormatForDate.format(date)
    }


    private var isTexting = false

    private var imageDialog: ChooseImageDialog? = null

    private fun initUI() {
        btnRecord.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                recordData.visibility = View.VISIBLE
                etChat.isEnabled = false
                appointmentVM.record(this@AyuboChatActivity)
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
            } else {
                recordData.visibility = View.GONE
                etChat.isEnabled = true
                chronometer.stop()
                appointmentVM.stopRecoding()

                showAlertTwoButton(R.string.confirmation,
                    R.string.msg_upload_confirm,
                    R.string.btn_text_yes,
                    R.string.btn_text_no,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            dialog!!.dismiss()
//
                            val file = appointmentVM.getFile()
                            progressBar.visibility = View.VISIBLE
                            Handler().postDelayed({
                                subscription.add(appointmentVM.addChat(
                                    doctorId!!,
                                    file,
                                    etChat.text.toString()
                                )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                                    .doOnTerminate { progressBar.visibility = View.GONE }
                                    .doOnError { progressBar.visibility = View.GONE }
                                    .subscribe({
                                        if (it.isSuccess) {
                                            if (appointmentVM.conversations != null) {
                                                adapterMy!!.notifyDataSetChanged()
//                                                listConversations.smoothScrollToPosition(appointmentVM.conversations!!.size - 1)
                                                setData();
//                                                layout_chat_scroll_view.smoothScrollTo(0, chatByDateObjectDataList.size - 1)
//                                                layout_chat_scroll_view.smoothScrollTo(0, 0)
                                                layout_chat_scroll_view.isSmoothScrollingEnabled =
                                                    true
                                                layout_chat_scroll_view.fullScroll(View.FOCUS_DOWN);
                                            }
                                        } else {
                                            showMessage(MSG_FAILED_REQUEST)
                                        }
                                    }, {
                                        showMessage(MSG_FAILED_REQUEST)
                                    })
                                )
                            }, 1000)
                        }

                    },
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            dialog!!.dismiss()
                        }

                    })
            }
        }

        btnSend.setOnClickListener {
            subscription.add(appointmentVM.addChat(
                doctorId!!,
                null,
                etChat.text.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        if (appointmentVM.conversations != null) {
                            adapterMy!!.notifyDataSetChanged()
//                                listConversations.smoothScrollToPosition(appointmentVM.conversations!!.size - 1)
                            setData();
//                                layout_chat_scroll_view.smoothScrollTo(0, chatByDateObjectDataList.size - 1)
                        }
                        etChat.text.clear()
                    } else {
                        showMessage(MSG_FAILED_REQUEST)
                    }
                }, {
                    showMessage(MSG_FAILED_REQUEST)
                })
            )


        }

//        btnAttachment.setOnClickListener { it ->
//            imageDialog = ChooseImageDialog.newInstance { it ->
//                if (it?.toString() == null || it.toString().isEmpty()) {
//                    showAlertOneButton(
//                        R.string.title_failed,
//                        R.string.msg_upload_failed,
//                        R.string.btn_text_ok,
//                        object : DialogInterface.OnClickListener {
//                            override fun onClick(p0: DialogInterface?, p1: Int) {
//                                p0!!.dismiss()
//                                return@onClick
//                            }
//                        })
//                    return@newInstance
//                }
//
//
//                val compress = ImageCompresser(this)
//                val filePath = compress.compressImage(it.toString(), 600F, 800F)
//                if (filePath == null) {
//                    showAlertOneButton(
//                        R.string.title_failed,
//                        R.string.msg_upload_failed,
//                        R.string.btn_text_ok,
//                        object : DialogInterface.OnClickListener {
//                            override fun onClick(p0: DialogInterface?, p1: Int) {
//                                p0!!.dismiss()
//                                return@onClick
//                            }
//                        })
//                    return@newInstance
//                }
//
//
//                val builder = AlertDialog.Builder(this@AyuboChatActivity)
//
//                builder.setMessage("Are you sure you want to Send ?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes") { dialog, id ->
//                        val file = File(filePath)
//                        subscription.add(appointmentVM.addChat(
//                            doctorId!!,
//                            file,
//                            etChat.text.toString()
//                        )
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
//                            .doOnTerminate { progressBar.visibility = View.GONE }
//                            .doOnError { progressBar.visibility = View.GONE }
//                            .subscribe({
//                                if (it.isSuccess) {
//                                    if (appointmentVM.conversations != null) {
//                                        adapterMy!!.notifyDataSetChanged()
////                                                listConversations.smoothScrollToPosition(appointmentVM.conversations!!.size - 1)
//                                        setData();
////                                                layout_chat_scroll_view.smoothScrollTo(0, chatByDateObjectDataList.size - 1)
//                                    }
//                                } else {
//                                    showMessage(MSG_FAILED_REQUEST)
//                                }
//                            }, {
//                                showMessage(MSG_FAILED_REQUEST)
//                            })
//                        )
//                    }
//                    .setNegativeButton("No") { dialog, id ->
//                        // Dismiss the dialog
//                        dialog.dismiss()
//                    }
//                val alert = builder.create()
//                alert.show()
//                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
//                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
//
//
//            }
//            imageDialog!!.show(supportFragmentManager, "chooseimage")
//        }


        btnAttachment.setOnClickListener { it ->
            if (Build.VERSION.SDK_INT >= 21) {
                if (ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_PERMISSION_STORAGE
                    );
                } else {
                    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT);
                    intent.type = "*/*";
                    startActivityForResult(intent, REQUEST_GALLERY);
                }
            } else {
                val intent: Intent = Intent(Intent.ACTION_GET_CONTENT);
                intent.type = "*/*";
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        }

        btnCamera.setOnClickListener { it ->
            imageDialog = ChooseImageDialog.newInstance { it ->
                if (it?.toString() == null || it.toString().isEmpty()) {
                    showAlertOneButton(
                        R.string.title_failed,
                        R.string.msg_upload_failed,
                        R.string.btn_text_ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.dismiss()
                                return@onClick
                            }
                        })
                    return@newInstance
                }


                val compress = ImageCompresser(this)
                val filePath = compress.compressImage(it.toString(), 600F, 800F)
                if (filePath == null) {
                    showAlertOneButton(
                        R.string.title_failed,
                        R.string.msg_upload_failed,
                        R.string.btn_text_ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.dismiss()
                                return@onClick
                            }
                        })
                    return@newInstance
                }


                val builder = AlertDialog.Builder(this@AyuboChatActivity)

                builder.setMessage("Are you sure you want to Send ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        val file = File(filePath)
                        subscription.add(appointmentVM.addChat(
                            doctorId!!,
                            file,
                            etChat.text.toString()
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                            .doOnTerminate { progressBar.visibility = View.GONE }
                            .doOnError { progressBar.visibility = View.GONE }
                            .subscribe({
                                if (it.isSuccess) {
                                    if (appointmentVM.conversations != null) {
                                        adapterMy!!.notifyDataSetChanged()
//                                                listConversations.smoothScrollToPosition(appointmentVM.conversations!!.size - 1)
                                        setData();
//                                                layout_chat_scroll_view.smoothScrollTo(0, chatByDateObjectDataList.size - 1)
                                    }
                                } else {
                                    showMessage(MSG_FAILED_REQUEST)
                                }
                            }, {
                                showMessage(MSG_FAILED_REQUEST)
                            })
                        )
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)


            }
            imageDialog!!.show(supportFragmentManager, "chooseimage")
        }

        etChat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    isTexting = true
                    btnRecord.visibility = View.GONE
                    btnSend.visibility = View.VISIBLE
                } else {
                    isTexting = false
                    btnRecord.visibility = View.VISIBLE
                    btnSend.visibility = View.GONE
                }
            }
        })

        chat_back_btn.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {

                if (intent != null) {

                    try {
                        val uri: Uri? = intent.data;

                        val filePath: String = com.ayubo.life.ayubolife.health.FileUtils.getPath(
                            getApplicationContext(),
                            uri
                        );

                        val builder = AlertDialog.Builder(this@AyuboChatActivity)

                        builder.setMessage("Are you sure you want to Send ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, id ->
                                val file = File(filePath)
                                subscription.add(appointmentVM.addChat(
                                    doctorId!!,
                                    file,
                                    etChat.text.toString()
                                )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                                    .doOnTerminate { progressBar.visibility = View.GONE }
                                    .doOnError { progressBar.visibility = View.GONE }
                                    .subscribe({
                                        if (it.isSuccess) {
                                            if (appointmentVM.conversations != null) {
                                                adapterMy!!.notifyDataSetChanged()
//                                                listConversations.smoothScrollToPosition(appointmentVM.conversations!!.size - 1)
                                                setData();
//                                                layout_chat_scroll_view.smoothScrollTo(0, chatByDateObjectDataList.size - 1)
                                            }
                                        } else {
                                            showMessage(MSG_FAILED_REQUEST)
                                        }
                                    }, {
                                        showMessage(MSG_FAILED_REQUEST)
                                    })
                                )
                            }
                            .setNegativeButton("No") { dialog, id ->
                                // Dismiss the dialog
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }


            }
        }
    }


    fun onClosePreviewClick(view: View?) {
        previewContainer.visibility = View.GONE
        btnClosePreview.visibility = View.GONE
        if (simpleExoPlayerView.isPlaying) {
            simpleExoPlayerView.stopPlayback()
        }
        simpleExoPlayerView.visibility = View.GONE
    }

    fun onReviewLaterClick(view: View) {
//        subscription.add(appointmentVM.updateStatus(appointment!!.appointment_id, view.tag as String)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
//                .doOnTerminate { progressBar.visibility = View.GONE }
//                .doOnError { progressBar.visibility = View.GONE }
//                .subscribe({
//                    if (it.isSuccess) {
//                        finish()
//                    }
//                }, {
//
//                })
//        )
    }

    fun onCompleteClick(view: View) {
//        subscription.add(appointmentVM.updateStatus(appointment!!.appointment_id, view.tag as String)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
//                .doOnTerminate { progressBar.visibility = View.GONE }
//                .doOnError { progressBar.visibility = View.GONE }
//                .subscribe({
//                    if (it.isSuccess) {
//                        setResult(Activity.RESULT_OK)
//                        finish()
//                    }
//                }, {
//
//                })
//        )
    }

    fun onCreatePrescriptionClick(view: View) {
//        SelectPatientActivity.startActivity(this, appointment!!.patient.mobile_number)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (imageDialog != null) {
            imageDialog!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        callPhone.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (previewContainer.visibility == View.VISIBLE) {
            onClosePreviewClick(null)
        } else {
            super.onBackPressed()
        }
    }


}
