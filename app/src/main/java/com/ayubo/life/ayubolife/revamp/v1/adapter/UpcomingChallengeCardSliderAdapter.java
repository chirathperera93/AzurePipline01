package com.ayubo.life.ayubolife.revamp.v1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.revamp.v1.model.YTDChallenges;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UpcomingChallengeCardSliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<YTDChallenges> theSlideItemsModelClassList2;
    private UpcomingChallengeCardSliderAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(UpcomingChallengeCardSliderAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public UpcomingChallengeCardSliderAdapter(Context context, ArrayList<YTDChallenges> yTDChallengesArrayList) {
        this.context = context;
        this.theSlideItemsModelClassList2 = yTDChallengesArrayList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        YTDChallenges card = this.theSlideItemsModelClassList2.get(position);
        if (card.getType().equals("inprogress")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.in_progress_card, null);

            CardView mainCard = sliderLayout.findViewById(R.id.mainCard);
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(card.getAction(), card.getMeta());
                }
            });

            ImageView imageView = sliderLayout.findViewById(R.id.image);
            Glide.with(context).load(card.getImage()).into(imageView);

            TextView type = sliderLayout.findViewById(R.id.type);
            type.setText(card.getStatus_lable().getText());

            CardView cardViewForType = sliderLayout.findViewById(R.id.cardViewForType);
            cardViewForType.setCardBackgroundColor(Color.parseColor(card.getStatus_lable().getBg_color()));

            TextView title = sliderLayout.findViewById(R.id.title);
            title.setText(card.getTitle());

            TextView counter_1 = sliderLayout.findViewById(R.id.counter_1);
            counter_1.setText(card.getCounter_1().getValue());
            counter_1.setTextColor(Color.parseColor(card.getCounter_1().getColor()));

            TextView target_1 = sliderLayout.findViewById(R.id.target_1);
            target_1.setText("/" + card.getTarget_1());

            TextView lable_1 = sliderLayout.findViewById(R.id.lable_1);
            lable_1.setText(card.getLable_1());

            TextView counter_2 = sliderLayout.findViewById(R.id.counter_2);
            counter_2.setText(card.getCounter_2().getValue());
            counter_2.setTextColor(Color.parseColor(card.getCounter_2().getColor()));

            TextView target_2 = sliderLayout.findViewById(R.id.target_2);
            target_2.setText("/" + card.getTarget_2());

            TextView lable_2 = sliderLayout.findViewById(R.id.lable_2);
            lable_2.setText(card.getLable_2());


            TextView footer_lable_1 = sliderLayout.findViewById(R.id.footer_lable_1);
            footer_lable_1.setText(card.getFooter().getLable_1() + " : ");

            TextView footer_value_1 = sliderLayout.findViewById(R.id.footer_value_1);
            footer_value_1.setText(card.getFooter().getValue_1());

            TextView footer_lable_2 = sliderLayout.findViewById(R.id.footer_lable_2);
            footer_lable_2.setText(card.getFooter().getLable_2() + " : ");

            TextView footer_value_2 = sliderLayout.findViewById(R.id.footer_value_2);
            footer_value_2.setText(card.getFooter().getValue_2());


            container.addView(sliderLayout);
            return sliderLayout;
        } else if (card.getType().equals("finished")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.finished_card, null);

            CardView mainCard = sliderLayout.findViewById(R.id.mainCard);
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(card.getAction(), card.getMeta());
                }
            });

            ImageView imageView = sliderLayout.findViewById(R.id.image);
            Glide.with(context).load(card.getImage()).into(imageView);

            TextView type = sliderLayout.findViewById(R.id.type);
            type.setText(card.getStatus_lable().getText());

            CardView cardViewForType = sliderLayout.findViewById(R.id.cardViewForType);
            cardViewForType.setCardBackgroundColor(Color.parseColor(card.getStatus_lable().getBg_color()));

            TextView title = sliderLayout.findViewById(R.id.title);
            title.setText(card.getTitle());

            TextView counter_1 = sliderLayout.findViewById(R.id.counter_1);
            counter_1.setText(card.getCounter_1().getValue());
            counter_1.setTextColor(Color.parseColor(card.getCounter_1().getColor()));

            TextView target_1 = sliderLayout.findViewById(R.id.target_1);
            target_1.setText("/" + card.getTarget_1());

            TextView lable_1 = sliderLayout.findViewById(R.id.lable_1);
            lable_1.setText(card.getLable_1());

            TextView footer_lable_1 = sliderLayout.findViewById(R.id.footer_lable_1);
            footer_lable_1.setText(card.getFooter().getLable_1() + " : ");

            TextView footer_value_1 = sliderLayout.findViewById(R.id.footer_value_1);
            footer_value_1.setText(card.getFooter().getValue_1());

            TextView footer_lable_2 = sliderLayout.findViewById(R.id.footer_lable_2);
            footer_lable_2.setText(card.getFooter().getLable_2() + " : ");

            TextView footer_value_2 = sliderLayout.findViewById(R.id.footer_value_2);
            footer_value_2.setText(card.getFooter().getValue_2());

            container.addView(sliderLayout);
            return sliderLayout;
        } else if (card.getType().equals("upcoming")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.upcoming_card, null);

            CardView mainCard = sliderLayout.findViewById(R.id.mainCard);
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(card.getAction(), card.getMeta());
                }
            });

            ImageView imageView = sliderLayout.findViewById(R.id.image);
            Glide.with(context).load(card.getImage()).into(imageView);

            TextView type = sliderLayout.findViewById(R.id.type);
            type.setText(card.getStatus_lable().getText());

            CardView cardViewForType = sliderLayout.findViewById(R.id.cardViewForType);
            cardViewForType.setCardBackgroundColor(Color.parseColor(card.getStatus_lable().getBg_color()));

            TextView title = sliderLayout.findViewById(R.id.title);
            title.setText(card.getTitle());

            TextView target_1 = sliderLayout.findViewById(R.id.target_1);
            target_1.setText(card.getTarget_1());

            TextView lable_1 = sliderLayout.findViewById(R.id.lable_1);
            lable_1.setText(card.getLable_1());

            TextView target_2 = sliderLayout.findViewById(R.id.target_2);
            target_2.setText(card.getTarget_2());

            TextView lable_2 = sliderLayout.findViewById(R.id.lable_2);
            lable_2.setText(card.getLable_2());

            TextView footer_lable_1 = sliderLayout.findViewById(R.id.footer_lable_1);
            footer_lable_1.setText(card.getFooter().getLable_1() + " : ");

            TextView footer_value_1 = sliderLayout.findViewById(R.id.footer_value_1);
            footer_value_1.setText(card.getFooter().getValue_1());

            TextView footer_lable_2 = sliderLayout.findViewById(R.id.footer_lable_2);
            footer_lable_2.setText(card.getFooter().getLable_2() + " : ");

            TextView footer_value_2 = sliderLayout.findViewById(R.id.footer_value_2);
            footer_value_2.setText(card.getFooter().getValue_2());

            container.addView(sliderLayout);
            return sliderLayout;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.upcoming_card, null);

            CardView mainCard = sliderLayout.findViewById(R.id.mainCard);
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(card.getAction(), card.getMeta());
                }
            });

            ImageView imageView = sliderLayout.findViewById(R.id.image);
            Glide.with(context).load(card.getImage()).into(imageView);

            TextView type = sliderLayout.findViewById(R.id.type);
            type.setText(card.getStatus_lable().getText());

            CardView cardViewForType = sliderLayout.findViewById(R.id.cardViewForType);
            cardViewForType.setCardBackgroundColor(Color.parseColor(card.getStatus_lable().getBg_color()));

            TextView title = sliderLayout.findViewById(R.id.title);
            title.setText(card.getTitle());

            TextView target_1 = sliderLayout.findViewById(R.id.target_1);
            target_1.setText(card.getTarget_1());

            TextView lable_1 = sliderLayout.findViewById(R.id.lable_1);
            lable_1.setText(card.getLable_1());

            TextView target_2 = sliderLayout.findViewById(R.id.target_2);
            target_2.setText(card.getTarget_2());

            TextView lable_2 = sliderLayout.findViewById(R.id.lable_2);
            lable_2.setText(card.getLable_2());

            TextView footer_lable_1 = sliderLayout.findViewById(R.id.footer_lable_1);
            footer_lable_1.setText(card.getFooter().getLable_1() + " : ");

            TextView footer_value_1 = sliderLayout.findViewById(R.id.footer_value_1);
            footer_value_1.setText(card.getFooter().getValue_1());

            TextView footer_lable_2 = sliderLayout.findViewById(R.id.footer_lable_2);
            footer_lable_2.setText(card.getFooter().getLable_2() + " : ");

            TextView footer_value_2 = sliderLayout.findViewById(R.id.footer_value_2);
            footer_value_2.setText(card.getFooter().getValue_2());

            container.addView(sliderLayout);
            return sliderLayout;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.5F;
    }

    @Override
    public int getCount() {
        return theSlideItemsModelClassList2.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface OnItemClickListener {
        void onItemClick(String action, String meta);
    }
}
