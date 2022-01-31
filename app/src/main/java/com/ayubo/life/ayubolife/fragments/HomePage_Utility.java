package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HomePage_Utility {
    PrefManager pref=null;
    Context context=null;
    public HomePage_Utility(Context c){

        context=c;
        pref=new PrefManager(context);
    }


    public void openChatView(String doctorId){

        Intent intent = new Intent(context, AyuboChatActivity.class);
        intent.putExtra("doctorId", doctorId);
        intent.putExtra("isAppointmentHistory", false);
        context.startActivity(intent);
    }


    public void showAlert_Deleted(Context c, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_and_share, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_share = (TextView) layoutView.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                pref.setIsGoalAdded("yes");

                String  my_goal_sponser_large_image=  pref.getMyGoalData().get("my_goal_sponser_large_image");
                String  my_goal_share_large_image=  pref.getMyGoalData().get("my_goal_share_large_image");
                String  goalName=  pref.getMyGoalData().get("my_goal_name");

                Intent intent = new Intent(context, ShareGoals_Activity.class);
                intent.putExtra("sponserURl", my_goal_sponser_large_image);
                intent.putExtra("shareImageURl", my_goal_share_large_image);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "achived");
                context.startActivity(intent);

            }
        });


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });

        dialog.show();
    }
}
