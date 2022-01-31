package com.ayubo.life.ayubolife.autocomplete;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DBString;

import java.util.List;

public class LocationListAdapter extends ArrayAdapter<DBString> {
    private final Context context;
    private final List<DBString> values;

    public LocationListAdapter(Context context,List<DBString> locationList) {
        super(context, R.layout.locationlistitem, locationList);
        this.context = context;
        this.values = locationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.locationlistitem, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_content);

        DBString sdsd=values.get(position);
        textView.setText(sdsd.getName());

        return rowView;
    }
}