package com.ayubo.life.ayubolife.revamp.v1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.revamp.v1.common.CommonFunction;
import com.ayubo.life.ayubolife.revamp.v1.model.DashboardCardItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class V1DashboardMainCardSliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<DashboardCardItem> theSlideItemsModelClassList;
    private V1DashboardMainCardSliderAdapter.OnUpComingItemClickListener upComingListener;

    public void setOnUpComingItemClickListener(V1DashboardMainCardSliderAdapter.OnUpComingItemClickListener listener) {
        this.upComingListener = listener;
    }

    public V1DashboardMainCardSliderAdapter(Context context, ArrayList<DashboardCardItem> dashboardCardItemArrayList) {
        this.context = context;
        this.theSlideItemsModelClassList = dashboardCardItemArrayList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        DashboardCardItem card = this.theSlideItemsModelClassList.get(position);


        if (card.getType().equals("challengev1_active")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_challenge_v1_active_card, null);

            System.out.println(card);
            TextView subheading_right = sliderLayout.findViewById(R.id.subheading_right);
            TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
            TextView circle_left_center_text = sliderLayout.findViewById(R.id.circle_left_center_text);
            TextView circle_left_sub_text = sliderLayout.findViewById(R.id.circle_left_sub_text);
            TextView circle_right_center_text = sliderLayout.findViewById(R.id.circle_right_center_text);
            TextView circle_right_sub_text = sliderLayout.findViewById(R.id.circle_right_sub_text);
            CardView footer_desc_card = sliderLayout.findViewById(R.id.footer_desc_card);
            ImageView circle_left_icon_url = sliderLayout.findViewById(R.id.circle_left_icon_url);
            ImageView circle_right_icon_url = sliderLayout.findViewById(R.id.circle_right_icon_url);
            ProgressBar circularProgressBarDailyTarget = sliderLayout.findViewById(R.id.circularProgressBarDailyTarget);
            ProgressBar circularProgressBarWeeklyTarget = sliderLayout.findViewById(R.id.circularProgressBarWeeklyTarget);


            ImageView card_background_image = sliderLayout.findViewById(R.id.card_background_image);


            if (card.getCard_bground() != null && !card.getCard_bground().equals("")) {
                card_background_image.setVisibility(View.VISIBLE);
                Glide.with(context).load(card.getCard_bground()).centerCrop().into(card_background_image);
            } else {
                card_background_image.setVisibility(View.VISIBLE);
                int startColor = Color.parseColor(card.getCardbgcolor_gradient_top());
                int endColor = Color.parseColor(card.getCardbgcolor_gradient_bottom());
                GradientDrawable gdCommon = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );
                gdCommon.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                card_background_image.setBackgroundDrawable(gdCommon);
            }


            subheading_right.setText(card.getSubheading_right());

            if (!card.getSubheading_right_text_color().equals(""))
                subheading_right.setTextColor(Color.parseColor(card.getSubheading_right_text_color()));

            if (!card.getFooter_desc_color().equals(""))
                footer_desc_card.setCardBackgroundColor(Color.parseColor(card.getFooter_desc_color()));

            footer_desc.setText(card.getFooter_desc());

            if (!card.getFooter_desc_text_color().equals(""))
                footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));


            Glide.with(context).load(card.getCircle_left().getIcon_url()).into(circle_left_icon_url);
            Glide.with(context).load(card.getCircle_right().getIcon_url()).into(circle_right_icon_url);

            circle_left_center_text.setText(card.getCircle_left().getCenter_text());
            circle_right_center_text.setText(card.getCircle_right().getCenter_text());

            circle_left_sub_text.setText(card.getCircle_left().getSub_text());
            circle_right_sub_text.setText(card.getCircle_right().getSub_text());


            // set left card
            LayerDrawable progressBarDrawable = (LayerDrawable) circularProgressBarDailyTarget.getProgressDrawable();
            Drawable progressDrawable = progressBarDrawable.getDrawable(1);
            if (!card.getCircle_left().getFill_color().equals("")) {
                progressDrawable.setColorFilter(Color.parseColor(card.getCircle_left().getFill_color()), PorterDuff.Mode.SRC_IN);
            } else {
                progressDrawable.setColorFilter(context.getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
            }
            circularProgressBarDailyTarget.setProgress(0);
            new CommonFunction().startAnimationCounter(circularProgressBarDailyTarget, 0, card.getCircle_left().getPercentage());


            // set right card
            LayerDrawable circularProgressBarWeeklyTargetLayerDrawable = (LayerDrawable) circularProgressBarWeeklyTarget.getProgressDrawable();
            Drawable circularProgressBarWeeklyTargetDrawable = circularProgressBarWeeklyTargetLayerDrawable.getDrawable(1);
            if (card.getCircle_right().getFill_color() != null && !card.getCircle_right().getFill_color().equals("")) {
                circularProgressBarWeeklyTargetDrawable.setColorFilter(Color.parseColor(card.getCircle_right().getFill_color()), PorterDuff.Mode.SRC_IN);
            } else {
                circularProgressBarWeeklyTargetDrawable.setColorFilter(context.getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
            }
            circularProgressBarWeeklyTarget.setProgress(0);
            new CommonFunction().startAnimationCounter(circularProgressBarWeeklyTarget, 0, card.getCircle_right().getPercentage());

            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;
        } else if (card.getType().equals("challengev1_inactive")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_challenge_v1_inactive_card, null);
            System.out.println(card);

            CardView footer_text_card = sliderLayout.findViewById(R.id.footer_text_card);
            ImageView card_center_image_url = sliderLayout.findViewById(R.id.card_center_image_url);
            TextView subheading_right = sliderLayout.findViewById(R.id.subheading_right);
            TextView circle_left_center_text = sliderLayout.findViewById(R.id.circle_left_center_text);
            TextView circle_left_sub_text = sliderLayout.findViewById(R.id.circle_left_sub_text);
            TextView circle_right_center_text = sliderLayout.findViewById(R.id.circle_right_center_text);
            TextView circle_right_sub_text = sliderLayout.findViewById(R.id.circle_right_sub_text);


            ImageView card_background_image = sliderLayout.findViewById(R.id.card_background_image);
            if (card.getCard_bground() != null && !card.getCard_bground().equals("")) {
                card_background_image.setVisibility(View.VISIBLE);
                Glide.with(context).load(card.getCard_bground()).centerCrop().into(card_background_image);
            } else {
                card_background_image.setVisibility(View.VISIBLE);
                int startColor = Color.parseColor(card.getCardbgcolor_gradient_top());
                int endColor = Color.parseColor(card.getCardbgcolor_gradient_bottom());
                GradientDrawable gdCommon = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );
                gdCommon.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                card_background_image.setBackgroundDrawable(gdCommon);
            }

            if (!card.getFooter_text().equals("")) {
                footer_text_card.setVisibility(View.VISIBLE);
            }


            if (!card.getFooter_text_bg_color().equals(""))
                footer_text_card.setCardBackgroundColor(Color.parseColor(card.getFooter_text_bg_color()));


            circle_right_sub_text.setText(card.getCircle_right().getSub_text());
            circle_right_center_text.setText(card.getCircle_right().getCenter_text());
            circle_left_sub_text.setText(card.getCircle_left().getSub_text());
            circle_left_center_text.setText(card.getCircle_left().getCenter_text());


            subheading_right.setText(card.getSubheading_right());

            if (!card.getSubheading_right_text_color().equals(""))
                subheading_right.setTextColor(Color.parseColor(card.getSubheading_right_text_color()));

            Glide.with(context).load(card.getCard_center_image_url()).into(card_center_image_url);


            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;

        } else if (card.getType().equals("challengev2")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_challenge_v2_card, null);
            TextView center_main_text = sliderLayout.findViewById(R.id.center_main_text);
            TextView center_sub_text = sliderLayout.findViewById(R.id.center_sub_text);
            TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
            center_main_text.setText(card.getCenter_main_text());


            ImageView card_background_image = sliderLayout.findViewById(R.id.card_background_image);
            if (card.getCard_bground() != null && !card.getCard_bground().equals("")) {
                card_background_image.setVisibility(View.VISIBLE);
                Glide.with(context).load(card.getCard_bground()).centerCrop().into(card_background_image);
            } else {
                card_background_image.setVisibility(View.VISIBLE);
                int startColor = Color.parseColor(card.getCardbgcolor_gradient_top());
                int endColor = Color.parseColor(card.getCardbgcolor_gradient_bottom());
                GradientDrawable gdCommon = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );
                gdCommon.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                card_background_image.setBackgroundDrawable(gdCommon);
            }

            if (!card.getCenter_main_text_color().equals(""))
                center_main_text.setTextColor(Color.parseColor(card.getCenter_main_text_color()));

            center_sub_text.setText(card.getCenter_sub_text());
            if (!card.getCenter_sub_text_color().equals(""))
                center_sub_text.setTextColor(Color.parseColor(card.getCenter_sub_text_color()));

            footer_desc.setText(card.getFooter_desc());
            if (!card.getFooter_desc_text_color().equals(""))
                footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));
            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;

        } else if (card.getType().equals("general")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_general_card, null);
            TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
            TextView center_main_text = sliderLayout.findViewById(R.id.center_main_text);
            TextView center_sub_text = sliderLayout.findViewById(R.id.center_sub_text);


            ImageView card_background_image = sliderLayout.findViewById(R.id.card_background_image);
            if (card.getCard_bground() != null && !card.getCard_bground().equals("")) {
                card_background_image.setVisibility(View.VISIBLE);
                Glide.with(context).load(card.getCard_bground()).centerCrop().into(card_background_image);
            } else {
                card_background_image.setVisibility(View.VISIBLE);
                int startColor = Color.parseColor(card.getCardbgcolor_gradient_top());
                int endColor = Color.parseColor(card.getCardbgcolor_gradient_bottom());
                GradientDrawable gdCommon = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );
                gdCommon.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                card_background_image.setBackgroundDrawable(gdCommon);
            }

            center_main_text.setText(card.getCenter_main_text());
            if (!card.getCenter_main_text_color().equals(""))
                center_main_text.setTextColor(Color.parseColor(card.getCenter_main_text_color()));

            center_sub_text.setText(card.getCenter_sub_text());
            if (!card.getCenter_sub_text_color().equals(""))
                center_sub_text.setTextColor(Color.parseColor(card.getCenter_sub_text_color()));

            footer_desc.setText(card.getFooter_desc());
            if (!card.getFooter_desc_text_color().equals(""))
                footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));
            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;

        } else if (card.getType().equals("general_center_image")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_general_center_image_card, null);
            ImageView body_image = sliderLayout.findViewById(R.id.body_image);
            TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
            TextView footer_text = sliderLayout.findViewById(R.id.footer_text);
            TextView center_text_main = sliderLayout.findViewById(R.id.center_text_main);
            LinearLayout row4 = sliderLayout.findViewById(R.id.row4);
//            LinearLayout row3 = sliderLayout.findViewById(R.id.row3);

//            if (!card.getSubheading().equals("")) {
//                row3.setVisibility(View.VISIBLE);
//            }

            if (!card.getCenter_main_text().equals("")) {
                row4.setVisibility(View.VISIBLE);
                center_text_main.setText(card.getCenter_main_text());

                if (!card.getCenter_main_text_color().equals(""))
                    center_text_main.setTextColor(Color.parseColor(card.getCenter_main_text_color()));
            }

            Glide.with(context).load(card.getFooter_icon()).into(body_image);


            if (!card.getFooter_desc().equals("")) {
                footer_desc.setText(card.getFooter_desc() + " ");
                if (!card.getFooter_desc_text_color().equals(""))
                    footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));
            }

            if (!card.getFooter_text().equals("")) {
                footer_text.setVisibility(View.VISIBLE);
            }


            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;

        } else if (card.getType().equals("more")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_more_card, null);
            CardView card_click = sliderLayout.findViewById(R.id.card_click);
            TextView center_main_text = sliderLayout.findViewById(R.id.center_main_text);
            center_main_text.setText(card.getCenter_main_text());
            if (!card.getCardbgcolor_gradient_bottom().equals(""))
                card_click.setCardBackgroundColor(Color.parseColor(card.getCardbgcolor_gradient_bottom()));
            card_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upComingListener.upComingItemClick(theSlideItemsModelClassList, card, card.getCard_click().getAction(), card.getCard_click().getMeta());
                }
            });
            container.addView(sliderLayout);
            return sliderLayout;

        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_general_card, null);
            TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
            footer_desc.setText(card.getFooter_desc());
            if (!card.getFooter_desc_text_color().equals(""))
                footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));
            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;
        }


    }


    public void setUIData(View sliderLayout, DashboardCardItem card) {
        CardView card_click = sliderLayout.findViewById(R.id.card_click);
        TextView summary = sliderLayout.findViewById(R.id.summary);
        TextView title = sliderLayout.findViewById(R.id.title);
        TextView subheading = sliderLayout.findViewById(R.id.subheading);
        TextView footer_text = sliderLayout.findViewById(R.id.footer_text);
        ImageView card_icon_url = sliderLayout.findViewById(R.id.card_icon_url);
        CardView corner_click_card = sliderLayout.findViewById(R.id.corner_click_card);
        ImageView corner_click = sliderLayout.findViewById(R.id.corner_click);


        if (!card.getCorner_click().getIcon_url().equals("")) {
            corner_click_card.setCardBackgroundColor(Color.parseColor(card.getCorner_click().getBg_color()));
            corner_click_card.setVisibility(View.VISIBLE);
            Glide.with(context).load(card.getCorner_click().getIcon_url()).into(corner_click);
            corner_click_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (card.getCorner_click().getAction().equals("close_notify")) {
                        theSlideItemsModelClassList.remove(card);
                        System.out.println(theSlideItemsModelClassList);
                    }

                    upComingListener.upComingItemClick(theSlideItemsModelClassList, card, card.getCorner_click().getAction(), card.getCorner_click().getMeta());
                }
            });
        }

        if (!card.getCard_icon_url().equals("")) {
            card_icon_url.setVisibility(View.VISIBLE);
            Glide.with(context).load(card.getCard_icon_url()).into(card_icon_url);
        }


        footer_text.setText(card.getFooter_text() + " ");

        if (!card.getFooter_text_color().equals(""))
            footer_text.setTextColor(Color.parseColor(card.getFooter_text_color()));


//        if (!card.getCardbgcolor_gradient_bottom().equals(""))
//            card_click.setCardBackgroundColor(Color.parseColor(card.getCardbgcolor_gradient_bottom()));

        card_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SplashScreen.firebaseAnalytics != null) {
                    if (card.getType().equals("challengev1_active")) {
                        SplashScreen.firebaseAnalytics.logEvent("UpEvent_WH_Large_Tile", null);
                    } else if (card.getType().equals("challengev2")) {
                        SplashScreen.firebaseAnalytics.logEvent("UpEvent_WH_Small_Tile", null);
                    } else {
                        SplashScreen.firebaseAnalytics.logEvent(card.getCategory(), null);
                    }
                }
                upComingListener.upComingItemClick(theSlideItemsModelClassList, card, card.getCard_click().getAction(), card.getCard_click().getMeta());
            }
        });

        summary.setText(card.getSummary());
        if (!card.getSummary_color().equals(""))
            summary.setTextColor(Color.parseColor(card.getSummary_color()));


        title.setText(card.getTitle());
        if (!card.getTitle_text_color().equals(""))
            title.setTextColor(Color.parseColor(card.getTitle_text_color()));


        if (!card.getSubheading().equals("")) {
            subheading.setVisibility(View.VISIBLE);
        } else {
            subheading.setVisibility(View.GONE);
        }

        subheading.setText(card.getSubheading());
        if (!card.getSubheading_text_color().equals(""))
            subheading.setTextColor(Color.parseColor(card.getSubheading_text_color()));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {

        String type = theSlideItemsModelClassList.get(position).getType();

        if (type.equals("challengev1_active") || type.equals("challengev1_inactive")) {
            return 0.7F;
        } else {
            return 0.35F;
        }

    }

    @Override
    public int getCount() {
        return theSlideItemsModelClassList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface OnUpComingItemClickListener {
        void upComingItemClick(ArrayList<DashboardCardItem> DashboardCardItemArrayList, DashboardCardItem card, String action, String meta);
    }
}