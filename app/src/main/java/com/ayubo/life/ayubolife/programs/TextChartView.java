package com.ayubo.life.ayubolife.programs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

import java.util.List;


public class TextChartView extends AppCompatActivity {
    ImageView drawingImageView;

    public void initPaintEndConer(String endConerColor, Canvas canva,int width, int densityDpi){
        Paint paintEndConer = new Paint();
        if(endConerColor.equals("red")){
            paintEndConer.setColor(Color.RED);
        }
        if(endConerColor.equals("orange")){
            paintEndConer.setColor(Color.parseColor("#ff912a"));
        }else{
            paintEndConer.setColor(Color.RED);
        }

        paintEndConer.setColor(Color.parseColor("#53db7f"));
        canva.drawCircle(width-10, densityDpi*50, 10, paintEndConer);

    }

    public void initPaintStartConer(String startConerColor,Canvas canva,int densityDpi){

        Paint paintStartConer = new Paint();
        if(startConerColor.equals("red")){
            paintStartConer.setColor(Color.RED);
        }
        if(startConerColor.equals("green")){
            paintStartConer.setColor(Color.parseColor("#53db7f"));
        }
        paintStartConer.setColor(Color.RED);
        canva.drawCircle(10, densityDpi*50, 10, paintStartConer);
    }

    //    public void drawMainBarInRed(Canvas canva,int width){
//        Paint paint = new Paint();
//        paint.setColor(Color.parseColor("#53db7f"));
//        paint.setStrokeWidth(20);
//        int startx = 10;
//        int endx = width-10;
//        int starty = 100;
//        int endy = 100;
//        canva.drawLine(startx, starty, endx, endy, paint);
//    }
    public void drawMainBarInRed(Canvas canva,int width,int densityDpi){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#53db7f"));
        paint.setStrokeWidth(20);
        int startx = 10;
        int endx = width-10;
        int starty = densityDpi*50;
        int endy = densityDpi*50;
        canva.drawLine(startx, starty, endx, endy, paint);
    }

    public void initPaintSectionTwo(int width,Canvas canva,int densityDpi){
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ff912a"));
        paint2.setStrokeWidth(20);
        int startx2 = width;
        int endx2 = width+width;

        int startY = densityDpi*50;
        int endY = densityDpi*50;
        canva.drawLine(startx2, startY, endx2, endY, paint2);
    }
    public void initPaintSectionOne(int width,Canvas canva,int densityDpi){
        Paint paint3 = new Paint();
        paint3.setColor(Color.RED);
        paint3.setStrokeWidth(20);
        int startY = densityDpi*50;
        int endY = densityDpi*50;
        canva.drawLine(10, startY, width, endY, paint3);
    }

    public void initShowYouText(Context ac,int width,String current, Canvas canva ,int densityDpi,List<String> values){

        Paint paint5 = new Paint();
        paint5.setColor(Color.BLACK);
        paint5.setTextSize(20);
        int part=width/6;
        int partSmall=width/12;
        int pos1=part-(part/2);
        int pos2=(width/2)-partSmall;
        int pos3=(width-(width/6))-partSmall;

        //(values)
        int statusLevel=0;

        for (int i = 0; i < values.size(); i++) {
             String status =   values.get(i);
             if(current.equals(status)){
                 statusLevel=i;
             }
        }



        int position=0;

        if(statusLevel==1){
            position=pos1;
        }
        if(statusLevel==2){
            position=pos2;
        }
        if(statusLevel==3){
            position=pos3;
        }
        //  canva.drawBitmap(b2, position, 10, paint7);
        //  canva.drawText("You", position+35, 45, paint5);

        LayoutInflater li = (LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert li != null;//program_dashboard_textchart_you_and_target
        View v = li.inflate(R.layout.program_dashboard_chart_bubble_yellow_you, null);
        v.measure(View.MeasureSpec.getSize(v.getMeasuredWidth()), View.MeasureSpec.getSize(v.getMeasuredHeight()));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canva.save();
        int xLocation=((int)position);
        canva.translate(xLocation, 0);
        v.draw(canva);
        canva.restore();

//===================================================

    }

    public void initMyStatus(Context ac, int width, String current, Canvas canva , int densityDpi, List<String> values){

        LayoutInflater captionLay = (LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert captionLay != null;
        View vCaption = captionLay.inflate(R.layout.program_dashboard_textchart_you_and_target, null);



        RelativeLayout ayout_caption1=vCaption.findViewById(R.id.ayout_caption1);
        RelativeLayout ayout_caption2=vCaption.findViewById(R.id.ayout_caption2);
        RelativeLayout ayout_caption3=vCaption.findViewById(R.id.ayout_caption3);

        LinearLayout layout_caption1=vCaption.findViewById(R.id.layout_caption1);
        LinearLayout layout_caption2=vCaption.findViewById(R.id.layout_caption2);
        LinearLayout layout_caption3=vCaption.findViewById(R.id.layout_caption3);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_caption1.getLayoutParams();
        lp.width = width/3;
        layout_caption1.setLayoutParams(lp);

        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) layout_caption2.getLayoutParams();
        lp2.width = width/3;
        layout_caption2.setLayoutParams(lp2);

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) layout_caption3.getLayoutParams();
        lp3.width = width/3;
        layout_caption3.setLayoutParams(lp3);



        int statusLevel=0;

        for (int i = 0; i < values.size(); i++) {
            String status =   values.get(i);
            if(current.equals(status)){
                statusLevel=i;
            }
        }

        if(statusLevel==0){
            ayout_caption1.setVisibility(View.VISIBLE);
            ayout_caption2.setVisibility(View.GONE);
            ayout_caption3.setVisibility(View.GONE);
        }
        if(statusLevel==1){
            ayout_caption1.setVisibility(View.GONE);
            ayout_caption2.setVisibility(View.VISIBLE);
            ayout_caption3.setVisibility(View.GONE);
        }
        if(statusLevel==2){
            ayout_caption1.setVisibility(View.GONE);
            ayout_caption2.setVisibility(View.GONE);
            ayout_caption3.setVisibility(View.VISIBLE);
        }

        vCaption.measure(width, View.MeasureSpec.getSize(vCaption.getMeasuredHeight()));
        vCaption.layout(1, 0, 0, 0);
        canva.save();
        canva.translate(0, 0);
        vCaption.draw(canva);
        canva.restore();
    }

    public void initTargetStatus(Context ac, int width, String current, Canvas canva , int densityDpi, List<String> values){

        LayoutInflater captionLay = (LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert captionLay != null;
        View vCaption = captionLay.inflate(R.layout.program_dashboard_textchart_target, null);



        RelativeLayout ayout_caption1=vCaption.findViewById(R.id.ayout_caption1);
        RelativeLayout ayout_caption2=vCaption.findViewById(R.id.ayout_caption2);
        RelativeLayout ayout_caption3=vCaption.findViewById(R.id.ayout_caption3);

        LinearLayout layout_caption1=vCaption.findViewById(R.id.layout_caption1);
        LinearLayout layout_caption2=vCaption.findViewById(R.id.layout_caption2);
        LinearLayout layout_caption3=vCaption.findViewById(R.id.layout_caption3);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_caption1.getLayoutParams();
        lp.width = width/3;
        layout_caption1.setLayoutParams(lp);

        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) layout_caption2.getLayoutParams();
        lp2.width = width/3;
        layout_caption2.setLayoutParams(lp2);

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) layout_caption3.getLayoutParams();
        lp3.width = width/3;
        layout_caption3.setLayoutParams(lp3);



        int statusLevel=0;

        for (int i = 0; i < values.size(); i++) {
            String status =   values.get(i);
            if(current.equals(status)){
                statusLevel=i;
            }
        }

        if(statusLevel==0){
            ayout_caption1.setVisibility(View.VISIBLE);
            ayout_caption2.setVisibility(View.GONE);
            ayout_caption3.setVisibility(View.GONE);
        }
        if(statusLevel==1){
            ayout_caption1.setVisibility(View.GONE);
            ayout_caption2.setVisibility(View.VISIBLE);
            ayout_caption3.setVisibility(View.GONE);
        }
        if(statusLevel==2){
            ayout_caption1.setVisibility(View.GONE);
            ayout_caption2.setVisibility(View.GONE);
            ayout_caption3.setVisibility(View.VISIBLE);
        }

        vCaption.measure(width, View.MeasureSpec.getSize(vCaption.getMeasuredHeight()));
        vCaption.layout(1, 0, 0, 0);
        canva.save();
        canva.translate(0, 0);
        vCaption.draw(canva);
        canva.restore();
    }


    public void initCaptionText(Context ac, int width, String current, Canvas canva , int densityDpi, List<String> values){

        LayoutInflater captionLay = (LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert captionLay != null;
        View vCaption = captionLay.inflate(R.layout.program_dashboard_textchart_lables, null);

        TextView txt_caption_one=vCaption.findViewById(R.id.txt_caption_one);
        txt_caption_one.setText(values.get(0));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) txt_caption_one.getLayoutParams();
        lp.width = width/3;
        txt_caption_one.setLayoutParams(lp);

        TextView txt_caption_two=vCaption.findViewById(R.id.txt_caption_two);
        txt_caption_two.setText(values.get(1));
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) txt_caption_two.getLayoutParams();
        lp2.width = width/3;
        txt_caption_two.setLayoutParams(lp2);

        TextView txt_caption_three=vCaption.findViewById(R.id.txt_caption_three);
        txt_caption_three.setText(values.get(2));
        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) txt_caption_three.getLayoutParams();
        lp3.width = width/3;
        txt_caption_three.setLayoutParams(lp3);

        vCaption.measure(width, View.MeasureSpec.getSize(vCaption.getMeasuredHeight()));
        vCaption.layout(1, 0, 0, 0);
        canva.save();
        canva.translate(0, densityDpi*65);
        vCaption.draw(canva);
        canva.restore();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        drawingImageView = (ImageView) this.findViewById(R.id.drawingImageView);
        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);




    }
}
