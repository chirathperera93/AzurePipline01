package com.ayubo.life.ayubolife.programs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

public class BarChart extends AppCompatActivity {
    ImageView drawingImageView;

    AbsoluteLayout layout2;
    LinearLayout layout;
   Context context=null;
   public BarChart(){

   }
   public BarChart(Context c){
       context=c;
   }

    public void initPaintEndConer(String endConerColor, Canvas canva,int width,int densityDpi){
        Paint paintEndConer = new Paint();
        if(endConerColor.equals("red")){
            paintEndConer.setColor(Color.RED);
        }
        if(endConerColor.equals("orange")){
            paintEndConer.setColor(Color.parseColor("#ff912a"));
        }
        if(endConerColor.equals("green")){
            paintEndConer.setColor(Color.parseColor("#53db7f"));
        }

        canva.drawCircle(width-10, densityDpi*52, 10, paintEndConer);

    }

    public void initPaintStartConer(String startConerColor,Canvas canva,int densityDpi){
        Paint paintStartConer = new Paint();
        if(startConerColor.equals("red")){
            paintStartConer.setColor(Color.RED);
        }
        if(startConerColor.equals("orange")){
            paintStartConer.setColor(Color.parseColor("#ff912a"));
        }
        if(startConerColor.equals("green")){
            paintStartConer.setColor(Color.parseColor("#53db7f"));
        }

        canva.drawCircle(10, densityDpi*52, 10, paintStartConer);
    }

    public void drawMainBarInRed(Canvas canva,int width,int densityDpi){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        int startx = 10;
        int endx = width-10;
        int starty = densityDpi*52;
        int endy = densityDpi*52;
        canva.drawLine(startx, starty, endx, endy, paint);
    }
    public void initPaintSectionTwo(Double starttX,Double enddX,Canvas canva,int densityDpi){
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ff912a"));
        paint2.setStrokeWidth(20);

        int startX= starttX.intValue();
        int endX= enddX.intValue();

        int startY = densityDpi*52;
        int endY = densityDpi*52;
        canva.drawLine(startX, startY, endX, endY, paint2);
    }
    public void initPaintSectionOne(Double start,Double enddX,Canvas canva,int densityDpi){
        Paint paint3 = new Paint();
        paint3.setColor(Color.parseColor("#53db7f"));
        paint3.setStrokeWidth(20);

        int startX = 0;
        int endX = 0;

        int startY = densityDpi*52;
        int endY = densityDpi*52;

        startX= start.intValue();
        endX= enddX.intValue();
        canva.drawLine(startX, startY, endX, endY, paint3);
    }


    public void initBottomBubbleGreen(Context ac, String target, Canvas canva,float Xvalue ,int densityDpi){

        LayoutInflater li = (LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert li != null;
        View v = li.inflate(R.layout.program_dashboard_chart_bubble, null);
        TextView txt_values=v.findViewById(R.id.txt_values);
        txt_values.setText(target);
        v.measure(View.MeasureSpec.getSize(v.getMeasuredWidth()), View.MeasureSpec.getSize(v.getMeasuredHeight()));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canva.save();
        int xLocation=((int)Xvalue)-25;
        canva.translate(xLocation, densityDpi*62);
        v.draw(canva);
        canva.restore();
    }
    public void drawYourValue(Context ac,String val, Canvas canva,float Xvalue ,int densityDpi){

        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert li != null;
        View v = li.inflate(R.layout.program_dashboard_chart_bubble_yellow, null);
        TextView txt_values=v.findViewById(R.id.txt_values);
        txt_values.setText(val);
        v.measure(View.MeasureSpec.getSize(v.getMeasuredWidth()), View.MeasureSpec.getSize(v.getMeasuredHeight()));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canva.save();
        int xLocation=((int)Xvalue)-25;
        canva.translate(xLocation, 0);
        v.draw(canva);
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
