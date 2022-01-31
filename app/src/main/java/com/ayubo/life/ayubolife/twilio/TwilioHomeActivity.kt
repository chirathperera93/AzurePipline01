package com.ayubo.life.ayubolife.twilio

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.*
import android.os.*
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.bumptech.glide.Glide
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.koushikdutta.ion.Ion
import com.twilio.video.*
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.android.synthetic.main.activity_report_preview.*
import kotlinx.android.synthetic.main.activity_twilio_home.*
import kotlinx.android.synthetic.main.dialog_choose_image.view.*
import kotlinx.android.synthetic.main.my_badges.*
import java.util.*
import kotlin.concurrent.schedule


class TwilioHomeActivity : AppCompatActivity() {

    private val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
    private val TAG = "VideoActivity------"

    lateinit var mCameraManager: CameraManager;
    lateinit var mCameraId: String;

/*
 * You must provide a Twilio Access Token to connect to the Video service
 */

    private val TWILIO_ACCESS_TOKEN = ""
    private val ACCESS_TOKEN_SERVER = ""
    private val USE_TOKEN_SERVER = false


    /*
     * Access token used to connect. This field will be set either from the console generated token
     * or the request to the token server.
     */
    private lateinit var accessToken: String
    private lateinit var twillioRoomName: String
    private lateinit var name: String
    private lateinit var callerMessage: String
    private lateinit var consultant_pic: String
    private lateinit var consultant_specilization: String
    private lateinit var consultant_name: String

    /*
     * A Room represents communication between a local participant and one or more participants.
     */
    private var room: Room? = null
    private var localParticipant: LocalParticipant? = null

/*
 * AudioCodec and VideoCodec represent the preferred codec for encoding and decoding audio and
 * video.
 */

    private val audioCodec: AudioCodec
        get() {
            val audioCodecName = sharedPreferences.getString(
                TwilioSettingsActivity.PREF_AUDIO_CODEC,
                TwilioSettingsActivity.PREF_AUDIO_CODEC_DEFAULT
            )

            return when (audioCodecName) {
                IsacCodec.NAME -> IsacCodec()
                OpusCodec.NAME -> OpusCodec()
                PcmaCodec.NAME -> PcmaCodec()
                PcmuCodec.NAME -> PcmuCodec()
                G722Codec.NAME -> G722Codec()
                else -> OpusCodec()
            }
        }


    private val videoCodec: VideoCodec
        get() {
            val videoCodecName = sharedPreferences.getString(
                TwilioSettingsActivity.PREF_VIDEO_CODEC,
                TwilioSettingsActivity.PREF_VIDEO_CODEC_DEFAULT
            )

            return when (videoCodecName) {
                Vp8Codec.NAME -> {
                    val simulcast = sharedPreferences.getBoolean(
                        TwilioSettingsActivity.PREF_VP8_SIMULCAST,
                        TwilioSettingsActivity.PREF_VP8_SIMULCAST_DEFAULT
                    )
                    Vp8Codec(simulcast)
                }
                H264Codec.NAME -> H264Codec()
                Vp9Codec.NAME -> Vp9Codec()
                else -> Vp8Codec()
            }
        }

    private val enableAutomaticSubscription: Boolean
        get() {
            return sharedPreferences.getBoolean(
                TwilioSettingsActivity.PREF_ENABLE_AUTOMATIC_SUBSCRIPTION,
                TwilioSettingsActivity.PREF_ENABLE_AUTOMATIC_SUBCRIPTION_DEFAULT
            )
        }

    /*
     * Encoding parameters represent the sender side bandwidth constraints.
     */
    private val encodingParameters: EncodingParameters
        get() {
            val defaultMaxAudioBitrate =
                TwilioSettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT
            val defaultMaxVideoBitrate =
                TwilioSettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT
            val maxAudioBitrate = Integer.parseInt(
                sharedPreferences.getString(
                    TwilioSettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE,
                    defaultMaxAudioBitrate
                ) ?: defaultMaxAudioBitrate
            )
            val maxVideoBitrate = Integer.parseInt(
                sharedPreferences.getString(
                    TwilioSettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE,
                    defaultMaxVideoBitrate
                ) ?: defaultMaxVideoBitrate
            )

            return EncodingParameters(maxAudioBitrate, maxVideoBitrate)
        }

/*
 * Room events listener
 */


    private val roomListener = object : Room.Listener {
        override fun onConnected(room: Room) {
            localParticipant = room.localParticipant
            videoStatusTextView.text = "Connected to ${room.name}"
            title = room.name

            // Only one participant is supported
            room.remoteParticipants?.firstOrNull()?.let { addRemoteParticipant(it) }
        }

        override fun onReconnected(room: Room) {
            videoStatusTextView.text = "Connected to ${room.name}"
            // reconnectingProgressBar.visibility = View.GONE;
        }

        override fun onReconnecting(room: Room, twilioException: TwilioException) {
            videoStatusTextView.text = "Reconnecting to ${room.name}"
            //reconnectingProgressBar.visibility = View.VISIBLE;
        }

        override fun onConnectFailure(room: Room, e: TwilioException) {
            videoStatusTextView.text = "Failed to connect"
            Toast.makeText(this@TwilioHomeActivity, e.toString(), Toast.LENGTH_LONG).show()
            videoStatusTextView.visibility = View.VISIBLE
            configureAudio(false)
            initializeUI()
        }

        override fun onDisconnected(room: Room, e: TwilioException?) {
            localParticipant = null
            videoStatusTextView.text = "Disconnected from ${room.name}"
            // reconnectingProgressBar.visibility = View.GONE
            this@TwilioHomeActivity.room = null
            // Only reinitialize the UI if disconnect was not called from onDestroy()
            if (!disconnectedFromOnDestroy) {
                configureAudio(false)
                initializeUI()
                moveLocalVideoToPrimaryView()
            }

            finish()
        }

        override fun onParticipantConnected(room: Room, participant: RemoteParticipant) {
            addRemoteParticipant(participant)
        }

        override fun onParticipantDisconnected(room: Room, participant: RemoteParticipant) {
            removeRemoteParticipant(participant)
        }

        override fun onRecordingStarted(room: Room) {
            /*
             * Indicates when media shared to a Room is being recorded. Note that
             * recording is only available in our Group Rooms developer preview.
             */
            Log.d(TAG, "onRecordingStarted")
        }

        override fun onRecordingStopped(room: Room) {
            /*
             * Indicates when media shared to a Room is no longer being recorded. Note that
             * recording is only available in our Group Rooms developer preview.
             */
            Log.d(TAG, "onRecordingStopped")
        }
    }

    /*
     * RemoteParticipant events listener
     */
    private val participantListener = object : RemoteParticipant.Listener {
        override fun onAudioTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            Log.i(
                TAG, "onAudioTrackPublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteAudioTrackPublication: sid=${remoteAudioTrackPublication.trackSid}, " +
                        "enabled=${remoteAudioTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteAudioTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteAudioTrackPublication.trackName}]"
            )
            videoStatusTextView.text = "onAudioTrackAdded"
        }

        override fun onAudioTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            Log.i(
                TAG, "onAudioTrackUnpublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteAudioTrackPublication: sid=${remoteAudioTrackPublication.trackSid}, " +
                        "enabled=${remoteAudioTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteAudioTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteAudioTrackPublication.trackName}]"
            )
            videoStatusTextView.text = "onAudioTrackRemoved"
        }

        override fun onDataTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication
        ) {
            Log.i(
                TAG, "onDataTrackPublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteDataTrackPublication: sid=${remoteDataTrackPublication.trackSid}, " +
                        "enabled=${remoteDataTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteDataTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteDataTrackPublication.trackName}]"
            )
            videoStatusTextView.text = "onDataTrackPublished"
        }

        override fun onDataTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication
        ) {
            Log.i(
                TAG, "onDataTrackUnpublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteDataTrackPublication: sid=${remoteDataTrackPublication.trackSid}, " +
                        "enabled=${remoteDataTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteDataTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteDataTrackPublication.trackName}]"
            )
            videoStatusTextView.text = "onDataTrackUnpublished"
        }

        override fun onVideoTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            Log.i(
                TAG, "onVideoTrackPublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteVideoTrackPublication: sid=${remoteVideoTrackPublication.trackSid}, " +
                        "enabled=${remoteVideoTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteVideoTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteVideoTrackPublication.trackName}]"
            )

            txt_name.text = "Calling with $name..."
        }

        override fun onVideoTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            Log.i(
                TAG, "onVideoTrackUnpublished: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteVideoTrackPublication: sid=${remoteVideoTrackPublication.trackSid}, " +
                        "enabled=${remoteVideoTrackPublication.isTrackEnabled}, " +
                        "subscribed=${remoteVideoTrackPublication.isTrackSubscribed}, " +
                        "name=${remoteVideoTrackPublication.trackName}]"
            )
            videoStatusTextView.text = "onVideoTrackUnpublished"
        }

        override fun onAudioTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            remoteAudioTrack: RemoteAudioTrack
        ) {
            Log.i(
                TAG, "onAudioTrackSubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteAudioTrack: enabled=${remoteAudioTrack.isEnabled}, " +
                        "playbackEnabled=${remoteAudioTrack.isPlaybackEnabled}, " +
                        "name=${remoteAudioTrack.name}]"
            )
            videoStatusTextView.text = "onAudioTrackSubscribed"
        }

        override fun onAudioTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            remoteAudioTrack: RemoteAudioTrack
        ) {
            Log.i(
                TAG, "onAudioTrackUnsubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteAudioTrack: enabled=${remoteAudioTrack.isEnabled}, " +
                        "playbackEnabled=${remoteAudioTrack.isPlaybackEnabled}, " +
                        "name=${remoteAudioTrack.name}]"
            )
            videoStatusTextView.text = "onAudioTrackUnsubscribed"
        }

        override fun onAudioTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            twilioException: TwilioException
        ) {
            Log.i(
                TAG, "onAudioTrackSubscriptionFailed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteAudioTrackPublication: sid=${remoteAudioTrackPublication.trackSid}, " +
                        "name=${remoteAudioTrackPublication.trackName}]" +
                        "[TwilioException: code=${twilioException.code}, " +
                        "message=${twilioException.message}]"
            )
            videoStatusTextView.text = "onAudioTrackSubscriptionFailed"
        }

        override fun onDataTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            remoteDataTrack: RemoteDataTrack
        ) {
            Log.i(
                TAG, "onDataTrackSubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteDataTrack: enabled=${remoteDataTrack.isEnabled}, " +
                        "name=${remoteDataTrack.name}]"
            )
            videoStatusTextView.text = "onDataTrackSubscribed"
        }

        override fun onDataTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            remoteDataTrack: RemoteDataTrack
        ) {
            Log.i(
                TAG, "onDataTrackUnsubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteDataTrack: enabled=${remoteDataTrack.isEnabled}, " +
                        "name=${remoteDataTrack.name}]"
            )
            videoStatusTextView.text = "onDataTrackUnsubscribed"
        }

        override fun onDataTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            twilioException: TwilioException
        ) {
            Log.i(
                TAG, "onDataTrackSubscriptionFailed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteDataTrackPublication: sid=${remoteDataTrackPublication.trackSid}, " +
                        "name=${remoteDataTrackPublication.trackName}]" +
                        "[TwilioException: code=${twilioException.code}, " +
                        "message=${twilioException.message}]"
            )
            videoStatusTextView.text = "onDataTrackSubscriptionFailed"
        }

        override fun onVideoTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            remoteVideoTrack: RemoteVideoTrack
        ) {
            Log.i(
                TAG, "onVideoTrackSubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteVideoTrack: enabled=${remoteVideoTrack.isEnabled}, " +
                        "name=${remoteVideoTrack.name}]"
            )

            txt_name.text = "Calling with $name..."
            //   videoStatusTextView.text = "onVideoTrackSubscribed"
            addRemoteParticipantVideo(remoteVideoTrack)
        }

        override fun onVideoTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            remoteVideoTrack: RemoteVideoTrack
        ) {
            Log.i(
                TAG, "onVideoTrackUnsubscribed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteVideoTrack: enabled=${remoteVideoTrack.isEnabled}, " +
                        "name=${remoteVideoTrack.name}]"
            )
            videoStatusTextView.text = "onVideoTrackUnsubscribed"
            removeParticipantVideo(remoteVideoTrack)
        }

        override fun onVideoTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            twilioException: TwilioException
        ) {
            Log.i(
                TAG, "onVideoTrackSubscriptionFailed: " +
                        "[RemoteParticipant: identity=${remoteParticipant.identity}], " +
                        "[RemoteVideoTrackPublication: sid=${remoteVideoTrackPublication.trackSid}, " +
                        "name=${remoteVideoTrackPublication.trackName}]" +
                        "[TwilioException: code=${twilioException.code}, " +
                        "message=${twilioException.message}]"
            )
            videoStatusTextView.text = "onVideoTrackSubscriptionFailed"
            null
        }

        override fun onAudioTrackEnabled(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
        }

        override fun onVideoTrackEnabled(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            primaryVideoView.visibility = View.VISIBLE
            user_camera_off_image.visibility = View.GONE

        }

        override fun onVideoTrackDisabled(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            Glide.with(baseContext).load(consultant_pic).into(user_camera_off_image)
            primaryVideoView.visibility = View.GONE
            user_camera_off_image.visibility = View.VISIBLE
        }

        override fun onAudioTrackDisabled(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
        }
    }

    private var localAudioTrack: LocalAudioTrack? = null
    private var localVideoTrack: LocalVideoTrack? = null
    private var alertDialog: AlertDialog? = null
    private val cameraCapturerCompat by lazy {
        CameraCapturerCompat(this, getAvailableCameraSource())
    }
    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this@TwilioHomeActivity)
    }
    private val audioManager by lazy {
        this@TwilioHomeActivity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    lateinit var prefManager: PrefManager

    //    lateinit var ringtone: MediaPlayer
    private var participantIdentity: String? = null

    private var previousAudioMode = 0
    private var previousMicrophoneMute = false
    private lateinit var localVideoView: VideoRenderer
    private var disconnectedFromOnDestroy = false
    private var isSpeakerPhoneEnabled = true


    var CLICK_DRAG_TOLERANCE: Float =
        10F; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    var downRawX: Float = 0F
    var downRawY: Float = 0F;
    var dX: Float = 0F
    var directionY: Float = 0F;

    var set: AnimatorSet? = null;
    var isCamePosition: Boolean = false;


    // lateinit var ringtone:Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twilio_home)
        println("========TwilioHomeActivity======" + "true4")
        //FOR VIDEO CALL ....
        println("========TwilioHomeActivity=======" + "true")
        prefManager = PrefManager(this)

        time_count_down.stop();
        val notificationManager: NotificationManager = prefManager.notificationMgr;
        if (notificationManager !== null) {
            notificationManager.cancelAll()
        }

        readExtras()

//        val vibrator =
//            this@TwilioHomeActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        if (Build.VERSION.SDK_INT >= 26) {
//            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
//        } else {
//            vibrator.vibrate(1000)
//        }

//    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//    ringtone = RingtoneManager.getRingtone(this@TwilioHomeActivity,uri)
//    ringtone.play()

        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ayubolife:MyWakelockTag")
        wakeLock.acquire(60 * 1000 * 2)

//        ringtone = MediaPlayer.create(this, R.raw.beep)
//        ringtone.start()
//        ringtone.isLooping = true

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        /*
         * Set local video view to primary view
         */
        localVideoView = primaryVideoView

        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        volumeControlStream = AudioManager.STREAM_VOICE_CALL

        /*
         * Needed for setting/abandoning audio focus during call
         */
        audioManager.isSpeakerphoneOn = true

        /*
         * Set access token
         */
        setAccessToken()

        /*
         * Request permissions.
         */
        requestPermissionForCameraAndMicrophone()

        /*
         * Set the initial state of the UI
         */
        initializeUI()


//    if(IS_PRO_APP){
//

        val images = arrayOf(img4, img3, img2, img1)
        val anims = ArrayList<ObjectAnimator>(images.size * 2)

        try {
            Handler().postDelayed(Runnable {
                for (v in images) anims.add(
                    ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f).setDuration(100)
                )


                for (v in images) anims.add(
                    ObjectAnimator.ofFloat(v, View.ALPHA, 1f, 0f).setDuration(100)
                )

                set = AnimatorSet()


                set!!.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) =
                        set!!.start()
                })

                set!!.startDelay = 800

                for (i in 0 until anims.size - 1) set!!.play(anims[i])
                    .before(anims[i + 1])

                set!!.start()

            }, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (Constants.type == Constants.Type.LIFEPLUS) {
            videoStatusTextView.setText("Life+ video call")
        } else {
            videoStatusTextView.setText("AyuboLife video call")
        }

//
//    }


        connectActionFab.setOnTouchListener(object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                val layoutParams: ViewGroup.MarginLayoutParams =
                    view!!.getLayoutParams() as ViewGroup.MarginLayoutParams;
                when (motionEvent?.action) {


                    MotionEvent.ACTION_UP -> {
                        val upRawX: Float = motionEvent.getRawX();
                        val upRawY: Float = motionEvent.getRawY();

                        val upDX: Float = upRawX - downRawX;
                        val upDY: Float = upRawY - downRawY;

                        if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                            return true;
                        } else { // A drag
                            return true; // Consumed
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
//                        val viewWidth: Int = view.getWidth();
                        val viewHeight: Int = view.getHeight();

                        val viewParent: View = view.getParent() as (View);
//                        val parentWidth: Int = viewParent.getWidth();
                        val parentHeight: Int = viewParent.getHeight();

//                        var newX: Float = motionEvent.getRawX() + dX;
//                        newX = Math.max(
//                            layoutParams.leftMargin.toFloat(),
//                            newX
//                        );
//                        newX = Math.min(
//                            parentWidth.toFloat() - viewWidth.toFloat() - layoutParams.rightMargin.toFloat(),
//                            newX
//                        );

                        System.out.println("directionY directionY directionY ******************   =    " + directionY)

                        var newY: Float = motionEvent.getRawY() + directionY;
                        newY = Math.max(
                            layoutParams.topMargin.toFloat(),
                            newY
                        );

                        newY = Math.min(
                            parentHeight.toFloat() - viewHeight.toFloat() - layoutParams.bottomMargin.toFloat(),
                            newY
                        );

                        System.out.println("New yyyyyyyyyyyyyyyyyyyy   =    " + newY)
                        newY = 1500F

                        if (!isCamePosition && newY == 1500F) {
                            view.cancelDragAndDrop();
                            isCamePosition = true
                            view.animate()
                                .y(newY)
                                .setDuration(500)
                                .start();
                            Timer("SettingUp", false).schedule(500) {
                                connectTheCall();

                            }
                            return false
                        } else {
                            return true; // Consumed
                        }


                    }

                    MotionEvent.ACTION_DOWN -> {
                        downRawY = motionEvent.getRawY();
                        directionY = view.getY() - downRawY;

                        return true; // Consumed
                    }
                }

                return view.onTouchEvent(motionEvent) ?: true
            }
        })

    }

    fun connectTheCall() {

        this@TwilioHomeActivity.runOnUiThread(java.lang.Runnable {
            set!!.cancel()
            connectActionFab.visibility = View.GONE
            call_cut_btn.visibility = View.GONE
            call_text_btn.visibility = View.GONE
            username.visibility = View.GONE
            ring_slider.visibility = View.GONE

            disconnectCall.visibility = View.VISIBLE
            flash_light_btn.visibility = View.VISIBLE
            camera_switch_btn.visibility = View.VISIBLE
            voice_mute_unmute_btn.visibility = View.VISIBLE
            video_turn_off_on_btn.visibility = View.VISIBLE
            caller_detail.visibility = View.VISIBLE

            time_count_down.visibility = View.VISIBLE
            time_count_down.setBase(SystemClock.elapsedRealtime());
            time_count_down.start();



            if (prefManager.ringtone != null) {
                val savedRingtone: Ringtone = prefManager.ringtone;
                if (savedRingtone !== null) {
                    savedRingtone.stop();
                    prefManager.setRingingTone(null)
                }
            }


            if (prefManager.vibrator != null) {
                val vibrator: Vibrator = prefManager.vibrator;
                if (vibrator !== null) {
                    vibrator.cancel();
                    prefManager.setVibrator(null)
                }
            }


//            if (ringtone.isPlaying) {
//                ringtone.stop()
//            }

            connectToRoom(twillioRoomName)
        })


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            var cameraAndMicPermissionGranted = true

            for (grantResult in grantResults) {
                cameraAndMicPermissionGranted = cameraAndMicPermissionGranted and
                        (grantResult == PackageManager.PERMISSION_GRANTED)
            }

            if (cameraAndMicPermissionGranted) {
                setupPermissions()

            } else {
                Toast.makeText(
                    this,
                    "permissions_needed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        /*
         * If the local video track was released when the app was put in the background, recreate.
         */
        localVideoTrack = if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
            LocalVideoTrack.create(
                this,
                true,
                cameraCapturerCompat.videoCapturer
            )
        } else {
            localVideoTrack
        }



        localVideoTrack?.addRenderer(localVideoView)

        /*
         * If connected to a Room then share the local video track.
         */
        localVideoTrack?.let { localParticipant?.publishTrack(it) }

        /*
         * Update encoding parameters if they have changed.
         */
        localParticipant?.setEncodingParameters(encodingParameters)

        /*
         * Route audio through cached value.
         */
        audioManager.isSpeakerphoneOn = isSpeakerPhoneEnabled

        /*
         * Update reconnecting UI
         */


//    room?.let {
//       // reconnectingProgressBar.visibility = if (it.state != Room.State.RECONNECTING)
//            View.GONE else
//            View.VISIBLE
//        videoStatusTextView.text = "Connected to ${it.name}"
//    }
    }

    private fun setupPermissions() {


        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
        } else {
            createAudioAndVideoTracks()
        }
    }

    override fun onPause() {

        localVideoTrack?.let { localParticipant?.unpublishTrack(it) }
        localVideoTrack?.release()
        localVideoTrack = null
        super.onPause()
    }

    override fun onDestroy() {
        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        room?.disconnect()
        disconnectedFromOnDestroy = true


//        if (ringtone.isPlaying) {
//            ringtone.stop()
//        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        localAudioTrack?.release()
        localVideoTrack?.release()

        super.onDestroy()
    }

//override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    val inflater = menuInflater
//    inflater.inflate(R.menu.menu, menu)
//    return true
//}

//override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    when (item.itemId) {
//        R.id.menu_settings -> startActivity(Intent(this, TwilioSettingsActivity::class.java))
//        R.id.speaker_menu_item -> if (audioManager.isSpeakerphoneOn) {
//            audioManager.isSpeakerphoneOn = false
//            item.setIcon(R.drawable.ic_phonelink_ring_white_24dp)
//            isSpeakerPhoneEnabled = false
//        } else {
//            audioManager.isSpeakerphoneOn = true
//            item.setIcon(R.drawable.ic_volume_up_white_24dp)
//            isSpeakerPhoneEnabled = true
//        }
//    }
//    return true
//}

    private fun requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            Toast.makeText(
                this,
                "permissions_needed",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_MIC_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkPermissionForCameraAndMicrophone(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED
    }

    private fun createAudioAndVideoTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true)

        // Share your camera
        localVideoTrack = LocalVideoTrack.create(
            this,
            true,
            cameraCapturerCompat.videoCapturer
        )
    }

    private fun getAvailableCameraSource(): CameraCapturer.CameraSource {
        return if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA))
            CameraCapturer.CameraSource.FRONT_CAMERA
        else
            CameraCapturer.CameraSource.BACK_CAMERA
    }

    private fun setAccessToken() {
        if (!USE_TOKEN_SERVER) {
            /*
             * OPTION 1 - Generate an access token from the getting started portal
             * https://www.twilio.com/console/video/dev-tools/testing-tools and add
             * the variable TWILIO_ACCESS_TOKEN setting it equal to the access token
             * string in your local.properties file.
             */
            //  this.accessToken = TWILIO_ACCESS_TOKEN
        } else {
            /*
             * OPTION 2 - Retrieve an access token from your own web app.
             * Add the variable ACCESS_TOKEN_SERVER assigning it to the url of your
             * token server and the variable USE_TOKEN_SERVER=true to your
             * local.properties file.
             */
            retrieveAccessTokenfromServer()
        }
    }

    private fun connectToRoom(roomName: String) {

//        if (ringtone.isPlaying) {
//            ringtone.stop()
//        }

        configureAudio(true)
        val connectOptionsBuilder = ConnectOptions.Builder(accessToken)
            .roomName(roomName)

        /*
         * Add local audio track to connect options to share with participants.
         */

        localAudioTrack?.let {
            connectOptionsBuilder.audioTracks(listOf(it))
        }

        /*
         * Add local video track to connect options to share with participants.
         */
        localVideoTrack?.let {
            connectOptionsBuilder.videoTracks(listOf(it))
        }

        /*
         * Set the preferred audio and video codec for media.
         */
        connectOptionsBuilder.preferAudioCodecs(listOf(audioCodec))
        connectOptionsBuilder.preferVideoCodecs(listOf(videoCodec))

        /*
         * Set the sender side encoding parameters.
         */
        connectOptionsBuilder.encodingParameters(encodingParameters)

        /*
         * Toggles automatic track subscription. If set to false, the LocalParticipant will receive
         * notifications of track publish events, but will not automatically subscribe to them. If
         * set to true, the LocalParticipant will automatically subscribe to tracks as they are
         * published. If unset, the default is true. Note: This feature is only available for Group
         * Rooms. Toggling the flag in a P2P room does not modify subscription behavior.
         */
        connectOptionsBuilder.enableAutomaticSubscription(enableAutomaticSubscription)

        room = Video.connect(this, connectOptionsBuilder.build(), roomListener)

//        setDisconnectAction()
    }

    /*
     * The initial state when there is no active room.
     */
//    companion object {
//        fun startActivity(context: Context?, tokn:String, room:String,calltype:String) {
//            val intent = Intent(context, TwilioHomeActivity::class.java)
//            intent.putExtra(TWILIO_ACCESSTOKEN, tokn)
//            intent.putExtra(TWILIO_ROOM_NAME, room)
//            intent.putExtra(TWILIO_CALL_TYPE, calltype)
//            context!!.startActivity(intent)
//        }
//    }

    private fun readExtras() {

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("tw_token")) {

            accessToken = prefManager.vRoomToken

            consultant_pic = prefManager.vConsultantPic;
            consultant_specilization = prefManager.vConsultantSpecilization;
            consultant_name = prefManager.vTitle;

            // accessToken = bundle.getSerializable("tw_token") as String
            twillioRoomName = bundle.getSerializable("tw_name") as String
            callerMessage = bundle.getSerializable("twillio_caller_name") as String
            name = bundle.getSerializable("caller_id") as String
            println("========accessToken=======$accessToken")
            Log.d("twillioRoomName...", twillioRoomName)
            Log.d("accessToken...", accessToken)

            Log.d("twillio_caller_name...", name)
            //     Toast.makeText(this@TwilioHomeActivity,accessToken,Toast.LENGTH_LONG).show()
        } else {
            println("========TwilioHomeActivity=======" + "readExtras  null")
        }

    }


    @SuppressLint("SetTextI18n")
    private fun initializeUI() {


        Glide.with(this).load(consultant_pic).into(doctor_image)
        txt_name.text = consultant_name

        doctor_name.text = consultant_name

        speciality.setText(consultant_specilization)

//    connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
//        R.drawable.pdf_icon_for_reports))
        // connectActionFab.setBackgroundResource(R.drawable.pdf_icon_for_reports)
//        connectActionFab.setOnClickListener(connectActionClickListener())

        disconnectCall.setOnClickListener {
//            if (ringtone.isPlaying) {
//                ringtone.stop()
//            }

            // room?.disconnect()


//            val builder = android.app.AlertDialog.Builder(this@TwilioHomeActivity)
//
//            builder.setMessage(Html.fromHtml("<font color='#000000'>Are you sure you want to cut the call ?</font>"))
//                .setCancelable(false)
//                .setPositiveButton("Yes") { dialog, id ->
//
//                    time_count_down.stop();
//                    initializeUI()
//
//                    disconnect()
//                }
//                .setNegativeButton("No") { dialog, id ->
//                    // Dismiss the dialog
//                    dialog.dismiss()
//                }
//            val alert = builder.create()
//            alert.show()
//            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
//            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
//
//            alert.window!!.setBackgroundDrawableResource(R.drawable.text_view_rouded_corner)


            runOnUiThread {
                val bottomSheetDialog: BottomSheetDialog =
                    BottomSheetDialog(this@TwilioHomeActivity);
                bottomSheetDialog.setContentView(R.layout.om_delete_address_bottom_sheet);
                bottomSheetDialog.show();


                val address: TextView? =
                    bottomSheetDialog.findViewById<TextView>(R.id.address)
                val deleteAddress: TextView? =
                    bottomSheetDialog.findViewById<TextView>(R.id.delete_address)
                val deleteBtn: Button? =
                    bottomSheetDialog.findViewById<Button>(R.id.delete_btn)
                val notNow: TextView? =
                    bottomSheetDialog.findViewById<TextView>(R.id.not_now)


                address?.text = "Leave the Call";
                deleteAddress?.text = "Are you sure you want leave ?"

                deleteBtn?.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    time_count_down.stop();
                    initializeUI()
                    disconnect()

                }

                notNow?.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }


//                val builder: BottomSheet.Builder = BottomSheet.Builder(this@TwilioHomeActivity)
//
//
//                builder.setTitle("Title")?.setView(R.layout.om_delete_address_bottom_sheet)
//                    ?.setFullWidth(false)?.show();
//
//                val address: TextView? =
//                    builder.view?.findViewById<TextView>(R.id.address)
//                val deleteAddress: TextView? =
//                    builder.view?.findViewById<TextView>(R.id.delete_address)
//                val deleteBtn: Button? =
//                    builder.view?.findViewById<Button>(R.id.delete_btn)
//                val notNow: TextView? =
//                    builder.view?.findViewById<TextView>(R.id.not_now)
//
//
//                address?.text = "Leave the Call";
//                deleteAddress?.text = "Are you sure you want leave ?"
//
//                deleteBtn?.setOnClickListener {
//                    builder.dismiss()
//                    time_count_down.stop();
//                    initializeUI()
//                    disconnect()
//
//                }
//
//                notNow?.setOnClickListener {
//                    builder.dismiss()
//                }
            }


        }

        disconnectCallNew.setOnClickListener {

            val savedRingtone: Ringtone = prefManager.ringtone;
            val vibrator: Vibrator = prefManager.vibrator;
//            val notificationManager: NotificationManager = prefManager.notificationMgr;


            time_count_down.stop();
            if (savedRingtone !== null) {
                savedRingtone.stop();
                prefManager.setRingingTone(null)
            }

            if (vibrator !== null) {
                vibrator.cancel();
                prefManager.setVibrator(null)
            }


//            if (notificationManager !== null) {
//                notificationManager.cancelAll()
//            }

//            if (ringtone.isPlaying) {
//                ringtone.stop()
//            }

            // room?.disconnect()

            initializeUI()

            disconnect()

        }


//    switchCameraActionFab.show()
        //switchCameraActionFab.setOnClickListener(switchCameraClickListener())


//    localVideoActionFab.show()
        // localVideoActionFab.setOnClickListener(localVideoClickListener())
//    muteActionFab.show()
//        muteActionOffFab.setOnClickListener(muteClickListener())

        muteActionOffFab.setOnClickListener {
            muteActionOffFab.visibility = View.GONE
            muteActionOnFab.visibility = View.VISIBLE
            localAudioTrack!!.enable(false);

        }

        muteActionOnFab.setOnClickListener {
            muteActionOnFab.visibility = View.GONE
            muteActionOffFab.visibility = View.VISIBLE
            localAudioTrack!!.enable(true);

        }


        cameraSwitchActionFab.setOnClickListener(switchCameraClickListener())

//        turnOffCameraActionFab.setOnClickListener(localVideoClickListener())


        turnOffCameraActionFab.setOnClickListener {
            turnOffCameraActionFab.visibility = View.GONE
            turnOnCameraActionFab.visibility = View.VISIBLE
//            thumbnailVideoView.visibility = View.GONE
            localVideoTrack!!.enable(false);

        }

        turnOnCameraActionFab.setOnClickListener {
            turnOnCameraActionFab.visibility = View.GONE
            turnOffCameraActionFab.visibility = View.VISIBLE
//            thumbnailVideoView.visibility = View.VISIBLE
            localVideoTrack!!.enable(true);

        }


        // get the camera
//        getCamera();

        flashLightOnActionFab.setOnClickListener {
//            flashLightOnActionFab.visibility = View.GONE
//            flashLightOffActionFab.visibility = View.VISIBLE
//
//            turnOnFlash();

        }

        flashLightOffActionFab.setOnClickListener {
//            flashLightOffActionFab.visibility = View.GONE
//            flashLightOnActionFab.visibility = View.VISIBLE
//            turnOffFlash();

        }


    }


    // Turning On flash
    fun turnOnFlash() {

        var camManager: CameraManager? = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                camManager =
                    baseContext.getSystemService(Context.CAMERA_SERVICE) as (CameraManager);
                var cameraId: String? = null;
                if (camManager != null) {
                    cameraId = camManager.cameraIdList[0];
                    camManager.setTorchMode(cameraId, true);
                }
            } catch (e: CameraAccessException) {
                Log.e(TAG, e.toString());
            }
        } else {
            var mCamera: Camera? = null;
            val parameters: Camera.Parameters;
            mCamera = Camera.open();
            parameters = mCamera.parameters;
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH;
            mCamera.parameters = parameters;
            mCamera.startPreview();
        }

    }

    // Turning Off flash
    fun turnOffFlash() {
        var camManager: CameraManager? = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                camManager =
                    baseContext.getSystemService(Context.CAMERA_SERVICE) as (CameraManager);
                var cameraId: String? = null;
                if (camManager != null) {
                    cameraId = camManager.cameraIdList[0]; // Usually front camera is at 0 position.
                    camManager.setTorchMode(cameraId, false);
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace();
            }
        } else {
            var mCamera: Camera? = null;
            val parameters: Camera.Parameters;
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.stopPreview();
        }


    }


    /*
     * The actions performed during disconnect.
     */
//    private fun setDisconnectAction() {
//
//        disconnectCall.visibility = View.GONE
//
//        connectActionFab.setBackgroundResource(R.drawable.cut_the_call)
//
//        connectActionFab.setOnClickListener(disconnectClickListener())
//
//    }

    private fun disconnect() {

        room?.disconnect()

        localParticipant = null

        this@TwilioHomeActivity.room = null

        localVideoTrack?.release()

//        val buttonIntent = Intent(baseContext, NewHomeWithSideMenuActivity::class.java)
//        val buttonIntent = Intent(baseContext, LifePlusProgramActivity::class.java)
//        val buttonIntent = Intent(baseContext, ProfileNew::class.java)
//        startActivity(buttonIntent)


        finish()


    }
/*
 * Creates an connect UI dialog
 */

    private fun showConnectDialog() {
        val roomEditText = EditText(this)
        alertDialog = createConnectDialog(
            roomEditText,
            connectClickListener(roomEditText), cancelConnectDialogClickListener(), this
        )
        alertDialog!!.show()
    }

    /*
     * Called when participant joins the room
     */
    private fun addRemoteParticipant(remoteParticipant: RemoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView.visibility == View.VISIBLE) {
//        Snackbar.make(connectActionFab,
//            "Multiple participants are not currently support in this UI",
//            Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
            return
        }
        participantIdentity = remoteParticipant.identity
        videoStatusTextView.text = "Participant $participantIdentity joined"

        /*
         * Add participant renderer
         */
        remoteParticipant.remoteVideoTracks.firstOrNull()?.let { remoteVideoTrackPublication ->
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                remoteVideoTrackPublication.remoteVideoTrack?.let { addRemoteParticipantVideo(it) }
            }
        }

        /*
         * Start listening for participant events
         */
        remoteParticipant.setListener(participantListener)
    }

    /*
     * Set primary view as renderer for participant video track
     */
    private fun addRemoteParticipantVideo(videoTrack: VideoTrack) {
        moveLocalVideoToThumbnailView()
        primaryVideoView.mirror = false
        videoTrack.addRenderer(primaryVideoView)
    }

    private fun moveLocalVideoToThumbnailView() {
        if (thumbnailVideoView.visibility == View.GONE) {
            thumbnailVideoView.visibility = View.VISIBLE
            with(localVideoTrack) {
                this?.removeRenderer(primaryVideoView)
                this?.addRenderer(thumbnailVideoView)
            }
            localVideoView = thumbnailVideoView
            thumbnailVideoView.mirror = cameraCapturerCompat.cameraSource ==
                    CameraCapturer.CameraSource.FRONT_CAMERA
        }
    }

    /*
     * Called when participant leaves the room
     */
    private fun removeRemoteParticipant(remoteParticipant: RemoteParticipant) {
        videoStatusTextView.text = "Participant $remoteParticipant.identity left."
        if (remoteParticipant.identity != participantIdentity) {
            return
        }

        /*
         * Remove participant renderer
         */
        remoteParticipant.remoteVideoTracks.firstOrNull()?.let { remoteVideoTrackPublication ->
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                remoteVideoTrackPublication.remoteVideoTrack?.let { removeParticipantVideo(it) }
            }
        }
        // moveLocalVideoToPrimaryView()

        finish()
    }

    private fun removeParticipantVideo(videoTrack: VideoTrack) {
        videoTrack.removeRenderer(primaryVideoView)
    }

    private fun moveLocalVideoToPrimaryView() {
        if (thumbnailVideoView.visibility == View.VISIBLE) {
            thumbnailVideoView.visibility = View.GONE
            with(localVideoTrack) {
                this?.removeRenderer(thumbnailVideoView)
                this?.addRenderer(primaryVideoView)
            }
            localVideoView = primaryVideoView
            primaryVideoView.mirror = cameraCapturerCompat.cameraSource ==
                    CameraCapturer.CameraSource.FRONT_CAMERA
        }
    }

    private fun connectClickListener(roomEditText: EditText): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, _ ->
            /*
             * Connect to room
             */
            connectToRoom(twillioRoomName)
        }
    }

    private fun disconnectClickListener(): View.OnClickListener {
        return View.OnClickListener {
            /*
             * Disconnect from room
             */
//            if (ringtone.isPlaying) {
//                ringtone.stop()
//            }

            // room?.disconnect()

            initializeUI()

            disconnect()
        }
    }


    private fun connectActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            //  showConnectDialog()

            time_count_down.visibility = View.VISIBLE
            time_count_down.start();

            val savedRingtone: Ringtone = prefManager.ringtone;
            val vibrator: Vibrator = prefManager.vibrator;



            if (savedRingtone !== null) {
                savedRingtone.stop();
                prefManager.setRingingTone(null)
            }

            if (vibrator !== null) {
                vibrator.cancel();
                prefManager.setVibrator(null)
            }




            connectActionFab.visibility = View.GONE
            call_cut_btn.visibility = View.GONE
            call_text_btn.visibility = View.GONE
            username.visibility = View.GONE
            ring_slider.visibility = View.GONE



            disconnectCall.visibility = View.VISIBLE
            flash_light_btn.visibility = View.VISIBLE
            camera_switch_btn.visibility = View.VISIBLE
            voice_mute_unmute_btn.visibility = View.VISIBLE
            video_turn_off_on_btn.visibility = View.VISIBLE
            caller_detail.visibility = View.VISIBLE

//            if (ringtone.isPlaying) {
//                ringtone.stop()
//            }

            connectToRoom(twillioRoomName)
        }
    }

    private fun cancelConnectDialogClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, _ ->
            initializeUI()
            alertDialog!!.dismiss()
        }
    }

    private fun switchCameraClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val cameraSource = cameraCapturerCompat.cameraSource
            cameraCapturerCompat.switchCamera()
            if (thumbnailVideoView.visibility == View.VISIBLE) {
                thumbnailVideoView.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
            } else {
                primaryVideoView.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
            }
        }
    }

    private fun localVideoClickListener(): View.OnClickListener {
        return View.OnClickListener {
            /*
             * Enable/disable the local video track
             */
            localVideoTrack?.let {
                val enable = !it.isEnabled
                it.enable(enable)
                val icon: Int
                if (enable) {
                    icon = R.drawable.turn_on_camera
                } else {
                    icon = R.drawable.turn_off_camera
                }
                turnOffCameraActionFab.setBackgroundResource(0);
                turnOffCameraActionFab.layoutParams.height = 200
                turnOffCameraActionFab.layoutParams.width = 200
                turnOffCameraActionFab.requestLayout();
                turnOffCameraActionFab.setImageDrawable(
                    ContextCompat.getDrawable(this@TwilioHomeActivity, icon)
                )


            }
        }
    }

    private fun muteClickListener(): View.OnClickListener {
        return View.OnClickListener {
            /*
             * Enable/disable the local audio track. The results of this operation are
             * signaled to other Participants in the same Room. When an audio track is
             * disabled, the audio is muted.
             */
            localAudioTrack?.let {
                val enable = !it.isEnabled
                it.enable(enable)
                val icon = if (enable)
                    R.drawable.mute_microphone
                else
                    R.drawable.unmute_microphone
                muteActionOffFab.setBackgroundResource(0)
                muteActionOffFab.layoutParams.height = 200
                muteActionOffFab.layoutParams.width = 200

                muteActionOffFab.requestLayout();
                muteActionOffFab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@TwilioHomeActivity, icon
                    )
                )

            }
        }
    }

    private fun retrieveAccessTokenfromServer() {
        Ion.with(this)
            .load("$ACCESS_TOKEN_SERVER?identity=${UUID.randomUUID()}")
            .asString()
            .setCallback { e, token ->
                if (e == null) {
                    this@TwilioHomeActivity.accessToken = token
                } else {
                    Toast.makeText(
                        this@TwilioHomeActivity,
                        R.string.error_retrieving_access_token, Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

    private fun configureAudio(enable: Boolean) {
        with(audioManager) {
            if (enable) {
                previousAudioMode = audioManager.mode
                // Request audio focus before making any device switch
                requestAudioFocus()
                /*
                 * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
                 * to be in this mode when playout and/or recording starts for the best
                 * possible VoIP performance. Some devices have difficulties with
                 * speaker mode if this is not set.
                 */
                mode = AudioManager.MODE_IN_COMMUNICATION
                /*
                 * Always disable microphone mute during a WebRTC call.
                 */
                previousMicrophoneMute = isMicrophoneMute
                isMicrophoneMute = false
            } else {
                mode = previousAudioMode
                abandonAudioFocus(null)
                isMicrophoneMute = previousMicrophoneMute
            }
        }
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { }
                .build()
            audioManager.requestAudioFocus(focusRequest)
        } else {
            audioManager.requestAudioFocus(
                null, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    private fun createConnectDialog(
        participantEditText: EditText,
        callParticipantsClickListener: DialogInterface.OnClickListener,
        cancelClickListener: DialogInterface.OnClickListener,
        context: Context
    ): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context).apply {
            setIcon(R.drawable.ic_video_call_white_24dp)
            setTitle("Connect to a room")
            setPositiveButton("Connect", callParticipantsClickListener)
            setNegativeButton("Cancel", cancelClickListener)
            setCancelable(false)
        }

        setRoomNameFieldInDialog(participantEditText, alertDialogBuilder, context)

        return alertDialogBuilder.create()
    }

    @SuppressLint("RestrictedApi")
    private fun setRoomNameFieldInDialog(
        roomNameEditText: EditText,
        alertDialogBuilder: AlertDialog.Builder,
        context: Context
    ) {
        roomNameEditText.hint = "room name"
        val horizontalPadding =
            context.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
        val verticalPadding =
            context.resources.getDimensionPixelOffset(R.dimen.activity_vertical_margin)
        alertDialogBuilder.setView(
            roomNameEditText,
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            0
        )
    }


}
