package com.ayubo.life.ayubolife.push;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.twilio.TwilioHomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flavors.changes.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Map;

import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private PrefManager pref;
    private static final String TAG = com.ayubo.life.ayubolife.push.MyFirebaseMessagingService.class.getSimpleName();
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private String notificationTitle;
    private String notificationText;
    String message;
    private NotificationUtils notificationUtils;
    String type, meta, heading, action;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    public static final Integer NOTIFICATION_ID = 123;
    RemoteMessage remoteMessage;

    @SuppressLint("WrongConstant")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessag) {

        Log.d("onMessageReceived", "myFirebaseMessagingService - onMessageReceived - message: " + remoteMessage);
        Log.d("onMessageReceived", "..............................");
        pref = new PrefManager(this);
        remoteMessage = remoteMessag;

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT...indika..", object.toString());


        try {
            heading = object.get("title").toString();
            message = object.get("message").toString();
            type = object.get("type").toString();
            meta = object.get("meta").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        handleNotification(heading, message, meta);

    }

    // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//
//            type = remoteMessage.getData().get("type");
//            String title = remoteMessage.getData().get("video_chat");
//            String caller_id = remoteMessage.getData().get("caller_id");
//            String room_name = remoteMessage.getData().get("appointment_id");
//            String twillioToken = remoteMessage.getData().get("access_token");
//            message = remoteMessage.getData().get("message");
//
//            if ((title!=null) && (title.equals("true"))) {
//                pref.setPrefVdioCallerName(message);
//                pref.setVideocall("yes");
//                pref.setQRLink(twillioToken);
//                pref.setAppVersion(caller_id);
//
//                //FOR VIDEO CALL ....
//                System.out.println("========title.equals=======" + "true");
//                System.out.println("========twillioToken=======" + twillioToken);
//
//                if(twillioToken!=null) {
//                    try {
//                        System.out.println("========twillioToken======" + "true");
//                      //   Intent launchIntent = new Intent(getApplicationContext(), SplashScreen.class);
//                        handleNotification(heading,message);
////                        launchIntent.setClass(getBaseContext(), TwilioHomeActivity.class);
////                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        launchIntent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
////                        launchIntent.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
////                        launchIntent.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////                        launchIntent.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//                     //   System.out.println("========twillioToken======" + "true3");
////                        Intent i = new Intent(MyService.this, MyActivity.class);
////                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        MyService.this.startActivity(i);
//
//                        final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
//                        mp.start();
//                        mp.setLooping(true);
//                        Intent launchIntent = new Intent(getApplicationContext(), SplashScreen.class);
//                        launchIntent.putExtra("twillio_caller_name", message);
//                        launchIntent.putExtra("twillio_room_name", room_name);
//                        launchIntent.putExtra("twillio_access_token", twillioToken);
//                        launchIntent.putExtra("caller_id", caller_id);
//                        launchIntent.putExtra("Ayubo.Life is Calling", message);
//                        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        launchIntent.setAction(Intent.ACTION_MAIN);
//                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(), 0, launchIntent, 0);
//                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),
//                                NOTIFICATION_CHANNEL_ID)
//                                .setSmallIcon(R.drawable.answer_icon)
//                                .setContentTitle("Test")
//                                .setContentText("Hello! This is my first push notification")
//                                .setContentIntent(resultIntent);
//                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                            int importance = NotificationManager.IMPORTANCE_HIGH;
//                            NotificationChannel notificationChannel = new
//                                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//                            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//                            assert mNotificationManager != null;
//                         //   mNotificationManager.createNotificationChannel(notificationChannel);
//                        }
//                        assert mNotificationManager != null;
////                        mNotificationManager.notify((int) System.currentTimeMillis(),
////                                mBuilder.build());
//
//
//                      //  launchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                      //  startActivity(launchIntent);
//                        System.out.println("========twillioToken======" + "true3 3");
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//            else {
//                pref.setVideocall("no");
//                System.out.println("========title.equals=======" + "false");
//                String meta = remoteMessage.getData().get("meta");

    //                System.out.println("Notification=========" + meta);
//                type = remoteMessage.getData().get("type");
//                meta = remoteMessage.getData().get("meta");
//               // heading = remoteMessage.getData().get("heading");
//                heading = remoteMessage.getNotification().getTitle();
//                pref.setPushMeta(meta);
//                handleNotification(heading,message);
//            }
//            try {
//               // JSONObject json = new JSONObject(remoteMessage.getData().toString());
//              //  handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//
//        }
//        else{
//            type = remoteMessage.getData().get("type");
//            meta = remoteMessage.getData().get("meta");
//            heading = remoteMessage.getData().get("title");
//            //remoteMessage.getNotification().getBody()
//            heading = remoteMessage.getNotification().getTitle();
//            pref.setPushMeta(meta);
//            handleNotification(heading,remoteMessage.getNotification().getBody());
//
//        }
    private Spannable getActionText(@StringRes int stringRes, @ColorRes int colorRes) {
        Spannable spannable = new SpannableString(this.getText(stringRes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // This will only work for cases where the Notification.Builder has a fullscreen intent set
            // Notification.Builder that does not have a full screen intent will take the color of the
            // app and the following leads to a no-op.
            spannable.setSpan(
                    new ForegroundColorSpan(this.getColor(colorRes)), 0, spannable.length(), 0);
        }
        return spannable;
    }

    @SuppressLint({"WrongConstant", "MissingPermission"})
    void callVideo() {

        if (remoteMessage.getData().size() > 0) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (pref.getRingtone() == null) {
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                ringtone.play();
                pref.setRingingTone(ringtone);
            }

            if (pref.getVibrator() == null) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(60000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(60000);
                }


                pref.setVibrator(vibrator);
            }

//            if (Build.VERSION.SDK_INT >= 26) {
////                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
//            } else {
////                vibrator.vibrate(1000);
//            }


            type = remoteMessage.getData().get("type");
            String title = remoteMessage.getData().get("title");
            String caller_id = remoteMessage.getData().get("caller_id");
            String room_name = remoteMessage.getData().get("appointment_id");
            String twillioToken = remoteMessage.getData().get("access_token");
            String consultant_pic = remoteMessage.getData().get("consultant_pic");
            String consultant_specilization = remoteMessage.getData().get("consultant_specilization");
            message = remoteMessage.getData().get("message");

            pref.setPrefVdioCallerName(message);
            pref.setVideocall("yes");
            pref.setVCallerName(twillioToken);
            pref.setVRoomName(room_name);
            pref.setVRoomToken(twillioToken);
            pref.setVCallerId(caller_id);
            pref.setVConsultantPic(consultant_pic);
            pref.setVConsultantSpecilization(consultant_specilization);
            pref.setVTitle(title);

            Intent launchIntent = new Intent(getApplicationContext(), TwilioHomeActivity.class);
            launchIntent.putExtra("twillio_caller_name", message);
            launchIntent.putExtra("tw_name", room_name);
            launchIntent.putExtra("tw_token", twillioToken);
            launchIntent.putExtra("caller_id", caller_id);
            launchIntent.putExtra("Ayubo.Life is Calling", message);
            launchIntent.putExtra("consultant_pic", consultant_pic);
            launchIntent.putExtra("consultant_specilization", consultant_specilization);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setAction(Intent.ACTION_MAIN);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            launchIntent.addFlags(FLAG_SHOW_WHEN_LOCKED);
            launchIntent.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            launchIntent.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;

            Uri customSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beep);

//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(getBaseContext().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_MAX;
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel =
                        new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_MAX);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                notificationChannel.setSound(notification, audioAttributes);
                notificationChannel.getImportance();
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.getLockscreenVisibility();

                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(notificationChannel);
            }

//           Intent buttonIntent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);

//            Intent buttonIntent = new Intent(getBaseContext(), LifePlusProgramActivity.class);
            Intent buttonIntent = new Intent(getBaseContext(), ProfileNew.class);
            buttonIntent.putExtra("notificationId", NOTIFICATION_CHANNEL_ID);

            PendingIntent dismissIntent = NotificationActivity.getDismissIntent(0, getBaseContext());

//            PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);


            PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(), 0, launchIntent, 0);

//            final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
//            mp.start();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.answer_icon);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),
//                    NOTIFICATION_CHANNEL_ID)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap))
//                    .setSmallIcon(R.drawable.answer_icon)
//                    .setCategory(NotificationCompat.CATEGORY_CALL)
//                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setSound(uri)
//                    .setTimeoutAfter(50000)
//                    .setFullScreenIntent(resultIntent, true)
//                    .setContentTitle(title)
//                    .setContentText(message)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(message))
//                    .setContentIntent(resultIntent);
//            mBuilder.setAutoCancel(true);

//            Typeface face1 = ResourcesCompat.getFont(getBaseContext(), R.font.montserrat_bold);

            @SuppressLint("RemoteViewLayout")
            RemoteViews remoteView1 = new RemoteViews(getPackageName(), R.layout.custom_push);

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(getBaseContext()));


            try {
                Bitmap remoteViewBitmap = Glide.with(getBaseContext())
                        .asBitmap()
                        .load(consultant_pic)
                        .apply(requestOptions)
                        .submit(512, 512)
                        .get();

//                remoteView1.setImageViewResource(R.id.image, R.drawable.userpic);
                remoteView1.setImageViewBitmap(R.id.image, remoteViewBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


            SpannableString titleSpannableString = new SpannableString(caller_id);
            titleSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, caller_id.length(), 0);
            remoteView1.setTextViewText(R.id.title, titleSpannableString);
            remoteView1.setTextViewText(R.id.text, message);
            String sAnswer = "Answer";
            String sDecline = "Decline";
            SpannableString answerSpannableString = new SpannableString(sAnswer);
            answerSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, sAnswer.length(), 0);
            remoteView1.setTextViewText(R.id.answer_text, answerSpannableString);
            SpannableString declineSpannableString = new SpannableString(sDecline);
            declineSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, sDecline.length(), 0);
            remoteView1.setTextViewText(R.id.decline_text, declineSpannableString);


            if (Constants.type == Constants.Type.LIFEPLUS) {
                remoteView1.setTextViewText(R.id.notification_brand, "Life+");
            } else {
                remoteView1.setTextViewText(R.id.notification_brand, "AyuboLife1");
            }
            remoteView1.setOnClickPendingIntent(R.id.answer, resultIntent);
            remoteView1.setOnClickPendingIntent(R.id.decline, dismissIntent);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),
                    NOTIFICATION_CHANNEL_ID)
//                    .setContentTitle(caller_id)
//                    .setContentText(message)
                    .setCustomContentView(remoteView1)
                    .setCustomBigContentView(remoteView1)
                    .setContent(remoteView1)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap))


//                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())

                    .setTimeoutAfter(180000)

//                    .addAction(R.drawable.cut_the_call, "Reject",
//                            dismissIntent)

                    .setVisibility(Notification.VISIBILITY_PUBLIC)

//                    .addAction(R.drawable.answer_icon, "Answer",
//                            resultIntent)


                    .setSmallIcon(R.drawable.answer_icon)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setSound(notification)

                    .setFullScreenIntent(resultIntent, true)
                    .setOngoing(true)
                    .setVibrate(new long[]{500, 1000})


//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(message))


                    .setContentIntent(resultIntent);
            mBuilder.setAutoCancel(true);

//                    .addAction(R.drawable.btn_signin, "DISMISS", dismissIntent) // #0
//                    .addAction(R.drawable.btn_signin, "ACCEPT", resultIntent)  // #1
// .setStyle(new NotificationCompat.InboxStyle()
//                    .addLine("ACCEPT")
//                    .addLine("CANCEL"))
            Notification incomingCallNotification = mBuilder.build();

            notificationManager.notify(0, mBuilder.build());
            pref.setNotificationMgr(notificationManager);


//            if (!NotificationUtils.isAppRunning(getApplicationContext(), "com.ayubo.life")) {
//                startForeground(0, incomingCallNotification);
//            }else{
//                notificationManager.notify(0, mBuilder.build());
//            }
            // notificationManager.notify(0, mBuilder.build());

//                if (!NotificationUtils.isAppRunning(getApplicationContext(), "com.ayubo.life")) {
//                    notificationManager.notify(0, mBuilder.build());
//                }else{
//                    startForeground(0, incomingCallNotification);
//                }
            //startForeground(0, incomingCallNotification);


            // int notificationId = 123;
            //  NotificationManagerCompat.from(getApplicationContext()).cancel(notificationId, 0));

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("d");
    }

    private void handleNotification(String title, String message, String meta) {

        if (!NotificationUtils.isAppRunning(getApplicationContext(), "com.ayubo.life")) {

            if (remoteMessage.getData().size() > 0) {
                if (remoteMessage.getData().get("video_chat").equals("true")) {
                    callVideo();
                } else {

//                    Intent resultIntent = new Intent(getApplicationContext(), NewHomeWithSideMenuActivity.class);
                    Intent resultIntent = new Intent(getApplicationContext(), ProfileNew.class);
                    resultIntent.putExtra("message", message);
                    resultIntent.putExtra("meta", meta);
                    resultIntent.putExtra("type", type);

                    pref.setAllFireBaseNotificationData(type, meta);

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    if (heading != null && heading.length() > 0) {
                        heading = title;
                    } else {
                        if (Constants.type == Constants.Type.LIFEPLUS) {
                            heading = "LifePlus";
                        }
                        if (Constants.type == Constants.Type.AYUBO) {
                            heading = "ayubo.life";
                        } else if (Constants.type == Constants.Type.SHESHELLS) {
                            heading = "sheshells";
                        } else if (Constants.type == Constants.Type.HEMAS) {
                            heading = "Hemas Health";
                        }

                    }
                    showNotificationMessage(getApplicationContext(), heading, message, timestamp.toString(), resultIntent);

//                    if (type != null) {
//
//                        Intent intent = null;
//
//                        if (type.equals("goal")) {
//
//                            if (pref.getPushMeta().length() > 0) {
//                                PrefManager prefManager = new PrefManager(getBaseContext());
//                                String status = prefManager.getMyGoalData().get("my_goal_status");
//
//                                switch (status) {
//                                    case "Pending":
//                                        intent = new Intent(getBaseContext(), AchivedGoal_Activity.class);
//                                        startActivity(intent);
//                                        break;
//                                    case "Pick":
//                                        intent = new Intent(getBaseContext(), PickAGoal_Activity.class);
//                                        startActivity(intent);
//                                        break;
//                                    case "Completed":
//                                        HomePage_Utility serviceObj = new HomePage_Utility(getBaseContext());
//                                        serviceObj.showAlert_Deleted(getBaseContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
//                                        break;
//                                }
//                            }
//                        }
//
//                        switch (type) {
//                            case "programtimeline":
//                                intent = new Intent(getBaseContext(), ProgramActivity.class);
//                                intent.putExtra("meta", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "program":
//                                intent = new Intent(getBaseContext(), SingleTimeline_Activity.class);
//                                intent.putExtra("related_by_id", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra("type", "program");
//                                startActivity(intent);
//                                break;
//                            case "reports":
//                                intent = new Intent(getBaseContext(), ReportDetailsActivity.class);
//                                intent.putExtra("data", "all");
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Ram.setReportsType("fromHome");
//                                startActivity(intent);
//                                break;
//                            case "points":
//                                intent = new Intent(getBaseContext(), LifePointActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "native_post":
//                                intent = new Intent(getBaseContext(), NativePostActivity.class);
//                                intent.putExtra("meta", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//
//                                break;
//                            case "native_post_json":
//                                intent = new Intent(getBaseContext(), NativePostJSONActivity.class);
//                                intent.putExtra("meta", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//
//                                break;
//                            case "help":
//                                intent = new Intent(getBaseContext(), HelpFeedbackActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "chanelling":
//                                intent = new Intent(getBaseContext(), DashboardActivity.class);
//                                Ram.setMapSreenshot(null);
//                                intent.putExtra("activityName", "chanelling");
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//
//                                break;
//                            case "doc_chat":
//                                intent = new Intent(getBaseContext(), AyuboChatActivity.class);
//                                intent.putExtra("doctorId", pref.getPushMeta());
//                                intent.putExtra("isAppointmentHistory", false);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//
//                                break;
//                            case "chat":
//
//                                if (pref.getPushMeta().length() > 0) {
//                                    intent = new Intent(getBaseContext(), AyuboChatActivity.class);
//                                    intent.putExtra("doctorId", pref.getPushMeta());
//                                    intent.putExtra("isAppointmentHistory", false);
//                                    startActivity(intent);
//                                } else {
//                                    intent = new Intent(getBaseContext(), AskActivity.class);
//                                }
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "challenge":
//
//                                if (pref.getPushMeta().length() > 0) {
//                                    intent = new Intent(getBaseContext(), MapChallengeActivity.class);
//                                    pref.setIsFromPush(true);
//                                    intent.putExtra("challenge_id", pref.getPushMeta());
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                }
//                                break;
//                            case "Videocall":
//                                if (pref.getPushMeta().length() > 0) {
//                                    String activity = "my_doctor";
//                                    intent = new Intent(getBaseContext(), MyDoctorLocations_Activity.class);
//                                    intent.putExtra("doctor_id", pref.getPushMeta());
//                                    intent.putExtra("activityName", activity);
//                                } else {
//                                    intent = new Intent(getBaseContext(), MyDoctor_Activity.class);
//                                    intent.putExtra("activityName", "myExperts");
//                                }
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "web":
//                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pref.getPushMeta()));
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "commonview":
//                                intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
//                                intent.putExtra("URL", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "post":
//                                intent = new Intent(getBaseContext(), OpenPostActivity.class);
//                                intent.putExtra("postID", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "channeling":
//                                if (pref.getPushMeta().length() > 0) {
//                                    startDoctorsActivity(pref.getPushMeta());
//                                } else {
//                                    intent = new Intent(getBaseContext(), DashboardActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                }
//                                break;
//
//                            case "janashakthiwelcome": {
//                                PrefManager pref = new PrefManager(getBaseContext());
//                                pref.setRelateID(pref.getPushMeta());
//                                pref.setIsJanashakthiWelcomee(true);
//                                intent = new Intent(getBaseContext(), JanashakthiWelcomeActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            }
//                            case "janashakthireports":
//                                intent = new Intent(getBaseContext(), MedicalUpdateActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "dynamicquestion": {
//                                PrefManager pref = new PrefManager(getBaseContext());
//                                pref.setIsJanashakthiDyanamic(true);
//                                pref.setRelateID(pref.getPushMeta());
//                                intent = new Intent(getBaseContext(), IntroActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            }
//                            case "store_group":
//
//                                intent = new Intent(getBaseContext(), GroupViewActivity.class);
//                                intent.putExtra("paymentmeta", pref.getPushMeta());
//
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                startActivity(intent);
//                                break;
//                            case "home":
//                                intent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case "discover":
//                                intent = new Intent(getBaseContext(), LifePlusProgramActivity.class);
//                                intent.putExtra("isFromSearchResults", false);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//
////                        case "data_challenge":
////                            intent = new Intent(getBaseContext(), WalkWinMainActivity.class);
////                            intent.putExtra("challenge_id", meta);
////                            startActivity(intent);
////                            break;
//
//
////                        default:
////                            intent = new Intent(getApplicationContext(), NewHomeWithSideMenuActivity.class);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            intent.putExtra("message", message);
////                            startActivity(intent);
//                        }
//
//
//                    }

//                else {
//                    Intent intent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
                }

            }


        } else {
            if (remoteMessage.getData().get("video_chat").equals("true")) {
                callVideo();
            }


//            else {
//
//                Intent intent = null;
//
//                if (type != null) {
//
//
//                    if (type.equals("goal")) {
//                        if (pref.getPushMeta().length() > 0) {
//                            PrefManager prefManager = new PrefManager(getBaseContext());
//                            String status = prefManager.getMyGoalData().get("my_goal_status");
//
//                            switch (status) {
//                                case "Pending":
//                                    intent = new Intent(getBaseContext(), AchivedGoal_Activity.class);
//                                    startActivity(intent);
//                                    break;
//                                case "Pick":
//                                    intent = new Intent(getBaseContext(), PickAGoal_Activity.class);
//                                    startActivity(intent);
//                                    break;
//                                case "Completed":
//                                    HomePage_Utility serviceObj = new HomePage_Utility(getBaseContext());
//                                    serviceObj.showAlert_Deleted(getBaseContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
//                                    break;
//                            }
//                        }
//                    }
//
//
//                    switch (type) {
//                        case "programtimeline":
//                            intent = new Intent(getBaseContext(), ProgramActivity.class);
//                            intent.putExtra("meta", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "program":
//                            intent = new Intent(getBaseContext(), SingleTimeline_Activity.class);
//                            intent.putExtra("related_by_id", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("type", "program");
//                            startActivity(intent);
//                            break;
//                        case "reports":
//                            intent = new Intent(getBaseContext(), ReportDetailsActivity.class);
//                            intent.putExtra("data", "all");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            Ram.setReportsType("fromHome");
//                            startActivity(intent);
//                            break;
//                        case "points":
//                            intent = new Intent(getBaseContext(), LifePointActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "native_post":
//                            intent = new Intent(getBaseContext(), NativePostActivity.class);
//                            intent.putExtra("meta", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            break;
//                        case "native_post_json":
//                            intent = new Intent(getBaseContext(), NativePostJSONActivity.class);
//                            intent.putExtra("meta", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            break;
//                        case "help":
//                            intent = new Intent(getBaseContext(), HelpFeedbackActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "chanelling":
//                            intent = new Intent(getBaseContext(), DashboardActivity.class);
//                            Ram.setMapSreenshot(null);
//                            intent.putExtra("activityName", "chanelling");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            break;
//                        case "doc_chat":
//                            intent = new Intent(getBaseContext(), AyuboChatActivity.class);
//                            intent.putExtra("doctorId", pref.getPushMeta());
//                            intent.putExtra("isAppointmentHistory", false);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            break;
//                        case "chat":
//
//                            if (pref.getPushMeta().length() > 0) {
//                                intent = new Intent(getBaseContext(), AyuboChatActivity.class);
//                                intent.putExtra("doctorId", pref.getPushMeta());
//                                intent.putExtra("isAppointmentHistory", false);
//                                startActivity(intent);
//                            } else {
//                                intent = new Intent(getBaseContext(), AskActivity.class);
//                            }
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "challenge":
//
//                            if (pref.getPushMeta().length() > 0) {
//                                intent = new Intent(getBaseContext(), MapChallengeActivity.class);
//                                pref.setIsFromPush(true);
//                                intent.putExtra("challenge_id", pref.getPushMeta());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                            break;
//                        case "Videocall":
//                            if (pref.getPushMeta().length() > 0) {
//                                String activity = "my_doctor";
//                                intent = new Intent(getBaseContext(), MyDoctorLocations_Activity.class);
//                                intent.putExtra("doctor_id", pref.getPushMeta());
//                                intent.putExtra("activityName", activity);
//                            } else {
//                                intent = new Intent(getBaseContext(), MyDoctor_Activity.class);
//                                intent.putExtra("activityName", "myExperts");
//                            }
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "web":
//                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pref.getPushMeta()));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "commonview":
//                            intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
//                            intent.putExtra("URL", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "post":
//                            intent = new Intent(getBaseContext(), OpenPostActivity.class);
//                            intent.putExtra("postID", pref.getPushMeta());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "channeling":
//                            if (pref.getPushMeta().length() > 0) {
//                                startDoctorsActivity(pref.getPushMeta());
//                            } else {
//                                intent = new Intent(getBaseContext(), DashboardActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                            break;
//                        case "janashakthiwelcome": {
//                            PrefManager pref = new PrefManager(getBaseContext());
//                            pref.setRelateID(pref.getPushMeta());
//                            pref.setIsJanashakthiWelcomee(true);
//                            intent = new Intent(getBaseContext(), JanashakthiWelcomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        }
//                        case "janashakthireports":
//                            intent = new Intent(getBaseContext(), MedicalUpdateActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "dynamicquestion": {
//                            PrefManager pref = new PrefManager(getBaseContext());
//                            pref.setIsJanashakthiDyanamic(true);
//                            pref.setRelateID(pref.getPushMeta());
//                            intent = new Intent(getBaseContext(), IntroActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        }
//                        case "home":
//                            intent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
//                        case "discover":
//                            intent = new Intent(getBaseContext(), LifePlusProgramActivity.class);
//                            intent.putExtra("isFromSearchResults", false);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            break;
////                        case "data_challenge":
////                            intent = new Intent(this, WalkWinMainActivity.class);
////                            intent.putExtra("challenge_id", meta);
////                            startActivity(intent);
////                            break;
////                        default:
////                            intent = new Intent(getApplicationContext(), NewHomeWithSideMenuActivity.class);
////                            intent.putExtra("message", message);
////                            startActivity(intent);
//                    }
//
//
//                }
//
//
////                else {
////                    Intent intent2 = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
////                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivity(intent2);
////                }
//
//
////            else{
////                intent = new Intent(getApplicationContext(), NewHomeWithSideMenuActivity.class);
////                intent.putExtra("message", message);
////                startActivity(intent);
////            }
//
//                // check for image attachment
////            if (TextUtils.isEmpty("")) {
////                showNotificationMessage(getApplicationContext(), "title", message, null, intent);
////            } else {
////                // image is present, show notification with image
////                showNotificationMessageWithBigImage(getApplicationContext(), "title", message, null, intent, "");
////            }
//            }
        }
    }


    void startDoctorsActivity(String doctorID) {
        DocSearchParameters parameters;
        parameters = new DocSearchParameters();
        PrefManager pref = new PrefManager(getBaseContext());
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }


    void playSound() {
//        try{
//            AudioManager mobilemode = (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
////   int streamMaxVolume = mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING);
//            switch (mobilemode.getRingerMode()) {
//                case AudioManager.RINGER_MODE_SILENT:
//                    Log.i("MyApp","Silent mode");
//
//                    mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                    mobilemode.setStreamVolume(AudioManager.STREAM_RING,mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING),0);
//                 //   mobilemode.setStreamVolume (AudioManager.STREAM_MUSIC,mobilemode.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
//                    break;
//                case AudioManager.RINGER_MODE_VIBRATE:
//                    Log.i("MyApp","Vibrate mode");
//                    mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                    mobilemode.setStreamVolume(AudioManager.STREAM_RING,mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING),0);
//                //    mobilemode.setStreamVolume (AudioManager.STREAM_MUSIC,mobilemode.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
//                    break;
//                case AudioManager.RINGER_MODE_NORMAL:
//                    Log.i("MyApp","Normal mode");
//                    mobilemode.setStreamVolume(AudioManager.STREAM_RING,mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING),0);
//                    break;
//            }
//        }catch(Exception e){
//       e.printStackTrace();
//        }

//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setNotificationData() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.arr)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, com.ayubo.life.ayubolife.push.MyFirebaseMessagingService.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(com.ayubo.life.ayubolife.push.MyFirebaseMessagingService.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());
    }

    private void handleDataMessage(JSONObject json) {
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("============handleDataMessage=================");
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("=============================");

        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String type = data.getString("type");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                //    notificationUtils.playNotificationSound();
            } else {

                Intent intent = null;
                // app is in background, show the notification in notification tray

                if ((type != null) && (type.equals("chat"))) {
                    pref.setQuestionGroupId("1");
                    pref.setIsJanashakthiWelcomee(true);
                    intent = new Intent(getBaseContext(), JanashakthiWelcomeActivity.class);

                } else {
//                    intent = new Intent(getApplicationContext(), NewHomeWithSideMenuActivity.class);
//                    intent = new Intent(getApplicationContext(), LifePlusProgramActivity.class);
                    intent = new Intent(getApplicationContext(), ProfileNew.class);
                    intent.putExtra("message", message);
                }


                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, intent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, intent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("============showNotificationMessageWithBigImage=================");
        System.out.println("=============================");
        System.out.println("=============================");
        System.out.println("=============================");
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


}