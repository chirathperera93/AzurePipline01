package com.ayubo.life.ayubolife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.programs.DrawView;

import static com.ayubo.life.ayubolife.R.*;

public class TestActivity extends AppCompatActivity {
    ImageView drawingImageView;

    AbsoluteLayout layout2;
    LinearLayout layout;



   public void initPaintEndConer(String endConerColor,Canvas canva){
       Paint paintEndConer = new Paint();
       if(endConerColor.equals("red")){
           paintEndConer.setColor(Color.RED);
       }
       if(endConerColor.equals("orange")){
           paintEndConer.setColor(Color.parseColor("#ff912a"));
       }else{
           paintEndConer.setColor(Color.RED);
       }
        canva.drawCircle(710, 100, 10, paintEndConer);

    }

    public void initPaintStartConer(String startConerColor,Canvas canva){
        Paint paintStartConer = new Paint();
        if(startConerColor.equals("red")){
            paintStartConer.setColor(Color.RED);
        }
        if(startConerColor.equals("green")){
            paintStartConer.setColor(Color.parseColor("#53db7f"));
        }

        canva.drawCircle(10, 100, 10, paintStartConer);
    }

    public void initPaintMainBar(Canvas canva,int width){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        int startx = 10;
        int endx = width-10;
        int starty = 100;
        int endy = 100;
        canva.drawLine(startx, starty, endx, endy, paint);
    }
    public void initPaintSectionTwo(int startX,int endX,Canvas canva){
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ff912a"));
        paint2.setStrokeWidth(20);
        int startx2 = startX;
        int endx2 = endX;
        int starty2 = 100;
        int endy2 = 100;
        canva.drawLine(startx2, starty2, endx2, endy2, paint2);
    }
    public void initPaintSectionOne(int startX,int endX,Canvas canva){
        Paint paint3 = new Paint();
        paint3.setColor(Color.parseColor("#53db7f"));
        paint3.setStrokeWidth(20);
        int starty3 = 100;
        int endy3 = 100;
        if(startX<11){
            startX=10;
        }
        canva.drawLine(startX, starty3, endX, endy3, paint3);
    }


    public void initBottomBubbleGreen(Context ac,String target,Canvas canva){
        Bitmap b= BitmapFactory.decodeResource(ac.getResources(), drawable.bubbleg);
        Paint paint4 = new Paint();
        canva.drawBitmap(b, 190, 110, paint4);

        Paint paint8 = new Paint();
        paint8.setColor(Color.BLACK);
        paint8.setTextSize(20);
        canva.drawText(target, 210, 160, paint8);
        canva.drawText("Target", 210, 180, paint8);


    }
    public void initShowYourValue(Context ac,String val, Canvas canva ){

        Bitmap b2= BitmapFactory.decodeResource(ac.getResources(), drawable.bubbleg2);
        Paint paint7 = new Paint();
        canva.drawBitmap(b2, 460, 25, paint7);

        Paint paint5 = new Paint();
        paint5.setColor(Color.BLACK);
        paint5.setTextSize(20);
        canva.drawText("You", 485, 45, paint5);
        canva.drawText(val, 485, 66, paint5);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        drawingImageView = (ImageView) this.findViewById(id.drawingImageView);
        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);

//        initPaintStartConer(canvas);
//
//        initPaintSectionOne(canvas);
//
//        initPaintSectionTwo(canvas);
//
//        initPaintStartConer(canvas);
//
//        initPaintEndConer(canvas);
//
//        initPaintSectionThree(canvas);
//
//        initTopBubbleGreen(canvas);
//
//        initBottomBubbleGreen(canvas);


    }
}
