package com.ayubo.life.ayubolife.lifeplus;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;
    private final Integer textColor;

    public CustomTypefaceSpan(String family, Typeface type, Integer color) {
        super(family);
        newType = type;
        textColor = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType, textColor);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType, textColor);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf, Integer color) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
            paint.setColor(color);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
            paint.setColor(color);
        }

        paint.setTypeface(tf);
    }
}
