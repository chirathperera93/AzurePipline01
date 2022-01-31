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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.revamp.v1.common.CommonFunction;
import com.ayubo.life.ayubolife.revamp.v1.model.V1DashboardWidgetCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class V1DashboardMainCardSlider2Adapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<V1DashboardWidgetCard> v1DashboardWidgetCards;
    private V1DashboardMainCardSlider2Adapter.OnCardSet2ItemClickListener listener;

    public void setOnItemClickListener(V1DashboardMainCardSlider2Adapter.OnCardSet2ItemClickListener listener) {
        this.listener = listener;
    }

    public V1DashboardMainCardSlider2Adapter(Context context, ArrayList<V1DashboardWidgetCard> dashboardCardItemArrayList) {
        this.context = context;
        this.v1DashboardWidgetCards = dashboardCardItemArrayList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        V1DashboardWidgetCard card = this.v1DashboardWidgetCards.get(position);
        if (card.getType().equals("steps")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_step_tracker_card, null);
            ProgressBar stepTracker = sliderLayout.findViewById(R.id.stepTracker);
            TextView bottom_left_1_text = sliderLayout.findViewById(R.id.bottom_left_1_text);
            TextView bottom_right_1_text = sliderLayout.findViewById(R.id.bottom_right_1_text);
            TextView bottom_left_2_text = sliderLayout.findViewById(R.id.bottom_left_2_text);
            TextView bottom_right_2_text = sliderLayout.findViewById(R.id.bottom_right_2_text);
            TextView circle_center_text = sliderLayout.findViewById(R.id.circle_center_text);
            TextView circle_sub_text = sliderLayout.findViewById(R.id.circle_sub_text);
            ImageView circle_icon_url = sliderLayout.findViewById(R.id.circle_icon_url);

            bottom_left_1_text.setText(card.getBottom_left_1_text());
            if (!card.getBottom_left_1_color().equals("")) {
                bottom_left_1_text.setTextColor(Color.parseColor(card.getBottom_left_1_color()));
            }

            bottom_right_1_text.setText(card.getBottom_right_1_text());
            if (!card.getBottom_right_1_color().equals("")) {
                bottom_left_1_text.setTextColor(Color.parseColor(card.getBottom_right_1_color()));
            }

            bottom_left_2_text.setText(card.getBottom_left_2_text());
            if (!card.getBottom_left_2_color().equals("")) {
                bottom_left_1_text.setTextColor(Color.parseColor(card.getBottom_left_2_color()));
            }

            bottom_right_2_text.setText(card.getBottom_right_2_text());
            if (!card.getBottom_right_2_color().equals("")) {
                bottom_right_2_text.setTextColor(Color.parseColor(card.getBottom_right_2_color()));
            }

            Glide.with(context).load(card.getCircle().getIcon_url()).into(circle_icon_url);
            circle_center_text.setText(card.getCircle().getCenter_text());
            circle_sub_text.setText(card.getCircle().getSub_text());

//            stepTracker.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onCardSet2ItemClickListener("step_details", "");
//                }
//            });

            LayerDrawable progressBarDrawable = (LayerDrawable) stepTracker.getProgressDrawable();
            Drawable progressDrawable = progressBarDrawable.getDrawable(1);
            if (!card.getCircle().getFill_color().equals("")) {
                progressDrawable.setColorFilter(Color.parseColor(card.getCircle().getFill_color()), PorterDuff.Mode.SRC_IN);
            } else {
                progressDrawable.setColorFilter(context.getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
            }
            stepTracker.setProgress(0);
            new CommonFunction().startAnimationCounter(stepTracker, 0, card.getCircle().getPercentage());
            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;
        } else if (card.getType().equals("weight")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_weight_tracker_card, null);
            ImageView card_center_image_url = sliderLayout.findViewById(R.id.card_center_image_url);
            ImageView bottom_right_icon_url = sliderLayout.findViewById(R.id.bottom_right_icon_url);
            TextView bottom_left_1_text = sliderLayout.findViewById(R.id.bottom_left_1_text);
            TextView center_main_text = sliderLayout.findViewById(R.id.center_main_text);


            Glide.with(context).load(card.getCard_center_image_url()).into(card_center_image_url);
            Glide.with(context).load(card.getBottom_right_icon_url()).into(bottom_right_icon_url);

            bottom_right_icon_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardSet2ItemClickListener(card.getBottom_action(), card.getBottom_meta());
                }
            });

            bottom_left_1_text.setText(card.getBottom_left_1_text());
            if (!card.getBottom_left_1_color().equals("")) {
                bottom_left_1_text.setTextColor(Color.parseColor(card.getBottom_left_1_color()));
            }

            center_main_text.setText(card.getCenter_main_text());
            if (!card.getCenter_main_text_color().equals("")) {
                center_main_text.setTextColor(Color.parseColor(card.getCenter_main_text_color()));
            }

            setUIData(sliderLayout, card);
            container.addView(sliderLayout);
            return sliderLayout;
        }


//        else if (card.getType().equals("connect_google_fit")) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View sliderLayout = inflater.inflate(R.layout.v1_connect_google_fit_card, null);
//            Button mainGoogleFitConnectButton = sliderLayout.findViewById(R.id.mainGoogleFitConnectButton);
//            TextView googleFitTextView = sliderLayout.findViewById(R.id.googleFitTextView);
//
//            if (this.isConnectedFit) {
//                mainGoogleFitConnectButton.setText("Connected");
//                googleFitTextView.setText("Connected with google fit");
//            } else {
//                mainGoogleFitConnectButton.setText("Connect Now");
//                googleFitTextView.setText("You need to Connect Google Fit to Get step updates");
//            }
//
//            mainGoogleFitConnectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onCardSet2ItemClickListener("connect_google_fit", "");
//                }
//            });
//
//            container.addView(sliderLayout);
//            return sliderLayout;
//        }


        else if (card.getType().equals("more")) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_add_more_card, null);

            RelativeLayout mainAddMoreCard = sliderLayout.findViewById(R.id.mainAddMoreCard);
            ImageView card_center_image_url = sliderLayout.findViewById(R.id.card_center_image_url);
            TextView center_main_text = sliderLayout.findViewById(R.id.center_main_text);


            Glide.with(context).load(card.getCard_center_image_url()).into(card_center_image_url);

            center_main_text.setText(card.getCenter_main_text());
            if (!card.getCenter_main_text_color().equals("")) {
                center_main_text.setTextColor(Color.parseColor(card.getCenter_main_text_color()));
            }

            int gColorValue1 = Color.parseColor(card.getCardbgcolor_gradient_top());
            int gColorValue2 = Color.parseColor(card.getCardbgcolor_gradient_bottom());

            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{gColorValue1, gColorValue2}
            );
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            mainAddMoreCard.setBackgroundDrawable(gradientDrawable);

            mainAddMoreCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardSet2ItemClickListener(card.getCard_click().getAction(), card.getCard_click().getMeta());
                }
            });

            container.addView(sliderLayout);
            return sliderLayout;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sliderLayout = inflater.inflate(R.layout.v1_challenge_v2_card, null);
            container.addView(sliderLayout);
            return sliderLayout;
        }
    }

    private void setUIData(View sliderLayout, V1DashboardWidgetCard card) {
        TextView title = sliderLayout.findViewById(R.id.title);
        TextView footer_text = sliderLayout.findViewById(R.id.footer_text);
        TextView footer_desc = sliderLayout.findViewById(R.id.footer_desc);
        RelativeLayout mainCardRelativeLayout = sliderLayout.findViewById(R.id.mainCardRelativeLayout);


        int gColorValue1 = Color.parseColor(card.getCardbgcolor_gradient_top());
        int gColorValue2 = Color.parseColor(card.getCardbgcolor_gradient_bottom());

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{gColorValue1, gColorValue2}
        );
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mainCardRelativeLayout.setBackgroundDrawable(gradientDrawable);

        title.setText(card.getTitle());
        if (!card.getTitle_text_color().equals("")) {
            title.setTextColor(Color.parseColor(card.getTitle_text_color()));
        }

        footer_text.setText(card.getFooter_text());
        if (!card.getFooter_text_color().equals("")) {
            footer_text.setTextColor(Color.parseColor(card.getFooter_text_color()));
        }

        footer_desc.setText(card.getFooter_desc());
        if (!card.getFooter_desc_text_color().equals("")) {
            footer_desc.setTextColor(Color.parseColor(card.getFooter_desc_text_color()));
        }

        mainCardRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SplashScreen.firebaseAnalytics != null) {
                    if (card.getType().equals("steps")) {
                        SplashScreen.firebaseAnalytics.logEvent("Widget_Steps", null);
                    } else if (card.getType().equals("weight")) {
                        SplashScreen.firebaseAnalytics.logEvent("Widget_Weight", null);
                    } else if (card.getType().equals("more")) {
                        SplashScreen.firebaseAnalytics.logEvent("Widget_AddMore", null);
                    } else {
                        SplashScreen.firebaseAnalytics.logEvent(card.getCategory(), null);
                    }
                }


                listener.onCardSet2ItemClickListener(card.getCard_click().getAction(), card.getCard_click().getMeta());
            }
        });
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.42F;
    }

    @Override
    public int getCount() {
        return v1DashboardWidgetCards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface OnCardSet2ItemClickListener {
        void onCardSet2ItemClickListener(String action, String meta);
    }
}