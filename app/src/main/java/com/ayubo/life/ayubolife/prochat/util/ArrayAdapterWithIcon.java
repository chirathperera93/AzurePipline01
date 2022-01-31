package com.ayubo.life.ayubolife.prochat.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

private List<Drawable> images;

public ArrayAdapterWithIcon(Context context, List<String> items, List<Drawable> images) {
    super(context, android.R.layout.select_dialog_item, items);
    this.images = images;
}

public ArrayAdapterWithIcon(Context context, String[] items, Drawable[] images) {
    super(context, android.R.layout.select_dialog_item, items);
    this.images = Arrays.asList(images);
}



@Override
public View getView(int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    TextView textView = (TextView) view.findViewById(android.R.id.text1);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), null, null,null);
    } else {
        textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position),null, null,null);
    }
    textView.setCompoundDrawablePadding(
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
    return view;
}

}