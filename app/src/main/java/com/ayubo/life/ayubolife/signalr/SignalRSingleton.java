package com.ayubo.life.ayubolife.signalr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.prochat.appointment.CommonPayload;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.ArrayList;

/**
 * Created by Chirath Perera on 2021-08-22.
 */
public class SignalRSingleton {

    private static SignalRSingleton instance;

    public HubConnection hubConnection;

    PrefManager prefManager;

    String userId = "";

    private SignalRObjectListener signalRObjectListener;

    public interface SignalRObjectListener {

        void onReceiveNewMessageReady(CommonPayload commonPayload);

        void onMessageTyping(CommonPayload commonPayload);

        void onMessageAcknowledgement(CommonPayload commonPayload);

        void onUserStats(JsonObject jsonObject);
    }


    public SignalRSingleton(Context context) {
        prefManager = new PrefManager(context);
        userId = prefManager.getLoginUser().get("uid").toString();
        hubConnection = HubConnectionBuilder
                .create("https://ayubo-notifications.azurewebsites.net/api/v1/?negotiateVersion=1")
                .withHeader("user_id", prefManager.getLoginUser().get("uid"))
                .build();

        hubConnection.on("user_presence", (message) -> {
//            signalRObjectListener.onUserPresenceReady(message);
//            System.out.println("user_presence ==========      " + message.getUser_id() + " is now online");


        }, UserPresence.class);

        hubConnection.on("user_stats", (user_stats) -> {
            System.out.println("user_stats ==========     " + user_stats);
            try {
                signalRObjectListener.onUserStats(user_stats);
            } catch (Exception e) {
                Log.i("user_stats", "Error on calling onUserStats");
            }

        }, JsonObject.class);

        hubConnection.on("on_newmessage", (new_message) -> {
            System.out.println("on_newmessage ==========     " + new_message);
            CommonPayload commonPayload = new Gson().fromJson(
                    new_message,
                    CommonPayload.class
            );
            try {
                ArrayList<String> currentUserArrayList = new ArrayList<String>();
                currentUserArrayList.add(userId);
                // do deliver
                sendSignal("message_delivered", new_message, currentUserArrayList);
                signalRObjectListener.onReceiveNewMessageReady(commonPayload);
            } catch (Exception e) {
                Log.i("on_newmessage", "Error on calling onReceiveNewMessageReady");
            }

        }, JsonObject.class);

        hubConnection.on("message_acknowledgement", (message_acknowledgement) -> {
            System.out.println("message_acknowledgement ==========     " + message_acknowledgement);
            CommonPayload commonPayload = new Gson().fromJson(
                    message_acknowledgement,
                    CommonPayload.class
            );

            try {
                signalRObjectListener.onMessageAcknowledgement(commonPayload);
            } catch (Exception e) {
                Log.i("message_acknowledgement", "Error on calling onMessageAcknowledgement");
            }


        }, JsonObject.class);

        hubConnection.on("typing", (typing) -> {
            System.out.println("typing ==========     " + typing);


            CommonPayload commonPayload = new Gson().fromJson(
                    typing,
                    CommonPayload.class
            );

            try {
                signalRObjectListener.onMessageTyping(commonPayload);
            } catch (Exception e) {

                Log.i("typing", "Error on calling onMessageTyping");
//                e.printStackTrace();
            }


        }, JsonObject.class);

        hubConnection.onClosed((error) -> {
            startConnection();
        });
    }

    public static SignalRSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new SignalRSingleton(context);
        }

        return instance;
    }

    @SuppressLint("CheckResult")
    public boolean startConnection() {


        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            try {
                hubConnection.start().doOnComplete(() -> {
//                        String loginUserId = prefManager.getLoginUser().get("uid");
//                        assert loginUserId != null;
                            UserPresence userPresence = new UserPresence(userId, "online");
                            hubConnection.invoke("user_presence", userId, userPresence);
                        }
                ).doOnError((err) -> {
                    err.printStackTrace();
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        } else {
            return false;
        }


    }

    public void sendSignal(String method, JsonObject payload, ArrayList<String> subscriberArrayList) {
        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            CommonPayload commonPayload = new CommonPayload(
                    method,
                    payload,
                    subscriberArrayList
            );
            hubConnection.invoke(method, userId, commonPayload);
        }
    }


    public void setSignalRObjectListener(SignalRObjectListener signalRObjectListener) {
        this.signalRObjectListener = signalRObjectListener;
    }


}
