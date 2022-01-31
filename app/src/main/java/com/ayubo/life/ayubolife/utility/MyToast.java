package com.ayubo.life.ayubolife.utility;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.home.RelaxViewActivity;

public class MyToast extends AppCompatActivity {


    public static TextView textt;
    public static Toast toastt;
    public static LayoutInflater inflatert;
    public static View layoutt;
    static Context mContext;
    public MyToast(Context mContext) {
        this.mContext = mContext;
    }



    public static void showPopup(Context context, String msg) {

        inflatert = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
      //  layoutt = inflatert.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(context);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        textt.setText(msg);
        toastt.setView(layoutt);
        toastt.show();

    }
}
