package com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context mContext;

    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        mContext = context;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public CardItem getCardItem(Integer position) {
        return mData.get(position);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.profile_card_adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        TextView membership_card_no = (TextView) view.findViewById(R.id.membership_card_no);
        TextView membership_card_expire_date = (TextView) view.findViewById(R.id.membership_card_expire_date);
        TextView membership_card_name = (TextView) view.findViewById(R.id.membership_card_name);

        ImageView membershipCardLinerLayout = (ImageView) view.findViewById(R.id.profile_card_view);
        ImageView membershipCardMainLogo = (ImageView) view.findViewById(R.id.mainLogo);
        ImageView sponsorLogo = (ImageView) view.findViewById(R.id.sponsorLogo);
        Glide.with(mContext).load(item.getBackgroundImage()).into(membershipCardLinerLayout);
        Glide.with(mContext).load(item.getMainLogo()).into(membershipCardMainLogo);
        Glide.with(mContext).load(item.getSponsorLogo()).into(sponsorLogo);
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
        membership_card_no.setText(item.getCardId());
        membership_card_expire_date.setText(item.getExpireDate());
        membership_card_name.setText(item.getFullName());
    }

}
