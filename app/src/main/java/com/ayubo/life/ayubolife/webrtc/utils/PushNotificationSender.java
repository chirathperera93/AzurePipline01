package com.ayubo.life.ayubolife.webrtc.utils;



import com.ayubo.life.ayubolife.R;
import java.util.ArrayList;

/**
 * Created by tereha on 13.05.16.
 */
public class PushNotificationSender {

    public static void sendPushMessage(ArrayList<Integer> recipients, String senderName) {
        String outMessage = String.format(String.valueOf(R.string.text_push_notification_message), senderName);


    }
}
