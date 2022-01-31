package com.ayubo.life.ayubolife.map_challenges;

/**
 * Created by appdev on 2/21/2018.
 */

//public class BadgesGridAdapter  {
//}

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.Achievement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class BadgesGridAdapter extends BaseAdapter implements View.OnClickListener {
    ArrayList<Achievement> achievement = null;
    private final Context mContext;
    //private final Achievement books;

    public BadgesGridAdapter(Context context, ArrayList<Achievement> achievement) {
        this.mContext = context;
        this.achievement = achievement;
    }

    @Override
    public int getCount() {
        return achievement.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Achievement book = achievement.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.data_item, null);

            ImageView imageViewCoverArt = (ImageView) convertView.findViewById(R.id.data_item_image);
            final TextView nameTextView = (TextView) convertView.findViewById(R.id.data_item_text);
            //  final TextView data_item_times = (TextView)convertView.findViewById(R.id.data_item_times);

            final ViewHolder viewHolder = new ViewHolder(nameTextView, imageViewCoverArt);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (book.getCount().equals("0")) {
            viewHolder.nameTextView.setText(book.getName());
        } else if (book.getCount().equals("1")) {
            viewHolder.nameTextView.setText(book.getCount() + " Time");
        } else {
            viewHolder.nameTextView.setText(book.getCount() + " Times");
        }


        // viewHolder.imageViewFavorite.setImageResource(book.getIsFavorite() ? R.drawable.star_enabled : R.drawable.star_disabled);
        //   viewHolder.imageViewFavorite.setOnClickListener();
        viewHolder.imageViewFavorite.setTag(position);
        viewHolder.imageViewFavorite.setOnClickListener(this);


        Picasso.with(mContext).load(book.getImage()).into(viewHolder.imageViewFavorite);


        return convertView;
    }

    @Override
    public void onClick(View view) {
        String pos = view.getTag().toString();

        Achievement ach = achievement.get(Integer.parseInt(pos));
        String status = ach.getStatus();

        String image = ach.getImage();
        String name = ach.getName();
        String last_date = ach.getLast_achive_date();
        String desc = ach.getDescription();
        String share_image = ach.getShare_image();

        Intent in = new Intent(mContext, BadgesDetails_Activity.class);
        in.putExtra("image", image);
        in.putExtra("name", name);
        in.putExtra("last_date", last_date);
        in.putExtra("desc", desc);
        in.putExtra("status", status);
        in.putExtra("share_image", share_image);
        view.getContext().startActivity(in);


    }

    private class ViewHolder {
        private final ImageView imageViewFavorite;
        private final TextView nameTextView;


        public ViewHolder(TextView nameTextView, ImageView imageViewFavorite) {
            this.nameTextView = nameTextView;

            this.imageViewFavorite = imageViewFavorite;
        }
    }

}
