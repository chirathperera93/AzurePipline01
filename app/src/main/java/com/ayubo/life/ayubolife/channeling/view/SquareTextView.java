package com.ayubo.life.ayubolife.channeling.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class SquareTextView extends AppCompatTextView {

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        setMeasuredDimension(width, height);
//        else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
    }
}
