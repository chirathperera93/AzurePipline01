package com.ayubo.life.ayubolife.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);


        View view_wattala = findViewById(R.id.layout_contact_info_wattala);
        setMainButton(view_wattala, getString(R.string.hos_wattala_name),getString(R.string.hos_wattala_add1),getString(R.string.hos_wattala_add2),getString(R.string.hos_wattala_add3), ContextCompat.getDrawable(this, R.drawable.logo_hemas),
                getString(R.string.video_call_message));

        View view_thalawathugoda = findViewById(R.id.layout_contact_info_thalawathugoda);
        setMainButton(view_thalawathugoda, getString(R.string.hos_thalawathugoda_name),getString(R.string.hos_thalawathugoda_add1),getString(R.string.hos_thalawathugoda_add2),getString(R.string.hos_thalawathugoda_add3), ContextCompat.getDrawable(this, R.drawable.logo_hemas),
                getString(R.string.video_call_message));


        View view_phone = findViewById(R.id.layout_contact_info_phone);
        setSubButton(view_phone,
                getString(R.string.hos_phone),
                ContextCompat.getDrawable(this, R.drawable.hospital_contact_sub_ic_call));

        View view_email = findViewById(R.id.layout_contact_info_email);
//        setSubButton(view_email,
//                getString(R.string.hos_email),
//                ContextCompat.getDrawable(this, R.drawable.hospital_contact_sub_ic_email));

        View view_web = findViewById(R.id.layout_contact_info_web);
        setSubButton(view_web,
                getString(R.string.hos_web),
                ContextCompat.getDrawable(this, R.drawable.hospital_contact_sub_ic_web));
                                                            //     hospital_contact_sub_ic_web
        view_wattala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/Hemas+Hospital+Wattala/@6.98878,79.8890276,17z/data=!3m1!4b1!4m5!3m4!1s0x3ae2f7d95375536b:0x2c49002a9cec30dc!8m2!3d6.98878!4d79.8912163"));
                startActivity(browserIntent);
            }
        });
        view_thalawathugoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/Hemas+Hospital,+Thalawathugoda/@6.878514,79.9331133,17z/data=!3m1!4b1!4m5!3m4!1s0x3ae250961b3ef46b:0x4771284288c53fd7!8m2!3d6.878514!4d79.935302"));
                startActivity(browserIntent);
            }
        });
        view_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0117888888"));
                startActivity(intent);
            }
        });
        view_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "info@hemashospitals.com");
                intent.setData(Uri.parse("mailto:"+"info@hemashospitals.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Message from Hemas Health");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                try {

                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("Email error:",e.toString());
                }


            }
        });
        view_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hemashospitals.com"));
                startActivity(browserIntent);
            }
        });



        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setMainButton(View view, String name,String add1,String add2,String add3, Drawable drawable, String message) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_hospital_name)).setText(name);
        ((TextView) view.findViewById(R.id.txt_name_button1)).setText(add1);
        ((TextView) view.findViewById(R.id.txt_name_button2)).setText(add2);
        ((TextView) view.findViewById(R.id.txt_name_button3)).setText(add3);
    }

    private void setSubButton(View view, String name,Drawable drawable) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_contact_name)).setText(name);

    }



}

