package com.ayubo.life.ayubolife.lifeplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

//public class MyCustomMarkView extends MarkerView {
//
//
//    TextView tv;
//    float width;
//
//    float height;
//
//
//    public MyCustomMarkView(Context context) {
//        //Pass in your own layout file
//        super(context, R.layout.custom_marker_view);
//        tv = findViewById(R.id.tvContent);
//    }
//
//    @Override
//    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
//        MPPointF offset = new MPPointF(posX, posY);
//        Chart chart = getChartView();
//        width = getWidth();
//        height = getHeight();
//        offset.x = offset.x - (width / 2);
//        offset.y = offset.y - (height) - 50;
//        return offset;
//    }
//
//
//    @Override
//    public void refreshContent(Entry e, Highlight highlight) {
//        super.refreshContent(e, highlight);
//        Log.i("TAG", "refreshContent: " + e.getX());
//
//        float f = highlight.getY();
//
//
//        tv.setText(String.valueOf(f));
//    }
//
//
//    @Override
//    public void draw(Canvas canvas, float posX, float posY) {
//        //Draw the border brush
//        Paint p = new Paint();
//        p.setColor(Color.parseColor("#Ac72bd"));
//        p.setStrokeWidth(2);
//        // p.setStyle(Paint.Style.FILL_AND_STROKE);
//        p.setStyle(Paint.Style.STROKE);
//
//        //Draw the fill brush
//        Paint p1 = new Paint();
//        p1.setColor(Color.WHITE);
//        p1.setStyle(Paint.Style.FILL);
//        p1.setStrokeWidth(0.5f);
//        //Call the getOffsetForDrawingAtPoint() method to get the offset coordinates
//        MPPointF f = getOffsetForDrawingAtPoint(posX, posY);
//        //With arrow style
//        canvas.drawLine(f.x, f.y, f.x, f.y + height, p);
//        canvas.drawLine(f.x + width, f.y, f.x + width, f.y + height, p);
//        canvas.drawLine(f.x, f.y, f.x + width, f.y, p);
//        canvas.drawLine(f.x, f.y + height, f.x + (width / 2) - 15, f.y + height, p);
//        canvas.drawLine(f.x + (width / 2) + 15, f.y + height, f.x + width, f.y + height, p);
//        //arrow
//        canvas.drawLine(f.x + (width / 2) - 15, f.y + height, f.x + (width / 2), f.y + height + 30, p);
//        canvas.drawLine(f.x + (width / 2) + 15, f.y + height, f.x + (width / 2), f.y + height + 30, p);
//        //Ordinary box style
//        // canvas.drawArc();
//        //  RectF rectF=new RectF(f.x,f.y,f.x+width,f.y+height);
//        // canvas.drawRoundRect(rectF,10,10,p1);
//        // canvas.drawRoundRect(rectF,10,10,p);
//
//        canvas.translate(f.x, f.y);
//
//        draw(canvas);
//    }
//}


@SuppressLint("ViewConstructor")
public class MyCustomMarkView extends MarkerView {

    private final TextView tvContent;

    public MyCustomMarkView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}